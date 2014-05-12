/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import rmipacket.RemoteObjectRef;
import rmipacket.Message;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {

    private Message message;
    private final int clientListenPort = 2020;
    private byte[] buffer;
    private RemoteObjectRef remoteObject;
    private GUI view;

    public Controller(GUI view) {
        message = new Message();
        buffer = new byte[9000];
        this.view = view;
    }

    private void resetBuffer() {
        buffer = new byte[9000];
    }

    private RemoteObjectRef getRemoteRef() {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(clientListenPort);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(packet);
            buffer = packet.getData();

            RemoteObjectRef remoteObject = new RemoteObjectRef();
            remoteObject = remoteObject.toRemoteObjectRef(buffer);

            datagramSocket.close();
            return remoteObject;
        } catch (SocketException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Object convertReply(String s, Class<?> type) {
        if (type.equals(Double.TYPE)) {
            return Double.parseDouble(s);
        } else if (type.equals(Integer.TYPE)) {
            return Integer.parseInt(s);
        } else if (type.equals(Boolean.TYPE)) {
            return Boolean.parseBoolean(s);
        } else {
            return s;
        }
    }

    public void start() {
        remoteObject = getRemoteRef();
        
        String methodList[] = remoteObject.getRemoteInterface().getMethodsName();
        String list = "";
        String s;
        Class<?>[] c;
        
        /* Se descomentar esse treco, o retorno do remoteObject.getRemoteInterface().getMethodsName() muda e as requisições não funcionam mais*/
//        for (int i = 0; i < methodList.length; i++) {
//            s = "";
//            c = remoteObject.getRemoteInterface().getArgsType(i);
//            int lenght = c.length;
//            for (int j = 0; j < lenght; j++) {
//                s += c[j].getSimpleName();
//                if (j != lenght - 1) {
//                    s += ",";
//                }
//            }
//            methodList[i] += "("+s+")" +" "+ remoteObject.getRemoteInterface().getReturnType(i).getSimpleName();
//            list += methodList[i] + "\n";
//        }
//
//        view.showMethods(list);
    }

    public String runRemote(String method, String args) {
        
        Object answer = null;
        String reply;

        String[] methodName = remoteObject.getRemoteInterface().getMethodsName();
        for (String c : methodName) {
            System.out.println("\t" + c);
        }
        
        int methodId;
        for (methodId = 0; methodId < methodName.length; methodId++) {
            System.out.println(method +" "+ methodName[methodId]);
            if (method.equals(methodName[methodId])) {
                break;
            }
        }

        try {
            byte[] byteArgs = args.getBytes();
            
            buffer = message.doOperation(remoteObject, methodId, byteArgs);
            reply = new String(buffer);

            Class<?> c = remoteObject.getRemoteInterface().getReturnType(methodId);
            answer = convertReply(reply, c);
            view.writeAnswer("> Resultado é um " + c.getSimpleName() + " com o valor = " + answer.toString());
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return args;
    }
}
