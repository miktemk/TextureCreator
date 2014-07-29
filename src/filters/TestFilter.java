package filters;

import gui.TCFilter;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import gui.*;

public class TestFilter extends TCFilter
{
    JLabel gui;
    public TestFilter()
    {
        gui = new JLabel("No settings are requiered... No! it is: NO SETTING THAT YOU CAN DO TO MEEE!!!!!");
    }
    public void filter(BufferedImage src, BufferedImage dest)
    {
        for(int y = 0; y < src.getHeight(); y++)
        {
            for(int x = 0; x < src.getWidth(); x++)
            {
                dest.setRGB(x, y, src.getRGB(x, y) & 0x00FFFF);
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
        return "Blueator!!!! hahahahahaha";
    }
}
