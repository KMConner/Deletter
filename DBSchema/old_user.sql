CREATE TABLE old_user (
  id                            INTEGER NOT NULL PRIMARY KEY,
  user_id                       INTEGER NOT NULL,
  name                          TEXT    NOT NULL,
  profile_image_path            TEXT,
  profile_background_image_path TEXT,
  description                   TEXT,
  created_at                    TEXT    NOT NULL,
  protected                     INTEGER,
  screen_name                   TEXT    NOT NULL,
  url                           TEXT,
  location                      TEXT,
  updated_at                    TEXT    NOT NULL
)