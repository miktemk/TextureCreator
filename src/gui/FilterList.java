package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;

public class FilterList extends JPanel implements ActionListener, ListSelectionListener
{
    Vector filters;
    JList list;
    JButton newButton, delButton, up, down, settings;
    FiltersDialog dialog;
    FilterSetter setter;
    ImageManager man;
    public FilterList(String name, ImageManager man, FiltersDialog dialog, FilterSetter setter)
    {
        super(new BorderLayout());
        this.man = man;
        this.dialog = dialog;
        this.setter = setter;
        filters = new Vector();
        list = new JList(filters);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        
        delButton = new JButton("Delete");
        delButton.addActionListener(this);
        delButton.setEnabled(getIndex() != -1);
        
        newButton = new JButton("Add...");
        newButton.addActionListener(this);
        newButton.setEnabled(false);
        
        up = new JButton("UP");
        up.addActionListener(this);
        up.setEnabled(getIndex() != -1);
        down = new JButton("DOWN");
        down.addActionListener(this);
        down.setEnabled(getIndex() != -1);
        
        settings = new JButton("Settings");
        settings.addActionListener(this);
        settings.setEnabled(getIndex() != -1);
        
        JPanel top = new JPanel(new FlowLayout());
        top.add(newButton);
        top.add(delButton);
        top.add(settings);
        
        JPanel bot = new JPanel(new FlowLayout());
        bot.add(up);
        bot.add(down);
        
        add(new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);
        add(bot, BorderLayout.SOUTH);
        
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), name));
        /*
        filters.add("BOO!!");
        filters.add("BOO2!!");
        filters.add("BOO3!!");
        filters.add("BOO4!!");
        filters.add("BOO5!!");//*/
    }
    public Vector getFilters()
    {
        return filters;
    }
    private int getIndex()
    {
        return list.getSelectedIndex();
    }
    public void enable()
    {
        newButton.setEnabled(true);
    }
    //####################################
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if(source == delButton)
        {
            int option = JOptionPane.showConfirmDialog(this, new JLabel("Are you sure?"), "Delete?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(option == JOptionPane.YES_OPTION)
            {
                filters.removeElementAt(getIndex());
                list.updateUI();
                boolean en = (getIndex() < filters.size() && getIndex() >= 0);
                delButton.setEnabled(en);
                up.setEnabled(en);
                down.setEnabled(en);
                settings.setEnabled(en);
                man.refresh();
            }
        }
        else if(source == up)
        {
            int i = getIndex();
            if(i > 0)
            {
                Object o = filters.elementAt(i);
                filters.removeElementAt(i);
                filters.insertElementAt(o, i-1);
                list.setSelectedIndex(i-1);
                man.refresh();
            }
        }
        else if(source == down)
        {
            int i = getIndex();
            if(i < filters.size()-1)
            {
                Object o = filters.elementAt(i);
                filters.removeElementAt(i);
                filters.insertElementAt(o, i+1);
                list.setSelectedIndex(i+1);
                man.refresh();
            }
        }
        else if(source == newButton)
        {
            int i = getIndex();
            TCFilter filt = dialog.getNewFilter();
            if(filt != null)
            {
                filters.addElement(filt);
                list.updateUI();
                boolean en = (getIndex() < filters.size() && getIndex() >= 0);
                delButton.setEnabled(en);
                up.setEnabled(en);
                down.setEnabled(en);
                settings.setEnabled(en);
                man.refresh();
            }
        }
        else if(source == settings)
        {
            TCFilter filt = (TCFilter)list.getSelectedValue();
            if(filt != null)
            {
                setter.set(filt);
            }
        }
    }
    public void valueChanged(ListSelectionEvent e)
    {
        boolean en = (getIndex() != -1);
        delButton.setEnabled(en);
        up.setEnabled(en);
        down.setEnabled(en);
        settings.setEnabled(en);
        //TCFilter filt = (TCFilter)list.getSelectedValue();
       // if(filt != null)
        {
            
        }
    }
    //####################################
}
