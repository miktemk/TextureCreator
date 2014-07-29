package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;

public class FilterPanel extends JPanel
{
    FilterList preList, postList;
    public FilterPanel(ImageManager man)
    {
        super(new GridLayout(1, 2));
        
        FiltersDialog dialog = new FiltersDialog();
        FilterSetter setter = new FilterSetter(man);
        
        preList = new FilterList("Pre-Filters", man, dialog, setter);
        postList = new FilterList("Post-Filters", man, dialog, setter);
        
        man.setFilters(preList.getFilters(), postList.getFilters());
        add(preList);
        add(postList);
    }
    public void enable()
    {
        preList.enable();
        postList.enable();
    }
}
