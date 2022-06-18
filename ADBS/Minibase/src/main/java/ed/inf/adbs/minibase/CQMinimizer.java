package ed.inf.adbs.minibase;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.parser.QueryParser;

import ed.inf.adbs.minibase.base.Variable;
import ed.inf.adbs.minibase.base.Constant;
import ed.inf.adbs.minibase.base.IntegerConstant;
import ed.inf.adbs.minibase.base.StringConstant;

import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * Minimization of conjunctive queries
 *
 */
public class CQMinimizer {

    public static void main(String[] args) {

//        try {
//            Query query = QueryParser.parse("Q(x) :- R(x, 3), R(x, z)");
//            RelationalAtom head = query.getHead();
//            List<Atom> body = query.getBody();
//            List<Term> headTerms = head.getTerms();  // special variables in Q
//
//            // Compare atom1 with atom2
//            int len = body.size();
//            for (int i = 0; i < len; ++i) {  // atom1
//                for (int j = 0; j < len; ++j) {  // atom2
//                    // Set atom1 and atom2
//                    RelationalAtom atom1 = (RelationalAtom) body.get(i);
//                    RelationalAtom atom2 = (RelationalAtom) body.get(j);
//                    // atom1 == atom2, skip j
//                    if (i == j) {
//                        continue;
//                    }
//                    List<Term> terms1 = atom1.getTerms();
//                    List<Term> terms2 = atom2.getTerms();
//
//                    // Compare the size of atom1 and atom2, if not equal, skip j
//                    if ((terms1.size() != terms2.size()) || !atom1.getName().equals(atom2.getName())) {
//                        continue;
//                    }
//
//                    // Get Atom's name
//                    String atomName = atom1.getName();
//
//                    ArrayList<Integer> markerList1 = new ArrayList<>();
//                    ArrayList<Integer> markerList2 = new ArrayList<>();
//
//                    // Compare every Term of term1 and term2
//                    int signNotDelete = 0;  // sign for delete  0: delete  1: not delete
//                    int signCompDelete = 0;
//                    int varDelete1 = 0; int varDelete2 = 0;  // sign for delete variable in variable and special variable (for query3)
//
//                    for (int k = 0; k < terms1.size(); ++k) {
//                        // Sign for delete term1 and term2
//
//                        Term term1 = terms1.get(k);
//                        Term term2 = terms2.get(k);
//
//                        // Case1:  term1: special variable  term2: special variable
//                        if (headTerms.contains(term1) && headTerms.contains(term2)) {
//                            if (!term1.equals(term2)) {  // 2 different Variable, not able to delete
//                                signNotDelete = 1;
//                                break;
//                            } else {
//                                markerList1.add(0); markerList2.add(0);
//                            }
//                        }
//                        // Case2:  term1: special variable  term2: variable
//                        else if (headTerms.contains(term1) && !headTerms.contains(term2) && term2 instanceof Variable) {
//                            varDelete2 = 1;
//                            markerList1.add(0); markerList2.add(1);
//                        }
//                        // Case3:  term1: special variable  term2: constant
//                        else if (headTerms.contains(term1) && term2 instanceof Constant) {
//                            signNotDelete = 1;
//                            break;
//                        }
//                        // Case4:  term1: variable          term2: special variable
//                        else if (!headTerms.contains(term1) && term1 instanceof Variable && headTerms.contains(term2)) {
//                            varDelete1 = 1;
//                            markerList1.add(1); markerList2.add(0);
//                        }
//                        // Case5:  term1: variable          term2: variable
//                        else if (!headTerms.contains(term1) && term1 instanceof Variable && !headTerms.contains(term2) && term2 instanceof Variable) {
//                            markerList1.add(0); markerList2.add(0);
//                        }
//                        // Case6:  term1: variable          term2: constant
//                        else if (!headTerms.contains(term1) && term1 instanceof Variable && term2 instanceof Constant) {
//                            for (int p = 0; p < len; p++) {
//                                if (p == i || p == j) continue;
//                                RelationalAtom compAtom1 = (RelationalAtom) body.get(p);
//                                if (compAtom1.getName().equals(atom1.getName())) continue;
//                                List<Term> compTerms1 = compAtom1.getTerms();
//                                if (compTerms1.contains(term1)) {
//                                    signCompDelete = 1;
//                                    for (int q = 0; q < len; q++) {
//                                        if (q == i || q == j || q == p) continue;
//                                        RelationalAtom compAtom2 = (RelationalAtom) body.get(q);
//                                        if (!compAtom1.getName().equals(compAtom2.getName())) continue;
//                                        List<Term> compTerm2 = compAtom2.getTerms();
//                                        if (compTerm2.contains(term2)) {
//                                            signCompDelete = 0;
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
//                            if (signCompDelete == 1) {
//                                signNotDelete = 1;
//                                break;
//                            } else {
//                                markerList1.add(1); markerList2.add(0);
//                            }
//                        }
//                        // Case7:  term1: constant          term2: special variable
//                        else if (term1 instanceof Constant && headTerms.contains(term2)) {
//                            signNotDelete = 1;
//                            break;
//                        }
//                        // Case8:  term1: constant          term2: variable
//                        else if (term1 instanceof Constant && !headTerms.contains(term2) && term2 instanceof Variable) {
//                            for (int p = 0; p < len; p++) {
//                                if (p == i || p == j) continue;
//                                RelationalAtom compAtom1 = (RelationalAtom) body.get(p);
//                                if (compAtom1.getName().equals(atom2.getName())) continue;
//                                List<Term> compTerms1 = compAtom1.getTerms();
//                                if (compTerms1.contains(term2)) {
//                                    signCompDelete = 1;
//                                    for (int q = 0; q < len; q++) {
//                                        if (q == i || q == j || q == p) continue;
//                                        RelationalAtom compAtom2 = (RelationalAtom) body.get(q);
//                                        if (!compAtom1.getName().equals(compAtom2.getName())) continue;
//                                        List<Term> compTerm2 = compAtom2.getTerms();
//                                        if (compTerm2.contains(term1)) {
//                                            signCompDelete = 0;
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
//                            if (signCompDelete == 1) {
//                                signNotDelete = 1;
//                                break;
//                            } else {
//                                markerList1.add(0); markerList2.add(1);
//                            }
//                        }
//                        // Case9:  term1: constant          term2: constant
//                        else if (term1 instanceof Constant && term2 instanceof Constant) {
//                            if (!term1.equals(term2)) {
//                                signNotDelete = 1;
//                                break;
//                            } else {
//                                markerList1.add(0); markerList2.add(0);
//                            }
//                        }
//                    }
//
//                    // signDelete: 0 for delete, 1 for not delete
//                    if (signNotDelete != 1) {
//                        if (markerList1.contains(1) && !markerList2.contains(1)) {
//                            body.remove(i);
//                            len--;
//                            i--;
//                            break;  // skip j
//                        }
//                        else if (!markerList1.contains(1) && markerList2.contains(1)) {
//                            body.remove(j);
//                            len--;
//                            j--;
//                            if (i > j) i--;
//                        }
//                        else if (markerList1.contains(1) && markerList2.contains(1)) {  // terms1  terms2
//                            if (varDelete1 != 1 || varDelete2 != 1) {   // for query3
//                                List<Term> termsNew = new ArrayList<>();
//                                for (int m = 0; m < markerList1.size(); ++m) {
//                                    if (markerList1.get(m) == 1) {
//                                        termsNew.add(terms2.get(m));
//                                    } else {
//                                        termsNew.add(terms1.get(m));
//                                    }
//                                }
//                                // Set new Atom     delete i and update j
//                                RelationalAtom atomNew = new RelationalAtom(atomName, termsNew);
//                                body.set(j, atomNew);
//                                body.remove(i);
//                                len--;
//                                i--;
//                                break;  // skip j
//                            }
//                        }
//
//                    }
//
//                }
//            }
//
//            Query queryNew = new Query(head, body);
//            System.out.println(queryNew);
//
//            // Output to file
//            try {
//                FileOutputStream fos = new FileOutputStream("data/minimization/output/query3.txt");
//                String s = queryNew.toString();
//                fos.write(s.getBytes());
//                fos.close();
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        }
//        catch (Exception e)
//        {
//            System.err.println("Exception occurred during parsing");
//            e.printStackTrace();
//        }
        if (args.length != 2) {
            System.err.println("Usage: CQMinimizer input_file output_file");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];

        minimizeCQ(inputFile, outputFile);

        parsingExample(inputFile);

        parsingExample("data/minimization/input/query1.txt");

    }

    /**
     * CQ minimization procedure
     *
     * Assume the body of the query from inputFile has no comparison atoms
     * but could potentially have constants in its relational atoms.
     *
     */
    public static void minimizeCQ(String inputFile, String outputFile) {
        // TODO: add your implementation
        try {
            Query query = QueryParser.parse(Paths.get(inputFile));
            RelationalAtom head = query.getHead();
            List<Atom> body = query.getBody();
            List<Term> headTerms = head.getTerms();  // special variables in Q

            // Compare atom1 with atom2
            int len = body.size();
            for (int i = 0; i < len; ++i) {  // atom1
                for (int j = 0; j < len; ++j) {  // atom2
                    // Set atom1 and atom2
                    RelationalAtom atom1 = (RelationalAtom) body.get(i);
                    RelationalAtom atom2 = (RelationalAtom) body.get(j);

                    // atom1 == atom2, skip j
                    if (i == j) continue;

                    List<Term> terms1 = atom1.getTerms();
                    List<Term> terms2 = atom2.getTerms();

                    // Compare the size of atom1 and atom2, if not equal, skip j
                    if ((terms1.size() != terms2.size()) || !atom1.getName().equals(atom2.getName())) continue;

                    // Get Atom's name
                    String atomName = atom1.getName();

                    ArrayList<Integer> markerList1 = new ArrayList<>();
                    ArrayList<Integer> markerList2 = new ArrayList<>();

                    // Compare every Term of term1 and term2
                    int signNotDelete = 0;  // sign for delete  0: delete  1: not delete
                    int signCompDelete = 0;
                    int varDelete1 = 0; int varDelete2 = 0;

                    for (int k = 0; k < terms1.size(); ++k) {
                        Term term1 = terms1.get(k);
                        Term term2 = terms2.get(k);

                        // Case1:  term1: special variable  term2: special variable
                        if (headTerms.contains(term1) && headTerms.contains(term2)) {
                            if (!term1.equals(term2)) {  // 2 different Variable, not able to delete
                                signNotDelete = 1;
                                break;
                            } else {
                                markerList1.add(0); markerList2.add(0);
                            }
                        }
                        // Case2:  term1: special variable  term2: variable
                        else if (headTerms.contains(term1) && !headTerms.contains(term2) && term2 instanceof Variable) {
                            varDelete2 = 1;
                            markerList1.add(0); markerList2.add(1);
                        }
                        // Case3:  term1: special variable  term2: constant
                        else if (headTerms.contains(term1) && term2 instanceof Constant) {
                            signNotDelete = 1;
                            break;
                        }
                        // Case4:  term1: variable          term2: special variable
                        else if (!headTerms.contains(term1) && term1 instanceof Variable && headTerms.contains(term2)) {
                            varDelete1 = 1;
                            markerList1.add(1); markerList2.add(0);
                        }
                        // Case5:  term1: variable          term2: variable
                        else if (!headTerms.contains(term1) && term1 instanceof Variable && !headTerms.contains(term2) && term2 instanceof Variable) {
                            markerList1.add(0); markerList2.add(0);
                        }
                        // Case6:  term1: variable          term2: constant
                        else if (!headTerms.contains(term1) && term1 instanceof Variable && term2 instanceof Constant) {
                            for (int p = 0; p < len; p++) {
                                if (p == i || p == j) continue;
                                RelationalAtom compAtom1 = (RelationalAtom) body.get(p);
                                if (compAtom1.getName().equals(atom1.getName())) continue;
                                List<Term> compTerms1 = compAtom1.getTerms();
                                if (compTerms1.contains(term1)) {
                                    signCompDelete = 1;
                                    for (int q = 0; q < len; q++) {
                                        if (q == i || q == j || q == p) continue;
                                        RelationalAtom compAtom2 = (RelationalAtom) body.get(q);
                                        if (!compAtom1.getName().equals(compAtom2.getName())) continue;
                                        List<Term> compTerm2 = compAtom2.getTerms();
                                        if (compTerm2.contains(term2)) {
                                            signCompDelete = 0;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (signCompDelete == 1) {
                                signNotDelete = 1;
                                break;
                            } else {
                                markerList1.add(1); markerList2.add(0);
                            }
                        }
                        // Case7:  term1: constant          term2: special variable
                        else if (term1 instanceof Constant && headTerms.contains(term2)) {
                            signNotDelete = 1;
                            break;
                        }
                        // Case8:  term1: constant          term2: variable
                        else if (term1 instanceof Constant && !headTerms.contains(term2) && term2 instanceof Variable) {
                            for (int p = 0; p < len; p++) {
                                if (p == i || p == j) continue;
                                RelationalAtom compAtom1 = (RelationalAtom) body.get(p);
                                if (compAtom1.getName().equals(atom2.getName())) continue;
                                List<Term> compTerms1 = compAtom1.getTerms();
                                if (compTerms1.contains(term2)) {
                                    signCompDelete = 1;
                                    for (int q = 0; q < len; q++) {
                                        if (q == i || q == j || q == p) continue;
                                        RelationalAtom compAtom2 = (RelationalAtom) body.get(q);
                                        if (!compAtom1.getName().equals(compAtom2.getName())) continue;
                                        List<Term> compTerm2 = compAtom2.getTerms();
                                        if (compTerm2.contains(term1)) {
                                            signCompDelete = 0;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (signCompDelete == 1) {
                                signNotDelete = 1;
                                break;
                            } else {
                                markerList1.add(0); markerList2.add(1);
                            }
                        }
                        // Case9:  term1: constant          term2: constant
                        else if (term1 instanceof Constant && term2 instanceof Constant) {
                            if (!term1.equals(term2)) {
                                signNotDelete = 1;
                                break;
                            } else {
                                markerList1.add(0); markerList2.add(0);
                            }
                        }
                    }

                    // sign=1: not able to delete
                    if (signNotDelete != 1) {
                        if (markerList1.contains(1) && !markerList2.contains(1)) {
                            body.remove(i);
                            len--;
                            i--;
                            break;  // skip j
                        }
                        else if (!markerList1.contains(1) && markerList2.contains(1)) {
                            body.remove(j);
                            len--;
                            j--;
                            if (i > j) i--;
                        }
                        else if (markerList1.contains(1) && markerList2.contains(1)) {  // terms1  terms2
                            if (varDelete1 != 1 || varDelete2 != 1) {
                                List<Term> termsNew = new ArrayList<>();
                                for (int m = 0; m < markerList1.size(); ++m) {
                                    if (markerList1.get(m) == 1) {
                                        termsNew.add(terms2.get(m));
                                    } else {
                                        termsNew.add(terms1.get(m));
                                    }
                                }
                                // Set new Atom     delete i and update j
                                RelationalAtom atomNew = new RelationalAtom(atomName, termsNew);
                                body.set(j, atomNew);
                                body.remove(i);
                                len--;
                                i--;
                                break;  // skip j
                            }
                        }
                    }
                }
            }

            Query queryNew = new Query(head, body);

            // Output to file
            try {
                FileOutputStream fos = new FileOutputStream(outputFile);
                String s = queryNew.toString();
                fos.write(s.getBytes());
                fos.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }

    }

    /**
     * Example method for getting started with the parser.
     * Reads CQ from a file and prints it to screen, then extracts Head and Body
     * from the query and prints them to screen.
     */
    public static void parsingExample(String filename) {

        try {
            Query query = QueryParser.parse(Paths.get(filename));
            System.out.println("Entire query: " + query);
            RelationalAtom head = query.getHead();
            System.out.println("Head: " + head);
            List<Atom> body = query.getBody();
            System.out.println("Body: " + body);
        }
        catch (Exception e)
        {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }

}
