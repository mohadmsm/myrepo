package Assignement;

import java.awt.*;


@SuppressWarnings("serial")
public class MyCanvas extends Canvas {
	private double [] RecivedValues= new double[10];
	public MyCanvas () {
		
		this.setPreferredSize(new Dimension(400,400));
		 for (int i = 0; i < 10; i++) {
			 RecivedValues[i]= i *10 + 3;
		 }
	}
	public void paint(Graphics g) {
		int width = getWidth();
        int height = getHeight();

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
            g.setColor(Color.GRAY);
            //g.drawline(x,y,);
        }
		
	}

}
