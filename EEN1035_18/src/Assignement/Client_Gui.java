package Assignement;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class Client_Gui extends JFrame implements  ActionListener, WindowListener
{
	private JButton Connect, ChangeName;
	private JTextField Sensor1,Sensor2,Sensor3;
	private JLabel CN, temp, Sound, Humidity;
	private JSlider slider1, slider2, slider3;	
	public Client_Gui(){
		String s ="1";
		JPanel hp = new JPanel();
		hp.setLayout(new BoxLayout(hp, BoxLayout.Y_AXIS));
		this.getContentPane().add(hp);
		JPanel p;
		p = new JPanel();
		p.setLayout(new FlowLayout());
		CN = new JLabel("Device "+s);
		p.add(CN);
		hp.add(p);
		// Sensor 1 temperature 
		temp = new JLabel("Temperature   ");
		Sensor1 = new JTextField(5);
		 slider1 = new JSlider(-10, 100, 20);
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.setBorder(new TitledBorder("Sensor 1"));
		slider1 = new JSlider(0, 100, 10);
		slider1.setPaintTicks(true);
		slider1.setMajorTickSpacing(10);
		p.add(temp);
		p.add(Sensor1);
		p.add(slider1);
		hp.add(p);
		// Sensor 2 CO2 Level
		Sound = new JLabel("Sound Level    ");
		Sensor2 = new JTextField(5);
		slider2 = new JSlider(-10, 100, 20);
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.setBorder(new TitledBorder("Sensor 2"));
		slider2 = new JSlider(0, 100, 20);
		slider2.setPaintTicks(true);
		slider2.setMajorTickSpacing(10);
		p.add(Sound);
		p.add(Sensor2);
		p.add(slider2);
		hp.add(p);
		// Sensor 3 Pressure
		Humidity = new JLabel("Humidity Level ");
		Sensor3 = new JTextField(5);
		slider3 = new JSlider(-10, 100, 20);
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.setBorder(new TitledBorder("Sensor 2"));
		slider3 = new JSlider(0, 100, 20);
		slider3.setPaintTicks(true);
		slider3.setMajorTickSpacing(10);
		p.add(Humidity);	
		p.add(Sensor3);
		p.add(slider3);
		hp.add(p);
		// buttons 
		Connect = new JButton("connect");
		ChangeName = new JButton("Edit");
		p = new JPanel();
		p.setLayout(new GridLayout(1,2));
		p.add(Connect);
		p.add(ChangeName);
		hp.add(p);
		this.pack();
		this.slider1.addChangeListener(e -> updateText(Sensor1, slider1));
		this.slider2.addChangeListener(e -> updateText(Sensor2, slider2));
		this.slider3.addChangeListener(e -> updateText(Sensor3, slider3));
		this.updateText(Sensor1, slider1);
		this.updateText(Sensor2, slider2);
		this.updateText(Sensor3, slider3);
		this.setVisible(true);
		this.addWindowListener(this);
	
	}
	public void actionPerformed(ActionEvent e){
	
	}
	public static void main(String[] args) {

		new Client_Gui();
	}

	public void windowActivated(WindowEvent arg0) {
	}
	public void windowClosed(WindowEvent arg0) {}
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
	}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}

	private void updateText(JTextField sensor, JSlider slider) {
		sensor.setText(String.valueOf(slider.getValue()));		
	}
}