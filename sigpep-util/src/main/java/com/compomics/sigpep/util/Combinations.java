package com.compomics.sigpep.util;

import java.util.*;

/**
 * The Combinations class provides an enumeration of all subsets of a group of
 * n elements taken k at a time. The constructor for Combinations accepts the group as
 * a Set of Objects, along with the number k to select.
 */
public class Combinations<T> implements Enumeration<Set<T>>, Iterator<Set<T>> {

    private ArrayList<T> inArray;
    private int n;
    private int k;
    private int[] index;
    private boolean hasMore = true;

    /**
     * Create a Combination to enumerate through all subsets of the
     * supplied Object array, selecting k at a time. If k is large
     * than the size of the set it will be set to the set size.
     *
     * @param elements the elements to chose from
     * @param k the number to select in each choice
     */
    public Combinations(int k, T... elements) {
        this.inArray = new ArrayList<T>();
        Collections.addAll(inArray, elements);
        this.n = inArray.size();
        this.k = k;

        if (k > n) {
            this.k = n;
        }

        /**
         * index is an array of ints that keep track of the next combination to return.
         * For example, an index on 5 things taken 3 at a time might contain {0 3 4}.
         * This index will be followed by {1 2 3}. Initially, the index is {0 ... m - 1}.
         */
        index = new int[k];
        for (int i = 0; i < k; i++) {
            index[i] = i;
        }
    }

    /**
     * Create a Combination to enumerate through all subsets of the
     * supplied Object array, selecting k at a time. If k is large
     * than the size of the set it will be set to the set size.
     *
     * @param elements the elements to chose from
     * @param k the number to select in each choice
     */
    public Combinations(int k, Set<T> elements) {
        this.inArray = new ArrayList<T>();
        this.inArray.addAll(elements);
        this.n = inArray.size();
        this.k = k;

        if (k > n) {
            this.k = n;
        }

        /**
         * index is an array of ints that keep track of the next combination to return.
         * For example, an index on 5 things taken 3 at a time might contain {0 3 4}.
         * This index will be followed by {1 2 3}. Initially, the index is {0 ... m - 1}.
         */
        index = new int[k];
        for (int i = 0; i < k; i++) {
            index[i] = i;
        }
    }

    /**
     * Returns true, unless we have already returned the last combination.
     *
     * @return true, unless we have already returned the last combination.
     */
    public boolean hasMoreElements() {
        return hasMore;
    }

    /**
     * Returns true if has next.
     * @return true if has next
     */
    public boolean hasNext() {
        return hasMore;
    }

    /**
     * Move the index forward a notch. The algorithm finds the rightmost
     * index element that can be incremented, increments it, and then
     * changes the elements to the right to each be 1 plus the element on their left.
     * <p>
     * For example, if an index of 5 things taken 3 at a time is at {0 3 4}, only the 0 can
     * be incremented without running out of room. The next index is {1, 1+1, 1+2) or
     * {1, 2, 3}. This will be followed by {1, 2, 4}, {1, 3, 4}, and {2, 3, 4}.
     * <p>
     * The algorithm is from Applied Combinatorics, by Alan Tucker.
     *
     */
    private void moveIndex() {
        int i = rightmostIndexBelowMax();
        if (i >= 0) {
            index[i] = index[i] + 1;
            for (int j = i + 1; j < k; j++) {
                index[j] = index[j - 1] + 1;
            }
        } else {
            hasMore = false;
        }
    }

    /**
     * @TODO: JavaDoc missing
     * 
     * @return
     */
    public Set<T> next() {
        return nextElement();
    }

    /**
     * @return java.lang.Object, the next combination from the supplied Object array.
     * <p>
     * Actually, an array of Objects is returned. The declaration must say just Object,
     * because the Combinations class implements Enumeration, which declares that the
     * nextElement() returns a plain Object. Users must cast the returned object to (Object[]).
     */
    public Set<T> nextElement() {
        if (!hasMore) {
            return null;
        }

        Set<T> out = new HashSet<T>(k);
        for (int i = 0; i < k; i++) {
            out.add(inArray.get(index[i]));
        }

        moveIndex();
        return out;
    }

    /**
     * @TODO: JavaDoc missing
     * 
     * @return the index which can be bumped up.
     */
    private int rightmostIndexBelowMax() {
        for (int i = k - 1; i >= 0; i--) {
            if (index[i] < n - k + i) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes from the underlying collection the last element returned by the
     * iterator (optional operation).  This method can be called only once per
     * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
     * the underlying collection is modified while the iteration is in
     * progress in any way other than by calling this method.
     *
     * @throws UnsupportedOperationException if the <tt>remove</tt>
     *                                       operation is not supported by this Iterator.
     * @throws IllegalStateException         if the <tt>next</tt> method has not
     *                                       yet been called, or the <tt>remove</tt> method has already
     *                                       been called after the last call to the <tt>next</tt>
     *                                       method.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
