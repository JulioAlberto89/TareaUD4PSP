package Actividad_1;

import javax.swing.*;
import javax.swing.event.*;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ClienteFTP extends JFrame {

    private static final long serialVersionUID = 1L;

    // campos de cabecera parte superior
    static JTextField cab = new JTextField();
    static JTextField cab2 = new JTextField();
    static JTextField cab3 = new JTextField();

    // campos de mensajes parte inferior
    private static JTextField campo = new JTextField();
    private static JTextField campo2 = new JTextField();

    // botones
    JButton botonSalir = new JButton("Desconectar");

    // contenedor
    private final Container c = getContentPane();

    // Datos del servidor FTP
    static FTPClient cliente = new FTPClient();// cliente FTP
    String servidor = "localhost";
    String user;
    String pasw;
    boolean login;
    static String direcInicial = "/";
    static String rutaLog; // Ruta para el archivo LOG.txt
    
    static String direcSelec = direcInicial;

    // constructor
    public ClienteFTP() throws IOException {
        super("CLIENTE BÁSICO FTP");
        iniciarSesion();

        campo.setBounds(new Rectangle(3, 485, 485, 30));
        campo.setForeground(Color.blue);
        campo.setFont(new Font("Verdana", Font.BOLD, 12));
        campo.setText("<<   ARBOL DE DIRECTORIOS CONSTRUIDO    >>");
        campo2.setBounds(new Rectangle(3, 515, 485, 30));
        campo2.setForeground(Color.blue);
        campo2.setFont(new Font("Verdana", Font.BOLD, 12));
        campo2.setText(" ");

        cab.setBounds(new Rectangle(5, 5, 200, 30));
        cab.setBorder(null);
        cab.setForeground(Color.blue);
        cab.setFont(new Font("Arial", Font.BOLD, 14));
        cab.setText("Servidor FTP: " + servidor);

        cab2.setBounds(new Rectangle(350, 5, 140, 30));
        cab2.setBorder(null);
        cab2.setFont(new Font("Arial", Font.BOLD, 14));
        cab2.setForeground(Color.blue);
        cab2.setText("Usuario: " + user); // Actualizado al iniciar sesión

        cab3.setBounds(new Rectangle(5, 34, 140, 30));
        cab3.setBorder(null);
        cab3.setFont(new Font("Arial", Font.BOLD, 14));
        cab3.setForeground(Color.blue);
        cab3.setText("DIRECTORIO RAIZ: " + direcInicial);

        botonSalir.setBounds(new Rectangle(350, 350, 140, 30));

        // PREPARACION DE LA LISTA
        JScrollPane barraDesplazamiento = new JScrollPane();
        barraDesplazamiento.setPreferredSize(new Dimension(335, 420));
        barraDesplazamiento.setBounds(new Rectangle(5, 65, 335, 420));
        c.add(barraDesplazamiento);
        c.setLayout(null);

        c.add(campo);
        campo.setEditable(false);
        c.add(campo2);
        campo2.setEditable(false);
        c.add(botonSalir);
        c.add(cab);
        c.add(cab2);
        c.add(cab3);
        cab.setEditable(false);
        cab2.setEditable(false);
        cab3.setEditable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(510, 600);
        setVisible(true);

        //--al hacer clic en el botón Salir
        botonSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    cliente.disconnect();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                iniciarSesion(); // Volver a pedir credenciales
            }
        });
    }// ..FIN CONSTRUCTOR

    private void iniciarSesion() {
        JTextField usuarioField = new JTextField();
        JPasswordField contrasenaField = new JPasswordField();
        Object[] message = {
            "Usuario:", usuarioField,
            "Contraseña:", contrasenaField
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Inicio de sesión", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String usuario = usuarioField.getText();
            String contrasena = new String(contrasenaField.getPassword());
            if (usuario.equals("usuario1") && contrasena.equals("usu1") ||
                usuario.equals("usuario2") && contrasena.equals("usu2") ||
                usuario.equals("usuario3") && contrasena.equals("usu3")) {
                user = usuario;
                pasw = contrasena;
                cab2.setText("Usuario: " + user); // Actualizar el nombre de usuario
                System.out.println("Conectando a " + servidor);

                try {
                    cliente.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
                    cliente.connect(servidor);
                    cliente.enterLocalPassiveMode();
                    login = cliente.login(user, pasw);

                    // Establecer la ruta del archivo LOG.txt según el usuario
                    rutaLog = "C:/FTPserver/" + user.toUpperCase() + "/LOG/LOG.txt";

                    // Escribir el registro de conexión en el archivo LOG.txt
                    escribirRegistroConexion();

                    cliente.changeWorkingDirectory(direcInicial);

                    FTPFile[] files = cliente.listFiles();
                    // Construyendo arbol de directorios, espere un momento	
                    llenarLista(files, direcInicial);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                iniciarSesion(); // Volver a pedir credenciales
            }
        } else {
            System.exit(0); // Si se cancela el inicio de sesión, salir del programa
        }
    }

    private void escribirRegistroConexion() {
        try (FileWriter fw = new FileWriter(rutaLog, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
            Date hora = new Date(System.currentTimeMillis());
            out.println("Conexiones del usuario:");
            out.println("Hora de conexión: " + sdf.format(hora));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    private static void llenarLista(FTPFile[] files, String direc2) {
        if (files == null) {
            return;
        }
        DefaultListModel modeloLista = new DefaultListModel();

        // listaDirec.setForeground(Color.blue); // No se necesita más
        Font fuente = new Font("Courier", Font.PLAIN, 12);
        // listaDirec.setFont(fuente); // No se necesita más
        // listaDirec.removeAll(); // No se necesita más

        try {
            cliente.changeWorkingDirectory(direc2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        direcSelec = direc2;
        modeloLista.addElement(direc2);
        for (int i = 0; i < files.length; i++) {
            if (!(files[i].getName()).equals(".") && !(files[i].getName()).equals("..")) {
                String f = files[i].getName();
                if (files[i].isDirectory()) {
                    f = "(DIR) " + f;
                }
                modeloLista.addElement(f);
            }
        }
        // listaDirec.setModel(modeloLista); // No se necesita más
    }// Fin llenarLista

    // main---------------------------------------------------------------------
    public static void main(String[] args) throws IOException {
        new ClienteFTP();
    }// ..FIN main

}// .fin clase
