package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class TFSlider extends JPanel implements ActionListener, ChangeListener, FocusListener
{
    JSlider s;
    JTextField tf;
    String title;
    ValueSetListener listener;
    int mode;
    double value;
    double min, max;
    public static final int INT = 0,
                            DOUBLE = 1;
    final int res = 1000;
    final int dcplc = 6;
    public TFSlider(int mode, double min, double max, double value, String title, ValueSetListener listener)
    {
        super(new BorderLayout());
        this.mode = mode;
        this.min = min;
        this.max = max;
        this.value = value;
        this.title = title;
        this.listener = listener;
        
        if(mode == INT)
        {
            s = new JSlider((int)min, (int)max, (int)value);
            tf = new JTextField(""+(int)value, 8);
        }
        else if(mode == DOUBLE)
        {
            s = new JSlider(0, res, (int)((value-min)*res/(max-min)));
            tf = new JTextField(""+sigfig(value, dcplc), 8);
        }
        else
        {
            throw new IllegalArgumentException("Illegal TFSlider mode!");
        }
        
        tf.addActionListener(this);
        tf.addFocusListener(this);
        s.addChangeListener(this);
        
        add(s, BorderLayout.CENTER);
        add(tf, BorderLayout.EAST);
        add(new JLabel(title+":"), BorderLayout.WEST);
    }
    public void setMin(double min)
    {
        this.min = min;
        if(value < min) value = min;
        s.setMinimum((int)min);
        if(mode == INT)
        {
            tf.setText(""+(int)value);
            s.setValue((int)value);
        }
        else if(mode == DOUBLE)
        {
            tf.setText(""+sigfig(value, dcplc));
            s.setValue((int)((value-min)*res/(max-min)));
        }
        listener.valueSet(this);
    }
    public void setMax(double max)
    {
        this.max = max;
        if(value > max) value = max;
        s.setMaximum((int)max);
        if(mode == INT)
        {
            tf.setText(""+(int)value);
            s.setValue((int)value);
        }
        else if(mode == DOUBLE)
        {
            tf.setText(""+sigfig(value, dcplc));
            s.setValue((int)((value-min)*res/(max-min)));
        }
        listener.valueSet(this);
    }
    private double sigfig(double a, int decimalPlaces) { return Math.round(Math.pow(10, decimalPlaces)*a)/Math.pow(10, decimalPlaces); }
    public String getName()
    {
        return title;
    }
    public int intValue()
    {
        return (int)value;
    }
    public double value()
    {
        return value;
    }
    //########################################
    public void actionPerformed(ActionEvent e)
    {
        try
        {
            double val = Double.parseDouble(tf.getText());
            if(val < min)      val = min;
            else if(val > max) val = max;
            value = val;
            if(mode == INT)
            {
                tf.setText(""+(int)value);
                s.setValue((int)value);
            }
            else if(mode == DOUBLE)
            {
                tf.setText(""+sigfig(value, dcplc));
                s.setValue((int)((value-min)*res/(max-min)));
            }
            listener.valueSet(this);
        }
        catch(Exception ex)
        {
            if(mode == INT)
            {
                tf.setText(""+(int)value);
            }
            else if(mode == DOUBLE)
            {
                tf.setText(""+sigfig(value, dcplc));
            }
        }
    }
    public void stateChanged(ChangeEvent e)
    {
        int val = s.getValue();
        if(mode == INT)
        {
            value = val;
            tf.setText(""+val);
        }
        else if(mode == DOUBLE)
        {
            value = min + val*(max-min)/res;
            tf.setText(""+sigfig(value, dcplc));
        }
        listener.valueSet(this);
    }
    public void focusGained(FocusEvent e){}
    public void focusLost(FocusEvent e)
    {
        try
        {
            double val = Double.parseDouble(tf.getText());
            if(val < min)      val = min;
            else if(val > max) val = max;
            value = val;
            if(mode == INT)
            {
                tf.setText(""+(int)value);
                s.setValue((int)value);
            }
            else if(mode == DOUBLE)
            {
                tf.setText(""+sigfig(value, dcplc));
                s.setValue((int)((value-min)*res/(max-min)));
            }
            listener.valueSet(this);
        }
        catch(Exception ex)
        {
            if(mode == INT)
            {
                tf.setText(""+(int)value);
            }
            else if(mode == DOUBLE)
            {
                tf.setText(""+sigfig(value, dcplc));
            }
        }
    }
}
