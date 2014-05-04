package server;

import static com.sun.org.apache.xml.internal.serialize.OutputFormat.Defaults.Encoding;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Controller {

    private ArrayList<Object> objList;
    private Calculator calc;
    private CommunicationModule communication;

    public Controller() {
        objList = new ArrayList<Object>(20);
        calc = new Calculator();

        communication = new CommunicationModule();
    }

    public void run() throws UnknownHostException {
        objList.add(calc);
        long time = Calendar.getInstance().getTimeInMillis();
        RemoteObjectRef ref = new RemoteObjectRef("localhost", 2020, time, objList.size() - 1, objList.get(objList.size() - 1));
        // send to client

        // byte[] b = communication.getRequest()
        // communication = communication.toCommunicationModule(b);
        // runMethod(communication.getObjectRef(), communication.getMethodId(), communication.getArgs());
        
        // testing
        runMethod(ref, 0, communication.getArgs());
    }

    private void runMethod(RemoteObjectRef ref, int methodId, byte[] byteArgs) {
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
            System.out.println(m.invoke(objList.get(ref.getObjNumber()), objArgs));
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
