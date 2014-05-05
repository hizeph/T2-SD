package rmipacket;

import server.Controller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Message implements Serializable {

    private int messageType;
    static private int requestId = 0;
    private RemoteObjectRef objectRef;
    private int methodId;
    private byte[] args;

    public Message() {
        this.requestId++;
        this.messageType = 0;
    }

    public RemoteObjectRef getObjectRef() {
        return objectRef;
    }

    public int getMethodId() {
        return methodId;
    }

    public byte[] getArgs() {
        return args;
    }

    public byte[] doOperation(RemoteObjectRef o, int methodId, byte[] arguments) throws IOException {

        this.objectRef = o;
        this.methodId = methodId;
        this.args = arguments;

        byte[] buffer = new byte[9000];
        buffer = this.toByte();
        
        DatagramSocket datagramSocket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, objectRef.getAddress(), objectRef.getPort());
        
        datagramSocket.send(packet);
        datagramSocket.close();
        return buffer;

    }

    public byte[] getRequest() {
        byte[] b = new byte[1];
        // readDatagramPacket(b);
        return b;
    }

    public void sendReply(byte[] reply, InetAddress clientHost, int clientPort) throws SocketException, IOException {
        
        DatagramPacket reply_double = new DatagramPacket(
                reply, reply.length, clientHost, clientPort);
            DatagramSocket datagramSocket = new DatagramSocket(clientPort);
            datagramSocket.send(reply_double);
            datagramSocket.close();
    }

    

    public byte[] toByte() {
        ObjectOutputStream os = null;
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            os = new ObjectOutputStream(byteStream);
            os.flush();
            os.writeObject(this);
            os.flush();
            return byteStream.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(RemoteObjectRef.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(RemoteObjectRef.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public Message toMessage(byte[] b) {
        ObjectInputStream os = null;
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(b);
            os = new ObjectInputStream(byteStream);
            return (Message) os.readObject();

        } catch (IOException ex) {
            Logger.getLogger(RemoteObjectRef.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteObjectRef.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(RemoteObjectRef.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

}
