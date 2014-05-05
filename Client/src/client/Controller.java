/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import rmipacket.RemoteObjectRef;
import rmipacket.Message;
import rmipacket.RemoteObjectInterface;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {

    private ArrayList<Object> objList;
    private Message message;
    private final int clientListenPort = 2020;
    private byte[] buffer;

    public Controller() {
        objList = new ArrayList<Object>(20);
        message = new Message();
        buffer = new byte[9000];
    }

    private void resetBuffer() {
        buffer = new byte[9000];
    }

    private RemoteObjectRef getRemoteRef() {
        try {
            /*recebe referencia do objeto remoto do servidor*/
            DatagramSocket datagramSocket = new DatagramSocket(clientListenPort);

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            datagramSocket.receive(packet);

            //System.out.println("Tamanho packet: " + packet.getLength());
            buffer = packet.getData();
            //System.out.println("Tamanho ror: " + ror.length);

            RemoteObjectRef remoteObject = new RemoteObjectRef();
            remoteObject = remoteObject.toRemoteObjectRef(buffer);

            datagramSocket.close();
            return remoteObject;
            //System.out.println("Recebido objeto");
        } catch (SocketException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private Object convertReply(String s, Class<?> type){
        if(type.equals(Double.TYPE)){
            return Double.parseDouble(s);
        } else if (type.equals(Integer.TYPE)){
            return Integer.parseInt(s);
        } else if (type.equals(Boolean.TYPE)){
            return Boolean.parseBoolean(s);
        } else {
            return s;
        }
    }

    public void run() {

        System.out.println("Cliente rodando, aguardando referencia");
        
        RemoteObjectRef remoteObject = getRemoteRef();
        
        String[] methodName = remoteObject.getRemoteInterface().getMethodsName();
        for (String c : methodName) {
            System.out.println("\t" + c);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o metodo que deseja utilizar");
        String requestMethod = sc.nextLine();
        int i;
        for (i = 0; i < methodName.length; i++) {
            if (requestMethod.equals(methodName[i])) {
                break;
            }
        }
        int methodId = i;
        System.out.println("Numero do metodo: " + i);
        Double n1, n2;
        System.out.println("Digite o primeiro numero");
        n1 = 8.0;//sc.nextDouble();
        System.out.println("Digite o segundo numero");
        n2 = 4.0;//sc.nextDouble();
        byte[] byteArgs;
        byteArgs = new byte[Double.SIZE * 2];
        String s = String.valueOf(n1);
        s += "," + String.valueOf(n2);
        byteArgs = s.getBytes();

        try {
            buffer = message.doOperation(remoteObject, methodId, byteArgs);
            Object answer=null;
            s = new String(buffer);
            Class<?> c = remoteObject.getRemoteInterface().getReturnType(methodId);
            answer = convertReply(s,c);
            
            System.out.println("Result is a "+c.getSimpleName()+" with value = "+answer.toString());
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
