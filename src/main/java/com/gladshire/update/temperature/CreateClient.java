package com.gladshire.update.temperature;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil.KeyStorePasswordPair;

public class CreateClient {
	
	private String getFilePath(String filename) {
		InputStream input = null;
		String filePath = null;
		try {

			
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
				return null;
			} else {
				String prefix = "temp";
				String suffix  = "tmp";
				File tempFile = File.createTempFile(prefix, suffix);
				tempFile.deleteOnExit();
				FileOutputStream out = new FileOutputStream(tempFile);
				IOUtils.copy(input, out);
				System.out.println("Temp file created.");
				filePath = tempFile.getAbsolutePath();
			}

			

		} catch (IOException ex) {
			ex.printStackTrace();			
		}
		return filePath;
	}

	public AWSIotMqttClient getClient() {
		
		String certificateFile = getFilePath("devices/desiredTemperature/desiredTemp.cert.pem");
		String privateKeyFile =  getFilePath("devices/desiredTemperature/desiredTemp.private.key");
		
		
		String clientEndpoint = "a24x1e67am5bw6.iot.us-west-2.amazonaws.com"; // replace <prefix> and <region> with your
		// own
		String clientId = "SpringBoot"; // replace with your own client ID. Use unique client IDs for
		// concurrent connections.
		//String certificateFile = prop.getProperty("desiredCertificateFile");
		//String certificateFile = "desiredTemp/desiredTemp.cert.pem";
		// based
		// certificate
		// file
		//String privateKeyFile = prop.getProperty("desiredkeyFile");
		//String privateKeyFile = "desiredTemp/desiredTemp.private.key";
		System.out.println("Cert file = "+ certificateFile);
		System.out.println("Private key file = "+ privateKeyFile);
		// SampleUtil.java and its dependency PrivateKeyReader.java can be copied from
		// the sample source code.
		// Alternatively, you could load key store directly from a file - see the
		// example included in this README.
		KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile);
		System.out.println("Created keywordPair");
		AWSIotMqttClient client = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
		return client;
	}
}
