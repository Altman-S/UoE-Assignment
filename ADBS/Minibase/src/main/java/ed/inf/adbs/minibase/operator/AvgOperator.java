package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.IntegerConstant;
import ed.inf.adbs.minibase.base.Term;
import ed.inf.adbs.minibase.base.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Class AvgOperator: Process the query which contains AVG
 *
 * @author Pai
 * @version 2022.03.19
 */
public class AvgOperator {
    private Variable avgVariable;
    private QueryPlan queryPlan;
    private List<Term> headTerms;
    private List<Map<Term,Term>> tupleMapList;
    private List<String> outputStringList = new ArrayList<>();

    /**
     * Initialise AvgOperator with query and dbFile
     *
     * @param query query to be processed
     * @param dbFile path to database files
     */
    public AvgOperator(String query, String dbFile) {
        this.avgVariable = new Variable(query.substring(query.indexOf("AVG(") + 4, query.indexOf(")")));
        this.queryPlan = new QueryPlan(query, dbFile);
        this.headTerms = this.queryPlan.getHeadTerms();

//        System.out.println("targetVariable: " + this.avgVariable);
//        System.out.println("headTerms in AvgOperator: " + this.headTerms);
//        System.out.println("initialise AvgOperator");
    }


    public List<Map<Term,Term>> getTupleMapList() { return this.queryPlan.getTupleMapList(); }


    public List<String> getOutputStringList() { return this.outputStringList; }


    /**
     * Get outputStringList, which is used for generate .csv file
     */
    public void dump() {
        this.tupleMapList = getTupleMapList();

        if (this.tupleMapList.size() <= 0) return;

        // initialisation
        int sum = 0;
        int count = 0;
        Map<Term,Term> lastTupleMap = this.tupleMapList.get(0);

//        System.out.println(this.queryPlan.getHeadAtom());

        for (Map.Entry<Term,Term> entry : lastTupleMap.entrySet()) {
            if (entry.getKey().equals(this.avgVariable)) {
                IntegerConstant integerConstant = (IntegerConstant) entry.getValue();
                sum += integerConstant.getValue();
                count += 1;
            }
        }

        for (int i = 1; i < this.tupleMapList.size(); i++) {
            Map<Term,Term> thisTupleMap = this.tupleMapList.get(i);

            // Sign to output sum
            boolean sumSign = true;
            for (int j = 0; j < this.headTerms.size() - 1; j++) {
                if (this.headTerms.get(j).equals(avgVariable)) continue;
                // may have some problems
//                if (!lastTupleMap.get(compTerm).equals(thisTupleMap.get(compTerm))) sumSign = false;
                for (Map.Entry<Term,Term> lastEntry : lastTupleMap.entrySet()) {
                    for (Map.Entry<Term,Term> thisEntry : thisTupleMap.entrySet()) {
                        if (thisEntry.getKey().equals(this.avgVariable) && thisEntry.getKey().equals(lastEntry.getKey())) {
                            if (!thisEntry.getValue().equals(lastEntry.getValue())) sumSign = false;
                        }
                    }
                }
            }

            if (sumSign) {
                for (Map.Entry<Term,Term> entry : thisTupleMap.entrySet()) {
                    if (entry.getKey().equals(this.avgVariable)) {
                        IntegerConstant integerConstant = (IntegerConstant) entry.getValue();
                        sum += integerConstant.getValue();
                        count += 1;
                    }
                }
            }
            else {
                StringBuilder line = new StringBuilder();

                for (int n = 0; n < this.headTerms.size() - 1; n++) {
                    for (Map.Entry<Term,Term> entry : lastTupleMap.entrySet()) {
                        if (entry.getKey().equals(this.headTerms.get(n))) {
                            if (entry.getKey().equals(this.avgVariable)) {
//                                System.out.print(Math.round((float) sum / count) + ", ");
                                line.append(Math.round((float) sum / count));
                                line.append(", ");
                            }
                            else {
//                                System.out.print(entry.getValue() + ", ");
                                line.append(entry.getValue());
                                line.append(", ");
                            }
                        }
                    }
                }
                for (Map.Entry<Term,Term> entry : lastTupleMap.entrySet()) {
                    if (entry.getKey().equals(this.headTerms.get(this.headTerms.size() - 1))) {
                        if (entry.getKey().equals(this.avgVariable)) {
//                            System.out.println(Math.round((float) sum / count));
                            line.append(Math.round((float) sum / count));
                        }
                        else {
//                            System.out.println(entry.getValue());
                            line.append(entry.getValue());
                        }
                    }
                }

                this.outputStringList.add(line.toString());

                // Reset lastTupleMap and sum
                lastTupleMap = thisTupleMap;
                sum = 0; count = 0;
                for (Map.Entry<Term,Term> entry : lastTupleMap.entrySet()) {
                    if (entry.getKey().equals(this.avgVariable)) {
                        IntegerConstant integerConstant = (IntegerConstant) entry.getValue();
                        sum += integerConstant.getValue();
                        count += 1;
                    }
                }
            }
        }

        // Output the last sum
        StringBuilder lastLine = new StringBuilder();
        for (int m = 0; m < this.headTerms.size() - 1; m++) {
            for (Map.Entry<Term,Term> entry : lastTupleMap.entrySet()) {
                if (entry.getKey().equals(this.headTerms.get(m))) {
                    if (entry.getKey().equals(this.avgVariable)) {
//                        System.out.print(Math.round((float) sum / count) + ", ");
                        lastLine.append(Math.round((float) sum / count));
                        lastLine.append(", ");
                    }
                    else {
//                        System.out.print(entry.getValue() + ", ");
                        lastLine.append(entry.getValue());
                        lastLine.append(", ");
                    }
                }
            }
        }
        for (Map.Entry<Term,Term> entry : lastTupleMap.entrySet()) {
            if (entry.getKey().equals(this.headTerms.get(this.headTerms.size() - 1))) {
                if (entry.getKey().equals(this.avgVariable)) {
//                    System.out.println(Math.round((float) sum / count));
                    lastLine.append(Math.round((float) sum / count));
                }
                else {
//                    System.out.println(entry.getValue());
                    lastLine.append(entry.getValue());
                }
            }
        }

        this.outputStringList.add(lastLine.toString());

    }
}
