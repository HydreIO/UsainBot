package fr.aresrpg.eratz.domain;

import fr.aresrpg.eratz.domain.proxy.DofusGameProxy;
import fr.aresrpg.eratz.domain.proxy.DofusRealmProxy;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class TheBotFather {

	private static TheBotFather instance = new TheBotFather();
	private List<DofusGameProxy> clients = new ArrayList<>();


	public static void main(String... args) throws IOException {
		System.out.println("Loading...");
		ServerSocket server = new ServerSocket(30111);
		Executors.FIXED.execute(TheBotFather::init);
		Socket socket;
		while ((socket = server.accept()) != null) {
			System.out.println("Client accepted...");
			new DofusRealmProxy(socket.getInputStream(), socket.getOutputStream());
		}
	}

	public static void init() {
		getInstance().startScanner();
	}

	public static void registerPlayer(DofusGameProxy pl) {
		getInstance().clients.add(pl);
	}

	/**
	 * @return the instance
	 */
	public static TheBotFather getInstance() {
		return instance;
	}

	public void startScanner() {
		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			String nextLine = sc.nextLine();
			clients.forEach(d -> {
				try {
					System.out.println("Send: " + nextLine);
					String nn = nextLine + "\n\0";
					d.getRemoteOutputStream().write(nn.getBytes());
					d.getRemoteOutputStream().flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

}
