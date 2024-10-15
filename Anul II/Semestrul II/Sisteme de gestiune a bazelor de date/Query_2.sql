USE MyGlamping;
GO

-- Read Transactions --

-- Dirty Reads --
--SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED -- dirty reads
SET TRANSACTION ISOLATION LEVEL READ COMMITTED -- dirty reads solution
BEGIN TRAN;
SELECT * FROM Clients WHERE idCLNT = (SELECT MAX(idCLNT) FROM Clients);
WAITFOR DELAY '00:00:05';
SELECT * FROM Clients WHERE idCLNT = (SELECT MAX(idCLNT) FROM Clients);
COMMIT TRAN;

-- Unrepeatable Reads--
--SET TRANSACTION ISOLATION LEVEL READ COMMITTED -- unrepeatable reads
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ -- unrepeatable reads solution
BEGIN TRAN;
SELECT * FROM Clients WHERE idCLNT = (SELECT MAX(idCLNT) FROM Clients);
WAITFOR DELAY '00:00:10';
SELECT * FROM Clients WHERE idCLNT = (SELECT MAX(idCLNT) FROM Clients);
COMMIT TRAN;

-- Phantom Reads --
--SET TRANSACTION ISOLATION LEVEL REPEATABLE READ -- phantom reads
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE -- phantom reads solutions
BEGIN TRAN;
SELECT * FROM Clients WHERE idCLNT = (SELECT MAX(idCLNT) FROM Clients);
WAITFOR DELAY '00:00:05';
SELECT * FROM Clients WHERE idCLNT = (SELECT MAX(idCLNT) FROM Clients);
COMMIT TRAN;

-- Deadlock --
GO
CREATE OR ALTER PROC deadlock_transaction2 AS
BEGIN
	--SET DEADLOCK_PRIORITY NORMAL; -- deadlock
	SET DEADLOCK_PRIORITY HIGH; -- solutie deadlock
	BEGIN TRAN;
	-- locking Tents
	UPDATE Tents
	SET Name = 'Deadlock' + CONVERT(VARCHAR(3), ABS(CHECKSUM(NEWID()) % 300) + 1)
	WHERE idT = (SELECT MAX(idT) FROM Tents);
	WAITFOR DELAY '00:00:05';
	-- trying to access resource from Clients
	UPDATE Clients
	SET FirstName = 'Deadlock' + CONVERT(VARCHAR(3), ABS(CHECKSUM(NEWID()) % 300) + 1)
	WHERE idCLNT = (SELECT MAX(idCLNT) FROM Clients);
	COMMIT TRAN;
END;
GO

EXEC deadlock_transaction2;

SELECT * FROM Clients WHERE idCLNT = (SELECT MAX(idCLNT) FROM Clients);
SELECT * FROM Tents WHERE idT = (SELECT MAX(idT) FROM Tents);