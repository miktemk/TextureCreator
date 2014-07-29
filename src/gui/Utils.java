package gui;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

class ImageFrame extends JFrame
{
    private class ImagePanel extends JPanel
    {
        Image i;
        public ImagePanel(Image i)
        {
            this.i = i;
        }
        public void paint(Graphics g)
        {
            g.drawImage(i, 0, 0, this);
        }
    }
    public ImageFrame(Image i)
    {
        super("This is black as BLOODY HELL!!");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new ImagePanel(i), BorderLayout.CENTER);
        
        setSize(i.getWidth(this)+100, i.getHeight(this)+100);
        setVisible(true);
    }
}

public class Utils
{
    public static void skew(BufferedImage src,
                            BufferedImage dest,
                            int x1, int y1,
                            int x2, int y2,
                            int x3, int y3,
                            int x4, int y4)
    {
        int w = dest.getWidth();
        int h = dest.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < h; y++)
        {
            for(int x = 0; x < w; x++)
            {
                double cax = (double)(x2-x1)*x/w;
                double cay = (double)(y2-y1)*x/w;
                double ccx = (double)(x3-x4)*x/w + x4 - x1;
                double ccy = (double)(y3-y4)*x/w + y4 - y1;
                
                double srcX = x1 + cax + (ccx-cax)*y/h;
                double srcY = y1 + cay + (ccy-cay)*y/h;
                try
                {
                    dest.setRGB(x, y, src.getRGB((int)Math.round(srcX), (int)Math.round(srcY)));
                }
                catch(Exception ex)
                {
                    System.out.println("Error at: x = "+x+"   y = "+y+"   srcX = "+srcX+"   srcY = "+srcY);
                }
            }
        }
    }
    public static BufferedImage skew(BufferedImage src,
                                     int x1, int y1,
                                     int x2, int y2,
                                     int x3, int y3,
                                     int x4, int y4,
                                     boolean magX, boolean magY)
    {
        double len12 = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
        double len23 = Math.sqrt((x3-x2)*(x3-x2) + (y3-y2)*(y3-y2));
        double len34 = Math.sqrt((x4-x3)*(x4-x3) + (y4-y3)*(y4-y3));
        double len14 = Math.sqrt((x4-x1)*(x4-x1) + (y4-y1)*(y4-y1));
        int w = 0;
        if(magX) w = (int)Math.max(len12, len34);
        else     w = (int)Math.min(len12, len34);
        int h = 0;
        if(magY) h = (int)Math.max(len23, len14);
        else     h = (int)Math.min(len23, len14);//*/
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < h; y++)
        {
            for(int x = 0; x < w; x++)
            {
                double cax = (double)(x2-x1)*x/w;
                double cay = (double)(y2-y1)*x/w;
                double ccx = (double)(x3-x4)*x/w + x4 - x1;
                double ccy = (double)(y3-y4)*x/w + y4 - y1;
                
                double srcX = x1 + cax + (ccx-cax)*y/h;
                double srcY = y1 + cay + (ccy-cay)*y/h;
                try
                {
                    bi.setRGB(x, y, src.getRGB((int)Math.round(srcX), (int)Math.round(srcY)));
                }
                catch(Exception ex)
                {
                    System.out.println("Error at: x = "+x+"   y = "+y+"   srcX = "+srcX+"   srcY = "+srcY);
                }
            }
        }
        return bi;
    }
    /** Copies src into dest */
    public static void copyImage(BufferedImage src, BufferedImage dest)
    {
        for(int y = 0; y < src.getHeight(); y++)
        {
            for(int x = 0; x < src.getWidth(); x++)
            {
                dest.setRGB(x, y, src.getRGB(x, y));
            }
        }
    }
    public static BufferedImage loadImage(String filename)
    {
        Image img = Toolkit.getDefaultToolkit().getImage(filename);
        MediaTracker mt = new MediaTracker(new Container());
        mt.addImage(img, 0);
        try
        {
            mt.waitForID(0);
        }
        catch(InterruptedException e) {}//*/
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bi.createGraphics();
        big.drawImage(img, 0, 0, null);
        return bi;
    }
    /* //Testing purposes 
    public static void main(String[] args)
    {
        BufferedImage bi1 = loadImage("cracks1.jpg");
        BufferedImage bi2 = skew(bi1, 0, 10, 300, 10, 0, 200, 300, 200, true, true);
        new ImageFrame(bi1);
        new ImageFrame(bi2);
    }//*/
}
