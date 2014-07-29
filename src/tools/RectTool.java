package tools;

import gui.QuadSelection;
import gui.SelectionTool;
import gui.*;

public class RectTool extends SelectionTool
{
    int x_1, y_1;
    public void moved(QuadSelection q, double x1, double y1, double x2, double y2)
    {
        q.x1 = (int)Math.round(Math.min(x_1, x2));    q.y1 = (int)Math.round(Math.min(y_1, y2));
        q.x2 = (int)Math.round(Math.max(x_1, x2));    q.y2 = (int)Math.round(Math.min(y_1, y2));
        q.x3 = (int)Math.round(Math.max(x_1, x2));    q.y3 = (int)Math.round(Math.max(y_1, y2));
        q.x4 = (int)Math.round(Math.min(x_1, x2));    q.y4 = (int)Math.round(Math.max(y_1, y2));
    }
    public void pressed(QuadSelection q, double x, double y)
    {
        q.x1 = (int)Math.round(x);    q.y1 = (int)Math.round(y);
        q.x2 = (int)Math.round(x);    q.y2 = (int)Math.round(y);
        q.x3 = (int)Math.round(x);    q.y3 = (int)Math.round(y);
        q.x4 = (int)Math.round(x);    q.y4 = (int)Math.round(y);
        x_1 = q.x1;
        y_1 = q.y1;
    }
    public int getOrder()
    {
        return 1;
    }
}
