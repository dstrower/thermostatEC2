package com.aws.get.started;

public class Temperature {

	private TemperatureState state;

	public TemperatureState getState() {
		return state;
	}

	public void setState(TemperatureState state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Temperature [state=" + state + "]";
	}
	
	
}
