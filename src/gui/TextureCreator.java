package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.border.*;
import java.io.*;
import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;

import editor.ExtFilter;
import editor.*;

public class TextureCreator extends JFrame
{
    public static final int EXIT_OK = 0,
                            DONT_EXIT = 1;
    OriginalHalf oh;
    FilterPanel filts;
    TexMenuBar menu;
    JFileChooser fc;
    ImageManager man;
    UndoManager undoMan;
    File currentFile = null;
    String ext = "jpg";
    public TextureCreator()
    {
        super("Texture Creator");
        
        Container pane = getContentPane();
        pane.setLayout(new GridLayout(1, 2));
        
        ResultDisplay disp = new ResultDisplay();
        man = new ImageManager(disp);
        undoMan = new UndoManager();
        oh = new OriginalHalf(man, undoMan);
        filts = new FilterPanel(man);
        DimensionSetter dimset = new DimensionSetter(man);
        
        JPanel left = new JPanel(new BorderLayout());
        JPanel right = new JPanel(new BorderLayout());
        
        left.add(oh, BorderLayout.CENTER);
        left.add(filts, BorderLayout.SOUTH);
        right.add(disp, BorderLayout.CENTER);
        right.add(dimset, BorderLayout.NORTH);
        
        pane.add(left);
        pane.add(right);
        
        menu = new TexMenuBar(this);
        setJMenuBar(menu);
        
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                exitProgram();
            }
        });
        addComponentListener(new ComponentAdapter()
        {
            public void componentHidden(ComponentEvent e)
            {
                if(!man.isSaved())
                {
                    setVisible(true);
                }
            }
        });
        
        setSize(1000, 600);
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
        
        fc = new JFileChooser();
        fc.addChoosableFileFilter(new ExtFilter("jpg:jpeg:JPG:JPEG", "JPEG Images"));
        fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
        
        //setImage(Utils.loadImage("cracks1.jpg"));
    }
    public void setImage(BufferedImage bi)
    {
        man.setSrcImage(bi);
        oh.setImage(bi);
        currentFile = null;
        filts.enable();
    }
    public UndoManager getUndoManager()
    {
        return undoMan;
    }
    /** This checks for fileExists/not
      * @return an JFileChooser option*/
    public int getSaveFileOption()
    {
        int option = fc.showSaveDialog(this);
        while(option == JFileChooser.APPROVE_OPTION)
        {
            File target = fc.getSelectedFile();
            if(!target.exists()) return option;
            int subOpt = JOptionPane.showConfirmDialog(fc, new JLabel(target.getName()+" exists. Overwrite?"), "File Exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(subOpt == JOptionPane.YES_OPTION) return JFileChooser.APPROVE_OPTION;
            option = fc.showSaveDialog(this);
        }
        return option;
    }
    public int save(boolean saveAs)
    {
        if(currentFile == null || saveAs)
        {
            int option = getSaveFileOption();
            if(option == JFileChooser.APPROVE_OPTION)
            {
                File tmp = fc.getSelectedFile();
                ExtFilter filt = (ExtFilter)fc.getFileFilter();
                ext = filt.getBasicExt();
                if(tmp.getPath().indexOf(".") != -1)
                {
                    currentFile = tmp;
                }
                else
                {
                    currentFile = new File(tmp.getPath()+"."+ext);
                }
                try
                {
                	saveCurImageFile();
                    return EXIT_OK;
                }
                catch(Exception ex)
                {
                    currentFile = null;
                    JOptionPane.showMessageDialog(this, new JLabel("Exception: "+ex.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
                    return DONT_EXIT;
                }
            }
            else
            {
                return DONT_EXIT;
            }
        }
        else
        {
            try
            {
            	saveCurImageFile();
                return EXIT_OK;
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(this, new JLabel("Exception: "+ex.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
                return DONT_EXIT;
            }
        }
    }
    
    private void saveCurImageFile() throws IOException
    {
    	ImageIO.write(man.getImageToSave(), ext, currentFile);
    	
    	// LINK: https://stackoverflow.com/questions/17015197/quality-loss-using-imageio-write
    	ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
    	ImageWriteParam param = writer.getDefaultWriteParam();
    	param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // Needed see javadoc
    	param.setCompressionQuality(1.0F); // Highest quality
    	ImageOutputStream ioStream = ImageIO.createImageOutputStream(currentFile);
    	writer.setOutput(ioStream);
    	writer.write(man.getImageToSave());
	}
    
	public void exitProgram()
    {
        if(!man.isSaved())
        {
            int option = JOptionPane.showConfirmDialog(this, new JLabel("Save Result Image?"), "Will you save?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(option == JOptionPane.YES_OPTION)
            {
                //save...
                option = save(false);
                if(option == EXIT_OK)
                    System.exit(0);
            }
            else if(option == JOptionPane.NO_OPTION)
            {
                System.exit(0);
            }
        }
        else
        {
            System.exit(0);
        }
    }
    public static void main(String[] args)
    {
        new TextureCreator();
    }
}
