-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1
-- Время создания: Июн 27 2026 г., 06:28
-- Версия сервера: 10.4.32-MariaDB
-- Версия PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `financial_db`
--

-- --------------------------------------------------------

--
-- Структура таблицы `currencies`
--

CREATE TABLE `currencies` (
  `id_currency` int(11) NOT NULL,
  `type` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `currencies`
--

INSERT INTO `currencies` (`id_currency`, `type`) VALUES
(4, 'CNY'),
(3, 'EUR'),
(1, 'RUB'),
(2, 'USD');

-- --------------------------------------------------------

--
-- Структура таблицы `currency_rates`
--

CREATE TABLE `currency_rates` (
  `id_currency` int(11) NOT NULL,
  `rate_date` date NOT NULL,
  `coefficient` decimal(12,6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `currency_rates`
--

INSERT INTO `currency_rates` (`id_currency`, `rate_date`, `coefficient`) VALUES
(1, '2024-01-01', 1.000000),
(1, '2024-04-01', 1.000000),
(1, '2024-07-01', 1.000000),
(1, '2024-10-01', 1.000000),
(1, '2025-01-01', 1.000000),
(1, '2025-04-01', 1.000000),
(1, '2025-07-01', 1.000000),
(2, '2024-01-01', 90.000000),
(2, '2024-04-01', 91.500000),
(2, '2024-07-01', 92.300000),
(2, '2024-10-01', 93.800000),
(2, '2025-01-01', 95.200000),
(2, '2025-04-01', 96.700000),
(2, '2025-07-01', 98.100000),
(3, '2024-01-01', 98.500000),
(3, '2024-04-01', 99.800000),
(3, '2024-07-01', 100.200000),
(3, '2024-10-01', 101.500000),
(3, '2025-01-01', 102.800000),
(3, '2025-04-01', 104.100000),
(3, '2025-07-01', 105.500000),
(4, '2024-01-01', 13.500000),
(4, '2024-04-01', 13.800000),
(4, '2024-07-01', 14.100000),
(4, '2024-10-01', 14.300000),
(4, '2025-01-01', 14.500000),
(4, '2025-04-01', 14.700000),
(4, '2025-07-01', 14.900000);

-- --------------------------------------------------------

--
-- Структура таблицы `enterprises`
--

CREATE TABLE `enterprises` (
  `id_enterprise` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  `phone_number` varchar(30) DEFAULT NULL,
  `contact_person` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `enterprises`
--

INSERT INTO `enterprises` (`id_enterprise`, `name`, `details`, `phone_number`, `contact_person`) VALUES
(1, 'ПАО \"Магнит\"', 'ИНН 2309085638', '+7 861 210 98 10', 'Евгений Сергеевич Случевский'),
(2, 'ООО \"КубанскийМолочник\"', 'ИНН 2311068781', '+7 861 258 69 46', 'Дудик Нина Михайловна'),
(3, 'ОАО \"ТеннисПро\"', 'ИНН 235678901234', '+7 988 333 44 55', 'Ермоленко Анастасия Сергеевна'),
(4, 'ИП Товкач С.А.', 'ИНН 123789012345', '+7 918 274 52 67', 'Товкач Светлана Алексеевна');

-- --------------------------------------------------------

--
-- Структура таблицы `enterprise_periods`
--

CREATE TABLE `enterprise_periods` (
  `id_enterprise` int(11) NOT NULL,
  `id_period` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `enterprise_periods`
--

INSERT INTO `enterprise_periods` (`id_enterprise`, `id_period`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(2, 1),
(2, 2),
(2, 3),
(2, 4),
(2, 5),
(2, 6),
(2, 7),
(2, 8),
(2, 9),
(3, 5),
(3, 6),
(3, 7),
(3, 8),
(4, 5),
(4, 6),
(4, 7),
(4, 8);

-- --------------------------------------------------------

--
-- Структура таблицы `indicators`
--

CREATE TABLE `indicators` (
  `id_indicator` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `indicators`
--

INSERT INTO `indicators` (`id_indicator`, `name`) VALUES
(1, 'Выручка'),
(5, 'Дебиторская задолженность'),
(4, 'Операционные расходы'),
(2, 'Себестоимость'),
(3, 'Чистая прибыль');

-- --------------------------------------------------------

--
-- Структура таблицы `indicator_currencies`
--

CREATE TABLE `indicator_currencies` (
  `id_indicator` int(11) NOT NULL,
  `id_currency` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `indicator_currencies`
--

INSERT INTO `indicator_currencies` (`id_indicator`, `id_currency`) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 3),
(5, 4);

-- --------------------------------------------------------

--
-- Структура таблицы `indicator_currencies_enterprise_periods`
--

CREATE TABLE `indicator_currencies_enterprise_periods` (
  `id_enterprise` int(11) NOT NULL,
  `id_period` int(11) NOT NULL,
  `id_indicator` int(11) NOT NULL,
  `id_currency` int(11) NOT NULL,
  `value` decimal(18,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `indicator_currencies_enterprise_periods`
--

INSERT INTO `indicator_currencies_enterprise_periods` (`id_enterprise`, `id_period`, `id_indicator`, `id_currency`, `value`) VALUES
(1, 1, 1, 1, 15000000.00),
(1, 1, 2, 1, 9000000.00),
(1, 1, 3, 2, 12000.00),
(1, 1, 4, 3, 3000000.00),
(1, 1, 5, 4, 1000000.00),
(1, 2, 1, 1, 17500000.00),
(1, 2, 2, 1, 10500000.00),
(1, 2, 3, 2, 14500.00),
(1, 2, 4, 3, 3200000.00),
(1, 2, 5, 4, 1100000.00),
(1, 3, 1, 1, 19000000.00),
(1, 3, 2, 1, 11000000.00),
(1, 3, 3, 2, 16000.00),
(1, 3, 4, 3, 3500000.00),
(1, 3, 5, 4, 1050000.00),
(1, 4, 1, 1, 21000000.00),
(1, 4, 2, 1, 12500000.00),
(1, 4, 3, 2, 18500.00),
(1, 4, 4, 3, 3800000.00),
(1, 4, 5, 4, 1200000.00),
(1, 5, 1, 1, 16000000.00),
(1, 5, 2, 1, 9500000.00),
(1, 5, 3, 2, 13000.00),
(1, 5, 4, 3, 3100000.00),
(1, 5, 5, 4, 1200000.00),
(1, 6, 1, 1, 18000000.00),
(1, 6, 2, 1, 10800000.00),
(1, 6, 3, 2, 15000.00),
(1, 6, 4, 3, 3300000.00),
(1, 6, 5, 4, 1350000.00),
(1, 7, 1, 1, 20500000.00),
(1, 7, 2, 1, 12000000.00),
(1, 7, 3, 2, 17500.00),
(1, 7, 4, 3, 3600000.00),
(1, 7, 5, 4, 1100000.00),
(1, 8, 1, 1, 23000000.00),
(1, 8, 2, 1, 13500000.00),
(1, 8, 3, 2, 20000.00),
(1, 8, 4, 3, 4000000.00),
(1, 8, 5, 4, 980000.00),
(2, 1, 1, 1, 8000000.00),
(2, 1, 2, 1, 4800000.00),
(2, 1, 3, 2, 5000.00),
(2, 1, 4, 3, 1500000.00),
(2, 1, 5, 4, 400000.00),
(2, 2, 1, 1, 8500000.00),
(2, 2, 2, 1, 5100000.00),
(2, 2, 3, 2, 5500.00),
(2, 2, 4, 3, 1600000.00),
(2, 2, 5, 4, 450000.00),
(2, 3, 1, 1, 9200000.00),
(2, 3, 2, 1, 5500000.00),
(2, 3, 3, 2, 6000.00),
(2, 3, 4, 3, 1750000.00),
(2, 3, 5, 4, 430000.00),
(2, 4, 1, 1, 9800000.00),
(2, 4, 2, 1, 5900000.00),
(2, 4, 3, 2, 6500.00),
(2, 4, 4, 3, 1900000.00),
(2, 4, 5, 4, 480000.00),
(2, 5, 1, 1, 8200000.00),
(2, 5, 2, 1, 4900000.00),
(2, 5, 3, 2, 5200.00),
(2, 5, 4, 3, 1550000.00),
(2, 5, 5, 4, 500000.00),
(2, 6, 1, 1, 8800000.00),
(2, 6, 2, 1, 5300000.00),
(2, 6, 3, 2, 5700.00),
(2, 6, 4, 3, 1650000.00),
(2, 6, 5, 4, 550000.00),
(2, 7, 1, 1, 9500000.00),
(2, 7, 2, 1, 5700000.00),
(2, 7, 3, 2, 6200.00),
(2, 7, 4, 3, 1800000.00),
(2, 7, 5, 4, 480000.00),
(2, 8, 1, 1, 10200000.00),
(2, 8, 2, 1, 6100000.00),
(2, 8, 3, 2, 6800.00),
(2, 8, 4, 3, 1950000.00),
(2, 8, 5, 4, 420000.00),
(3, 5, 1, 1, 676767.67),
(3, 5, 2, 1, 1500000.00),
(3, 5, 3, 2, 1500.00),
(3, 5, 4, 3, 500000.00),
(3, 5, 5, 4, 150000.00),
(3, 6, 1, 1, 2800000.00),
(3, 6, 2, 1, 1680000.00),
(3, 6, 3, 2, 1700.00),
(3, 6, 4, 3, 550000.00),
(3, 6, 5, 4, 170000.00),
(3, 7, 1, 1, 3100000.00),
(3, 7, 2, 1, 1860000.00),
(3, 7, 3, 2, 1900.00),
(3, 7, 4, 3, 600000.00),
(3, 7, 5, 4, 140000.00),
(3, 8, 1, 1, 3400000.00),
(3, 8, 2, 1, 2040000.00),
(3, 8, 4, 3, 650000.00),
(3, 8, 5, 4, 120000.00),
(4, 5, 1, 1, 500000.00),
(4, 5, 2, 1, 300000.00),
(4, 5, 3, 2, 300.00),
(4, 5, 4, 3, 100000.00),
(4, 5, 5, 4, 30000.00),
(4, 6, 1, 1, 550000.00),
(4, 6, 2, 1, 330000.00),
(4, 6, 3, 2, 340.00),
(4, 6, 4, 3, 110000.00),
(4, 6, 5, 4, 35000.00),
(4, 7, 1, 1, 600000.00),
(4, 7, 2, 1, 360000.00),
(4, 7, 3, 2, 380.00),
(4, 7, 4, 3, 120000.00),
(4, 7, 5, 4, 32000.00),
(4, 8, 1, 1, 650000.00),
(4, 8, 2, 1, 390000.00),
(4, 8, 3, 2, 420.00),
(4, 8, 4, 3, 130000.00),
(4, 8, 5, 4, 28000.00);

-- --------------------------------------------------------

--
-- Структура таблицы `periods`
--

CREATE TABLE `periods` (
  `id_period` int(11) NOT NULL,
  `quarter` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `periods`
--

INSERT INTO `periods` (`id_period`, `quarter`) VALUES
(1, 'Q1 2024'),
(2, 'Q2 2024'),
(3, 'Q3 2024'),
(4, 'Q4 2024'),
(5, 'Q1 2025'),
(6, 'Q2 2025'),
(7, 'Q3 2025'),
(8, 'Q4 2025'),
(9, 'Q1 2026');

-- --------------------------------------------------------

--
-- Структура таблицы `roles`
--

CREATE TABLE `roles` (
  `id_role` int(11) NOT NULL,
  `access_level` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `roles`
--

INSERT INTO `roles` (`id_role`, `access_level`) VALUES
(1, 'admin'),
(2, 'user');

-- --------------------------------------------------------

--
-- Структура таблицы `users_roles_enterprises`
--

CREATE TABLE `users_roles_enterprises` (
  `id_user` int(11) NOT NULL,
  `id_enterprise` int(11) DEFAULT NULL,
  `id_role` int(11) NOT NULL,
  `login` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `users_roles_enterprises`
--

INSERT INTO `users_roles_enterprises` (`id_user`, `id_enterprise`, `id_role`, `login`, `password`) VALUES
(1, 3, 1, 'dasha', '$2a$10$0PR2yuUQ.Y45wErNi.ACTeYdIxFBokFnB.AfGHEelTMWQXM2FApz6'),
(2, 2, 2, 'margo', '$2a$10$y0QFZ43jzpS.WuQnkEpHOuPY1Ridw/MUDrCwXlrZKnDOiH3y76uvG');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `currencies`
--
ALTER TABLE `currencies`
  ADD PRIMARY KEY (`id_currency`),
  ADD UNIQUE KEY `type` (`type`);

--
-- Индексы таблицы `currency_rates`
--
ALTER TABLE `currency_rates`
  ADD PRIMARY KEY (`id_currency`,`rate_date`);

--
-- Индексы таблицы `enterprises`
--
ALTER TABLE `enterprises`
  ADD PRIMARY KEY (`id_enterprise`),
  ADD UNIQUE KEY `phone_number` (`phone_number`);

--
-- Индексы таблицы `enterprise_periods`
--
ALTER TABLE `enterprise_periods`
  ADD PRIMARY KEY (`id_enterprise`,`id_period`),
  ADD KEY `enterprise_periods_ibfk_2` (`id_period`);

--
-- Индексы таблицы `indicators`
--
ALTER TABLE `indicators`
  ADD PRIMARY KEY (`id_indicator`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Индексы таблицы `indicator_currencies`
--
ALTER TABLE `indicator_currencies`
  ADD PRIMARY KEY (`id_indicator`,`id_currency`),
  ADD KEY `indicator_currencies_ibfk_2` (`id_currency`);

--
-- Индексы таблицы `indicator_currencies_enterprise_periods`
--
ALTER TABLE `indicator_currencies_enterprise_periods`
  ADD PRIMARY KEY (`id_enterprise`,`id_period`,`id_indicator`,`id_currency`),
  ADD KEY `indicator_currencies_enterprise_periods_ibfk_2` (`id_indicator`,`id_currency`);

--
-- Индексы таблицы `periods`
--
ALTER TABLE `periods`
  ADD PRIMARY KEY (`id_period`);

--
-- Индексы таблицы `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id_role`),
  ADD UNIQUE KEY `access_level` (`access_level`);

--
-- Индексы таблицы `users_roles_enterprises`
--
ALTER TABLE `users_roles_enterprises`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `login` (`login`),
  ADD KEY `users_roles_enterprises_ibfk_1` (`id_enterprise`),
  ADD KEY `users_roles_enterprises_ibfk_2` (`id_role`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `currencies`
--
ALTER TABLE `currencies`
  MODIFY `id_currency` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT для таблицы `enterprises`
--
ALTER TABLE `enterprises`
  MODIFY `id_enterprise` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT для таблицы `indicators`
--
ALTER TABLE `indicators`
  MODIFY `id_indicator` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT для таблицы `periods`
--
ALTER TABLE `periods`
  MODIFY `id_period` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT для таблицы `roles`
--
ALTER TABLE `roles`
  MODIFY `id_role` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT для таблицы `users_roles_enterprises`
--
ALTER TABLE `users_roles_enterprises`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `currency_rates`
--
ALTER TABLE `currency_rates`
  ADD CONSTRAINT `currency_rates_ibfk_1` FOREIGN KEY (`id_currency`) REFERENCES `currencies` (`id_currency`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `enterprise_periods`
--
ALTER TABLE `enterprise_periods`
  ADD CONSTRAINT `enterprise_periods_ibfk_1` FOREIGN KEY (`id_enterprise`) REFERENCES `enterprises` (`id_enterprise`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `enterprise_periods_ibfk_2` FOREIGN KEY (`id_period`) REFERENCES `periods` (`id_period`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `indicator_currencies`
--
ALTER TABLE `indicator_currencies`
  ADD CONSTRAINT `indicator_currencies_ibfk_1` FOREIGN KEY (`id_indicator`) REFERENCES `indicators` (`id_indicator`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `indicator_currencies_ibfk_2` FOREIGN KEY (`id_currency`) REFERENCES `currencies` (`id_currency`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `indicator_currencies_enterprise_periods`
--
ALTER TABLE `indicator_currencies_enterprise_periods`
  ADD CONSTRAINT `indicator_currencies_enterprise_periods_ibfk_1` FOREIGN KEY (`id_enterprise`,`id_period`) REFERENCES `enterprise_periods` (`id_enterprise`, `id_period`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `indicator_currencies_enterprise_periods_ibfk_2` FOREIGN KEY (`id_indicator`,`id_currency`) REFERENCES `indicator_currencies` (`id_indicator`, `id_currency`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `users_roles_enterprises`
--
ALTER TABLE `users_roles_enterprises`
  ADD CONSTRAINT `users_roles_enterprises_ibfk_1` FOREIGN KEY (`id_enterprise`) REFERENCES `enterprises` (`id_enterprise`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `users_roles_enterprises_ibfk_2` FOREIGN KEY (`id_role`) REFERENCES `roles` (`id_role`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
