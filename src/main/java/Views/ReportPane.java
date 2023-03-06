package Views;

import OLCCompiler.Utils.ReporthPaths;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ReportPane extends JScrollPane {

    public ReportPane(String imagePath) {
        super(new ImagePane(imagePath));
        initComponents();
    }

    private void initComponents(){
        setPreferredSize(new Dimension(400, 400));
    }

}
