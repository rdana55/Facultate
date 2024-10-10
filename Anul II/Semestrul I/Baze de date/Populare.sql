use MyGlamping

-- Inserare date de test în tabela Tests
INSERT INTO Tests (Name) VALUES ('Test1'), ('Test2'), ('Test3');

-- Inserare date de test în tabela Tables
INSERT INTO Tables (Name) VALUES ('Table1'), ('Table2'), ('Table3');

-- Asociere între teste și tabele în tabela TestTables
INSERT INTO TestTables (TestID, TableID, NoOfRows, Position) VALUES
    (1, 1, 100, 1),
    (2, 2, 50, 2),
    (3, 3, 75, 3);

-- Inserare date de test în tabela Views
INSERT INTO Views (Name) VALUES ('View1'), ('View2'), ('View3');

-- Asociere între teste și view-uri în tabela TestViews
INSERT INTO TestViews (TestID, ViewID) VALUES
    (1, 1),
    (2, 2),
    (3, 3);

-- Inserare date de test în tabela TestRuns
INSERT INTO TestRuns (Description, StartAt, EndAt) VALUES
    ('TestRun1', GETDATE(), GETDATE()),
    ('TestRun2', GETDATE(), GETDATE()),
    ('TestRun3', GETDATE(), GETDATE());

-- Inserare date de test în tabela TestRunTables
INSERT INTO TestRunTables (TestRunID, TableID, StartAt, EndAt) VALUES
    (1, 1, GETDATE(), GETDATE()),
    (2, 2, GETDATE(), GETDATE()),
    (3, 3, GETDATE(), GETDATE());

-- Inserare date de test în tabela TestRunViews
INSERT INTO TestRunViews (TestRunID, ViewID, StartAt, EndAt) VALUES
    (1, 1, GETDATE(), GETDATE()),
    (2, 2, GETDATE(), GETDATE()),
    (3, 3, GETDATE(), GETDATE());

-- Inserare date de test în tabela Glamping și legături în tabelele cu chei străine
INSERT INTO Glamping (Name, Adress, PhoneNumber, Description) VALUES
    ('Glamping1', 'Adresa1', '123456789', 'Descriere1'),
    ('Glamping2', 'Adresa2', '987654321', 'Descriere2'),
    ('Glamping3', 'Adresa3', '456789123', 'Descriere3');

INSERT INTO Admin (idA, Name, Email, Password) VALUES
    (1, 'Admin1', 'admin1@example.com', 'pass1'),
    (2, 'Admin2', 'admin2@example.com', 'pass2'),
    (3, 'Admin3', 'admin3@example.com', 'pass3');

INSERT INTO Costs (idG, Total, Date, Type) VALUES
    (1, 1000, GETDATE(), 'Type1'),
    (2, 2000, GETDATE(), 'Type2'),
    (3, 3000, GETDATE(), 'Type3');

INSERT INTO Cottages (idG, Name, Capacity, Price) VALUES
    (1, 'Cottage1', 2, 150),
    (2, 'Cottage2', 3, 200),
    (3, 'Cottage3', 4, 250);

-- Inserare de date în tabela FreeSpaces
INSERT INTO FreeSpaces (idG, Name, Surface, Price) VALUES
    (1, 'FreeSpace1', 100, 50),
    (2, 'FreeSpace2', 120, 60),
    (3, 'FreeSpace3', 150, 75);

-- Inserare de date în tabela Tents
INSERT INTO Tents (idG, Name, Capacity, Price) VALUES
    (1, 'Tent1', 2, 80),
    (2, 'Tent2', 3, 100),
    (3, 'Tent3', 4, 120);

-- Inserare de date în tabela Clients
INSERT INTO Clients (FirstName, Surname, Email, PhoneNumber, Adress) VALUES
    ('John', 'Doe', 'john.doe@example.com', '123456789', 'Street1'),
    ('Jane', 'Smith', 'jane.smith@example.com', '987654321', 'Street2'),
    ('Bob', 'Johnson', 'bob.johnson@example.com', '456789123', 'Street3');

-- Inserare de date în tabela Bookings
INSERT INTO Bookings (idCTG, idFS, idT, idCLNT, CheckIn, CheckOut) VALUES
    (1, NULL, NULL, 1, '2023-01-01', '2023-01-05'),
    (NULL, 2, NULL, 2, '2023-02-01', '2023-02-10'),
    (NULL, NULL, 3, 3, '2023-03-01', '2023-03-07');

-- Inserare de date în tabela Services
INSERT INTO Services (Type, Availability) VALUES
    ('Service1', 10),
    ('Service2', 5),
    ('Service3', 8);

-- Inserare de date în tabela ServicesBookings
INSERT INTO ServicesBookings (idBS, idSRV, Total, Period) VALUES
    (1, 1, 50, 'Daily'),
    (2, 2, 30, 'Weekly'),
    (3, 3, 40, 'Monthly');

-- Inserare de date în tabela Bills
INSERT INTO Bills (idCLNT, Service, Date, Total) VALUES
    (1, 'Service1', '2023-01-05', 50),
    (2, 'Service2', '2023-02-15', 30),
    (3, 'Service3', '2023-03-10', 40);

-- Inserare de date în tabela Reviews
INSERT INTO Reviews (idCLNT, Description, Rating) VALUES
    (1, 'Great experience!', 5),
    (2, 'Good service!', 4),
    (3, 'Awesome place!', 5);