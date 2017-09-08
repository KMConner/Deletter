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
