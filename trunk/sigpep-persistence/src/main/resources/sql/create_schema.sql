DROP SCHEMA IF EXISTS :schemaName;
CREATE SCHEMA :schemaName;
USE :schemaName;

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
  `sequence_id` int unsigned,
  `start_pos` INT UNSIGNED NOT NULL,
  `end_pos` INT UNSIGNED NOT NULL,
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

CREATE TABLE gene2protein(
    protein_id  INT UNSIGNED,
	gene_id     INT UNSIGNED,
	PRIMARY KEY (protein_id,gene_id)
)ENGINE=InnoDB;

CREATE TABLE gene2organism(
    gene_id  INT UNSIGNED,
	organism_id     INT UNSIGNED,
	PRIMARY KEY (gene_id,organism_id)
)ENGINE=InnoDB;

CREATE TABLE protein2sequence(
    protein_id  INT UNSIGNED,
    sequence_id INT UNSIGNED,
    PRIMARY KEY (protein_id,sequence_id)
)ENGINE=InnoDB;

CREATE TABLE protein2gene(
    protein_id  INT UNSIGNED,
    gene_id INT UNSIGNED,
    PRIMARY KEY (protein_id,gene_id)
)ENGINE=InnoDB;

CREATE TABLE protein2organism(
    protein_id  INT UNSIGNED,
    organism_id INT UNSIGNED,
    PRIMARY KEY (protein_id,organism_id)
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


-- Fill the protease table.
INSERT INTO protease (`protease_id`, `name`, `cleavage_site`, `full_name`) VALUES (1,"v8e","EQ","V8E");
INSERT INTO protease (`protease_id`, `name`, `cleavage_site`, `full_name`) VALUES (2,"tryp","KR","Trypsin");
INSERT INTO protease (`protease_id`, `name`, `cleavage_site`, `full_name`) VALUES (3,"cnbr","M","CNBr");
INSERT INTO protease (`protease_id`, `name`, `cleavage_site`, `full_name`) VALUES (4,"argc","R","Arg-C");
INSERT INTO protease (`protease_id`, `name`, `cleavage_site`, `full_name`) VALUES (5,"pepa","FL","Pepsin A");
INSERT INTO protease (`protease_id`, `name`, `cleavage_site`, `full_name`) VALUES (6,"lysc","K","Lys-C");
INSERT INTO protease (`protease_id`, `name`, `cleavage_site`, `full_name`) VALUES (7,"v8de","DNEQ","V8DE");

