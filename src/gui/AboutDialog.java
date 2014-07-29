package gui;

import javax.swing.*;

public class AboutDialog extends JPanel
{
    public AboutDialog()
    {
        add(new JLabel("<html><center><font size=5 align=CENTER>Texture Creator v1.2 - <font color=\"#FF8800\">September 2006 Edition</font><br>Creator created by: Mikhail Temkine<br>Have a LOT of FUN!</font></center></html>"));
    }
    public void popUp()
    {
        JOptionPane.showMessageDialog(null, this, "About Texture Creator", JOptionPane.PLAIN_MESSAGE);
    }
    //public static void main(String[] args)
    //{ new AboutDialog(null).popUp(); }
}
