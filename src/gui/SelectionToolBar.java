package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import tools.CorrectionTool;
import tools.RectTool;

public class SelectionToolBar extends JToolBar implements ActionListener
{
    private class ToolComparator implements Comparator<SelectionTool>
    {
        public int compare(SelectionTool o1, SelectionTool o2)
        {
            SelectionTool n1 = (SelectionTool)o1;
            SelectionTool n2 = (SelectionTool)o2;
                 if(n1.getOrder() <  n2.getOrder()) return -1;
            else if(n1.getOrder() == n2.getOrder()) return 0;
            else                                            return 1;
        }
        public boolean equals(Object obj)
        {
            return false;
        }
    }
    private class SelButton extends JToggleButton
    {
        SelectionTool st;
        public SelButton(SelectionTool st, String iconName)
        {
            super(new ImageIcon(iconName));
            this.st = st;
        }
        
    }
    ImageZoomPanel izp;
    JButton col;
    public SelectionToolBar(ImageZoomPanel izp)
    {
        super(JToolBar.VERTICAL);
        this.izp = izp;
        ButtonGroup bg = new ButtonGroup();
        //Object[] tools = Util.loadPlugins("tools", "gui.SelectionTool", true); // doesn't work any more, we cannot load classes
        SelectionTool[] tools = new SelectionTool[] {
        		new RectTool(),
        		new CorrectionTool()
        };
        Arrays.sort(tools, new ToolComparator());
        SelButton first = null;
        for(int i = 0; i < tools.length; i++)
        {
            SelectionTool st = tools[i];
            String toBsplit = st.getClass().getName().replace(".", ":");
            String[] packPath = toBsplit.split(":");
            String iconName = packPath[packPath.length-1];
            SelButton but = new SelButton(st, "img/"+iconName+".gif");
            but.addActionListener(this);
            add(but);
            bg.add(but);
            if(first == null)
                first = but;
        }
        first.setSelected(true);
        izp.setSelectionTool(first.st);
        
        col = new JButton("Color");
        col.addActionListener(this);
        add(col);
    }
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if(source instanceof SelButton)
        {
            SelButton b = (SelButton)source;
            izp.setSelectionTool(b.st);
        }
        else if(source == col)
        {
            Color newColor = JColorChooser.showDialog(this, "Selectoion Lines Color", izp.getCursorColor());
            if(newColor != null) izp.setCursorColor(newColor);
        }
    }
}
