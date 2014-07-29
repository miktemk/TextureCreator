package gui;

public abstract class SelectionTool
{
    public abstract void moved(QuadSelection q, double x1, double y1, double x2, double y2);
    public abstract void pressed(QuadSelection q, double x, double y);
    public abstract int getOrder();
}
