import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Temperature {
	
	private float temp;
	private	String date;
	private int sample;
	private static int nextSample = 0;
	private static final String PATH = "/sys/bus/iio/devices/iio:device0/in_voltage";
	
	Temperature(){
		this.temp = this.calcTemperature();
		this.date = Calendar.getInstance().getTime().toString();
		this.sample = nextSample;
		nextSample++;
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
		int value = readAnalog(0);
		float voltage = value*(1.80f / 4096.0f);
		float degrees = (voltage - 0.75f)/0.01f;
		return (25.0f + degrees);
		
	}
	
	public void display(){
		System.out.println("Temp: " + temp);
		System.out.println("Time: " + date);
		System.out.println("Sample: " + sample);
	}
	
	public static void main(String[] args){
		Temperature t1 = new Temperature();
		t1.display();
		
		Temperature t2 = new Temperature();
		t2.display();
		
		Temperature t3 = new Temperature();
		t3.display();
		
		t1.display();
	}
	
}
