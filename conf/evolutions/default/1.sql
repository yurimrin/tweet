# Tasks schema
 
# --- !Ups

CREATE SEQUENCE tweet_id_seq;
CREATE TABLE tweet (
    id integer NOT NULL DEFAULT nextval('tweet_id_seq'),
    content varchar(255),
    createdTime timestamp
);

# --- !Downs
 
DROP TABLE tweet;
DROP SEQUENCE tweet_id_seq;
