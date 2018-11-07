package antonio.sockets;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Cliente {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MarcoCliente mimarco=new MarcoCliente();

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}

class MarcoCliente extends JFrame {

    public MarcoCliente(){

        setBounds(600,300,280,350);

        LaminaMarcoCliente milamina=new LaminaMarcoCliente();

        add(milamina);

        setVisible(true);

        addWindowListener(new EnvioOnline());
    }

}
//-------------------------ENVÍA SEÑAL ONLINE-----------------------------------------------------

class EnvioOnline extends WindowAdapter{

    public void windowOpened(WindowEvent e){

        try {
            Socket misocket = new Socket("192.168.1.37",9999);
            PaqueteEnvio datos=new PaqueteEnvio();

            datos.setMensaje("online");

            ObjectOutputStream paquete_datos=new ObjectOutputStream(misocket.getOutputStream());
            paquete_datos.writeObject(datos);

            misocket.close();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
//-------------------------------------------------------------------------------------------------

class LaminaMarcoCliente extends JPanel implements Runnable {

    public LaminaMarcoCliente(){

        String nick_usuario=JOptionPane.showInputDialog("Nick: ");

        JLabel n_nick=new JLabel("Nick: ");
        add(n_nick);

        nick=new JLabel();
        nick.setText(nick_usuario);
        add(nick);

        ip=new JComboBox();
        ip.addItem("Usuario1");
        ip.addItem("Usuario2");
        ip.addItem("Usuario3");
        ip.addItem("Usuario4");
        ip.addItem("Usuario5");
        ip.addItem("Usuario6");
        ip.addItem("Usuario7");
        ip.addItem("Usuario8");
        ip.addItem("Usuario9");
        ip.addItem("Usuario10");
        add(ip);

        JLabel texto=new JLabel("Online: ");
        add(texto);

        campochat=new JTextArea(12,20);
        add(campochat);

        campo1=new JTextField(20);

        add(campo1);

        miboton=new JButton("Enviar");

        EnviaTexto mievento = new EnviaTexto();
        miboton.addActionListener(mievento);

        add(miboton);




        Thread mihilo=new Thread(this);
        mihilo.start();
    }

    @Override
    public void run() {

        try {
            ServerSocket servidor_cliente=new ServerSocket(9090);

            Socket cliente;

            PaqueteEnvio paqueteRecibido;

            while(true){

                cliente=servidor_cliente.accept();

                ObjectInputStream flujoentrada=new ObjectInputStream(cliente.getInputStream());

                try {
                    paqueteRecibido=(PaqueteEnvio) flujoentrada.readObject();

                    campochat.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje());

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Clase que envía texto
    private class EnviaTexto implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            campochat.append("\n" + campo1.getText());

            try {
                Socket misocket = new Socket("192.168.1.37",9999);

                PaqueteEnvio datos = new PaqueteEnvio();

                datos.setNick(nick.getText());
                datos.setIp(ip.getSelectedItem().toString());
                datos.setMensaje(campo1.getText());

                ObjectOutputStream paquete_datos=new ObjectOutputStream(misocket.getOutputStream());
                paquete_datos.writeObject(datos);

                misocket.close();



            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        }
    }

    private JTextField campo1;

    private JComboBox ip;

    private JLabel nick;

    private JButton miboton;

    private JTextArea campochat;

}

class PaqueteEnvio implements Serializable {

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    private String nick, ip, mensaje;
}