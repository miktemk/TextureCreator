package gui;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

public class ImageManager
{
    ResultDisplay disp;
    BufferedImage src;
    BufferedImage dest;
    BufferedImage tmp1, tmp2, tmp3, tmp4;
    BufferedImage preImg = null;
    QuadSelection lastQ = null;
    Vector prefilts = null,
           postfilts = null;
    boolean saved = true;
    public ImageManager(ResultDisplay disp)
    {
        this.disp = disp;
        dest = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
    }
    public void setSrcImage(BufferedImage src)
    {
        this.src = src;
        lastQ = null;
        refresh();
    }
    public void setFilters(Vector prefilters, Vector postfilters)
    {
        prefilts = prefilters;
        postfilts = postfilters;
    }
    public void setDim(int w, int h)
    {
        if(w != dest.getWidth() || h != dest.getHeight())
        {
            dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            refresh();
        }
        saved = false;
    }
    /**This method should be called by the Image Zoom Panel*/
    public void refresh(QuadSelection q)
    {
        lastQ = q;
        if(preImg != null)
            Utils.skew(preImg, dest, q.x1, q.y1, q.x2, q.y2, q.x3, q.y3, q.x4, q.y4);
        else
            Utils.skew(src, dest, q.x1, q.y1, q.x2, q.y2, q.x3, q.y3, q.x4, q.y4);
        if(postfilts != null)
        {
            BufferedImage post = postfilterImage(dest, postfilts);
            disp.setImage(post);
            //Utils.copyImage(post, dest);
        }
        else
            disp.setImage(dest);
        saved = false;
    }
    /**This method should be called by the Undo Management*/
    public void refreshJustQuad()
    {
        if(lastQ != null)
        {
            if(preImg != null)
                Utils.skew(preImg, dest, lastQ.x1, lastQ.y1, lastQ.x2, lastQ.y2, lastQ.x3, lastQ.y3, lastQ.x4, lastQ.y4);
            else
                Utils.skew(src, dest, lastQ.x1, lastQ.y1, lastQ.x2, lastQ.y2, lastQ.x3, lastQ.y3, lastQ.x4, lastQ.y4);
        }
        else
        {
            if(preImg != null)
                Utils.skew(preImg, dest, 0, 0, preImg.getWidth()-1, 0, preImg.getWidth()-1, preImg.getHeight()-1, 0, preImg.getHeight()-1);
            else
                Utils.skew(src, dest, 0, 0, preImg.getWidth()-1, 0, preImg.getWidth()-1, preImg.getHeight()-1, 0, preImg.getHeight()-1);
        }
        if(postfilts != null)
        {
            BufferedImage post = postfilterImage(dest, postfilts);
            disp.setImage(post);
            //Utils.copyImage(post, dest);
        }
        else
            disp.setImage(dest);
        saved = false;
    }
    /**This method should be called by the Filter Lists*/
    public void refresh()
    {
        if(prefilts != null)
        {
            preImg = prefilterImage(src, prefilts);
        }
        if(lastQ != null)
        {
            Utils.skew(preImg, dest, lastQ.x1, lastQ.y1, lastQ.x2, lastQ.y2, lastQ.x3, lastQ.y3, lastQ.x4, lastQ.y4);
        }
        else
        {
            Utils.skew(preImg, dest, 0, 0, preImg.getWidth()-1, 0, preImg.getWidth()-1, preImg.getHeight()-1, 0, preImg.getHeight()-1);
        }
        if(postfilts != null)
        {
            BufferedImage post = postfilterImage(dest, postfilts);
            disp.setImage(post);
            //Utils.copyImage(post, dest);
        }
        else
            disp.setImage(dest);
        saved = false;
    }
    public boolean isSaved()
    {
        return saved;
    }
    public BufferedImage prefilterImage(BufferedImage source, Vector filters)
    {
        prefilts = filters;
        if(tmp1 == null || tmp1.getWidth() != source.getWidth() || tmp1.getHeight() != source.getHeight())
        {
            //System.out.println("new tmp1");
            tmp1 = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
        }
        if(tmp2 == null || tmp2.getWidth() != source.getWidth() || tmp2.getHeight() != source.getHeight())
        {
            //System.out.println("new tmp2");
            tmp2 = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
        }
        //Utils.copyImage(source, tmp1);
        int i = 0;
        for(Enumeration en = filters.elements(); en.hasMoreElements();)
        {
            TCFilter cur = (TCFilter)en.nextElement();
            if(i == 0)          cur.filter(source, tmp1);
            else if(i % 2 == 1) cur.filter(tmp1, tmp2);
            else                cur.filter(tmp2, tmp1);
            i++;
        }
        if(i == 0)          return source;
        else if(i % 2 == 1) return tmp1;
        else                return tmp2;
    }
    public BufferedImage postfilterImage(BufferedImage source, Vector filters)
    {
        postfilts = filters;
        if(tmp3 == null || tmp3.getWidth() != source.getWidth() || tmp3.getHeight() != source.getHeight())
        {
            //System.out.println("new tmp3");
            tmp3 = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
        }
        if(tmp4 == null || tmp4.getWidth() != source.getWidth() || tmp4.getHeight() != source.getHeight())
        {
            //System.out.println("new tmp4");
            tmp4 = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
        }
        //Utils.copyImage(source, tmp3);
        int i = 0;
        for(Enumeration en = filters.elements(); en.hasMoreElements();)
        {
            TCFilter cur = (TCFilter)en.nextElement();
            if(i == 0)          cur.filter(source, tmp3);
            else if(i % 2 == 1) cur.filter(tmp3, tmp4);
            else                cur.filter(tmp4, tmp3);
            i++;
        }
        if(i == 0)          return source;
        else if(i % 2 == 1) return tmp3;
        else                return tmp4;
    }
    public BufferedImage getImageBeforeFilter(TCFilter f)
    {
        //if(prefilts.contains(f)) System.out.println("ALELOUIA!!!!!!");
        BufferedImage next = src;
        if(prefilts != null)
        {
            if(tmp1 == null || tmp1.getWidth() != src.getWidth() || tmp1.getHeight() != src.getHeight())
            {
                tmp1 = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
            }
            if(tmp2 == null || tmp2.getWidth() != src.getWidth() || tmp2.getHeight() != src.getHeight())
            {
                tmp2 = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
            }
            int i = 0;
            for(Enumeration en = prefilts.elements(); en.hasMoreElements();)
            {
                TCFilter cur = (TCFilter)en.nextElement();
                //System.out.println(cur+" != "+f+"??? WTF??????");
                if(cur == f)
                {
                    if(i == 0)          return src;
                    else if(i % 2 == 1) return tmp1;
                    else                return tmp2;
                }
                if(i == 0)          cur.filter(src, tmp1);
                else if(i % 2 == 1) cur.filter(tmp1, tmp2);
                else                cur.filter(tmp2, tmp1);
                i++;
            }
            if(i == 0)          next = src;
            else if(i % 2 == 1) next = tmp1;
            else                next = tmp2;
        }
        if(lastQ != null)
        {
            Utils.skew(next, dest, lastQ.x1, lastQ.y1, lastQ.x2, lastQ.y2, lastQ.x3, lastQ.y3, lastQ.x4, lastQ.y4);
        }
        else
        {
            Utils.skew(next, dest, 0, 0, preImg.getWidth()-1, 0, preImg.getWidth()-1, preImg.getHeight()-1, 0, preImg.getHeight()-1);
        }
        if(postfilts != null)
        {
            if(tmp3 == null || tmp3.getWidth() != dest.getWidth() || tmp3.getHeight() != dest.getHeight())
            {
                tmp3 = new BufferedImage(dest.getWidth(), dest.getHeight(), BufferedImage.TYPE_INT_RGB);
            }
            if(tmp4 == null || tmp4.getWidth() != dest.getWidth() || tmp4.getHeight() != dest.getHeight())
            {
                tmp4 = new BufferedImage(dest.getWidth(), dest.getHeight(), BufferedImage.TYPE_INT_RGB);
            }
            int i = 0;
            for(Enumeration en = postfilts.elements(); en.hasMoreElements();)
            {
                TCFilter cur = (TCFilter)en.nextElement();
                if(cur == f)
                {
                    //System.out.println("ALELOUIA!!!!!!" + i);
                    if(i == 0)
                    {
                        //System.out.println("return dest");
                        return dest;
                    }
                    else if(i % 2 == 1) return tmp3;
                    else                return tmp4;
                }
                if(i == 0)          cur.filter(dest, tmp3);
                else if(i % 2 == 1) cur.filter(tmp3, tmp4);
                else                cur.filter(tmp4, tmp3);
                i++;
            }
        }
        return null;
    }
    public BufferedImage getImage2BeforeFilter(TCFilter f)
    {
        BufferedImage next = src;
        if(prefilts != null)
        {
            if(tmp1 == null || tmp1.getWidth() != src.getWidth() || tmp1.getHeight() != src.getHeight())
            {
                tmp1 = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
            }
            if(tmp2 == null || tmp2.getWidth() != src.getWidth() || tmp2.getHeight() != src.getHeight())
            {
                tmp2 = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
            }
            int i = 0;
            for(Enumeration en = prefilts.elements(); en.hasMoreElements();)
            {
                TCFilter cur = (TCFilter)en.nextElement();
                if(cur == f)
                {
                    if(i == 0)          return tmp1;
                    else if(i % 2 == 1) return tmp2;
                    else                return tmp1;
                }
                if(i == 0)          cur.filter(src, tmp1);
                else if(i % 2 == 1) cur.filter(tmp1, tmp2);
                else                cur.filter(tmp2, tmp1);
                i++;
            }
            if(i == 0)          next = src;
            else if(i % 2 == 1) next = tmp1;
            else                next = tmp2;
        }
        if(lastQ != null)
        {
            Utils.skew(next, dest, lastQ.x1, lastQ.y1, lastQ.x2, lastQ.y2, lastQ.x3, lastQ.y3, lastQ.x4, lastQ.y4);
        }
        else
        {
            Utils.skew(next, dest, 0, 0, preImg.getWidth()-1, 0, preImg.getWidth()-1, preImg.getHeight()-1, 0, preImg.getHeight()-1);
        }
        if(postfilts != null)
        {
            if(tmp3 == null || tmp3.getWidth() != dest.getWidth() || tmp3.getHeight() != dest.getHeight())
            {
                tmp3 = new BufferedImage(dest.getWidth(), dest.getHeight(), BufferedImage.TYPE_INT_RGB);
            }
            if(tmp4 == null || tmp4.getWidth() != dest.getWidth() || tmp4.getHeight() != dest.getHeight())
            {
                tmp4 = new BufferedImage(dest.getWidth(), dest.getHeight(), BufferedImage.TYPE_INT_RGB);
            }
            //Utils.copyImage(source, tmp3);
            int i = 0;
            for(Enumeration en = postfilts.elements(); en.hasMoreElements();)
            {
                TCFilter cur = (TCFilter)en.nextElement();
                if(cur == f)
                {
                    if(i == 0)          return tmp3;
                    else if(i % 2 == 1) return tmp4;
                    else                return tmp3;
                }
                if(i == 0)          cur.filter(dest, tmp3);
                else if(i % 2 == 1) cur.filter(tmp3, tmp4);
                else                cur.filter(tmp4, tmp3);
                i++;
            }
        }
        return null;
    }
    /**Only call this prior to AUTOMATIC saving... no more cancellings!!!*/
    public BufferedImage getImageToSave()
    {
        saved = true;
        if(prefilts != null)
        {
            preImg = prefilterImage(src, prefilts);
        }
        if(lastQ != null)
        {
            Utils.skew(preImg, dest, lastQ.x1, lastQ.y1, lastQ.x2, lastQ.y2, lastQ.x3, lastQ.y3, lastQ.x4, lastQ.y4);
        }
        else
        {
            Utils.skew(preImg, dest, 0, 0, preImg.getWidth()-1, 0, preImg.getWidth()-1, preImg.getHeight()-1, 0, preImg.getHeight()-1);
        }
        if(postfilts != null)
        {
            BufferedImage post = postfilterImage(dest, postfilts);
            return post;
        }
        else
            return dest;
    }
}
