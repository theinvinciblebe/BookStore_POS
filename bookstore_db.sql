/*
 Navicat Premium Dump SQL

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 100432 (10.4.32-MariaDB)
 Source Host           : localhost:3306
 Source Schema         : bookstore_db

 Target Server Type    : MySQL
 Target Server Version : 100432 (10.4.32-MariaDB)
 File Encoding         : 65001

 Date: 06/11/2024 16:31:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tblauthors
-- ----------------------------
DROP TABLE IF EXISTS `tblauthors`;
CREATE TABLE `tblauthors`  (
  `author_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`author_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tblauthors
-- ----------------------------
INSERT INTO `tblauthors` VALUES (1, 'Charlie', 'Donlea');
INSERT INTO `tblauthors` VALUES (2, 'Freidaa', 'McFadden');
INSERT INTO `tblauthors` VALUES (3, 'Bob', 'Woodward');
INSERT INTO `tblauthors` VALUES (4, 'Louise', 'Penny');
INSERT INTO `tblauthors` VALUES (5, 'Casey', 'Means, MD');
INSERT INTO `tblauthors` VALUES (6, 'Calley', 'Means');
INSERT INTO `tblauthors` VALUES (7, 'Ennis', 'Jemmy');
INSERT INTO `tblauthors` VALUES (8, 'Joe', 'Giorello');
INSERT INTO `tblauthors` VALUES (9, 'Sibella', 'Giorello');
INSERT INTO `tblauthors` VALUES (10, 'Dav', 'Pilkey');

-- ----------------------------
-- Table structure for tblbooks
-- ----------------------------
DROP TABLE IF EXISTS `tblbooks`;
CREATE TABLE `tblbooks`  (
  `book_id` int NOT NULL AUTO_INCREMENT,
  `isbn` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`book_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tblbooks
-- ----------------------------
INSERT INTO `tblbooks` VALUES (1, '1496736982', 'The Girl Who Was Taken', 'D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\product_image\\The Girl Who Was Taken.jpg');
INSERT INTO `tblbooks` VALUES (6, NULL, 'The Boyfriend', 'D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\product_image\\The Boyfriend.jpg');
INSERT INTO `tblbooks` VALUES (8, NULL, 'Great Battle For Boys', 'D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\product_image\\Great Battles For Boys.jpg');
INSERT INTO `tblbooks` VALUES (9, NULL, 'WAR', 'D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\product_image\\War.jpg');
INSERT INTO `tblbooks` VALUES (14, NULL, 'Dog Man Big Jim Begins', 'D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\product_image\\Dog Man Big Jim Begins.jpg');
INSERT INTO `tblbooks` VALUES (15, NULL, 'The Grey Wolf', 'D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\product_image\\The Grey Wolf.jpg');
INSERT INTO `tblbooks` VALUES (16, NULL, 'WW2 Inspiring Stories for kids', 'D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\product_image\\WW2 Inspiring Stories for kids.jpg');
INSERT INTO `tblbooks` VALUES (17, NULL, 'Good Energy', 'D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\product_image\\Good Energy.jpg');

-- ----------------------------
-- Table structure for tblbooks_authors
-- ----------------------------
DROP TABLE IF EXISTS `tblbooks_authors`;
CREATE TABLE `tblbooks_authors`  (
  `book_id` int NOT NULL,
  `author_id` int NULL DEFAULT NULL,
  INDEX `author_id`(`author_id` ASC) USING BTREE,
  INDEX `book_author`(`book_id` ASC) USING BTREE,
  CONSTRAINT `author_id` FOREIGN KEY (`author_id`) REFERENCES `tblauthors` (`author_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `book_author` FOREIGN KEY (`book_id`) REFERENCES `tblbooks` (`book_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tblbooks_authors
-- ----------------------------
INSERT INTO `tblbooks_authors` VALUES (1, 1);
INSERT INTO `tblbooks_authors` VALUES (6, 2);
INSERT INTO `tblbooks_authors` VALUES (8, 2);
INSERT INTO `tblbooks_authors` VALUES (9, 1);
INSERT INTO `tblbooks_authors` VALUES (14, 10);
INSERT INTO `tblbooks_authors` VALUES (15, 4);
INSERT INTO `tblbooks_authors` VALUES (16, 7);
INSERT INTO `tblbooks_authors` VALUES (17, 5);

-- ----------------------------
-- Table structure for tblbooks_genres
-- ----------------------------
DROP TABLE IF EXISTS `tblbooks_genres`;
CREATE TABLE `tblbooks_genres`  (
  `book_id` int NOT NULL,
  `genre_id` int NULL DEFAULT NULL,
  INDEX `bookid`(`book_id` ASC) USING BTREE,
  INDEX `genreid`(`genre_id` ASC) USING BTREE,
  CONSTRAINT `bookid` FOREIGN KEY (`book_id`) REFERENCES `tblbooks` (`book_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `genreid` FOREIGN KEY (`genre_id`) REFERENCES `tblgenres` (`genre_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tblbooks_genres
-- ----------------------------
INSERT INTO `tblbooks_genres` VALUES (1, 2);
INSERT INTO `tblbooks_genres` VALUES (6, 1);
INSERT INTO `tblbooks_genres` VALUES (8, 2);
INSERT INTO `tblbooks_genres` VALUES (9, 1);
INSERT INTO `tblbooks_genres` VALUES (14, 5);
INSERT INTO `tblbooks_genres` VALUES (15, 6);
INSERT INTO `tblbooks_genres` VALUES (16, 6);
INSERT INTO `tblbooks_genres` VALUES (17, 2);

-- ----------------------------
-- Table structure for tblexpense_details
-- ----------------------------
DROP TABLE IF EXISTS `tblexpense_details`;
CREATE TABLE `tblexpense_details`  (
  `expense_detail_id` int NOT NULL AUTO_INCREMENT,
  `expense_id` int NULL DEFAULT NULL,
  `item_descirption` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `total` decimal(10, 2) NULL DEFAULT NULL,
  `qty` int NULL DEFAULT NULL,
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`expense_detail_id`) USING BTREE,
  INDEX `expenseID`(`expense_id` ASC) USING BTREE,
  CONSTRAINT `expenseID` FOREIGN KEY (`expense_id`) REFERENCES `tblexpenses` (`expense_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tblexpense_details
-- ----------------------------

-- ----------------------------
-- Table structure for tblexpenses
-- ----------------------------
DROP TABLE IF EXISTS `tblexpenses`;
CREATE TABLE `tblexpenses`  (
  `expense_id` int NOT NULL AUTO_INCREMENT,
  `expense_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `amount` decimal(10, 2) NULL DEFAULT NULL,
  `expense_date` datetime NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`expense_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tblexpenses
-- ----------------------------
INSERT INTO `tblexpenses` VALUES (1, 'Staff-Related Expenses', 2500.00, '2024-11-04 00:00:00', 'Salary for Staff Meiching');
INSERT INTO `tblexpenses` VALUES (4, 'Operational Expenses', 20.00, '2024-11-05 00:00:00', 'Utility');
INSERT INTO `tblexpenses` VALUES (5, 'Rent and Utilities', 2500.00, '2024-11-03 00:00:00', 'Rent');
INSERT INTO `tblexpenses` VALUES (7, 'Inventory Expenses', 720.00, '2024-10-19 00:00:00', 'Supplier: TheForestBook, Title: Dog Man Big Jim Begins');
INSERT INTO `tblexpenses` VALUES (8, 'Inventory Expenses', 2700.00, '2024-11-05 00:00:00', 'Supplier: JingJang, Title: The Grey Wolf');
INSERT INTO `tblexpenses` VALUES (9, 'Inventory Expenses', 3800.00, '2024-11-06 00:00:00', 'Supplier: JingJang, Title: WW2 Inspiring Stories for kids');
INSERT INTO `tblexpenses` VALUES (10, 'Inventory Expenses', 2100.00, '2024-10-19 00:00:00', 'Supplier: JingJang, Title: Good Energy');

-- ----------------------------
-- Table structure for tblgenres
-- ----------------------------
DROP TABLE IF EXISTS `tblgenres`;
CREATE TABLE `tblgenres`  (
  `genre_id` int NOT NULL AUTO_INCREMENT,
  `genre_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`genre_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tblgenres
-- ----------------------------
INSERT INTO `tblgenres` VALUES (1, 'Ficton');
INSERT INTO `tblgenres` VALUES (2, 'Fantasy');
INSERT INTO `tblgenres` VALUES (3, 'History');
INSERT INTO `tblgenres` VALUES (4, 'War');
INSERT INTO `tblgenres` VALUES (5, 'Cartoon');
INSERT INTO `tblgenres` VALUES (6, 'Super Hero');
INSERT INTO `tblgenres` VALUES (7, 'Romance');

-- ----------------------------
-- Table structure for tblimports
-- ----------------------------
DROP TABLE IF EXISTS `tblimports`;
CREATE TABLE `tblimports`  (
  `import_id` int NOT NULL AUTO_INCREMENT,
  `supplier_id` int NULL DEFAULT NULL,
  `Import_date` date NULL DEFAULT NULL,
  `total_cost` decimal(10, 2) NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`import_id`) USING BTREE,
  INDEX `SuppID`(`supplier_id` ASC) USING BTREE,
  CONSTRAINT `SuppID` FOREIGN KEY (`supplier_id`) REFERENCES `tblsuppliers` (`supplier_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tblimports
-- ----------------------------
INSERT INTO `tblimports` VALUES (1, 1, '2024-10-11', 2000.00, 'paid');
INSERT INTO `tblimports` VALUES (2, 1, '2024-10-11', 400.00, NULL);
INSERT INTO `tblimports` VALUES (3, 1, '2024-10-03', 350.00, NULL);
INSERT INTO `tblimports` VALUES (4, 1, '2024-10-13', 111.00, NULL);
INSERT INTO `tblimports` VALUES (5, 1, '2024-10-16', 321.00, NULL);
INSERT INTO `tblimports` VALUES (6, 1, '2024-10-15', 7000.00, NULL);
INSERT INTO `tblimports` VALUES (7, 1, '2024-10-14', 1500.21, NULL);
INSERT INTO `tblimports` VALUES (8, 1, '2024-10-18', 2500.00, NULL);
INSERT INTO `tblimports` VALUES (9, 1, '2024-10-19', 444.00, NULL);
INSERT INTO `tblimports` VALUES (13, 3, '2024-11-04', 2392.00, NULL);
INSERT INTO `tblimports` VALUES (14, 1, '2024-10-19', 720.00, NULL);
INSERT INTO `tblimports` VALUES (15, 3, '2024-11-05', 2700.00, NULL);
INSERT INTO `tblimports` VALUES (16, 3, '2024-11-06', 5700.00, NULL);
INSERT INTO `tblimports` VALUES (17, 3, '2024-10-19', 2100.00, NULL);

-- ----------------------------
-- Table structure for tblimports_details
-- ----------------------------
DROP TABLE IF EXISTS `tblimports_details`;
CREATE TABLE `tblimports_details`  (
  `import_id` int NOT NULL,
  `book_id` int NULL DEFAULT NULL,
  `qty_import` int NULL DEFAULT NULL,
  `import_price` decimal(10, 2) NULL DEFAULT NULL,
  INDEX `ImportID`(`import_id` ASC) USING BTREE,
  INDEX `Import_BookID`(`book_id` ASC) USING BTREE,
  CONSTRAINT `ImportID` FOREIGN KEY (`import_id`) REFERENCES `tblimports` (`import_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Import_BookID` FOREIGN KEY (`book_id`) REFERENCES `tblbooks` (`book_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tblimports_details
-- ----------------------------
INSERT INTO `tblimports_details` VALUES (1, 1, 220, 8.50);
INSERT INTO `tblimports_details` VALUES (6, 6, 320, 10.00);
INSERT INTO `tblimports_details` VALUES (8, 8, 40, 8.70);
INSERT INTO `tblimports_details` VALUES (9, 9, 162, 8.90);
INSERT INTO `tblimports_details` VALUES (14, 14, 40, 8.00);
INSERT INTO `tblimports_details` VALUES (15, 15, 100, 9.00);
INSERT INTO `tblimports_details` VALUES (16, 16, 300, 19.00);
INSERT INTO `tblimports_details` VALUES (17, 17, 50, 7.00);

-- ----------------------------
-- Table structure for tblincomes
-- ----------------------------
DROP TABLE IF EXISTS `tblincomes`;
CREATE TABLE `tblincomes`  (
  `income_id` int NOT NULL AUTO_INCREMENT,
  `income_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `amount` decimal(10, 2) NULL DEFAULT NULL,
  `income_date` datetime NULL DEFAULT NULL,
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`income_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tblincomes
-- ----------------------------
INSERT INTO `tblincomes` VALUES (5, 'Book Order Sale', 44.55, '2024-11-04 22:28:58', 'Order of 2 books');
INSERT INTO `tblincomes` VALUES (6, 'Book Order Sale', 46.48, '2024-11-04 22:34:04', 'Order of 4 books');
INSERT INTO `tblincomes` VALUES (7, 'Book Order Sale', 71.50, '2024-11-04 22:36:29', 'Order of 3 books');
INSERT INTO `tblincomes` VALUES (8, 'Book Order Sale', 18.70, '2024-11-04 23:01:25', 'Order of 1 books');
INSERT INTO `tblincomes` VALUES (9, 'Book Order Sale', 45.10, '2024-11-04 23:03:20', 'Order of 2 books');
INSERT INTO `tblincomes` VALUES (10, 'Book Order Sale', 44.00, '2024-11-05 20:18:50', 'Order of 2 books');
INSERT INTO `tblincomes` VALUES (11, 'Book Order Sale', 89.10, '2024-11-05 20:21:27', 'Order of 4 books');
INSERT INTO `tblincomes` VALUES (12, 'Book Order Sale', 88.00, '2024-11-06 15:59:55', 'Order of 1 books');
INSERT INTO `tblincomes` VALUES (13, 'Book Order Sale', 45.10, '2024-11-06 16:02:35', 'Order of 2 books');

-- ----------------------------
-- Table structure for tblorderdetails
-- ----------------------------
DROP TABLE IF EXISTS `tblorderdetails`;
CREATE TABLE `tblorderdetails`  (
  `order_id` int NOT NULL,
  `book_id` int NOT NULL,
  `qty` int NULL DEFAULT NULL,
  `quoted_price` decimal(10, 2) NULL DEFAULT NULL,
  INDEX `OrderID`(`order_id` ASC) USING BTREE,
  INDEX `booksid`(`book_id` ASC) USING BTREE,
  CONSTRAINT `OrderID` FOREIGN KEY (`order_id`) REFERENCES `tblorders` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `booksid` FOREIGN KEY (`book_id`) REFERENCES `tblbooks` (`book_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tblorderdetails
-- ----------------------------
INSERT INTO `tblorderdetails` VALUES (1, 1, 1, 8.25);
INSERT INTO `tblorderdetails` VALUES (1, 6, 1, 12.00);
INSERT INTO `tblorderdetails` VALUES (2, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (2, 6, 3, 36.00);
INSERT INTO `tblorderdetails` VALUES (4, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (4, 6, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (6, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (6, 6, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (9, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (11, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (12, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (12, 6, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (30, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (30, 6, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (31, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (31, 6, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (32, 1, 1, 8.25);
INSERT INTO `tblorderdetails` VALUES (32, 6, 1, 12.00);
INSERT INTO `tblorderdetails` VALUES (33, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (33, 6, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (34, 6, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (34, 9, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (35, 8, 2, 20.00);
INSERT INTO `tblorderdetails` VALUES (35, 6, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (36, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (36, 6, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (37, 9, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (37, 8, 2, 20.00);
INSERT INTO `tblorderdetails` VALUES (38, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (38, 6, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (39, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (39, 9, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (40, 1, 1, 8.25);
INSERT INTO `tblorderdetails` VALUES (40, 6, 1, 12.00);
INSERT INTO `tblorderdetails` VALUES (40, 9, 1, 12.00);
INSERT INTO `tblorderdetails` VALUES (40, 8, 1, 10.00);
INSERT INTO `tblorderdetails` VALUES (41, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (41, 6, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (41, 9, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (42, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (43, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (43, 6, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (44, 14, 2, 20.00);
INSERT INTO `tblorderdetails` VALUES (44, 15, 2, 20.00);
INSERT INTO `tblorderdetails` VALUES (45, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (45, 9, 2, 24.00);
INSERT INTO `tblorderdetails` VALUES (45, 14, 2, 20.00);
INSERT INTO `tblorderdetails` VALUES (45, 15, 2, 20.00);
INSERT INTO `tblorderdetails` VALUES (46, 17, 10, 80.00);
INSERT INTO `tblorderdetails` VALUES (47, 1, 2, 16.50);
INSERT INTO `tblorderdetails` VALUES (47, 6, 2, 24.00);

-- ----------------------------
-- Table structure for tblorders
-- ----------------------------
DROP TABLE IF EXISTS `tblorders`;
CREATE TABLE `tblorders`  (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `order_date` datetime NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  `amount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`) USING BTREE,
  INDEX `userID`(`user_id` ASC) USING BTREE,
  CONSTRAINT `userID` FOREIGN KEY (`user_id`) REFERENCES `tblusers` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tblorders
-- ----------------------------
INSERT INTO `tblorders` VALUES (1, '2024-10-18 10:21:54', 2, '46.475', 'Completed');
INSERT INTO `tblorders` VALUES (2, '2024-10-25 11:31:56', 2, '57.75', 'Completed');
INSERT INTO `tblorders` VALUES (4, '2024-10-30 17:37:38', 2, '44.55', 'Completed');
INSERT INTO `tblorders` VALUES (6, '2024-10-30 17:44:12', 2, '44.55', 'Completed');
INSERT INTO `tblorders` VALUES (7, '2024-10-30 18:00:10', 2, '22.0', 'Completed');
INSERT INTO `tblorders` VALUES (9, '2024-10-31 08:45:31', 2, '18.15', 'Completed');
INSERT INTO `tblorders` VALUES (11, '2024-10-31 09:12:14', 2, '18.15', 'Completed');
INSERT INTO `tblorders` VALUES (12, '2024-10-31 15:28:33', 2, '44.55', 'Completed');
INSERT INTO `tblorders` VALUES (14, '2024-10-31 15:56:11', 2, '92.95', 'Completed');
INSERT INTO `tblorders` VALUES (15, '2024-10-31 16:13:31', 2, '22.0', 'Completed');
INSERT INTO `tblorders` VALUES (16, '2024-11-01 11:06:24', 2, '18.975', 'Completed');
INSERT INTO `tblorders` VALUES (17, '2024-11-01 11:32:04', 2, '44.55', 'Completed');
INSERT INTO `tblorders` VALUES (21, '2024-11-01 16:05:13', 2, '18.15', 'Completed');
INSERT INTO `tblorders` VALUES (22, '2024-11-01 16:24:08', 2, '104.775', 'Completed');
INSERT INTO `tblorders` VALUES (27, '2024-11-01 16:52:17', 1, '18.15', 'Completed');
INSERT INTO `tblorders` VALUES (28, '2024-11-01 17:08:11', 2, '26.4', 'Completed');
INSERT INTO `tblorders` VALUES (29, '2024-11-01 17:12:59', 2, '66.0', 'Completed');
INSERT INTO `tblorders` VALUES (30, '2024-11-01 17:25:58', 1, '66.55', 'Completed');
INSERT INTO `tblorders` VALUES (31, '2024-11-02 17:24:14', 2, '44.55', 'Completed');
INSERT INTO `tblorders` VALUES (32, '2024-11-02 17:51:05', 2, '22.275', 'Completed');
INSERT INTO `tblorders` VALUES (33, '2024-11-02 17:55:07', 2, '44.55', 'Completed');
INSERT INTO `tblorders` VALUES (34, '2024-11-02 18:01:02', 2, '52.8', 'Completed');
INSERT INTO `tblorders` VALUES (35, '2024-11-04 21:53:13', 2, '48.4', 'Completed');
INSERT INTO `tblorders` VALUES (36, '2024-11-04 22:16:01', 1, '44.55', 'Completed');
INSERT INTO `tblorders` VALUES (37, '2024-11-04 22:17:52', 2, '48.4', 'Completed');
INSERT INTO `tblorders` VALUES (38, '2024-11-04 22:18:23', 2, '44.55', 'Completed');
INSERT INTO `tblorders` VALUES (39, '2024-11-04 22:28:56', 2, '44.55', 'Completed');
INSERT INTO `tblorders` VALUES (40, '2024-11-04 22:34:03', 2, '46.475', 'Completed');
INSERT INTO `tblorders` VALUES (41, '2024-11-04 22:36:28', 2, '70.95', 'Completed');
INSERT INTO `tblorders` VALUES (42, '2024-11-04 23:01:24', 1, '18.15', 'Completed');
INSERT INTO `tblorders` VALUES (43, '2024-11-04 23:03:18', 1, '44.55', 'Completed');
INSERT INTO `tblorders` VALUES (44, '2024-11-05 20:18:49', 1, '44.0', 'Completed');
INSERT INTO `tblorders` VALUES (45, '2024-11-05 20:21:26', 2, '88.55', 'Completed');
INSERT INTO `tblorders` VALUES (46, '2024-11-06 15:59:54', 1, '88.0', 'Completed');
INSERT INTO `tblorders` VALUES (47, '2024-11-06 16:02:35', 2, '44.55', 'Completed');

-- ----------------------------
-- Table structure for tblsales
-- ----------------------------
DROP TABLE IF EXISTS `tblsales`;
CREATE TABLE `tblsales`  (
  `sale_id` int NOT NULL AUTO_INCREMENT,
  `book_id` int NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  `qty_sold` int NULL DEFAULT NULL,
  `sale_price` decimal(10, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`sale_id`) USING BTREE,
  INDEX `user_sales`(`user_id` ASC) USING BTREE,
  INDEX `book4sales`(`book_id` ASC) USING BTREE,
  CONSTRAINT `book4sales` FOREIGN KEY (`book_id`) REFERENCES `tblbooks` (`book_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_sales` FOREIGN KEY (`user_id`) REFERENCES `tblusers` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tblsales
-- ----------------------------
INSERT INTO `tblsales` VALUES (1, 1, 1, 89, 8.25);
INSERT INTO `tblsales` VALUES (2, 6, NULL, 93, 12.00);
INSERT INTO `tblsales` VALUES (6, 9, NULL, 55, 12.00);
INSERT INTO `tblsales` VALUES (7, 8, NULL, 9, 10.00);
INSERT INTO `tblsales` VALUES (11, 14, NULL, 46, 10.00);
INSERT INTO `tblsales` VALUES (12, 15, NULL, 196, 10.00);
INSERT INTO `tblsales` VALUES (13, 17, NULL, 240, 8.00);

-- ----------------------------
-- Table structure for tblsuppliers
-- ----------------------------
DROP TABLE IF EXISTS `tblsuppliers`;
CREATE TABLE `tblsuppliers`  (
  `supplier_id` int NOT NULL AUTO_INCREMENT,
  `company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `company_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supplier_tel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`supplier_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tblsuppliers
-- ----------------------------
INSERT INTO `tblsuppliers` VALUES (1, 'TheForestBook', 'TK, PP', 'Sochea', '091234441');
INSERT INTO `tblsuppliers` VALUES (2, 'Amazone', 'USA', 'Hea Bee', '09123444');
INSERT INTO `tblsuppliers` VALUES (3, 'JingJang', 'PP', 'Long Sarith', '092144441');

-- ----------------------------
-- Table structure for tblusers
-- ----------------------------
DROP TABLE IF EXISTS `tblusers`;
CREATE TABLE `tblusers`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tblusers
-- ----------------------------
INSERT INTO `tblusers` VALUES (1, 'Theng Sothea', 'admin', '123', 'admin');
INSERT INTO `tblusers` VALUES (2, 'Hourt Meijing', 'staff', '123', 'staff');
INSERT INTO `tblusers` VALUES (15, 'Chamrong', 'Thea', '123', 'admin');

SET FOREIGN_KEY_CHECKS = 1;
