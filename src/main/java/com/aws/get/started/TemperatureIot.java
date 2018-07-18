package com.aws.get.started;

import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotDeviceProperty;

public class TemperatureIot extends AWSIotDevice {

	private static final String THING_NAME = "temperature";

	public TemperatureIot(String tg) {
		super(THING_NAME);
	}

	@AWSIotDeviceProperty
	private String temperature;
	
	@AWSIotDeviceProperty
	private String humidity;
	
	@AWSIotDeviceProperty
	private String lastUpdate;

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
