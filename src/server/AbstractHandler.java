/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public abstract class AbstractHandler implements Runnable{
    protected ObjectInputStream inputObj;
    protected ObjectOutputStream outputObj;
    protected DataInputStream inputData;
    protected DataOutputStream outputData;
    protected ServerMainFrame serverMainFrame;
    protected Socket connection;
    protected Server server;
    boolean dead = false;
    
    public AbstractHandler(Socket connection, Server server, ServerMainFrame serverMainFrame){
        this.connection = connection;
        this.server = server;
        this.serverMainFrame = serverMainFrame;
    }

    protected abstract void processConnection() throws IOException;

    protected void getDataOutPutStream() throws IOException{
        outputData = new DataOutputStream(connection.getOutputStream());
    }
    
    protected void getDataInPutStream() throws IOException{
        inputData = new DataInputStream(connection.getInputStream());
    }
    
    protected void getObjectOutPutStream() throws IOException{
        outputObj = new ObjectOutputStream(connection.getOutputStream());
    }
    
    protected void getObjectInPutStream() throws IOException{
        inputObj = new ObjectInputStream(connection.getInputStream());
    }  
    
    public void sendBoolean(Boolean bool){
        try {
            outputData.writeBoolean(bool);
            outputData.flush();
        } catch (Exception e) {
            dead = true;
            this.serverMainFrame.printMsb("\nUser " + this.connection.getInetAddress() + " terminated connection!");
        }
    }
    
    public void sendMsg(String msg){
        try {
            outputData.writeUTF(msg);
            outputData.flush();
        } catch (Exception e) {
            dead = true;
            this.serverMainFrame.printMsb("\nUser " + this.connection.getInetAddress() + " terminated connection!");
        }
    }
    
    public void sendInt(int value){
        try {
            outputData.writeInt(value);
            outputData.flush();
        } catch (Exception e) {
            dead = true;
            this.serverMainFrame.printMsb("\nUser " + this.connection.getInetAddress() + " terminated connection!");
        }
    }
    
    public void sendObject(Object obj){
        try {
            outputObj.writeObject(obj);
            outputObj.flush();
        } catch (IOException ex) {            
            dead = true;
            this.serverMainFrame.printMsb("\nUser " + this.connection.getInetAddress() + " terminated connection!");
        }
    }
    
    public Object readObject() {
        Object obj = null;
        try {
            obj = inputObj.readObject();
        } catch (IOException e) {            
            this.serverMainFrame.printMsb("\nUser " + this.connection.getInetAddress() + " terminated connection!");
        } catch (ClassNotFoundException e) {
            this.serverMainFrame.printMsb("\nUser " + this.connection.getInetAddress() + " terminated connection!");
        }

        return obj;
    }
    
    public boolean readBoolean(){
        boolean bool = false;
        try {
            bool = inputData.readBoolean();
        } catch (IOException ex) {
            dead = true;
            this.serverMainFrame.printMsb("\nUser " + this.connection.getInetAddress() + " terminated connection!");
        }
        return bool;
    }
    
    public String readMsg(){        
        try {
            return inputData.readUTF();
        } catch (IOException ex) {
            dead = true;
            this.serverMainFrame.printMsb("\nUser " + this.connection.getInetAddress() + " terminated connection!");
            return null;
        }        
    }
    
    public int readInt(){
        int value = -1;
        try {
            value = inputData.readInt();
        } catch (IOException ex) {
            dead = true;
            this.serverMainFrame.printMsb("\nUser " + this.connection.getInetAddress() + " terminated connection!");
        }
        return value;
    }
    
    public void close() throws IOException{
        dead = true;
        if(inputData != null)
            inputData.close();
        if(outputData != null)
            outputData.close();
        if(inputObj != null)
            inputObj.close();
        if(outputObj != null)
            outputObj.close();
        if(connection != null)
            connection.close();
    }
}
