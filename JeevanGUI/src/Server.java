import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server {
	ServerSocket ss;
	Socket s;
	DataOutputStream dout;
	DataInputStream din;
	BufferedReader br;

	public void server() throws Exception {
		System.out.println("Server is here");
		ss = new ServerSocket(2029);
		while (true) {
			try {
				s = ss.accept();

				System.out.println("A new client is connected : " + s);
				dout = new DataOutputStream(s.getOutputStream());
				din = new DataInputStream(s.getInputStream());
				br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Assigning new thread for this client");
				Thread t = new ClientHandler(s, din, dout);

				// Invoking the start() method
				t.start();
			} catch (Exception e) {
				s.close();
				e.printStackTrace();
			}
		}
		// Created Streams

	}

	public static void main(String[] args) throws Exception {
		Server s1 = new Server();
		s1.server();
	}
}

class ClientHandler extends Thread {
	DataOutputStream dout;
	DataInputStream din;
	BufferedReader br;
	String clientNumber;
	String genOTP;
	String clientOTP;
	String confirmation;
	String getAlertMsg;
	Socket s;
	String getAlertAddress;
	Socket sHosp;
	PrintWriter file;

	// Constructor
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
		this.s = s;
		this.din = dis;
		this.dout = dos;
	}

	@Override
	public void run() {
		try {
			// Taking client's contact
			clientNumber = din.readUTF();
			// OTP generated
			genOTP = new String(OTP(6));
			System.out.print("You OTP is : " + genOTP);
			// OTP sent to client's phone number
			for (int i = 1; i <= 3; i++) {
				clientOTP = din.readUTF();
				System.out.println("Client OTP: " + clientOTP);
				System.out.println("gentt OTP: " + genOTP);
				// if OTP matched
				if (genOTP.equals(clientOTP)) {
					confirmation = "Confirmed";
					dout.writeUTF(confirmation);
					dout.flush();
					break;
				}
				// if OTP doesn't match
				else {
					confirmation = "Invalid OTP";
					dout.writeUTF(confirmation);
					dout.flush();
					new Server().server();
				}
			}

			getAlertMsg = din.readUTF();
			if (getAlertMsg.equals("ALERT")) {
				getAlert();
			}

			else {

			}
		}

		catch (Exception e) {
			System.out.println("Something Wrong");
		}
	}

	public void getAlert() throws Exception {
		InetAddress addr = InetAddress.getLocalHost();
		dout.writeUTF("Send address");
		getAlertAddress = din.readUTF();
		System.out.println("Alert sent by");
		System.out.println("IP Address: " + addr.getHostAddress());
		System.out.println("Contact: " + clientNumber);
		System.out.println("Accident happend is in: " + getAlertAddress);
		s.close();
		file = new PrintWriter(new FileWriter("C:\\Users\\Rahul_Shaw\\Desktop\\Jeevan.txt", true));
		file.println();
		file.println("Alert Send By: ");
		file.println("IP Address: " + addr.getHostAddress());
		file.println("Contact: " + clientNumber);
		file.println("Address: " + getAlertAddress);
		file.println();
		System.out.println("Successfully Added in file");
		file.close();
		// sending alert to hospital
		toHosp();

	}

	public void toHosp() {
		try {
			System.out.println("Sent address to hospital");
			sHosp = new Socket("192.168.43.242", 3322);
			dout = new DataOutputStream(sHosp.getOutputStream());
			// sending alert to hospital
			dout.writeUTF("Alert!! \n need ambulance in " + getAlertAddress);
			dout.flush();
		} catch (Exception e) {
			System.out.println("toHosp");
		}

	}

	// code for OTP Generation
	static char[] OTP(int len) {
		System.out.println("Generating OTP using random() : ");

		// Using numeric values
		String numbers = "0123456789";

		// Using random method
		Random rndm_method = new Random();

		char[] otp = new char[len];

		for (int i = 0; i < len; i++) {
			// Use of charAt() method : to get character value
			// Use of nextInt() as it is scanning the value as int
			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		return otp;
	}

}
