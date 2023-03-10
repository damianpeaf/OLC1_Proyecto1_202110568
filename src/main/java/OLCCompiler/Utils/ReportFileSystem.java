package OLCCompiler.Utils;

import java.io.File;

public class ReportFileSystem {

    public static String treeReportPath;
    public static String dfaReportPath;
    public static String ndfaReportPath;
    public static String nextTableReportPath;
    public static String transitionTableReportPath;
    public static String errorReportPath;
    public static String outputReportPath;

    public static String filename;

    public static boolean createReportsFolders(String filename){
        try {

            if(filename != null){
                ReportFileSystem.filename = filename;
            }else{
                ReportFileSystem.filename = "default";
            }

            File tree = new File("reports/ARBOLES_202110568");
            File dfa = new File("reports/AFD_202110568");
            File ndfa = new File("reports/AFND_202110568");
            File nextTable = new File("reports/SIGUIENTES_202110568");
            File transitionTable = new File("reports/TRANSICIONES_202110568");
            File error = new File("reports/ERRORES_202110568");
            File output = new File("reports/SALIDA_202110568");

            if (!tree.exists()) {
                tree.mkdirs();
            }

            if (!dfa.exists()) {
                dfa.mkdirs();
            }

            if (!ndfa.exists()) {
                ndfa.mkdirs();
            }

            if (!nextTable.exists()) {
                nextTable.mkdirs();
            }

            if (!transitionTable.exists()) {
                transitionTable.mkdirs();
            }

            if (!error.exists()) {
                error.mkdirs();
            }

            if (!output.exists()) {
                output.mkdirs();
            }

            treeReportPath = tree.getAbsolutePath();
            dfaReportPath = dfa.getAbsolutePath();
            ndfaReportPath = ndfa.getAbsolutePath();
            nextTableReportPath = nextTable.getAbsolutePath();
            transitionTableReportPath = transitionTable.getAbsolutePath();
            errorReportPath = error.getAbsolutePath();
            outputReportPath = output.getAbsolutePath();

            return true;
        }catch (Exception e){
            return false;
        }
    }

}
