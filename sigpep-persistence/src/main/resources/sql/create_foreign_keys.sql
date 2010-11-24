USE :schemaName;

----------------
--foreign keys--
----------------

--peptide2protein--
-------------------

ALTER TABLE peptide ADD CONSTRAINT fk_peptide2protein_protein_id
	FOREIGN KEY (sequence_id) REFERENCES protein_sequence(sequence_id)
	ON DELETE CASCADE;

--peptide2protease--
--------------------

ALTER TABLE peptide2protease ADD CONSTRAINT fk_peptide2protease_protease_id
	FOREIGN KEY (protease_id) REFERENCES protease(protease_id)
	ON DELETE CASCADE;

ALTER TABLE peptide2protease ADD CONSTRAINT fk_peptide2protease_peptide_id
	FOREIGN KEY (peptide_id) REFERENCES peptide(peptide_id)
	ON DELETE CASCADE;

--gene2organism--
-----------------

ALTER TABLE gene2organism ADD CONSTRAINT fk_gene2organism_gene_id
	FOREIGN KEY (gene_id) REFERENCES gene(gene_id)
	ON DELETE CASCADE;

ALTER TABLE gene2organism ADD CONSTRAINT fk_gene2organism_organism_id
	FOREIGN KEY (organism_id) REFERENCES organism(organism_id)
	ON DELETE CASCADE;

--protein2gene--
----------------

ALTER TABLE protein2gene ADD CONSTRAINT fk_protein2gene_protein_id
    FOREIGN KEY (protein_id) REFERENCES protein(protein_id)
    ON DELETE CASCADE;

ALTER TABLE protein2gene ADD CONSTRAINT fk_protein2gene_gene_id
    FOREIGN KEY (gene_id) REFERENCES gene(gene_id)
    ON DELETE CASCADE;

--protein2organism--
--------------------

ALTER TABLE protein2organism ADD CONSTRAINT fk_protein2organism_protein_id
	FOREIGN KEY (protein_id) REFERENCES protein(protein_id)
	ON DELETE CASCADE;

ALTER TABLE protein2organism ADD CONSTRAINT fk_protein2organism_organism_id
	FOREIGN KEY (organism_id) REFERENCES organism(organism_id)
	ON DELETE CASCADE;

--sequence2signature_protease--
-------------------------------

ALTER TABLE sequence2signature_protease ADD CONSTRAINT fk_sequence2signature_proteas_sequence_id
	FOREIGN KEY (sequence_id) REFERENCES protein_sequence(sequence_id)
	ON DELETE CASCADE;

ALTER TABLE sequence2signature_protease ADD CONSTRAINT fk_sequence2signature_proteas_protease_id
	FOREIGN KEY (protease_id) REFERENCES protease(protease_id)
	ON DELETE CASCADE;

--peptide_mass--
----------------

ALTER TABLE peptide_mass ADD CONSTRAINT fk_peptide_mass_peptide_id
	FOREIGN KEY (peptide_id) REFERENCES peptide(peptide_id)
	ON DELETE CASCADE;

--protein2sequence--
-------------------------------

ALTER TABLE protein2sequence ADD CONSTRAINT fk_protein2sequence_sequence_id
	FOREIGN KEY (sequence_id) REFERENCES protein_sequence(sequence_id)
	ON DELETE CASCADE;

ALTER TABLE protein2sequence ADD CONSTRAINT fk_protein2sequence_protein_id
	FOREIGN KEY (protein_id) REFERENCES protein(protein_id)
	ON DELETE CASCADE;

--peptide_mass--
----------------

ALTER TABLE signature_peptide ADD CONSTRAINT fk_signature_peptide_peptide_id
	FOREIGN KEY (peptide_id) REFERENCES peptide(peptide_id)
	ON DELETE CASCADE;