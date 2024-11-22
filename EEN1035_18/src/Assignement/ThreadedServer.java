/* The Date Server Class - Written by Derek Molloy for the EE402 Module
 * See: ee402.eeng.dcu.ie
 */

package Assignement;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

@SuppressWarnings("serial")
public class ThreadedServer extends JFrame implements ActionListener, WindowListener, Runnable 
{
	private static int portNumber = 5050;
	boolean listening = true;
    ServerSocket serverSocket = null;
    private MyCanvas plot = new MyCanvas();
    private JButton start;
    private Thread thread;
    private JList<String> ClientList;
	public ThreadedServer() {
		JPanel hp = new JPanel();
		hp.setLayout(new BoxLayout(hp, BoxLayout.Y_AXIS));
		JPanel p;
		this.getContentPane().add(hp);
		p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(plot);
		hp.add(p, BorderLayout.CENTER);
		p = new JPanel();
		p.setLayout(new FlowLayout());
		start = new JButton("run the server");
		this.start.addActionListener(this);
		p.add(start);
		hp.add(p);
		this.pack();
		this.addWindowListener(this);
		this.setVisible(true);
		this.thread = new Thread(this);
		
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
            
            ThreadedConnectionHandler con = new ThreadedConnectionHandler(clientSocket);
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
	

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) { closeSocket(); System.exit(0);}

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
		
	} 
}
