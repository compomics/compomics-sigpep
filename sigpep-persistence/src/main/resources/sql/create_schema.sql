DROP SCHEMA IF EXISTS :schemaName;
CREATE SCHEMA :schemaName;
USE :schemaName;

--DROP SCHEMA IF EXISTS presentation_schema;
--CREATE SCHEMA presentation_schema;
--USE presentation_schema;

CREATE TABLE organism(
	organism_id   INT UNSIGNED,
	organism_name VARCHAR(255),
	ncbi_taxon_id INT NOT NULL,
	PRIMARY KEY (organism_id),
	INDEX (ncbi_taxon_id)
)ENGINE=InnoDB;

CREATE TABLE gene(
	gene_id        INT UNSIGNED,
	gene_accession VARCHAR(50) NOT NULL,
	PRIMARY KEY (gene_id),
	INDEX (gene_accession)
)ENGINE=InnoDB;

CREATE TABLE protein(
	protein_id        INT UNSIGNED,
	protein_accession VARCHAR(50) NOT NULL,
	coord_sys         VARCHAR(20) NOT NULL,
	version           VARCHAR(20) NOT NULL,
	start_pos         INT UNSIGNED NOT NULL,
	end_pos           INT UNSIGNED NOT NULL,
	strand            TINYINT(1) NOT NULL,
	known             TINYINT(1) UNSIGNED,
	PRIMARY KEY (protein_id),
	INDEX (protein_accession)
)ENGINE=InnoDB;

CREATE TABLE protein_sequence(
    sequence_id INT UNSIGNED,
    aa_sequence TEXT,
    PRIMARY KEY (sequence_id)
)ENGINE=InnoDB;

CREATE TABLE exon(
    exon_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    exon_accession VARCHAR(50) NOT NULL,
    PRIMARY KEY (exon_id)
)ENGINE=InnoDB;

CREATE TABLE protease (
	protease_id INT UNSIGNED,
	name VARCHAR(255),
	full_name VARCHAR(50),
	cleavage_site VARCHAR(30),
	PRIMARY KEY (protease_id)
)ENGINE=InnoDB;

CREATE TABLE peptide (
  `peptide_id` int(10) unsigned NOT NULL DEFAULT '0',
  `mass` double unsigned DEFAULT NULL,
  `is_signature_peptide` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`peptide_id`)
) ENGINE=InnoDB;

CREATE TABLE splice_event(
    splice_event_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    exon_id_1 VARCHAR(50) NOT NULL,
    exon_id_2 VARCHAR(50) NOT NULL,
    PRIMARY KEY (splice_event_id)
)ENGINE=InnoDB;

CREATE TABLE  `peptide2splice_event` (
  `peptide_id` int(10) unsigned NOT NULL,
  `splice_event_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`splice_event_id`,`peptide_id`)
) ENGINE=InnoDB;

--CREATE TABLE  splice_event_feature (
--  `splice_event_feature_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
--  `splice_event_id` int(10) unsigned NOT NULL,
--  `sequence_id` int(10) unsigned NOT NULL DEFAULT '0',
--  `pos_start` int(10) unsigned NOT NULL DEFAULT '0',
--  `pos_end` int(10) unsigned NOT NULL DEFAULT '0',
--  PRIMARY KEY (`splice_event_feature_id`)
--) ENGINE=InnoDB;

CREATE TABLE peptide_feature (
	peptide_feature_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
	peptide_id         INT UNSIGNED,
	sequence_id        INT UNSIGNED,
	pos_start          INT UNSIGNED,
	pos_end            INT UNSIGNED,
	PRIMARY KEY (peptide_feature_id)
	--PRIMARY KEY (peptide_id, sequence_id, pos_start, pos_end)
)ENGINE=InnoDB;

CREATE TABLE splice_event_feature(
    splice_event_feature_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    splice_event_id INT UNSIGNED NOT NULL,
    sequence_id INT UNSIGNED NOT NULL,
    pos_start INT UNSIGNED NOT NULL,
    pos_end INT UNSIGNED NOT NULL,
    PRIMARY KEY (splice_event_feature_id)
)ENGINE=InnoDB;

CREATE TABLE gene2protein(
    protein_id  INT UNSIGNED,
	gene_id     INT UNSIGNED,
	PRIMARY KEY (protein_id,gene_id)
)ENGINE=InnoDB;

CREATE TABLE protein2sequence(
    protein_id  INT UNSIGNED,
    sequence_id INT UNSIGNED,
    PRIMARY KEY (protein_id,sequence_id)
)ENGINE=InnoDB;

CREATE TABLE protease2peptide_feature (
    protease_id INT UNSIGNED,
	peptide_feature_id  INT UNSIGNED,
	PRIMARY KEY (peptide_feature_id, protease_id)
)ENGINE=InnoDB;

CREATE TABLE peptide_feature2splice_event_feature(
    peptide_feature_id INT UNSIGNED NOT NULL,
    splice_event_feature_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (splice_event_feature_id, peptide_feature_id)
)ENGINE=InnoDB;

CREATE TABLE peptide2protease (
	peptide_id  INT UNSIGNED,
	protease_id INT UNSIGNED,
	PRIMARY KEY (protease_id, peptide_id)
)ENGINE=InnoDB;

CREATE TABLE  signature_peptide (
  `peptide_id` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`peptide_id`)
)ENGINE=InnoDB;

CREATE TABLE  signature_peptide_protein (
  `peptide_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`peptide_id`)
)ENGINE=InnoDB;

CREATE TABLE  sequence2signature_protease (
  `sequence_id` int(10) unsigned NOT NULL DEFAULT '0',
  `protease_id` int(10) unsigned NOT NULL DEFAULT '0',
  `signature_peptide_count` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`sequence_id`,`protease_id`)
)ENGINE=InnoDB;

--UPDATE protease SET cleavage_site="KR" WHERE name = 'tryp'
--UPDATE protease SET cleavage_site="FL" WHERE name = 'pepa'
--UPDATE protease SET cleavage_site="R" WHERE name = 'argc'
--UPDATE protease SET cleavage_site="K" WHERE name = 'lysc'
--UPDATE protease SET cleavage_site="DNEQ" WHERE name = 'v8de'
--UPDATE protease SET cleavage_site="EQ" WHERE name = 'v8d'
--UPDATE protease SET cleavage_site="M" WHERE name = 'cnbr'

--INSERT INTO protease2peptide_feature(protease_id,peptide_feature_id)
--SELECT DISTINCT prot.protease_id, pf.peptide_feature_id
--  FROM peptide_feature pf,
--       protein_sequence seq,
--       peptide2protease prot2pep,
--       protease prot
-- WHERE pf.sequence_id=seq.sequence_id
--   AND pf.peptide_id=prot2pep.peptide_id
--   AND prot.protease_id=prot2pep.protease_id
--   AND prot.name IN ('tryp')
--   AND pf.peptide_feature_id NOT IN (
--SELECT DISTINCT pf.peptide_feature_id
--  FROM peptide_feature pf,
--       protein_sequence seq,
--       peptide2protease prot2pep,
--       protease prot
-- WHERE pf.sequence_id=seq.sequence_id
--   AND pf.peptide_id=prot2pep.peptide_id
--   AND prot.protease_id=prot2pep.protease_id
--   AND prot.name IN ('tryp')
--  AND (CHAR_LENGTH(seq.aa_sequence) != pf.pos_end AND SUBSTR(seq.aa_sequence, pf.pos_end, 1) NOT REGEXP CONCAT('[', prot.cleavage_site, ']'))
--)

--INSERT INTO peptide_feature2splice_event_feature (peptide_feature_id,splice_event_feature_id)
--SELECT pf.peptide_feature_id, sf.splice_event_feature_id
--FROM peptide_feature pf,
--     splice_event_feature sf
--WHERE pf.sequence_id=sf.sequence_id
--  AND pf.pos_start <= sf.pos_start
--  AND pf.pos_end >= sf.pos_end
