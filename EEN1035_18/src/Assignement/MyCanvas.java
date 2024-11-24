package Assignement;

import java.awt.*;
import java.util.*;


@SuppressWarnings("serial")
public class MyCanvas extends Canvas {
	private double [] RecivedValues= new double[10];
	private double tempAvg, humidityAvg, soundAvg;
	public MyCanvas () {
		
		this.setPreferredSize(new Dimension(800,400));
		 for (int i = 0; i < 10; i++) {
			 RecivedValues[i]= i *10 + 3;
		 }
	}
    public void updateAverages(double temp, double sound, double humidity) {
        this.tempAvg = temp;
        this.soundAvg =  sound;
        this.humidityAvg = humidity;
        repaint();
    }
    public void findAvg(Stack<SensorObject> stack) {
    	double temp=0,sound=0,hum=0;
    	for (Object o : stack) {
    		SensorObject sensor = (SensorObject) o;
    		temp += sensor.getValue(1);
    		sound += sensor.getValue(2);
    		hum +=sensor.getValue(3);
    	}
    	temp = temp/stack.size();
    	sound = sound/stack.size();
    	hum = hum/stack.size();
    	updateAverages(temp, sound, hum);
    }
	public void paint(Graphics g) {
		int width = 400;
        int height = 400;

        // Draw background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // Draw axes
        g.setColor(Color.BLACK);
        g.drawLine(50, height - 50, width - 50, height - 50); // x-axis
        g.drawLine(50, height - 50, 50, 50); // y-axis
        int xAxisLength = width-100;
        // Draw axis labels
        g.drawString("0", 40, height - 45); // Origin
        // Draw tick marks and labels on x-axis
       for (int i = 1; i <= 10; i++) {
            int x = 50 + (i * (width - 100) / 10);
            g.drawLine(x, height - 55, x, height - 45);
            g.drawString(Integer.toString(i), x - 5, height - 30);
        }

        // Draw tick marks and labels on y-axis
        for (int i = 1; i <= 10; i++) {
            int y = height - 50 - (i * (height - 100) / 10);
            g.drawLine(45, y, 55, y);
            g.drawString(Integer.toString(i * 10), 20, y + 5);
        }

        // Plot points from RecivedValues
        
        for (int i = 0; i < RecivedValues.length; i++) {
        	g.setColor(Color.RED);
        	int x = 50 + ((i+1) * (xAxisLength) / 10); // Start from 1
            int y = (int) (height - 50 - (RecivedValues[i] * (height - 100) / 100)); // Scale y (0-100)
            g.fillOval(x - 3, y - 3, 6, 6); // Draw point as a small circle
        }
     // Draw the gauge for average values
        drawGauge(g, width + 50, 100, "Temperature Avg", tempAvg, Color.BLUE);
        drawGauge(g, width + 50, 200, "Humidity Avg", humidityAvg, Color.GREEN);
        drawGauge(g, width + 50, 300, "Sound Avg", soundAvg, Color.ORANGE);
		
	}
    private void drawGauge(Graphics g, int x, int y, String label, double value, Color color) {
        int gaugeWidth = 150;
        int gaugeHeight = 30;

        // Draw background of the gauge
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, gaugeWidth, gaugeHeight);

        // Draw filled part of the gauge based on value (scale 0-100)
        int filledWidth = (int) (value * gaugeWidth / 100); // Scale value to gauge width
        g.setColor(color);
        g.fillRect(x, y, filledWidth, gaugeHeight);

        // Draw gauge border
        g.setColor(Color.BLACK);
        g.drawRect(x, y, gaugeWidth, gaugeHeight);

        // Draw label and value
        g.drawString(label + ": " + String.format("%.2f", value), x, y - 5);
    }

}
