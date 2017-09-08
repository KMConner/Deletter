CREATE TABLE hashtag (
  id        INTEGER PRIMARY KEY,
  status_id INTEGER NOT NULL,
  text      TEXT    NOT NULL,
  start     INTEGER NOT NULL,
  end       INTEGER NOT NULL
)