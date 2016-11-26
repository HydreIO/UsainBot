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
import fr.aresrpg.eratz.domain.util.config.Configurations;
import fr.aresrpg.eratz.domain.util.config.Configurations.Config;
import fr.aresrpg.eratz.domain.util.config.Variables;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.*;

public class TheBotFather {

	private static TheBotFather instance;
	public static final InetSocketAddress SERVER_ADRESS = new InetSocketAddress(Constants.IP, Constants.PORT);
	private Selector selector;
	private boolean running;
	private Config config;

	public TheBotFather() throws IOException {
		instance = this;
		this.running = true;
		this.config = Configurations.generate("botfather.yml", Variables.class, Optional.of(() -> {
			System.out.println("CONFIGURATION JUST CREATED PLEASE RESTART !");
			System.exit(0);
		}));
		this.selector = Selector.open();
		ServerSocketChannel botSocket = ServerSocketChannel.open();
		InetSocketAddress addr = new InetSocketAddress(Constants.LOCALHOST, 2727);
		botSocket.bind(addr);
		botSocket.configureBlocking(false);
		botSocket.register(selector, botSocket.validOps());
		Executors.FIXED.execute(() -> startServer(botSocket));
	}

	/**
	 * @return the config
	 */
	public Config getConfig() {
		return config;
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
						new DofusProxy(account, client, SocketChannel.open(SERVER_ADRESS));
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
