package com.aws.get.started;

import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotDeviceProperty;

public class DesiredTemperatureIot extends AWSIotDevice  {
	
	private static final String THING_NAME = "desiredTemperature";

	public DesiredTemperatureIot(String thingName) {
		super(THING_NAME);
		// TODO Auto-generated constructor stub
	}
	
	
	public String getTemperature() {
		return temperature;
	}


	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}


	public String getLastUpdate() {
		return lastUpdate;
	}


	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}


	@AWSIotDeviceProperty
	private String temperature;
	
	@AWSIotDeviceProperty
	private  String lastUpdate;

}
