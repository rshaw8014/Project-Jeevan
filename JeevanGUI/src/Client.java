import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Client extends JFrame implements ActionListener {

	public Client() {
		super("JEEVAN");
	}

	Scanner sc = new Scanner(System.in);
	// input contact for sign Up
	String number;
	Socket s;
	DataOutputStream dout;

	// GUI Requirements
	JLabel welcome;
	JLabel mobLabel;
	JTextField mobile;
	JButton signUp;
	JLabel img1;
	JLabel img2;

	public void clientSignUp() throws Exception {
		// added image
		ImageIcon ii1 = new ImageIcon("D:\\Eclipse\\Eclipse j2se\\JeevanGUI\\src\\rsz_button.png");
		img1 = new JLabel(ii1);
		ImageIcon ii2 = new ImageIcon("D:\\Eclipse\\Eclipse j2se\\JeevanGUI\\src\\rsz_ambulance.png");
		img2 = new JLabel(ii2);

		// Label greeting
		welcome = new JLabel("Welcome to JEEVAN");

		// mobile Textfield
		mobLabel = new JLabel("Mob.No:");
		mobile = new JTextField();
		s = new Socket("192.168.43.173", 11100);

		// Sending number to server
		// submitting number with button
		signUp = new JButton("Sign Up");
		signUp.addActionListener(this);
		dout = new DataOutputStream(s.getOutputStream());

		welcome.setBounds(120, 50, 500, 50);
		mobLabel.setBounds(50, 180, 500, 50);
		mobile.setBounds(50, 220, 80, 30);
		signUp.setBounds(260, 220, 80, 30);
		img1.setBounds(50, 60, 400, 170);
		img2.setBounds(50, 60, 180, 170);

		add(welcome);
		add(mobLabel);
		add(mobile);
		add(signUp);
		add(img1);
		add(img2);

		setSize(400, 400);
		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}

	public static void main(String[] args) throws Exception {
		Client c1 = new Client();
		c1.clientSignUp();
	}

	@Override

	// Sending number with signUp button
	public void actionPerformed(ActionEvent e) {
		try {
			dout = new DataOutputStream(s.getOutputStream());
			number = mobile.getText();
			Pattern p = Pattern.compile("(0|91)?[7-9][0-9]{9}");
			Matcher m = p.matcher(number);
			boolean isPhoneNo = m.matches();
			if (number.equals("") || !isPhoneNo)
				JOptionPane.showMessageDialog(this, "Invalid Number");
			else {
				dout.writeUTF(number);
				dout.flush();
				try {
					new OtpGen().getOTP(s);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}

class OtpGen extends JFrame implements ActionListener {

	// TODO Auto-generated catch b
	public OtpGen() {
		super("OTP");
	}

	private String OTP;
	BufferedReader br;
	DataInputStream din;
	DataOutputStream dout;
	String isValidated;
	Socket s;

	// GUI code req
	JLabel enterOtp;
	JTextField getOtp;
	JButton submit;
	JLabel img3;

	// Server will generate OTP for number
	public void getOTP(Socket s) throws Exception {
		// img
		ImageIcon ii3 = new ImageIcon("D:\\Eclipse\\Eclipse j2se\\JeevanGUI\\src\\rsz_otp.png");
		img3 = new JLabel(ii3);

		this.s = s;
		// Client enter OTP label
		enterOtp = new JLabel("Enter OTP");
		// Client enter OTP Textfield
		getOtp = new JTextField();

		submit = new JButton("Submit");
		submit.addActionListener(this);

		img3.setBounds(50, 60, 280, 220);
		enterOtp.setBounds(150, 50, 500, 50);
		getOtp.setBounds(50, 220, 80, 30);
		submit.setBounds(260, 220, 80, 30);

		add(enterOtp);
		add(getOtp);
		add(submit);
		add(img3);
		setSize(400, 400);
		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	// When OTP submitted
	@Override
	public void actionPerformed(ActionEvent e) {
		// OTP sent to server by client
		try {
			OTP = getOtp.getText();
			dout = new DataOutputStream(s.getOutputStream());
			din = new DataInputStream(s.getInputStream());
			br = new BufferedReader(new InputStreamReader(System.in));
			dout.writeUTF(OTP);
			isValidated = din.readUTF();
			dout.flush();

			// If Signed Up Successfully
			if (isValidated.equals("Confirmed")) {
				new AlertPage().alertPage(s);
			}
			// if not Signed Up
			else {
				JOptionPane.showMessageDialog(this, "Invalid OTP");
			}

		} catch (Exception e1) {
		}
	}

}

// Alert Page
class AlertPage extends JFrame implements ActionListener {

	public AlertPage() {
		super("ALERT");
	}

	String alertMsg;
	DataInputStream din;
	DataOutputStream dout;
	BufferedReader br;
	Socket s;
	String reqSer;

	// GUI Code req
	JButton alert;

	public void alertPage(Socket s) throws Exception {
		this.s = s;
		ImageIcon icon = new ImageIcon("D:\\Eclipse\\Eclipse j2se\\JeevanGUI\\src\\rsz_alert.png");
		alert = new JButton("ALERT", icon);
		alert.addActionListener(this);

		alert.setBounds(150, 150, 100, 50);

		add(alert);

		setSize(400, 400);
		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private ImageIcon createImage(String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			// alert MSG sent to server by client
			alertMsg = "ALERT";
			dout = new DataOutputStream(s.getOutputStream());
			din = new DataInputStream(s.getInputStream());
			br = new BufferedReader(new InputStreamReader(System.in));
			dout.writeUTF(alertMsg);
			dout.flush();

			// When server requests for address
			reqSer = din.readUTF();

			// for getting address
			if (reqSer.equals("Send address")) {
				new AlertPlace().alertPlace(s);
			}
		} catch (Exception e2) {
		}

	}
}

// To take address
class AlertPlace extends JFrame implements ActionListener {

	public AlertPlace() {
		super("ALERT ADDRESS");
	}

	Socket s;
	String alertAddress;
	DataOutputStream dout;

	// GUI Code req
	JLabel address;
	JLabel addLabel;
	JTextField getAddress;
	JButton sendAddress;
	JLabel img4;

	public void alertPlace(Socket s) {
		// img
		ImageIcon ii4 = new ImageIcon("D:\\Eclipse\\Eclipse j2se\\JeevanGUI\\src\\loc.png");
		img4 = new JLabel(ii4);

		this.s = s;
		// address label
		address = new JLabel("Enter Address");

		// address textfield
		getAddress = new JTextField();

		// sendAddress Button
		sendAddress = new JButton("Send");
		addLabel = new JLabel("Enter Address:");
		sendAddress.addActionListener(this);

		img4.setBounds(60, 60, 280, 220);
		address.setBounds(150, 50, 500, 50);
		addLabel.setBounds(50, 180, 500, 50);
		getAddress.setBounds(50, 220, 80, 30);
		sendAddress.setBounds(260, 220, 80, 30);

		add(address);
		add(addLabel);
		add(getAddress);
		add(sendAddress);
		add(img4);

		setSize(400, 400);
		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}

	public void actionPerformed(ActionEvent e) {
		try {
			dout = new DataOutputStream(s.getOutputStream());
			alertAddress = getAddress.getText();
			dout.writeUTF(alertAddress);
			new LastPage().thankYou();
		} catch (Exception e3) {
		}
	}
}

class LastPage extends JFrame {
	JLabel img5;

	public LastPage() {
		super("Greetings");
	}

	public void thankYou() {
		ImageIcon ii5 = new ImageIcon("D:\\Eclipse\\Eclipse j2se\\JeevanGUI\\src\\rsz_thankYou.png");
		img5 = new JLabel(ii5);
		img5.setBounds(60, 60, 280, 220);
		add(img5);

		setSize(400, 400);
		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}