package Client;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;

import Server.Temperature;


public class CustomCanvas extends JComponent{

	private int width, height,range, yOffset, xOffset;
	private float avg = 0;
	private int stringWidth = 80; 
	private float max,min,scale,xrange;
	String maxString, minString;
	private Vector<Temperature> v;
	
	CustomCanvas(){
		super();
		this.range = 10;
	}
	
	@Override
	protected void paintComponent(Graphics g){
		
		super.paintComponent(g);
		
		g.setColor(new Color(255,255,255));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(new Color(0,0,0));	
		g.drawLine(stringWidth, height+yOffset * 2,stringWidth, yOffset);
		g.drawLine(stringWidth, height+yOffset * 2,width + stringWidth * 2, height+yOffset * 2);
		
		int start = 0;

		if(v != null){
			if(v.size() > range)
				start = v.size() - range;
			
			int count = 1;
			for(int i = start; i < v.size(); i++){	
				g.setColor(new Color(0,0,255));
				Temperature t = v.get(i);
				this.xrange = max - min;
				int ypixel = this.yCoordinate(t.getTemperature());
				int xpixel = this.xCoordinate(count);
				g.fillRect(xpixel, ypixel, 5, 5);
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YY");
				SimpleDateFormat timeFormat = new SimpleDateFormat("(HH:mm:ss)"); 
				String date = dateFormat.format(t.getSampleDate());
				String time = timeFormat.format(t.getSampleDate());
				String temp = t.getTemperature()+"";
			
				g.drawString(time, xpixel-20, height+yOffset * 6);
				g.drawString(date, xpixel-20, height+yOffset * 8);
				g.drawString(temp, xpixel - 20, ypixel + 15);
				
				String minString = min + " deg";
				g.drawString(minString, 0, this.yCoordinate(Collections.min(v).getTemperature()) + 5);
				String avgString = avg + " deg";
				g.drawString(avgString, 0, this.yCoordinate(avg) + 5);
				String maxString = max + " deg";
				g.drawString(maxString, 0, this.yCoordinate(Collections.max(v).getTemperature()) + 5);
				
				g.setColor(new Color(255,255,0));
				g.drawLine(stringWidth, this.yCoordinate(avg),width + stringWidth * 2, this.yCoordinate(avg));
				g.setColor(new Color(255,0,0));
				g.drawLine(stringWidth, this.yCoordinate(max),width + stringWidth * 2, this.yCoordinate(max));
				g.setColor(new Color(0,255,0));
				g.drawLine(stringWidth, this.yCoordinate(min),width + stringWidth * 2, this.yCoordinate(min));
					
				count++;
			}
		}
	}
	
	
	public void drawGraph(Vector<Temperature> v, int range){
		
		height = this.getHeight() - 50;
		width = this.getWidth() - 2*stringWidth;
		
		max = 0;
		min = 0;
		avg = 0;
		
		if (!v.isEmpty()){
			max = Collections.max(v).getTemperature();
			min = Collections.min(v).getTemperature();
		
			avg = 0;
			if(!v.isEmpty()){
				for(Temperature tem : v){
					avg += tem.getTemperature();
				}
				avg = avg/v.size();
			}
		}
		scale = max - min;
		scale = height/scale;
		
		this.v = v;
		this.range = range;
		
		this.repaint();
	}
	
	private int xCoordinate(int count){
		xOffset = stringWidth + 1;
		int xPixel = (width/range) *(count);
		return xPixel + xOffset;
	}
	
	private int yCoordinate(float t){
		yOffset = 5;
		int yPixel = (int) (((xrange - (t - min)) /xrange) * height);
		return yPixel + yOffset;
	}
	

}
