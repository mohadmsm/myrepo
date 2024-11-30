
package Assignement;

import java.net.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

@SuppressWarnings("serial")
public class Client extends JFrame implements ChangeListener, ActionListener, WindowListener,KeyListener, Runnable{
	
	private static int portNumber = 5050;
    private Socket socket = null;
    private ObjectOutputStream os = null;
    private ObjectInputStream is = null;
    private JButton Connect, ChangeName;
	private JTextField Sensor1,Sensor2,Sensor3;
	private JLabel CN, temp, Sound, Humidity, noiseL;
	private JSlider slider1, slider2, slider3;
	private String serverIP;
	private Boolean Status = false;
	private Thread thread;
	private JCheckBox  noiseBox;
	// the constructor expects the IP address of the server - the port is fixed
    public Client(String serverIP, String DeviceName) {
    	this.serverIP = serverIP;
		JPanel hp = new JPanel();
		hp.setLayout(new BoxLayout(hp, BoxLayout.Y_AXIS));
		this.getContentPane().add(hp);
		JPanel p;
		p = new JPanel();
		p.setLayout(new FlowLayout());
		CN = new JLabel("Device "+DeviceName);
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
		// Sensor 2 Sound Level
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
		// Sensor 3 humidity
		Humidity = new JLabel("Humidity Level ");
		Sensor3 = new JTextField(5);
		slider3 = new JSlider(-10, 100, 20);
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.setBorder(new TitledBorder("Sensor 3"));
		slider3 = new JSlider(0, 100, 20);
		slider3.setPaintTicks(true);
		slider3.setMajorTickSpacing(10);
		p.add(Humidity);	
		p.add(Sensor3);
		p.add(slider3);
		hp.add(p);
		//noise 
		p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		p.setBorder(new TitledBorder("Real life Signals"));
		noiseL = new JLabel("Add Noise");
		noiseBox = new JCheckBox();
		p.add(noiseL);
		p.add(noiseBox);
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
		this.slider1.addChangeListener(this);
		this.slider2.addChangeListener(this);
		this.slider3.addChangeListener(this);
		this.Connect.addActionListener(this);
		this.ChangeName.addActionListener(this);
		this.updateText(Sensor1, slider1);
		this.updateText(Sensor2, slider2);
		this.updateText(Sensor3, slider3);
		this.setVisible(true);
		this.addWindowListener(this);
		this.Sensor1.addKeyListener(this);
		this.Sensor2.addKeyListener(this);
		this.Sensor3.addKeyListener(this);
		this.thread = new Thread(this);
    	
    }
    private void updateText(JTextField sensor, JSlider slider) {
		sensor.setText(String.valueOf(slider.getValue()));		
	}
    private int addNoise(int value) {
        int noise = (int) (Math.random() * 10) - 4; // Generates a random number 
        int noisyValue = value + noise;
        return noisyValue;}
    
    private boolean connectToServer(String serverIP) {
    	try { // open a new socket to the server 
    		this.socket = new Socket(serverIP,portNumber);
    		this.os = new ObjectOutputStream(this.socket.getOutputStream());
    		this.is = new ObjectInputStream(this.socket.getInputStream());
    		System.out.println("00. -> Connected to Server:" + this.socket.getInetAddress() 
    				+ " on port: " + this.socket.getPort());
    		System.out.println("    -> from local address: " + this.socket.getLocalAddress() 
    				+ " and port: " + this.socket.getLocalPort());
    	} 
        catch (Exception e) {
        	System.out.println("XX. Failed to Connect to the Server at port: " + portNumber);
        	System.out.println("    Exception: " + e.toString());	
        	return false;
        }
		return true;
    }
     
    private void SendObject() {
    	int tempValue = Integer.parseInt(this.Sensor1.getText());
        int soundValue = Integer.parseInt(this.Sensor2.getText());
        int humidityValue = Integer.parseInt(this.Sensor3.getText());

        // Add noise if the checkbox is selected
        if (noiseBox.isSelected()) {
            tempValue = addNoise(tempValue);
            soundValue = addNoise(soundValue);
            humidityValue = addNoise(humidityValue);
        }
    	SensorObject MyObject = new SensorObject(this.CN.getText(),tempValue, soundValue,humidityValue, this.Status);
    	this.send(MyObject);
    	
    }
	
    // method to send a generic object.
    private void send(Object o) {
		try {
		    System.out.println("02. -> Sending an object...");
		    os.writeObject(o);
		    os.flush();
		} 
	    catch (Exception e) {
		    System.out.println("XX. Exception Occurred on Sending:" +  e.toString());
		}
    }


    public static void main(String args[]) 
    {
    	System.out.println("**. Java Client Application - EE402 OOP Module, DCU");
    	if(args.length>1){
    
    		new Client(args[0],args[1]);
		    //theApp.getDate();
		}
    	else
    	{
    		System.out.println("Error: you must provide the address of the server and client name");
    		System.out.println("Usage is:  java Client x.x.x.x ClientName  (e.g. java Client 192.168.7.2 Mohammed)");
    		System.out.println("      or:  java Client hostname ClientName (e.g. java Client localhost Salim)");
    	}    
    }

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) { if(socket !=null| Status) {Status = false; this.SendObject();}
	System.exit(0);}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(Connect)) {
			if(!this.Status) {
			if (!this.connectToServer(serverIP)) {
	    		System.out.println("XX. Failed to open socket connection to: " + serverIP);            
	    	}
			else {
				Status = true;
				this.Connect.setText("Connected");
				this.Connect.setForeground(Color.green);
				this.thread.start();
			}}
			else {
				Status = false;
				this.Connect.setText("Connect");
				this.Connect.setForeground(Color.black);
				//this.thread.suspend();
				this.SendObject();
			}
		}
		if(e.getSource().equals(ChangeName)) {
			String s = JOptionPane.showInputDialog(this, "Enter the device Name?", 
					"A Question", JOptionPane.QUESTION_MESSAGE);
			this.CN.setText("Device "+ s);
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		if(!Character.isDigit(e.getKeyChar())) {
			e.consume();
		}	
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ENTER) {
			JTextField source = (JTextField) e.getSource();
			int value = Integer.parseInt(source.getText());		
			if(value>100) {
				source.setText("100");
			}
			else if(value<0){
				source.setText("0");
			}
			if (source == Sensor1) {
                slider1.setValue(value);
            } else if (source == Sensor2) {
                slider2.setValue(value);
            } else if (source == Sensor3) {
                slider3.setValue(value);
            }
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {	
	}
	@Override
	public void run() {
		while(Status) {
			this.SendObject();
			try {
				Thread.sleep(5000);
			}catch(InterruptedException e) {
				System.out.println("Thread was Interrupted");
			}				
		}	
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource().equals(slider1)) {
			updateText(Sensor1, slider1);
		}
		if(e.getSource().equals(slider2)) {
			updateText(Sensor2, slider2);
		}
		if(e.getSource().equals(slider3)) {
			updateText(Sensor3, slider3);
		}	
	}
	
}
