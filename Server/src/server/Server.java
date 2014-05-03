package server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

class Controller {

    private ArrayList<Object> objList;
    private Calculator calc;

    public Controller() {
        objList = new ArrayList<Object>(20);
        calc = new Calculator();
    }

    protected void run() throws UnknownHostException {
        objList.add(calc);
        long time = Calendar.getInstance().getTimeInMillis();
        RemoteObjectRef ref = new RemoteObjectRef("localhost", 2020, time, objList.size() - 1, objList.get(objList.size() - 1));
        // send to client
        

        // testing
        //runMethod(ref);

    }

    private void runMethod(RemoteObjectRef ref) {

//        Method m = ref.getMethod(objList.get(ref.getObjNumber()), 0);
//        System.out.println(m.toGenericString());
//        Object[] o = new Object[2];
//        o[0] = new Integer(5);
//        o[1] = new Integer(8);
//        try {
//            System.out.println(m.invoke(objList.get(ref.getObjNumber()), o));
//        } catch (IllegalAccessException ex) {
//            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalArgumentException ex) {
//            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InvocationTargetException ex) {
//            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}

public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Controller controller = new Controller();
        try {
            controller.run();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
