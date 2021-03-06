package rmipacket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Message implements Serializable {

    private int messageType;
    static private int requestId = 0;
    private RemoteObjectRef objectRef;
    private int methodId;
    private byte[] args;

    public Message(int requestId) {
        // request
        this.requestId = requestId;
        this.messageType = 0;
    }

    public Message() {
        // reply
        this.messageType = 1;
    }

    public RemoteObjectRef getObjectRef() {
        return objectRef;
    }

    public int getMethodId() {
        return methodId;
    }

    public byte[] getArgs() {
        return args;
    }

    /*
    * Esse método é utilizado pelo usuário (cliente) que utiliza um objeto remoto 
    * para invocar um método codificado por outro usuário (servidor)
    * Os argumentos são empacotados no objeto Mensagem e serializados,
    * então são enviados via UDP para o servidor e espera a resposta via UDP também.
    */
    public byte[] doOperation(RemoteObjectRef remoteObj, int methodId, byte[] arguments) throws IOException {
        this.objectRef = remoteObj;
        this.methodId = methodId;
        this.args = arguments;

        byte[] buffer = new byte[9000];
        byte[] bufferIn = new byte[9000];

        buffer = toByte(this);

        DatagramSocket datagramSocket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, remoteObj.getAddress(), remoteObj.getPort());
        datagramSocket.send(packet);
        datagramSocket.close();

        datagramSocket = new DatagramSocket(2020);

        packet = new DatagramPacket(bufferIn, buffer.length);
        datagramSocket.receive(packet);
        bufferIn = packet.getData();
        datagramSocket.close();

        return bufferIn;
    }

    /*
    * Este método é utilizado pelo servidor para receber um objeto Mensagem do cliente via UDP.
    * Retorna o objeto Mensagem serializado enviado pelo cliente.
    */
    public byte[] getRequest() {
        try {

            DatagramSocket datagramSocket = new DatagramSocket(2021);
            System.out.println("Aguardando pedido do cliente");
            byte[] buffer = new byte[9000];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            datagramSocket.receive(packet);
            buffer = packet.getData();

            datagramSocket.close();

            return buffer;

        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /*
    * Este método é utilizado pelo servidor para enviar o retorno do método executado pra o cliente que o requisitou
    */
    public void sendReply(byte[] reply, InetAddress clientHost, int clientPort) throws SocketException, IOException {

        DatagramPacket packet = new DatagramPacket(
                reply, reply.length, clientHost, clientPort);
        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.send(packet);
        datagramSocket.close();
    }

    public static byte[] toByte(Message m) {
        ObjectOutputStream os = null;
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            os = new ObjectOutputStream(byteStream);
            os.flush();
            os.writeObject(m);
            os.flush();
            return byteStream.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(RemoteObjectRef.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(RemoteObjectRef.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static Message toMessage(byte[] b) {
        ObjectInputStream os = null;
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(b);
            os = new ObjectInputStream(byteStream);
            return (Message) os.readObject();

        } catch (IOException ex) {
            Logger.getLogger(RemoteObjectRef.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RemoteObjectRef.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(RemoteObjectRef.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

}
