package Assignement;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SensorObject implements Serializable {
	private String Name;
	private Integer ID, V1, V2, V3;
	public SensorObject(String Name, Integer ID, Integer V1, Integer V2, Integer V3) {
		this.Name = Name; this.ID = ID; this.V1= V1; this.V2= V2; this.V3= V3;
	}
	String display() {
		return "name "+ this.Name + "ID " + this.ID;
	}

}
