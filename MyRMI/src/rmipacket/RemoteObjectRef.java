package rmipacket;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteObjectRef implements Serializable {

    private InetAddress ip;
    private int port;
    private float time;
    private int objectNumber;
    public RemoteObjectInterface remoteInterface;

    public RemoteObjectRef() {

    }

    public RemoteObjectRef(String host, int port, float time, int objectNumber, Object remoteInterface) throws UnknownHostException {
        this.ip = InetAddress.getByName(host);
        this.port = port;
        this.time = time;
        this.objectNumber = objectNumber;
        this.remoteInterface = new RemoteObjectInterface(remoteInterface);
    }

    public Method getMethod(Object obj, int methodNumber) {
        return remoteInterface.getMethod(obj, methodNumber);
    }

    public int getObjNumber() {
        return objectNumber;
    }

    public RemoteObjectInterface getRemoteInterface() {
        return remoteInterface;
    }

    public InetAddress getAddress() throws UnknownHostException {
        return ip;
    }

    public int getPort() {
        return port;
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

    public RemoteObjectRef toRemoteObjectRef(byte[] b) {
        ObjectInputStream os = null;
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(b);
            os = new ObjectInputStream(byteStream);
            return (RemoteObjectRef) os.readObject();

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
