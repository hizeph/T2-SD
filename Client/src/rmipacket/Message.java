package rmipacket;

import client.Controller;
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

    public Message(RemoteObjectRef o, int methodId, byte[] arguments) {
        this.requestId++;
        this.messageType = 0;
        this.objectRef = o;
        this.methodId = methodId;
        this.args = arguments;
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

    public byte[] doOperation() throws IOException {
        /* 
         * Chama host especificado pelo RemoteObjetcRef - como o RemoteObjectRef recebe os dados do host?
         * Bloqueia esperando resposta
         */
        byte[] buffer = this.toByte();
        InetAddress address = InetAddress.getByName("localhost");

        DatagramPacket remoteObject = new DatagramPacket(
                buffer, buffer.length, address, 2020);

        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(remoteObject);

            System.out.println("aguardando...");
            //aguarda resposta

            byte[] bufferIn = new byte[1000];
            DatagramPacket packet = new DatagramPacket(bufferIn, bufferIn.length);

            datagramSocket.receive(packet);

            System.out.println("Resposta");
            System.out.println("Tamanho packet: " + packet.getLength());
            byte[] answare = packet.getData();
            return answare;
        } catch (SocketException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

//        return this.toByte();
        // datagramPacket(toByte());
        // this = toObject(b)
//        byte[] b = new byte[1];
//        return b;
        return buffer;
    }

    public byte[] getRequest() {
        byte[] b = new byte[1];
        // readDatagramPacket(b);
        return b;
    }

    public void sendReply(byte[] reply, InetAddress clientHost, int clientPort) {

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

    public Message toCommunicationModule(byte[] b) {
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
