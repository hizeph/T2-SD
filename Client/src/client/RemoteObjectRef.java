
package client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

class RemoteObjInterface implements Serializable {
    
    protected String[] methodName;
    protected Class<?>[][] args;
    
    public RemoteObjInterface(Method[] methods){
        int size = methods.length;
        methodName = new String[size];
        args = new Class<?>[size][];
        int i = 0;
        for(Method m : methods){
            methodName[i] = m.getName();
            args[i] = m.getParameterTypes();
        }
    }
    
    protected Object runMethod(Object obj, int i){
        try {
            Method m = obj.getClass().getMethod(methodName[i], args[i]);
            
            
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(RemoteObjInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(RemoteObjInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
   
}


public class RemoteObjectRef implements Serializable {
    
    private InetAddress ip;
    private int port;
    private double time;
    private int objectNumber;
    private RemoteObjInterface remoteInterface;
    
    public RemoteObjectRef(){
        
    }

    public RemoteObjectRef(InetAddress ip, int port, double time, int objectNumber, RemoteObjInterface remoteInterface){
        this.ip = ip;
        this.port = port;
        this.time = time;
        this.objectNumber = objectNumber;
        this.remoteInterface = remoteInterface;
    }
   
    
    public byte[] toByte(){
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
        } finally{
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
