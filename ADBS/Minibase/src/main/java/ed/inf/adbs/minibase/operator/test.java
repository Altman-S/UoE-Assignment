package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.parser.QueryParser;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class test {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        String dbFile = "data/evaluation/db";
        String inputFile = "data/evaluation/input/query1.txt";
        String outputFile = "data/evaluation/output/query1.csv";


        // test ScanOperator
        Query query = QueryParser.parse("Q(x,y,z) :- R(x, y, z)");
        List<Atom> body = query.getBody();
        RelationalAtom body1 = (RelationalAtom) body.get(0);
        List<Term> terms = body1.getTerms();
        System.out.println(terms);

        for (Term term : terms) {
            System.out.print(term);
            System.out.print(": ");
            System.out.println(term.getClass());
        }

        System.out.println();

        ScanOperator so = new ScanOperator(body1, dbFile);
        so.dump();
        so.reset();
        System.out.println();
        System.out.println("The second time: ");
        so.dump();


        // test ComparisonAtom and ComparisonOperator
//        Query query = QueryParser.parse("Q(x,y,z) :- R(x, y, z), 4 > 3");
//        List<Atom> body = query.getBody();
//        ComparisonAtom cpAtom = (ComparisonAtom) body.get(1);
//
//
//        Term termLeft = new IntegerConstant(3);
//        Term termRight = new IntegerConstant(4);
//        ComparisonOperator op = ComparisonOperator.EQ;
//
//        System.out.println(cpAtom.getOp());
//        System.out.println(cpAtom.getTerm1());
//
//        if (cpAtom.getOp() == ComparisonOperator.GT) {
//            System.out.println("yes");
//        } else {
//            System.out.println("no");
//        }
//
//        String str = "abc";
//
//        String abc = "1, 9, 'adbs'";
//        String[] line = abc.split(", ");
//        for (int i = 0; i < line.length; i++) {
//            System.out.println(line[i]);
//        }


        // test SelectOperator
//        Query query = QueryParser.parse("Q(x,y,z) :- R(x, y, 'rl'), x > 1, k > n");
//        List<Atom> body = query.getBody();
//        RelationalAtom body1 = (RelationalAtom) body.get(0);
//        ComparisonAtom body2 = (ComparisonAtom) body.get(1);
//        ComparisonAtom body3 = (ComparisonAtom) body.get(2);
//        List<Term> terms = body1.getTerms();
//        System.out.println(body1);
//        System.out.println(body2);
//        System.out.println(body3);
//
//        List<RelationalAtom> relationalAtomList = new ArrayList<>();
//        List<ComparisonAtom> comparisonAtomList = new ArrayList<>();
//
//        for (Atom atom : body) {
//            if (atom instanceof ComparisonAtom) comparisonAtomList.add((ComparisonAtom) atom);
//        }
//
//        SelectOperator selectOperator = new SelectOperator(body1, comparisonAtomList, dbFile);
//        selectOperator.dump();
//
//        System.out.println();
//        selectOperator.reset();
//        System.out.println("reset()");
//        System.out.println();
//        selectOperator.dump();


        // test ProjectOperator
//        String query = "Q(x, z) :- R(x, 9, z), x > 7";
//        ProjectOperator projectOperator = new ProjectOperator(query, dbFile);
//
//        projectOperator.dump();
//        projectOperator.reset();
//        System.out.println();
//        System.out.println("reset()");
//        System.out.println();
//        projectOperator.dump();


        // test JoinOperator
//        Query query = QueryParser.parse("Q(x, y, z, u, w, t) :- R(x, y, z), S(u, w, t), x = u");
//        RelationalAtom headAtom = query.getHead();
//        List<Term> headTerms = headAtom.getTerms();
//        List<RelationalAtom> relationalAtomList = new ArrayList<>();
//        List<ComparisonAtom> comparisonAtomList = new ArrayList<>();
//
//        List<Atom> body = query.getBody();
//        // The Atom in the query body may be relationalAtom or comparisonAtom, so we need to separate them to different lists
//        for (Atom atom : body) {
//            if (atom instanceof RelationalAtom) relationalAtomList.add((RelationalAtom) atom);
//            if (atom instanceof ComparisonAtom) comparisonAtomList.add((ComparisonAtom) atom);
//        }
//
//        System.out.println(headAtom);
//        System.out.println(relationalAtomList);
//        System.out.println(comparisonAtomList);
//
//        SelectOperator selectOperator = new SelectOperator(relationalAtomList.get(0), comparisonAtomList, dbFile);
//        selectOperator.dump();
//        List<Map<Term,Term>> leftTupleMapList = selectOperator.getTupleMapList();
//        selectOperator.reset();
//
//        JoinOperator joinOperator = new JoinOperator(leftTupleMapList, relationalAtomList.get(1), comparisonAtomList, dbFile);
//
//        joinOperator.dump();
//
//        List<Map<Term,Term>> tupleMapList = joinOperator.getTupleMapList();
//
//        for (Map<Term,Term> oneTupleMap : tupleMapList) {
////            System.out.println(oneTupleMap);
//            for (int i = 0; i < headTerms.size() - 1; i++) {
//                for (Map.Entry<Term,Term> entry : oneTupleMap.entrySet()) {
//                    if (entry.getKey().equals(headTerms.get(i))) {
//                        System.out.print(entry.getValue() + ", ");
//                    }
//                }
//            }
//            for (Map.Entry<Term,Term> entry : oneTupleMap.entrySet()) {
//                if (entry.getKey().equals(headTerms.get(headTerms.size() - 1))) {
//                    System.out.println(entry.getValue());
//                }
//            }
//        }
//
//        joinOperator.reset();
//        System.out.println();
//        System.out.println("reset()");
//        System.out.println();
//        joinOperator.dump();
//
//        tupleMapList = joinOperator.getTupleMapList();
//
//        System.out.println(tupleMapList.get(0).keySet());
//
//        for (Map<Term,Term> oneTupleMap : tupleMapList) {
////            System.out.println(oneTupleMap);
//            for (int i = 0; i < headTerms.size() - 1; i++) {
//                for (Map.Entry<Term,Term> entry : oneTupleMap.entrySet()) {
//                    if (entry.getKey().equals(headTerms.get(i))) {
//                        System.out.print(entry.getValue() + ", ");
//                    }
//                }
//            }
//            for (Map.Entry<Term,Term> entry : oneTupleMap.entrySet()) {
//                if (entry.getKey().equals(headTerms.get(headTerms.size() - 1))) {
//                    System.out.println(entry.getValue());
//                }
//            }
//        }


        // test QueryPlan
//        QueryPlan queryPlan = new QueryPlan("Q(SUM(y)) :- R(x, y, z), S(x, w, t)", dbFile);
//        queryPlan.dump();
//        List<String> outputStringList = new ArrayList<>(queryPlan.getOutputStringList());
//        System.out.println(outputStringList);


        // test SumOperator
//        SumOperator sumOperator = new SumOperator("Q(SUM(y)) :- R(x, y, z), S(x, w, t)", dbFile);
//        sumOperator.dump();
//        List<String> outputStringList = new ArrayList<>(sumOperator.getOutputStringList());
//        System.out.println(outputStringList);


        // test AvgOperator
//        AvgOperator avgOperator = new AvgOperator("Q(AVG(y)) :- R(x, y, z), S(x, w, t)", dbFile);
//        avgOperator.dump();
//        List<String> outputStringList = new ArrayList<>(avgOperator.getOutputStringList());
//        System.out.println(outputStringList);


        // test all
//        String query = "Q(x, SUM(t)) :- R(x, y, z), S(x, w, t), x >= 5";
//        String head = query.substring(0, query.indexOf(":-"));
//        System.out.println(head);
//
//        List<String> outputStringList = new ArrayList<>();
//
//        if (head.contains("SUM")) {
//            SumOperator sumOperator = new SumOperator(query, dbFile);
//            sumOperator.dump();
//            outputStringList = sumOperator.getOutputStringList();
//        }
//        else if (head.contains("AVG")) {
//            AvgOperator avgOperator = new AvgOperator(query, dbFile);
//            avgOperator.dump();
//            outputStringList =avgOperator.getOutputStringList();
//        }
//        else {
//            QueryPlan queryPlan = new QueryPlan(query, dbFile);
//            queryPlan.dump();
//            outputStringList = queryPlan.getOutputStringList();
//        }
//
//        System.out.println(outputStringList.get(0));
//        System.out.println(outputStringList.get(1));
    }
}
