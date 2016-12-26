/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain;

import fr.aresrpg.commons.domain.condition.Option;
import fr.aresrpg.commons.domain.log.Logger;
import fr.aresrpg.commons.domain.log.LoggerBuilder;
import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.dofus.util.DofusMapView;
import fr.aresrpg.eratz.domain.antibot.AntiBot;
import fr.aresrpg.eratz.domain.antibot.behavior.DuelCrashBehavior;
import fr.aresrpg.eratz.domain.antibot.behavior.GroupCrashBehavior;
import fr.aresrpg.eratz.domain.data.*;
import fr.aresrpg.eratz.domain.data.dofus.item.DofusItems2;
import fr.aresrpg.eratz.domain.data.dofus.player.BotJob;
import fr.aresrpg.eratz.domain.data.dofus.player.Classe;
import fr.aresrpg.eratz.domain.data.player.Account;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.object.Road;
import fr.aresrpg.eratz.domain.data.player.state.PlayerState;
import fr.aresrpg.eratz.domain.gui.MapView;
import fr.aresrpg.eratz.domain.ia.Roads;
import fr.aresrpg.eratz.domain.ia.behavior.move.BankDepositPath;
import fr.aresrpg.eratz.domain.io.proxy.DofusProxy;
import fr.aresrpg.eratz.domain.util.*;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;
import fr.aresrpg.eratz.domain.util.config.*;
import fr.aresrpg.eratz.domain.util.config.Configurations.Config;
import fr.aresrpg.eratz.domain.util.config.dao.GroupBean;
import fr.aresrpg.eratz.domain.util.config.dao.PlayerBean;
import fr.aresrpg.eratz.domain.util.config.dao.PlayerBean.PersoBean;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class TheBotFather {

	private static TheBotFather instance;
	public static final Logger LOGGER = new LoggerBuilder("BOT").setUseConsoleHandler(true, true, Option.none(), Option.none()).build();
	public static final InetSocketAddress SERVER_ADRESS = new InetSocketAddress(Constants.IP, Constants.PORT);
	private Selector selector;
	private boolean running;
	private Config config;
	private Config botBlackList;

	public TheBotFather() throws IOException {
		instance = this;
		this.running = true;
		Executors.FIXED.execute(() -> {
			try {
				LOGGER.info("Initialisating items..");
				BenchTime t = new BenchTime();
				ItemsData.getInstance().init();
				LOGGER.info("Items initialized ! (" + t.getAsLong() + "ms)");
				LOGGER.info("Initialisating maps..");
				BenchTime t2 = new BenchTime();
				MapsData.getInstance().init();
				LOGGER.info("Maps initialized ! (" + t2.getAsLong() + "ms)");
				LOGGER.info("Initialisating langs..");
				BenchTime t3 = new BenchTime();
				InfosData.getInstance().init();
				LOGGER.info("Langs initialized ! (" + t3.getAsLong() + "ms)");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		this.config = Configurations.generate("botfather.yml", Variables.class, Optional.of(() -> {
			LOGGER.info("CONFIGURATION JUST CREATED PLEASE RESTART !");
			System.exit(0);
		}), Optional.of(() -> {
			Variables.ACCOUNTS.add(new PlayerBean("Exemple1", "password", new PersoBean("Jowed", BotJob.CRASHER.name(), Server.ERATZ, Classe.ENUTROF.name(), true),
					new PersoBean("Joe-larecolte", "bread_provider", Server.ERATZ, Classe.SRAM.name(), true)));
			Variables.ACCOUNTS.add(new PlayerBean("Exemple2", "password"));
			Variables.GROUPS.add(new GroupBean("Testgroup", "Jowed", "Jawad"));
		}));
		this.botBlackList = Configurations.generate("blacklist.yml", BlackList.class, Optional.of(() -> {
			LOGGER.info("BlackList created !");
		}), Optional.of(() -> {
			BlackList.BOTS.add("Lor_hoth");
			BlackList.BOTS.add("Smero-Badaa");
			BlackList.BOTS.add("Mariad");
		}));
		this.selector = Selector.open();
		ServerSocketChannel botSocket = ServerSocketChannel.open();
		InetSocketAddress addr = new InetSocketAddress(Variables.IP_MACHINE, Variables.PORT_BOT);
		botSocket.bind(addr);
		botSocket.configureBlocking(false);
		botSocket.register(selector, botSocket.validOps());
		AntiBot.getInstance();
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
			LOGGER.info("Listening for connection..");
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
						LOGGER.info("Client Accepted");
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
				case "bucheron":
					Executors.FIXED.execute(() -> {
						AccountsManager.getInstance().getAccounts().forEach((s, a) -> {
							if (a.isClientOnline() || a.isBotOnline()) {
								Perso p = a.getCurrentPlayed();
								MindManager.getInstance().lvlUpBucheron(p);
							}
						});
					});
					break;
				case "ble":
					Executors.FIXED.execute(() -> {
						AccountsManager.getInstance().getAccounts().forEach((s, a) -> {
							if (a.isClientOnline() || a.isBotOnline()) {
								Perso p = a.getCurrentPlayed();
								MindManager.getInstance().lvlUpPaysan(p);
							}
						});
					});
					break;
				case "bank":
					Executors.FIXED.execute(() -> {
						AccountsManager.getInstance().getAccounts().forEach((s, a) -> {
							if (a.isClientOnline() || a.isBotOnline()) {
								Perso p = a.getCurrentPlayed();
								LOGGER.debug(p.getPseudo() + " va vider son inventaire !");
								new BankDepositPath(p, DofusItems2.HACHE_DE_L_APPRENTI_BÛCHERON.getId()).start();
								LOGGER.success("Inventaire vidé !");
							}
						});

					});
					break;
				case "goto":
					Executors.FIXED.execute(() -> {
						Instant now = Instant.now();
						AccountsManager.getInstance().getAccounts().forEach((s, a) -> {
							if (a.isClientOnline() || a.isBotOnline()) {
								Perso p = a.getCurrentPlayed();
								String[] coords = nextLine[1].split(",");
								int x = Integer.parseInt(coords[0]);
								int y = Integer.parseInt(coords[1]);
								p.getNavigation().joinCoords(x, y);
								LOGGER.success(p.getPseudo() + " est arrivé à destination ! (" + Duration.ofMillis(Instant.now().minusMillis(now.toEpochMilli()).toEpochMilli()).toMinutes() + "m)");
							}
						});
					});
					break;
				case "whoami":
					AccountsManager.getInstance().getAccounts().forEach((s, a) -> {
						if (a.isClientOnline() || a.isBotOnline()) {
							Perso p = a.getCurrentPlayed();
							LOGGER.debug("Ndc = " + p.getAccount().getUsername());
							LOGGER.debug("Account State = " + p.getAccount().getState());
							LOGGER.debug("State = " + p.getState());
							LOGGER.debug("Id = " + p.getId());
							LOGGER.debug("Pseudo = " + p.getPseudo());
							LOGGER.debug("Lvl = " + p.getStatsInfos().getLvl());
							LOGGER.debug("Energie = " + p.getStatsInfos().getEnergy());
							LOGGER.debug("Pods libre = " + p.getStatsInfos().getFreePods());
							LOGGER.debug("Vie = " + p.getStatsInfos().getLife());
							LOGGER.debug("Xp restant = " + (p.getStatsInfos().getMaxXp() - p.getStatsInfos().getXp()));
							LOGGER.debug("Grp = " + p.getGroup());
							LOGGER.debug("Fight = " + p.getFightInfos().getCurrentFight());
							LOGGER.debug("Inv = " + p.getInventory().showContent());
						}
					});
					break;
				case "whereami":
					AccountsManager.getInstance().getAccounts().forEach((s, a) -> {
						if (a.isClientOnline() || a.isBotOnline()) {
							Perso p = a.getCurrentPlayed();
							LOGGER.debug("Map = " + p.getMapInfos().getMap().getInfos());
							LOGGER.debug("CellId = " + p.getMapInfos().getCellId());
						}
					});
					break;
				case "road":
					AccountsManager.getInstance().getAccounts().forEach((s, a) -> {
						if (a.isClientOnline() || a.isBotOnline()) {
							Perso p = a.getCurrentPlayed();
							p.setState(PlayerState.RUNNING);
							Road r = Roads.nearestRoad(p.getMapInfos().getMap());
							if (r == null) {
								LOGGER.error("Route introuvable !");
							} else {
								LOGGER.success(p.getPseudo() + " va éssayer de rejoindre " + r.getLabel());
								r.takeRoad(p);
							}
						}
					});
					break;
				case "humanfight":
					Variables.HUMAN_FIGHT = !Variables.HUMAN_FIGHT;
					LOGGER.success("Human Fights = " + Variables.HUMAN_FIGHT);
					break;
				case "removebot":
					String na = nextLine[1];
					if (BlackList.BOTS.remove(na)) LOGGER.info("Removing " + na + " as bot !");
					botBlackList.apply();
					break;
				case "addbot":
					String name = nextLine[1];
					LOGGER.info("Adding " + name + " as bot !");
					BlackList.BOTS.add(name);
					botBlackList.apply();
					break;
				case "crash":
					LOGGER.severe("Starting crash session !");
					AccountsManager.getInstance().getAccounts().forEach((s, a) -> {
						if (a.isClientOnline() || a.isBotOnline()) {
							Perso p = a.getCurrentPlayed();
							int id = 0;
							try {
								id = Integer.parseInt(nextLine[1]);
							} catch (Exception e) {
								id = p.getMapInfos().getMap().getIdOf(nextLine[1]);
							}
							if (id == 0) LOGGER.error(nextLine[1] + " not found !");
							else {
								DuelCrashBehavior b = new DuelCrashBehavior(p, id);
								b.start();
							}
						}
					});
					break;
				case "party":
					LOGGER.severe("Starting crash session !");
					String naa = nextLine[1];
					AccountsManager.getInstance().getAccounts().forEach((s, a) -> {
						if (a.isClientOnline() || a.isBotOnline()) {
							Perso p = a.getCurrentPlayed();
							GroupCrashBehavior b = new GroupCrashBehavior(p, naa);
							b.start();
						}
					});
					break;
				case "reload":
					botBlackList.pull();
					break;
				case "view":
					AccountsManager.getInstance().getAccounts().forEach((s, a) -> {
						if (a.isClientOnline() || a.isBotOnline()) {
							a.getCurrentPlayed().setDebugView(new DofusMapView());
							a.getCurrentPlayed().getDebugView().setMap(a.getCurrentPlayed().getMapInfos().getMap().getDofusMap());
							a.getCurrentPlayed().getDebugView().setCurrentPosition(a.getCurrentPlayed().getMapInfos().getCellId());
							a.getCurrentPlayed().getDebugView().setOnCellClick(cell -> Executors.FIXED.execute(() -> {
								System.out.println(a.getCurrentPlayed().getMapInfos().getMap().getDofusMap().getCell(cell));
								a.getCurrentPlayed().getNavigation().moveToCell(cell);
							}));
							MapView.getInstance().startView(a.getCurrentPlayed().getDebugView(), a.getCurrentPlayed().getPseudo() + " | " + a.getCurrentPlayed().getMapInfos().getMap().getInfos());
						}
					});
					break;
				case "exit":
					LOGGER.info(Hastebin.post());
					System.exit(0);
					break;
				case "listaccounts":
					LOGGER.info(AccountsManager.getInstance()
							.getAccounts().values().stream().map(Account::getUsername)
							.collect(Collectors.joining(", ")));
					break;
				case "connect":
					String perso = nextLine.length == 3 ? nextLine[2] : AccountsManager.getInstance().getAccounts().get(nextLine[1]).getPersos().get(0).getPseudo();
					if (perso == null) {
						LOGGER.info("Perso introuvable");
						break;
					}
					LOGGER.info("Selection de " + perso);
					AccountsManager.getInstance().connectAccount(nextLine[1], perso);
					break;
			}
			/*
			 * AccountsManager.getInstance().getAccounts().forEach((s, a) -> {
			 * if (a.isClientOnline()) {
			 * SocketChannel channel = (SocketChannel) a.getRemoteConnection().getChannel();
			 * LOGGER.info("Send: " + nextLine);
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
		LOGGER.info("Starting server..");
		new TheBotFather();
		MapView.main(args);
	}

}
