package com.aws.get.started;

public class TemperatureReportedState {
	private String lastUpdate;
    private String temperature;
    private String humidity;
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
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	@Override
	public String toString() {
		return "TemperatureReportedState [lastUpdate=" + lastUpdate + ", temperature=" + temperature + ", humidity="
				+ humidity + "]";
	}
	
}
