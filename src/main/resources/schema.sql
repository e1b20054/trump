CREATE TABLE TRUMP(
  id IDENTITY,
  number INT NOT NULL,
  mark VARCHAR NOT NULL
);

CREATE TABLE THE_GAME(
  id IDENTITY,
  number INT NOT NULL
);

CREATE TABLE THE_GAME_Deck(
  id IDENTITY,
  number INT NOT NULL
);

CREATE TABLE THE_GAME_Tehuda(
  id IDENTITY,
  name VARCHAR NOT NULL,
  number INT NOT NULL
);


CREATE TABLE THE_GAME_Field(
  id IDENTITY,
  ba INT,
  number INT NOT NULL
);

CREATE TABLE CHAMBER(
  id IDENTITY,
  name VARCHAR NOT NULL,
  win INT NOT NULL
);

CREATE TABLE Deck(
  id IDENTITY,
  number INT NOT NULL,
  mark VARCHAR NOT NULL
);

CREATE TABLE Tehuda(
  id IDENTITY,
  number INT NOT NULL,
  mark VARCHAR NOT NULL,
  turn INT,
  name VARCHAR
);

CREATE TABLE MemoryDeck(
  id IDENTITY,
  number INT NOT NULL,
  mark VARCHAR NOT NULL,
  open boolean NOT NULL,
  get boolean NOT NULL,
  getter VARCHAR
);

CREATE TABLE Shitinarabe(
  id IDENTITY,
  number INT NOT NULL,
  mark VARCHAR NOT NULL,
  name INT,
  state VARCHAR
);

CREATE TABLE ShitinarabeField(
  id IDENTITY,
  number INT NOT NULL,
  mark VARCHAR NOT NULL,
  Field boolean
);

CREATE TABLE Smatch(
  id IDENTITY,
  number INT NOT NULL,
  mark VARCHAR NOT NULL,
  name INT,
  nextuser INT,
  isActive boolean
);

CREATE TABLE Field(
  id IDENTITY,
  number INT,
  mark VARCHAR,
  turn INT,
  name VARCHAR
);

CREATE TABLE MemoryChamber(
  id IDENTITY,
  name VARCHAR NOT NULL,
  oya boolean NOT NULL
);
