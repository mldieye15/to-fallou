-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : sam. 20 jan. 2024 à 12:50
-- Version du serveur : 5.7.36
-- Version de PHP : 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `office`
--

-- --------------------------------------------------------

--
-- Structure de la table `academie`
--

DROP TABLE IF EXISTS `academie`;
CREATE TABLE IF NOT EXISTS `academie` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `libelle_court` varchar(255) NOT NULL,
  `libelle_long` varchar(255) NOT NULL,
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK91rjxy79526damtnj0m0csyo7` (`libelle_long`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `academie`
--

INSERT INTO `academie` (`id`, `date_creation`, `date_modification`, `libelle_court`, `libelle_long`, `uti_cree`, `uti_modifie`) VALUES
(1, '2023-11-24 13:29:19', '2023-11-24 13:29:19', 'IA Thies', 'THIES', NULL, NULL),
(4, '2023-11-27 13:01:38', '2023-11-27 13:01:38', 'IA KAOLACK', 'KAOLACK', NULL, NULL),
(3, '2023-11-27 13:01:07', '2023-11-27 13:01:07', 'IA DAKAR', 'DAKAR', NULL, NULL),
(5, '2023-12-26 15:16:52', '2023-12-26 15:16:52', 'DL', 'DIOURBEL', NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `annee`
--

DROP TABLE IF EXISTS `annee`;
CREATE TABLE IF NOT EXISTS `annee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `libelle_long` varchar(255) NOT NULL,
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  `encours` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKbepxf1dhd7se9eyssfvml2r0k` (`libelle_long`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `annee`
--

INSERT INTO `annee` (`id`, `date_creation`, `date_modification`, `libelle_long`, `uti_cree`, `uti_modifie`, `encours`) VALUES
(6, '2023-12-05 13:43:24', '2023-12-05 14:01:50', '2020', NULL, NULL, b'0'),
(7, '2023-12-05 13:43:37', '2023-12-05 14:01:49', '2012', NULL, NULL, b'0'),
(8, '2023-12-05 13:43:47', '2024-01-03 13:16:00', '2022', NULL, NULL, b'0'),
(9, '2023-12-05 13:43:55', '2024-01-18 17:18:43', '2023', NULL, NULL, b'1'),
(11, '2024-01-04 09:44:19', '2024-01-18 17:18:42', '2024', NULL, NULL, b'0'),
(12, '2024-01-16 10:44:14', '2024-01-16 10:44:14', '2025', NULL, NULL, b'0'),
(13, '2024-01-18 11:41:40', '2024-01-18 11:41:40', '2026', NULL, NULL, b'0');

-- --------------------------------------------------------

--
-- Structure de la table `centre`
--

DROP TABLE IF EXISTS `centre`;
CREATE TABLE IF NOT EXISTS `centre` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `libelle_court` varchar(255) NOT NULL,
  `libelle_long` varchar(255) NOT NULL,
  `nombre_jury` int(11) DEFAULT '0',
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  `type_centre_id` bigint(20) DEFAULT NULL,
  `ville_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKcctnpevrwxaxu5dwvriqij23w` (`libelle_long`),
  KEY `FKbyy2eufmkuiwmcqg4fp7j2nng` (`type_centre_id`),
  KEY `FKp403erpfj282colmcnul3ffy2` (`ville_id`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `centre`
--

INSERT INTO `centre` (`id`, `date_creation`, `date_modification`, `libelle_court`, `libelle_long`, `nombre_jury`, `uti_cree`, `uti_modifie`, `type_centre_id`, `ville_id`) VALUES
(1, '2023-12-18 15:34:42', '2023-12-18 15:34:42', 'LPA', 'LYCEE PARCELLE', 1, NULL, NULL, 1, 1),
(2, '2023-12-18 15:35:13', '2023-12-18 15:35:13', 'CEM PA', 'CEM PARCELLE', 1, NULL, NULL, 3, 1),
(3, '2023-12-18 15:35:37', '2023-12-18 15:35:37', 'CEM ML', 'CEM MODOU LO', 0, NULL, NULL, 3, 1),
(4, '2023-12-18 15:36:08', '2023-12-18 15:36:08', 'LGW', 'LYCEE GUEDIAWAYE', 1, NULL, NULL, 1, 2),
(5, '2023-12-18 15:36:49', '2023-12-18 15:36:49', 'CEM GU', 'CEM GUEDIAWAYE', 0, NULL, NULL, 3, 2),
(6, '2023-12-19 11:02:19', '2023-12-19 11:02:19', 'LTT', 'LYCEE TOUBA TOUL', 1, NULL, NULL, 1, 3),
(7, '2023-12-19 16:52:00', '2023-12-19 16:52:00', 'LKH', 'LYCCE KHOMBOLE', 0, NULL, NULL, 3, 4),
(8, '2023-12-26 15:18:19', '2023-12-26 15:18:19', 'LRF', 'LYCEE RAFANE', 0, NULL, NULL, 1, 6),
(9, '2023-12-26 15:18:50', '2023-12-26 15:18:50', 'LBM', 'LYCEE BAMBEY', 0, NULL, NULL, 1, 5),
(10, '2023-12-29 09:39:00', '2023-12-29 09:39:00', 'CEM TT', 'CEM TOUBA TOUL', 0, NULL, NULL, 3, 3),
(11, '2023-12-29 10:44:56', '2023-12-29 10:44:56', 'CEM ND', 'CEM NDONDOL', 0, NULL, NULL, 1, 8),
(12, '2023-12-29 13:17:04', '2023-12-29 13:17:04', 'LTV', 'LYCEE TIVAOUNE', 1, NULL, NULL, 1, 7),
(13, '2024-01-18 10:51:37', '2024-01-18 10:51:37', 'ccc', 'cente1', 0, NULL, NULL, 3, 7);

-- --------------------------------------------------------

--
-- Structure de la table `codification`
--

DROP TABLE IF EXISTS `codification`;
CREATE TABLE IF NOT EXISTS `codification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `date_creation` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKe62jox46oumo4iv0xptmx7d5u` (`code`),
  UNIQUE KEY `UKgpak35cy9u1tdgxgksxo230x1` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `codification`
--

INSERT INTO `codification` (`id`, `code`, `date_creation`, `date_modification`, `email`, `uti_cree`, `uti_modifie`) VALUES
(3, 'fallou12', '2023-12-14 14:27:20', '2023-12-14 14:27:20', 'fallouofficediouf@gmail.com ', NULL, NULL),
(4, 'mamediarra12', '2023-12-14 14:27:44', '2023-12-14 14:27:44', 'mamediarraoffice@gmail.com', NULL, NULL),
(5, 'fallouthiaw12', '2023-12-14 14:28:03', '2023-12-14 14:28:03', 'fallouthiawoffice@gmail.com', NULL, NULL),
(6, 'gueye12', '2023-12-14 14:28:27', '2023-12-14 14:28:27', 'gueyedaoudagueye5@gmail.com', NULL, NULL),
(7, 'khabane12', '2023-12-14 14:28:44', '2023-12-14 14:28:44', 'khabaneoffice@gmail.com', NULL, NULL),
(8, 'diouf12', '2023-12-14 14:29:12', '2023-12-14 14:29:12', 'diouffadel406@gmail.com', NULL, NULL),
(9, '1234567', '2024-01-18 10:33:54', '2024-01-18 10:33:54', 'fadel@diouf', NULL, NULL),
(10, 'code123', '2024-01-18 15:38:42', '2024-01-18 15:38:42', 'dioufyama@yama', NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `demande`
--

DROP TABLE IF EXISTS `demande`;
CREATE TABLE IF NOT EXISTS `demande` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation` datetime DEFAULT NULL,
  `date_demande` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `date_rejet_demande` datetime DEFAULT NULL,
  `delais_de_validation` int(11) DEFAULT NULL,
  `ordre_arrivee` int(11) DEFAULT NULL,
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  `academie_id` bigint(20) DEFAULT NULL,
  `centre_id` bigint(20) DEFAULT NULL,
  `etat_demande_id` bigint(20) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `ville_id` bigint(20) DEFAULT NULL,
  `date_confirmation_demande` datetime DEFAULT NULL,
  `rang` int(11) DEFAULT NULL,
  `total_demande` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe043io443blc9ldrdibve9t2d` (`academie_id`),
  KEY `FK32aynyheqkxh4g0w1fropopkm` (`centre_id`),
  KEY `FK1xjtx0qq8f5tnsndmok2cioya` (`etat_demande_id`),
  KEY `FKgqcexujohnq8070n4el8nau99` (`session_id`),
  KEY `FKe90uq675txdigms4t6lb9ky8l` (`user_id`),
  KEY `FK6p0u2mydherp2ogpat13cqi59` (`ville_id`)
) ENGINE=MyISAM AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `demande`
--

INSERT INTO `demande` (`id`, `date_creation`, `date_demande`, `date_modification`, `date_rejet_demande`, `delais_de_validation`, `ordre_arrivee`, `uti_cree`, `uti_modifie`, `academie_id`, `centre_id`, `etat_demande_id`, `session_id`, `user_id`, `ville_id`, `date_confirmation_demande`, `rang`, `total_demande`) VALUES
(18, '2024-01-17 13:35:18', '2024-01-17 13:35:18', '2024-01-18 17:05:45', NULL, NULL, 1, NULL, NULL, 3, NULL, 1, 22, 25, 2, NULL, 1, 0),
(17, '2024-01-17 13:35:18', '2024-01-19 15:47:20', '2024-01-19 15:47:20', NULL, NULL, 1, NULL, NULL, 3, NULL, 1, 22, 25, 1, NULL, NULL, 0),
(16, '2024-01-17 13:35:18', '2024-01-17 13:35:18', '2024-01-18 10:46:02', NULL, NULL, 1, NULL, NULL, 1, NULL, 1, 22, 25, 3, NULL, NULL, 0),
(15, '2024-01-17 13:34:40', '2024-01-17 13:34:40', '2024-01-18 17:05:45', NULL, NULL, 2, NULL, NULL, 3, NULL, 1, 22, 22, 2, NULL, 2, 0),
(14, '2024-01-17 13:34:40', '2024-01-20 12:48:32', '2024-01-20 12:48:32', NULL, NULL, 2, NULL, NULL, 3, 2, 2, 22, 22, 1, NULL, 1, 0),
(13, '2024-01-17 13:34:40', '2024-01-17 13:34:40', '2024-01-18 10:46:02', NULL, NULL, 2, NULL, NULL, 1, NULL, 1, 22, 22, 3, NULL, NULL, 0),
(12, '2024-01-17 13:34:01', '2024-01-17 13:34:01', '2024-01-18 17:05:45', NULL, NULL, 4, NULL, NULL, 3, NULL, 1, 22, 24, 2, NULL, 3, 0),
(11, '2024-01-17 13:34:01', '2024-01-17 13:34:01', '2024-01-18 17:05:45', NULL, NULL, 4, NULL, NULL, 3, NULL, 1, 22, 24, 1, NULL, 2, 0),
(10, '2024-01-17 13:34:01', '2024-01-20 12:46:47', '2024-01-20 12:46:47', NULL, NULL, 1, NULL, NULL, 1, 12, 2, 22, 24, 7, NULL, 1, 0),
(19, '2024-01-17 13:36:03', '2024-01-18 11:04:01', '2024-01-18 11:11:44', NULL, NULL, 3, NULL, NULL, 1, NULL, 1, 22, 27, 3, NULL, NULL, 0),
(20, '2024-01-17 13:36:03', '2024-01-20 12:48:55', '2024-01-20 12:48:55', NULL, NULL, 3, NULL, NULL, 3, 1, 2, 22, 27, 1, NULL, NULL, 0),
(21, '2024-01-17 13:36:03', '2024-01-20 12:35:15', '2024-01-20 12:35:15', NULL, NULL, 3, NULL, NULL, 3, NULL, 1, 22, 27, 2, NULL, NULL, 0);

-- --------------------------------------------------------

--
-- Structure de la table `details_candidat`
--

DROP TABLE IF EXISTS `details_candidat`;
CREATE TABLE IF NOT EXISTS `details_candidat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `affectable` bit(1) NOT NULL,
  `appreciation` varchar(255) DEFAULT NULL,
  `bonus` int(11) DEFAULT '0',
  `date_creation` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `malus` int(11) DEFAULT '0',
  `note` int(11) DEFAULT '0',
  `note_anciennete` int(11) NOT NULL,
  `note_etablissement_provenance` int(11) NOT NULL,
  `note_fonction` int(11) NOT NULL,
  `note_supervisseur` int(11) DEFAULT '0',
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  `annee_id` bigint(20) DEFAULT NULL,
  `candidat_id` bigint(20) DEFAULT NULL,
  `rang` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKncrg5enyx0yx5vvd26pv1gseg` (`annee_id`),
  KEY `FKpfo9kyj9kxj729syc21clam7s` (`candidat_id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `details_candidat`
--

INSERT INTO `details_candidat` (`id`, `affectable`, `appreciation`, `bonus`, `date_creation`, `date_modification`, `malus`, `note`, `note_anciennete`, `note_etablissement_provenance`, `note_fonction`, `note_supervisseur`, `uti_cree`, `uti_modifie`, `annee_id`, `candidat_id`, `rang`) VALUES
(6, b'0', NULL, 20, '2024-01-17 13:35:18', '2024-01-19 15:54:54', 5, 106, 0, 50, 25, 15, NULL, NULL, 9, 25, 1),
(5, b'1', NULL, 0, '2024-01-17 13:34:40', '2024-01-19 19:53:39', 0, 100, 0, 50, 30, 16, NULL, NULL, 9, 22, 2),
(4, b'1', NULL, 20, '2024-01-17 13:34:01', '2024-01-18 17:48:06', 20, 90, 0, 50, 25, 17, NULL, NULL, 9, 24, 1),
(7, b'1', NULL, 0, '2024-01-17 13:36:03', '2024-01-19 15:50:07', 0, 92, 0, 50, 25, 14, NULL, NULL, 9, 27, NULL);

--
-- Déclencheurs `details_candidat`
--
DROP TRIGGER IF EXISTS `detailsCandidat_before_update`;
DELIMITER $$
CREATE TRIGGER `detailsCandidat_before_update` BEFORE UPDATE ON `details_candidat` FOR EACH ROW BEGIN
   IF NEW.note_supervisseur != OLD.note_supervisseur THEN
        -- La colonne note_superviseur a été modifiée
        IF NEW.note_supervisseur > 15 THEN
            -- Si la note du superviseur est >= 15, définir affectable sur TRUE
            SET NEW.affectable = TRUE;
        ELSE
            -- Si la note du superviseur est < 15, définir affectable sur FALSE
            SET NEW.affectable = FALSE;
        END IF;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `etablissement`
--

DROP TABLE IF EXISTS `etablissement`;
CREATE TABLE IF NOT EXISTS `etablissement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `libelle_long` varchar(255) NOT NULL,
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  `type_etablissement_id` bigint(20) DEFAULT NULL,
  `ville_id` bigint(20) DEFAULT NULL,
  `libelle_court` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp06mfbfn7kcnvjkpm52pg42cg` (`type_etablissement_id`),
  KEY `FK1vrpg4msiytjjbgf3tq9j401f` (`ville_id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `etablissement`
--

INSERT INTO `etablissement` (`id`, `date_creation`, `date_modification`, `libelle_long`, `uti_cree`, `uti_modifie`, `type_etablissement_id`, `ville_id`, `libelle_court`) VALUES
(10, '2023-11-13 20:55:44', '2023-12-06 09:26:01', 'UNIVERSITE CHEIKH ANTA DIOP DE DAKAR', NULL, NULL, 1, 1, 'UCAD'),
(11, '2023-11-22 13:28:47', '2023-12-06 09:26:13', 'UNIVERSITE THIES', NULL, NULL, 1, 2, 'UVT');

-- --------------------------------------------------------

--
-- Structure de la table `etat_demande`
--

DROP TABLE IF EXISTS `etat_demande`;
CREATE TABLE IF NOT EXISTS `etat_demande` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  `libelle_long` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKeyt2groe6ens7b1hovcrtf3ni` (`libelle_long`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `etat_demande`
--

INSERT INTO `etat_demande` (`id`, `date_creation`, `date_modification`, `uti_cree`, `uti_modifie`, `libelle_long`) VALUES
(1, '2023-12-14 10:25:04', '2023-12-14 10:25:04', NULL, NULL, 'EN ATTENTE'),
(2, '2023-12-14 10:27:31', '2023-12-14 10:27:31', NULL, NULL, 'ACCEPTE'),
(3, '2023-12-14 10:27:50', '2023-12-14 10:27:50', NULL, NULL, 'VALIDE'),
(4, '2023-12-14 10:28:41', '2023-12-14 10:28:41', NULL, NULL, 'OBSOLETE'),
(5, '2023-12-14 10:28:57', '2023-12-14 10:28:57', NULL, NULL, 'REJETE');

-- --------------------------------------------------------

--
-- Structure de la table `fonction`
--

DROP TABLE IF EXISTS `fonction`;
CREATE TABLE IF NOT EXISTS `fonction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `libelle_court` varchar(255) NOT NULL,
  `libelle_long` varchar(255) NOT NULL,
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  `nombre_point` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKa6she247cqbwq3jgws58al2p4` (`libelle_long`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `fonction`
--

INSERT INTO `fonction` (`id`, `date_creation`, `date_modification`, `libelle_court`, `libelle_long`, `uti_cree`, `uti_modifie`, `nombre_point`) VALUES
(1, '2023-11-24 13:50:12', '2024-01-05 16:42:19', 'Chercheur', 'Enseignant chercheur', NULL, NULL, '30'),
(3, '2023-11-27 11:19:33', '2023-11-27 11:19:33', 'Inspecteur', 'Inspecteur', NULL, NULL, '25'),
(4, '2023-11-27 11:19:54', '2023-11-27 11:19:54', 'formateur', 'Formateur', NULL, NULL, '20');

-- --------------------------------------------------------

--
-- Structure de la table `jury`
--

DROP TABLE IF EXISTS `jury`;
CREATE TABLE IF NOT EXISTS `jury` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `numero` varchar(255) NOT NULL,
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  `centre_id` bigint(20) DEFAULT NULL,
  `nom` varchar(255) NOT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7x1qqrcike743prq6n6mma4o3` (`numero`),
  UNIQUE KEY `UK2mop8r1sx6ve9ctdil60snkxe` (`nom`),
  KEY `FK2vvwme6aly2aj8ns0tihl9ivh` (`centre_id`),
  KEY `FK2ct7cb7c4xpvrmjrlllv4i2h1` (`session_id`)
) ENGINE=MyISAM AUTO_INCREMENT=139 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `jury`
--

INSERT INTO `jury` (`id`, `date_creation`, `date_modification`, `numero`, `uti_cree`, `uti_modifie`, `centre_id`, `nom`, `session_id`) VALUES
(132, '2024-01-12 13:21:41', '2024-01-12 13:21:41', '239000', NULL, NULL, 1, 'LPA001', 22),
(133, '2024-01-16 11:37:01', '2024-01-16 11:37:01', '12335', NULL, NULL, 6, 'LTT001', 22),
(135, '2024-01-16 13:06:26', '2024-01-16 13:06:26', '222222', NULL, NULL, 2, 'CEM PA003', 22),
(137, '2024-01-18 11:20:40', '2024-01-18 11:20:40', '34567', NULL, NULL, 12, 'LTV001', 22),
(138, '2024-01-18 17:19:05', '2024-01-18 17:19:05', '345678', NULL, NULL, 4, 'LGW001', 22);

-- --------------------------------------------------------

--
-- Structure de la table `refresh_token`
--

DROP TABLE IF EXISTS `refresh_token`;
CREATE TABLE IF NOT EXISTS `refresh_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `token` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=221 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `refresh_token`
--

INSERT INTO `refresh_token` (`id`, `created_date`, `token`) VALUES
(1, '2023-11-06 12:23:29', '0273ef3e-aca8-4756-a032-9063cf35ddaf'),
(2, '2023-11-07 09:34:39', 'df4e2b55-c359-4f21-8821-de3e7e1107aa'),
(3, '2023-11-07 14:03:24', 'ab81b691-a0a2-4c0d-9cdc-e4f8f5664d5d'),
(4, '2023-11-07 14:44:43', '58c48756-033f-42c1-9926-09713a3c8478'),
(5, '2023-11-07 14:45:27', 'c70322cd-8310-4668-b7eb-0151ab70e93f'),
(6, '2023-11-07 15:00:04', 'a4be13e2-6fce-4bc3-b676-033689c262df'),
(7, '2023-11-07 15:08:10', '50e7af41-b7c5-4023-8838-7fb85d520735'),
(8, '2023-11-07 16:38:20', 'a338ff1a-bfe5-42d8-aa0e-569e2da40c54'),
(9, '2023-11-08 16:10:56', '7a6abdc1-39ff-4c93-96bd-2ee0eb60ed9b'),
(10, '2023-11-09 09:29:12', '655c0633-34ee-4fe0-9c78-f4a91ae22cfc'),
(11, '2023-11-09 10:05:54', '79ed816b-299e-4f61-993f-6227f550f230'),
(12, '2023-11-09 14:21:21', '486f9130-4854-406c-9c3c-bb8d6083e917'),
(13, '2023-11-09 14:31:58', 'ebeada98-ba85-4c34-bbe5-e2d30115dc9a'),
(14, '2023-11-10 10:45:29', '7008daa7-0908-41f2-a31c-c2b69c10700d'),
(15, '2023-11-10 12:17:19', '0be2f482-57c8-44e3-b54e-d1de755e479d'),
(16, '2023-11-13 09:46:11', '8e3f5074-98ed-4ea5-91b4-531dbf98c15f'),
(17, '2023-11-13 14:21:31', '1bef36f8-c475-440d-b25c-242d9bc8103a'),
(18, '2023-11-13 14:22:36', 'ce946616-0f16-44a8-bed9-5ae8831989f0'),
(19, '2023-11-13 14:55:58', '52bb2e09-8369-4e94-9207-66e45c1d5972'),
(20, '2023-11-13 15:23:44', '2ef8429c-bef0-45ec-b4d1-66e262a51ce5'),
(21, '2023-11-14 14:23:56', '7e107a4f-bda3-4599-8b7b-2617ac51c0fa'),
(22, '2023-11-16 11:08:44', '2fcd82c1-1e2a-42c4-8bfc-a328bb8f237e'),
(23, '2023-11-21 14:29:07', 'b50cc842-964e-41e7-ba95-353ab5ac9a2d'),
(24, '2023-11-24 19:24:45', 'ac58f2c3-4783-4dfb-95a7-dee115590f79'),
(25, '2023-11-24 19:24:57', '3e0d9894-7984-4133-8bdc-df03e3e70c36'),
(26, '2023-11-27 14:41:05', '4d47835b-69cc-4927-a637-955ddac94c71'),
(27, '2023-11-27 17:09:46', '64b0102f-b456-4ffc-8a5c-c69bdc984bcd'),
(28, '2023-11-30 10:20:16', 'f75ccd4a-f3ad-4727-ab50-f62a0ee2eb8c'),
(29, '2023-12-01 09:32:02', '75f2a2be-6f4a-41eb-b49b-69f90eeb6aca'),
(30, '2023-12-01 09:32:04', '91b06278-379c-4bcf-ba57-3a141894b2af'),
(31, '2023-12-04 09:32:54', 'c2762a0b-ff1d-4194-8720-c54a468db8a9'),
(32, '2023-12-04 09:54:28', '11def7fc-682b-4c9a-94ea-028cbf32776b'),
(33, '2023-12-04 10:48:27', '62b72ef7-5c53-46f9-af72-257cb232d1db'),
(34, '2023-12-11 14:15:48', 'c4d74da5-038c-4770-86ce-a6f8b8472c53'),
(35, '2023-12-14 10:08:38', 'f5ea422a-fa65-489b-8c63-67b01c076e12'),
(36, '2023-12-18 14:24:41', 'ea44e0bc-8fe1-46ad-8cc4-08b7c7a3a660'),
(37, '2023-12-20 13:38:04', '8f69e0a5-624e-4423-891e-37f927ae442e'),
(38, '2023-12-20 14:21:11', '0270e2de-020a-4f67-8538-0fdf8fafa938'),
(39, '2023-12-21 14:55:23', '3725a7a8-c7aa-4a3e-b842-010979cfe89c'),
(40, '2023-12-21 14:57:56', '7900c05c-b079-40a5-a422-742dbfd6ad5c'),
(41, '2023-12-21 15:01:32', 'f46fd4ff-88a8-46bf-8528-8b28b76a9493'),
(42, '2023-12-21 15:04:49', '833a5032-415c-48ed-afc1-257e23824340'),
(43, '2023-12-21 15:04:52', 'af292466-8c4b-444e-a8ba-95c5f13dfb7a'),
(44, '2023-12-21 15:05:18', 'aa79e0a9-b882-4085-9255-b78fdcf3a29d'),
(45, '2023-12-21 15:06:35', '6004a482-1a0e-41a9-a1aa-754f9169057d'),
(46, '2023-12-21 15:09:02', '0d948b39-7ceb-4f72-8880-9519792bc85d'),
(47, '2023-12-21 15:11:33', 'b3e19b21-042c-4514-8994-812056853de3'),
(48, '2023-12-21 15:20:09', '24e4bd43-e770-47ba-b097-5ea734f63118'),
(49, '2023-12-21 15:23:34', 'a6ac3f60-43d6-4b8a-8671-97645f4294d1'),
(50, '2023-12-21 15:25:06', '2523eaa7-19e6-4481-aa9c-64a7147c5d20'),
(51, '2023-12-21 15:27:39', '3dffb138-ed59-420c-a0ff-d81ed9bf483c'),
(52, '2023-12-21 15:29:22', 'bc8a6413-1247-4db4-aad3-1077b0edeaad'),
(53, '2023-12-21 15:32:22', '9c2da64d-0358-4ee6-9ddd-130973e4d3d5'),
(54, '2023-12-21 15:37:12', 'dfdf34fa-5412-481b-b015-bd08be84b914'),
(55, '2023-12-21 15:43:16', '6b6e3268-762a-4677-a4fe-d563a93d0076'),
(56, '2023-12-21 15:44:24', 'a522fd90-c24e-4a94-98d1-982e382f0b56'),
(57, '2023-12-21 15:52:31', '1a331028-a1cc-4656-95c9-be126f0d2d68'),
(58, '2023-12-21 15:54:31', '8e7f5b9a-aaf0-4852-8743-697976df45d5'),
(59, '2023-12-21 21:27:52', 'd0761923-3f10-4483-ba36-ef2e9e573e3d'),
(60, '2023-12-21 22:05:21', '1e42fad3-3c04-4438-9ea3-e1931a10695c'),
(76, '2023-12-22 16:56:01', '01e7eac8-3fea-41c1-ba3c-671ca6b75c4d'),
(62, '2023-12-21 22:15:46', '3a18c8c7-e660-49bc-b09b-40a970be22da'),
(64, '2023-12-21 22:27:55', 'f5434456-cb10-4159-8557-42c1a21cfa19'),
(66, '2023-12-22 09:47:00', 'bc42e388-865a-44d4-b193-9999f36c24bd'),
(67, '2023-12-22 09:56:18', '4b1b5e10-80b8-4a5d-a105-0fbff2a50821'),
(68, '2023-12-22 11:26:41', 'd813a5a7-f9b6-4414-8dce-f5934f3aa886'),
(71, '2023-12-22 16:21:54', '24a941db-b2ee-4c28-969c-739c6771cd5a'),
(72, '2023-12-22 16:35:59', '15fe53c7-1568-4ed3-a705-e25ceb489027'),
(74, '2023-12-22 16:54:47', '9c339928-7150-40c1-a11e-11652fb96691'),
(218, '2024-01-18 15:38:05', '1329fa06-0bd7-4597-b655-b2ac4e2c4ce6'),
(77, '2023-12-22 17:06:40', '51a35a54-1b35-4b8e-ac64-db79a6592598'),
(78, '2023-12-24 18:54:09', 'b22837e3-683f-4d48-8f4b-5b666de01c67'),
(170, '2024-01-05 15:45:13', 'ebe87a30-a968-4eff-8d69-5d84637bc281'),
(99, '2023-12-26 14:15:37', '6f26aa4c-ba28-407c-9019-105398939966'),
(167, '2024-01-03 12:53:42', '78ef8820-cc14-414c-8c0a-e36722803e27'),
(176, '2024-01-10 10:42:01', '40252331-6aac-4ccc-8aae-4d67aeea818a'),
(179, '2024-01-11 10:36:17', '0b99a3b5-5286-4ac1-9743-066c112cb969'),
(180, '2024-01-15 09:46:28', '2c565634-2d0e-4064-9ff4-eab729bcd112'),
(189, '2024-01-16 09:26:47', 'f7efdf79-11a5-4221-9c87-d4d7479d04e4'),
(193, '2024-01-16 10:21:36', 'dc706d8c-cfdb-4381-a408-aecbe952fe2e'),
(219, '2024-01-18 16:45:16', '123aa6d1-1099-477c-8bcb-461b69b9c380'),
(220, '2024-01-19 15:35:11', '63808c01-59d6-48e9-8aca-73a448a4874a');

-- --------------------------------------------------------

--
-- Structure de la table `roles`
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation` datetime NOT NULL,
  `date_modification` datetime DEFAULT NULL,
  `nom` varchar(40) NOT NULL,
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_bbxiq3fbrr32wfkco6ichxaly` (`nom`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `roles`
--

INSERT INTO `roles` (`id`, `date_creation`, `date_modification`, `nom`, `uti_cree`, `uti_modifie`) VALUES
(1, '2023-12-22 09:51:55', '2023-12-22 09:51:55', 'ROLE_USER', NULL, NULL),
(2, '2023-12-22 09:51:55', '2023-12-22 09:51:55', 'ROLE_ADMIN', NULL, NULL),
(3, '2024-01-09 13:30:15', '2024-01-09 13:30:15', 'ROLE_PLANIFICATEUR', NULL, NULL),
(4, '2024-01-09 13:30:48', '2024-01-09 13:30:48', 'ROLE_SUPERVISSEUR', NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `session`
--

DROP TABLE IF EXISTS `session`;
CREATE TABLE IF NOT EXISTS `session` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_cloture_depot_candidature` datetime NOT NULL,
  `date_creation` datetime DEFAULT NULL,
  `date_debut` datetime NOT NULL,
  `date_fin` datetime NOT NULL,
  `date_modification` datetime DEFAULT NULL,
  `date_ouverture_depot_candidature` datetime NOT NULL,
  `delais_validation` int(11) NOT NULL,
  `libelle_long` varchar(255) NOT NULL,
  `nombre_demande_autorise` int(11) NOT NULL,
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  `annee_id` bigint(20) DEFAULT NULL,
  `type_session_id` bigint(20) DEFAULT NULL,
  `candidature_ouvert` bit(1) NOT NULL,
  `session_ouvert` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK773jj7fyqsvbm55kggythvxiy` (`annee_id`),
  KEY `FKdvd2c6t60tcooo43nbt87yme1` (`type_session_id`)
) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `session`
--

INSERT INTO `session` (`id`, `date_cloture_depot_candidature`, `date_creation`, `date_debut`, `date_fin`, `date_modification`, `date_ouverture_depot_candidature`, `delais_validation`, `libelle_long`, `nombre_demande_autorise`, `uti_cree`, `uti_modifie`, `annee_id`, `type_session_id`, `candidature_ouvert`, `session_ouvert`) VALUES
(19, '2024-01-29 00:00:00', '2023-11-29 14:03:41', '2023-11-01 00:00:00', '2024-12-10 00:00:00', '2024-01-10 10:43:42', '2023-11-05 00:00:00', 72, 'Session NORMALE 2023', 4, NULL, NULL, 9, 1, b'0', b'0'),
(22, '2023-12-22 00:00:00', '2023-12-05 16:17:54', '2023-12-05 00:00:00', '2023-12-31 00:00:00', '2024-01-18 11:12:33', '2023-12-06 00:00:00', 72, 'Session NORMALE 2023', 3, NULL, NULL, 9, 1, b'0', b'1');

-- --------------------------------------------------------

--
-- Structure de la table `tokens`
--

DROP TABLE IF EXISTS `tokens`;
CREATE TABLE IF NOT EXISTS `tokens` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `expiry_date` datetime DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2dylsfo39lgjyqml2tbe0b0ss` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `tokens`
--

INSERT INTO `tokens` (`id`, `expiry_date`, `token`, `user_id`) VALUES
(1, NULL, 'afe78139-68f6-4459-ab1d-e7e889d6cacf', 1),
(2, NULL, 'cd9aed07-7bb4-464a-a96d-cd14754506b4', 2),
(3, NULL, '7d4d3813-5719-4ede-9b6d-6c12a8a21619', 4),
(4, NULL, 'cbacd180-0cda-4a4a-bcdb-5fd551cd478b', 5),
(5, NULL, '57dd3c54-25be-4220-9155-ddd040431c50', 10),
(6, NULL, 'a9ec3272-346b-4d91-8706-5fceb5969af9', 11),
(7, NULL, '8e247110-f719-4cbe-807c-5a6155374881', 12),
(8, NULL, 'f6d70dc1-332f-446e-9a97-22318695e59f', 13),
(9, NULL, '99f4ab62-12f4-424b-8be5-03f1c379b1b8', 14),
(10, NULL, 'c129e140-032d-43fb-b7e5-9969d8f2af7f', 19),
(11, NULL, 'f7f5a8c9-cd50-48f8-9668-a7d60c816811', 22),
(12, NULL, '0f1e8fed-9a76-4026-9ffb-4fd0e1b58f9d', 24),
(13, NULL, '28f9087f-6249-478a-9d5f-1d7d878b1d76', 25),
(14, NULL, '75e472ab-257d-4484-ba2d-c3effd4ae019', 26),
(15, NULL, '206e3ba0-2550-4e60-9516-9e874e4f6839', 27),
(16, NULL, 'f44c7e51-e729-4471-bc97-9f7774e661ab', 28),
(17, NULL, 'ab6e80e2-04d2-42d0-a79e-3e21f9fe65ac', 29),
(18, NULL, '13cc84d4-aa8b-4f9c-8318-df9b5df0db59', 30),
(19, NULL, 'e0596ee3-ebe2-418a-abd5-1ecf97902d92', 31),
(20, NULL, 'a1e215b1-684f-4fa4-badc-0efd4df26da2', 32),
(21, NULL, '16b5d2da-61c2-4300-90b2-1fa90f8ebb86', 33),
(22, NULL, 'c3b688c5-befc-4e7d-928f-b395c9ae2463', 34),
(23, NULL, '6a9bd5d2-7167-474e-af3c-7dfb5188479d', 35),
(24, NULL, '964cbf4d-fa76-4bca-a4c7-860a49d0d793', 36),
(25, NULL, 'a9d539ed-bbc0-41fd-8ba8-0ebfc4143986', 37),
(26, NULL, '82e053cf-c6bb-4959-a359-6809956ca280', 38),
(27, NULL, '8f3073bc-171a-4328-91bf-a3e038a9665e', 39),
(28, NULL, '7f86b7dc-129d-44f2-9c29-3e8e6850a590', 40);

-- --------------------------------------------------------

--
-- Structure de la table `type_centre`
--

DROP TABLE IF EXISTS `type_centre`;
CREATE TABLE IF NOT EXISTS `type_centre` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `libelle_court` varchar(255) NOT NULL,
  `libelle_long` varchar(255) NOT NULL,
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKd7omymyvcorrvuqxrjtm63nx6` (`libelle_long`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `type_centre`
--

INSERT INTO `type_centre` (`id`, `date_creation`, `date_modification`, `libelle_court`, `libelle_long`, `uti_cree`, `uti_modifie`) VALUES
(1, '2023-11-24 13:38:25', '2023-12-21 09:59:19', 'PL', 'PRINCIPAL', NULL, NULL),
(3, '2023-11-27 11:01:31', '2023-11-27 11:01:31', '2 nd', 'SECONDAIRE', NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `type_etablissement`
--

DROP TABLE IF EXISTS `type_etablissement`;
CREATE TABLE IF NOT EXISTS `type_etablissement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `libelle_court` varchar(255) NOT NULL,
  `libelle_long` varchar(255) NOT NULL,
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  `nombre_point` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK95588vc6a9tis285yf4hnqkex` (`libelle_long`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `type_etablissement`
--

INSERT INTO `type_etablissement` (`id`, `date_creation`, `date_modification`, `libelle_court`, `libelle_long`, `uti_cree`, `uti_modifie`, `nombre_point`) VALUES
(1, '2023-11-24 12:07:41', '2023-11-27 11:26:46', 'universite', 'Université', NULL, NULL, '50'),
(2, '2023-11-27 11:11:14', '2023-11-27 11:27:31', 'IA', 'Inspections d\'académie', NULL, NULL, '30'),
(3, '2023-11-27 11:15:15', '2023-11-27 11:27:41', 'C.A.0', 'Centres académiques d’orientation', NULL, NULL, '30'),
(4, '2023-11-27 11:16:00', '2023-11-27 11:27:49', 'CRFPE', 'CRFPE', NULL, NULL, '30');

-- --------------------------------------------------------

--
-- Structure de la table `type_session`
--

DROP TABLE IF EXISTS `type_session`;
CREATE TABLE IF NOT EXISTS `type_session` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `libelle_long` varchar(255) NOT NULL,
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKeiyq4j5d1wp3hc5cx2ar4autl` (`libelle_long`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `type_session`
--

INSERT INTO `type_session` (`id`, `date_creation`, `date_modification`, `libelle_long`, `uti_cree`, `uti_modifie`) VALUES
(1, '2023-11-24 14:18:29', '2023-11-24 14:18:29', 'NORMALE', NULL, NULL),
(2, '2023-11-27 11:40:39', '2023-11-29 11:51:07', 'REMPLACEMENT', NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_non_expired` bit(1) NOT NULL,
  `account_non_locked` bit(1) NOT NULL,
  `anciennete` int(11) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `credentials_non_expired` bit(1) NOT NULL,
  `date_creation` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `date_naiss` datetime NOT NULL,
  `email` varchar(100) NOT NULL,
  `identifiant` varchar(255) DEFAULT NULL,
  `is_enabled` bit(1) NOT NULL,
  `is_locked` bit(1) NOT NULL,
  `join_date` datetime DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `matricule` varchar(255) NOT NULL,
  `mdpasse` varchar(255) NOT NULL,
  `nom` varchar(40) NOT NULL,
  `prenoms` varchar(40) NOT NULL,
  `profile_image_url` varchar(255) DEFAULT NULL,
  `sexe` varchar(255) NOT NULL,
  `telephone` varchar(255) NOT NULL,
  `username` varchar(50) NOT NULL,
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  `fonction_id` bigint(20) DEFAULT NULL,
  `etablissement_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UK_71vrxovabe8x9tom8xwefi3e7` (`code`),
  UNIQUE KEY `UK71vrxovabe8x9tom8xwefi3e7` (`code`),
  KEY `FKf82gw8kj6qhvoje3cqqcqby1g` (`etablissement_id`),
  KEY `FK8auunvta6y6b6d6afue2iyi4e` (`fonction_id`)
) ENGINE=MyISAM AUTO_INCREMENT=42 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `account_non_expired`, `account_non_locked`, `anciennete`, `code`, `credentials_non_expired`, `date_creation`, `date_modification`, `date_naiss`, `email`, `identifiant`, `is_enabled`, `is_locked`, `join_date`, `last_login_date`, `matricule`, `mdpasse`, `nom`, `prenoms`, `profile_image_url`, `sexe`, `telephone`, `username`, `uti_cree`, `uti_modifie`, `fonction_id`, `etablissement_id`) VALUES
(24, b'0', b'0', 2, 'fallou12', b'0', '2023-12-14 15:25:32', '2023-12-14 15:26:05', '2024-12-08 00:00:00', 'fallouofficediouf@gmail.com', NULL, b'1', b'0', NULL, NULL, 'matricule3', '$2a$10$6RwccS9Wfi8ZHMzjZsIJq.NeFtvrv9Okyvo6XgfDkudK9O5ebfqNm', 'TESTN', 'fallouoffice', '', 'masculin', '778901212', 'test1', NULL, NULL, 3, 10),
(22, b'0', b'0', 2, 'mamediarra12', b'0', '2023-12-14 15:13:23', '2023-12-14 15:15:16', '2024-12-08 00:00:00', 'mamediarraoffice@gmail.com', NULL, b'1', b'0', NULL, NULL, 'matricule2', '$2a$10$LVOxPzeqhwb4879EbZ1.MusR4b/bpfyyjidcFQmtWUTBwqZCnyGxG', 'TESTN', 'mamediarra', '', 'feminin', '778901212', 'test2', NULL, NULL, 1, 11),
(25, b'0', b'0', 2, 'fallouthiaw12', b'0', '2023-12-14 15:27:39', '2023-12-14 15:30:04', '2024-12-08 00:00:00', 'fallouthiawoffice@gmail.com', NULL, b'1', b'0', NULL, NULL, 'matricule3', '$2a$10$jPxnouZ3WrTOhhMRowH7MOxRZ2.vH0X5dc4RhZq52c4lgrRZ2OmUa', 'TESTN', 'fallouthiawoffice', '', 'masculin', '778901212', 'test4', NULL, NULL, 3, 10),
(26, b'0', b'0', 2, 'gueye12', b'0', '2023-12-14 15:31:40', '2024-01-12 11:16:10', '2024-12-08 00:00:00', 'gueyedaoudagueye5@gmail.com', NULL, b'0', b'0', NULL, NULL, 'matricule4', '$2a$10$pPYUThOp5sfsNiqrPNQrKu/srab6VATHhR6B/fFRDxxReShqENTeK', 'gueye', 'gueyedaouda', NULL, 'masculin', '778901212', 'test5', NULL, NULL, 3, 10),
(27, b'0', b'0', 2, 'khabane12', b'0', '2023-12-14 15:36:41', '2023-12-14 15:37:06', '2024-12-08 00:00:00', 'khabaneoffice@gmail.com', NULL, b'1', b'0', NULL, NULL, 'matricule5', '$2a$10$mZfQDwqBlveoZL0geIg4G.AKD2KI1ISc2nTGZHBPzfl1IsKp6l1Pq', 'TESTN', 'khabaneoffice', '', 'masculin', '778901212', 'test6', NULL, NULL, 3, 10),
(40, b'0', b'0', NULL, NULL, b'0', '2024-01-16 10:20:40', '2024-01-16 10:22:37', '2024-12-08 00:00:00', 'falloudiarra5@gmail.com', NULL, b'1', b'0', NULL, NULL, 'matriculepl1', '$2a$10$h5dt3T1CbHAdOE5OYsIv8.Mt.ebjESJjTsZmeRI7Ad6usNZ.rfFkC', 'DIOUF', 'fallou', NULL, 'masculin', '778901212', 'planif1', NULL, NULL, NULL, NULL),
(34, b'0', b'0', NULL, NULL, b'0', '2024-01-15 09:46:52', '2024-01-15 09:47:46', '2024-12-08 00:00:00', 'diouffadel406@gmail.com', NULL, b'1', b'0', NULL, NULL, 'matriculePl', '$2a$10$NJObPIFiTX0kmnwOfFbPIeL81vrCitbKfOihpYSFvVsUwHvGmSJE.', 'DIOUF', 'fallou123', NULL, 'masculin', '778901212', 'sup1', NULL, NULL, NULL, NULL),
(36, b'0', b'0', NULL, NULL, b'0', '2024-01-15 15:05:43', '2024-01-16 10:03:24', '2024-01-24 00:00:00', 'bala@ndiaye', NULL, b'0', b'0', NULL, NULL, 'mat123', '$2a$10$HY7Q079tdLMKQyIQB/50b.MnMUh3Di4PmC.8C55VOdLCJzjn5YTbK', 'ndiaye', 'bala', NULL, 'Homme', '775721212', 'planif2', NULL, NULL, NULL, NULL),
(37, b'0', b'0', NULL, NULL, b'0', '2024-01-15 15:18:57', '2024-01-15 15:18:57', '2024-01-30 00:00:00', 'bala@ndiaye1', NULL, b'0', b'0', NULL, NULL, 'bala123', '$2a$10$lq5vZXnKlnmBhlThsFe2A.4nw7ZNFMO8TzfT19hkJqkt1BAz11dla', 'bala', 'ndiaye ndiaye', NULL, 'Homme', '66666666666', 'planif3', NULL, NULL, NULL, NULL),
(38, b'0', b'0', NULL, NULL, b'0', '2024-01-15 15:35:25', '2024-01-15 15:35:25', '2024-01-31 00:00:00', 'tall@mariama', NULL, b'0', b'0', NULL, NULL, 'tall123', '$2a$10$luEG0uvBX9qBQM9waghdeOZcTVlSlfbx0SH.46FudCC2v5ok13KsW', 'tall', 'mariama', NULL, 'Femme', '77777', 'sup2', NULL, NULL, NULL, NULL),
(41, b'0', b'0', 2, 'code123', b'0', '2024-01-18 16:03:51', '2024-01-18 16:04:20', '2024-01-25 00:00:00', 'dioufyama@yama', NULL, b'0', b'0', NULL, NULL, 'ccccc', '$2a$10$bzsw6/8e1tN1z88azsNprO.YUUPvMfQLJPu.Mcd0K0igMk5wnKgXS', 'paser', 'compte', NULL, 'Femme', 'cccc', 'ccccc', NULL, NULL, 4, 11);

-- --------------------------------------------------------

--
-- Structure de la table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
CREATE TABLE IF NOT EXISTS `users_roles` (
  `app_user_id` bigint(20) NOT NULL,
  `roles_id` bigint(20) NOT NULL,
  KEY `FKa62j07k5mhgifpp955h37ponj` (`roles_id`),
  KEY `FKar2y0lww0xn3x3aoqfg9qsgr5` (`app_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `users_roles`
--

INSERT INTO `users_roles` (`app_user_id`, `roles_id`) VALUES
(34, 4),
(24, 1),
(22, 1),
(26, 1),
(27, 1),
(33, 3),
(25, 1),
(35, 2),
(36, 3),
(39, 3),
(40, 3);

-- --------------------------------------------------------

--
-- Structure de la table `ville`
--

DROP TABLE IF EXISTS `ville`;
CREATE TABLE IF NOT EXISTS `ville` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `libelle_court` varchar(255) NOT NULL,
  `libelle_long` varchar(255) NOT NULL,
  `total_jury` int(11) DEFAULT '0',
  `uti_cree` bigint(20) DEFAULT NULL,
  `uti_modifie` bigint(20) DEFAULT NULL,
  `academie_id` bigint(20) DEFAULT NULL,
  `total_demandes` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcpcjuejqflsqqv087m59npy5d` (`academie_id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `ville`
--

INSERT INTO `ville` (`id`, `date_creation`, `date_modification`, `libelle_court`, `libelle_long`, `total_jury`, `uti_cree`, `uti_modifie`, `academie_id`, `total_demandes`) VALUES
(1, '2023-12-18 15:33:29', '2023-12-18 15:33:29', 'PA', 'PARCELLE', 2, NULL, NULL, 3, 0),
(2, '2023-12-18 15:33:52', '2023-12-18 15:33:52', 'GW', 'GUEDIAWAYE', 1, NULL, NULL, 3, 0),
(3, '2023-12-19 10:13:24', '2023-12-19 10:13:24', 'TT', 'TOUBA TOUL', 1, NULL, NULL, 1, 0),
(4, '2023-12-19 16:51:11', '2023-12-19 16:51:11', 'KH', 'KHOMBOLE', 0, NULL, NULL, 1, 0),
(5, '2023-12-26 15:17:12', '2023-12-26 15:17:12', 'BM', 'BAMBEY', 0, NULL, NULL, 5, 0),
(6, '2023-12-26 15:17:30', '2023-12-26 15:17:30', 'RF', 'REFANE', 0, NULL, NULL, 5, 0),
(7, '2023-12-27 10:53:17', '2023-12-27 10:53:17', 'TIV', 'TIVAOUNE', 1, NULL, NULL, 1, 0),
(8, '2023-12-29 10:44:21', '2023-12-29 10:44:21', 'ND', 'NDONDOL', 0, NULL, NULL, 5, 0);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
