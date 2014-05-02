
package server;

import java.net.InetAddress;


public class CommunicationModule {
    
    /*
    ***********************************************************
    **************** Copiar do cliente ************************
    ***********************************************************
    */
    
    public byte[] doOperation(RemoteObjectRef o, int methodId, byte[] arguments){
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
