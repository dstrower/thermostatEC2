package com.mkyong;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.aws.get.started.DesiredTemperature;
import com.aws.get.started.DesiredTemperatureIot;
import com.aws.get.started.DesiredTemperatureReportedState;
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
	DesiredTemperatureIot desiredTemperatureIot = null;
	
	// inject via application.properties
	@Value("${welcome.message:test}")
	private String message = "Hello World";

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		System.out.println("Controller started");
		CreateClient createClient = new CreateClient();
		client = createClient.getClient();
		if (client != null) {
			String thingName = "temperature";
			temperatureIot = new TemperatureIot(thingName);
			desiredTemperatureIot = new DesiredTemperatureIot("");
			try {
				client.attach(temperatureIot);
				client.attach(desiredTemperatureIot);
				model.put("desiredTemperature", 80);
				client.connect();
				String state = temperatureIot.get();
				// String state = "";
				System.out.println("The state is: " + state);
				String desiredState = desiredTemperatureIot.get();
				System.out.println("The desiredState is: " + desiredState);
				
				client.disconnect();
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Temperature temperature = objectMapper.readValue(state, Temperature.class);
				model.put("temperature", temperature.getState().getReported().getTemperature());
				model.put("humidity", temperature.getState().getReported().getHumidity());
				model.put("lastUpdate", temperature.getState().getReported().getLastUpdate());
				DesiredTemperature  desiredTemperature = objectMapper.readValue(desiredState,DesiredTemperature.class);
				model.put("desiredTemperature", desiredTemperature.getState().getReported().getTemperature());
				System.out.println(temperature);
				// client.disconnect();
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

	
	private static String getTime() {
		Calendar rightNow = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String strDate = dateFormat.format(rightNow.getTime());
		return strDate;
	}
	
	@RequestMapping(value = "/changeDesire", method = RequestMethod.POST)
	public String changeDesireTemperature(DesiredTemperatureReportedState desiredTemperatureReportedState) {
		System.out.println("Controller for changeDesiredTemperature is called.");
		System.out.println("Looking at the desiredTemperature");
		String temp = desiredTemperatureReportedState.getTemperature();
		System.out.println("DesiredTemperature = " + temp);
        String time = getTime();
		CreateClient createClient = new CreateClient();
		client = createClient.getClient();
		if (client != null) {
			System.out.println("Client created.");
			DesiredTemperatureIot desiredTemperatureIot = new DesiredTemperatureIot("");
			desiredTemperatureIot.setTemperature(temp);
			desiredTemperatureIot.setLastUpdate(time);
			try {
				client.attach(desiredTemperatureIot);
				client.connect();
				System.out.println("Client connected.");
			} catch (AWSIotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return "success";
	}
}
