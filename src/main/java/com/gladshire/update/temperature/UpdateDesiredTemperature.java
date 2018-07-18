package com.gladshire.update.temperature;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil.KeyStorePasswordPair;
import com.aws.get.started.DesiredTemperatureIot;

public class UpdateDesiredTemperature {
	
	/***
	
	public static void main(String[] args) {
		UpdateDesiredTemperature myApp = new UpdateDesiredTemperature();
		myApp.runApp();
	}

**/
	private void runApp() {
		AWSIotMqttClient client = getClient();
		if(client == null) {
			System.out.println("Cleint is null.");
		} else {
			System.out.println("Client has been created");
			DesiredTemperatureIot desiredTemperatureIot = new DesiredTemperatureIot("");
			desiredTemperatureIot.setTemperature("72");
			String lastUpdate = getTime();
			desiredTemperatureIot.setLastUpdate(lastUpdate);
			long reportInterval = 5000; // 5 seconds
			desiredTemperatureIot.setReportInterval(reportInterval);
			try {
				client.attach(desiredTemperatureIot);
				client.connect();
				System.out.println("Connected");
				desiredTemperatureIot.setTemperature("72");
			} catch (AWSIotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static String getTime() {
		Calendar rightNow = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String strDate = dateFormat.format(rightNow.getTime());
		return strDate;
	}
	
	public AWSIotMqttClient getClient() {
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
		String certificateFile = prop.getProperty("desiredCertificateFile");
		// based
		// certificate
		// file
		String privateKeyFile = prop.getProperty("desiredkeyFile");
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

}
