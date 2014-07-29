package tools;

import gui.QuadSelection;
import gui.SelectionTool;
import gui.*;

public class CorrectionTool extends SelectionTool
{
    int n = 1;
    public void moved(QuadSelection q, double x1, double y1, double x2, double y2)
    {
        int dx = (int)Math.round(x2-x1);
        int dy = (int)Math.round(y2-y1);
        if(n == 1)
        {
            q.x1 += dx;
            q.y1 += dy;
        }
        else if(n == 2)
        {
            q.x2 += dx;
            q.y2 += dy;
        }
        else if(n == 3)
        {
            q.x3 += dx;
            q.y3 += dy;
        }
        else if(n == 4)
        {
            q.x4 += dx;
            q.y4 += dy;
        }
    }
    public void pressed(QuadSelection q, double x, double y)
    {
        double d1 = dist(x, y, q.x1, q.y1);
        double d2 = dist(x, y, q.x2, q.y2);
        double d3 = dist(x, y, q.x3, q.y3);
        double d4 = dist(x, y, q.x4, q.y4);
        n = 1;
        double dmin = d1;
        if(d2 < dmin)
        {
            dmin = d2;
            n = 2;
        }
        if(d3 < dmin)
        {
            dmin = d3;
            n = 3;
        }
        if(d4 < dmin)
        {
            dmin = d4;
            n = 4;
        }
    }
    public int getOrder()
    {
        return 2;
    }
    private double dist(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }
}
