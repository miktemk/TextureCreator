package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
//import debug.*;

public class FilterSetter extends JPanel
{
    ImageManager man;
    ImageDisplay srcDisp, destDisp;
    JPanel bottom;
    public FilterSetter(ImageManager man)
    {
        super(new BorderLayout());
        this.man = man;
        
        srcDisp = new ImageDisplay();
        destDisp = new ImageDisplay();
        JPanel imgs = new JPanel(new GridLayout(1, 2));
        imgs.add(srcDisp);
        imgs.add(destDisp);
        JPanel labels = new JPanel(new GridLayout(1, 2));
        labels.add(new JLabel("Before:"));
        labels.add(new JLabel("After:"));
        bottom = new JPanel();
        
        add(labels, BorderLayout.NORTH);
        add(imgs, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        
        setPreferredSize(new Dimension(800, 500));
    }
    public void setGUI(Component c)
    {
        bottom.removeAll();
        bottom.add(c);
    }
    public void set(TCFilter filter)
    {
        BufferedImage src = man.getImageBeforeFilter(filter);
        BufferedImage dest = man.getImage2BeforeFilter(filter);
        setGUI(filter.getSettingGUI(src, dest, destDisp));
        srcDisp.setImage(src);
        destDisp.setImage(dest);
        int option = JOptionPane.showConfirmDialog(this, this, "Filter Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(option == JOptionPane.OK_OPTION)
        {
            
        }
        else
        {
            filter.reset();
        }
        man.refresh();
    }
}
