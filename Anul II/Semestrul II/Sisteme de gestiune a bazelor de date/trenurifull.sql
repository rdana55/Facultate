-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 26, 2024 at 12:33 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `trenurifull`
--

-- --------------------------------------------------------

--
-- Table structure for table `trains`
--

CREATE TABLE `trains` (
  `trainNo` int(11) NOT NULL,
  `trainType` varchar(50) NOT NULL,
  `start` varchar(50) NOT NULL,
  `stop` varchar(50) NOT NULL,
  `dHour` varchar(50) NOT NULL,
  `aHour` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `trains`
--

INSERT INTO `trains` (`trainNo`, `trainType`, `start`, `stop`, `dHour`, `aHour`) VALUES
(1, 'Interregio', 'Oradea', 'Timisoara', '09:00', '14:30'),
(2, 'Regio', 'Cluj-Napoca', 'Oradea', '15:00', '18:00'),
(3, 'Regio', 'Oradea', 'Cluj-Napoca', '08:30', '11:45'),
(4, 'Regio', 'Cluj-Napoca', 'Alba Iulia', '12:00', '15:15'),
(5, 'Interregio', 'Satu Mare', 'Bucuresti', '04:00', '22:30'),
(6, 'Interregio', 'Satu Mare', 'Oradea', '04:00', '08:00'),
(7, 'Interregio', 'Oradea', 'Bucuresti', '08:15', '22:30');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `trains`
--
ALTER TABLE `trains`
  ADD PRIMARY KEY (`trainNo`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `trains`
--
ALTER TABLE `trains`
  MODIFY `trainNo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
