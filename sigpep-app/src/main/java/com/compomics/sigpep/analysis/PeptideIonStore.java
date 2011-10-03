package com.compomics.sigpep.analysis;

import com.compomics.sigpep.model.PeptideIon;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @TODO: JavaDoc missing.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 10-Jul-2008<br/>
 * Time: 18:20:34<br/>
 */
public interface PeptideIonStore<P extends PeptideIon> {

    void populate(Collection<P> peptides);

    Set<P> getPeptideIonsInMassRange(double lowerMassLimit, double upperMassLimit);

    Set<P> getPeptideIonsWithMass(double peptideMass);

    Map<Integer, Set<P>> getPeptideIonsWithOverlappingMassOverCharge(P peptide);

    Map<Integer, Set<P>> getPeptideIonsWithOverlappingMassOverCharge(double neutralMassPeptide);

    Set<Double> getUniqueNeutralPeptideIonMasses();
}
