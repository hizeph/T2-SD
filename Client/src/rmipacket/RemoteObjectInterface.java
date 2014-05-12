package rmipacket;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteObjectInterface implements Serializable {
    
    protected String[] methodName;
    protected Class<?>[][] args;
    protected Class<?>[] returnType;
    
    public RemoteObjectInterface(Object obj){
        Method[] methods = obj.getClass().getDeclaredMethods();
        int size = methods.length;
        methodName = new String[size];
        args = new Class<?>[size][];
        returnType = new Class<?>[size];
        int i = 0;
        for(Method m : methods){
            methodName[i] = m.getName();
            args[i] = m.getParameterTypes();
            returnType[i] = m.getReturnType();
            i++;
        }
    }
    public String[] getMethodsName(){
        return methodName;
    }
    
    public Class<?> getReturnType(int id){
        return returnType[id];
    }
    
    public Class<?>[] getArgsType(int id){
        return args[id];
    }
    
    public Method getMethod(Object obj, int methodNumber){
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
