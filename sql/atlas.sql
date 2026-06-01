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

INSERT INTO Person (cpf, name, email, gender, birthDate) VALUES ('00000000000', 'Admin Chefe', 'admin@atlas.com', 'F', '2000-01-01');
INSERT INTO Employee (cpf, password) VALUES ('00000000000', 123456);
INSERT INTO Administrator (cpf) VALUES ('00000000000');

INSERT INTO Person (cpf, name, email, gender, birthDate) VALUES
('11111111111', 'Ana Beatriz Souza', 'ana.souza@email.com', 'F', '2001-04-15'),
('22222222222', 'Carlos Henrique Lima', 'carlos.lima@email.com', 'M', '1999-09-21'),
('33333333332', 'Nalva Alves Costa', 'nalva.costa@email.com', 'F', '2003-01-10'),
('44444444444', 'Joao Pedro Martins', 'joao.martins@email.com', 'M', '1995-06-18'),
('55555555555', 'Fernanda Rocha', 'fernanda.rocha@email.com', 'F', '1992-11-03'),
('66666666666', 'Ricardo Mendes', 'ricardo.mendes@email.com', 'M', '1988-08-27'),
('77777777777', 'Juliana Ferreira', 'juliana.ferreira@email.com', 'F', '1990-02-14'),
('33333333333', 'Mariana Alves Costa', 'mariana.costa@email.com', 'F', '2003-01-10'),
('88888888888', 'Pedro Henrique Silva', 'pedro.silva@email.com', 'M', '1998-03-22'),
('99999999999', 'Lucia Fernanda Reis',  'lucia.reis@email.com',   'F', '2000-07-05'),
('10110110101', 'Lucas Silva Ribeiro', 'lucas.ribeiro@email.com', 'M', '1994-03-12'),
('10210210202', 'Amanda Rodrigues Costa', 'amanda.rcosta@email.com', 'F', '1997-07-25'),
('10310310303', 'Bruno Alves Almeida', 'bruno.alves@email.com', 'M', '2002-11-05'),
('10410410404', 'Camila Pires Santos', 'camila.pires@email.com', 'F', '1991-01-30'),
('10510510505', 'Daniel Guedes Oliveira', 'daniel.guedes@email.com', 'M', '1985-05-14'),
('10610610606', 'Elena Martins Fonseca', 'elena.martins@email.com', 'F', '2000-09-18'),
('10710710707', 'Felipe Neves Miranda', 'felipe.neves@email.com', 'M', '1996-12-03'),
('10810810808', 'Gabriela Xavier Cruz', 'gabriela.xavier@email.com', 'F', '2004-02-22'),
('10910910909', 'Hugo Rezende Carvalho', 'hugo.rezende@email.com', 'M', '1993-06-08'),
('11011011010', 'Isabela Viana Borges', 'isabela.viana@email.com', 'F', '1998-10-27');

INSERT INTO Client (cpf, address) VALUES 
('11111111111', 'Rua das Flores, 120'), 
('22222222222', 'Av. Central, 450'),
('33333333333', 'Rua das Palmeiras, 88'),
('88888888888', 'Av. Brasil, 210'),
('99999999999', 'Rua do Sol, 45'),
('10110110101', 'Rua Bahia, 45'), 
('10210210202', 'Av. Getulio Vargas, 1012'),
('10310310303', 'Rua Paraiba, 890'), 
('10410410404', 'Alameda dos Anjos, 22'),
('10510510505', 'Av. Contorno, 3344'), 
('10610610606', 'Rua Goias, 77'),
('10710710707', 'Travessa da Paz, 12'), 
('10810810808', 'Rua do Comercio, 541'),
('10910910909', 'Av. Amazonas, 99'),
('33333333332', 'Rua das Palmeiras, 90'),
('11011011010', 'Rua das Palmeiras, 123');

INSERT INTO Employee (cpf, password) VALUES ('44444444444', 1234), ('55555555555', 5678), ('66666666666', 4321), ('77777777777', 8765);
INSERT INTO Attendant (cpf) VALUES ('44444444444'), ('55555555555');
INSERT INTO Librarian (cpf) VALUES ('66666666666'), ('77777777777');

INSERT INTO Author (authorName) VALUES 
('Machado de Assis'), ('Clarice Lispector'), ('J. K. Rowling'), ('George Orwell'),
('J.R.R. Tolkien'), ('George R.R. Martin'), ('Agatha Christie'), ('Stephen King'),
('Arthur Conan Doyle'), ('Gabriel Garcia Marquez'), ('Edgar Allan Poe'), ('Franz Kafka'),
('Virginia Woolf'), ('Isaac Asimov');

INSERT INTO Category (categoryName) VALUES 
('Romance'), ('Fantasia'), ('Ficcao Cientifica'), ('Drama'),
('Terror'), ('Suspense'), ('Policial'), ('Fantasia Epica'), ('Poesia'), ('Biografia');

INSERT INTO Book (bookName, bookLocation, numberOfPages, publisher) VALUES 
('Dom Casmurro', 'A1', 256, 'Editora Globo'),
('A Hora da Estrela',    'B-1', 88,  'Rocco'),
('1984',                 'B-2', 328, 'Companhia das Letras'),
('Harry Potter',         'C-1', 432, 'Rocco'),
('Memorias Postumas',    'A-2', 200, 'Editora Globo'),
('O Senhor dos Aneis', 'D-1', 1200, 'HarperCollins'),
('A Guerra dos Tronos', 'D-2', 600, 'Leya'),
('O Assassinato no Expresso Oriente', 'E-1', 240, 'HarperCollins'),
('O Iluminado', 'F-1', 464, 'Suma'),
('Um Estudo em Vermelho', 'E-2', 170, 'Zahar'),
('Cem Anos de Solidao', 'A-3', 448, 'Record'),
('O Corvo e Outros Contos', 'F-2', 200, 'DarkSide'),
('A Metamorfose', 'A-4', 96, 'Companhia das Letras'),
('Ao Farol', 'A-5', 240, 'Autentica'),
('Eu, Robo', 'B-3', 320, 'Aleph');

INSERT INTO BookAuthor (bookId, authorId) VALUES
(1, 1), (2, 2), (3, 4), (4, 3), (5, 1),
(6, 5), (7, 6), (8, 7), (9, 8), (10, 9),
(11, 10), (12, 11), (13, 12), (14, 13), (15, 14);

INSERT INTO BookCategory (bookId, categoryId) VALUES
(1, 1), (2, 4), (3, 3), (4, 2), (5, 1),
(6, 8), (7, 8), (8, 7), (9, 5), (10, 7),
(11, 1), (12, 5), (13, 4), (14, 4), (15, 3);

INSERT INTO BookCopy (bookId, statusAvailable) VALUES
(1, TRUE), (1, FALSE), (2, FALSE), (2, FALSE), (3, TRUE), (3, FALSE),
(4, FALSE), (4, TRUE), (5, TRUE), (5, FALSE), (6, FALSE), (6, FALSE),
(7, FALSE), (7, FALSE), (8, FALSE), (8, FALSE), (9, FALSE), (9, FALSE),
(10, FALSE), (10, FALSE), (11, FALSE), (11, FALSE), (12, FALSE), (12, FALSE),
(13, FALSE), (13, FALSE), (14, FALSE), (14, FALSE), (15, FALSE), (15, FALSE);

INSERT INTO Loan (cpf, bookCopyId, loanDate, expectedReturnDate, renewals, active) VALUES
('11111111111', 2, NOW(), DATE_ADD(NOW(), INTERVAL 8 DAY), 0, TRUE),
('22222222222', 6, DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, TRUE),
('33333333333', 10, DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY), 2, TRUE),
('11111111111', 3, NOW(), DATE_ADD(NOW(), INTERVAL 8 DAY), 0, TRUE),
('11111111111', 7, NOW(), DATE_ADD(NOW(), INTERVAL 8 DAY), 0, TRUE),
('99999999999', 9, DATE_SUB(NOW(), INTERVAL 32 DAY), DATE_ADD(NOW(), INTERVAL 8 DAY), 3, TRUE),
('22222222222', 4, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, FALSE),
('22222222222', 11, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 3 DAY), 0, FALSE),
('10110110101', 12, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY), 1, TRUE),
('10110110101', 13, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_ADD(NOW(), INTERVAL 4 DAY), 0, TRUE),
('10110110101', 14, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, FALSE),
('10210210202', 15, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY), 0, FALSE),
('10210210202', 16, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY), 1, TRUE),
('10210210202', 17, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_ADD(NOW(), INTERVAL 4 DAY), 0, TRUE),
('10310310303', 18, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_ADD(NOW(), INTERVAL 3 DAY), 3, TRUE),
('10310310303', 19, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 6 DAY), 0, TRUE),
('10310310303', 20, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, FALSE),
('10410410404', 21, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY), 0, FALSE),
('10410410404', 22, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), 1, FALSE),
('10410410404', 23, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 3 DAY), 0, TRUE),
('10510510505', 24, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_ADD(NOW(), INTERVAL 4 DAY), 1, TRUE),
('10510510505', 25, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_ADD(NOW(), INTERVAL 7 DAY), 0, TRUE),
('10510510505', 26, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), 0, FALSE),
('10610610606', 27, DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 32 DAY), 2, FALSE),
('10610610606', 28, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_ADD(NOW(), INTERVAL 5 DAY), 0, TRUE),
('10610610606', 29, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 1 DAY), 0, TRUE),
('10710710707', 30, DATE_SUB(NOW(), INTERVAL 11 DAY), DATE_ADD(NOW(), INTERVAL 5 DAY), 1, TRUE),
('10710710707', 1,  DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 11 DAY), 0, FALSE),
('10710710707', 2,  DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY), 0, TRUE),
('10810810808', 3,  DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, FALSE),
('10810810808', 4,  DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 14 DAY), 0, FALSE),
('10810810808', 5,  DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_ADD(NOW(), INTERVAL 4 DAY), 0, TRUE),
('10910910909', 6,  DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_ADD(NOW(), INTERVAL 3 DAY), 2, TRUE),
('10910910909', 7,  DATE_SUB(NOW(), INTERVAL 17 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY), 0, FALSE),
('10910910909', 8,  DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 13 DAY), 0, FALSE),
('11011011010', 9,  DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_ADD(NOW(), INTERVAL 6 DAY), 1, TRUE),
('11011011010', 10, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, FALSE),
('11011011010', 11, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), 0, FALSE);

INSERT INTO Renewal (newReturnDate, renewalNumber, loanId) VALUES
(DATE_SUB(NOW(), INTERVAL 8 DAY),  1, 3),
(DATE_ADD(NOW(), INTERVAL 2 DAY),  2, 3),
(DATE_SUB(NOW(), INTERVAL 16 DAY), 1, 6),
(DATE_SUB(NOW(), INTERVAL 8 DAY),  2, 6),
(DATE_ADD(NOW(), INTERVAL 8 DAY),  3, 6),
(DATE_ADD(NOW(), INTERVAL 2 DAY),  1, 9),
(DATE_ADD(NOW(), INTERVAL 2 DAY),  1, 13),
(DATE_SUB(NOW(), INTERVAL 5 DAY),  1, 15),
(DATE_SUB(NOW(), INTERVAL 1 DAY),  2, 15),
(DATE_ADD(NOW(), INTERVAL 3 DAY),  3, 15),
(DATE_SUB(NOW(), INTERVAL 14 DAY), 1, 19),
(DATE_ADD(NOW(), INTERVAL 4 DAY),  1, 21),
(DATE_SUB(NOW(), INTERVAL 34 DAY), 1, 24),
(DATE_SUB(NOW(), INTERVAL 32 DAY), 2, 24),
(DATE_ADD(NOW(), INTERVAL 5 DAY),  1, 27),
(DATE_ADD(NOW(), INTERVAL 3 DAY),  1, 33),
(DATE_ADD(NOW(), INTERVAL 5 DAY),  2, 33),
(DATE_ADD(NOW(), INTERVAL 6 DAY),  1, 36);

INSERT INTO ReturnBook (returnDate, loanId) VALUES
(DATE_SUB(NOW(), INTERVAL 2 DAY), 6),
(DATE_SUB(NOW(), INTERVAL 7 DAY), 7),
(DATE_SUB(NOW(), INTERVAL 1 DAY), 8),
(DATE_SUB(NOW(), INTERVAL 7 DAY), 11),
(DATE_SUB(NOW(), INTERVAL 12 DAY), 12),
(DATE_SUB(NOW(), INTERVAL 6 DAY), 17),
(DATE_SUB(NOW(), INTERVAL 22 DAY), 18),
(DATE_SUB(NOW(), INTERVAL 10 DAY), 19),
(DATE_SUB(NOW(), INTERVAL 8 DAY), 23),
(DATE_SUB(NOW(), INTERVAL 32 DAY), 24),
(DATE_SUB(NOW(), INTERVAL 11 DAY), 28),
(DATE_SUB(NOW(), INTERVAL 6 DAY), 30),
(DATE_SUB(NOW(), INTERVAL 14 DAY), 31),
(DATE_SUB(NOW(), INTERVAL 9 DAY), 34),
(DATE_SUB(NOW(), INTERVAL 13 DAY), 35),
(DATE_SUB(NOW(), INTERVAL 4 DAY), 37),
(DATE_SUB(NOW(), INTERVAL 8 DAY), 38);

UPDATE Client SET startSuspensionDate = CURDATE(), endSuspensionDate = DATE_ADD(CURDATE(), INTERVAL 7 DAY) WHERE cpf = '88888888888';

-- NOVOS DADOS PARA O BD--

-- ======================================================
-- EXPANSÃO DE DADOS: LIVROS COMPLEMENTARES (16 A 60)
-- ======================================================

INSERT INTO Book (bookName, bookLocation, numberOfPages, publisher) VALUES 
('O Hobbit', 'D-3', 336, 'HarperCollins'),
('O Silmarillion', 'D-4', 416, 'HarperCollins'),
('A Furia dos Reis', 'D-5', 688, 'Leya'),
('A Tormenta de Espadas', 'D-6', 884, 'Leya'),
('O Festim dos Corvos', 'D-7', 644, 'Leya'),
('A Danca dos Dragoes', 'D-8', 864, 'Leya'),
('Morte no Nilo', 'E-3', 272, 'HarperCollins'),
('O Misterio de Sittaford', 'E-4', 224, 'HarperCollins'),
('E Nao Sobrou Nenhum', 'E-5', 400, 'HarperCollins'),
('Um Corpo na Biblioteca', 'E-6', 224, 'HarperCollins'),
('O Iluminado Vol 2', 'F-3', 400, 'Suma'),
('It A Coisa', 'F-4', 1104, 'Suma'),
('Misery', 'F-5', 328, 'Suma'),
('O Cemiterio', 'F-6', 424, 'Suma'),
('As Aventuras de Sherlock Holmes', 'E-7', 288, 'Zahar'),
('O Cao dos Baskerville', 'E-8', 224, 'Zahar'),
('O Signo dos Quatro', 'E-9', 160, 'Zahar'),
('O Amor nos Tempos do Colera', 'A-6', 434, 'Record'),
('Cronica de uma Morte Anunciada', 'A-7', 160, 'Record'),
('Do Amor e Outros Demonios', 'A-8', 192, 'Record'),
('O Gato Preto e Outras Historias', 'F-7', 180, 'DarkSide'),
('A Queda da Casa de Usher', 'F-8', 150, 'DarkSide'),
('O Processo', 'A-9', 250, 'Companhia das Letras'),
('O Castelo', 'A-10', 320, 'Companhia das Letras'),
('Carta ao Pai', 'A-11', 120, 'Companhia das Letras'),
('Mrs Dalloway', 'A-12', 224, 'Autentica'),
('Orlando', 'A-13', 288, 'Autentica'),
('As Ondas', 'A-14', 256, 'Autentica'),
('Fundacao', 'B-4', 240, 'Aleph'),
('Fundacao e Imperio', 'B-5', 256, 'Aleph'),
('Segunda Fundacao', 'B-6', 256, 'Aleph'),
('O Fim da Eternidade', 'B-7', 288, 'Aleph'),
('Fahrenheit 451', 'B-8', 216, 'Ballantine'),
('Cronicas Marcianas', 'B-9', 300, 'Ballantine'),
('Admiravel Mundo Novo', 'B-10', 312, 'Antofagica'),
('A Revolucao dos Bichos', 'B-11', 144, 'Companhia das Letras'),
('Grande Sertao Veredas', 'A-15', 600, 'Companhia das Letras'),
('O Alquimista', 'C-2', 172, 'Rocco'),
('O Diario de Anne Frank', 'G-1', 350, 'Record'),
('Sapiens', 'G-2', 464, 'L&PM'),
('Homo Deus', 'G-3', 448, 'L&PM'),
('Quincas Borba', 'A-16', 200, 'Editora Globo'),
('O Alienista', 'A-17', 120, 'Editora Globo'),
('A Paixao Segundo G.H.', 'B-12', 180, 'Rocco'),
('Agua Viva', 'B-13', 90, 'Rocco');

INSERT INTO BookAuthor (bookId, authorId) VALUES
(16, 5), (17, 5), (18, 6), (19, 6), (20, 6), (21, 6), (22, 7), (23, 7), (24, 7), (25, 7),
(26, 8), (27, 8), (28, 8), (29, 8), (30, 9), (31, 9), (32, 9), (33, 10), (34, 10), (35, 10),
(36, 11), (37, 11), (38, 12), (39, 12), (40, 12), (41, 13), (42, 13), (43, 13), (44, 14), (45, 14),
(46, 14), (47, 14), (48, 4), (49, 4), (50, 4), (51, 4), (52, 1), (53, 3), (54, 10), (55, 14),
(56, 14), (57, 1), (58, 1), (59, 2), (60, 2);

INSERT INTO BookCategory (bookId, categoryId) VALUES
(16, 2), (17, 2), (18, 8), (19, 8), (20, 8), (21, 8), (22, 7), (23, 7), (24, 7), (25, 7),
(26, 5), (27, 5), (28, 5), (29, 5), (30, 7), (31, 7), (32, 7), (33, 1), (34, 1), (35, 1),
(36, 5), (37, 5), (38, 4), (39, 4), (40, 4), (41, 4), (42, 4), (43, 4), (44, 3), (45, 3),
(46, 3), (47, 3), (48, 3), (49, 3), (50, 3), (51, 3), (52, 1), (53, 2), (54, 10), (55, 10),
(56, 10), (57, 1), (58, 1), (59, 4), (60, 4);

INSERT INTO BookCopy (bookId, statusAvailable) VALUES
(16, FALSE), (17, FALSE), (18, FALSE), (19, FALSE), (20, FALSE), (21, FALSE), (22, FALSE), (23, FALSE),
(24, FALSE), (25, FALSE), (26, FALSE), (27, FALSE), (28, FALSE), (29, FALSE), (30, FALSE), (31, FALSE),
(32, FALSE), (33, FALSE), (34, FALSE), (35, FALSE), (36, FALSE), (37, FALSE), (38, FALSE), (39, FALSE),
(40, FALSE), (41, FALSE), (42, FALSE), (43, FALSE), (44, FALSE), (45, FALSE), (46, FALSE), (47, FALSE),
(48, FALSE), (49, FALSE), (50, FALSE), (51, FALSE), (52, FALSE), (53, FALSE), (54, FALSE), (55, FALSE),
(56, FALSE), (57, FALSE), (58, FALSE), (59, FALSE), (60, FALSE);

INSERT INTO Loan (cpf, bookCopyId, loanDate, expectedReturnDate, renewals, active) VALUES
('11111111111', 31, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 3 DAY), 0, TRUE),
('22222222222', 32, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 0, FALSE),
('33333333333', 33, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY), 1, TRUE),
('88888888888', 34, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), 0, FALSE),
('88888888888', 35, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 14 DAY), 0, FALSE),
('88888888888', 36, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_ADD(NOW(), INTERVAL 4 DAY), 0, TRUE),
('99999999999', 37, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), 1, FALSE),
('99999999999', 38, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 3 DAY), 0, TRUE),
('10110110101', 39, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, FALSE),
('10210210202', 40, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 17 DAY), 1, FALSE),
('10310310303', 41, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_ADD(NOW(), INTERVAL 4 DAY), 0, TRUE),
('10410410404', 42, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_ADD(NOW(), INTERVAL 4 DAY), 1, TRUE),
('10510510505', 43, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY), 0, FALSE),
('10610610606', 44, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY), 0, TRUE),
('10710710707', 45, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), 0, FALSE),
('10810810808', 46, DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 11 DAY), 0, FALSE),
('10910910909', 47, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY), 1, TRUE),
('11011011010', 48, DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, FALSE),
('33333333332', 49, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_ADD(NOW(), INTERVAL 7 DAY), 0, TRUE),
('33333333332', 50, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), 0, FALSE),
('33333333332', 51, DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 13 DAY), 0, FALSE);

INSERT INTO Renewal (newReturnDate, renewalNumber, loanId) VALUES
(DATE_ADD(NOW(), INTERVAL 2 DAY), 1, 41),
(DATE_SUB(NOW(), INTERVAL 10 DAY), 1, 48),
(DATE_ADD(NOW(), INTERVAL 4 DAY), 1, 50),
(DATE_SUB(NOW(), INTERVAL 17 DAY), 1, 52),
(DATE_ADD(NOW(), INTERVAL 2 DAY), 1, 55);

INSERT INTO ReturnBook (returnDate, loanId) VALUES
(DATE_SUB(NOW(), INTERVAL 4 DAY), 40),
(DATE_SUB(NOW(), INTERVAL 7 DAY), 42),
(DATE_SUB(NOW(), INTERVAL 14 DAY), 43),
(DATE_SUB(NOW(), INTERVAL 10 DAY), 45),
(DATE_SUB(NOW(), INTERVAL 6 DAY), 47),
(DATE_SUB(NOW(), INTERVAL 17 DAY), 48),
(DATE_SUB(NOW(), INTERVAL 22 DAY), 51),
(DATE_SUB(NOW(), INTERVAL 8 DAY), 53),
(DATE_SUB(NOW(), INTERVAL 11 DAY), 54),
(DATE_SUB(NOW(), INTERVAL 5 DAY), 56),
(DATE_SUB(NOW(), INTERVAL 6 DAY), 58),
(DATE_SUB(NOW(), INTERVAL 13 DAY), 59);