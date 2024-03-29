DELETE FROM Lekcja;
DELETE FROM Frekwencja;
DELETE FROM NauczycielPrzedmiot;
DELETE FROM Ocena;
DELETE FROM Uczen;
DELETE FROM Nauczyciel;
DELETE FROM Konto;
DELETE FROM Przedmiot;
DELETE FROM Klasa;
DELETE FROM Sesja;
DELETE FROM Sekretarka;
DELETE FROM GodzinyLekcyjne;

DROP TABLE Lekcja;
DROP TABLE Frekwencja;
DROP TABLE NauczycielPrzedmiot;
DROP TABLE Ocena;
DROP TABLE Uczen;
DROP TABLE Nauczyciel;
DROP TABLE Sesja;
DROP TABLE Konto;
DROP TABLE Przedmiot;
DROP TABLE Klasa;
DROP TABLE Sekretarka;
DROP TABLE GodzinyLekcyjne;


CREATE TABLE Konto (
    Nickname NVARCHAR2(32) NOT NULL,
    Haslo NVARCHAR2(256) NOT NULL,
    Typ NUMBER NOT NULL,
    Id NVARCHAR2(36) NOT NULL
);


CREATE TABLE Nauczyciel (
    Id NVARCHAR2(36),
    Imie NVARCHAR2(32) NOT NULL,
    Nazwisko NVARCHAR2(32) NOT NULL,
    DataZatrudnienia DATE DEFAULT sysdate,
    StopienZawodowy NVARCHAR2(16),
    KlasaId NVARCHAR2(3) DEFAULT 'Bez',
    CONSTRAINT Nauczyciel_pk PRIMARY KEY (Id)
);

CREATE TABLE Sekretarka (
    Id NVARCHAR2(36),
    Imie NVARCHAR2(32) NOT NULL,
    Nazwisko NVARCHAR2(32) NOT NULL,
    CONSTRAINT Sekretarka_pk PRIMARY KEY (Id)
);

CREATE TABLE Sesja (
    Token NVARCHAR2(64) NOT NULL,
    Expr TIMESTAMP NOT NULL,
    Id NVARCHAR2(36) NOT NULL,
    Typ NUMBER NOT NULL
);

CREATE TABLE Klasa (
    Nazwa NVARCHAR2(3) NOT NULL,
    Poziom NUMBER NOT NULL,
    CONSTRAINT Klasa_pk PRIMARY KEY (Nazwa)
);

CREATE TABLE Uczen (
    Id NVARCHAR2(36),
    Numer INT DEFAULT 0,
    Imie NVARCHAR2(32) NOT NULL,
    Nazwisko NVARCHAR2(32) NOT NULL,
    PESEL CHAR(11) NOT NULL,
    DataUrodzenia DATE NOT NULL,
    Miejsceurodzenia NVARCHAR2(32) NOT NULL,
    Klasa NVARCHAR2(3) DEFAULT '0',
    CONSTRAINT Uczen_pk PRIMARY KEY (Id),
    CONSTRAINT Klasa_fk FOREIGN KEY (Klasa) REFERENCES Klasa(Nazwa)
);

CREATE TABLE Przedmiot (
    Id NUMBER GENERATED BY DEFAULT AS IDENTITY,
    Nazwa NVARCHAR2(64) NOT NULL,
    Poziom NUMBER,
    Ilosc NUMBER,
    Obowiazkowy CHAR(1) DEFAULT 'T',
    CONSTRAINT Przedmiot_pk PRIMARY KEY (Id)
);

CREATE TABLE NauczycielPrzedmiot (
    nauczycielId NVARCHAR2(36),
    przedmiotId NUMBER,
    CONSTRAINT Przedmiot_fk FOREIGN KEY (przedmiotId) REFERENCES Przedmiot(Id) ON DELETE CASCADE,
    CONSTRAINT Nauczyciel_fk FOREIGN KEY (nauczycielId) REFERENCES Nauczyciel(Id) ON DELETE CASCADE,
    CONSTRAINT Nauczyciel_Przedmiot_pk PRIMARY KEY (nauczycielId, przedmiotId)
);

CREATE TABLE Lekcja (
    Id NUMBER GENERATED BY DEFAULT AS IDENTITY,
    DzienTygodnia NUMBER NOT NULL,
    Godzina NUMBER NOT NULL,
    KlasaId NVARCHAR2(3) NOT NULL,
    PrzedmiotId NUMBER NOT NULL,
    NauczycielId NVARCHAR2(36) NOT NULL,
    CONSTRAINT Lekcja_pk PRIMARY KEY (Id),
    CONSTRAINT Lekcja_Klasa_fk FOREIGN KEY (KlasaId) REFERENCES Klasa(Nazwa) ON DELETE CASCADE,
    CONSTRAINT Lekcja_Przedmiot_fk FOREIGN KEY (PrzedmiotId) REFERENCES Przedmiot(Id) ON DELETE CASCADE,
    CONSTRAINT Lekcja_Nauczyciel_fk FOREIGN KEY (NauczycielId) REFERENCES Nauczyciel(Id) ON DELETE CASCADE
);

CREATE TABLE Ocena(
    Id NUMBER GENERATED BY DEFAULT AS IDENTITY,
    Stopien NVARCHAR2(3) NOT NULL,
    Waga NUMBER NOT NULL,
    Kategoria NVARCHAR2(32),
    Komentarz NVARCHAR2(64) DEFAULT '',
    UczenId NVARCHAR2(36) NOT NULL,
    PrzedmiotId NUMBER NOT NULL,
    NauczycielId NVARCHAR2(36) NOT NULL,
    Data DATE NOT NULL,
    CONSTRAINT Ocena_pk PRIMARY KEY (Id),
    CONSTRAINT Ocana_Uczen_fk FOREIGN KEY (UczenId) REFERENCES Uczen(Id) ON DELETE CASCADE,
    CONSTRAINT Ocana_Przedmiot_fk FOREIGN KEY (PrzedmiotId) REFERENCES Przedmiot(Id) ON DELETE CASCADE,
    CONSTRAINT Ocana_Nauczyciel_fk FOREIGN KEY (NauczycielId) REFERENCES Nauczyciel(Id) ON DELETE CASCADE
);

CREATE TABLE Frekwencja (
    Rodzaj NUMBER NOT NULL,
    UczenId NVARCHAR2(36) NOT NULL,
    Klasa NVARCHAR2(3) NOT NULL,
    NauczycielId NVARCHAR2(36) NOT NULL,
    Tydzien NUMBER NOT NULL,
    DzienTygodnia NUMBER NOT NULL,
    Godzina NUMBER NOT NULL,
    CONSTRAINT Frekwencja_Uczen_fk FOREIGN KEY (UczenId) REFERENCES Uczen(Id) ON DELETE CASCADE,
    CONSTRAINT Frekwencja_Nauczyciel_fk FOREIGN KEY (NauczycielId) REFERENCES Nauczyciel(Id) ON DELETE CASCADE
);

CREATE TABLE GodzinyLekcyjne (
    Id NUMBER GENERATED BY DEFAULT AS IDENTITY,
    GodzRozp NVARCHAR2(8) NOT NULL,
    GodzZak NVARCHAR2(8) NOT NULL
);


CREATE OR REPLACE VIEW NauczycieleView AS
SELECT
    Nauczyciel.Id AS NauczycielId, Nauczyciel.Imie, Nauczyciel.Nazwisko, Nauczyciel.DataZatrudnienia,
    Nauczyciel.StopienZawodowy, LISTAGG(CONCAT(CONCAT(CONCAT(Przedmiot.Nazwa,' - Klasa '),Przedmiot.Poziom), ', ')) WITHIN GROUP (ORDER BY Przedmiot.Nazwa, Przedmiot.Poziom)  AS Przedmioty, Nauczyciel.KlasaId
FROM Nauczyciel
         LEFT JOIN NauczycielPrzedmiot ON Nauczyciel.Id = NauczycielPrzedmiot.NauczycielId
         LEFT JOIN Przedmiot ON Przedmiot.Id = NauczycielPrzedmiot.PrzedmiotId
GROUP BY Nauczyciel.Id, Nauczyciel.Imie, Nauczyciel.Nazwisko, Nauczyciel.DataZatrudnienia, Nauczyciel.StopienZawodowy, Nauczyciel.KlasaId
ORDER BY Nauczyciel.Nazwisko, Nauczyciel.Imie;

CREATE OR REPLACE VIEW KlasyView AS
SELECT Klasa.Nazwa, Klasa.Poziom, COUNT(Uczen.Id) LiczbaUczniow, CONCAT(CONCAT(Nauczyciel.imie, ' '), Nauczyciel.nazwisko) AS Wychowawca
FROM Klasa
         LEFT JOIN Nauczyciel ON Nauczyciel.KlasaId = Klasa.Nazwa
         INNER JOIN Uczen ON Uczen.Klasa = Klasa.Nazwa
GROUP BY Klasa.Nazwa, Klasa.Poziom, CONCAT(CONCAT(Nauczyciel.imie, ' '), Nauczyciel.nazwisko)
ORDER BY Klasa.Nazwa, Klasa.Poziom;

CREATE OR REPLACE VIEW NauczycielUczy AS
SELECT * FROM Nauczyciel
    INNER JOIN NauczycielPrzedmiot ON NauczycielPrzedmiot.NauczycielId = Nauczyciel.Id;

CREATE OR REPLACE VIEW NauczycielLekcje AS
SELECT DISTINCT
    Nauczyciel.Id AS nauczycielId, Przedmiot.Id AS przedmiotId,
    Przedmiot.Nazwa, lekcja.klasaid FROM Nauczyciel
    INNER JOIN Lekcja ON Lekcja.NauczycielId = Nauczyciel.Id
    INNER JOIN Przedmiot ON Przedmiot.Id = Lekcja.przedmiotId
ORDER BY Lekcja.klasaId, Przedmiot.Nazwa;



INSERT INTO Klasa VALUES ('0', 0);
INSERT INTO Konto VALUES ('Dyrektor', '1234', 3, 'Djeden');

INSERT INTO GodzinyLekcyjne VALUES (1, '08:00', '08:45');
INSERT INTO GodzinyLekcyjne VALUES (2, '08:50', '09:35');
INSERT INTO GodzinyLekcyjne VALUES (3, '09:45', '10:30');
INSERT INTO GodzinyLekcyjne VALUES (4, '10:40', '11:25');
INSERT INTO GodzinyLekcyjne VALUES (5, '11:45', '12:30');
INSERT INTO GodzinyLekcyjne VALUES (6, '12:40', '13:25');
INSERT INTO GodzinyLekcyjne VALUES (7, '13:35', '14:20');
INSERT INTO GodzinyLekcyjne VALUES (8, '15:05', '15:50');
INSERT INTO GodzinyLekcyjne VALUES (9, '16:35', '17:20');
INSERT INTO GodzinyLekcyjne VALUES (10, '17:30', '18:15');