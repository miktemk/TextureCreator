package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.border.*;

public class OriginalHalf extends JPanel
{
    ImageZoomPanel izp;
    StatusBar sb;
    public OriginalHalf(ImageManager manager, UndoManager undoMan)
    {
        super(new BorderLayout());
        sb = new StatusBar();
        izp = new ImageZoomPanel(manager, undoMan);
        izp.setStatusBar(sb);
        SelectionToolBar stb = new SelectionToolBar(izp);
        
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        
        add(izp, BorderLayout.CENTER);
        add(sb, BorderLayout.SOUTH);
        add(stb, BorderLayout.WEST);
    }
    public void setImage(BufferedImage bi)
    {
        izp.setImage(bi);
        sb.setMessage("Original Dimensions: "+bi.getWidth()+"x"+bi.getHeight());
    }
    public void callSelectAll()
    {
        izp.selectAll();
    }
    public void callRepaint()
    {
        izp.repaint();
    }
}
