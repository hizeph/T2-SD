/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import classeRmi.Message;
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

        communication = new Message();
    }

    public void run() throws SocketException, IOException {
        System.out.println("Cliente rodando, aguardando referencia");
        /*recebe referencia do objeto remoto do servidor*/
        DatagramSocket datagramSocket = new DatagramSocket(2020);

        byte[] buffer = new byte[880];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        datagramSocket.receive(packet);

        System.out.println("Tamanho packet: " + packet.getLength());
        byte[] ror = packet.getData();
        System.out.println("Tamanho ror: " + ror.length);

        RemoteObjectRef object = new RemoteObjectRef();
        object = object.toRemoteObjectRef(packet.getData());

        System.out.println("/n Recebido objeto /n");


        String[] methodName = object.remoteInterface.methodName;
        System.out.println("Argumentos: ");

        for (String c : methodName) {
            System.out.println("/t " + c);
        }
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o metodo que deseja utilizar");
        String pedido = sc.nextLine();

    }
}
