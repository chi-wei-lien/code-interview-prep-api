CREATE TABLE users
(
   ID         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   USERNAME   VARCHAR(255) NOT NULL,
   PASSWORD   VARCHAR(255) NOT NULL,
   EMAIL      VARCHAR(255) NOT NULL
);
