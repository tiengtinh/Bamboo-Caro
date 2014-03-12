/**
 *
 */
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Ronny Nguyen
 *
 */
public class Client {

    private Socket socket;
    private DataOutputStream outData;
    private ObjectOutputStream outObj;
    private ObjectInputStream inObj;
    private DataInputStream inData;

    public Client contactServer(String host, int port) throws UnknownHostException, IOException {
        socket = new Socket(host, port);
        return this;
    }

    public void connectDataInputStream() throws IOException {
        if (socket != null) {
            inData = new DataInputStream(socket.getInputStream());
        }
    }

    public void connectObjOutputStream() throws IOException {
        if (socket != null) {
            outObj = new ObjectOutputStream(socket.getOutputStream());
        }
    }

    public void connectObjInputStream() throws IOException {
        if (socket != null) {
            inObj = new ObjectInputStream(socket.getInputStream());
        }
    }

    public void connectDataOutputStream() throws IOException {
        if (socket != null) {
            outData = new DataOutputStream(socket.getOutputStream());
        }
    }

    public void closeConnection() throws Exception {
        if (inData != null) {
            inData.close();
        }

        if (outData != null) {
            outData.close();
        }

        if (outObj != null) {
            outObj.close();
        }

        if (socket != null) {
            socket.close();
        }

        Thread.sleep(100);
    }

    public void sendMessage(String ms) throws IOException {
        outData.writeUTF(ms);
        outData.flush();
    }

    public String readMessage() throws IOException {
        return inData.readUTF();
    }

    public boolean readBoolean() throws IOException {
        return inData.readBoolean();
    }

    public void sendObject(Object obj) throws IOException {
        outObj.writeObject(obj);
        outObj.flush();
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return inObj.readObject();
    }

    public void sendInt(int i) throws IOException {
        outData.writeInt(i);
        outData.flush();
    }

    public int readInt() throws IOException {
        return inData.readInt();
    }
}
