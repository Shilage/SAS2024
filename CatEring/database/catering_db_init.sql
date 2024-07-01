-- MySQL dump 10.13 Distrib 5.7.26, for osx10.10 (x86_64)
-- Host: 127.0.0.1 Database: catering
-- ------------------------------------------------------
-- Server version 5.7.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

-- Create the database catering
CREATE DATABASE IF NOT EXISTS catering CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE catering;

-- Table structure for table `Events`
DROP TABLE IF EXISTS `Events`;
CREATE TABLE `Events`
(
    `id`                    int(11) NOT NULL AUTO_INCREMENT,
    `name`                  varchar(128) DEFAULT NULL,
    `date_start`            date         DEFAULT NULL,
    `date_end`              date         DEFAULT NULL,
    `expected_participants` int(11)      DEFAULT NULL,
    `organizer_id`          int(11) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for table `Users`
DROP TABLE IF EXISTS `Users`;
CREATE TABLE `Users`
(
    `id`       int(11)      NOT NULL AUTO_INCREMENT,
    `username` varchar(128) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for table `UserRoles`
DROP TABLE IF EXISTS `UserRoles`;
CREATE TABLE `UserRoles`
(
    `user_id` int(11) NOT NULL,
    `role_id` char(1) NOT NULL DEFAULT 's',
    FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for table `Roles`
DROP TABLE IF EXISTS `Roles`;
CREATE TABLE `Roles`
(
    `id`   char(1)      NOT NULL,
    `role` varchar(128) NOT NULL DEFAULT 'servizio',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for table `Preparations`
DROP TABLE IF EXISTS `Preparations`;
CREATE TABLE `Preparations`
(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for table `Recipes`
DROP TABLE IF EXISTS `Recipes`;
CREATE TABLE `Recipes`
(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for table `RecipePreparations`
DROP TABLE IF EXISTS `RecipePreparations`;
CREATE TABLE `RecipePreparations`
(
    `recipe_id`      int(11) NOT NULL,
    `preparation_id` int(11) NOT NULL,
    PRIMARY KEY (`recipe_id`, `preparation_id`),
    FOREIGN KEY (`recipe_id`) REFERENCES `Recipes` (`id`),
    FOREIGN KEY (`preparation_id`) REFERENCES `Preparations` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for table `CookingProcedures`
DROP TABLE IF EXISTS `CookingProcedures`;
CREATE TABLE `CookingProcedures`
(
    `id`                        int(11) NOT NULL AUTO_INCREMENT,
    `name`                      tinytext,
    `type`                      ENUM ('preparation', 'recipe'),
    `fk_referenced_recipe`      int(11) DEFAULT NULL,
    `fk_referenced_preparation` int(11) DEFAULT NULL,
    FOREIGN KEY (`fk_referenced_recipe`) REFERENCES `Recipes` (`id`),
    FOREIGN KEY (`fk_referenced_preparation`) REFERENCES `Preparations` (`id`),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for table `Menus`
DROP TABLE IF EXISTS `Menus`;
CREATE TABLE `Menus`
(
    `id`        int(11) NOT NULL AUTO_INCREMENT,
    `title`     tinytext,
    `owner_id`  int(11)    DEFAULT NULL,
    `published` tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for table `MenuSections`
DROP TABLE IF EXISTS `MenuSections`;
CREATE TABLE `MenuSections`
(
    `id`       int(11) NOT NULL AUTO_INCREMENT,
    `menu_id`  int(11) NOT NULL,
    `name`     tinytext,
    `position` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`menu_id`) REFERENCES `Menus` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for table `MenuItems`
DROP TABLE IF EXISTS `MenuItems`;
CREATE TABLE `MenuItems`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `menu_id`     int(11) NOT NULL,
    `section_id`  int(11) DEFAULT NULL,
    `description` tinytext,
    `recipe_id`   int(11) NOT NULL,
    `position`    int(11) DEFAULT NULL,
    FOREIGN KEY (`menu_id`) REFERENCES `Menus` (`id`),
    FOREIGN KEY (`section_id`) REFERENCES `MenuSections` (`id`),
    FOREIGN KEY (`recipe_id`) REFERENCES `Recipes` (`id`),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for table `MenuFeatures`
DROP TABLE IF EXISTS `MenuFeatures`;
CREATE TABLE `MenuFeatures`
(
    `menu_id` int(11)      NOT NULL,
    `name`    varchar(128) NOT NULL DEFAULT '',
    `value`   tinyint(1)            DEFAULT '0',
    PRIMARY KEY (`menu_id`, `name`),
    FOREIGN KEY (`menu_id`) REFERENCES `Menus` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for table `Services`
DROP TABLE IF EXISTS `Services`;
CREATE TABLE `Services`
(
    `id`                    int(11) NOT NULL AUTO_INCREMENT,
    `event_id`              int(11) NOT NULL,
    `name`                  varchar(128) DEFAULT NULL,
    `used_menu_id`          int(11)      DEFAULT NULL,
    `chef_id`               int(11)      DEFAULT NULL,
    `summary_sheet_id`     int(11)       DEFAULT NULL,
    `service_date`          date         DEFAULT NULL,
    `time_start`            time         DEFAULT NULL,
    `time_end`              time         DEFAULT NULL,
    `expected_participants` int(11)      DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`event_id`) REFERENCES `Events` (`id`),
    FOREIGN KEY (`used_menu_id`) REFERENCES `Menus` (`id`),
    FOREIGN KEY (`chef_id`) REFERENCES `Users` (`id`),
    FOREIGN KEY (`summary_sheet_id`) references SummarySheets(`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for Tasks
DROP TABLE IF EXISTS `Tasks`;
CREATE TABLE `Tasks`
(
    `id`                   int(11)      NOT NULL AUTO_INCREMENT,
    `cooking_procedure_id` int(11)      NOT NULL,
    `cook_id`              int(11)      DEFAULT NULL,
    `shift_id`             int(11)      DEFAULT NULL,
    `initial_task`         int(11)      DEFAULT NULL,
    `time_to_complete`     varchar(20)  DEFAULT NULL,
    `completed`            boolean      DEFAULT false,
    `amount`               varchar(255) DEFAULT NULL,
    `doses`                varchar(255) DEFAULT NULL,
    `to_prepare`           boolean      DEFAULT true,
    FOREIGN KEY (`cooking_procedure_id`) references CookingProcedures (`id`),
    FOREIGN KEY (`cook_id`) references Users (`id`),
    -- FOREIGN KEY (`shift_id`) references CookingShift(`id`),
    FOREIGN KEY (`initial_task`) references Tasks (`id`),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for SummarySheets
DROP TABLE IF EXISTS `SummarySheets`;
CREATE TABLE `SummarySheets`
(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for ListedTasks (in SummarySheet)
DROP TABLE IF EXISTS `ListedTasks`;
CREATE TABLE `ListedTasks`
(
    `summary_sheet_id` int(11) NOT NULL,
    `task_id`          int(11) NOT NULL,
    FOREIGN KEY (`summary_sheet_id`) REFERENCES SummarySheets (`id`),
    FOREIGN KEY (`task_id`) REFERENCES Tasks (`id`),
    PRIMARY KEY (`summary_sheet_id`, `task_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Table structure for ListedProcedures (in SummarySheet)
DROP TABLE IF EXISTS `ListedProcedures`;
CREATE TABLE `ListedProcedures`
(
    `summary_sheet_id` int(11) NOT NULL,
    `procedure_id`     int(11) NOT NULL,
    `position`         int(11) NOT NULL,
    FOREIGN KEY (`summary_sheet_id`) REFERENCES SummarySheets (`id`),
    FOREIGN KEY (`procedure_id`) REFERENCES CookingProcedures (`id`),
    PRIMARY KEY (`summary_sheet_id`, `procedure_id`, `position`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- Inserting data for table `Roles`
INSERT INTO `Roles` (`id`, `role`)
VALUES ('c', 'cuoco'),
       ('h', 'chef'),
       ('o', 'organizzatore'),
       ('s', 'servizio');


-- Inserting data for table `Users`
INSERT INTO `Users` (`id`, `username`)
VALUES (1, 'Alice'),
       (2, 'Bob'),
       (3, 'Charlie'),
       (4, 'Dana'),
       (5, 'Eva'),
       (6, 'Frank'),
       (7, 'Georgia'),
       (8, 'Henry'),
       (9, 'Isla'),
       (10, 'Jack');

-- Inserting data for table `UserRoles`
INSERT INTO `UserRoles` (`user_id`, `role_id`)
VALUES (1, 'o'),
       (2, 'h'),
       (3, 's'),
       (4, 'c'),
       (5, 'h'), -- Eva as Chef
       (6, 'h'), -- Frank as Head Chef
       (7, 'o'), -- Georgia as Organizer
       (8, 's'), -- Henry as Service Staff
       (9, 'c'), -- Isla as Chef
       (10, 'h'); -- Jack as Head Chef

-- Inserting data for table `Events`
INSERT INTO `Events` (`id`, `name`, `date_start`, `date_end`, `expected_participants`, `organizer_id`)
VALUES (1, 'Wedding Reception', '2023-07-15', '2023-07-15', 200, 2),
       (2, 'Corporate Gala', '2023-08-01', '2023-08-01', 300, 3),
       (3, 'Birthday Party', '2023-09-20', '2023-09-20', 100, 1);



-- Inserting data for table `Preparations`
INSERT INTO `Preparations` (`id`)
VALUES (1),
       (2),
       (3),
       (4),
       (5),
       (6),
       (7),
       (8);

-- Inserting data for table `Recipes`
INSERT INTO `Recipes` (`id`)
VALUES (1),
       (2),
       (3);


-- Inserting data for table `RecipePreparations`
INSERT INTO `RecipePreparations` (`recipe_id`, `preparation_id`)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (3, 2),
       (3, 7),
       (3, 8);

-- Inserting data for table `CookingProcedures`
INSERT INTO `CookingProcedures` (`id`, `name`, `type`, `fk_referenced_recipe`, `fk_referenced_preparation`)
VALUES (1, 'Pasta', 'recipe', 1, NULL),
       (2, 'Pasta Dough', 'preparation', NULL, 1),
       (3, 'Tomato Sauce', 'preparation', NULL, 2),
       (4, 'Boil Water', 'preparation', NULL, 3),
       (5, 'Curry Chicken', 'recipe', 2, NULL),
       (6, 'Marinate Raw Chicken', 'preparation', NULL, 4),
       (7, 'Grill Chicken', 'preparation', NULL, 5),
       (8, 'Curry Sauce', 'preparation', NULL, 6),
       (9, 'Lasagna', 'recipe', 3, NULL),
       (11, 'Besciamella', 'preparation', NULL, 7),
       (12, 'Bake in the oven', 'preparation', NULL, 8);

-- Inserting data for table `Menus`
INSERT INTO `Menus` (`id`, `title`, `owner_id`, `published`)
VALUES (1, 'Spring Wedding Menu', 5, 1),
       (2, 'Corporate Dinner', 3, 1),
       (3, 'Birthday Desserts', 2, 1);

-- Inserting data for table `MenuSections`
INSERT INTO `MenuSections` (`id`, `menu_id`, `name`, `position`)
VALUES (1, 1, 'Main Course', 1),
       (2, 1, 'Proteins', 2),
       (3, 2, 'Main Course', 3);

-- Inserting data for table `MenuItems`
INSERT INTO `MenuItems` (`id`, `menu_id`, `section_id`, `description`, `recipe_id`, `position`)
VALUES (1, 1, 1, 'Homemade Pasta with Tomato Sauce', 1, 1),
       (2, 1, 2, 'Chicken Curry', 2, 2),
       (3, 2, 3, 'Lasagna', 3, 1);

-- Inserting data for table `MenuFeatures`
INSERT INTO `MenuFeatures` (`menu_id`, `name`, `value`)
VALUES (1, 'Normal', 1),
       (2, 'Gluten Free', 1);


-- Inserting data for table `SummarySheets`
INSERT INTO `SummarySheets` (`id`)
VALUES (1), (2), (3);

-- Inserting data for table `Services`
INSERT INTO `Services` (`id`, `event_id`, `name`, `used_menu_id`, `service_date`, `time_start`, `time_end`, `expected_participants`, `chef_id`, `summary_sheet_id`)
VALUES (1, 1, 'Lunch Service', 1, '2023-07-15', '12:00:00', '15:00:00', 200, 5, 1),
       (2, 2, 'Dinner Service', 2, '2023-08-01', '18:00:00', '21:00:00', 300, 2, 2);

INSERT INTO `ListedProcedures` (summary_sheet_id, procedure_id, position)
VALUES (1, 1, 0),
       (1, 5, 1)
