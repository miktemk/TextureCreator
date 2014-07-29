package gui;

import java.awt.*;
import java.awt.image.*;

/**
 * @author Mikhail Temkine
 * The basis of all filters. Any filter must extend this class,
 * implement all the abstract methods and be located in the
 * filters package.
 */
public abstract class TCFilter
{
    protected ImageDisplay id = null;
    protected BufferedImage src = null;
    protected BufferedImage dest = null;
    /**
     * Performs the filtering on a given source (src) image,
     * writing the result into the destination (dest) image.
     * src and dest are of equal size.
     * @param src the source image
     * @param dest the destination image
     */
    public abstract void filter(BufferedImage src, BufferedImage dest);
    /**
     * Returns the gui component for this filter.
     * Normally, such gui is initialized in the filter constructor.
     * @return the gui component for this filter
     */
    public abstract Component getGUI();
    /**
     * Rollback any changes made to whatever-gui-has-control-over
     * eversince getSettingGUI() was last time called.
     * This function is called if the user presses Cancel instead
     * of OK in the filter settings dialog.
     */
    public abstract void reset();
    /**
     * This function is called by the filter dialog window whenever
     * settings are asked to be brought up.
     * Children extending this class should not call this method.
     * @param src source image
     * @param dest destination-to-be image
     * @param id reference image displace (Don't concern yourself with this)
     * @return the gui component having set references to src and dest internally.
     */
    public Component getSettingGUI(BufferedImage src, BufferedImage dest, ImageDisplay id)
    {
        this.src = src;
        this.dest = dest;
        this.id = id;
        return getGUI();
    }
    /**
     * Updates the image display.
     * Children extending this class should not call this method.
     */
    public void update()
    {
        if(id != null && src != null && dest != null)
        {
            filter(src, dest);
            id.setImage(dest);
        }
    }
    /**
     * Returns the red component of the given GRB color
     * @param rgb RGB color represented all as one integer.
     * @return the red component of the given GRB color
     */
    protected int r(int rgb)
    {
        return (rgb & 0xFF0000)/65536;
    }
    /**
     * Returns the green component of the given GRB color
     * @param rgb RGB color represented all as one integer.
     * @return the green component of the given GRB color
     */
    protected int g(int rgb)
    {
        return (rgb & 0x00FF00)/256;
    }
    /**
     * Returns the blue component of the given GRB color
     * @param rgb RGB color represented all as one integer.
     * @return the blue component of the given GRB color
     */
    protected int b(int rgb)
    {
        return rgb & 0x0000FF;
    }
    /**
     * Returns the average of the 3 Red, Green, Blue components
     * (i.e. the grayscale)
     * @param r red component (out of 255)
     * @param g green component (out of 255)
     * @param b blue component (out of 255)
     * @return the average of the 3 R, G, B components
     * (i.e. the grayscale)
     */
    protected int av(int r, int g, int b)
    {
        return (r+g+b)/3;
    }
}
