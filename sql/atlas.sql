-- ======================================================
-- BANCO DE DADOS ATLAS - ESTRUTURA (DDL)
-- ======================================================
CREATE SCHEMA IF NOT EXISTS atlas;
USE atlas;

CREATE TABLE Person (
    cpf VARCHAR(14) NOT NULL,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    gender CHAR(1) NOT NULL,
    birthDate DATE NOT NULL,
    CONSTRAINT pk_person PRIMARY KEY (cpf)
);

CREATE TABLE Client (
    cpf VARCHAR(14),
    address VARCHAR(250),
    startSuspensionDate DATE DEFAULT NULL,
    endSuspensionDate DATE DEFAULT NULL,
    CONSTRAINT pk_client PRIMARY KEY (cpf),
    CONSTRAINT fk_client_person FOREIGN KEY (cpf) REFERENCES Person(cpf)
);

CREATE TABLE Author (
    authorId INT AUTO_INCREMENT,
    authorName VARCHAR(150) NOT NULL,
    CONSTRAINT pk_author PRIMARY KEY (authorId)
);

CREATE TABLE Category (
    categoryId INT AUTO_INCREMENT,
    categoryName VARCHAR(100) NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (categoryId)
);

CREATE TABLE Book (
    bookId INT AUTO_INCREMENT,
    bookName VARCHAR(150) NOT NULL,
    bookLocation VARCHAR(100),
    numberOfPages INT,
    publisher varchar(150),
    CONSTRAINT pk_book PRIMARY KEY (bookId)
);

CREATE TABLE BookCopy (
    bookCopyId INT AUTO_INCREMENT,
    bookId INT NOT NULL,
    statusAvailable BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_bookCopy PRIMARY KEY (bookCopyId),
    CONSTRAINT fk_bookCopy_book FOREIGN KEY (bookId) REFERENCES Book(bookId)
);

CREATE TABLE BookAuthor (
    bookId INT NOT NULL,
    authorId INT NOT NULL,
    CONSTRAINT pk_book_author PRIMARY KEY (bookId, authorId),
    CONSTRAINT fk_ba_book   FOREIGN KEY (bookId)   REFERENCES Book(bookId),
    CONSTRAINT fk_ba_author FOREIGN KEY (authorId) REFERENCES Author(authorId)
);

CREATE TABLE BookCategory (
    bookId INT NOT NULL,
    categoryId INT NOT NULL,
    CONSTRAINT pk_book_category PRIMARY KEY (bookId, categoryId),
    CONSTRAINT fk_bc_book     FOREIGN KEY (bookId)     REFERENCES Book(bookId),
    CONSTRAINT fk_bc_category FOREIGN KEY (categoryId) REFERENCES Category(categoryId)
);

CREATE TABLE Employee (
    cpf VARCHAR(14),
    password INT NOT NULL,
    CONSTRAINT pk_employee PRIMARY KEY (cpf),
    CONSTRAINT fk_employee_person FOREIGN KEY (cpf) REFERENCES Person(cpf)
);

CREATE TABLE Attendant (
    cpf VARCHAR(14),
    CONSTRAINT pk_attendant PRIMARY KEY (cpf),
    CONSTRAINT fk_attendant_employee FOREIGN KEY (cpf) REFERENCES Employee(cpf) ON DELETE CASCADE
);

CREATE TABLE Librarian (
    cpf VARCHAR(14),
    CONSTRAINT pk_librarian PRIMARY KEY (cpf),
    CONSTRAINT fk_librarian_employee FOREIGN KEY (cpf) REFERENCES Employee(cpf) ON DELETE CASCADE
);

CREATE TABLE Administrator (
    cpf VARCHAR(14),
    CONSTRAINT pk_administrator PRIMARY KEY (cpf),
    CONSTRAINT fk_administrator_employee FOREIGN KEY (cpf) REFERENCES Employee(cpf) ON DELETE CASCADE
);

CREATE TABLE Loan (
    loanId INT AUTO_INCREMENT,
    cpf VARCHAR(14) NOT NULL,
    bookCopyId INT NOT NULL,
    renewals INT NOT NULL DEFAULT 0,
    loanDate DATETIME NOT NULL,
    expectedReturnDate DATETIME NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    CONSTRAINT pk_loan PRIMARY KEY (loanId),
    CONSTRAINT fk_loan_client FOREIGN KEY (cpf) REFERENCES Client(cpf),
    CONSTRAINT fk_loan_copy   FOREIGN KEY (bookCopyId) REFERENCES BookCopy(bookCopyId)
);

CREATE TABLE ReturnBook (
    returnId INT AUTO_INCREMENT,
    returnDate DATETIME NOT NULL,
    loanId INT NOT NULL,
    CONSTRAINT pk_return PRIMARY KEY (returnId),
    CONSTRAINT fk_return_loan FOREIGN KEY (loanId) REFERENCES Loan(loanId)
);

CREATE TABLE Renewal (
    renewalId INT PRIMARY KEY AUTO_INCREMENT,
    newReturnDate DATETIME NOT NULL,
    renewalNumber INT DEFAULT 0,
    loanId INT NOT NULL,
    CONSTRAINT fk_renewal_loan FOREIGN KEY (loanId) REFERENCES Loan(loanId)
);


-- ======================================================
-- CARGA INICIAL (DML)
-- ======================================================

-- Admin Chefe
INSERT INTO Person (cpf, name, email, gender, birthDate)
VALUES ('00000000000', 'Admin Chefe', 'admin@atlas.com', 'F', '2000-01-01');

INSERT INTO Employee (cpf, password)
VALUES ('00000000000', 123456);

INSERT INTO Administrator (cpf)
VALUES ('00000000000');

-- Outros Funcionários e Clientes
INSERT INTO Person (cpf, name, email, gender, birthDate) VALUES
('11111111111', 'Ana Beatriz Souza', 'ana.souza@email.com', 'F', '2001-04-15'),
('22222222222', 'Carlos Henrique Lima', 'carlos.lima@email.com', 'M', '1999-09-21'),
('33333333332', 'Mariana Alves Costa', 'mariana.costa@email.com', 'F', '2003-01-10'),
('44444444444', 'Joao Pedro Martins', 'joao.martins@email.com', 'M', '1995-06-18'),
('55555555555', 'Fernanda Rocha', 'fernanda.rocha@email.com', 'F', '1992-11-03'),
('66666666666', 'Ricardo Mendes', 'ricardo.mendes@email.com', 'M', '1988-08-27'),
('77777777777', 'Juliana Ferreira', 'juliana.ferreira@email.com', 'F', '1990-02-14'),
('33333333333', 'Mariana Alves Costa', 'mariana.costa@email.com', 'F', '2003-01-10'),
('88888888888', 'Pedro Henrique Silva', 'pedro.silva@email.com', 'M', '1998-03-22'),
('99999999999', 'Lucia Fernanda Reis',  'lucia.reis@email.com',   'F', '2000-07-05');

-- Vinculando Cargos
INSERT INTO Client (cpf, address) VALUES ('11111111111', 'Rua das Flores, 120'), ('22222222222', 'Av. Central, 450');
INSERT INTO Employee (cpf, password) VALUES ('44444444444', 1234), ('55555555555', 5678), ('66666666666', 4321), ('77777777777', 8765);
INSERT INTO Attendant (cpf) VALUES ('44444444444'), ('55555555555');
INSERT INTO Librarian (cpf) VALUES ('66666666666'), ('77777777777');

-- Autores e Categorias
INSERT INTO Author (authorName) VALUES ('Machado de Assis'), ('Clarice Lispector'), ('J. K. Rowling'), ('George Orwell');
INSERT INTO Category (categoryName) VALUES ('Romance'), ('Fantasia'), ('Ficcao Cientifica'), ('Drama');

-- Livros e Cópias
INSERT INTO Book (bookName, bookLocation, numberOfPages, publisher) VALUES ('Dom Casmurro', 'A1', 256, 'Editora Globo');
INSERT INTO BookAuthor (bookId, authorId) VALUES (1, 1);
INSERT INTO BookCategory (bookId, categoryId) VALUES (1, 1);
INSERT INTO BookCopy (bookId, statusAvailable) VALUES (1, TRUE), (1, FALSE);

INSERT INTO Client (cpf, address) VALUES
('33333333333', 'Rua das Palmeiras, 88'),
('88888888888', 'Av. Brasil, 210'),
('99999999999', 'Rua do Sol, 45');

-- ======================================================
-- LIVROS ADICIONAIS
-- ======================================================
INSERT INTO Book (bookName, bookLocation, numberOfPages, publisher) VALUES
('A Hora da Estrela',    'B-1', 88,  'Rocco'),
('1984',                 'B-2', 328, 'Companhia das Letras'),
('Harry Potter',         'C-1', 432, 'Rocco'),
('Memórias Póstumas',    'A-2', 200, 'Editora Globo');

-- Autores
INSERT INTO BookAuthor (bookId, authorId) VALUES
(2, 2), -- A Hora da Estrela -> Clarice Lispector
(3, 4), -- 1984 -> George Orwell
(4, 3), -- Harry Potter -> J. K. Rowling
(5, 1); -- Memórias Póstumas -> Machado de Assis

-- Categorias
INSERT INTO BookCategory (bookId, categoryId) VALUES
(2, 4), -- A Hora da Estrela -> Drama
(3, 3), -- 1984 -> Ficção Científica
(4, 2), -- Harry Potter -> Fantasia
(5, 1); -- Memórias Póstumas -> Romance

-- Exemplares
INSERT INTO BookCopy (bookId, statusAvailable) VALUES
(2, TRUE),  -- bookCopyId = 3
(2, TRUE),  -- bookCopyId = 4
(3, TRUE),  -- bookCopyId = 5
(3, FALSE), -- bookCopyId = 6 (emprestado)
(4, TRUE),  -- bookCopyId = 7
(4, TRUE),  -- bookCopyId = 8
(5, TRUE),  -- bookCopyId = 9
(5, FALSE); -- bookCopyId = 10 (emprestado)

INSERT INTO Loan (cpf, bookCopyId, loanDate, expectedReturnDate, renewals, active)
VALUES ('11111111111', 3,
        NOW(),
        DATE_ADD(NOW(), INTERVAL 8 DAY),
        0, TRUE);
UPDATE BookCopy SET statusAvailable = FALSE WHERE bookCopyId = 3;

INSERT INTO Loan (cpf, bookCopyId, loanDate, expectedReturnDate, renewals, active)
VALUES ('22222222222', 6,
        DATE_SUB(NOW(), INTERVAL 13 DAY),
        DATE_SUB(NOW(), INTERVAL 5 DAY),
        0, TRUE);

INSERT INTO Loan (cpf, bookCopyId, loanDate, expectedReturnDate, renewals, active)
VALUES ('33333333333', 10,
        DATE_SUB(NOW(), INTERVAL 24 DAY),
        DATE_ADD(NOW(), INTERVAL 2 DAY),
        2, TRUE);

INSERT INTO Renewal (newReturnDate, renewalNumber, loanId)
VALUES
(DATE_SUB(NOW(), INTERVAL 8 DAY),  1, LAST_INSERT_ID()),
(DATE_ADD(NOW(), INTERVAL 2 DAY),  2, LAST_INSERT_ID());

INSERT INTO Loan (cpf, bookCopyId, loanDate, expectedReturnDate, renewals, active)
VALUES ('11111111111', 5,
        DATE_SUB(NOW(), INTERVAL 10 DAY),
        DATE_SUB(NOW(), INTERVAL 2 DAY),
        0, FALSE);

INSERT INTO ReturnBook (returnDate, loanId)
VALUES (DATE_SUB(NOW(), INTERVAL 2 DAY), LAST_INSERT_ID());


UPDATE Client
SET startSuspensionDate = CURDATE(),
    endSuspensionDate   = DATE_ADD(CURDATE(), INTERVAL 7 DAY)
WHERE cpf = '88888888888';

INSERT INTO Loan (cpf, bookCopyId, loanDate, expectedReturnDate, renewals, active)
VALUES ('99999999999', 9,
        DATE_SUB(NOW(), INTERVAL 32 DAY),
        DATE_ADD(NOW(), INTERVAL 8 DAY),
        3, TRUE);
UPDATE BookCopy SET statusAvailable = FALSE WHERE bookCopyId = 9;

INSERT INTO Renewal (newReturnDate, renewalNumber, loanId) VALUES
(DATE_SUB(NOW(), INTERVAL 16 DAY), 1, LAST_INSERT_ID()),
(DATE_SUB(NOW(), INTERVAL 8 DAY),  2, LAST_INSERT_ID()),
(DATE_ADD(NOW(), INTERVAL 8 DAY),  3, LAST_INSERT_ID());



-- Exemplar 4 (A Hora da Estrela) — disponível
INSERT INTO Loan (cpf, bookCopyId, loanDate, expectedReturnDate, renewals, active)
VALUES ('11111111111', 4,
        NOW(),
        DATE_ADD(NOW(), INTERVAL 8 DAY),
        0, TRUE);
UPDATE BookCopy SET statusAvailable = FALSE WHERE bookCopyId = 4;

-- Exemplar 7 (Harry Potter) — disponível
INSERT INTO Loan (cpf, bookCopyId, loanDate, expectedReturnDate, renewals, active)
VALUES ('11111111111', 7,
        NOW(),
        DATE_ADD(NOW(), INTERVAL 8 DAY),
        0, TRUE);
UPDATE BookCopy SET statusAvailable = FALSE WHERE bookCopyId = 7;