CREATE TABLE media (
  id           INTEGER PRIMARY KEY,
  status_id    INTEGER NOT NULL,
  media_url    TEXT    NOT NULL,
  url          TEXT    NOT NULL,
  display_url  TEXT    NOT NULL,
  expanded_url TEXT    NOT NULL,
  type         TEXT    NOT NULL,
  local_path   TEXT    NOT NULL,
  start        INTEGER NOT NULL,
  end          INTEGER NOT NULL
)