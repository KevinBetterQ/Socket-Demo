import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class SocketServer {

	private static List<Socket> clients = new LinkedList<Socket>();

	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(6666);
			System.out.println("listening");
			while (true) {
				Socket client = server.accept();
				clients.add(client);
				System.out.println("connect success");
				receive(client);
			}
		} catch (Exception e) {
		}
	}

	public static void receive(final Socket client) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					DataInputStream in = new DataInputStream(client.getInputStream());  //用来接收数据
					while (true) {
						byte[] b = new byte[1024];
						int len = in.read(b, 0, b.length);  //读取数据
						String receive = new String(b, 0, len);
						System.out.println(receive);
						for (Socket temp : clients) {
							if (!client.equals(temp)) {
								temp.getOutputStream().write(receive.getBytes());
								temp.getOutputStream().flush();
							}
						}
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}
}