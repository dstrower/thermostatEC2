package com.aws.get.started;

public class TemperatureState {

	private TemperatureReportedState reported;

	public TemperatureReportedState getReported() {
		return reported;
	}

	public void setReported(TemperatureReportedState reported) {
		this.reported = reported;
	}

	@Override
	public String toString() {
		return "TemperatureState [reported=" + reported + "]";
	}
	
	
}
