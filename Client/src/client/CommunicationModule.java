
package client;

import java.net.InetAddress;


public class CommunicationModule {
    
    int messageType;
    int requestId;
    RemoteObjectRef objectRef;
    int methodId;
    byte[] args;
    
    public byte[] doOperation(RemoteObjectRef o, int methodId, byte[] arguments){
        /* 
        * Chama host especificado pelo RemoteObjetcRef
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

}
