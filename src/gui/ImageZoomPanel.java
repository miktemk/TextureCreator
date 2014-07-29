package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.border.*;

import tools.*;

import java.lang.*;

public class ImageZoomPanel extends JPanel implements MouseListener,
                                                      MouseMotionListener,
                                                      KeyListener,
                                                      Runnable
{
	protected double xstep = 1.0, ystep = 1.0;
	protected double xstep_s = 1.0, ystep_s = 1.0;
    protected double xstep_stmp = 1.0, ystep_stmp = 1.0;
	protected double scale = 1.0, scaleMark = 0.0, scaleinc = 0.05, scaleinctmp = 0.0;
    protected double xtraZoomPow = 20.0;
    int mouseX = 0, mouseY = 0;
	protected double x = 0, y = 0;
    long snapX, snapY;
    double gridXoff = 0.0, gridYoff = 0.0;         //grid offsets due to translation
    double transX = 0, transY = 0;                 //Translation in REAL pixels
    double transXinc = 4, transYinc = 4, transXinctmp = 0, transYinctmp = 0;
    double transXtmp = 0, transYtmp = 0;
    int x1=0, y1=0, x2=0, y2=0;
    public static final int EXPONENTIAL = 0;
    public static final int LINEAR      = 1;
    public static final int NONE        = 2;
    int gridZoomType = NONE;
    int zoomSnapPoint = 2;
    boolean zoomIntoMouse = true;
    boolean animatedZoom = true;
    boolean repaintJobIsFree = true;
    boolean snapToGrid = true;
	StatusBar b = new StatusBar();
    Color cursorColor = Color.red;
    Thread zoomer = new Thread(this);
    int roundPlaces = 4;
    BasicStroke bs1 = new BasicStroke(1.0f);     //1 pixel stoke
    AffineTransform at0 = new AffineTransform(); // nelo aff trans-bounce ;)
    BufferedImage img = null;
    ImageManager manager;
    UndoManager undoMan;
    UndoableEdit newEdit = null;
    QuadSelection q = new QuadSelection();
    SelectionTool tool = null;
	protected void initialize()
	{
        this.xstep_s = xstep;
		this.ystep_s = ystep;
        this.xstep_stmp = xstep;
		this.ystep_stmp = ystep;
        setDoubleBuffered(false);
		addMouseListener(this);
		addMouseMotionListener(this);
        addKeyListener(this);
        this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        setFocusable(true);
        zoomer.start();
	}
	public ImageZoomPanel(ImageManager manager, UndoManager undoMan)
	{
		initialize();
        this.manager = manager;
        this.undoMan = undoMan;
	}
//--------Private Methods-----------------------
    protected void setXY(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
    protected void setXYFromMouse(int MouseX, int MouseY)
	{
        setSnapXYFromMouse(MouseX, MouseY);
        if((gridZoomType == EXPONENTIAL)||(gridZoomType == LINEAR))
        {
            if(snapToGrid)
            {
                this.x = Math.round((MouseX+transX)/xstep_stmp)*xstep_stmp/xstep_s;
                this.y = Math.round((MouseY+transY)/ystep_stmp)*ystep_stmp/ystep_s;
            }
            else
            {
                this.x = (MouseX+transX)/xstep_s;
                this.y = (MouseY+transY)/ystep_s;
            }
        }
        else if(gridZoomType == NONE)
        {
            if(snapToGrid)
            {
                this.x = Math.round((MouseX+transX)/xstep_s);
                this.y = Math.round((MouseY+transY)/xstep_s);
            }
            else
            {
                this.x = (MouseX+transX)/xstep_s;
                this.y = (MouseY+transY)/xstep_s;
            }
        }
	}
    protected void setSnapXYFromMouse(int MouseX, int MouseY)
	{
      if(snapToGrid)
      {
        if((gridZoomType == EXPONENTIAL)||(gridZoomType == LINEAR))
        {
            this.snapX = (int)((MouseX-gridXoff+0.5*xstep_stmp)/xstep_stmp);
            this.snapY = (int)((MouseY-gridYoff+0.5*ystep_stmp)/ystep_stmp);
        }
        else if(gridZoomType == NONE)
        {
            this.snapX = (long)((MouseX-gridXoff)/xstep_s);
            this.snapY = (long)((MouseY-gridYoff)/ystep_s);
        }
      }
      else
      {
          this.snapX = MouseX;
          this.snapY = MouseY;
      }
	}
    protected double sigfig(double a, int decimalPlaces) { return Math.round(Math.pow(10, decimalPlaces)*a)/Math.pow(10, decimalPlaces); }
    protected void setRepaintingEnabled(boolean aflag) { this.repaintJobIsFree = aflag; }
//--------Public Methods------------------------
    public void setGridZoomingPreference(int preference)                  //Grid changing Preferences
    {
        if((preference >= 0)&&(preference <= 2)) this.gridZoomType = preference;
        else                                     this.gridZoomType = EXPONENTIAL;
    }
    public int getGridZoomingPreference() { return gridZoomType; }
    
    public void setDecimals(int roundPlaces) { this.roundPlaces = roundPlaces; }  //Decimal Places for displaying
    public int  getDecimals()                { return roundPlaces; }
    
	public double getCurX(){ return x; }
	public double getCurY(){ return y; }
    
	public void setXstep(double xstep) { this.xstep = xstep; }
	public void setYstep(double ystep) { this.ystep = ystep; }
	public void setXYstep(double xstep, double ystep)
	{
		this.xstep = xstep;
		this.ystep = ystep;
	}
	public double getXstep() { return xstep; }
	public double getYstep() { return ystep; }
    
    public void translateXY(double tX, double tY)
    {
        this.transX = tX;
        this.transY = tY;
        if(transX >= 0) { gridXoff = xstep_stmp - (transX % xstep_stmp); }
        else            { gridXoff = - (transX % xstep_stmp);            }
        if(transY >= 0) { gridYoff = ystep_stmp - (transY % ystep_stmp); }
        else            { gridYoff = - (transY % ystep_stmp);            }
        //System.out.println("gridXoff: "+gridXoff+"         gridYoff: "+gridYoff);
    }
    
	public void setScale(double scale)
	{
		this.scale = scale;
		xstep_s = xstep * scale;
		ystep_s = ystep * scale;
        if(scale > 1)
        {
            if(gridZoomType == EXPONENTIAL)
            {
                xstep_stmp = (xstep_s)/Math.pow(zoomSnapPoint, (int)(Math.log(xstep_s/xstep)/Math.log(zoomSnapPoint)));
                ystep_stmp = (ystep_s)/Math.pow(zoomSnapPoint, (int)(Math.log(ystep_s/ystep)/Math.log(zoomSnapPoint)));
            }
            else if(gridZoomType == LINEAR)
            {
                xstep_stmp = (xstep_s)/((int)(xstep_s/xstep));
                ystep_stmp = (ystep_s)/((int)(ystep_s/ystep));
            }
            else
            {
                xstep_stmp = xstep_s;
                ystep_stmp = ystep_s;
            }
        }
        else if(scale == 1.0)
        {
            xstep_stmp = xstep_s;
            ystep_stmp = ystep_s;
        }
        else
        {
            if((gridZoomType == EXPONENTIAL)||(gridZoomType == NONE))
            {
                xstep_stmp = (xstep_s)*Math.pow(zoomSnapPoint, (int)(Math.log(xstep/xstep_s)/Math.log(zoomSnapPoint))+1);
                ystep_stmp = (ystep_s)*Math.pow(zoomSnapPoint, (int)(Math.log(ystep/ystep_s)/Math.log(zoomSnapPoint))+1);
            }
            else if(gridZoomType == LINEAR)
            {
                xstep_stmp = (xstep_s)*((int)(xstep/xstep_s)+1);
                ystep_stmp = (ystep_s)*((int)(ystep/ystep_s)+1);
            }
        }
	}
	public double getScale(){ return scale; }
    
    public void setZoomSnapPoint(int newPoint)
    {
        if(newPoint > 1) this.zoomSnapPoint = newPoint;
        else             this.zoomSnapPoint = 2;
    }
    public int  getZoomSnapPoint() { return zoomSnapPoint; }
    
    public void setXtraZoomPower(double xtraZoomPow)
    { this.xtraZoomPow = xtraZoomPow; }
    public double getXtraZoomPower()
    { return xtraZoomPow; }
    
    public void setZoomingIntoMouseEnabled(boolean aflag)
    {   this.zoomIntoMouse = aflag;   }
    
    public void setAnimatedZoomingEnabled(boolean aflag)
    {   this.animatedZoom = aflag;   }
    
    public void setSnapToGrid(boolean aflag)
    {   this.snapToGrid = aflag; }
    
    public void setScaleInc(double scaleinc)
    { this.scaleinc = scaleinc;	}
	public double getScaleInc(){ return scaleinc; }
    
    //################################################################
	public void setStatusBar(StatusBar b)
	{
		this.b = b;
	}
    public void setImage(BufferedImage img)
    {
        int lastW = 0;
        int lastH = 0;
        if(this.img != null)
        {
            lastW = this.img.getWidth();
            lastH = this.img.getHeight();
        }
        this.img = img;
        if(img.getWidth() < lastW || img.getHeight() < lastH)
            q.setRect(img.getWidth(), img.getHeight());
        manager.refresh(q);
        repaint();
    }
    public void setCursorColor(Color c)
    {
        cursorColor = c;
        repaint();
    }
    public Color getCursorColor()
    {
        return cursorColor;
    }
    public void setSelectionTool(SelectionTool tool)
    {
        this.tool = tool;
    }
    public void selectAll()
    {
        if(img != null)
        {
            q.setRect(img.getWidth(), img.getHeight());
            manager.refresh(q);
        }
    }
    //################################################################
//--------Paint Grid procedure!!!!!!!!!---------------------------------
    public void paint(Graphics g1)
    {
        Graphics2D g = (Graphics2D)g1;
        g.setColor(Color.white);
        g.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        if(img != null)
        {
            //----------prepare for drawing of onscr objs
            g.translate(-transX, -transY);
            g.scale(xstep_s, ystep_s);
            float strW = (float)(1/(scale*(xstep+ystep)/2));
            g.setStroke(new BasicStroke( strW ));
            g.drawImage(img, 0, 0, this);
            
            //WE DONT NEED DASHING!!!!!!!!!!!!!!
            //g.setStroke(new BasicStroke(strW, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, new float[] {2*strW, 2*strW}, 0.0f));
            g.setColor(cursorColor);
            g.draw(new Line2D.Double(q.x1, q.y1, q.x2, q.y2));
            g.draw(new Line2D.Double(q.x2, q.y2, q.x3, q.y3));
            g.draw(new Line2D.Double(q.x3, q.y3, q.x4, q.y4));
            g.draw(new Line2D.Double(q.x4, q.y4, q.x1, q.y1));
            g.fill(new Ellipse2D.Double(q.x1-1, q.y1-1, 2, 2));
            g.fill(new Ellipse2D.Double(q.x2-1, q.y2-1, 2, 2));
            g.fill(new Ellipse2D.Double(q.x3-1, q.y3-1, 2, 2));
            g.fill(new Ellipse2D.Double(q.x4-1, q.y4-1, 2, 2));
            
            g.setStroke(bs1);
            g.setTransform(at0);
        }
        drawCursor(g);
    }
    public void drawCursor(Graphics g1)
	{
        Graphics2D g = (Graphics2D)g1;
        g.setColor(cursorColor);
        if(snapToGrid)
        {
            if((gridZoomType == EXPONENTIAL)||(gridZoomType == LINEAR))
            {
                //g.draw(new Rectangle2D.Double((snapX*xstep_stmp + gridXoff)-1, (snapY*ystep_stmp + gridYoff)-1, 2, 2));//   this is the way it was
                g.draw(new Rectangle2D.Double((snapX*xstep_stmp + gridXoff)-2, (snapY*ystep_stmp + gridYoff)-2, 4, 4));
            }
            else if(gridZoomType == NONE)
            {
                //g.draw(new Rectangle2D.Double((snapX*xstep_s + gridXoff)-1, (snapY*ystep_s + gridYoff)-1, 2, 2));//         this is the way it was
                g.draw(new Rectangle2D.Double((snapX*xstep_s + gridXoff)-1, (snapY*ystep_s + gridYoff)-1, Math.max(3, xstep_s), Math.max(3, ystep_s)));
            }
        }
        else
        {
            g.draw(new Rectangle2D.Double(snapX-1, snapY-1, 2, 2));
        }
	}
//--------Mouse Listener------------------------
	public void mouseClicked(MouseEvent e)
    {
        //doForMouseClicked();
        repaint();
    }
	public void mouseEntered(MouseEvent e)
    {
        requestFocus();
    }
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e)
    {
        requestFocus();
        mouseX = e.getX();
        mouseY = e.getY();
        setXYFromMouse(mouseX, mouseY);
        x1 = (int)Math.round(x);
        y1 = (int)Math.round(y);
        newEdit = new PointMoveEdit(q);
		b.setXY(sigfig(x, roundPlaces), sigfig(y, roundPlaces));
        if(tool != null)
        {
            tool.pressed(q, x, y);
            if(img != null)
            {
                q.bound(img.getWidth(), img.getHeight());
                manager.refresh(q);
            }
        }
        repaint();
    }
	public void mouseReleased(MouseEvent e)
    {
        mouseX = e.getX();
        mouseY = e.getY();
        setXYFromMouse(mouseX, mouseY);
        x2 = (int)Math.round(x);
        y2 = (int)Math.round(y);
        if(x1 != x2 || y1 != y2)
            undoMan.addEdit(newEdit);
        repaint();
    }
//--------Mouse Motion Listener-----------------
	public void mouseMoved(MouseEvent e)
	{
        mouseX = e.getX();
        mouseY = e.getY();
        setXYFromMouse(mouseX, mouseY);
		b.setXY(sigfig(x, roundPlaces), sigfig(y, roundPlaces));
        repaint();
	}
	public void mouseDragged(MouseEvent e)
    {
        double prevX = x;
        double prevY = y;
        mouseX = e.getX();
        mouseY = e.getY();
        setXYFromMouse(mouseX, mouseY);
		b.setXY(sigfig(x, roundPlaces), sigfig(y, roundPlaces));
        if(tool != null)
        {
            tool.moved(q, prevX, prevY, x, y);
            if(img != null)
            {
                q.bound(img.getWidth(), img.getHeight());
                manager.refresh(q);
            }
        }
        repaint();
    }
//--------Key Listener--------------------------
    public void keyPressed(KeyEvent e)
    {
        if(!e.isShiftDown() && !e.isControlDown() && (img != null))
        {
            if(e.getKeyCode() == KeyEvent.VK_X)
            {
                if(animatedZoom) { scaleinctmp = scaleinc; }
                else
                {
                    if(zoomIntoMouse)
                    {
                        transXtmp = (mouseX + transX)/xstep_s;
                        transYtmp = (mouseY + transY)/ystep_s;
                    }
                    scaleMark += xtraZoomPow*scaleinc;
                    scale = Math.pow(zoomSnapPoint, scaleMark);
                    setScale(scale);
                    if(zoomIntoMouse)
                    {
                        translateXY(xstep_s*transXtmp - mouseX, ystep_s*transYtmp - mouseY);
                    }
                    setXYFromMouse(mouseX, mouseY);
                    b.setXY(sigfig(x, roundPlaces), sigfig(y, roundPlaces));
                    repaint();
                    //b.setMessage("Scale: "+scale);
                }
            }
            else if(e.getKeyCode() == KeyEvent.VK_Z)
            {
                if(animatedZoom) { scaleinctmp = -scaleinc; }
                else
                {
                    if(zoomIntoMouse)
                    {
                        transXtmp = (mouseX + transX)/xstep_s;
                        transYtmp = (mouseY + transY)/ystep_s;
                    }
                    scaleMark -= xtraZoomPow*scaleinc;
                    scale = Math.pow(zoomSnapPoint, scaleMark);
                    setScale(scale);
                    if(zoomIntoMouse)
                    {
                        translateXY(xstep_s*transXtmp - mouseX, ystep_s*transYtmp - mouseY);
                    }
                    setXYFromMouse(mouseX, mouseY);
                    b.setXY(sigfig(x, roundPlaces), sigfig(y, roundPlaces));
                    repaint();
                    //b.setMessage("Scale: "+scale);
                }
            }
            else if(e.getKeyCode() == KeyEvent.VK_A) { transXinctmp = -transXinc; }
            else if(e.getKeyCode() == KeyEvent.VK_D) { transXinctmp = transXinc;  }
            else if(e.getKeyCode() == KeyEvent.VK_S) { transYinctmp = transYinc;  }
            else if(e.getKeyCode() == KeyEvent.VK_W) { transYinctmp = -transYinc; }
            else if(e.getKeyCode() == KeyEvent.VK_O)
            {
                this.setScale(1.0);
                this.translateXY(0.0, 0.0);
                scaleMark = 0.0;
                repaint();
                //b.setMessage("Scale: "+scale);
            }
        }
    }
    public void keyReleased(KeyEvent e)
    {
             if(e.getKeyCode() == KeyEvent.VK_X) { scaleinctmp = 0; }
        else if(e.getKeyCode() == KeyEvent.VK_Z) { scaleinctmp = 0; }
        else if(e.getKeyCode() == KeyEvent.VK_A) { transXinctmp = 0; }
        else if(e.getKeyCode() == KeyEvent.VK_D) { transXinctmp = 0; }
        else if(e.getKeyCode() == KeyEvent.VK_S) { transYinctmp = 0; }
        else if(e.getKeyCode() == KeyEvent.VK_W) { transYinctmp = 0; }
        //gpr.setRepaintWhole();
        repaint();
    }
    public void keyTyped(KeyEvent e) {}
//--------Runnable------------------------------
    public void run()
    {
        while(true)
        {
            if((scaleinctmp != 0.0)&&(animatedZoom))
            {
                if(zoomIntoMouse)
                {
                    transXtmp = (mouseX + transX)/xstep_s;
                    transYtmp = (mouseY + transY)/ystep_s;
                }
                scaleMark += scaleinctmp;
                scale = Math.pow(zoomSnapPoint, scaleMark);
                setScale(scale);
                if(zoomIntoMouse)
                {
                    translateXY(xstep_s*transXtmp - mouseX, ystep_s*transYtmp - mouseY);
                }
                setXYFromMouse(mouseX, mouseY);
                b.setXY(sigfig(x, roundPlaces), sigfig(y, roundPlaces));
                //b.setMessage("Scale: "+scale);
                //gpr.setRepaintWhole();
            }
            if(transXinctmp != 0)
            {
                translateXY(transX + transXinctmp, transY);
                setXYFromMouse(mouseX, mouseY);
                b.setXY(sigfig(x, roundPlaces), sigfig(y, roundPlaces));
                //gpr.setRepaintWhole();
            }
            if(transYinctmp != 0)
            {
                translateXY(transX, transY + transYinctmp);
                setXYFromMouse(mouseX, mouseY);
                b.setXY(sigfig(x, roundPlaces), sigfig(y, roundPlaces));
                //gpr.setRepaintWhole();
            }
            if( (((scaleinctmp != 0.0)&&(animatedZoom))||(transXinctmp != 0)||(transYinctmp != 0))&&repaintJobIsFree )
            {
                repaint();
            }
            try { zoomer.sleep(10); } catch(InterruptedException e){}
       }
    }
    /*public abstract void doForMouseClicked();
    public abstract void doForMouseEntered();
    public abstract void doForMouseExited();
    public abstract void doForMousePressed();
    public abstract void doForMouseReleased();
    public abstract void doForMouseMoved();
    public abstract void doForMouseDragged();//*/
}
