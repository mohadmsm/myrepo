/* The Date Server Class - Written by Derek Molloy for the EE402 Module
 * See: ee402.eeng.dcu.ie
 */

package Assignement;
import java.net.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.Stack;

@SuppressWarnings("serial")
public class ThreadedServer extends JFrame implements ActionListener, WindowListener, Runnable 
{
	private static int portNumber = 5050;
	boolean listening = true;
    ServerSocket serverSocket = null;
    private MyCanvas plot = new MyCanvas();
    private JButton start;
    private Thread thread;
	private List  ClientList;
	int currentSelectedClient = -1;
	private JCheckBox  tempSwitch, soundSwitch, humiditySwitch;
	public ThreadedServer() {
		// Main panel with vertical layout to stack sections
	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	    this.getContentPane().add(mainPanel);
	    
	    // Section 1: Switch Panel
	    JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    switchPanel.setBorder(BorderFactory.createTitledBorder("Switches"));
	    tempSwitch = new JCheckBox ("Temp");
	    soundSwitch = new JCheckBox ("Sound");
	    humiditySwitch =new JCheckBox ("Humidity");
	    switchPanel.add(tempSwitch);
	    switchPanel.add(soundSwitch);
	    switchPanel.add(humiditySwitch);
	    mainPanel.add(switchPanel);
	    this.tempSwitch.addActionListener(this);
	    this.soundSwitch.addActionListener(this);
	    this.humiditySwitch.addActionListener(this);
	    // Section 2: Plot Panel
	    JPanel plotPanel = new JPanel(new BorderLayout());
	    plotPanel.setBorder(BorderFactory.createTitledBorder("Plot Section"));
	    plotPanel.add(plot, BorderLayout.CENTER);
	    mainPanel.add(plotPanel);
	    
	 // Section 3: Control Panel
	    JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    controlPanel.setBorder(BorderFactory.createTitledBorder("Server Control"));
	    start = new JButton("Run the Server");
	    this.start.addActionListener(this);
	    controlPanel.add(start);
	    mainPanel.add(controlPanel);

	    // Section 4: Client List Panel
	    JPanel clientPanel = new JPanel(new BorderLayout());
	    clientPanel.setBorder(BorderFactory.createTitledBorder("Client List"));
	    ClientList = new List(6, false);
	    for (int i = 0; i < 5; i++) {
	        ClientList.add("Client" + (i + 1));
	    }
	    ClientList.add("Average");
	    ClientList.addActionListener(e -> onClientSelected());
	    clientPanel.add(ClientList, BorderLayout.CENTER);
	    mainPanel.add(clientPanel);
	    this.pack();
	    this.setTitle("Threaded Server GUI");
	    this.setVisible(true);


	    this.thread = new Thread(this);
	    this.addWindowListener(this);
		
	}
	public void rePlot(Stack<SensorObject> stack) {
        plot.rePlot(stack); // Update canvas with new averages
    }
	
	public static void main(String args[]) {
		new ThreadedServer();
        
    }

	@Override
	public void run() {

		try 
        {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("New Server has started listening on port: " + portNumber );
        } 
        catch (IOException e) 
        {
            System.out.println("Cannot listen on port: " + portNumber + ", Exception: " + e);
            System.exit(1);
        }
        
        // Server is now listening for connections or would not get to this point
        while (listening) // almost infinite loop - loop once for each client request
        {
            Socket clientSocket = null;
            try{
            	System.out.println("**. Listening for a connection...");
                clientSocket = serverSocket.accept();//get stuck here until a client connect 
                System.out.println("00. <- Accepted socket connection from a client: ");
                System.out.println("    <- with address: " + clientSocket.getInetAddress().toString());
                System.out.println("    <- and port number: " + clientSocket.getPort());
            } 
            catch (IOException e){
                System.out.println("XX. Accept failed: " + portNumber + e);
                listening = false;   // end the loop - stop listening for further client requests
                closeSocket();
            }	
            
            ThreadedConnectionHandler con = new ThreadedConnectionHandler(clientSocket, this);
            con.start(); 
            System.out.println("02. -- Finished communicating with client:" + clientSocket.getInetAddress().toString());
        }
		
	}

	private void closeSocket() {
		try 
        {
            System.out.println("04. -- Closing down the server socket gracefully.");
            serverSocket.close();
        } 
        catch (IOException e) 
        {
            System.err.println("XX. Could not close server socket. " + e.getMessage());
        }
	}
	 private void onClientSelected() {
	        String selectedClient = ClientList.getSelectedItem();
	        if (selectedClient != null) {
	            try {
	                // Retrieve the index of the selected client
	                int index = ClientList.getSelectedIndex();
	                if (index >= 0 && index < ThreadedConnectionHandler.sensorStacksList.size()) {
	                    Stack<SensorObject> stack = ThreadedConnectionHandler.sensorStacksList.get(index);
	                    currentSelectedClient = index;
	                    plot.rePlot(stack); // Update the canvas with the selected client's averages
	                } else {
	                    plot.updateAverages(0, 0, 0); // No data for the selected client
	                }
	            } catch (Exception e) {
	                System.err.println("Error selecting client: " + e.getMessage());
	                plot.updateAverages(0, 0, 0);
	            }
	        }
	    }
	

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) { if (serverSocket!=null) {closeSocket(); }System.exit(0);}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(start)) {
			this.thread.start();
		}
		if (e.getSource().equals(tempSwitch) || e.getSource().equals(soundSwitch) || e.getSource().equals(humiditySwitch)) {
			 plot.setPlotVisibility(tempSwitch.isSelected(), soundSwitch.isSelected(), humiditySwitch.isSelected());      
		        
		    }
		
	} 
}
