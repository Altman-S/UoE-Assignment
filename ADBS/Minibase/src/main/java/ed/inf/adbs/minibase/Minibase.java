package ed.inf.adbs.minibase;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.operator.AvgOperator;
import ed.inf.adbs.minibase.operator.QueryPlan;
import ed.inf.adbs.minibase.operator.SumOperator;
import ed.inf.adbs.minibase.parser.QueryParser;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory database system
 *
 */
public class Minibase {

    public static void main(String[] args) {

        if (args.length != 3) {
            System.err.println("Usage: Minibase database_dir input_file output_file");
            return;
        }

        String databaseDir = args[0];
        String inputFile = args[1];
        String outputFile = args[2];

        evaluateCQ(databaseDir, inputFile, outputFile);

        parsingExample(inputFile);
    }

    public static void evaluateCQ(String databaseDir, String inputFile, String outputFile) {
        // TODO: add your implementation
        try {

            File filename = new File(inputFile); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String query = "";
            query = br.readLine();

            String head = query.substring(0, query.indexOf(":-"));
            List<String> outputStringList;

            if (head.contains("SUM")) {
                SumOperator sumOperator = new SumOperator(query, databaseDir);
                sumOperator.dump();
                outputStringList = sumOperator.getOutputStringList();
            }
            else if (head.contains("AVG")) {
                AvgOperator avgOperator = new AvgOperator(query, databaseDir);
                avgOperator.dump();
                outputStringList = avgOperator.getOutputStringList();
            }
            else {
                QueryPlan queryPlan = new QueryPlan(query, databaseDir);
                queryPlan.dump();
                outputStringList = queryPlan.getOutputStringList();
            }

            // Output to file
            File writeFile = new File(outputFile);
            try{
                BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));

                for (int i = 0; i < outputStringList.size(); i++) {
                    writeText.write(outputStringList.get(i));
                    if (i < outputStringList.size() - 1) writeText.newLine();
                }

                writeText.flush();
                writeText.close();
            }
            catch (FileNotFoundException e){
                System.out.println("File Not Found");
            }
            catch (IOException e){
                System.out.println("IO Exception");
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
//            Query query = QueryParser.parse("Q(x, y) :- R(x, z), S(y, z, w), z < w");
//            Query query = QueryParser.parse("Q(x, w) :- R(x, 'z'), S(4, z, w), 4 < 'test string' ");

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
