CREATE DATABASE incident_manager COLLATE utf8_general_ci;

USE incident_manager;

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `api_key` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
);

CREATE TABLE IF NOT EXISTS `incidents` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `incident` text NOT NULL,
  `lat` FLOAT( 10, 6 ) NOT NULL DEFAULT 0.000000000,
  `lng` FLOAT( 10, 6 ) NOT NULL DEFAULT 0.000000000,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

INSERT INTO users(name,email,api_key)
    VALUES ('Odin','bjornahlander@icloud.com','49GbMsQ9vXdPMryrupJZ');
