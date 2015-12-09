package Server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TemperatureService {
	
	private static final String PATH = "/sys/bus/iio/devices/iio:device0/in_voltage";
	
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
	
	public float calcTemperature(){
		int value = readAnalog(4); //1207;
		float voltage = value*(1.80f / 4096.0f);
		float degrees = (voltage - 0.75f)/0.01f;
		return (25.0f + degrees);
		//return (float) ((Math.random()*5.0f) + 10.0f);
		
	}
}
