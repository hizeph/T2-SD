
package rmipacket;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hizeph
 */
public class RemoteObjectInterface implements Serializable {
    
    protected String[] methodName;
    protected Class<?>[][] args;
    
    public RemoteObjectInterface(Object obj){
        Method[] methods = obj.getClass().getMethods();
        int size = methods.length;
        methodName = new String[size];
        args = new Class<?>[size][];
        int i = 0;
        for(Method m : methods){
            methodName[i] = m.getName();
            args[i] = m.getParameterTypes();
            i++;
        }
    }
    public String[] getMethodsName(){
        return methodName;
    }
    
    protected Method getMethod(Object obj, int methodNumber){
        try {
            Method m = obj.getClass().getMethod(methodName[methodNumber], args[methodNumber]);
            return m;
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(RemoteObjectInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(RemoteObjectInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
   
}
