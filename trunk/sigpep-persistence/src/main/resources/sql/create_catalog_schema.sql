CREATE TABLE organism(
	organism_id   INT UNSIGNED NOT NULL AUTO_INCREMENT,
	organism_name VARCHAR(255),
	ncbi_taxon_id INT NOT NULL,
	PRIMARY KEY (organism_id),
	INDEX (ncbi_taxon_id)
)ENGINE=InnoDB;

-- Fill the
INSERT INTO sigpep_catalog.organism (organism_id, organism_name, ncbi_taxon_id) VALUES (10090,"mus musculus",10090);
INSERT INTO sigpep_catalog.organism (organism_id, organism_name, ncbi_taxon_id) VALUES (9606,"homo sapiens",9606);
INSERT INTO sigpep_catalog.organism (organism_id, organism_name, ncbi_taxon_id) VALUES (9823,"sus scrofa",9823);
INSERT INTO sigpep_catalog.organism (organism_id, organism_name, ncbi_taxon_id) VALUES (4932,"saccharomyces cerevisiae",4932);
