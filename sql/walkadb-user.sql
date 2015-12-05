drop user 'walka'@'localhost';
create user 'walka'@'localhost' identified by 'walka';
grant all privileges on walkadb.* to 'walka'@'localhost';
flush privileges;
