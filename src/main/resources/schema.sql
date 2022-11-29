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
  number INT NOT NULL
);

CREATE TABLE THE_GAME_Field1(
  id IDENTITY,
  number INT NOT NULL
);

CREATE TABLE THE_GAME_Field2(
  id IDENTITY,
  number INT NOT NULL
);

CREATE TABLE THE_GAME_Field3(
  id IDENTITY,
  number INT NOT NULL
);

CREATE TABLE THE_GAME_Field4(
  id IDENTITY,
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
  mark VARCHAR NOT NULL
);

CREATE TABLE MemoryDeck(
  id IDENTITY,
  number INT NOT NULL,
  mark VARCHAR NOT NULL,
  open boolean NOT NULL,
  get boolean NOT NULL
);

CREATE TABLE Shitinarabe(
  id IDENTITY,
  number INT NOT NULL,
  mark VARCHAR NOT NULL,
  name INT,
  state VARCHAR
);

CREATE TABLE Smatch(
  id IDENTITY,
  number INT NOT NULL,
  mark VARCHAR NOT NULL,
  name INT,
  nextuser INT,
  isActive boolean
);
