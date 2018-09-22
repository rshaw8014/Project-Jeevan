import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Hospital extends JFrame {

	public Hospital() {
		super("Hospital's Server");
	}

	ServerSocket ss;
	Socket s;
	DataOutputStream dout;
	DataInputStream din;
	BufferedReader br;

	// GUI Code
	JLabel welcome;
	JTextField message;
	JLabel img;

	public void hospital() throws Exception {
		// img
		ImageIcon ii = new ImageIcon("D:\\Eclipse\\Eclipse j2se\\JeevanGUI\\src\\rsz_hospital.png");
		img = new JLabel(ii);

		System.out.println("Hospital's Server is Running");
		welcome = new JLabel("Welcome to JEEVAN");
		ss = new ServerSocket(3322);
		s = ss.accept();
		din = new DataInputStream(s.getInputStream());
		message = new JTextField(din.readUTF());

		img.setBounds(50, 60, 280, 220);
		welcome.setBounds(120, 50, 500, 50);
		message.setBounds(20, 285, 350, 65);

		add(welcome);
		add(message);
		add(img);

		setSize(400, 400);
		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}

	public static void main(String[] args) throws Exception {
		new Hospital().hospital();
	}
}
