/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package area.recognizer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author maryan
 */
class Label {

    private final int red;
    private final int green;
    private final int blue;
    private final Color randomColor;

    public Label(Color color) {
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.randomColor = new Color((int )(Math.random() * 255 ), (int )(Math.random() * 255 ),
                (int )(Math.random() * 255 ));
       
    }

    public boolean isThisLabel(Color c, int n) {
        int cent = 255 / 100;
        int percent1 = (Math.abs(this.red - c.getRed()) / cent);
        int percent2 = (Math.abs(this.green - c.getGreen()) / cent);
        int percent3 = (Math.abs(this.blue - c.getBlue()) / cent);
        int totalPercent = (percent1 + percent2 + percent3) / 3;
        return n > totalPercent;
    }

    public int getRGBColor() {
        return this.randomColor.getRGB();
    }

}

public class AreaRecognizer {
    
    public static void setArea(BufferedImage source, BufferedImage des, int i, int j, Label l) {
        try {
            for(int x = i-1; x <= i + 1; x++ ) {
                for (int y = j - 1; y <= j + 1; y++) {
                    if(x !=i || y !=j) {
                        Color test = new Color(source.getRGB(x, y));

                            if ( test.getRGB() != l.getRGBColor()) {
                                des.setRGB(x,y, new Color(255, 0, 0).getRGB());
                            }
                    }    
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
                    
                }
    }
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        
        for (int cluster = 1; cluster <= 5; cluster++) {
            BufferedImage img = ImageIO.read(new File("image.png"));
            List<Label> list = new ArrayList<>();

            for (int i = 0; i < img.getHeight(); i++) {
                for (int j = 0; j < img.getWidth(); j++) {

                    Color color = new Color(img.getRGB(i, j));

                    if (list.isEmpty()) {
                        Label label = new Label(color);
                        list.add(label);
                    }
                    boolean check = false;
                    for (Label l : list) {
                        if (l.isThisLabel(color, 100 / cluster)) {
                            img.setRGB(i, j, l.getRGBColor());

                            check = true;
                        }
                    }
                    if (!check) {
                        list.add(new Label(color));
                    }
                }
            }
            File outputfile = new File("saved"+cluster+".png");
            
            ImageIO.write(img, "png", outputfile);
            
            
            
            BufferedImage img1 = ImageIO.read( new File("saved"+cluster+".png"));
            int count = 0;
            for (Label l : list) {
                BufferedImage des = ImageIO.read(new File("image.png"));
                count ++;


                for (int i = 0; i < img1.getHeight(); i++) {
                    for (int j = 0; j < img1.getWidth(); j++) {

                        Color color = new Color(img1.getRGB(i, j));

                            if (color.getRGB() == l.getRGBColor()) {
                                AreaRecognizer.setArea(
                                        img1, des, i, j, l);
                            }
                        }// j
                    } // i

                File outputfile1 = new File("area"+cluster+"Label"+count+ ".png");
                ImageIO.write(des, "png", outputfile1);
            } 
            
            
        }
        

    }
}
