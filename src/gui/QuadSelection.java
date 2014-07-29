package gui;

public class QuadSelection
{
    public int x1, y1;
    public int x2, y2;
    public int x3, y3;
    public int x4, y4;
    public QuadSelection()
    {
        
    }
    public void bound(int w, int h)
    {
             if(x1 < 0) x1 = 0;
        else if(x1 >= w) x1 = w-1;
             if(y1 < 0) y1 = 0;
        else if(y1 >= h) y1 = h-1;
             if(x2 < 0) x2 = 0;
        else if(x2 >= w) x2 = w-1;
             if(y2 < 0) y2 = 0;
        else if(y2 >= h) y2 = h-1;
             if(x3 < 0) x3 = 0;
        else if(x3 >= w) x3 = w-1;
             if(y3 < 0) y3 = 0;
        else if(y3 >= h) y3 = h-1;
             if(x4 < 0) x4 = 0;
        else if(x4 >= w) x4 = w-1;
             if(y4 < 0) y4 = 0;
        else if(y4 >= h) y4 = h-1;
    }
    public void setQuad(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.x4 = x4;
        this.y4 = y4;
    }
    public void zeroate()
    {
        x1 = x2 = x3 = x4 = y1 = y2 = y3 = y4 = 0;
    }
    public void setRect(int w, int h)
    {
        x1 = x4 = y1 = y2 = 0;
        x2 = x3 = w-1;
        y3 = y4 = h-1;
    }
}
