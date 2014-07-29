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

public class LightenEdges extends TCFilter implements ValueSetListener
{
    double k = 0.7;
    int left = 0,
        right = 0,
        top = 0,
        bottom = 0;
    boolean edgeCrossover = true;
    int threshold = 50;
    
    JPanel gui;
    TFSlider topT, bottomT, leftT, rightT;
    public LightenEdges()
    {
        gui = new JPanel(new BorderLayout());
        
        JPanel edgeBounds = new JPanel(new BorderLayout());
        JPanel edgeCenter = new JPanel(new GridLayout(2, 2));
        topT = new TFSlider(TFSlider.INT, 0, 640, top, "Top", this);
        bottomT = new TFSlider(TFSlider.INT, 0, 640, bottom, "Bottom", this);
        leftT = new TFSlider(TFSlider.INT, 0, 640, left, "Left", this);
        rightT = new TFSlider(TFSlider.INT, 0, 640, right, "Right", this);
        edgeCenter.add(topT);
        edgeCenter.add(bottomT);
        edgeCenter.add(leftT);
        edgeCenter.add(rightT);
        JCheckBox cross = new JCheckBox("");
        edgeBounds.add(new JLabel("Edge Bounds:"), BorderLayout.NORTH);
        edgeBounds.add(edgeCenter, BorderLayout.CENTER);
        
        JPanel center = new JPanel(new GridLayout(2, 1));
        center.add(new TFSlider(TFSlider.DOUBLE, 0, 1, k, "Factor", this));
        center.add(new TFSlider(TFSlider.INT, 0, 255, threshold, "Threshold", this));
        
        gui.add(edgeBounds, BorderLayout.NORTH);
        gui.add(center, BorderLayout.CENTER);
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
                if(edgeCrossover && av(r, g, b) > threshold)
                {
                    if(x < left)
                    {
                        r += k*(255-r)*(left-x)/left;
                        g += k*(255-g)*(left-x)/left;
                        b += k*(255-b)*(left-x)/left;
                    }
                    if(x > src.getWidth()-right)
                    {
                        r += k*(255-r)*(right-src.getWidth()+x)/right;
                        g += k*(255-g)*(right-src.getWidth()+x)/right;
                        b += k*(255-b)*(right-src.getWidth()+x)/right;
                    }
                    if(y < top)
                    {
                        r += k*(255-r)*(top-y)/top;
                        g += k*(255-g)*(top-y)/top;
                        b += k*(255-b)*(top-y)/top;
                    }
                    if(y > src.getHeight()-bottom)
                    {
                        r += k*(255-r)*(bottom-src.getHeight()+y)/bottom;
                        g += k*(255-g)*(bottom-src.getHeight()+y)/bottom;
                        b += k*(255-b)*(bottom-src.getHeight()+y)/bottom;
                    }
                }
                else
                {
                    
                }
                dest.setRGB(x, y, (65536*r) | (256*g) | b);
            }
        }
    }
    public Component getGUI()
    {
        topT.setMax(dest.getHeight());
        bottomT.setMax(dest.getHeight());
        leftT.setMax(dest.getWidth());
        rightT.setMax(dest.getWidth());
        return gui;
    }
    public void reset(){}
    public String toString()
    {
        return "Lighten Edges: L="+left+" R="+right+" T="+top+" B="+bottom+" k="+k+" TH="+threshold;
    }
    //#######################################
    public void valueSet(TFSlider s)
    {
        if(s.getName() == "Top")
        {
            top = s.intValue();
        }
        else if(s.getName() == "Bottom")
        {
            bottom = s.intValue();
        }
        else if(s.getName() == "Left")
        {
            left = s.intValue();
        }
        else if(s.getName() == "Right")
        {
            right = s.intValue();
        }
        else if(s.getName() == "Factor")
        {
            k = s.value();
        }
        else if(s.getName() == "Threshold")
        {
            threshold = s.intValue();
        }
        update();
    }
}
