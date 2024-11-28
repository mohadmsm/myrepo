/* The Connection Handler Class - Written by Derek Molloy for the EE402 Module
 * See: ee402.eeng.dcu.ie
 */

package Assignement;

import java.net.*;
import java.util.*;
import java.io.*;

public class ThreadedConnectionHandler extends Thread
{
    private Socket clientSocket = null;				// Client socket object
    private ObjectInputStream is = null;			// Input stream
    private ObjectOutputStream os = null;			// Output stream
    static List<Stack<SensorObject>> sensorStacksList = new ArrayList<>();
    private ThreadedServer server;
	// The constructor for the connection handler
    public ThreadedConnectionHandler(Socket clientSocket, ThreadedServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    // Will eventually be the thread execution method - can't pass the exception back
    public void run() {
         try {
            this.is = new ObjectInputStream(clientSocket.getInputStream());
            this.os = new ObjectOutputStream(clientSocket.getOutputStream());
            while (this.readCommand()) {}
         } 
         catch (IOException e) 
         {
        	System.out.println("XX. There was a problem with the Input/Output Communication:");
            e.printStackTrace();
         }
    }

    // Receive and process incoming string commands from client socket 
    private synchronized boolean readCommand() {
       //Object s = null; //don't use this for the assignment use obj
        try {
            Object s =  is.readObject();
            if(s instanceof SensorObject) {
            	SensorObject data = (SensorObject) s;
            	data.setID(clientSocket.getPort());
            	if(data.getStatus() == false) {
            		System.out.println("Closing the Socket");
            		this.closeSocket();
            		Stack<SensorObject> stack = findOrCreateStack(data.getID());
            		if (sensorStacksList.indexOf(stack) == server.currentSelectedClient) {
            			server.disconnectClient();
            		}
            		sensorStacksList.remove(stack);
            		return false;
            	}
            	
                    Stack<SensorObject> stack = findOrCreateStack(data.getID());
                    if (stack.size()==10) {stack.remove(0);}
                    stack.push(data);
                    if (sensorStacksList.indexOf(stack) == server.currentSelectedClient) {
                        server.rePlot(stack);
                    }
                    else if(server.currentSelectedClient == 5) {
                    	server.FindAvg();
                    }
                    //server.findAvg(stack);
                            	
            } else {
                System.out.println("XX. Received an unknown object type.");	
            }
            System.out.println("-------------------------------------------");
        } 
        catch (Exception e){   // catch a general exception
            System.out.println("XX. Error in readCommand: " + e);
        	this.closeSocket();
            return false;
        }
        
        return true;
    }
    // don't change this even with objects 
    // Use our custom DateTimeService Class to get the date and time

    // Send a generic object back to the client 
    private void send(Object o) {
        try {
            System.out.println("02. -> Sending (" + o +") to the client.");
            this.os.writeObject(o);
            this.os.flush();
        } 
        catch (Exception e) {
            System.out.println("XX." + e.getStackTrace());
        }
    }
    private Stack<SensorObject> findOrCreateStack(int id) {
    	displaySensorStacks();
        for (Stack<SensorObject> stack : sensorStacksList) {
            if (!stack.isEmpty() && stack.peek().getID() == id) {
                return stack;
            }
        }
        // Create a new stack if not found
        Stack<SensorObject> newStack = new Stack<>();
        sensorStacksList.add(newStack);
        return newStack;
    }
    private void displaySensorStacks() {
        System.out.println("### Current Sensor Stacks ###");
        synchronized (sensorStacksList) {
            for (Stack<SensorObject> stack : sensorStacksList) {
                if (!stack.isEmpty()) {
                    SensorObject top = stack.peek(); // Get the top object to identify the stack
                    System.out.println("Stack for ID: " + top.getID());
                    for (SensorObject sensor : stack) {
                        System.out.println("    " + sensor);
                    }
                }
            }
        }
        System.out.println("##############################");
    }
    // Send a pre-formatted error message to the client 
    public void sendError(String message) { 
        this.send("Error:" + message);	//remember a String IS-A Object!
    }
     
    // Close the client socket 
    public void closeSocket() { //gracefully close the socket connection
        try {
            this.os.close();
            this.is.close();
            this.clientSocket.close();
        } 
        catch (Exception e) {
            System.out.println("XX. " + e.getStackTrace());
        }
    }
}