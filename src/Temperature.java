import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Temperature implements Serializable, TemperatureInterface{
	
	private float temp;
	private	String date;
	private int sample;
	private static final String PATH = "/sys/bus/iio/devices/iio:device0/in_voltage";
	private static int nextSample = 0;
	
	Temperature(){
		this.temp = this.calcTemperature();
		this.date = Calendar.getInstance().getTime().toString();
		this.sample = nextSample++;
	}
	
	private int readAnalog(int number){
		int value;
		try {
			BufferedReader br = new BufferedReader(new FileReader(PATH + number + "_raw"));
			value = Integer.parseInt(br.readLine());
			
		} catch (FileNotFoundException e) {
			System.err.println("Incorrect Path to sensor");
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			System.err.println("Error reading temperature from file");
			e.printStackTrace();
			return -1;
		}
		return value;
	}
	
	private float calcTemperature(){
		int value = readAnalog(4); //1207;
		float voltage = value*(1.80f / 4096.0f);
		float degrees = (voltage - 0.75f)/0.01f;
		return (25.0f + degrees);
		
	}
	
	public void display(){
		System.out.println("Temp: " + temp);
		System.out.println("Time: " + date);
		System.out.println("Sample: " + sample);
	}
	
	public Temperature updateTemperature(){
		this.temp = this.calcTemperature();
		this.date = Calendar.getInstance().getTime().toString();
		++this.sample;
		return this;
	}
	public float getTemperature(){
		return this.temp;
	}
	
	public String getSampleDate(){
		return this.date;
	}
	
	public int getSampleNumber(){
		return this.sample;
	}
}
