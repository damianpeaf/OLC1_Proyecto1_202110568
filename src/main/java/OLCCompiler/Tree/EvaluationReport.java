package OLCCompiler.Tree;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class EvaluationReport {

    ArrayList<Evaluation> evaluations = new ArrayList<>();
    private final String baseReportPath = "src/reports/SALIDAS_202110568";


    public void add(Evaluation evaluation){
        evaluations.add(evaluation);
    }

    public void json(String name) {

        try (PrintWriter out = new PrintWriter(new File(this.baseReportPath + "/"+ name + ".json"))) {

            out.write("[");
            for (int i = 0; i < evaluations.size(); i++) {
                out.write(evaluations.get(i).json());
                if (i != evaluations.size() - 1) {
                    out.write(",");
                }
            }
            out.write("]");

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
