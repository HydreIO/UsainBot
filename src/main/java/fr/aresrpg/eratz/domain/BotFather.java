/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.database.Collection;
import fr.aresrpg.dofus.protocol.chat.server.ChatMessageOkPacket;
import fr.aresrpg.dofus.protocol.chat.server.ChatServerMessagePacket;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.eratz.domain.command.*;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.gui.MapView;
import fr.aresrpg.eratz.domain.listener.*;
import fr.aresrpg.eratz.domain.util.*;
import fr.aresrpg.eratz.infra.map.dao.BotMapDao;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.Manchou;
import fr.aresrpg.tofumanchou.domain.data.Account;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;
import fr.aresrpg.tofumanchou.domain.plugin.ManchouPlugin;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.BootStrap;
import fr.aresrpg.tofumanchou.infra.config.Configurations;
import fr.aresrpg.tofumanchou.infra.config.Configurations.Config;
import fr.aresrpg.tofumanchou.infra.config.Variables;
import fr.aresrpg.tofumanchou.infra.data.ManchouPerso;
import fr.aresrpg.tofumanchou.infra.db.DbAccessor;

import java.io.*;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BotFather implements ManchouPlugin {

	private static BotFather instance;
	public static Collection<BotMapDao> MAPS_DB;
	private ConcurrentMap<Long, BotPerso> persos = new ConcurrentHashMap<>();
	private Config config;

	@Override
	public String getAuthor() {
		return "Sceat";
	}

	@Override
	public String getName() {
		return "BotFather";
	}

	@Override
	public int getSubVersion() {
		return 8;
	}

	@Override
	public int getVersion() {
		return 1;
	}

	public static BotPerso getPerso(long uuid) {
		return instance.persos.get(uuid);
	}

	public static BotPerso getPerso(Perso perso) {
		if (perso == null) return null;
		return getPerso(perso.getUUID());
	}

	public static BotPerso getPerso(Account client) {
		if (client == null) return null;
		return getPerso(client.getPerso());
	}

	public static BotPerso getPerso(String pseudo, Server srv) {
		for (BotPerso p : instance.persos.values())
			if (p.getPerso() != null && p.getPerso().getPseudo().equals(pseudo) && p.getPerso().getServer() == srv) return p;
		return null;
	}

	//account test Marine-Lpn eratz
	// account pktclient Bratva-Nazar henual GI
	// account pkt Marine-Lpn eratz eU1
	// account pkt Bratva-Nazar henual GI

	// account view bratva-nazar henual
	// account stopspeak
	// account view marine-lpn eratz
	// whoami henual bratva-nazar
	// harvest start henual bratva-nazar
	// fight start henual bratva-nazar
	// fight henual bratva-nazar
	// goto map 10356 marine-lpn eratz
	// goto cell 318 bratva-nazar henual
	// goto map 1286 nofiev henual
	// goto 21,-30 bratva-nazar henual
	// crash party bratva-nazar henual Ckcyzevbud
	// crash party bratva-nazar henual Xacamps
	// crash party bratva-nazar henual Henual-Services 
	// crash duel bratva-nazar henual 541976
	// crash duel nofiev henual 520804
	// crash await bratva-nazar henual 520804

	// account connect bratva-nazar henual
	// account disconnect bratva-nazar henual
	// account test bratva-nazar henual

	// action koin-buy bratva-nazar henual 50
	// action koin-sell bratva-nazar henual 121
	@Override
	public void onEnable() {
		instance = this;
		injectSyso();
		Variables.ACCOUNTS.forEach(p -> p.getPersos().forEach(pe -> {
			Perso perso = Accounts.registerPerso(pe.getPseudo(), p.getAccountName(), p.getPassword(), pe.getDofusServer());
			persos.put(pe.getUUID(), new BotPerso((ManchouPerso) perso));
		}));
		this.config = Configurations.generate("BotFather.yml", BotConfig.class, Optional.of(() -> {
			LOGGER.info("Configuration created ! please configure and then restart.");
			onDisable();
		}), Optional.empty());
		DbAccessor<BotMapDao> acc = DbAccessor.create(Manchou.getDatabase(), "maps", BotMapDao.class);
		MAPS_DB = acc.get();
		Executors.FIXED.execute(MapView::main);
		Accounts.registerAccount("SceatSifu");
		Accounts.registerAccount("sdfduq");
		Accounts.registerAccount("SceatDrop3");
		new ConnectionListener();
		MapViewListener.register();
		MapsDataListener.register();
		AntiBotListener.register();
		IaListener.register();
		AdminCmdListener.register();
		MapsManager.init();
		FightListener.register();
		Manchou.registerCommand(new WhoamiCommand());
		Manchou.registerCommand(new AccountCommand());
		Manchou.registerCommand(new GotoCommand());
		Manchou.registerCommand(new CrashCommand());
		Manchou.registerCommand(new HarvestCommand());
		Manchou.registerCommand(new ActionLoopCommand());
		Manchou.registerCommand(new FightCommand());
	}

	private void injectSyso() {
		PrintStream old = System.out;
		LOGGER.addHandler(new HastebinLoggerHandler());
		System.setOut(new PrintStream(new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				old.write(b);
				Logs.stream.write(b);
			}

			@Override
			public void write(byte[] b) throws IOException {
				old.write(b);
				Logs.stream.write(b);
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				old.write(b, off, len);
				Logs.stream.write(b, off, len);
			}
		}));
	}

	public static void broadcast(Chat chat, String msg) {
		instance.persos.values().forEach(perso -> {
			if (perso.isOnline() && perso.getPerso().isMitm()) {
				ChatMessageOkPacket pkt = new ChatMessageOkPacket();
				pkt.setPseudo(" ");
				pkt.setPlayerId(perso.getPerso().getUUID());
				pkt.setChat(chat);
				pkt.setMsg(msg);
				perso.sendPacketToClient(pkt);
			}
		});
	}

	public static void broadcastServerMsg(String msg) {
		ChatServerMessagePacket pkt = new ChatServerMessagePacket();
		pkt.setMsg(msg);
		instance.persos.values().forEach(perso -> {
			if (perso.isOnline() && perso.getPerso().isMitm()) perso.sendPacketToClient(pkt);
		});
	}

	@Override
	public void onDisable() {

	}

	/**
	 * @return the persos
	 */
	public synchronized Map<Long, BotPerso> getPersos() {
		return persos;
	}

	/**
	 * @return the instance
	 */
	public static BotFather getInstance() {
		return instance;
	}

	public static void main(String[] args) throws IOException {
		BootStrap.main(args);
		Manchou.directRegistry(new BotFather());
	}

}
