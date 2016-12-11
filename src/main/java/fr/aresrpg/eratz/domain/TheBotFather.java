/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain;

import fr.aresrpg.eratz.domain.behavior.move.type.IncarnamToAstrubPath;
import fr.aresrpg.eratz.domain.dofus.Constants;
import fr.aresrpg.eratz.domain.gui.MapView;
import fr.aresrpg.eratz.domain.handler.bot.BotHandler;
import fr.aresrpg.eratz.domain.player.Account;
import fr.aresrpg.eratz.domain.player.AccountsManager;
import fr.aresrpg.eratz.domain.proxy.DofusProxy;
import fr.aresrpg.eratz.domain.util.Hastebin;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;
import fr.aresrpg.eratz.domain.util.config.Configurations;
import fr.aresrpg.eratz.domain.util.config.Configurations.Config;
import fr.aresrpg.eratz.domain.util.config.Variables;
import fr.aresrpg.eratz.domain.util.config.dao.PlayerBean;
import fr.aresrpg.eratz.domain.util.config.dao.PlayerBean.PersoBean;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.*;
import java.util.stream.Collectors;

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
		}), Optional.of(() -> {
			Variables.ACCOUNTS.add(new PlayerBean("Exemple1", "password", new PersoBean("Jowed", null), new PersoBean("Joe-larecolte", "bread_provider")));
			Variables.ACCOUNTS.add(new PlayerBean("Exemple2", "password"));
		}));
		this.selector = Selector.open();
		ServerSocketChannel botSocket = ServerSocketChannel.open();
		InetSocketAddress addr = new InetSocketAddress(Constants.LOCALHOST, 2727);
		botSocket.bind(addr);
		botSocket.configureBlocking(false);
		botSocket.register(selector, botSocket.validOps());
		Executors.FIXED.execute(() -> startServer(botSocket));
		Executors.FIXED.execute(this::startScanner);
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
						new DofusProxy(client, SocketChannel.open(SERVER_ADRESS));
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

	public void startScanner() {
		Scanner sc = new Scanner(System.in);
		while (isRunning()) {
			if (!sc.hasNext()) continue;
			String[] nextLine = sc.nextLine().split(" ");
			switch (nextLine[0].toLowerCase()) {
				case "bot":
					AccountsManager.getInstance().getAccounts().forEach((s, a) -> {
						if (a.isClientOnline()) {
							a.getProxy().getRemoteHandler().addHandler(new BotHandler(a.getCurrentPlayed()));
							System.out.println("Bot Handler ajouté pour " + a.getCurrentPlayed().getPseudo());
						}
					});
					break;
				case "way":
					AccountsManager.getInstance().getAccounts().forEach((s, a) -> {
						if (a.isClientOnline() || a.isBotOnline())
							a.getCurrentPlayed().changeBehavior(new IncarnamToAstrubPath(a.getCurrentPlayed()));
					});
					break;
				case "view":
					AccountsManager.getInstance().getAccounts().forEach((s, a) -> {
						if (a.isClientOnline() || a.isBotOnline())
							MapView.getInstance().startView(a.getCurrentPlayed().getDebugView(), a.getCurrentPlayed().getPseudo());
					});
					break;
				case "exit":
					System.out.println(Hastebin.post());
					System.exit(0);
					break;
				case "listaccounts":
					System.out.println(AccountsManager.getInstance()
							.getAccounts().values().stream().map(Account::getUsername)
							.collect(Collectors.joining(", ")));
					break;
				case "selectaccount":
					String perso = nextLine.length == 3 ? nextLine[2] : AccountsManager.getInstance().getAccounts().get(nextLine[1]).getPersos().get(0).getPseudo();
					if (perso == null) {
						System.out.println("Perso introuvable");
						break;
					}
					System.out.println("Selection de " + perso);
					AccountsManager.getInstance().connectAccount(nextLine[1], perso);
					break;
			}
			/*
			 * AccountsManager.getInstance().getAccounts().forEach((s, a) -> {
			 * if (a.isClientOnline()) {
			 * SocketChannel channel = (SocketChannel) a.getRemoteConnection().getChannel();
			 * System.out.println("Send: " + nextLine);
			 * String nn = nextLine + "\n\0";
			 * try {
			 * channel.write(ByteBuffer.wrap(nn.getBytes()));
			 * } catch (Exception e) {
			 * e.printStackTrace();
			 * }
			 * }
			 * });
			 */
		}
	}

	public static void main(String... args) throws IOException {
		PrintStream old = System.out;
		System.setOut(new PrintStream(new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				old.write(b);
				Hastebin.stream.write(b);
			}

			@Override
			public void write(byte[] b) throws IOException {
				old.write(b);
				Hastebin.stream.write(b);
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				old.write(b, off, len);
				Hastebin.stream.write(b, off, len);
			}
		}));
		System.out.println("Starting server..");
		new TheBotFather();
		MapView.main(args);
	}

}
