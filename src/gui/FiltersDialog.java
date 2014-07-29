package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import editor.*;
import filters.LightenEdges;
import filters.SmoothenTransitions;
import filters.TestFilter;

public class FiltersDialog extends JPanel
{
    JComboBox<String> cb;
    public FiltersDialog()
    {
        super(new BorderLayout());
        //Object[] filters = Util.loadPluginNames("filters", "gui.TCFilter", false); // doesn't work. cannot load from .class files
//        TCFilter[] filters = new TCFilter[] {
//        		new LightenEdges(),
//        		new SmoothenTransitions(),
//        		new TestFilter()
//        };
        String[] filters = new String[] {
        		"LightenEdges",
        		"SmoothenTransitions",
        		"TestFilter"
        };
        cb = new JComboBox<String>(filters);
        
        add(new JLabel("Select a filter:"), BorderLayout.NORTH);
        add(cb, BorderLayout.CENTER);
    }
    public TCFilter getNewFilter()
    {
        int option = JOptionPane.showConfirmDialog(this, this, "Add Filter...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(option == JOptionPane.OK_OPTION)
        {
            String name = (String)cb.getSelectedItem();
            try
            {
                Class c = Class.forName("filters."+name);
                TCFilter filter = (TCFilter)c.newInstance();
                return filter;
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(this, new JLabel("Exception: "+ex.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }
}
