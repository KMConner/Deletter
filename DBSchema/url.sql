CREATE TABLE url (
  id           INTEGER PRIMARY KEY,
  status_id    INTEGER NOT NULL,
  display_url  TEXT    NOT NULL,
  expanded_url TEXT    NOT NULL,
  url          TEXT    NOT NULL,
  start        INTEGER NOT NULL,
  end          INTEGER NOT NULL
)