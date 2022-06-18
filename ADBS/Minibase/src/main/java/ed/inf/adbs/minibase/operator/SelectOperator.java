package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.*;

import java.io.BufferedReader;
import java.util.*;


/**
 * Class SelectOperator: Scan each line in the .csv file, if meet the selection condition then output it.
 *
 * @author Pai
 * @version 2022.03.12
 */
public class SelectOperator extends Operator {

    private ScanOperator scanOperator;
    private RelationalAtom relationalAtom;
    private List<ComparisonAtom> comparisonAtomList;

    private Tuple tuple = new Tuple(null);

    private List<Map<Term,Term>> tupleMapList = new ArrayList<>();


    /**
     * Initialize ScanOperator with relationalAtom, comparisonAtomList and dbFile
     *
     * @param relationalAtom relationalAtom which is needed to be scanned
     * @param comparisonAtomList selection condition
     * @param dbFile path to database files
     */
    // need to be improved, List<ComparisonAtom>
    public SelectOperator(RelationalAtom relationalAtom, List<ComparisonAtom> comparisonAtomList, String dbFile) {
        this.relationalAtom = relationalAtom;
        this.comparisonAtomList = comparisonAtomList;
        this.scanOperator = new ScanOperator(relationalAtom, dbFile);

//        System.out.println("initialise SelectOperator");
    }


    /**
     * Read next line in the .csv file and set current Tuple.
     * If the current Tuple meets selection condition, add it to tupleMapList which is useful for JoinOperator
     *
     * @return next Tuple
     */
    @Override
    public Tuple getNextTuple() {
        this.tuple = this.scanOperator.getNextTuple();

        if (this.tuple != null) {
            // Sign for output. If outputSign == true then output, else not output
            boolean outputSign = true;

            // Substitute termLeft and termRight with the true value
            Map<Term,Term> map = this.tuple.getMap();
            List<Term> mapKeyList = new ArrayList<>(map.keySet());

            // When terms in relationalAtom are Constant, check whether its key and value in the map are equal
            // To deal with query like Q(x, y, z) :- R(x, y, 4)
            for (Term termKey : mapKeyList) {
                if (termKey instanceof IntegerConstant || termKey instanceof StringConstant) {
//                    System.out.println("Constant");
//                    System.out.println(termKey);
                    Term termValue = map.get(termKey);
//                    System.out.println(termValue);
                    if (!termKey.equals(termValue)) outputSign = false;
                }
            }

//            System.out.println(mapKeyList);

            // To deal with query like Q(x, y, z) :- R(x, y, z), z = 4
            for (ComparisonAtom comparisonAtom : this.comparisonAtomList) {

                Term term1 = comparisonAtom.getTerm1();
                Term term2 = comparisonAtom.getTerm2();

                // All terms in comparisonAtom must be in mapKeyList
                if (term1 instanceof Variable && term2 instanceof Variable) {
                    if (!mapKeyList.contains(term1) || !mapKeyList.contains(term2)) continue;
                }
                else if (term1 instanceof Variable && term2 instanceof Constant) {
                    if (!mapKeyList.contains(term1)) continue;
                }
                else if (term1 instanceof Constant && term2 instanceof Variable) {
                    if (!mapKeyList.contains(term2)) continue;
                }

//            if (mapKeyList.contains(term1)) System.out.println("mapKey contains: " + term1);

                for (Map.Entry<Term,Term> entry : map.entrySet()) {
//                System.out.println(entry.getKey().getClass());
//                System.out.println(entry.getValue().getClass());

                    // the key must be Variable, so it can be converted
                    if (entry.getKey() instanceof Variable && entry.getKey().equals(term1)) {
                        term1 = entry.getValue();
                    }
                    if (entry.getKey() instanceof Variable && entry.getKey().equals(term2)) {
                        term2 = entry.getValue();
                    }
                }

                // Compare termLeft with termRight
                ComparisonOperator comparisonOperator = comparisonAtom.getOp();

                // If term1 and term2 can not meet the selection condition, set outputSign with false
                if (term1 instanceof IntegerConstant && term2 instanceof IntegerConstant) {
                    IntegerConstant termLeft = (IntegerConstant) term1;
                    IntegerConstant termRight = (IntegerConstant) term2;
                    switch (comparisonOperator) {
                        case EQ:
                            if (termLeft.getValue().compareTo(termRight.getValue()) != 0) outputSign = false;
                            break;
                        case NEQ:
                            if (termLeft.getValue().compareTo(termRight.getValue()) == 0) outputSign = false;
                            break;
                        case GT:
                            if (termLeft.getValue().compareTo(termRight.getValue()) <= 0) outputSign = false;
                            break;
                        case GEQ:
                            if (termLeft.getValue().compareTo(termRight.getValue()) < 0) outputSign = false;
                            break;
                        case LT:
                            if (termLeft.getValue().compareTo(termRight.getValue()) >= 0) outputSign = false;
                            break;
                        case LEQ:
                            if (termLeft.getValue().compareTo(termRight.getValue()) > 0) outputSign = false;
                            break;
                    }
                }
                else if (term1 instanceof StringConstant && term2 instanceof StringConstant) {
                    StringConstant termLeft = (StringConstant) term1;
                    StringConstant termRight = (StringConstant) term2;
                    switch (comparisonOperator) {
                        case EQ:
                            if (termLeft.getValue().compareTo(termRight.getValue()) != 0) outputSign = false;
                            break;
                        case NEQ:
                            if (termLeft.getValue().compareTo(termRight.getValue()) == 0) outputSign = false;
                            break;
                        case GT:
                            if (termLeft.getValue().compareTo(termRight.getValue()) <= 0) outputSign = false;
                            break;
                        case GEQ:
                            if (termLeft.getValue().compareTo(termRight.getValue()) < 0) outputSign = false;
                            break;
                        case LT:
                            if (termLeft.getValue().compareTo(termRight.getValue()) >= 0) outputSign = false;
                            break;
                        case LEQ:
                            if (termLeft.getValue().compareTo(termRight.getValue()) > 0) outputSign = false;
                            break;
                    }
                }
                else {  // If the types of term1 and term2 are different
                    if (comparisonOperator != ComparisonOperator.NEQ) outputSign = false;
                }

            }

            // If meet the selection condition, output the value
            if (outputSign) {
//                System.out.println("outputSign: " + this.tuple.getValue());

                // add Tuple which meets selection condition to tupleList
                Map<Term,Term> newMap = new HashMap<>(this.tuple.getMap());
                this.tupleMapList.add(newMap);
            }

            return this.tuple;
        }
        else {
            return null;
        }

    }


    public List<Map<Term,Term>> getTupleMapList() { return this.tupleMapList; }


    @Override
    public void reset() {
        this.tupleMapList = new ArrayList<>();
        this.scanOperator.reset();
    }


    /**
     * Test function
     */
    @Override
    public void dump() {
        this.tuple = getNextTuple();

        while (this.tuple != null) {
            Map<Term,Term> map = this.tuple.getMap();
//            System.out.println("select: " + map);

            this.tuple = getNextTuple();
        }

    }
}
