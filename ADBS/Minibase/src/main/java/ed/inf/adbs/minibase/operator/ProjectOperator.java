package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.parser.QueryParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Class ProjectOperator: use ScanOperator or SelectOperator to get tupleMapList, and output it.
 *
 * @author Pai
 * @version 2022.03.12
 */
public class ProjectOperator extends Operator {

    private Query query;
    private RelationalAtom headAtom;
    private List<Term> headTerms;
    private List<RelationalAtom> relationalAtomList = new ArrayList<>();
    private List<ComparisonAtom> comparisonAtomList = new ArrayList<>();

    private Operator operator;
    private Tuple tuple = new Tuple(null);

    private List<Map<Term,Term>> tupleMapList = new ArrayList<>();


    /**
     * Initialise ProjectOperator with query and dbFile.
     * A ProjectOperator may use ScanOperator or SelectOperator
     *
     * @param query the input query
     * @param dbFile path to database files
     */
    public ProjectOperator(String query, String dbFile) {
        this.query = QueryParser.parse(query);
        this.headAtom = this.query.getHead();
        this.headTerms = this.headAtom.getTerms();

        List<Atom> body = this.query.getBody();
        // The Atom in the query body may be relationalAtom or comparisonAtom, so we need to separate them to different lists
        for (Atom atom : body) {
            if (atom instanceof RelationalAtom) this.relationalAtomList.add((RelationalAtom) atom);
            if (atom instanceof ComparisonAtom) this.comparisonAtomList.add((ComparisonAtom) atom);
        }

        // To judge which operator we need to use (ScanOperator or SelectOperator)
        if (this.comparisonAtomList != null) {
//            System.out.println("SelectOperator");
            this.operator = new SelectOperator(this.relationalAtomList.get(0), this.comparisonAtomList, dbFile);
        }
        else {
//            System.out.println("ScanOperator");
            this.operator = new ScanOperator(this.relationalAtomList.get(0), dbFile);
            for (Term term : this.relationalAtomList.get(0).getTerms()) {
                if (term instanceof Constant) {

//                    System.out.println("SelectOperator");
                    this.operator = new SelectOperator(this.relationalAtomList.get(0), this.comparisonAtomList, dbFile);
                }
            }
        }

//        System.out.println("initialise ProjectOperator");
    }


    @Override
    public Tuple getNextTuple() {
        this.tuple = this.operator.getNextTuple();
        return this.tuple;
    }


    public List<Map<Term,Term>> getTupleMapList() {
        this.tupleMapList = this.operator.getTupleMapList();
        return this.tupleMapList;
    }


    @Override
    public void reset() {
        this.operator.reset();
    }


    /**
     * Test function
     */
    @Override
    public void dump() {
        this.tuple = getNextTuple();

        while (this.tuple != null) {
            Map<Term,Term> map = this.tuple.getMap();
//            System.out.println(map);

            this.tuple = getNextTuple();
        }

        this.tupleMapList = getTupleMapList();

        System.out.println();

        for (Map<Term,Term> oneTupleMap : this.tupleMapList) {
            for (int i = 0; i < this.headTerms.size() - 1; i++) {
                for (Map.Entry<Term,Term> entry : oneTupleMap.entrySet()) {
                    if (entry.getKey().equals(this.headTerms.get(i))) {
                        System.out.print(entry.getValue() + ", ");
                    }
                }
            }
            for (Map.Entry<Term,Term> entry : oneTupleMap.entrySet()) {
                if (entry.getKey().equals(this.headTerms.get(this.headTerms.size() - 1))) {
                    System.out.println(entry.getValue());
                }
            }
        }

    }
}
