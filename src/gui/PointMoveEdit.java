package gui;

import javax.swing.undo.*;

public class PointMoveEdit implements UndoableEdit
{
    int x1, y1, x2, y2, x3, y3, x4, y4;
    QuadSelection q;
    public PointMoveEdit(QuadSelection q)
    {
        this.x1 = q.x1;
        this.y1 = q.y1;
        this.x2 = q.x2;
        this.y2 = q.y2;
        this.x3 = q.x3;
        this.y3 = q.y3;
        this.x4 = q.x4;
        this.y4 = q.y4;
        this.q = q;
    }
    public void undo()
    {
        int x_1 = q.x1;  int y_1 = q.y1;
        int x_2 = q.x2;  int y_2 = q.y2;
        int x_3 = q.x3;  int y_3 = q.y3;
        int x_4 = q.x4;  int y_4 = q.y4;
        q.setQuad(x1, y1, x2, y2, x3, y3, x4, y4);
        x1 = x_1;  y1 = y_1;
        x2 = x_2;  y2 = y_2;
        x3 = x_3;  y3 = y_3;
        x4 = x_4;  y4 = y_4;
    }
    public void redo()
    {
        int x_1 = q.x1;  int y_1 = q.y1;
        int x_2 = q.x2;  int y_2 = q.y2;
        int x_3 = q.x3;  int y_3 = q.y3;
        int x_4 = q.x4;  int y_4 = q.y4;
        q.setQuad(x1, y1, x2, y2, x3, y3, x4, y4);
        x1 = x_1;  y1 = y_1;
        x2 = x_2;  y2 = y_2;
        x3 = x_3;  y3 = y_3;
        x4 = x_4;  y4 = y_4;
    }
    public boolean addEdit(UndoableEdit anEdit) { return false; }
    public boolean canRedo() { return true; }
    public boolean canUndo() { return true; }
    public void die() {}
    public String getPresentationName()     { return ""; }
    public String getRedoPresentationName() { return ""; }
    public String getUndoPresentationName() { return ""; }
    public boolean isSignificant() { return true; }
    public boolean replaceEdit(UndoableEdit anEdit) { return false; }
}
