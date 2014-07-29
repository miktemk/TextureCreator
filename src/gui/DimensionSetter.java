package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class DimensionSetter extends JPanel implements ActionListener, FocusListener
{
    ImageManager man;
    JTextField wF, hF;
    int w = 128, h = 128;
    public DimensionSetter(ImageManager man)
    {
        super(new FlowLayout());
        this.man = man;
        
        wF = new JTextField(""+w, 7);
        hF = new JTextField(""+h, 7);
        wF.addActionListener(this);
        wF.addFocusListener(this);
        hF.addActionListener(this);
        hF.addFocusListener(this);
        
        add(new JLabel("Resulting Image:  "));
        add(new JLabel("Width:"));
        add(wF);
        add(new JLabel("Height:"));
        add(hF);
    }
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if(source == wF)
        {
            try
            {
                int wtmp = Integer.parseInt(wF.getText());
                w = wtmp;
                man.setDim(w, h);
            }
            catch(Exception ex)
            {
                wF.setText(""+w);
            }
        }
        else if(source == hF)
        {
            try
            {
                int htmp = Integer.parseInt(hF.getText());
                h = htmp;
                man.setDim(w, h);
            }
            catch(Exception ex)
            {
                hF.setText(""+h);
            }
        }
    }
    public void focusGained(FocusEvent e){}
    public void focusLost(FocusEvent e)
    {
        Object source = e.getSource();
        if(source == wF)
        {
            try
            {
                int wtmp = Integer.parseInt(wF.getText());
                w = wtmp;
                man.setDim(w, h);
            }
            catch(Exception ex)
            {
                wF.setText(""+w);
            }
        }
        else if(source == hF)
        {
            try
            {
                int htmp = Integer.parseInt(hF.getText());
                h = htmp;
                man.setDim(w, h);
            }
            catch(Exception ex)
            {
                hF.setText(""+h);
            }
        }
    }
}
