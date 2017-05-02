-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: host6    Database: ark_bdadp_new
-- ------------------------------------------------------
-- Server version	5.5.47-log

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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userId` varchar(32) NOT NULL,
  `userName` varchar(45) DEFAULT NULL,
  `userPwd` varchar(45) DEFAULT NULL,
  `userDesc` varchar(255) DEFAULT NULL,
  `userStatus` int(11) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('2209a47118f04a3bad37b6192c92d4f7','chenliang','Y2hlbmxpYW5n','',0,'2016-10-20 16:20:56',NULL),('4e62156141ed4bc69a96881fe71539d4','wangshaoyan','Q0hhbmdlbWVfMTIz','',0,'2017-01-10 08:50:09',NULL),('60d29edaa1174031b837c72ad146e93c','admin1','NTY4NDY4','111',0,'2016-11-30 10:43:14','2016-11-30 11:05:37'),('972024f17c2c44a59da30ae7d29fd96c','caofei','MTIzNDU2','测试账号',0,'2017-01-10 08:50:26',NULL),('9cee02c6a9f848eeba3119b8a828c66d','admin','Q2hhbmdlbWVfMTIz','administrator',0,'2016-10-08 12:10:24',NULL),('a30d9dc1f28540219d676ba2919e8b2e','mengjz','bWVuZzEyMw==','mengjianzhuang',0,'2016-10-31 13:26:44',NULL),('a35538df5f2a4b94a8a90918982cdb6b','admin2','NTY4NDY4','123',0,'2016-11-30 11:04:27','2016-11-30 11:05:45'),('fb666fb759df4efe85019370d487c9fe','admin1','NTY4NDY4','123',0,'2016-11-30 10:42:55',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-02-20 13:38:26
