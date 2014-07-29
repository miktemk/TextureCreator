package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class ResultDisplay extends JPanel implements ActionListener
{
    private class ImagePanel extends JPanel
    {
        BufferedImage i;
        int n = 3;
        boolean grids = false;
        Color gridc = Color.black;
        public ImagePanel(BufferedImage i)
        {
            this.i = i;
        }
        public void paint(Graphics g1)
        {
            Graphics2D g = (Graphics2D)g1;
            g.setColor(Color.white);
            g.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
            if(i != null)
            {
                int w = i.getWidth();
                int h = i.getHeight();
                g.scale((double)getWidth()/(n*w), (double)getHeight()/(n*h));
                for(int j = 0; j < n; j++)
                {
                    for(int k = 0; k < n; k++)
                    {
                        g1.drawImage(i, j*w, k*h, this);
                    }
                }
                g.scale((double)(n*w)/getWidth(), (double)(n*h)/getHeight());
            }
            if(grids)
            {
                g.setColor(gridc);
                for(int i = 1; i < n; i++)
                {
                    g.draw(new Line2D.Double(0, (double)getHeight()*i/n, getWidth(), (double)getHeight()*i/n));
                    g.draw(new Line2D.Double((double)getWidth()*i/n, 0, (double)getWidth()*i/n, getHeight()));
                }
            }
        }
        public void setImage(BufferedImage i)
        {
            this.i = i;
            repaint();
        }
        public void setN(int n)
        {
            this.n = n;
            repaint();
        }
        public void setGrids(boolean on)
        {
            grids = on;
            repaint();
        }
        public void setGridColor(Color c)
        {
            gridc = c;
            repaint();
        }
        public Color getGridColor()
        {
            return gridc;
        }
    }
    ImagePanel p;
    JComboBox ns;
    JToggleButton gridlines;
    JButton gridColor;
    public ResultDisplay()
    {
        super(new BorderLayout());
        
        p = new ImagePanel(null);
        JPanel bottom = new JPanel(new FlowLayout());
        
        ns = new JComboBox();
        for(int i = 1; i <= 20; i++)
        {
            ns.addItem(""+i+"x"+i);
        }
        ns.setSelectedIndex(2);
        ns.addActionListener(this);
        bottom.add(ns);
        
        gridlines = new JToggleButton("Gridlines");
        gridlines.addActionListener(this);
        bottom.add(gridlines);
        
        gridColor = new JButton("Grid Color");
        gridColor.addActionListener(this);
        bottom.add(gridColor);
        
        add(p, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }
    public void setImage(BufferedImage i)
    {
        p.setImage(i);
    }
    //#############################
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if(source == ns)
        {
            int i = ns.getSelectedIndex();
            p.setN(i+1);
        }
        else if(source == gridlines)
        {
            p.setGrids(gridlines.isSelected());
        }
        else if(source == gridColor)
        {
            Color newColor = JColorChooser.showDialog(this, "Grid Lines Color", p.getGridColor());
            if(newColor != null) p.setGridColor(newColor);
        }
    }
}
