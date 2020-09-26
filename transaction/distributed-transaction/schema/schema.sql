/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : seata_demo

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 26/09/2020 12:13:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_account
-- ----------------------------
DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户id',
  `money` int DEFAULT NULL COMMENT '账户金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of t_account
-- ----------------------------
BEGIN;
INSERT INTO `t_account` VALUES (1, 'imyzt', 8);
COMMIT;

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户id',
  `commodity_code` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品编码',
  `count` int DEFAULT NULL COMMENT '数量',
  `money` int DEFAULT NULL COMMENT '金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of t_order
-- ----------------------------
BEGIN;
INSERT INTO `t_order` VALUES (12, 'imyzt', 'TAOBAO', 1, 2);
COMMIT;

-- ----------------------------
-- Table structure for t_storage
-- ----------------------------
DROP TABLE IF EXISTS `t_storage`;
CREATE TABLE `t_storage` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `commodity_code` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品编码',
  `count` int DEFAULT NULL COMMENT '商品库存',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of t_storage
-- ----------------------------
BEGIN;
INSERT INTO `t_storage` VALUES (1, 'TAOBAO', 9);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
