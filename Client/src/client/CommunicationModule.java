
package client;

import java.net.InetAddress;
import java.lang.reflect.Method;


public class CommunicationModule {
    
    int messageType;
    int requestId;
    RemoteObjectRef objectRef;
    Method methodId;
    byte[] args;
    
    public byte[] doOperation(RemoteObjectRef o, Method methodId, byte[] arguments){
        /* 
        * Chama host especificado pelo RemoteObjetcRef - como o RemoteObjectRef recebe os dados do host?
        * Bloqueia esperando resposta
        *  
        */
        byte[] b = new byte[1];
        return b;
    }
    
    public byte[] getRequest(){
        byte[] b = new byte[1];
        return b;
    }
    
    public void sendReply(byte[] reply, InetAddress clientHost, int clientPort){
        
    }
    
/*   public byte[] toByte(){
        
    }
    
    public CommunicationModule toObject(byte[] b){
       int i = b[0 -> Integer.size]
       int i2 = b[Integer.size+1 -> Integer.size]
       return new CommunicationModule(i,i2....)
    }
*/
    
}
