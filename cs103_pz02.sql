-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 07, 2018 at 06:43 PM
-- Server version: 10.1.28-MariaDB
-- PHP Version: 7.1.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cs103_pz02`
--

-- --------------------------------------------------------

--
-- Table structure for table `magacin`
--

CREATE TABLE `magacin` (
  `id` int(11) NOT NULL,
  `adresa` text NOT NULL,
  `stanje` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `magacin`
--

INSERT INTO `magacin` (`id`, `adresa`, `stanje`) VALUES
(1, 'Svetog Save 55', 100),
(2, 'Nikole Tesle 12', 120),
(3, 'Bulevar Oslobodjenja 114', 120),
(4, 'Bulevat kralja Aleksandra 345', 120),
(5, 'Kajmakcalanska 37', 120),
(6, 'Balkanska 140', 120),
(7, 'Svetozara Markovica 38', 120),
(8, 'Nemanjina 220', 120),
(9, 'Kralja Petra Prvog 42', 120),
(10, 'Trg Nikole Pasica 13', 120);

-- --------------------------------------------------------

--
-- Table structure for table `mapa`
--

CREATE TABLE `mapa` (
  `magacin_a` int(11) NOT NULL,
  `magacin_b` int(11) NOT NULL,
  `rastojanje` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mapa`
--

INSERT INTO `mapa` (`magacin_a`, `magacin_b`, `rastojanje`) VALUES
(3, 6, 400.5),
(6, 3, 352.43),
(6, 4, 212.53),
(6, 5, 43.12),
(6, 9, 1201.23),
(6, 8, 13.21),
(6, 2, 2120.12),
(6, 1, 310.24),
(6, 7, 400.21),
(6, 10, 4123.02),
(3, 4, 321),
(3, 5, 42.21),
(3, 2, 324.21),
(7, 8, 213.21),
(7, 4, 431.213),
(3, 6, 400.5),
(6, 3, 352.43),
(6, 4, 212.53),
(6, 5, 43.12),
(6, 9, 1201.23),
(6, 8, 13.21),
(6, 2, 2120.12),
(6, 1, 310.24),
(6, 7, 400.21),
(6, 10, 4123.02),
(3, 4, 321),
(3, 5, 42.21),
(3, 2, 324.21),
(7, 8, 213.21),
(7, 4, 431.213);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `magacin`
--
ALTER TABLE `magacin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `mapa`
--
ALTER TABLE `mapa`
  ADD KEY `magacin_a` (`magacin_a`),
  ADD KEY `magacin_b` (`magacin_b`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `magacin`
--
ALTER TABLE `magacin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `mapa`
--
ALTER TABLE `mapa`
  ADD CONSTRAINT `mapa_ibfk_1` FOREIGN KEY (`magacin_a`) REFERENCES `magacin` (`id`),
  ADD CONSTRAINT `mapa_ibfk_2` FOREIGN KEY (`magacin_b`) REFERENCES `magacin` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
