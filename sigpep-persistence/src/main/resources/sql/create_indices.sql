USE :schemaName;

--peptide--
CREATE INDEX idx_peptide_id ON peptide(peptide_id);
CREATE INDEX idx_sequence_id ON peptide(sequence_id);
--CREATE INDEX idx_start_pos ON peptide(start_pos);
--CREATE INDEX idx_end_pos ON peptide(end_pos);
CREATE INDEX idx_peptide_mass ON peptide(mass);

--protein2sequence
CREATE INDEX idx_protein2sequence_protein_id ON protein2sequence(protein_id);
CREATE INDEX idx_protein2sequence_sequence_id ON protein2sequence(sequence_id);
