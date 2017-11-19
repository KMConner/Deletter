CREATE TABLE status (
  id                    INTEGER PRIMARY KEY, -- Id of the tweet
  in_reply_to_status_id INTEGER, -- If this status is a reply, this filed contains the identifier of original tweet.
  created_at            TEXT    NOT NULL, -- UTC time when this tweet was created.
  retweet_status_id     INTEGER, -- If this status is a retweet, this filed contains the identifier of original tweet.
  text                  TEXT, -- The text of this tweet.
  user_id               INTEGER NOT NULL, -- The identifier of the user who posted this tweet.
  quote_status_id       INTEGER, -- If this tweet is a quote tweet, this field contains the identifier of original tweet.
  deleted               INTEGER NOT NULL DEFAULT 0, -- If this tweet has been deleted, the fields is set 1, otherwise, 0.
  hashtag_count         INTEGER NOT NULL, -- The number of hash tags  this tweet contains.
  url_count             INTEGER NOT NULL, -- The number of urls  this tweet contains.
  mention_count         INTEGER NOT NULL, -- The number of user mentions  this tweet contains.
  media_count           INTEGER NOT NULL -- The number of media entities  this tweet contains.
);

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
);

CREATE TABLE url (
  id           INTEGER PRIMARY KEY,
  status_id    INTEGER NOT NULL,
  display_url  TEXT    NOT NULL,
  expanded_url TEXT    NOT NULL,
  url          TEXT    NOT NULL,
  start        INTEGER NOT NULL,
  end          INTEGER NOT NULL
);

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
);

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
);

CREATE TABLE hashtag (
  id        INTEGER PRIMARY KEY,
  status_id INTEGER NOT NULL,
  text      TEXT    NOT NULL,
  start     INTEGER NOT NULL,
  end       INTEGER NOT NULL
);

CREATE TABLE delete_status (
  id               INTEGER PRIMARY KEY,
  delete_timestamp INTEGER NOT NULL,
  status_id        INTEGER NOT NULL,
  user_id          INTEGER NOT NULL
);