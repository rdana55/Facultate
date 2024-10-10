-- Procedura stocată pentru testarea și stocarea rezultatelor unei rulări de test pentru tabele
CREATE PROCEDURE dbo.TestRunTablesProcedure
    @TestRunID INT
AS
BEGIN
    DECLARE @TableName NVARCHAR(50)
    DECLARE @NoOfRows INT
    DECLARE @Position INT

    -- Obține informații despre tabelul asociat testului
    SELECT 
        @TableName = T.Name,
        @NoOfRows = TT.NoOfRows,
        @Position = TT.Position
    FROM Tests AS T
    JOIN TestTables AS TT ON T.TestID = TT.TestID
    WHERE T.TestID = @TestRunID;

    -- 1. Șterge datele din tabelul asociat testului în ordinea dată de câmpul Position
    EXEC('DELETE FROM ' + @TableName);

    -- 2. Inserează înregistrările în tabel în ordinea inversă dată de Position
    DECLARE @Counter INT = @NoOfRows;
    WHILE @Counter > 0
    BEGIN
        INSERT INTO @TableName DEFAULT VALUES;
        SET @Counter = @Counter - 1;
    END;

    -- Adăugați logica de evaluare a timpului de execuție aici

    -- Actualizează tabela TestRunTables cu informații despre performanța inserării în tabel
    INSERT INTO TestRunTables (TestRunID, TableID, StartAt, EndAt)
    VALUES (@TestRunID, (SELECT TableID FROM [Tables] WHERE Name = @TableName), GETDATE(), GETDATE());
END;
GO

-- Procedura stocată pentru testarea și stocarea rezultatelor unei rulări de test pentru view-uri
CREATE PROCEDURE dbo.TestRunViewsProcedure
    @TestRunID INT
AS
BEGIN
    DECLARE @ViewName NVARCHAR(50)

    -- Obține informații despre view-ul asociat testului
    SELECT @ViewName = V.Name
    FROM Tests AS T
    JOIN TestViews AS TV ON T.TestID = TV.TestID
    JOIN Views AS V ON TV.ViewID = V.ViewID
    WHERE T.TestID = @TestRunID;

    -- Adăugați logica de evaluare a timpului de execuție aici

    -- Actualizează tabela TestRunViews cu informații despre performanța execuției view-ului
    INSERT INTO TestRunViews (TestRunID, ViewID, StartAt, EndAt)
    VALUES (@TestRunID, (SELECT ViewID FROM Views WHERE Name = @ViewName), GETDATE(), GETDATE());
END;
GO

-- Procedura stocată pentru execuția completă a unui test
CREATE PROCEDURE dbo.RunTestProcedure
    @TestID INT
AS
BEGIN
    -- Execută testarea și stocarea rezultatelor pentru tabele
    EXEC dbo.TestRunTablesProcedure @TestID;

    -- Execută testarea și stocarea rezultatelor pentru view-uri
    EXEC dbo.TestRunViewsProcedure @TestID;

    -- Alte operații specifice testului...
END;
GO
