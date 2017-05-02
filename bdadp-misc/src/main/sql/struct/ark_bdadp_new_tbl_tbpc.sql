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
-- Table structure for table `tbl_tbpc`
--

DROP TABLE IF EXISTS `tbl_tbpc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_tbpc` (
  `cst_id` varchar(255) NOT NULL,
  `rcrd_expy_tms` varchar(255) NOT NULL,
  `multi_tenancy_id` varchar(255) NOT NULL,
  `ecif_cust_num` varchar(255) NOT NULL,
  `idv_lgl_nm` varchar(255) NOT NULL,
  `idv_nm_cpa_fullnm` varchar(255) DEFAULT NULL,
  `crdt_tpcd` varchar(255) DEFAULT NULL,
  `crdt_no` varchar(255) DEFAULT NULL,
  `brth_dt` varchar(255) DEFAULT NULL,
  `gnd_cd` varchar(255) DEFAULT NULL,
  `nat_cd` varchar(255) DEFAULT NULL,
  `rsdnc_nat_cd` varchar(255) DEFAULT NULL,
  `pref_lng_cd` varchar(255) DEFAULT NULL,
  `hshldrgst_adiv_cd` varchar(255) DEFAULT NULL,
  `mar_sttn_cd` varchar(255) DEFAULT NULL,
  `chl_sttn_cd` varchar(255) DEFAULT NULL,
  `ethnct_cd` varchar(255) DEFAULT NULL,
  `rlg_cd` varchar(255) DEFAULT NULL,
  `pltclparty_cd` varchar(255) DEFAULT NULL,
  `lcs_cd` varchar(255) DEFAULT NULL,
  `blng_insid` varchar(255) DEFAULT NULL,
  `ccb_empe_ind` varchar(255) DEFAULT NULL,
  `pln_fnc_efct_ind` varchar(255) DEFAULT NULL,
  `impt_psng_ind` varchar(255) DEFAULT NULL,
  `ptnl_vip_ind` varchar(255) DEFAULT NULL,
  `spclvip_ind` varchar(255) DEFAULT NULL,
  `stm_evl_cst_grd_cd` varchar(255) DEFAULT NULL,
  `mnul_evl_cst_grd_cd` varchar(255) DEFAULT NULL,
  `prvt_bnk_cst_grd_cd` varchar(255) DEFAULT NULL,
  `prvt_bnk_sign_cst_ind` varchar(255) DEFAULT NULL,
  `mo_incmam` double DEFAULT NULL,
  `cstmgr_id` varchar(255) DEFAULT NULL,
  `best_ctc_tm_cd` varchar(255) DEFAULT NULL,
  `best_ctc_mtdcd` varchar(255) DEFAULT NULL,
  `spnmlt_ind` varchar(255) DEFAULT NULL,
  `relparty_ind` varchar(255) DEFAULT NULL,
  `reg_afm_relparty_ind` varchar(255) DEFAULT NULL,
  `tel_tpcd` varchar(255) DEFAULT NULL,
  `best_ctc_tel` varchar(255) DEFAULT NULL,
  `lv1_br_no` varchar(255) DEFAULT NULL,
  `pref_msnd_mtdcd` varchar(255) DEFAULT NULL,
  `rcv_mail_adr_tpcd` varchar(255) DEFAULT NULL,
  `entp_adv_mgtppl_ind` varchar(255) DEFAULT NULL,
  `entp_act_ctrl_psn_ind` varchar(255) DEFAULT NULL,
  `enlgps_ind` varchar(255) DEFAULT NULL,
  `cst_chnl_bsop_ind` varchar(255) DEFAULT NULL,
  `empchnl_bsop_ind` varchar(255) DEFAULT NULL,
  `idcst_aum_bal` double DEFAULT NULL,
  `mt_ent_ind` varchar(255) DEFAULT NULL,
  `non_rsdnt_ind` varchar(255) DEFAULT NULL,
  `wrk_unit_char_cd` varchar(255) DEFAULT NULL,
  `wrk_unit_nm` varchar(255) DEFAULT NULL,
  `rsrv_fld1_inf` varchar(255) DEFAULT NULL,
  `rsrv_fld2_inf` varchar(255) DEFAULT NULL,
  `rsrv_fld3_inf` varchar(255) DEFAULT NULL,
  `crt_insid` varchar(255) DEFAULT NULL,
  `crt_empid` varchar(255) DEFAULT NULL,
  `last_udt_insid` varchar(255) DEFAULT NULL,
  `last_udt_empid` varchar(255) DEFAULT NULL,
  `cur_stm_crt_tms` double DEFAULT NULL,
  `cur_stm_udt_tms` double DEFAULT NULL,
  `lcl_yrmo_day` varchar(255) DEFAULT NULL,
  `lcl_hr_grd_scnd` varchar(255) DEFAULT NULL,
  `crt_stm_no` varchar(255) DEFAULT NULL,
  `udt_stm_no` varchar(255) DEFAULT NULL,
  `srcsys_crt_tms` double DEFAULT NULL,
  `srcsys_udt_tms` double DEFAULT NULL,
  PRIMARY KEY (`cst_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-02-20 13:36:56
