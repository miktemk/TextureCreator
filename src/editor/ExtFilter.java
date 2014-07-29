package editor;

import java.io.*;
import javax.swing.filechooser.*;

public class ExtFilter extends javax.swing.filechooser.FileFilter
{
    String[] ext;
    String descr;
    public ExtFilter(String exts, String descr)
    {
        ext = (exts+":").split(":");
        this.descr = descr;
    }
    public boolean accept(File pathname)
    {
        if(pathname.isDirectory()) return true;
        else
        {
            for(int i = 0; i < ext.length; i++)
            {
                if(pathname.getName().indexOf("."+ext[i]) != -1)
                    return true;
            }
            return false;
        }
    }
    public String getDescription()
    {
        return "(*."+ext[0]+") "+descr;
    }
    public String getBasicExt()
    {
        return ext[0];
    }
}
