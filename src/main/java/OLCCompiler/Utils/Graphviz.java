package OLCCompiler.Utils;

import java.io.File;
import java.io.IOException;

public class Graphviz {

    public static void generatePng(File dot, File image){
        try {
            ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", dot.getAbsolutePath(), "-o", image.getAbsolutePath());            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor();
        } catch (InterruptedException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
