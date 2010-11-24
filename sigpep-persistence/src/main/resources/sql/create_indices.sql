USE :schemaName;

--peptide--
CREATE INDEX idx_peptide_feature_sequence_id ON peptide_feature(sequence_id);
CREATE INDEX idx_peptide_feature_peptide_id ON peptide_feature(peptide_id);

--peptide2protease--
CREATE INDEX idx_protease2peptide_feature_protease_id ON protease2peptide_feature(protease_id);
CREATE INDEX idx_protease2peptide_feature_peptide_feature_id ON protease2peptide_feature(peptide_feature_id);

--protein2sequence
CREATE INDEX idx_protein2sequence_protein_id ON protein2sequence(protein_id);
CREATE INDEX idx_protein2sequence_sequence_id ON protein2sequence(sequence_id);

--peptide_mass--
CREATE INDEX idx_peptide_mass ON peptide(mass);

--splice_event_location--
CREATE INDEX idx_splice_event_feature_sequence_id ON splice_event_feature(sequence_id);
CREATE INDEX idx_splice_event_feature_splice_event_id ON splice_event_feature(splice_event_id);

--peptide2splice_event--
--CREATE INDEX idx_peptide_feature2splice_event_feature_splice_event_feature_id ON peptide2splice_event(splice_event_feature_id);
--CREATE INDEX idx_peptide_feature2splice_event_feature_peptide_feature_id ON peptide2splice_event(peptide_feature_id);