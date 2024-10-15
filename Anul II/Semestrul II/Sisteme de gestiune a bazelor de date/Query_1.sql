USE MyGlamping
GO

-- Update Transactions --
DELETE FROM Clients
WHERE idCLNT > 11;

-- Dirty Reads --
BEGIN TRAN;
UPDATE Clients
SET FirstName = 'Dirty' + CONVERT(VARCHAR(3), ABS(CHECKSUM(NEWID()) % 300) + 1)
WHERE idCLNT = (SELECT MAX(idCLNT) FROM Clients);
WAITFOR DELAY '00:00:05';
ROLLBACK TRAN;

-- Unrepeatable Reads --
BEGIN TRAN;
WAITFOR DELAY '00:00:05';
UPDATE Clients
SET FirstName = 'Unrepeatable' + CONVERT(VARCHAR(3), ABS(CHECKSUM(NEWID()) % 300) + 1)
WHERE idCLNT = (SELECT MAX(idCLNT) FROM Clients);
COMMIT TRAN;

-- Phantom Reads --
DELETE FROM Clients WHERE idCLNT = (SELECT MAX(idCLNT) FROM Clients);
SELECT * FROM Clients;

BEGIN TRAN;
WAITFOR DELAY '00:00:05';
INSERT INTO Clients(FirstName) VALUES ('Phantom404');
COMMIT TRAN;

-- Deadlock --
GO
CREATE OR ALTER PROC deadlock_transaction1 AS
BEGIN
	BEGIN TRAN;
	-- locking Clients
	UPDATE Clients
	SET FirstName = 'Deadlock' + CONVERT(VARCHAR(3), ABS(CHECKSUM(NEWID()) % 300) + 1)
	WHERE idCLNT = (SELECT MAX(idCLNT) FROM Clients);
	WAITFOR DELAY '00:00:05';
	-- trying to access resource from Tents
	UPDATE Tents
	SET Name = 'Deadlock' + CONVERT(VARCHAR(3), ABS(CHECKSUM(NEWID()) % 300) + 1)
	WHERE idT = (SELECT MAX(idT) FROM Tents);
	COMMIT TRAN;
END;
GO

