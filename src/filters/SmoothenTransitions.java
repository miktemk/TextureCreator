package filters;

import gui.TCFilter;
import gui.TFSlider;
import gui.ValueSetListener;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import gui.*;

public class SmoothenTransitions extends TCFilter implements ValueSetListener
{
    JPanel gui;
    int xDepth = 2,
        yDepth = 2;
    public SmoothenTransitions()
    {
        gui = new JPanel(new GridLayout(2, 1));
        gui.add(new TFSlider(TFSlider.INT, 0, 50, xDepth, "Horizontal Depth", this));
        gui.add(new TFSlider(TFSlider.INT, 0, 50, yDepth, "Vertical Depth", this));
    }
    public void filter(BufferedImage src, BufferedImage dest)
    {
        for(int y = 0; y < src.getHeight(); y++)
        {
            for(int x = 0; x < src.getWidth(); x++)
            {
                int rgb = src.getRGB(x, y);
                int r = r(rgb);
                int g = g(rgb);
                int b = b(rgb);
                if(x < xDepth)
                {
                    int rgb2 = src.getRGB(src.getWidth()-x-1, y);
                    int r2 = r(rgb2);
                    int g2 = g(rgb2);
                    int b2 = b(rgb2);
                    r = (int)((double)(x+xDepth)*r/(2*xDepth) + (double)(xDepth-x)*r2/(2*xDepth));
                    g = (int)((double)(x+xDepth)*g/(2*xDepth) + (double)(xDepth-x)*g2/(2*xDepth));
                    b = (int)((double)(x+xDepth)*b/(2*xDepth) + (double)(xDepth-x)*b2/(2*xDepth));
                }
                else if(x >= src.getWidth()-xDepth)
                {
                    int rgb2 = src.getRGB(src.getWidth()-x, y);
                    int r2 = r(rgb2);
                    int g2 = g(rgb2);
                    int b2 = b(rgb2);
                    r = (int)((double)(src.getWidth()-x+xDepth)*r/(2*xDepth) + (double)(xDepth-src.getWidth()+x)*r2/(2*xDepth));
                    g = (int)((double)(src.getWidth()-x+xDepth)*g/(2*xDepth) + (double)(xDepth-src.getWidth()+x)*g2/(2*xDepth));
                    b = (int)((double)(src.getWidth()-x+xDepth)*b/(2*xDepth) + (double)(xDepth-src.getWidth()+x)*b2/(2*xDepth));
                }
                if(y < yDepth)
                {
                    int rgb2 = src.getRGB(x, src.getHeight()-y-1);
                    int r2 = r(rgb2);
                    int g2 = g(rgb2);
                    int b2 = b(rgb2);
                    r = (int)((double)(y+yDepth)*r/(2*yDepth) + (double)(yDepth-y)*r2/(2*yDepth));
                    g = (int)((double)(y+yDepth)*g/(2*yDepth) + (double)(yDepth-y)*g2/(2*yDepth));
                    b = (int)((double)(y+yDepth)*b/(2*yDepth) + (double)(yDepth-y)*b2/(2*yDepth));
                }
                else if(y >= src.getHeight()-yDepth)
                {
                    int rgb2 = src.getRGB(x, src.getHeight()-y-1);
                    int r2 = r(rgb2);
                    int g2 = g(rgb2);
                    int b2 = b(rgb2);
                    r = (int)((double)(src.getHeight()-y+yDepth)*r/(2*yDepth) + (double)(yDepth-src.getHeight()+y)*r2/(2*yDepth));
                    g = (int)((double)(src.getHeight()-y+yDepth)*g/(2*yDepth) + (double)(yDepth-src.getHeight()+y)*g2/(2*yDepth));
                    b = (int)((double)(src.getHeight()-y+yDepth)*b/(2*yDepth) + (double)(yDepth-src.getHeight()+y)*b2/(2*yDepth));
                }
                dest.setRGB(x, y, (65536*r) | (256*g) | b);
            }
        }
    }
    public Component getGUI()
    {
        return gui;
    }
    public void reset(){}
    public String toString()
    {
        return "Smoothen Transitions: xDepth="+xDepth+" yDepth="+yDepth;
    }
    public void valueSet(TFSlider s)
    {
        if(s.getName() == "Horizontal Depth")
        {
            xDepth = s.intValue();
        }
        if(s.getName() == "Vertical Depth")
        {
            yDepth = s.intValue();
        }
        update();
    }
}
