package com.gladshire.update.temperature;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil.KeyStorePasswordPair;

public class CreateClient {

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
		String clientId = "SpringBoot"; // replace with your own client ID. Use unique client IDs for
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
