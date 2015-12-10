package Server;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Temperature implements Serializable, Comparable<Temperature>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float temp;
	private	Date date;
	private int sample;
	
	private static int nextSample = 1;
	
	public Temperature(){
		this.temp = 0;
		this.date = Calendar.getInstance().getTime();
		this.sample = nextSample++;
	}
	
	
	public void display(){
		System.out.println("Temp: " + temp);
		System.out.println("Time: " + date);
		System.out.println("Sample: " + sample);
	}
	

	public void setTemperature(float temp){
		this.temp = temp;
	}
	public float getTemperature(){
		return this.temp;
	}
	
	public Date getSampleDate(){
		return this.date;
	}
	
	public int getSampleNumber(){
		return this.sample;
	}

	@Override //Allow Collection max and min to be used.
	public int compareTo(Temperature t) {
		if(this.getTemperature() > t.getTemperature())
			return 1;
		else if(this.getTemperature() == t.getTemperature())
			return 0;
		else
			return -1;
	}
}
