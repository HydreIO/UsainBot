/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain;

import fr.aresrpg.eratz.domain.dofus.Constants;
import fr.aresrpg.eratz.domain.player.Account;
import fr.aresrpg.eratz.domain.player.AccountsManager;
import fr.aresrpg.eratz.domain.proxy.DofusProxy;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class TheBotFather {

	private static TheBotFather instance;
	private Selector selector;
	private boolean running;

	public TheBotFather() throws IOException {
		instance = this;
		this.running = true;
		this.selector = Selector.open();
		ServerSocketChannel botSocket = ServerSocketChannel.open();
		InetSocketAddress addr = new InetSocketAddress(Constants.LOCALHOST, 2727);
		botSocket.bind(addr);
		botSocket.configureBlocking(false);
		botSocket.register(selector, botSocket.validOps());
		Executors.FIXED.execute(() -> startServer(botSocket));
	}

	private void startServer(ServerSocketChannel channel) {
		while (isRunning()) {
			System.out.println("Listening for connection..");
			try {
				selector.select();
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					it.remove();
					if (key.isAcceptable()) {
						SocketChannel client = channel.accept();
						client.configureBlocking(false);
						System.out.println("Client Accepted");
						Account account = new Account("blablablabla", "");
						AccountsManager.getInstance().registerAccount(account);
						new DofusProxy(account, client, SocketChannel.open(new InetSocketAddress(Constants.IP, 443)));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isRunning() {
		return this.running;
	}

	/**
	 * @return the instance
	 */
	public static TheBotFather getInstance() {
		return instance;
	}

	/*
	 * public void startScanner() {
	 * Scanner sc = new Scanner(System.in);
	 * while (sc.hasNext()) {
	 * String nextLine = sc.nextLine();
	 * clients.forEach(d -> {
	 * try {
	 * System.out.println("Send: " + nextLine);
	 * String nn = nextLine + "\n\0";
	 * d.getRemoteOutputStream().write(nn.getBytes());
	 * d.getRemoteOutputStream().flush();
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * }
	 * });
	 * }
	 * }
	 */

	public static void main(String... args) throws IOException {
		System.out.println("Starting server..");
		new TheBotFather();
	}

}
