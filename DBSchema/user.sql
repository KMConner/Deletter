CREATE TABLE user (
  id                            INTEGER NOT NULL PRIMARY KEY,
  name                          TEXT,
  profile_image_url             TEXT,
  profile_image_path            TEXT,
  profile_background_image_url  TEXT,
  profile_background_image_path TEXT,
  description                   TEXT,
  created_at                    TEXT    NOT NULL,
  protected                     INTEGER,
  screen_name                   TEXT    NOT NULL,
  url                           TEXT,
  location                      TEXT,
  updated_at                    TEXT    NOT NULL
)