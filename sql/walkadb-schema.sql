drop database if exists walkadb;
create database walkadb;

use walkadb;

CREATE TABLE users (
    id BINARY(16) NOT NULL,
    loginid VARCHAR(15) NOT NULL UNIQUE,
    password BINARY(16) NOT NULL,
    email VARCHAR(255) NOT NULL,
    fullname VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user_roles (
    userid BINARY(16) NOT NULL,
    role ENUM ('registered'),
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    PRIMARY KEY (userid, role)
);

CREATE TABLE auth_tokens (
    userid BINARY(16) NOT NULL,
    token BINARY(16) NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    PRIMARY KEY (token)
);

CREATE TABLE events (
    id BINARY(16) NOT NULL,
    title VARCHAR(30) NOT NULL,
    creator BINARY(16) NOT NULL,  
    location VARCHAR(60) NOT NULL,
    notes VARCHAR(500) NOT NULL,
    startdate DATETIME NOT NULL,
    enddate DATETIME NOT NULL,
    last_modified TIMESTAMP NOT NULL,
    creation_timestamp DATETIME not null default current_timestamp,
    FOREIGN KEY (creator) REFERENCES users(id) on delete cascade,
    PRIMARY KEY (id)
);

CREATE TABLE groups (
    id BINARY(16) NOT NULL,
    idcreator BINARY(16) NOT NULL,
    gname VARCHAR(100) NOT NULL,  
    PRIMARY KEY (id),
    FOREIGN KEY (idcreator) REFERENCES users(id) on delete cascade
);

CREATE TABLE user_groups (
    idgroup BINARY(16) NOT NULL,
    iduser BINARY(16) NOT NULL, 
    FOREIGN KEY (idgroup) REFERENCES groups(id) on delete cascade,
    FOREIGN KEY (iduser) REFERENCES users(id) on delete cascade
);

CREATE TABLE user_events (
    idevent BINARY(16) NOT NULL,
    iduser BINARY(16) NOT NULL, 
    FOREIGN KEY (idevent) REFERENCES events(id) on delete cascade,
    FOREIGN KEY (iduser) REFERENCES users(id) on delete cascade
);

CREATE TABLE repositories (
    id BINARY(16) NOT NULL,
    idevent BINARY(16) NOT NULL,  
    last_modified TIMESTAMP NOT NULL,
    creation_timestamp DATETIME not null default current_timestamp,
    FOREIGN KEY (idevent) REFERENCES events(id) on delete cascade,
    PRIMARY KEY (id)
);

CREATE TABLE repo_files (
    idrepo BINARY(16) NOT NULL,
    fname VARCHAR(500) NOT NULL,
    FOREIGN KEY (idrepo) REFERENCES repositories(idevent) on delete cascade
);

CREATE TABLE checklists (
    idevent BINARY(16) NOT NULL,
    indexlist BINARY(16) NOT NULL,  
    textlist VARCHAR(500) NOT NULL,
    done BOOLEAN NOT NULL,
    FOREIGN KEY (idevent) REFERENCES events(id) on delete cascade
);

CREATE TABLE invitations (
    idevent BINARY(16) NOT NULL,
    idcreator BINARY(16) NOT NULL,
    iduser BINARY(16) NOT NULL,  
    invitaccept BOOLEAN NOT NULL, 
    FOREIGN KEY (idevent) REFERENCES events(id) on delete cascade,
    FOREIGN KEY (iduser) REFERENCES users(id) on delete cascade,
    FOREIGN KEY (idcreator) REFERENCES users(id) on delete cascade
);
