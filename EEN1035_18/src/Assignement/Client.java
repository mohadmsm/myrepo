/* The Client Class - Written by Derek Molloy for the EE402 Module
 * See: ee402.eeng.dcu.ie
 * 
 * 
 */

package Assignement;

import java.net.*;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;

@SuppressWarnings("serial")
public class Client extends JFrame implements ActionListener, WindowListener,KeyListener{
	
	private static int portNumber = 5050;
    private Socket socket = null;
    private ObjectOutputStream os = null;
    private ObjectInputStream is = null;
    private JButton Connect, ChangeName;
	private JTextField Sensor1,Sensor2,Sensor3;
	private JLabel CN, temp, Sound, Humidity;
	private JSlider slider1, slider2, slider3;
	private String serverIP;
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
		this.Connect.addActionListener(this);
		this.updateText(Sensor1, slider1);
		this.updateText(Sensor2, slider2);
		this.updateText(Sensor3, slider3);
		this.setVisible(true);
		this.addWindowListener(this);
		this.Sensor1.addKeyListener(this);
		this.Sensor2.addKeyListener(this);
		this.Sensor3.addKeyListener(this);
    	
    }
    private void updateText(JTextField sensor, JSlider slider) {
		sensor.setText(String.valueOf(slider.getValue()));		
	}

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

    private void getDate() {
    	String theDateCommand = "GetDate", theDateAndTime;
    	System.out.println("01. -> Sending Command (" + theDateCommand + ") to the server...");
    	this.send(theDateCommand);
    	try{
    		theDateAndTime = (String) receive();
    		System.out.println("05. <- The Server responded with: ");
    		System.out.println("    <- " + theDateAndTime);
    	}
    	catch (Exception e){
    		System.out.println("XX. There was an invalid object sent back from the server");
    	}
    	System.out.println("06. -- Disconnected from Server.");
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

    // method to receive a generic object.
    private Object receive() 
    {
		Object o = null;
		try {
			System.out.println("03. -- About to receive an object...");
		    o = is.readObject();
		    System.out.println("04. <- Object received...");
		} 
	    catch (Exception e) {
		    System.out.println("XX. Exception Occurred on Receiving:" + e.toString());
		}
		return o;
    }

    public static void main(String args[]) 
    {
    	System.out.println("**. Java Client Application - EE402 OOP Module, DCU");
    	if(args.length>=1){
    
    		new Client(args[0],args[1]);
		    //theApp.getDate();
		}
    	else
    	{
    		System.out.println("Error: you must provide the address of the server");
    		System.out.println("Usage is:  java Client x.x.x.x  (e.g. java Client 192.168.7.2)");
    		System.out.println("      or:  java Client hostname (e.g. java Client localhost)");
    	}    
    	System.out.println("**. End of Application.");
    }

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) { System.exit(0);}

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
			System.out.println("here");
			if (!this.connectToServer(serverIP)) {
	    		System.out.println("XX. Failed to open socket connection to: " + serverIP);            
	    	}
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
		// TODO Auto-generated method stub
		
	}
	
}