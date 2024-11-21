package Assignement;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SensorObject implements Serializable {
	private String Name;
	private Integer ID, V1, V2, V3;
	private Boolean s;
	public SensorObject(String Name, Integer V1, Integer V2, Integer V3, Boolean s) {
		this.Name = Name; this.V1= V1; this.V2= V2; this.V3= V3; this.s = s;
	}
    public String getName() {
        return Name;
    }
    public Boolean getStatus() {
    	return s;
    }
    public void setID(int ID) {
    	this.ID = ID;
    }
    public Integer getID() {
    	return this.ID;
    }
    public int getValue(int SensorNm) {
    	if (SensorNm ==1) {return V1;}
    	if (SensorNm ==2) {return V2;}
    	if (SensorNm ==3) {return V3;}
        return 0;
    }

}
