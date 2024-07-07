-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 07, 2024 at 08:31 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `shopapp`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `name`) VALUES
(1, 'Thú nhồi bông'),
(2, 'Nhựa'),
(3, 'Sách');

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE `comments` (
  `id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `comments`
--

INSERT INTO `comments` (`id`, `product_id`, `user_id`, `content`, `created_at`, `updated_at`) VALUES
(1, 1, 2, 'The content update', '2024-06-25 08:10:48', '2024-06-25 08:35:32');

-- --------------------------------------------------------

--
-- Table structure for table `coupons`
--

CREATE TABLE `coupons` (
  `id` int(11) NOT NULL,
  `code` varchar(50) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `coupons`
--

INSERT INTO `coupons` (`id`, `code`, `active`) VALUES
(1, 'HEAVEN', 1),
(2, 'DISCOUNT20', 1);

-- --------------------------------------------------------

--
-- Table structure for table `coupon_conditions`
--

CREATE TABLE `coupon_conditions` (
  `id` int(11) NOT NULL,
  `coupon_id` int(11) NOT NULL,
  `attribute` varchar(255) NOT NULL,
  `operator` varchar(10) NOT NULL,
  `value` varchar(255) NOT NULL,
  `discount_amount` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `coupon_conditions`
--

INSERT INTO `coupon_conditions` (`id`, `coupon_id`, `attribute`, `operator`, `value`, `discount_amount`) VALUES
(1, 1, 'minimum_amount', '>', '100', 10.00),
(2, 1, 'applicable_date', 'BETWEEN', '2024-12-28', 5.00),
(3, 1, 'minimum_amount', '>', '200', 20.00);

-- --------------------------------------------------------

--
-- Table structure for table `flyway_schema_history`
--

CREATE TABLE `flyway_schema_history` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT current_timestamp(),
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `flyway_schema_history`
--

INSERT INTO `flyway_schema_history` (`installed_rank`, `version`, `description`, `type`, `script`, `checksum`, `installed_by`, `installed_on`, `execution_time`, `success`) VALUES
(1, '0', '<< Flyway Baseline >>', 'BASELINE', '<< Flyway Baseline >>', NULL, 'root', '2024-06-22 02:33:35', 0, 1),
(2, '1', 'alter some tables', 'SQL', 'V1__alter_some_tables.sql', 1988689333, 'root', '2024-06-22 02:33:36', 849, 1),
(3, '2', 'change tokens', 'SQL', 'V2__change_tokens.sql', -139706514, 'root', '2024-06-22 03:42:39', 76, 1),
(4, '3', 'refresh tokens', 'SQL', 'V3__refresh_tokens.sql', 1258612817, 'root', '2024-06-22 05:27:49', 117, 1),
(5, '4', 'create comment table', 'SQL', 'V4__create_comment_table.sql', -2073042380, 'root', '2024-06-25 08:02:51', 56, 1),
(6, '5', 'create coupon table', 'SQL', 'V5__create_coupon_table.sql', -1508727558, 'root', '2024-06-25 17:21:04', 318, 1),
(7, '6', 'add email user table', 'SQL', 'V6__add_email_user_table.sql', -982120127, 'root', '2024-06-26 15:17:15', 27, 1),
(8, '7', 'alter order details drop coupon id', 'SQL', 'V7__alter_order_details_drop_coupon_id.sql', 2036710397, 'root', '2024-07-07 04:40:09', 48, 1);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `fullname` varchar(100) DEFAULT '',
  `email` varchar(100) DEFAULT '',
  `phone_number` varchar(20) NOT NULL,
  `address` varchar(200) NOT NULL,
  `note` varchar(100) DEFAULT '',
  `order_date` datetime DEFAULT current_timestamp(),
  `status` enum('pending','processing','shipped','delivered','cancelled') DEFAULT 'pending' COMMENT 'Trạng thái đơn hàng',
  `total_money` float DEFAULT NULL CHECK (`total_money` >= 0),
  `shipping_method` varchar(100) DEFAULT NULL,
  `shipping_address` varchar(200) DEFAULT NULL,
  `shipping_date` date DEFAULT NULL,
  `tracking_number` varchar(100) DEFAULT NULL,
  `payment_method` varchar(100) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  `coupon_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `user_id`, `fullname`, `email`, `phone_number`, `address`, `note`, `order_date`, `status`, `total_money`, `shipping_method`, `shipping_address`, `shipping_date`, `tracking_number`, `payment_method`, `active`, `coupon_id`) VALUES
(1, 2, 'Nguyễn Công Bình', 'binh19964@huce.edu.vn', '0971912772', 'Hà Nội', 'Hàng dễ vỡ, xin nhẹ tay', '2024-07-07 05:08:12', 'pending', 123.45, 'express', 'Nhà a, hàng phố', '2024-07-07', NULL, 'cod', 1, NULL),
(2, 2, 'Nguyễn Công Bình', 'binh19964@huce.edu.vn', '0971912772', 'Hà Nội', 'Hàng dễ vỡ, xin nhẹ tay', '2024-07-07 05:08:18', 'pending', 123.45, 'express', 'Nhà a, hàng phố', '2024-07-07', NULL, 'cod', 1, NULL),
(3, 2, 'Nguyễn Công Bình', 'binh19964@huce.edu.vn', '0971912772', 'Hà Nội', 'Hàng dễ vỡ, xin nhẹ tay', '2024-07-07 05:08:20', 'pending', 123.45, 'express', 'Nhà a, hàng phố', '2024-07-07', NULL, 'cod', 1, NULL),
(4, 2, 'Nguyễn Công Bình', 'binh19964@huce.edu.vn', '0971912772', 'Hà Nội', 'Hàng dễ vỡ, xin nhẹ tay', '2024-07-07 05:08:21', 'pending', 123.45, 'express', 'Nhà a, hàng phố', '2024-07-07', NULL, 'cod', 1, NULL),
(5, 2, 'Nguyễn Công Bình', 'binh19964@huce.edu.vn', '0971912772', 'Hà Nội', 'Hàng dễ vỡ, xin nhẹ tay', '2024-07-07 05:08:22', 'pending', 123.45, 'express', 'Nhà a, hàng phố', '2024-07-07', NULL, 'cod', 1, NULL),
(6, 2, 'Nguyễn Công Bình', 'binh19964@huce.edu.vn', '0971912772', 'Hà Nội', 'Hàng dễ vỡ, xin nhẹ tay', '2024-07-07 05:08:22', 'pending', 123.45, 'express', 'Nhà a, hàng phố', '2024-07-07', NULL, 'cod', 1, NULL),
(7, 2, 'Nguyễn Công Bình', 'binh19964@huce.edu.vn', '0971912772', 'Hà Nội', 'Hàng dễ vỡ, xin nhẹ tay', '2024-07-07 05:08:23', 'pending', 123.45, 'express', 'Nhà a, hàng phố', '2024-07-07', NULL, 'cod', 1, NULL),
(8, 2, 'Nguyễn Công Bình', 'binh19964@huce.edu.vn', '0971912772', 'Hà Nội', 'Hàng dễ vỡ, xin nhẹ tay', '2024-07-07 05:08:24', 'pending', 123.45, 'express', 'Nhà a, hàng phố', '2024-07-07', NULL, 'cod', 1, NULL),
(9, 2, 'Nguyễn Công Bình', 'binh19964@huce.edu.vn', '0971912772', 'Hà Nội', 'Hàng dễ vỡ, xin nhẹ tay', '2024-07-07 05:08:25', 'pending', 123.45, 'express', 'Nhà a, hàng phố', '2024-07-07', NULL, 'cod', 1, NULL),
(10, 2, 'Nguyễn Công Bình', 'binh19964@huce.edu.vn', '0971912772', 'Hà Nội', 'Hàng dễ vỡ, xin nhẹ tay', '2024-07-07 05:08:26', 'pending', 123.45, 'express', 'Nhà a, hàng phố', '2024-07-07', NULL, 'cod', 1, NULL),
(11, 2, 'Nguyễn Công Bình', 'binh19964@huce.edu.vn', '0971912772', 'Hà Nội', 'Hàng dễ vỡ, xin nhẹ tay', '2024-07-07 05:08:27', 'pending', 123.45, 'express', 'Nhà a, hàng phố', '2024-07-07', NULL, 'cod', 1, NULL),
(12, 2, 'Nguyễn Công Bình', 'binh19964@huce.edu.vn', '0971912772', 'Hà Nội', 'Hàng dễ vỡ, xin nhẹ tay', '2024-07-07 05:08:28', 'pending', 123.45, 'express', 'Nhà a, hàng phố', '2024-07-07', NULL, 'cod', 1, NULL),
(13, 2, 'Nguyễn Công Bình', 'binh19964@huce.edu.vn', '0971912772', 'Hà Nội', 'Hàng dễ vỡ, xin nhẹ tay', '2024-07-07 05:08:28', 'pending', 123.45, 'express', 'Nhà a, hàng phố', '2024-07-07', NULL, 'cod', 1, NULL),
(14, 2, 'Nguyễn Công Bình', 'binh19964@huce.edu.vn', '0971912772', 'Hà Nội', 'Hàng dễ vỡ, xin nhẹ tay', '2024-07-07 05:08:29', 'pending', 123.45, 'express', 'Nhà a, hàng phố', '2024-07-07', NULL, 'cod', 1, NULL),
(15, 2, 'Nguyễn Công Bình', 'binh19964@huce.edu.vn', '0971912772', 'Hà Nội', 'Hàng dễ vỡ, xin nhẹ tay', '2024-07-07 05:08:30', 'pending', 123.45, 'express', 'Nhà a, hàng phố', '2024-07-07', NULL, 'cod', 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `order_details`
--

CREATE TABLE `order_details` (
  `id` int(11) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `number_of_products` int(11) DEFAULT 1,
  `total_money` decimal(10,2) DEFAULT 0.00,
  `color` varchar(20) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_details`
--

INSERT INTO `order_details` (`id`, `order_id`, `product_id`, `price`, `number_of_products`, `total_money`, `color`) VALUES
(1, 1, 1, 1243.00, 4, 4972.00, NULL),
(2, 1, 2, 111.00, 5, 555.00, NULL),
(3, 1, 3, 63.40, 10, 634.00, NULL),
(4, 2, 1, 1243.00, 4, 4972.00, NULL),
(5, 2, 2, 111.00, 5, 555.00, NULL),
(6, 2, 3, 63.40, 10, 634.00, NULL),
(7, 3, 1, 1243.00, 4, 4972.00, NULL),
(8, 3, 2, 111.00, 5, 555.00, NULL),
(9, 3, 3, 63.40, 10, 634.00, NULL),
(10, 4, 1, 1243.00, 4, 4972.00, NULL),
(11, 4, 2, 111.00, 5, 555.00, NULL),
(12, 4, 3, 63.40, 10, 634.00, NULL),
(13, 5, 1, 1243.00, 4, 4972.00, NULL),
(14, 5, 2, 111.00, 5, 555.00, NULL),
(15, 5, 3, 63.40, 10, 634.00, NULL),
(16, 6, 1, 1243.00, 4, 4972.00, NULL),
(17, 6, 2, 111.00, 5, 555.00, NULL),
(18, 6, 3, 63.40, 10, 634.00, NULL),
(19, 7, 1, 1243.00, 4, 4972.00, NULL),
(20, 7, 2, 111.00, 5, 555.00, NULL),
(21, 7, 3, 63.40, 10, 634.00, NULL),
(22, 8, 1, 1243.00, 4, 4972.00, NULL),
(23, 8, 2, 111.00, 5, 555.00, NULL),
(24, 8, 3, 63.40, 10, 634.00, NULL),
(25, 9, 1, 1243.00, 4, 4972.00, NULL),
(26, 9, 2, 111.00, 5, 555.00, NULL),
(27, 9, 3, 63.40, 10, 634.00, NULL),
(28, 10, 1, 1243.00, 4, 4972.00, NULL),
(29, 10, 2, 111.00, 5, 555.00, NULL),
(30, 10, 3, 63.40, 10, 634.00, NULL),
(31, 11, 1, 1243.00, 4, 4972.00, NULL),
(32, 11, 2, 111.00, 5, 555.00, NULL),
(33, 11, 3, 63.40, 10, 634.00, NULL),
(34, 12, 1, 1243.00, 4, 4972.00, NULL),
(35, 12, 2, 111.00, 5, 555.00, NULL),
(36, 12, 3, 63.40, 10, 634.00, NULL),
(37, 13, 1, 1243.00, 4, 4972.00, NULL),
(38, 13, 2, 111.00, 5, 555.00, NULL),
(39, 13, 3, 63.40, 10, 634.00, NULL),
(40, 14, 1, 1243.00, 4, 4972.00, NULL),
(41, 14, 2, 111.00, 5, 555.00, NULL),
(42, 14, 3, 63.40, 10, 634.00, NULL),
(43, 15, 1, 1243.00, 4, 4972.00, NULL),
(44, 15, 2, 111.00, 5, 555.00, NULL),
(45, 15, 3, 63.40, 10, 634.00, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(350) DEFAULT NULL COMMENT 'Tên sản phẩm',
  `price` decimal(10,2) DEFAULT NULL,
  `thumbnail` varchar(255) DEFAULT NULL,
  `description` longtext DEFAULT '',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `price`, `thumbnail`, `description`, `created_at`, `updated_at`, `category_id`) VALUES
(1, 'Lion', 1243.00, 'king-lion.png', 'Lion', '2024-06-10 06:31:33', '2024-06-10 07:06:14', 1),
(2, 'Snake', 111.00, 'snake-cute.png', 'Snake', '2024-06-10 06:31:33', '2024-06-23 15:11:44', 3),
(3, 'Eagle', 63.40, 'eagle.png', 'Eagle', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 1),
(4, 'Killer whale', 36.50, 'killer-whale.png', 'Killer whale', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 1),
(5, 'Dolphin', 29.60, 'dolphin.png', 'Dolphin', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 1),
(6, 'Dog', 57.00, 'dog.png', 'Dog', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 1),
(7, 'Cat', 82.70, 'cat.png', 'Cat', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 1),
(8, 'Bear', 34.80, 'bear.png', 'Bear', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 1),
(9, 'Not found', 80.70, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 2),
(10, 'Not found', 25.00, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 1),
(11, 'Not found', 80.90, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 2),
(12, 'Not found', 62.40, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 2),
(13, 'Not found', 52.40, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 1),
(14, 'Not found', 52.90, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 2),
(15, 'Not found', 35.40, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 1),
(16, 'Not found', 49.60, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 2),
(17, 'Not found', 89.70, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 2),
(18, 'Not found', 14.90, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 2),
(19, 'Not found', 29.60, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 1),
(20, 'Not found', 80.50, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 1),
(21, 'Not found', 19.80, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 1),
(22, 'Not found', 19.40, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 1),
(23, 'Not found', 38.90, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 2),
(24, 'Not found', 87.60, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 2),
(25, 'Not found', 51.90, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 2),
(26, 'Not found', 64.50, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 2),
(27, 'Not found', 76.10, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 2),
(28, 'Not found', 11.00, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 2),
(29, 'Not found', 67.60, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 2),
(30, 'Not found', 44.20, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 2),
(31, 'Not found', 26.80, 'not-found.png', 'Enter content', '2024-06-10 06:31:33', '2024-06-10 06:31:33', 1),
(32, 'Not found', 18.50, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(33, 'Not found', 3.10, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(34, 'Not found', 21.20, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(35, 'Not found', 77.30, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(36, 'Not found', 62.70, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(37, 'Not found', 7.30, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(38, 'Not found', 58.70, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(39, 'Not found', 80.50, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(40, 'Not found', 50.10, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(41, 'Not found', 82.10, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(42, 'Not found', 71.80, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(43, 'Not found', 64.30, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(44, 'Not found', 65.40, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(45, 'Not found', 12.50, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(46, 'Not found', 60.30, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(47, 'Not found', 39.40, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(48, 'Not found', 61.80, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(49, 'Not found', 63.70, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(50, 'Not found', 69.60, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(51, 'Not found', 35.30, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(52, 'Not found', 60.30, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(53, 'Not found', 15.20, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(54, 'Not found', 87.00, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(55, 'Not found', 76.40, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(56, 'Not found', 56.30, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(57, 'Not found', 15.30, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(58, 'Not found', 87.10, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(59, 'Not found', 9.20, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(60, 'Not found', 43.90, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(61, 'Not found', 81.50, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(62, 'Not found', 40.80, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(63, 'Not found', 89.00, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(64, 'Not found', 86.60, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(65, 'Not found', 24.10, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(66, 'Not found', 9.00, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(67, 'Not found', 19.70, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(68, 'Not found', 9.30, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(69, 'Not found', 82.20, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(70, 'Not found', 65.50, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(71, 'Not found', 18.70, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(72, 'Not found', 12.50, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(73, 'Not found', 87.40, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(74, 'Not found', 957262.00, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(75, 'Not found', 39.60, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(76, 'Not found', 77.00, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(77, 'Not found', 82.30, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(78, 'Not found', 18.10, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(79, 'Not found', 21.30, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(80, 'Not found', 49.40, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(81, 'Not found', 72.90, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(82, 'Not found', 77.20, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(83, 'Not found', 30.60, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(84, 'Not found', 83.50, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(85, 'Not found', 768604.00, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(86, 'Not found', 42.40, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(87, 'Not found', 43.40, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(88, 'Not found', 14.40, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(89, 'Not found', 23.50, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(90, 'Not found', 18.80, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(91, 'Not found', 9.10, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(92, 'Not found', 71.90, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(93, 'Not found', 16.00, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(94, 'Not found', 82.00, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(95, 'Not found', 47.00, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(96, 'Not found', 60.90, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(97, 'Not found', 80.80, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(98, 'Not found', 63.10, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 1),
(99, 'Not found', 36.60, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2),
(100, 'Not found', 60.00, 'not-found.png', 'Enter content', '2024-06-10 06:31:34', '2024-06-10 06:31:34', 2);

-- --------------------------------------------------------

--
-- Table structure for table `product_images`
--

CREATE TABLE `product_images` (
  `id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `image_url` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product_images`
--

INSERT INTO `product_images` (`id`, `product_id`, `image_url`) VALUES
(1, 10, NULL),
(2, 10, 'king-lion.png'),
(3, 2, 'snake-cute.png'),
(4, 2, NULL),
(5, 2, 'snake-cute.png'),
(6, 2, NULL),
(7, 2, 'snake-cute.png'),
(8, 3, 'eagle.png'),
(9, 3, NULL),
(10, 3, 'eagle.png'),
(11, 3, NULL),
(12, 3, 'eagle.png'),
(13, 1, 'not-found.png'),
(14, 1, 'not-found.png'),
(15, 1, 'not-found.png');

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'USER'),
(2, 'ADMIN');

-- --------------------------------------------------------

--
-- Table structure for table `social_accounts`
--

CREATE TABLE `social_accounts` (
  `id` int(11) NOT NULL,
  `provider` varchar(20) NOT NULL COMMENT 'Tên nhà social network',
  `provider_id` varchar(50) NOT NULL,
  `email` varchar(150) NOT NULL COMMENT 'Email tài khoản',
  `name` varchar(100) NOT NULL COMMENT 'Tên người dùng',
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tokens`
--

CREATE TABLE `tokens` (
  `id` int(11) NOT NULL,
  `token` varchar(255) NOT NULL,
  `token_type` varchar(50) NOT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `revoked` tinyint(1) NOT NULL,
  `expired` tinyint(1) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `is_mobile` tinyint(1) DEFAULT 0,
  `refresh_token` varchar(255) DEFAULT '',
  `refresh_expiration_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tokens`
--

INSERT INTO `tokens` (`id`, `token`, `token_type`, `expiration_date`, `revoked`, `expired`, `user_id`, `is_mobile`, `refresh_token`, `refresh_expiration_date`) VALUES
(10, 'eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZU51bWJlciI6IjA5NzE5MTI3NzIiLCJ1c2VySWQiOjIsInN1YiI6IjA5NzE5MTI3NzIiLCJleHAiOjE3MjIwMTEwNDN9.iGh234ygcQGQcrNoEQvXTUObYrrR39RQ90K-UBaCp88', 'Bearer', '2024-07-26 16:24:03', 0, 0, 2, 0, '323264da-cf91-4309-a3cb-3e646979d5b8', '2024-08-25 16:24:03'),
(11, 'eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZU51bWJlciI6IjA5NzE5MTI3NzIiLCJ1c2VySWQiOjIsInN1YiI6IjA5NzE5MTI3NzIiLCJleHAiOjE3MjIwMTEzOTd9.8GZq-V_zq8Eza7-u3G7Q4zwVTsesDki_u1arxMP2U3Y', 'Bearer', '2024-07-26 16:30:02', 0, 0, 2, 0, '12abbeb4-5685-4fca-9b37-8a79202181f4', '2024-08-25 16:30:02'),
(30, 'eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZU51bWJlciI6IjA5NzE5MTI3NzYiLCJ1c2VySWQiOjEsInN1YiI6IjA5NzE5MTI3NzYiLCJleHAiOjE3MjI5MDkyMzB9.MZ7Qv-dk2VJY-wKqTJ1_bhPuT7IpwhbgoAiJaTmjujA', 'Bearer', '2024-08-06 01:53:50', 0, 0, 1, 0, 'ec8a0356-14c7-49f5-96a6-f5015dd58a63', '2024-09-05 01:53:50'),
(31, 'eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZU51bWJlciI6IjA5NzE5MTI3NzYiLCJ1c2VySWQiOjEsInN1YiI6IjA5NzE5MTI3NzYiLCJleHAiOjE3MjI5MDkzMjN9.gRBn0j3hzDV8Rf2Y8AxDxRT3CyOrIYxnbAKI4Vur2DU', 'Bearer', '2024-08-06 01:55:23', 0, 0, 1, 0, 'ef330e91-8912-4f27-b162-758d27640fea', '2024-09-05 01:55:23'),
(32, 'eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZU51bWJlciI6IjA5NzE5MTI3NzIiLCJ1c2VySWQiOjIsInN1YiI6IjA5NzE5MTI3NzIiLCJleHAiOjE3MjI5MDkzNzB9.xz52mE4x1WZX8mjCfoXBM9LXIgWZP2ZwpYNgmfYwSUA', 'Bearer', '2024-08-06 01:56:10', 0, 0, 2, 0, 'ba3c06c6-7ab1-4dd6-a27d-742e44820c65', '2024-09-05 01:56:10'),
(33, 'eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZU51bWJlciI6IjA5NzE5MTI3NzYiLCJ1c2VySWQiOjEsInN1YiI6IjA5NzE5MTI3NzYiLCJleHAiOjE3MjI5MDk0OTV9.-HWo-TJtxY5LO5AHffCzbt33WdLCMuHw-bHCvCa7v14', 'Bearer', '2024-08-06 01:58:15', 0, 0, 1, 0, '66c43751-f3a2-4ca1-8765-caf1cf53ad70', '2024-09-05 01:58:15');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `fullname` varchar(100) DEFAULT '',
  `phone_number` varchar(15) DEFAULT NULL,
  `address` varchar(200) DEFAULT '',
  `password` char(60) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT 1,
  `date_of_birth` date DEFAULT NULL,
  `facebook_account_id` int(11) DEFAULT 0,
  `google_account_id` int(11) DEFAULT 0,
  `role_id` int(11) DEFAULT 1,
  `email` varchar(255) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `fullname`, `phone_number`, `address`, `password`, `created_at`, `updated_at`, `is_active`, `date_of_birth`, `facebook_account_id`, `google_account_id`, `role_id`, `email`) VALUES
(1, 'Admin 1', '0971912776', 'Hà Đông, Hà Nội', '$2a$10$oBlXJ.v1fFLFTUpMGa3xwOIrWEOTpcvWfxk4qx3bGa43NNlky7g4W', '2024-06-10 20:18:26', '2024-06-10 20:18:26', 1, '2001-03-08', 0, 0, 2, 'binhsna@gmail.com'),
(2, 'Nguyễn Công Bình', '0971912772', 'Nhà a, ngõ b', '$2a$10$kShGPMz6OYC5HlTXWDCn4eCoUZLYgktldr4nHA5ks4mwHqkt3r7pq', '2024-06-13 11:35:31', '2024-06-25 06:45:35', 1, '2001-03-04', 1, 1, 1, 'binh19964@huce.edu.vn');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `product_id` (`product_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `coupons`
--
ALTER TABLE `coupons`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `coupon_conditions`
--
ALTER TABLE `coupon_conditions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `coupon_id` (`coupon_id`);

--
-- Indexes for table `flyway_schema_history`
--
ALTER TABLE `flyway_schema_history`
  ADD PRIMARY KEY (`installed_rank`),
  ADD KEY `flyway_schema_history_s_idx` (`success`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `fk_orders_coupon` (`coupon_id`);

--
-- Indexes for table `order_details`
--
ALTER TABLE `order_details`
  ADD PRIMARY KEY (`id`),
  ADD KEY `product_id` (`product_id`),
  ADD KEY `order_id` (`order_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `product_images`
--
ALTER TABLE `product_images`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_product_images_product_id` (`product_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `social_accounts`
--
ALTER TABLE `social_accounts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `tokens`
--
ALTER TABLE `tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `token` (`token`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD KEY `role_id` (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `comments`
--
ALTER TABLE `comments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `coupons`
--
ALTER TABLE `coupons`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `coupon_conditions`
--
ALTER TABLE `coupon_conditions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `order_details`
--
ALTER TABLE `order_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5781;

--
-- AUTO_INCREMENT for table `product_images`
--
ALTER TABLE `product_images`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `social_accounts`
--
ALTER TABLE `social_accounts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tokens`
--
ALTER TABLE `tokens`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  ADD CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `coupon_conditions`
--
ALTER TABLE `coupon_conditions`
  ADD CONSTRAINT `coupon_conditions_ibfk_1` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `fk_orders_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`);

--
-- Constraints for table `order_details`
--
ALTER TABLE `order_details`
  ADD CONSTRAINT `order_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  ADD CONSTRAINT `order_details_ibfk_3` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);

--
-- Constraints for table `product_images`
--
ALTER TABLE `product_images`
  ADD CONSTRAINT `fk_product_images_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `social_accounts`
--
ALTER TABLE `social_accounts`
  ADD CONSTRAINT `social_accounts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `tokens`
--
ALTER TABLE `tokens`
  ADD CONSTRAINT `tokens_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
