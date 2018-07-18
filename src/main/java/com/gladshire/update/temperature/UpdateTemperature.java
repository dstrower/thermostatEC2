package com.gladshire.update.temperature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.StringTokenizer;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil.KeyStorePasswordPair;
import com.aws.get.started.TemperatureIot;

public class UpdateTemperature {

	/***
	public static void main(String[] args) {
		UpdateTemperature myApp = new UpdateTemperature();
		myApp.runApp();
	}
    **/
	
	public void runApp() {
		TemperatureIot temperatureIot = null;
		AWSIotMqttClient client = getClient();
		System.out.println("Client created");
		File file = new File("temperature.txt");

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String st;
			String lastGoodLine = null;
			while (true) {
				if ((st = br.readLine()) != null) {
					System.out.println(st);
					lastGoodLine = st;
				} else {
					if (lastGoodLine != null) {
						temperatureIot = processLine(lastGoodLine, temperatureIot, client);
						lastGoodLine = null;
					}
					Thread.sleep(10000);
					System.out.println("Sleeping");
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private AWSIotMqttClient getClient() {
		Properties prop = new Properties();
		InputStream input = null;
		try {

			String filename = "config.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
				return null;
			}

			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		String clientEndpoint = "a24x1e67am5bw6.iot.us-west-2.amazonaws.com"; // replace <prefix> and <region> with your
		// own
		String clientId = "HelloWorld_Publisher"; // replace with your own client ID. Use unique client IDs for
		// concurrent connections.
		String certificateFile = prop.getProperty("certificateFile");
		// based
		// certificate
		// file
		String privateKeyFile = prop.getProperty("privateKeyFile");
		System.out.println("Cert file = "+ certificateFile);
		System.out.println("Private key file = "+ privateKeyFile);
		// SampleUtil.java and its dependency PrivateKeyReader.java can be copied from
		// the sample source code.
		// Alternatively, you could load key store directly from a file - see the
		// example included in this README.
		KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile);
		AWSIotMqttClient client = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
		return client;
	}

	private static TemperatureIot processLine(String line, TemperatureIot temperatureIot, AWSIotMqttClient client) {
		StringTokenizer st = new StringTokenizer(line, "|");
		if (st.countTokens() == 2) {
			String humidityToken = st.nextToken();
			String tempToken = st.nextToken();
			String humidity = getHumidity(humidityToken);
			String temperature = getTemperature(tempToken);
			if (humidity != null && temperature != null) {
				System.out.println("Humidity: " + humidity);
				System.out.println("Temperature: " + temperature);
				String lastUpdate = getTime();
				if (temperatureIot == null) {
					String thingName = "temperature";
					temperatureIot = new TemperatureIot(thingName);
					temperatureIot.setTemperature(temperature);
					temperatureIot.setHumidity(humidity);
					temperatureIot.setLastUpdate(lastUpdate);
					System.out.println("First temperature setting");
					long reportInterval = 5000; // 5 seconds
					temperatureIot.setReportInterval(reportInterval);
					try {
						client.attach(temperatureIot);
						client.connect();
					} catch (AWSIotException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					temperatureIot.setTemperature(temperature);
					temperatureIot.setHumidity(humidity);
					temperatureIot.setLastUpdate(lastUpdate);
					System.out.println("Another temperature setting");
				}
			}
		}
		return temperatureIot;
	}

	private static String getTime() {
		Calendar rightNow = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String strDate = dateFormat.format(rightNow.getTime());
		return strDate;
	}

	private static String getTemperature(String tempToken) {
		String temperature = null;
		StringTokenizer temp = new StringTokenizer(tempToken, "*");
		temperature = temp.nextToken();
		temperature = temperature.trim();
		double lTemp = Double.parseDouble(temperature);
		double five = 5.0;
		double fTemp = (lTemp * 9 / five) + 32;
		temperature = Double.toString(fTemp);
		return temperature;
	}

	private static String getHumidity(String humidityToken) {
		String humidity = null;
		StringTokenizer hum = new StringTokenizer(humidityToken, ":");
		if (hum.countTokens() == 2) {
			String nothing = hum.nextToken();
			String nextPart = hum.nextToken();
			StringTokenizer lastToken = new StringTokenizer(nextPart,"%");
			humidity = lastToken.nextToken();
			humidity = humidity.trim();
		}
		return humidity;
	}
}
