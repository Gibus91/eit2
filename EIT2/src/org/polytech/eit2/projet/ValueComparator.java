package org.polytech.eit2.projet;

import java.util.Comparator;
import java.util.Map;

/**
 * Cette classe permet de créer une treeMap triée par ordre décroissant des valeurs
 * @author Mathieu Jouve et Jean-Baptiste Borel
 *
 */
class ValueComparator implements Comparator<String> {

    Map<String, Integer> base;
    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}