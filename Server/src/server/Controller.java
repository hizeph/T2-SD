package server;

import rmipacket.Message;
import rmipacket.RemoteObjectRef;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {

    private ArrayList<Object> objList;
    private Calculator calc;
    private Message message;
    private final int serverListenPort = 2021;
    private final int clientListenPort = 2020;
    private byte[] buffer;

    public Controller() {
        objList = new ArrayList<Object>(20);
        calc = new Calculator();
        objList.add(calc);
        message = new Message();
        buffer = new byte[9000];
    }
    
   private void resetBuffer() {
        buffer = new byte[9000];
    }

    private void sendRemoteRef() {
        try {
            long time = Calendar.getInstance().getTimeInMillis();
            RemoteObjectRef ref = new RemoteObjectRef("localhost", serverListenPort, time, objList.size() - 1, objList.get(objList.size() - 1));
            
            byte[] bufferOut = ref.toByte();
            
            System.out.println("Tamanho ror: " + bufferOut.length);
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket remoteObject = new DatagramPacket(
                    bufferOut, bufferOut.length, address, clientListenPort);
            
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(remoteObject);
            datagramSocket.close();
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() throws UnknownHostException, IOException {
        
        sendRemoteRef();
        
        buffer = message.getRequest();

        Message request = Message.toMessage(buffer);
        
        runMethod(request.getObjectRef(), request.getMethodId(), request.getArgs());

    }

    private void runMethod(RemoteObjectRef ref, int methodId, byte[] byteArgs) throws UnknownHostException, IOException {
        System.out.println("Run Method " + methodId);
        int i = 0;
        Method m = ref.getMethod(objList.get(ref.getObjNumber()), methodId);

        String args = new String(byteArgs);
        System.out.println(args);
        String[] ss;
        ss = args.split(",");
        i = 0;
        Object[] objArgs = new Object[m.getParameterTypes().length];
        int srcStart = 0;
        for (Class<?> c : m.getParameterTypes()) {

            if (c.equals(Double.TYPE)) {
                objArgs[i] = Double.parseDouble(ss[i]);
            } else if (c.equals(Integer.TYPE)) {
                objArgs[i] = Integer.parseInt(ss[i]);
            }
            i++;
        }
        try {
            Object result;
            result =  m.invoke(objList.get(ref.getObjNumber()), objArgs);
            byte[] reply = String.valueOf(result).getBytes();
            
            InetAddress address = InetAddress.getByName("localhost");

            message.sendReply(reply, address, clientListenPort);
            
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
