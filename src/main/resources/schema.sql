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
  getter VARCHAR,
  endMatch boolean
);

CREATE TABLE Shitinarabe(
  id IDENTITY,
  number INT NOT NULL,
  mark VARCHAR NOT NULL,
  name VARCHAR,
  state VARCHAR
);

CREATE TABLE ShitinarabeField(
  id IDENTITY,
  number INT NOT NULL,
  mark VARCHAR NOT NULL,
  field boolean
);


CREATE TABLE Shitinarabematch(
  id IDENTITY,
  number INT NOT NULL,
  mark VARCHAR,
  name VARCHAR,
  nextname VARCHAR
);

CREATE TABLE Field(
  id IDENTITY,
  number INT,
  mark VARCHAR,
  turn INT,
  name VARCHAR,
  nextname VARCHAR
);

CREATE TABLE MemoryChamber(
  id IDENTITY,
  name VARCHAR NOT NULL,
  oya boolean NOT NULL,
  now boolean NOT NULL,
  isActive boolean NOT NULL,
  get INT NOT NULL
);

CREATE TABLE Turn(
  id IDENTITY,
  name VARCHAR NOT NULL,
  tehuda INT,
  turn INT
);
