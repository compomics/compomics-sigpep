package com.compomics.sigpep.model.impl;

import com.compomics.sigpep.model.*;
import com.compomics.sigpep.util.Combinations;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * @TODO: JavaDoc missing
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 14-Feb-2008<br/>
 * Time: 11:05:57<br/>
 */
public abstract class AbstractPeptide implements Peptide {

    /**
     * the logger
     */
    protected static Logger logger = Logger.getLogger(AbstractPeptide.class);

    /**
     * the precursor ion
     */
    protected PrecursorIon precursorIon;

    /**
     * the proteases that product this peptide
     */
    protected Set<Protease> proteases;

    /**
     * the peptide origin
     */
    protected Set<PeptideOrigin> origins = new HashSet<PeptideOrigin>();

    /**
     * {@inherit}
     */
    public Set<Protease> getProteases() {
        return proteases;
    }

    /**
     * {@inherit}
     */
    public void setProteases(Set<Protease> proteases) {
        this.proteases = proteases;
    }

    /**
     * {@inherit}
     */
    public PrecursorIon getPrecursorIon() {
        if (precursorIon == null) {
            precursorIon = new PrecursorIonImpl(this);
        }
        return precursorIon;
    }

    /**
     * Returns the length of the amino acid sequence.
     *
     * @return the sequence length
     */
    public int getSequenceLength() {
        return this.getSequenceString().length();
    }

    /**
     * Returns the sequence positions of a residue.
     *
     * @param residue the one letter amino acid code
     * @return the sequence positions
     */
    public Set<Integer> getResiduePositions(String residue) {

        Set<Integer> retVal = new TreeSet<Integer>();
        int position = 0;
        for (char aa : this.getSequenceString().toCharArray()) {
            position++;
            if (("" + aa).equalsIgnoreCase(residue)) {
                retVal.add(position);
            }

        }

        return retVal;
    }

    /**
     * {@inherit}
     */
    public int getResidueCount(String residue) {
        String sequenceWithoutResidues = this.getSequenceString().toUpperCase().replaceAll(residue.toUpperCase(), "");
        return this.getSequenceString().length() - sequenceWithoutResidues.length();
    }

    /**
     * Applies a post-translational modification to a peptide.
     * <p/>
     * A residue can only carry one modification. Modifications that affect residues that
     * have already been modified previously will be ignored.
     *
     * @param modification the modification
     * @return a list of modified peptides
     */
    public Set<Peptide> applyModification(Modification modification) {
        return applyModification(this, modification);
        //return removeUnmodified(peptides);
    }

    /**
     * Applies a post-translational modification to a peptide.
     * <p/>
     * A residue can only carry one modification. Modifications that affect residues that
     * have already been modified previously will be ignored.
     *
     * @param peptide                       the peptide to modify
     * @param modification the modification
     * @return a list of modified peptides
     */
    Set<Peptide> applyModification(Peptide peptide, Modification modification) {

        Set<Peptide> retVal = new HashSet<Peptide>();
        String residue = modification.getResidue();
        boolean isStatic = modification.isStatic();

        /////////
        //PROTEIN
        /////////
        //...if the modification applies to the PROTEIN...
        if (residue.equalsIgnoreCase("protein")) {

            /////////////////////////////////
            //PROTEIN -> POSITIONAL
            /////////////////////////////////

            ModifiedPeptide modPep;
            //...and the peptide is already modified
            //we will add the modification to the already
            //existing ones
            if (peptide instanceof ModifiedPeptide) {
                ModifiedPeptide original = (ModifiedPeptide) peptide;
                modPep = new ModifiedPeptideImpl(original.getUnmodifiedPeptide());
                modPep.getPostTranslationalModifications().putAll(original.getPostTranslationalModifications());

                //...if the peptide is not yet modified
                //we create a new modified peptide instance
            } else {
                modPep = new ModifiedPeptideImpl(peptide);
            }

            //...if modification is positional
            if (modification.isPositional()) {

                //check at which position it will occur (N-terminal, C-terminal)
                ModificationPosition position = modification.getPosition();

                //if position is N-terminal and the peptide can originate
                //from the N-term of a protein
                if (position == ModificationPosition.N_TERMINAL
                        && peptide.getOrigins().contains(PeptideOrigin.N_TERMINAL)) {

                    //...if the N-term is not already modified
                    if (!modPep.getPostTranslationalModifications().containsKey(ModificationPosition.N_TERMINAL.getIntegerValue())) {
                        modPep.getPostTranslationalModifications().put(ModificationPosition.N_TERMINAL.getIntegerValue(), modification);
                        retVal.add(modPep);
                    }

                    //if position is C-terminal and the peptide can originate
                    //from the C-term of a protein
                } else if (position == ModificationPosition.C_TERMINAL
                        && peptide.getOrigins().contains(PeptideOrigin.C_TERMINAL)) {

                    //...if the C-term is not already modified
                    if (!modPep.getPostTranslationalModifications().containsKey(ModificationPosition.C_TERMINAL.getIntegerValue())) {
                        modPep.getPostTranslationalModifications().put(ModificationPosition.C_TERMINAL.getIntegerValue(), modification);
                        retVal.add(modPep);
                    }
                }
            }

            if (!modification.isStatic()) {
                retVal.add(peptide);
            }

            /////////
            //RESIDUE
            /////////

        } else {

            ///////////////////
            //RESIDUE -> STATIC
            ///////////////////

            //if modification is STATIC...
            if (isStatic) {

                ModifiedPeptide modPep;
                //...and the peptide is already modified
                //we will add the modification to the already
                //existing ones
                if (peptide instanceof ModifiedPeptide) {
                    modPep = (ModifiedPeptide) peptide;

                    //...if the peptide is not yet modified
                    //we create a new modified peptide instance
                } else {
                    modPep = new ModifiedPeptideImpl(peptide);
                }

                int modifiedResidues = 0;

                /////////////////////////////////////
                //STATIC -> RESIDUE -> NON-POSITIONAL
                /////////////////////////////////////

                //...if the modification is non-positional...
                if (!modification.isPositional()) {

                    //...we apply the modification to all respective
                    //residues in the sequence...
                    for (Integer position : modPep.getResiduePositions(residue)) {

                        //...if they are not already modified
                        if (!modPep.getPostTranslationalModifications().containsKey(position)) {
                            modPep.getPostTranslationalModifications().put(position, modification);
                            modifiedResidues++;
                        }
                    }

                    /////////////////////////////////
                    //STATIC -> RESIDUE -> POSITIONAL
                    /////////////////////////////////

                    //...if the modification is positional...
                } else {

                    //...we check if the modified residue occurs at the required position
                    Set<Integer> residuePositions = modPep.getResiduePositions(residue);

                    if (modification.getPosition() == ModificationPosition.N_TERMINAL) {

                        if (residuePositions.contains(1)) {

                            //...if the position is not already modified
                            if (!modPep.getPostTranslationalModifications().containsKey(1)) {
                                modPep.getPostTranslationalModifications().put(1, modification);
                                modifiedResidues++;
                            }
                        }

                    } else if (modification.getPosition() == ModificationPosition.C_TERMINAL) {

                        int peptideLength = modPep.getSequenceLength();

                        if (residuePositions.contains(peptideLength)) {

                            //...if the position is not already modified
                            if (!modPep.getPostTranslationalModifications().containsKey(peptideLength)) {
                                modPep.getPostTranslationalModifications().put(peptideLength, modification);
                                modifiedResidues++;
                            }
                        }
                    } else if (modification.getPosition() == ModificationPosition.INTERNAL) {

                        int peptideLength = modPep.getSequenceLength();

                        for (Integer position : residuePositions) {

                            if (position != 1 && position != peptideLength) {

                                //...if the position is not already modified
                                if (!modPep.getPostTranslationalModifications().containsKey(position)) {
                                    modPep.getPostTranslationalModifications().put(position, modification);
                                    modifiedResidues++;
                                }
                            }
                        }
                    }
                }

                //if any residues were modified
                //add modified peptide to return
                //value
                if (modifiedResidues > 0) {
                    retVal.add(modPep);
                }

                ////////////////////
                //RESIDUE -> DYNAMIC
                ////////////////////

                //if modification is dynamic...
            } else if (!isStatic) {

                //////////////////////////////////////
                //RESIDUE -> DYNAMIC -> NON-POSITIONAL
                //////////////////////////////////////

                ///...and non-positional
                if (!modification.isPositional()) {

                    //... get all combinations of the residues
                    //to be modified in the sequence
                    Set<Integer> residuePositions = this.getResiduePositions(residue);
                    for (int k = 1; k <= residuePositions.size(); k++) {

                        Combinations residueCombinations = new Combinations(k, residuePositions);
                        while (residueCombinations.hasMoreElements()) {

                            //...create a new sequence for each combination
                            //and modify the respective residues...
                            ModifiedPeptide modPep = null;
                            //...and the peptide is already modified
                            //we will make a new modified peptide based
                            //on the unmodified and copy the already 
                            //existing modifications and add the new ones
                            //to the existing ones
                            if (peptide instanceof ModifiedPeptide) {
                                ModifiedPeptide original = (ModifiedPeptide) peptide;
                                modPep = new ModifiedPeptideImpl(original.getUnmodifiedPeptide());
                                modPep.getPostTranslationalModifications().putAll(original.getPostTranslationalModifications());

                                //...if the peptide is not yet modified
                                //we create a new modified peptide instance
                            } else {
                                modPep = new ModifiedPeptideImpl(peptide);
                            }

                            Set combination = residueCombinations.nextElement();
                            int modifiedResidues = 0;
                            for (Object positionObject : combination) {

                                Integer position = (Integer) positionObject;
                                //...if they are not already modified
                                if (!modPep.getPostTranslationalModifications().containsKey(position)) {
                                    modPep.getPostTranslationalModifications().put(position, modification);
                                    modifiedResidues++;
                                }
                            }

                            //if any residues were modified
                            //add modified peptide to return
                            //value
                            if (modifiedResidues > 0) {
                                retVal.add(modPep);
                            }
                        }
                    }

                    //also add unmodified peptide
                    retVal.add(peptide);

                    //////////////////////////////////////
                    //RESIDUE -> DYNAMIC -> POSITIONAL
                    //////////////////////////////////////

                } else {

                    //...create a new sequence for each combination
                    //and modify the respective residues...
                    ModifiedPeptide modPep = null;
                    //...and the peptide is already modified
                    //we will the modification to the already
                    //existing ones
                    if (peptide instanceof ModifiedPeptide) {
                        ModifiedPeptide original = (ModifiedPeptide) peptide;
                        modPep = new ModifiedPeptideImpl(original.getUnmodifiedPeptide());
                        modPep.getPostTranslationalModifications().putAll(original.getPostTranslationalModifications());

                        //...if the peptide is not yet modified
                        //we create a new modified peptide instance
                    } else {
                        modPep = new ModifiedPeptideImpl(peptide);
                    }

                    int modifiedResidues = 0;

                    //...we check if the modified residue occurs at the required position
                    Set<Integer> residuePositions = modPep.getResiduePositions(residue);

                    if (modification.getPosition() == ModificationPosition.N_TERMINAL) {

                        if (residuePositions.contains(1)) {

                            //...if the position is not already modified
                            if (!modPep.getPostTranslationalModifications().containsKey(1)) {
                                modPep.getPostTranslationalModifications().put(1, modification);
                                modifiedResidues++;
                            }
                        }

                        //if any residues were modified
                        //add modified peptide to return
                        //value
                        if (modifiedResidues > 0) {
                            retVal.add(modPep);
                        }

                    } else if (modification.getPosition() == ModificationPosition.C_TERMINAL) {

                        int peptideLength = modPep.getSequenceLength();

                        if (residuePositions.contains(peptideLength)) {

                            //...if the position is not already modified
                            if (!modPep.getPostTranslationalModifications().containsKey(peptideLength)) {
                                modPep.getPostTranslationalModifications().put(peptideLength, modification);
                                modifiedResidues++;
                            }
                        }

                        //if any residues were modified
                        //add modified peptide to return
                        //value
                        if (modifiedResidues > 0) {
                            retVal.add(modPep);
                        }

                    } else if (modification.getPosition() == ModificationPosition.INTERNAL) {

                        for (int k = 1; k <= residuePositions.size(); k++) {

                            Combinations residueCombinations = new Combinations(k, residuePositions);
                            while (residueCombinations.hasMoreElements()) {

                                //...and the peptide is already modified
                                //we will the modification to the already
                                //existing ones
                                if (peptide instanceof ModifiedPeptide) {
                                    ModifiedPeptide original = (ModifiedPeptide) peptide;
                                    modPep = new ModifiedPeptideImpl(original.getUnmodifiedPeptide());
                                    modPep.getPostTranslationalModifications().putAll(original.getPostTranslationalModifications());

                                    //...if the peptide is not yet modified
                                    //we create a new modified peptide instance
                                } else {
                                    modPep = new ModifiedPeptideImpl(peptide);
                                }

                               Set combination = residueCombinations.nextElement();

                                for (Object positionObject : combination) {

                                    Integer position = (Integer) positionObject;
                                    //...if they are not already modified
                                    if (!modPep.getPostTranslationalModifications().containsKey(position)) {
                                        modPep.getPostTranslationalModifications().put(position, modification);
                                        modifiedResidues++;
                                    }
                                }

                                //if any residues were modified
                                //add modified peptide to return
                                //value
                                if (modifiedResidues > 0) {
                                    retVal.add(modPep);
                                }
                            }
                        }
                    }
                }

                retVal.add(peptide);
            }
        }

        return retVal;
    }

    /**
     * Applies a set of post-translational modifications to a peptide.
     * <p/>
     * Static modifications will be applied first (in the order they occur in the set).
     * The remaining modifications will be applied in the order they are contained in the set.
     * A residue can only carry one modification. Modifications that affect residues that
     * have already been modified by a preceeding PTM will be ignored. If the set contains variable
     * modifications the returned peptide set will also containe peptides with none of the variably
     * modified residues modified.
     *
     * @param modifications the modifications
     * @return a list of modified peptides
     */
    public Set<Peptide> applyModifications(Set<Modification> modifications) {

        Set<Peptide> retVal = new HashSet<Peptide>();
        if(modifications == null || modifications.size() == 0){
            retVal.add(this);
            return retVal;
        }

        LinkedHashSet<Modification> ptmsWithStaticPtmsFirst = new LinkedHashSet<Modification>();
        for (Modification ptm : modifications) {
            if (ptm.isStatic()) {
                ptmsWithStaticPtmsFirst.add(ptm);
            }
        }

        for (Modification ptm : modifications) {
            if (!ptm.isStatic()) {
                ptmsWithStaticPtmsFirst.add(ptm);
            }
        }

        Iterator<Modification> ptms = ptmsWithStaticPtmsFirst.iterator();

        if (ptms.hasNext()) {
            Modification ptm = ptms.next();
            Set<Peptide> modifiedPeptides = this.applyModification(this, ptm);
            //as there might be no modifiable residue
            //we have to check if we got anything back...
            if (modifiedPeptides.size() == 0) {
                //...if not we have to add a 'dummy' to pass to
                //the recursive method
                modifiedPeptides.add(this);
            }

            //retVal.addAll(modifiedPeptides);

            retVal.addAll(applyModififcationsRecursively(modifiedPeptides, ptms));
        }

        //because we might have added an unmodified 'dummy'
        //we have to check and remove it before we return

        return retVal;
//        return removeUnmodified(retVal);
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param peptides
     * @return
     */
    private List<ModifiedPeptide> removeUnmodified(List<Peptide> peptides) {

        List<ModifiedPeptide> retVal = new ArrayList<ModifiedPeptide>();

        for (Peptide peptide : peptides) {
            if (peptide.isModified()) {
                retVal.add((ModifiedPeptide) peptide);
            }
        }

        return retVal;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param peptides
     * @param ptms
     * @return
     */
    private Set<Peptide> applyModififcationsRecursively(Set<Peptide> peptides, Iterator<Modification> ptms) {

        if (ptms.hasNext()) {

            Modification ptm = ptms.next();
            Set<Peptide> modifiedPeptides = new HashSet<Peptide>();
            for (Peptide peptide : peptides) {
                Set<Peptide> mp = this.applyModification(peptide, ptm);
                if (mp.size() == 0) {
                    modifiedPeptides.add(peptide);
                }
                modifiedPeptides.addAll(mp);
            }

            return applyModififcationsRecursively(modifiedPeptides, ptms);

        } else {
            return peptides;
        }
    }

    /**
     * Returns whether the peptide is modified.
     *
     * @return true if the peptide is modified
     */
    public boolean isModified() {
        return this instanceof ModifiedPeptide;
    }

    /**
     * Returns where in the protein sequence (N-terminus, C-terminus, internal) the peptide originates from.
     *
     * @return a PeptideOrigin enum
     */
    public Set<PeptideOrigin> getOrigins() {
        return origins;
    }

    /**
     * Returns where in the protein sequence (N-terminus, C-terminus, internal) the peptide originates from.
     *
     * @param origins a PeptideOrigin
     */
    public void setOrigins(Set<PeptideOrigin> origins) {
        this.origins = origins;
    }
}
