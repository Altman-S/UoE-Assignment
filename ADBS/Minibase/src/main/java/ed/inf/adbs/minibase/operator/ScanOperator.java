package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.parser.QueryParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Class ScanOperator: Scan each line in the .csv file, and output it.
 *
 * @author Pai
 * @version 2022.03.12
 */
public class ScanOperator extends Operator {

    private RelationalAtom relationalAtom;
    private String csvFile;
    private String schemaFile;
    private BufferedReader csvReader;

    private Tuple tuple = new Tuple(null);
    private List<Map<Term,Term>> tupleMapList = new ArrayList<>();

    /**
     * Initialize ScanOperator with relationalAtom and dbFile
     * relationalAtom can provide the type of Terms in the map of Tuple
     * dbFile can provide the access to .csv file and schema.txt
     *
     * @param relationalAtom relationalAtom which is needed to be scanned
     * @param dbFile path to database files
     */
    public ScanOperator(RelationalAtom relationalAtom, String dbFile) {
        this.relationalAtom = relationalAtom;
        // terms: [x, y, z]
        List<Term> terms = this.relationalAtom.getTerms();
        // the file it should read
        this.csvFile = dbFile + "/files/" + relationalAtom.getName() + ".csv";
        // To find the schema of relationalAtom
        this.schemaFile = dbFile + "/schema.txt";

//        System.out.println(this.csvFile);
//        System.out.println(this.schemaFile);
//        System.out.println();

        try {
            // read schema.txt, and find R int int string
            BufferedReader br = new BufferedReader(new FileReader(this.schemaFile));
            String line = "";
            // line will be like "R int int string"
            line = br.readLine();
            while (line != null) {
                String[] newStr = line.split("\\s+");
                // initialise HashMap in the Tuple
                if (newStr[0].equals(relationalAtom.getName())) {
                    for (int i = 1; i < newStr.length; i++) {
                        if (newStr[i].equals("int")) {
                            this.tuple.put(terms.get(i - 1), new IntegerConstant(null));
                        }
                        else if (newStr[i].equals("string")) {
                            this.tuple.put(terms.get(i - 1), new StringConstant(null));
                        }
                    }
                    break;
                }
                line = br.readLine();
            }

            // read R.csv and get data
            this.csvReader = new BufferedReader(new FileReader(this.csvFile));
        }
        catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }

//        System.out.println("initialise ScanOperator");
    }


    /**
     * Read next line in the .csv file and set current Tuple
     *
     * @return next Tuple
     */
    @Override
    public Tuple getNextTuple() {
        try {
//            Tuple newTuple = new Tuple(this.csvReader.readLine());
            this.tuple.setValue(this.csvReader.readLine(), this.relationalAtom);

            if (this.tuple.getValue() != null) {
                // add Tuple to tupleList
                Map<Term,Term> newMap = new HashMap<>(this.tuple.getMap());
                this.tupleMapList.add(newMap);

                return this.tuple;
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }

        return null;
    }


    public List<Map<Term,Term>> getTupleMapList() {
        return this.tupleMapList;
    }


    /**
     * Reset ScanOperator
     * Reset current csvReader, so it can read from the first line in the .csv file
     */
    @Override
    public void reset() {
        this.tupleMapList = new ArrayList<>();

        try {
            // Reset this.csvReader
            this.csvReader = new BufferedReader(new FileReader(this.csvFile));
            // Reset this.tuple
            this.tuple = new Tuple(null);

            List<Term> terms = this.relationalAtom.getTerms();
            BufferedReader br = new BufferedReader(new FileReader(this.schemaFile));
            String line = "";
            // line will be like "R int int string"
            line = br.readLine();
            while (line != null) {
                String[] newStr = line.split("\\s+");
                // initialise HashMap in the Tuple
                if (newStr[0].equals(relationalAtom.getName())) {
                    for (int i = 1; i < newStr.length; i++) {
                        if (newStr[i].equals("int")) {
                            this.tuple.put(terms.get(i - 1), new IntegerConstant(null));
                        }
                        else if (newStr[i].equals("string")) {
                            this.tuple.put(terms.get(i - 1), new StringConstant(null));
                        }
                    }
                    break;
                }
                line = br.readLine();
            }
        }
        catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }


    /**
     * Test function
     */
    @Override
    public void dump() {
        this.tuple = getNextTuple();
        while (this.tuple != null) {
//            System.out.println(this.tuple.getValue());
//            System.out.println(this.tuple.getMap());

            Query query = QueryParser.parse("Q(x,y,z) :- R(x, y, z)");
            List<Atom> body = query.getBody();
            RelationalAtom body1 = (RelationalAtom) body.get(0);
            List<Term> terms = body1.getTerms();
            Term termX = terms.get(0);
            Term termZ = terms.get(2);

            Map<Term, Term> hm = this.tuple.getMap();

//            for (Map.Entry<Term,Term> entry : hm.entrySet()) {
//                System.out.println("Key: " + entry.getKey().getClass() + " Value: " + entry.getValue().getClass());
//            }

            StringBuilder outputSb = new StringBuilder();
            for (Map.Entry<Term,Term> entry : hm.entrySet()) {
                if (termX.equals(entry.getKey())) {
                    outputSb.append(entry.getValue().toString());
                    outputSb.append(" ");
                }
                if (termZ.equals(entry.getKey())) {
                    outputSb.append(entry.getValue().toString());
                }

            }
            // if x = x, output it
            System.out.println(outputSb);

            this.tuple = getNextTuple();
        }
    }
}
