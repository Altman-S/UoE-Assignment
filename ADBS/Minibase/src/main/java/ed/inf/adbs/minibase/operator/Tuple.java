package ed.inf.adbs.minibase.operator;


import ed.inf.adbs.minibase.base.IntegerConstant;
import ed.inf.adbs.minibase.base.RelationalAtom;
import ed.inf.adbs.minibase.base.StringConstant;
import ed.inf.adbs.minibase.base.Term;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Class Tuple: A data structure to store each line in the .csv file.
 *
 * @author Pai
 * @version 2022/03/11
 */
public class Tuple {

    private String value;
    private Map<Term,Term> map = new HashMap<>();
    private RelationalAtom relationalAtom;

    /**
     * Create a Tuple with the value of String
     *
     * @param value each line in the .csv file
     */
    public Tuple(String value) {
        this.value = value;
    }

    public String getValue() { return this.value; }

    public Map<Term,Term> getMap() { return this.map; }

    /**
     * Set value and map of Tuple.
     * value is a string of each line in the .csv file
     * map is able to map from Terms in relationAtom to real Terms in .csv file according to schema.txt
     *
     * @param newValue each line in the .csv file
     * @param relationalAtom the input RelationalAtom
     */
    public void setValue(String newValue, RelationalAtom relationalAtom) {
        // if newValue != null, set value and map of the Tuple, else set value = null
        if (newValue != null) {
            this.value = newValue;
            String[] relationalAtomStr = newValue.split(", ");
            List<Term> terms = relationalAtom.getTerms();
            // this.map: {Variable -> IntegerConstant/StringConstant}
            for (int i = 0; i < relationalAtomStr.length; i++) {
                if (this.map.get(terms.get(i)) instanceof IntegerConstant) {
                    this.map.put(terms.get(i), new IntegerConstant(Integer.valueOf(relationalAtomStr[i])));
                }
                else if (this.map.get(terms.get(i)) instanceof StringConstant) {
                    this.map.put(terms.get(i), new StringConstant(relationalAtomStr[i].replace("'", "")));
                }
            }
        } else {
            this.value = null;
        }

    }

    /**
     * Modify map according to new line in the .csv file
     *
     * @param key Term in the relationalAtom
     * @param value Term in the .csv file
     */
    public void put(Term key, Term value) {
        this.map.put(key, value);
    }
}
