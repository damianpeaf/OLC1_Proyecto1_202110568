package Views;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImagePane extends JPanel {

    private String imagePath;
    private Image image;

    public ImagePane(String imagePath) {
        this.imagePath = imagePath;
        initComponents();
    }

    private void initComponents(){

        try {
            image = ImageIO.read(new File(this.imagePath));
        }catch (Exception e){
            System.out.println(e);
        }
        setPreferredSize(getImageSize());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(image, 0, 0, null);
    }

    private Dimension getImageSize() {
        if (image == null) {
            return new Dimension(0, 0);
        } else {
            return new Dimension(image.getWidth(null), image.getHeight(null));
        }
    }
}
