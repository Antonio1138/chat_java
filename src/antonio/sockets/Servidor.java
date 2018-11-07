package antonio.sockets;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MarcoServidor mimarco=new MarcoServidor();

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}

class MarcoServidor extends JFrame implements Runnable{

    public MarcoServidor(){

        setBounds(1200,300,280,350);

        JPanel milamina= new JPanel();

        milamina.setLayout(new BorderLayout());

        areatexto=new JTextArea();

        milamina.add(areatexto,BorderLayout.CENTER);

        add(milamina);

        setVisible(true);



        Thread mihilo=new Thread(this);
        mihilo.start();

    }



    @Override
    public void run() {
        try {
            //Con esta clase le decimos que nos abra el puerto 9999
            ServerSocket servidor=new ServerSocket(9999);

            String nick, ip, mensaje;

            PaqueteEnvio paquete_recibido;

            while(true) {

                //le decimos que acepte las conexiones
                Socket misocket = servidor.accept();



                ObjectInputStream paquete_datos=new ObjectInputStream(misocket.getInputStream());

                try {
                    paquete_recibido=(PaqueteEnvio) paquete_datos.readObject();

                    nick = paquete_recibido.getNick();
                    ip = paquete_recibido.getIp();
                    mensaje = paquete_recibido.getMensaje();

                    if(!mensaje.equals("online")) {

                        areatexto.append("\n" + nick + ": " + mensaje + " para " + ip);

                        //-------------------------------Puente--------------------------------------------

                        Socket enviaDestinatario = new Socket(ip, 9090);
                        ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
                        paqueteReenvio.writeObject(paquete_recibido);


                        paqueteReenvio.close();
                        enviaDestinatario.close();
                    } else {
                        //-----------------------------DETECTA USUARIOS ONLINE----------------------------

                        InetAddress localizacion=misocket.getInetAddress();
                        String ipRemota = localizacion.getHostAddress();


                        //--------------------------------------------------------------------------------
                    }



                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                misocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private	JTextArea areatexto;
}

