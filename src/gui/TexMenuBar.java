package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.border.*;

import gui.AboutDialog;

import editor.*;

public class TexMenuBar extends JMenuBar implements ActionListener
{
    TextureCreator tc;
    JFileChooser fc;
    AboutDialog abouts;
    public TexMenuBar(TextureCreator tc)
    {
        this.tc = tc;
        JMenu filemenu = new JMenu("File");
        filemenu.add(getMenuItem("Open Source Image...", KeyEvent.VK_O));
        filemenu.add(getMenuItem("Save Result", KeyEvent.VK_S));
        filemenu.add(getMenuItem("Save As...", KeyEvent.VK_S));
        filemenu.addSeparator();
        filemenu.add(getMenuItem("Exit", KeyEvent.VK_Q));
        add(filemenu);
        
        JMenu editmenu = new JMenu("Edit");
        editmenu.add(getMenuItem("Undo", KeyEvent.VK_Z));
        editmenu.add(getMenuItem("Redo", KeyEvent.VK_Y));
        editmenu.addSeparator();
        editmenu.add(getMenuItem("Select All", KeyEvent.VK_A));
        add(editmenu);
        
        JMenu helpmenu = new JMenu("Help");
        helpmenu.add(getMenuItem("About", KeyEvent.VK_F1));
        add(helpmenu);
        
        fc = new JFileChooser();
        fc.addChoosableFileFilter(new ExtFilter("gif:GIF", "Graphics Interchange Format"));
        fc.addChoosableFileFilter(new ExtFilter("jpg:jpeg:JPG:JPEG", "JPEG Images"));
        
        abouts = new AboutDialog();
    }
    private JMenuItem getMenuItem(String name)
    {
        JMenuItem mi = new JMenuItem(name);
        mi.addActionListener(this);
        return mi;
    }
    private JMenuItem getMenuItem(String name, int key)
    {
        JMenuItem mi = new JMenuItem(name);
        mi.addActionListener(this);
        mi.setAccelerator(KeyStroke.getKeyStroke(key, InputEvent.CTRL_MASK));
        return mi;
    }
    
    public JFileChooser getFileChooser()
    {
        return fc;
    }
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        JMenuItem item = (JMenuItem)source;
        if(item.getText() == "Open Source Image...")
        {
            int option = fc.showOpenDialog(this);
            if(option == JFileChooser.APPROVE_OPTION)
            {
                String filename = fc.getSelectedFile().getPath();
                BufferedImage newBI = Utils.loadImage(filename);
                tc.setImage(newBI);
            }
        }
        else if(item.getText() == "Save Result")
        {
            tc.save(false);
        }
        else if(item.getText() == "Save As...")
        {
            tc.save(true);
        }
        else if(item.getText() == "Exit")
        {
            tc.exitProgram();
        }
        else if(item.getText() == "Undo")
        {
            UndoManager und = tc.getUndoManager();
            try
            {
                und.undo();
            }
            catch(Exception ex){}
            tc.oh.callRepaint();
            tc.man.refreshJustQuad();
        }
        else if(item.getText() == "Redo")
        {
            UndoManager und = tc.getUndoManager();
            try
            {
                und.redo();
            }
            catch(Exception ex){}
            tc.oh.callRepaint();
            tc.man.refreshJustQuad();
        }
        else if(item.getText() == "Select All")
        {
            tc.oh.callSelectAll();
        }
        else if(item.getText() == "About")
        {
            abouts.popUp();
        }
    }
}
