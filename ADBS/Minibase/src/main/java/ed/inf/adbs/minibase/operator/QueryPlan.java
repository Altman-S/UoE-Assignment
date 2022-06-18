package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.parser.QueryParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Class QueryPlan: A class to process query when there is no SUM or AVG
 *
 * @author Pai
 * @version 2022.03.18
 */
public class QueryPlan {

    private Query query;
    private String dbFile;
    private RelationalAtom headAtom;
    private List<Term> headTerms;
    private List<RelationalAtom> relationalAtomList = new ArrayList<>();
    private List<ComparisonAtom> comparisonAtomList = new ArrayList<>();

    private List<Map<Term,Term>> leftTupleMapList = new ArrayList<>();
    private List<String> outputStringList = new ArrayList<>();


    /**
     * Initialise QueryPlan with query and dbFile
     *
     * @param query query to be processed
     * @param dbFile path to database files
     */
    public QueryPlan(String query, String dbFile) {
        this.query = QueryParser.parse(query);
        this.headAtom = this.query.getHead();
        this.headTerms = this.headAtom.getTerms();
        this.dbFile = dbFile;

//        System.out.println("query headAtom: " + this.headAtom.getTerms());

        List<Atom> body = this.query.getBody();
        // The Atom in the query body may be relationalAtom or comparisonAtom, so we need to separate them to different lists
        for (Atom atom : body) {
            if (atom instanceof RelationalAtom) this.relationalAtomList.add((RelationalAtom) atom);
            if (atom instanceof ComparisonAtom) this.comparisonAtomList.add((ComparisonAtom) atom);
        }

//        System.out.println("initialise QueryPlan");
    }


    public RelationalAtom getHeadAtom() { return this.headAtom; }


    public List<Term> getHeadTerms() { return this.headTerms; }


    public List<String> getOutputStringList() { return this.outputStringList; }


    /**
     * Get the final result of query.
     * Result is a list of Tuple
     *
     * @return the result Tuple List of query
     */
    public List<Map<Term,Term>> getTupleMapList() {

        if (this.relationalAtomList.size() >= 2) {

            // first leftChild
            Operator leftOperator = new SelectOperator(this.relationalAtomList.get(0), this.comparisonAtomList, this.dbFile);
            this.relationalAtomList.remove(this.relationalAtomList.get(0));
            leftOperator.dump();
            this.leftTupleMapList = leftOperator.getTupleMapList();

//            System.out.println("leftTupleMapList: " + this.leftTupleMapList.size());

            while (this.relationalAtomList.size() > 0) {
                List<Term> leftKeyList = new ArrayList<>(this.leftTupleMapList.get(0).keySet());
                List<Term> rightKeyList = new ArrayList<>();
                JoinOperator joinOperator = null;

//                System.out.println("relationalAtomList size: " + this.relationalAtomList.size());

                // Select rightChild
                for (ComparisonAtom comparisonAtom : this.comparisonAtomList) {

                    boolean breakSign = false;
                    Term term1 = comparisonAtom.getTerm1();
                    Term term2 = comparisonAtom.getTerm2();

                    for (int i = 0; i < this.relationalAtomList.size(); i++) {
                        rightKeyList = this.relationalAtomList.get(i).getTerms();

                        // for R(x, y, z), S(k, w, t), x = k
                        if (term1 instanceof Variable && term2 instanceof Variable && ((leftKeyList.contains(term1) && rightKeyList.contains(term2)) || (leftKeyList.contains(term2) && rightKeyList.contains(term1)))) {
                            joinOperator = new JoinOperator(this.leftTupleMapList, this.relationalAtomList.get(i), this.comparisonAtomList, this.dbFile);

//                            System.out.println("select rightChild 1: " + this.relationalAtomList.get(i));

                            this.relationalAtomList.remove(relationalAtomList.get(i));
                            breakSign = true;
                            break;
                        }
                    }
                    if (breakSign) break;
                }
                // for R(x, y, z), S(x, w, t)
                for (int i = 0; i < this.relationalAtomList.size(); i++) {
                    boolean breakSign = false;
                    rightKeyList = this.relationalAtomList.get(i).getTerms();

                    for (Term term : rightKeyList) {
                        if (leftKeyList.contains(term)) {
                            joinOperator = new JoinOperator(this.leftTupleMapList, this.relationalAtomList.get(i), this.comparisonAtomList, dbFile);

//                            System.out.println("select rightChild 2: " + this.relationalAtomList.get(i));

                            this.relationalAtomList.remove(relationalAtomList.get(i));
                            breakSign = true;
                            break;
                        }
                    }
                    if (breakSign) break;
                }
                if (joinOperator == null) {
                    joinOperator = new JoinOperator(this.leftTupleMapList, this.relationalAtomList.get(0), this.comparisonAtomList, this.dbFile);
                    this.relationalAtomList.remove(relationalAtomList.get(0));
                }

                // Update leftTupleMapList
                joinOperator.dump();
                this.leftTupleMapList = joinOperator.getTupleMapList();

//                System.out.println();
//                System.out.println("leftTupleMapList: " + this.leftTupleMapList);
//                System.out.println("length of leftTupleMapList: " + this.leftTupleMapList.size());
//                System.out.println();
            }
        }
        else if (this.relationalAtomList.size() == 1) {
            SelectOperator selectOperator = new SelectOperator(this.relationalAtomList.get(0), this.comparisonAtomList, this.dbFile);
            selectOperator.dump();
            this.leftTupleMapList = selectOperator.getTupleMapList();
        }
        else {
            this.leftTupleMapList = null;
        }


        return this.leftTupleMapList;
    }


    /**
     * Test function
     */
    public void dump() {
        List<Map<Term,Term>> tupleMapList = getTupleMapList();
//        System.out.println(tupleMapList);

        System.out.println(this.headAtom);

        for (Map<Term,Term> oneTupleMap : tupleMapList) {
//            System.out.println(oneTupleMap);
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < this.headTerms.size() - 1; i++) {
                for (Map.Entry<Term,Term> entry : oneTupleMap.entrySet()) {
                    if (entry.getKey().equals(this.headTerms.get(i))) {
//                        System.out.print(entry.getValue() + ", ");
                        line.append(entry.getValue());
                        line.append(", ");
                    }
                }
            }
            for (Map.Entry<Term,Term> entry : oneTupleMap.entrySet()) {
                if (entry.getKey().equals(this.headTerms.get(this.headTerms.size() - 1))) {
//                    System.out.println(entry.getValue());
                    line.append(entry.getValue());
                }
            }

            this.outputStringList.add(line.toString());
        }
    }
}



