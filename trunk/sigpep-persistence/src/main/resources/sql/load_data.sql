--USE sigpep_arabidopsis_thaliana;
--LOAD DATA LOCAL INFILE '/data/sigpep/arabidopsis_thaliana/sql/organism.sql' INTO TABLE organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/arabidopsis_thaliana/sql/gene.sql' INTO TABLE gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/arabidopsis_thaliana/sql/gene2organism.sql' INTO TABLE gene2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/arabidopsis_thaliana/sql/protein.sql' INTO TABLE protein;
--LOAD DATA LOCAL INFILE '/data/sigpep/arabidopsis_thaliana/sql/protein2gene.sql' INTO TABLE protein2gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/arabidopsis_thaliana/sql/protein2organism.sql' INTO TABLE protein2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/arabidopsis_thaliana/sql/protease.sql' INTO TABLE protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/arabidopsis_thaliana/sql/peptide.sql' INTO TABLE peptide;
--LOAD DATA LOCAL INFILE '/data/sigpep/arabidopsis_thaliana/sql/peptide2protease.sql' INTO TABLE peptide2protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/arabidopsis_thaliana/sql/peptide2protein.sql' INTO TABLE peptide2protein;
INSERT INTO signature_peptide SELECT peptide_id FROM peptide2protein GROUP BY peptide_id HAVING count(distinct protein_id) = 1
INSERT INTO sequence2signature_protease(sequence_id,protease_id,signature_peptide_count)
SELECT pep2pro.sequence_id,
       pep2prot.protease_id,
       count(sp.peptide_id)
FROM signature_peptide sp,
         peptide2protease pep2prot,
         peptide pep2pro
WHERE sp.peptide_id = pep2pro.peptide_id
    AND sp.peptide_id = pep2prot.peptide_id
GROUP BY pep2pro.sequence_id,
         pep2prot.protease_id;

--USE sigpep_bos_taurus;
--LOAD DATA LOCAL INFILE '/data/sigpep/bos_taurus/sql/organism.sql' INTO TABLE organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/bos_taurus/sql/gene.sql' INTO TABLE gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/bos_taurus/sql/gene2organism.sql' INTO TABLE gene2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/bos_taurus/sql/protein.sql' INTO TABLE protein;
--LOAD DATA LOCAL INFILE '/data/sigpep/bos_taurus/sql/protein2gene.sql' INTO TABLE protein2gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/bos_taurus/sql/protein2organism.sql' INTO TABLE protein2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/bos_taurus/sql/protease.sql' INTO TABLE protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/bos_taurus/sql/peptide.sql' INTO TABLE peptide;
--LOAD DATA LOCAL INFILE '/data/sigpep/bos_taurus/sql/peptide2protease.sql' INTO TABLE peptide2protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/bos_taurus/sql/peptide2protein.sql' INTO TABLE peptide2protein;
INSERT INTO signature_peptide SELECT peptide_id FROM peptide2protein GROUP BY peptide_id HAVING count(distinct protein_id) = 1
INSERT INTO protein2signature_protease(protein_id,protease_id,signature_peptide_count)
SELECT pep2pro.protein_id,
       pep2prot.protease_id,
       count(sp.peptide_id)
FROM signature_peptide sp,
         peptide2protease pep2prot,
         peptide2protein pep2pro
WHERE sp.peptide_id = pep2pro.peptide_id
    AND sp.peptide_id = pep2prot.peptide_id
GROUP BY pep2pro.protein_id,
         pep2prot.protease_id;


USE sigpep_caenorhabditis_elegans;
LOAD DATA LOCAL INFILE '/data/sigpep/caenorhabditis_elegans/sql/organism.sql' INTO TABLE organism;
LOAD DATA LOCAL INFILE '/data/sigpep/caenorhabditis_elegans/sql/gene.sql' INTO TABLE gene;
LOAD DATA LOCAL INFILE '/data/sigpep/caenorhabditis_elegans/sql/gene2organism.sql' INTO TABLE gene2organism;
LOAD DATA LOCAL INFILE '/data/sigpep/caenorhabditis_elegans/sql/protein.sql' INTO TABLE protein;
LOAD DATA LOCAL INFILE '/data/sigpep/caenorhabditis_elegans/sql/protein2gene.sql' INTO TABLE protein2gene;
LOAD DATA LOCAL INFILE '/data/sigpep/caenorhabditis_elegans/sql/protein2organism.sql' INTO TABLE protein2organism;
LOAD DATA LOCAL INFILE '/data/sigpep/caenorhabditis_elegans/sql/protease.sql' INTO TABLE protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/caenorhabditis_elegans/sql/peptide.sql' INTO TABLE peptide;
LOAD DATA LOCAL INFILE '/data/sigpep/caenorhabditis_elegans/sql/peptide2protease.sql' INTO TABLE peptide2protease;
LOAD DATA LOCAL INFILE '/data/sigpep/caenorhabditis_elegans/sql/peptide2protein.sql' INTO TABLE peptide2protein;
INSERT INTO signature_peptide SELECT peptide_id FROM peptide2protein GROUP BY peptide_id HAVING count(distinct protein_id) = 1
INSERT INTO protein2signature_protease(protein_id,protease_id,signature_peptide_count)
SELECT pep2pro.protein_id,
       pep2prot.protease_id,
       count(sp.peptide_id)
FROM signature_peptide sp,
         peptide2protease pep2prot,
         peptide2protein pep2pro
WHERE sp.peptide_id = pep2pro.peptide_id
    AND sp.peptide_id = pep2prot.peptide_id
GROUP BY pep2pro.protein_id,
         pep2prot.protease_id;


--USE sigpep_danio_rerio;
--LOAD DATA LOCAL INFILE '/data/sigpep/danio_rerio/sql/organism.sql' INTO TABLE organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/danio_rerio/sql/gene.sql' INTO TABLE gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/danio_rerio/sql/gene2organism.sql' INTO TABLE gene2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/danio_rerio/sql/protein.sql' INTO TABLE protein;
--LOAD DATA LOCAL INFILE '/data/sigpep/danio_rerio/sql/protein2gene.sql' INTO TABLE protein2gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/danio_rerio/sql/protein2organism.sql' INTO TABLE protein2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/danio_rerio/sql/protease.sql' INTO TABLE protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/danio_rerio/sql/peptide.sql' INTO TABLE peptide;
--LOAD DATA LOCAL INFILE '/data/sigpep/danio_rerio/sql/peptide2protease.sql' INTO TABLE peptide2protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/danio_rerio/sql/peptide2protein.sql' INTO TABLE peptide2protein;
INSERT INTO signature_peptide SELECT peptide_id FROM peptide2protein GROUP BY peptide_id HAVING count(distinct protein_id) = 1
INSERT INTO protein2signature_protease(protein_id,protease_id,signature_peptide_count)
SELECT pep2pro.protein_id,
       pep2prot.protease_id,
       count(sp.peptide_id)
FROM signature_peptide sp,
         peptide2protease pep2prot,
         peptide2protein pep2pro
WHERE sp.peptide_id = pep2pro.peptide_id
    AND sp.peptide_id = pep2prot.peptide_id
GROUP BY pep2pro.protein_id,
         pep2prot.protease_id;


--USE sigpep_drosophila_melanogaster;
--LOAD DATA LOCAL INFILE '/data/sigpep/drosophila_melanogaster/sql/organism.sql' INTO TABLE organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/drosophila_melanogaster/sql/gene.sql' INTO TABLE gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/drosophila_melanogaster/sql/gene2organism.sql' INTO TABLE gene2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/drosophila_melanogaster/sql/protein.sql' INTO TABLE protein;
--LOAD DATA LOCAL INFILE '/data/sigpep/drosophila_melanogaster/sql/protein2gene.sql' INTO TABLE protein2gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/drosophila_melanogaster/sql/protein2organism.sql' INTO TABLE protein2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/drosophila_melanogaster/sql/protease.sql' INTO TABLE protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/drosophila_melanogaster/sql/peptide.sql' INTO TABLE peptide;
--LOAD DATA LOCAL INFILE '/data/sigpep/drosophila_melanogaster/sql/peptide2protease.sql' INTO TABLE peptide2protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/drosophila_melanogaster/sql/peptide2protein.sql' INTO TABLE peptide2protein;
INSERT INTO signature_peptide SELECT peptide_id FROM peptide2protein GROUP BY peptide_id HAVING count(distinct protein_id) = 1
INSERT INTO protein2signature_protease(protein_id,protease_id,signature_peptide_count)
SELECT pep2pro.protein_id,
       pep2prot.protease_id,
       count(sp.peptide_id)
FROM signature_peptide sp,
         peptide2protease pep2prot,
         peptide2protein pep2pro
WHERE sp.peptide_id = pep2pro.peptide_id
    AND sp.peptide_id = pep2prot.peptide_id
GROUP BY pep2pro.protein_id,
         pep2prot.protease_id;

--USE sigpep_gallus_gallus;
--LOAD DATA LOCAL INFILE '/data/sigpep/gallus_gallus/sql/organism.sql' INTO TABLE organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/gallus_gallus/sql/gene.sql' INTO TABLE gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/gallus_gallus/sql/gene2organism.sql' INTO TABLE gene2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/gallus_gallus/sql/protein.sql' INTO TABLE protein;
--LOAD DATA LOCAL INFILE '/data/sigpep/gallus_gallus/sql/protein2gene.sql' INTO TABLE protein2gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/gallus_gallus/sql/protein2organism.sql' INTO TABLE protein2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/gallus_gallus/sql/protease.sql' INTO TABLE protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/gallus_gallus/sql/peptide.sql' INTO TABLE peptide;
--LOAD DATA LOCAL INFILE '/data/sigpep/gallus_gallus/sql/peptide2protease.sql' INTO TABLE peptide2protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/gallus_gallus/sql/peptide2protein.sql' INTO TABLE peptide2protein;
INSERT INTO signature_peptide SELECT peptide_id FROM peptide2protein GROUP BY peptide_id HAVING count(distinct protein_id) = 1
INSERT INTO protein2signature_protease(protein_id,protease_id,signature_peptide_count)
SELECT pep2pro.protein_id,
       pep2prot.protease_id,
       count(sp.peptide_id)
FROM signature_peptide sp,
         peptide2protease pep2prot,
         peptide2protein pep2pro
WHERE sp.peptide_id = pep2pro.peptide_id
    AND sp.peptide_id = pep2prot.peptide_id
GROUP BY pep2pro.protein_id,
         pep2prot.protease_id;

--USE sigpep_homo_sapiens;
--LOAD DATA LOCAL INFILE '/data/sigpep/homo_sapiens/sql/organism.sql' INTO TABLE organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/homo_sapiens/sql/gene.sql' INTO TABLE gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/homo_sapiens/sql/gene2organism.sql' INTO TABLE gene2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/homo_sapiens/sql/protein.sql' INTO TABLE protein;
--LOAD DATA LOCAL INFILE '/data/sigpep/homo_sapiens/sql/protein2gene.sql' INTO TABLE protein2gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/homo_sapiens/sql/protein2organism.sql' INTO TABLE protein2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/homo_sapiens/sql/protease.sql' INTO TABLE protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/homo_sapiens/sql/peptide.sql' INTO TABLE peptide;
--LOAD DATA LOCAL INFILE '/data/sigpep/homo_sapiens/sql/peptide2protease.sql' INTO TABLE peptide2protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/homo_sapiens/sql/peptide2protein.sql' INTO TABLE peptide2protein;
INSERT INTO signature_peptide SELECT peptide_id FROM peptide2protein GROUP BY peptide_id HAVING count(distinct protein_id) = 1
INSERT INTO protein2signature_protease(protein_id,protease_id,signature_peptide_count)
SELECT pep2pro.protein_id,
       pep2prot.protease_id,
       count(sp.peptide_id)
FROM signature_peptide sp,
         peptide2protease pep2prot,
         peptide2protein pep2pro
WHERE sp.peptide_id = pep2pro.peptide_id
    AND sp.peptide_id = pep2prot.peptide_id
GROUP BY pep2pro.protein_id,
         pep2prot.protease_id;

--USE sigpep_mus_musculus;
--LOAD DATA LOCAL INFILE '/data/sigpep/mus_musculus/sql/organism.sql' INTO TABLE organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/mus_musculus/sql/gene.sql' INTO TABLE gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/mus_musculus/sql/gene2organism.sql' INTO TABLE gene2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/mus_musculus/sql/protein.sql' INTO TABLE protein;
--LOAD DATA LOCAL INFILE '/data/sigpep/mus_musculus/sql/protein2gene.sql' INTO TABLE protein2gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/mus_musculus/sql/protein2organism.sql' INTO TABLE protein2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/mus_musculus/sql/protease.sql' INTO TABLE protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/mus_musculus/sql/peptide.sql' INTO TABLE peptide;
--LOAD DATA LOCAL INFILE '/data/sigpep/mus_musculus/sql/peptide2protease.sql' INTO TABLE peptide2protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/mus_musculus/sql/peptide2protein.sql' INTO TABLE peptide2protein;
INSERT INTO signature_peptide SELECT peptide_id FROM peptide2protein GROUP BY peptide_id HAVING count(distinct protein_id) = 1
INSERT INTO protein2signature_protease(protein_id,protease_id,signature_peptide_count)
SELECT pep2pro.protein_id,
       pep2prot.protease_id,
       count(sp.peptide_id)
FROM signature_peptide sp,
         peptide2protease pep2prot,
         peptide2protein pep2pro
WHERE sp.peptide_id = pep2pro.peptide_id
    AND sp.peptide_id = pep2prot.peptide_id
GROUP BY pep2pro.protein_id,
         pep2prot.protease_id;

--USE sigpep_rattus_norvegicus
--LOAD DATA LOCAL INFILE '/data/sigpep/rattus_norvegicus/sql/organism.sql' INTO TABLE organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/rattus_norvegicus/sql/gene.sql' INTO TABLE gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/rattus_norvegicus/sql/gene2organism.sql' INTO TABLE gene2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/rattus_norvegicus/sql/protein.sql' INTO TABLE protein;
--LOAD DATA LOCAL INFILE '/data/sigpep/rattus_norvegicus/sql/protein2gene.sql' INTO TABLE protein2gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/rattus_norvegicus/sql/protein2organism.sql' INTO TABLE protein2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/rattus_norvegicus/sql/protease.sql' INTO TABLE protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/rattus_norvegicus/sql/peptide.sql' INTO TABLE peptide;
--LOAD DATA LOCAL INFILE '/data/sigpep/rattus_norvegicus/sql/peptide2protease.sql' INTO TABLE peptide2protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/rattus_norvegicus/sql/peptide2protein.sql' INTO TABLE peptide2protein;
INSERT INTO signature_peptide SELECT peptide_id FROM peptide2protein GROUP BY peptide_id HAVING count(distinct protein_id) = 1
INSERT INTO protein2signature_protease(protein_id,protease_id,signature_peptide_count)
SELECT pep2pro.protein_id,
       pep2prot.protease_id,
       count(sp.peptide_id)
FROM signature_peptide sp,
         peptide2protease pep2prot,
         peptide2protein pep2pro
WHERE sp.peptide_id = pep2pro.peptide_id
    AND sp.peptide_id = pep2prot.peptide_id
GROUP BY pep2pro.protein_id,
         pep2prot.protease_id;

--USE sigpep_saccharomyces_cerevisiae;
--LOAD DATA LOCAL INFILE '/data/sigpep/saccharomyces_cerevisiae/sql/organism.sql' INTO TABLE organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/saccharomyces_cerevisiae/sql/gene.sql' INTO TABLE gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/saccharomyces_cerevisiae/sql/gene2organism.sql' INTO TABLE gene2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/saccharomyces_cerevisiae/sql/protein.sql' INTO TABLE protein;
--LOAD DATA LOCAL INFILE '/data/sigpep/saccharomyces_cerevisiae/sql/protein2gene.sql' INTO TABLE protein2gene;
--LOAD DATA LOCAL INFILE '/data/sigpep/saccharomyces_cerevisiae/sql/protein2organism.sql' INTO TABLE protein2organism;
--LOAD DATA LOCAL INFILE '/data/sigpep/saccharomyces_cerevisiae/sql/protease.sql' INTO TABLE protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/saccharomyces_cerevisiae/sql/peptide.sql' INTO TABLE peptide;
--LOAD DATA LOCAL INFILE '/data/sigpep/saccharomyces_cerevisiae/sql/peptide2protease.sql' INTO TABLE peptide2protease;
--LOAD DATA LOCAL INFILE '/data/sigpep/saccharomyces_cerevisiae/sql/peptide2protein.sql' INTO TABLE peptide2protein;
INSERT INTO signature_peptide SELECT peptide_id FROM peptide2protein GROUP BY peptide_id HAVING count(distinct protein_id) = 1
INSERT INTO protein2signature_protease(protein_id,protease_id,signature_peptide_count)
SELECT pep2pro.protein_id,
       pep2prot.protease_id,
       count(sp.peptide_id)
FROM signature_peptide sp,
         peptide2protease pep2prot,
         peptide2protein pep2pro
WHERE sp.peptide_id = pep2pro.peptide_id
    AND sp.peptide_id = pep2prot.peptide_id
GROUP BY pep2pro.protein_id,
         pep2prot.protease_id;
