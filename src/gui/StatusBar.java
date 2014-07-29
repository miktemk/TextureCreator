package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class StatusBar extends JPanel
{
	boolean repaintable = true;
	String msg;
	JLabel message;
	double x, y;
	JLabel coords;
	private void initialize()
	{
		setLayout(new BorderLayout());
		 JPanel messageCarrier = new JPanel();
		 messageCarrier.setLayout(new BorderLayout());
		 message = new JLabel();
		 messageCarrier.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		 messageCarrier.add(message, BorderLayout.CENTER);
	 //--------------------------------------------------------------------------
		 JPanel coordsCarrier = new JPanel();
		 coordsCarrier.setLayout(new BorderLayout());
		 coords = new JLabel();
		 coordsCarrier.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		 coordsCarrier.add(coords, BorderLayout.CENTER);
		 
		add(messageCarrier, BorderLayout.CENTER);
		add(coordsCarrier, BorderLayout.EAST);
	}
	public StatusBar()
	{
		initialize();
	}
	public StatusBar(String msg, double x, double y)
	{
		this.msg = msg;
		this.x = x;
		this.y = y;
		initialize();
	}
	public void setAutomaticallyRepaintable(boolean repaintable) { this.repaintable = repaintable; }
	public void bleep()
	{
		message.setText(""+msg);
		coords.setText(""+x+" : "+y);
		repaint();
	}
	public void setMessage(String msg)
	{
		this.msg = msg;
		if(repaintable) { bleep(); }
	}
	public void setX(double x)
	{
		this.x = x;
		if(repaintable) { bleep(); }
	}
	public void setY(double y)
	{
		this.y = y;
		if(repaintable) { bleep(); }
	}
	public void setXY(double x, double y)
	{
		this.x = x;
		this.y = y;
		if(repaintable) { bleep(); }
	}
}
