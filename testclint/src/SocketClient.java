import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
	
	private static Socket socket;
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws Exception{
		socket = new Socket("127.0.0.1", 6666);
		System.out.println("connect success");
		receiveMsg();
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		while (true) {
			String str = sc.nextLine();
			out.write(str.getBytes());
			out.flush();
		}
	}

	public static void receiveMsg() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					DataInputStream in = new DataInputStream(socket.getInputStream());
					while (true) {
						byte[] b = new byte[1024];
						int len = in.read(b, 0, b.length);
						String str = new String(b, 0, len);
						System.out.println(str);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
