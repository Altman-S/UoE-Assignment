package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.*;

import java.util.*;


/**
 * Class JoinOperator: join left RelationalAtom (leftTupleMapList) with right RelationalAtom.
 *
 * @author Pai
 * @version 2022.03.18
 */
public class JoinOperator extends Operator {

    private List<Map<Term,Term>> leftTupleMapList = new ArrayList<>();
    private int leftTupleListSize = 0;
    private int index = 0;
    private Operator rightOperator;
    private List<ComparisonAtom> comparisonAtomList;

    private Tuple rightTuple = new Tuple(null);
    private List<Map<Term,Term>> tupleMapList = new ArrayList<>();


    /**
     * Initialise JoinOperator with leftTupleMapList, rightChild, comparisonAtomList and dbFile
     *
     * @param leftTupleMapList the left RelationalAtom (leftTupleMapList or leftChild)
     * @param rightChild the right RelationalAtom (rightChild)
     * @param comparisonAtomList ComparisonAtom List
     * @param dbFile path to database files
     */
    public JoinOperator(List<Map<Term,Term>> leftTupleMapList, RelationalAtom rightChild, List<ComparisonAtom> comparisonAtomList, String dbFile) {
        this.leftTupleMapList = leftTupleMapList;
        this.leftTupleListSize = this.leftTupleMapList.size();
        this.rightOperator = new SelectOperator(rightChild, comparisonAtomList, dbFile);
        this.comparisonAtomList = comparisonAtomList;

//        System.out.println("initialise JoinOperator");
    }


    /**
     * Read left Tuple and right Tuple from leftChild and rightChild respectively.
     * If they meet join condition, merge leftTuple and rightTuple, then add it to tupleMapList
     *
     * @return next Tuple of rightChild
     */
    @Override
    public Tuple getNextTuple() {

        this.rightTuple = this.rightOperator.getNextTuple();

        if (this.rightTuple != null) {
            this.index = 0;

            // traverse rightOperator
            while (this.index < this.leftTupleListSize) {

                // Sign for merging leftMap and rightMap
                boolean mergeSign = true;
                boolean compareSign = true;

                Map<Term,Term> mergeMap = new HashMap<>();
                Map<Term,Term> leftMap = this.leftTupleMapList.get(index);
                List<Term> leftKeyList = new ArrayList<>(leftMap.keySet());
                Map<Term,Term> rightMap = this.rightTuple.getMap();
                List<Term> rightKeyList = new ArrayList<>(rightMap.keySet());

                // for R(x, y, z), S(x, w, t)
                for (Map.Entry<Term,Term> leftEntry : leftMap.entrySet()) {
                    for (Map.Entry<Term,Term> rightEntry : rightMap.entrySet()) {
                        if (leftEntry.getKey().equals(rightEntry.getKey())) {
                            if (!leftEntry.getValue().equals(rightEntry.getValue())) {
                                mergeSign = false;
                            }
                        }
                    }
                }

                // for R(x, y), S(z, u), y > u
                for (ComparisonAtom comparisonAtom : this.comparisonAtomList) {

                    Term term1 = comparisonAtom.getTerm1();
                    Term term2 = comparisonAtom.getTerm2();

                    if (leftKeyList.contains(term1) && rightKeyList.contains(term2)) {
//                        mergeSign = true;
                        for (Map.Entry<Term,Term> leftEntry : leftMap.entrySet()) {
                            if (leftEntry.getKey().equals(term1)) {
                                term1 = leftEntry.getValue();
                                break;
                            }
                        }
                        for (Map.Entry<Term,Term> rightEntry : rightMap.entrySet()) {
                            if (rightEntry.getKey().equals(term2)) {
                                term2 = rightEntry.getValue();
                                break;
                            }
                        }

                        // Compare termLeft with termRight
                        ComparisonOperator comparisonOperator = comparisonAtom.getOp();
                        // If term1 and term2 can not meet the selection condition, set compareSign with false
                        if (term1 instanceof IntegerConstant && term2 instanceof IntegerConstant) {
                            IntegerConstant termLeft = (IntegerConstant) term1;
                            IntegerConstant termRight = (IntegerConstant) term2;
                            switch (comparisonOperator) {
                                case EQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) != 0) compareSign = false;
                                    break;
                                case NEQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) == 0) compareSign = false;
                                    break;
                                case GT:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) <= 0) compareSign = false;
                                    break;
                                case GEQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) < 0) compareSign = false;
                                    break;
                                case LT:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) >= 0) compareSign = false;
                                    break;
                                case LEQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) > 0) compareSign = false;
                                    break;
                            }
                        }
                        else if (term1 instanceof StringConstant && term2 instanceof StringConstant) {
                            StringConstant termLeft = (StringConstant) term1;
                            StringConstant termRight = (StringConstant) term2;
                            switch (comparisonOperator) {
                                case EQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) != 0) compareSign = false;
                                    break;
                                case NEQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) == 0) compareSign = false;
                                    break;
                                case GT:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) <= 0) compareSign = false;
                                    break;
                                case GEQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) < 0) compareSign = false;
                                    break;
                                case LT:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) >= 0) compareSign = false;
                                    break;
                                case LEQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) > 0) compareSign = false;
                                    break;
                            }
                        }
                        else {  // If the types of term1 and term2 are different
                            if (comparisonOperator != ComparisonOperator.NEQ) compareSign = false;
                        }
                    }
                    else if (leftKeyList.contains(term2) && rightKeyList.contains(term1)) {
//                        mergeSign = true;
                        for (Map.Entry<Term,Term> leftEntry : leftMap.entrySet()) {
                            if (leftEntry.getKey().equals(term2)) {
                                term2 = leftEntry.getValue();
                                break;
                            }
                        }
                        for (Map.Entry<Term,Term> rightEntry : rightMap.entrySet()) {
                            if (rightEntry.getKey().equals(term1)) {
                                term1 = rightEntry.getValue();
                                break;
                            }
                        }

                        // Compare termLeft with termRight
                        ComparisonOperator comparisonOperator = comparisonAtom.getOp();
                        // If term1 and term2 can not meet the selection condition, set compareSign with false
                        if (term1 instanceof IntegerConstant && term2 instanceof IntegerConstant) {
                            IntegerConstant termLeft = (IntegerConstant) term2;
                            IntegerConstant termRight = (IntegerConstant) term1;
                            switch (comparisonOperator) {
                                case EQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) != 0) compareSign = false;
                                    break;
                                case NEQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) == 0) compareSign = false;
                                    break;
                                case GT:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) <= 0) compareSign = false;
                                    break;
                                case GEQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) < 0) compareSign = false;
                                    break;
                                case LT:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) >= 0) compareSign = false;
                                    break;
                                case LEQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) > 0) compareSign = false;
                                    break;
                            }
                        }
                        else if (term1 instanceof StringConstant && term2 instanceof StringConstant) {
                            StringConstant termLeft = (StringConstant) term2;
                            StringConstant termRight = (StringConstant) term1;
                            switch (comparisonOperator) {
                                case EQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) != 0) compareSign = false;
                                    break;
                                case NEQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) == 0) compareSign = false;
                                    break;
                                case GT:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) <= 0) compareSign = false;
                                    break;
                                case GEQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) < 0) compareSign = false;
                                    break;
                                case LT:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) >= 0) compareSign = false;
                                    break;
                                case LEQ:
                                    if (termLeft.getValue().compareTo(termRight.getValue()) > 0) compareSign = false;
                                    break;
                            }
                        }
                        else {  // If the types of term1 and term2 are different
                            if (comparisonOperator != ComparisonOperator.NEQ) compareSign = false;
                        }
                    }
                }

                // output Map
                if (mergeSign && compareSign) {
                    mergeMap.putAll(leftMap);
                    mergeMap.putAll(rightMap);

                    Map<Term,Term> newMergeMap = new HashMap<>();
                    for (Map.Entry<Term,Term> entry : mergeMap.entrySet()) {
                        boolean putSign = true;
                        for (Map.Entry<Term,Term> entryNew : newMergeMap.entrySet()) {
                            if (entry.getKey().equals(entryNew.getKey())) {
                                putSign = false;
                                break;
                            }
                        }
                        if (putSign) newMergeMap.put(entry.getKey(), entry.getValue());
                    }

//                    System.out.println("mergeMap");
                    this.tupleMapList.add(newMergeMap);
                }

                // add index
                this.index++;
            }

            return this.rightTuple;
        }
        else {
            return null;
        }
    }


    @Override
    public List<Map<Term,Term>> getTupleMapList() { return this.tupleMapList; }


    @Override
    public void reset() {
        this.tupleMapList = new ArrayList<>();
        this.rightOperator.reset();
    }


    /**
     * Test function
     */
    @Override
    public void dump() {
        this.rightTuple = getNextTuple();

        while (this.rightTuple != null) {
            Map<Term,Term> map = this.rightTuple.getMap();
//            System.out.println("join: " + map);
            this.rightTuple = getNextTuple();
        }

    }


}
