package edu.hm.ccwi.semantic.commons.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Create Combination of Word Lists.
 *
 * @author https://abhirama.wordpress.com/2009/06/09/java-code-to-generate-word-combintions/
 */
public class Combinations<T> {


    private List<T> originalList;

    public Combinations(List<T> originalList) {
        this.originalList = originalList;
    }

    public List<List<T>> getCombinations() {
        List<List<T>> combinations = new LinkedList<List<T>>();

        int size = this.originalList.size();

        int threshold = Double.valueOf(Math.pow(2, size)).intValue() - 1;

        for (int i = 1; i <= threshold; ++i) {
            LinkedList<T> individualCombinationList = new LinkedList<T>();
            int count = size - 1;

            int clonedI = i;
            while (count >= 0) {
                if ((clonedI & 1) != 0) {
                    individualCombinationList.addFirst(this.originalList.get(count));
                }

                clonedI = clonedI >>> 1;
                --count;
            }

            combinations.add(individualCombinationList);

        }

        return combinations;
    }
}

