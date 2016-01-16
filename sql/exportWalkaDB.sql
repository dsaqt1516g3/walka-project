-- MySQL dump 10.13  Distrib 5.6.27, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: walkadb
-- ------------------------------------------------------
-- Server version	5.6.27-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `auth_tokens`
--

DROP TABLE IF EXISTS `auth_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_tokens` (
  `userid` binary(16) NOT NULL,
  `token` binary(16) NOT NULL,
  PRIMARY KEY (`token`),
  KEY `userid` (`userid`),
  CONSTRAINT `auth_tokens_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_tokens`
--

LOCK TABLES `auth_tokens` WRITE;
/*!40000 ALTER TABLE `auth_tokens` DISABLE KEYS */;
INSERT INTO `auth_tokens` VALUES ('¼&\"¦jå±‰>Ž^ÃÕ','Å’ÿ¦jå±‰>Ž^ÃÕ'),('o¡¥‹å¿»>Ž^ÃÕ','z_º¥‹å¿»>Ž^ÃÕ'),('INd°Ùå¢4>Ž^ÃÕ','¿òÖ³Õå±ð>Ž^ÃÕ'),('~TBþ¶Nå—->Ž^ÃÕ','~\\¹¶Nå—->Ž^ÃÕ'),('•­2Î£ïå¿>Ž^ÃÕ','Þë¥å¿»>Ž^ÃÕ');
/*!40000 ALTER TABLE `auth_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `checklists`
--

DROP TABLE IF EXISTS `checklists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `checklists` (
  `idevent` binary(16) NOT NULL,
  `indexlist` binary(16) NOT NULL,
  `textlist` varchar(500) NOT NULL,
  `done` tinyint(1) NOT NULL,
  KEY `idevent` (`idevent`),
  CONSTRAINT `checklists_ibfk_1` FOREIGN KEY (`idevent`) REFERENCES `events` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checklists`
--

LOCK TABLES `checklists` WRITE;
/*!40000 ALTER TABLE `checklists` DISABLE KEYS */;
/*!40000 ALTER TABLE `checklists` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `events` (
  `id` binary(16) NOT NULL,
  `title` varchar(30) NOT NULL,
  `creator` binary(16) NOT NULL,
  `location` varchar(60) NOT NULL,
  `notes` varchar(500) NOT NULL,
  `startdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `last_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `creation_timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tag` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `creator` (`creator`),
  CONSTRAINT `events_ibfk_1` FOREIGN KEY (`creator`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `events`
--

LOCK TABLES `events` WRITE;
/*!40000 ALTER TABLE `events` DISABLE KEYS */;
INSERT INTO `events` VALUES ('){­‰å”>Ž^ÃÕ','Testing2','¼&\"¦jå±‰>Ž^ÃÕ','Castelldefels','Otro evento','2015-12-03 17:00:15','2015-12-03 17:00:16','2015-12-28 17:32:51','2015-12-28 18:32:51',NULL),('yò¦~å±‰>Ž^ÃÕ','Obtain test','¼&\"¦jå±‰>Ž^ÃÕ','Castelldefels','Otro evento','2015-12-19 17:00:15','2015-12-19 17:00:18','2015-12-19 18:26:38','2015-12-19 19:26:38',NULL),('¦/ï¹‡åˆ±>Ž^ÃÕ','Ieeee','¼&\"¦jå±‰>Ž^ÃÕ','Castelldefels','Otro evento','2015-08-19 17:00:15','2015-10-19 17:00:15','2016-01-12 23:49:02','2016-01-13 00:49:02','Trabajo'),('4Vx\0\0\0\0\0\0\0\0\0\0\0\0','Create','¼&\"¦jå±‰>Ž^ÃÕ','Somewhere','HHHHH','2015-10-19 17:00:15','2015-10-19 17:00:15','2015-12-29 16:24:08','2015-12-29 17:24:08','Ocio'),('(`¼.­Žå”>Ž^ÃÕ','Testing3','¼&\"¦jå±‰>Ž^ÃÕ','Castelldefels','Otro evento','2015-12-03 17:00:15','2015-12-03 17:00:16','2015-12-28 18:09:37','2015-12-28 19:09:37',NULL),('(lœ/¥’å¿»>Ž^ÃÕ','Event4','o¡¥‹å¿»>Ž^ÃÕ','Castelldefels','Hola que haces','2015-12-21 10:06:15','2015-12-21 17:06:15','2015-12-18 14:18:06','2015-12-18 15:18:06',NULL),('-ùf¦~å±‰>Ž^ÃÕ','Obtain test2','•­2Î£ïå¿>Ž^ÃÕ','Castelldefels','Otro evento','2015-12-19 17:00:15','2015-12-19 17:00:18','2015-12-19 18:27:37','2015-12-19 19:27:37',NULL),('¦\Zy§>åª¸>Ž^ÃÕ','Event test','¼&\"¦jå±‰>Ž^ÃÕ','Castelldefels','Otro evento','2015-08-19 17:00:15','2015-10-19 17:00:15','2015-12-20 17:25:22','2015-12-20 18:25:22',NULL),('¨Ïäµ¸låˆ±>Ž^ÃÕ','Test para grupoooos','¼&\"¦jå±‰>Ž^ÃÕ','Castelldefels','Otro evento','2015-08-19 17:00:15','2015-10-19 17:00:15','2016-01-11 14:07:33','2016-01-11 15:07:33','Trabajo'),('¸§|ü¦kå±‰>Ž^ÃÕ','Event test','¼&\"¦jå±‰>Ž^ÃÕ','Castelldefels','Otro evento','2015-10-19 17:00:15','2015-10-19 17:00:15','2015-12-19 16:15:29','2015-12-19 17:15:29',NULL),('Ïq®‹°Äå¢4>Ž^ÃÕ','Event test','¼&\"¦jå±‰>Ž^ÃÕ','Castelldefels','Otro evento','2015-08-19 17:00:15','2015-10-19 17:00:15','2016-01-01 20:18:24','2016-01-01 21:18:24','Trabajo'),('ÔÐÑ´˜å •>Ž^ÃÕ','Ieeeeep','¼&\"¦jå±‰>Ž^ÃÕ','Castelldefels','Otro evento','2015-08-19 17:00:15','2015-10-19 17:00:15','2016-01-06 17:13:40','2016-01-06 18:13:40','Trabajo'),('Ù‡¶­ˆå”>Ž^ÃÕ','Event3_modiiiiii','¼&\"¦jå±‰>Ž^ÃÕ','Castelldefels','','2015-12-21 10:06:15','2015-12-21 17:06:15','2016-01-01 20:36:47','2015-12-28 18:31:38','Ocio'),('Ýh-¹‡åˆ±>Ž^ÃÕ','Ieeee','¼&\"¦jå±‰>Ž^ÃÕ','Castelldefels','Otro evento','2015-08-19 17:00:15','2015-10-19 17:00:15','2016-01-12 23:54:48','2016-01-13 00:54:48','Trabajo'),('æªÈ®Iå”>Ž^ÃÕ','Testingbro','¼&\"¦jå±‰>Ž^ÃÕ','Castelldefels','Otro evento','2015-10-19 17:00:15','2015-10-20 17:00:15','2015-12-29 16:33:32','2015-12-29 17:33:32','Ocio'),('ý+|ž­ˆå”>Ž^ÃÕ','Testing2','¼&\"¦jå±‰>Ž^ÃÕ','Castelldefels','Otro evento','2015-12-02 17:00:15','2015-12-10 17:00:15','2015-12-28 17:32:37','2015-12-28 18:32:37',NULL);
/*!40000 ALTER TABLE `events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `id` binary(16) NOT NULL,
  `idcreator` binary(16) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `creation_timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idcreator` (`idcreator`),
  CONSTRAINT `groups_ibfk_1` FOREIGN KEY (`idcreator`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` VALUES (';Ýì\'¶ðå¹ø>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ','Grupo 1','Test','2016-01-09 17:44:21','2016-01-09 21:27:28'),('ym£\r¸fåˆ±>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ','Grupooo','Descripcion de prueba ','2016-01-11 14:23:16','2016-01-11 13:23:16');
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invitations`
--

DROP TABLE IF EXISTS `invitations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invitations` (
  `groupid` binary(16) NOT NULL,
  `userInvited` binary(16) NOT NULL,
  KEY `groupid` (`groupid`),
  KEY `userInvited` (`userInvited`),
  CONSTRAINT `invitations_ibfk_1` FOREIGN KEY (`groupid`) REFERENCES `groups` (`id`) ON DELETE CASCADE,
  CONSTRAINT `invitations_ibfk_2` FOREIGN KEY (`userInvited`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invitations`
--

LOCK TABLES `invitations` WRITE;
/*!40000 ALTER TABLE `invitations` DISABLE KEYS */;
/*!40000 ALTER TABLE `invitations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repo_files`
--

DROP TABLE IF EXISTS `repo_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `repo_files` (
  `idrepo` binary(16) NOT NULL,
  `fname` varchar(500) NOT NULL,
  KEY `idrepo` (`idrepo`),
  CONSTRAINT `repo_files_ibfk_1` FOREIGN KEY (`idrepo`) REFERENCES `repositories` (`idevent`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repo_files`
--

LOCK TABLES `repo_files` WRITE;
/*!40000 ALTER TABLE `repo_files` DISABLE KEYS */;
/*!40000 ALTER TABLE `repo_files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repositories`
--

DROP TABLE IF EXISTS `repositories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `repositories` (
  `id` binary(16) NOT NULL,
  `idevent` binary(16) NOT NULL,
  `last_modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `creation_timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idevent` (`idevent`),
  CONSTRAINT `repositories_ibfk_1` FOREIGN KEY (`idevent`) REFERENCES `events` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repositories`
--

LOCK TABLES `repositories` WRITE;
/*!40000 ALTER TABLE `repositories` DISABLE KEYS */;
/*!40000 ALTER TABLE `repositories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_events`
--

DROP TABLE IF EXISTS `user_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_events` (
  `idevent` binary(16) NOT NULL,
  `iduser` binary(16) NOT NULL,
  `colour` varchar(16) DEFAULT NULL,
  KEY `idevent` (`idevent`),
  KEY `iduser` (`iduser`),
  CONSTRAINT `user_events_ibfk_1` FOREIGN KEY (`idevent`) REFERENCES `events` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_events_ibfk_2` FOREIGN KEY (`iduser`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_events`
--

LOCK TABLES `user_events` WRITE;
/*!40000 ALTER TABLE `user_events` DISABLE KEYS */;
INSERT INTO `user_events` VALUES ('(lœ/¥’å¿»>Ž^ÃÕ','o¡¥‹å¿»>Ž^ÃÕ',NULL),('¸§|ü¦kå±‰>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ',NULL),('yò¦~å±‰>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ',NULL),('-ùf¦~å±‰>Ž^ÃÕ','•­2Î£ïå¿>Ž^ÃÕ',NULL),('¦\Zy§>åª¸>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ',NULL),('Ù‡¶­ˆå”>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ','orange'),('ý+|ž­ˆå”>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ',NULL),('){­‰å”>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ',NULL),('(`¼.­Žå”>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ',NULL),('æªÈ®Iå”>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ',NULL),('¸§|ü¦kå±‰>Ž^ÃÕ','o¡¥‹å¿»>Ž^ÃÕ',NULL),('Ïq®‹°Äå¢4>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ','yellow'),('ÔÐÑ´˜å •>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ','yellow'),('¸§|ü¦kå±‰>Ž^ÃÕ','~TBþ¶Nå—->Ž^ÃÕ',NULL),('¨Ïäµ¸låˆ±>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ','yellow'),('¨Ïäµ¸låˆ±>Ž^ÃÕ','INd°Ùå¢4>Ž^ÃÕ',NULL),('¨Ïäµ¸låˆ±>Ž^ÃÕ','~TBþ¶Nå—->Ž^ÃÕ',NULL),('¦/ï¹‡åˆ±>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ','yellow'),('Ýh-¹‡åˆ±>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ','blue');
/*!40000 ALTER TABLE `user_events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_groups`
--

DROP TABLE IF EXISTS `user_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_groups` (
  `idgroup` binary(16) NOT NULL,
  `iduser` binary(16) NOT NULL,
  KEY `idgroup` (`idgroup`),
  KEY `iduser` (`iduser`),
  CONSTRAINT `user_groups_ibfk_1` FOREIGN KEY (`idgroup`) REFERENCES `groups` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_groups_ibfk_2` FOREIGN KEY (`iduser`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_groups`
--

LOCK TABLES `user_groups` WRITE;
/*!40000 ALTER TABLE `user_groups` DISABLE KEYS */;
INSERT INTO `user_groups` VALUES ('ym£\r¸fåˆ±>Ž^ÃÕ','¼&\"¦jå±‰>Ž^ÃÕ'),('ym£\r¸fåˆ±>Ž^ÃÕ','~TBþ¶Nå—->Ž^ÃÕ'),('ym£\r¸fåˆ±>Ž^ÃÕ','INd°Ùå¢4>Ž^ÃÕ');
/*!40000 ALTER TABLE `user_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_roles` (
  `userid` binary(16) NOT NULL,
  `role` enum('registered') NOT NULL DEFAULT 'registered',
  PRIMARY KEY (`userid`,`role`),
  CONSTRAINT `user_roles_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES ('¼&\"¦jå±‰>Ž^ÃÕ','registered'),('o¡¥‹å¿»>Ž^ÃÕ','registered'),('INd°Ùå¢4>Ž^ÃÕ','registered'),('~TBþ¶Nå—->Ž^ÃÕ','registered'),('•­2Î£ïå¿>Ž^ÃÕ','registered');
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` binary(16) NOT NULL,
  `loginid` varchar(15) NOT NULL,
  `password` binary(16) NOT NULL,
  `email` varchar(255) NOT NULL,
  `fullname` varchar(255) NOT NULL,
  `country` varchar(16) DEFAULT NULL,
  `city` varchar(32) DEFAULT NULL,
  `phonenumber` varchar(32) DEFAULT NULL,
  `birthdate` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `loginid` (`loginid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('¼&\"¦jå±‰>Ž^ÃÕ','jhonD','Ü›ÛRÐMÂ\06ÛØ1>ÐU','jonD@bikinibottom.com','Jhon Doe',NULL,NULL,NULL,NULL),('o¡¥‹å¿»>Ž^ÃÕ','test2','­4‚’¹1–ºz‡+','test2@eetac.com','test2',NULL,NULL,NULL,NULL),('INd°Ùå¢4>Ž^ÃÕ','New','Ü›ÛRÐMÂ\06ÛØ1>ÐU','New@bikinibottom.com','New Doe',NULL,NULL,NULL,NULL),('~TBþ¶Nå—->Ž^ÃÕ','Marta','Ü›ÛRÐMÂ\06ÛØ1>ÐU','martaEdited@acme.com','Spongebob Squarepants','Marruecos','Gava','666666666','04/04/1995'),('•­2Î£ïå¿>Ž^ÃÕ','test','Ü›ÛRÐMÂ\06ÛØ1>ÐU','test@test.com','Test Tset',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-01-13  1:18:44
