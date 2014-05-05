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
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {

    private ArrayList<Object> objList;
    private Message communication;

    public Controller() {
        objList = new ArrayList<Object>(20);
    }

    public void run() throws SocketException, IOException {
        System.out.println("Cliente rodando, aguardando referencia");
        /*recebe referencia do objeto remoto do servidor*/
        DatagramSocket datagramSocket = new DatagramSocket(2020);

        byte[] buffer = new byte[1000];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        datagramSocket.receive(packet);

        System.out.println("Tamanho packet: " + packet.getLength());
        byte[] ror = packet.getData();
        System.out.println("Tamanho ror: " + ror.length);

        RemoteObjectRef object = new RemoteObjectRef();
        RemoteObjectRef remoteObject = object.toRemoteObjectRef(ror);

        System.out.println("Recebido objeto");

        String[] methodName = remoteObject.getRemoteInterface().getMethodsName();
        System.out.println("Argumentos: ");

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
        System.out.println("Numero do metodo: " + i);
        Double n1, n2;
        System.out.println("Digite o primeiro numero");
        n1 = sc.nextDouble();
        System.out.println("Digite o segundo numero");
        n2 = sc.nextDouble();

        byte[] byteArgs;

        byteArgs = new byte[Double.SIZE * 2];
        String s = Double.toString(n1);
        s += "," + Double.toString(n2);
        System.out.println(s);
        byteArgs = s.getBytes();

        communication = new Message(remoteObject, i, byteArgs);
        communication.doOperation();
    }
}
