package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class ImageDisplay extends JPanel
{
    BufferedImage i = null;
    public ImageDisplay(){}
    public ImageDisplay(BufferedImage i)
    {
        this.i = i;
    }
    public void setImage(BufferedImage i)
    {
        this.i = i;
        repaint();
    }
    public void paint(Graphics g1)
    {
        Graphics2D g = (Graphics2D)g1;
        g.setColor(Color.black);
        g.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        if(i != null)
        {
            int w = i.getWidth();
            int h = i.getHeight();
            g.scale((double)getWidth()/w, (double)getHeight()/h);
            g.drawImage(i, 0, 0, this);
        }
    }
}
