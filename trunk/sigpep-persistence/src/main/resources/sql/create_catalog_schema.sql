CREATE TABLE organism(
	organism_id   INT UNSIGNED NOT NULL AUTO_INCREMENT,
	organism_name VARCHAR(255),
	ncbi_taxon_id INT NOT NULL,
	PRIMARY KEY (organism_id),
	INDEX (ncbi_taxon_id)
)ENGINE=InnoDB;
