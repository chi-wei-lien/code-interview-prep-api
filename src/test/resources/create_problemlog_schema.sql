CREATE TABLE problem_log
(
   ID         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   NAME       VARCHAR(255) NOT NULL,
   DIFFICULTY REAL NOT NULL DEFAULT 0,
   URL        VARCHAR(255) NOT NULL,
   TIMESTAMP  TIMESTAMP WITH TIME ZONE
);