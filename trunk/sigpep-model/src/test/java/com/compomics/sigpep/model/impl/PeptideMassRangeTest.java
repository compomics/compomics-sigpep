package com.compomics.sigpep.model.impl;

import junit.framework.JUnit4TestAdapter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @TODO: JavaDoc missing
 * <p/>
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 22-Oct-2007<br>
 * Time: 14:57:56<br>
 */
public class PeptideMassRangeTest {

    double peptideMass = 100;
    Set<Integer> chargeStates = new TreeSet<Integer>();
    double accuracy = 0.3;

    @Before
    public void setUp() {
        chargeStates.add(2);
        chargeStates.add(3);
    }

    @Test
    public void testGetAccuracy() {
        MassOverChargeRangeImpl pmr = new MassOverChargeRangeImpl(peptideMass, chargeStates, accuracy);
        double expected = accuracy;
        assertTrue(pmr.getMassAccuracy() == expected);
    } // testGetAccuracy()

    @Test
    public void testSetAccuracy() {
        MassOverChargeRangeImpl pmr = new MassOverChargeRangeImpl(peptideMass, chargeStates, accuracy);
        double expected = 0.5;
        pmr.setMassAccuracy(expected);
        assertTrue(pmr.getMassAccuracy() == expected);
    } // testSetAccuracy()

    @Test
    public void testGetMaximumCharge() {
        MassOverChargeRangeImpl pmr = new MassOverChargeRangeImpl(peptideMass, chargeStates, accuracy);
        Set<Integer> expected = chargeStates;
        assertTrue(pmr.getChargeStates() == expected);
    } // testGetZRange()

    @Test
    public void testGetChargeStates() {
        MassOverChargeRangeImpl pmr = new MassOverChargeRangeImpl(peptideMass, chargeStates, accuracy);
        Set<Integer> expected = new HashSet<Integer>();
        pmr.setChargeStates(expected);
        assertTrue(pmr.getChargeStates() == expected);
    } // testSetZRange()

    @Test
    public void testGetPrecursorIonMassOverCharge() {

        MassOverChargeRangeImpl pmr = new MassOverChargeRangeImpl(peptideMass, chargeStates, accuracy);

        for (int z : pmr.getChargeStates()) {
            double expected = PrecursorIonImpl.calculateMassOverCharge(peptideMass, z);
            assertTrue(pmr.getPeptideIonMassOverCharge(z) == expected);
        }

    } // testGetPrecursorIonMassOverCharge()

    @Test
    public void testGetPeptideMass() {
        MassOverChargeRangeImpl pmr = new MassOverChargeRangeImpl(peptideMass, chargeStates, accuracy);
        double expected = peptideMass;
        assertTrue(pmr.getNeutralPeptideMass() == expected);
    } // testGetPeptideMass()

    @Test
    public void testSetPeptideMass() {
        MassOverChargeRangeImpl pmr = new MassOverChargeRangeImpl(peptideMass, chargeStates, accuracy);
        double expected = 200;
        pmr.setNeutralPeptideMass(expected);
        assertTrue(pmr.getNeutralPeptideMass() == expected);
    } // testSetPeptideMass()

    @Test
    public void testGetLowerBound() {
        MassOverChargeRangeImpl pmr = new MassOverChargeRangeImpl(peptideMass, chargeStates, accuracy);

        for (int z : pmr.getChargeStates()) {
            double expected = PrecursorIonImpl.calculateMassOverCharge(peptideMass, z) - accuracy;
            assertTrue(pmr.getLowerBound(z) == expected);
        }
    } // testGetLowerBound()

    @Test
    public void testGetUpperBound() {
        MassOverChargeRangeImpl pmr = new MassOverChargeRangeImpl(peptideMass, chargeStates, accuracy);

        for (int z : pmr.getChargeStates()) {
            double expected = PrecursorIonImpl.calculateMassOverCharge(peptideMass, z) + accuracy;
            assertTrue(pmr.getUpperBound(z) == expected);
        }
    } // testGetUpperBound()

    @Test
    public void testEquals() {

        Set<Integer> differentChargeState = new HashSet<Integer>();
        differentChargeState.add(4);

        MassOverChargeRangeImpl pmr1 = new MassOverChargeRangeImpl(peptideMass, chargeStates, accuracy);
        MassOverChargeRangeImpl pmr2 = new MassOverChargeRangeImpl(peptideMass, chargeStates, accuracy);
        MassOverChargeRangeImpl pmr3 = new MassOverChargeRangeImpl(peptideMass, differentChargeState, 1);
        MassOverChargeRangeImpl pmr4 = new MassOverChargeRangeImpl(900.234, chargeStates, accuracy);

        assertTrue(pmr1.equals(pmr2));
        assertFalse(pmr1.equals(pmr3));
        assertFalse(pmr1.equals(pmr4));

    } // testEquals()

    @Test
    public void testCompareTo() {
        MassOverChargeRangeImpl pmr1 = new MassOverChargeRangeImpl(peptideMass, chargeStates, accuracy);
        MassOverChargeRangeImpl pmr2 = new MassOverChargeRangeImpl(peptideMass + 1, chargeStates, accuracy);

        assertTrue(pmr1.compareTo(pmr1) == 0);
        assertTrue(pmr1.compareTo(pmr2) == -1);
        assertTrue(pmr2.compareTo(pmr1) == 1);


    } // testCompareTo()

    @Test
    public void testOverlappsWith() {


        double minOverlap = 0;

        MassOverChargeRangeImpl pmr1 = new MassOverChargeRangeImpl(peptideMass, chargeStates, accuracy);
        MassOverChargeRangeImpl pmr2 = new MassOverChargeRangeImpl(peptideMass, chargeStates, accuracy);
        MassOverChargeRangeImpl pmr3 = new MassOverChargeRangeImpl(peptideMass + (accuracy / 2), chargeStates, accuracy);
        HashSet<Integer> chargeStates = new HashSet<Integer>();
        chargeStates.add(1);
        MassOverChargeRangeImpl pmr4 = new MassOverChargeRangeImpl(3999.9607, chargeStates, 1);
        MassOverChargeRangeImpl pmr5 = new MassOverChargeRangeImpl(3999.9676, chargeStates, 1);

        assertTrue(pmr1.overlappsWith(pmr2, minOverlap));
        assertTrue(pmr1.overlappsWith(pmr3, minOverlap));
        assertTrue(pmr3.overlappsWith(pmr1, minOverlap));
        assertTrue(pmr3.overlappsWith(pmr1, minOverlap));
        assertTrue(pmr4.overlappsWith(pmr5, minOverlap));
        assertTrue(pmr5.overlappsWith(pmr4, minOverlap));
        assertFalse(pmr1.overlappsWith(pmr4, minOverlap));

    } // testOverlappsWith()

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PeptideMassRangeTest.class);
    }
}
