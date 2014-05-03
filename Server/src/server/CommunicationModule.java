package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommunicationModule {

    private int messageType;
    private int requestId;
    private RemoteObjectRef objectRef;
    private int methodId;
    private byte[] args;

    public CommunicationModule() {

    }

    public byte[] doOperation(RemoteObjectRef o, Method methodId, byte[] arguments) {
        /* 
         * Chama host especificado pelo RemoteObjetcRef - como o RemoteObjectRef recebe os dados do host?
         * Bloqueia esperando resposta
         *  
         */

        // datagramPacket(toByte());
        // readDatagramPacket(b);
        // this = toObject(b)
        byte[] b = new byte[1];
        return b;
    }

    public byte[] getRequest() {
        byte[] b = new byte[1];
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

    public CommunicationModule toObject(byte[] b) {
        ObjectInputStream os = null;
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(b);
            os = new ObjectInputStream(byteStream);
            return (CommunicationModule) os.readObject();

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
