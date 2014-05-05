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
    private Message communication;
    private final int serverListenPort = 2021;
    private final int clientListenPort = 2020;

    public Controller() {
        objList = new ArrayList<Object>(20);
        calc = new Calculator();

        communication = new Message();
    }

    public void run() throws UnknownHostException, IOException {
        objList.add(calc);
        long time = Calendar.getInstance().getTimeInMillis();
        RemoteObjectRef ref = new RemoteObjectRef("localhost", serverListenPort, time, objList.size() - 1, objList.get(objList.size() - 1));

        byte[] bufferOut = ref.toByte();

        System.out.println("Tamanho ror: " + bufferOut.length);
        InetAddress address = InetAddress.getByName("localhost");
//  envia objeto remoto
        DatagramPacket remoteObject = new DatagramPacket(
                bufferOut, bufferOut.length, address, clientListenPort);

        try {
            DatagramSocket datagramSocket = new DatagramSocket(serverListenPort);
            datagramSocket.send(remoteObject);
            System.out.println("Aguardando pedido do cliente");
            byte[] bufferIn = new byte[9000];
            DatagramPacket packet = new DatagramPacket(bufferIn, bufferIn.length);

            datagramSocket.receive(packet);
            bufferIn = packet.getData();
            Message request = new Message();

            Message request2 = request.toMessage(bufferIn);
            this.runMethod(request2.getObjectRef(), request2.getMethodId(), request2.getArgs());

        } catch (SocketException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

//        RemoteObjectRef ror = ref.toRemoteObjectRef(buffer);
//        
//        System.out.println(ror.getObjNumber());
        // byte[] b = communication.getRequest()
        // communication = communication.toCommunicationModule(b);
        // runMethod(communication.getObjectRef(), communication.getMethodId(), communication.getArgs());
        // testing
        //runMethod(ref, 0, communication.getArgs());
    }

    private void runMethod(RemoteObjectRef ref, int methodId, byte[] byteArgs) throws UnknownHostException, IOException {
        System.out.println("Run Method " + methodId);
        int i = 0;
        Method m = ref.getMethod(objList.get(ref.getObjNumber()), methodId);
//        byte[] byteArgs;
//        
//        byteArgs = new byte[Double.SIZE * 2];
//        String s = Double.toString(8);
//        s += ","+Double.toString(5);
//        System.out.println(s);
//        String convert = s;
//
//        byteArgs = s.getBytes();

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
            double result;
            System.out.println(m.invoke(objList.get(ref.getObjNumber()), objArgs));
            result = (double) m.invoke(objList.get(ref.getObjNumber()), objArgs);
            byte[] msg_reply = toByteArray(result);
            InetAddress address = InetAddress.getByName("localhost");

            communication.sendReply(msg_reply, address, clientListenPort);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] toByteArray(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    public double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }
}
