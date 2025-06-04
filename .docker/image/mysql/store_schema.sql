/*
 Navicat Premium Dump SQL

 Source Server         : 小米商城项目
 Source Server Type    : MySQL
 Source Server Version : 90200 (9.2.0)
 Source Host           : localhost:3306
 Source Schema         : db_store

 Target Server Type    : MySQL
 Target Server Version : 90200 (9.2.0)
 File Encoding         : 65001

 Date: 04/06/2025 16:55:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for Admins
-- ----------------------------
DROP TABLE IF EXISTS `Admins`;
CREATE TABLE `Admins`
(
    `admin_id` int                                                           NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
    `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '管理员用户名',
    `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员密码（建议加密存储）',
    PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '后台管理员表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for Cart
-- ----------------------------
DROP TABLE IF EXISTS `Cart`;
CREATE TABLE `Cart`
(
    `cart_id`     int      NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
    `user_id`     int      NOT NULL COMMENT '用户ID',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '购物车创建时间',
    PRIMARY KEY (`cart_id`) USING BTREE,
    UNIQUE INDEX `fk_cart_user` (`user_id` ASC) USING BTREE,
    CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 19
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for CartItems
-- ----------------------------
DROP TABLE IF EXISTS `CartItems`;
CREATE TABLE `CartItems`
(
    `cart_item_id` int NOT NULL AUTO_INCREMENT COMMENT '购物车详情ID',
    `cart_id`      int NOT NULL COMMENT '购物车ID',
    `commodity_id` int NOT NULL COMMENT '商品ID',
    `quantity`     int NOT NULL COMMENT '添加数量',
    `is_checked`   int NULL DEFAULT 0 COMMENT '检查用户是否勾选商品',
    PRIMARY KEY (`cart_item_id`) USING BTREE,
    INDEX `fk_cartitems_cart` (`cart_id` ASC) USING BTREE,
    INDEX `fk_cartitems_product` (`commodity_id` ASC) USING BTREE,
    CONSTRAINT `CartItems_ibfk_1` FOREIGN KEY (`commodity_id`) REFERENCES `Commodities` (`commodity_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_cartitems_cart` FOREIGN KEY (`cart_id`) REFERENCES `Cart` (`cart_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 136
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车详情表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for Categories
-- ----------------------------
DROP TABLE IF EXISTS `Categories`;
CREATE TABLE `Categories`
(
    `category_id` int                                                           NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NULL COMMENT '分类描述',
    PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 27
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for Commodities
-- ----------------------------
DROP TABLE IF EXISTS `Commodities`;
CREATE TABLE `Commodities`
(
    `commodity_id`   int                                                             NOT NULL AUTO_INCREMENT,
    `product_id`     int                                                             NULL DEFAULT NULL,
    `sku`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL,
    `description`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL DEFAULT NULL,
    `stock`          int                                                             NULL DEFAULT NULL,
    `price`          decimal(10, 2)                                                  NULL DEFAULT NULL,
    `specifications` json                                                            NULL,
    `images`         varchar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    PRIMARY KEY (`commodity_id`) USING BTREE,
    INDEX `product_id` (`product_id` ASC) USING BTREE,
    CONSTRAINT `Commodities_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `Products` (`product_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 96
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for DeliveryAddress
-- ----------------------------
DROP TABLE IF EXISTS `DeliveryAddress`;
CREATE TABLE `DeliveryAddress`
(
    `id`         int                                                           NOT NULL AUTO_INCREMENT,
    `user_id`    int                                                           NULL DEFAULT NULL,
    `tag`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `address`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `phone`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
    `is_deleted` int                                                           NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_user_id` (`user_id` ASC) USING BTREE,
    CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 14
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for OrderItems
-- ----------------------------
DROP TABLE IF EXISTS `OrderItems`;
CREATE TABLE `OrderItems`
(
    `order_item_id` int            NOT NULL AUTO_INCREMENT COMMENT '订单详情ID',
    `order_id`      int            NOT NULL COMMENT '订单ID',
    `commodity_id`  int            NOT NULL COMMENT '商品ID',
    `quantity`      int            NOT NULL COMMENT '购买数量',
    `price`         decimal(10, 2) NOT NULL COMMENT '购买时的单价',
    PRIMARY KEY (`order_item_id`) USING BTREE,
    INDEX `fk_orderitems_order` (`order_id` ASC) USING BTREE,
    INDEX `fk_orderitems_product` (`commodity_id` ASC) USING BTREE,
    CONSTRAINT `fk_orderitems_order` FOREIGN KEY (`order_id`) REFERENCES `Orders` (`order_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `OrderItems_ibfk_1` FOREIGN KEY (`commodity_id`) REFERENCES `Commodities` (`commodity_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 83
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单详情表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for Orders
-- ----------------------------
DROP TABLE IF EXISTS `Orders`;
CREATE TABLE `Orders`
(
    `order_id`       int                                                          NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id`        int                                                          NOT NULL COMMENT '用户ID',
    `total_price`    decimal(10, 2)                                               NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
    `status`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '待发货' COMMENT '订单状态（如待支付、待发货、已发货、已完成、已取消等）',
    `payment_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '未支付' COMMENT '支付状态',
    `address_id`     int                                                          NOT NULL COMMENT '配送地址',
    `create_time`    datetime                                                     NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
    `update_time`    datetime                                                     NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单更新时间',
    PRIMARY KEY (`order_id`) USING BTREE,
    INDEX `fk_orders_user` (`user_id` ASC) USING BTREE,
    INDEX `fk_address_id` (`address_id` ASC) USING BTREE,
    CONSTRAINT `fk_address_id` FOREIGN KEY (`address_id`) REFERENCES `DeliveryAddress` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 245
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for PaymentRecords
-- ----------------------------
DROP TABLE IF EXISTS `PaymentRecords`;
CREATE TABLE `PaymentRecords`
(
    `payment_id`     int                                                           NOT NULL AUTO_INCREMENT COMMENT '支付记录ID',
    `order_id`       int                                                           NOT NULL COMMENT '订单ID',
    `payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '支付方式（如支付宝、微信、信用卡等）',
    `amount`         decimal(10, 2)                                                NOT NULL COMMENT '支付金额',
    `payment_time`   datetime                                                      NULL DEFAULT CURRENT_TIMESTAMP COMMENT '支付时间',
    `transaction_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方支付交易号',
    PRIMARY KEY (`payment_id`) USING BTREE,
    INDEX `fk_paymentrecords_order` (`order_id` ASC) USING BTREE,
    CONSTRAINT `fk_paymentrecords_order` FOREIGN KEY (`order_id`) REFERENCES `Orders` (`order_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付记录表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for Products
-- ----------------------------
DROP TABLE IF EXISTS `Products`;
CREATE TABLE `Products`
(
    `product_id`  int                                                            NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '商品名称',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          NULL COMMENT '商品描述',
    `category_id` int                                                            NOT NULL COMMENT '所属分类ID',
    `image_url`   varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片地址',
    `create_time` datetime                                                       NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
    PRIMARY KEY (`product_id`) USING BTREE,
    INDEX `fk_products_category` (`category_id` ASC) USING BTREE,
    CONSTRAINT `fk_products_category` FOREIGN KEY (`category_id`) REFERENCES `Categories` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 88
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ShippingRecords
-- ----------------------------
DROP TABLE IF EXISTS `ShippingRecords`;
CREATE TABLE `ShippingRecords`
(
    `shipping_id`      int                                                           NOT NULL AUTO_INCREMENT COMMENT '配送记录ID',
    `order_id`         int                                                           NOT NULL COMMENT '订单ID',
    `shipping_company` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物流公司',
    `tracking_number`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '快递单号',
    `shipping_time`    datetime                                                      NULL DEFAULT NULL COMMENT '发货时间',
    `delivery_time`    datetime                                                      NULL DEFAULT NULL COMMENT '签收时间',
    PRIMARY KEY (`shipping_id`) USING BTREE,
    INDEX `fk_shippingrecords_order` (`order_id` ASC) USING BTREE,
    CONSTRAINT `fk_shippingrecords_order` FOREIGN KEY (`order_id`) REFERENCES `Orders` (`order_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '配送记录表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for Users
-- ----------------------------
DROP TABLE IF EXISTS `Users`;
CREATE TABLE `Users`
(
    `user_id`     int                                                           NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '用户名',
    `nickname`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
    `password`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码（建议加密存储）',
    `email`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电子邮件',
    `phone`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '电话号码',
    `address`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认收货地址',
    `create_time` datetime                                                      NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `avatar_url`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
    PRIMARY KEY (`user_id`) USING BTREE,
    UNIQUE INDEX `username` (`username` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 18
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表'
  ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
