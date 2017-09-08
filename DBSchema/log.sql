CREATE TABLE log
(
  id       INTEGER PRIMARY KEY AUTOINCREMENT,
  time     TEXT,
  level    TEXT,
  category TEXT,
  thread   TEXT,
  message  TEXT
);