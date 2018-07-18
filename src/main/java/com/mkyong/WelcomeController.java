package com.mkyong;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.aws.get.started.Temperature;
import com.aws.get.started.TemperatureIot;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gladshire.update.temperature.CreateClient;

@Controller
public class WelcomeController {
	
	AWSIotMqttClient client = null;
	TemperatureIot temperatureIot = null;
	
	
	

	// inject via application.properties
	@Value("${welcome.message:test}")
	private String message = "Hello World";

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		System.out.println("Controller started");
		CreateClient createClient = new CreateClient();
		client = createClient.getClient();
		if(client != null) {
			String thingName = "temperature";
		    temperatureIot =  new TemperatureIot(thingName);
		    
			try {
				client.attach(temperatureIot);
				client.connect();
				String state = temperatureIot.get();
				System.out.println("The state is: " + state);
				
			    client.disconnect();
			    ObjectMapper objectMapper = new ObjectMapper();
		        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		        Temperature temperature= objectMapper.readValue(state, Temperature.class);
		        model.put("temperature", temperature.getState().getReported().getTemperature());
		        model.put("humidity",temperature.getState().getReported().getHumidity());
		        model.put("lastUpdate",temperature.getState().getReported().getLastUpdate());
		        System.out.println(temperature);
				//client.disconnect();
			} catch (AWSIotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		model.put("message", "Hi There");
		return "welcome";
	}

}
