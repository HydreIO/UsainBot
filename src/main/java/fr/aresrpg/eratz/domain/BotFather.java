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
import fr.aresrpg.commons.domain.database.Collection;
import fr.aresrpg.commons.domain.log.Logger;
import fr.aresrpg.commons.domain.log.LoggerBuilder;
import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.eratz.domain.command.*;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.gui.MapView;
import fr.aresrpg.eratz.domain.listener.*;
import fr.aresrpg.eratz.domain.util.Hastebin;
import fr.aresrpg.eratz.infra.map.dao.BotMapDao;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.Manchou;
import fr.aresrpg.tofumanchou.domain.data.Account;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;
import fr.aresrpg.tofumanchou.domain.plugin.ManchouPlugin;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.BootStrap;
import fr.aresrpg.tofumanchou.infra.config.Variables;
import fr.aresrpg.tofumanchou.infra.data.ManchouPerso;
import fr.aresrpg.tofumanchou.infra.db.DbAccessor;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BotFather implements ManchouPlugin {

	private static BotFather instance;
	public static final Logger LOGGER = new LoggerBuilder("BOT").setUseConsoleHandler(true, true, Option.none(), Option.none()).build();
	public static Collection<BotMapDao> MAPS_DB;
	private Map<Long, BotPerso> persos = new HashMap<>();

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
		return 2;
	}

	@Override
	public int getVersion() {
		return 1;
	}

	public static BotPerso getPerso(long uuid) {
		return instance.persos.get(uuid);
	}

	public static BotPerso getPerso(Perso perso) {
		return getPerso(perso.getUUID());
	}

	public static BotPerso getPerso(Account client) {
		return getPerso(client.getPerso());
	}

	public static BotPerso getPerso(String pseudo, Server srv) {
		for (BotPerso p : instance.persos.values())
			if (p.getPerso() != null && p.getPerso().getPseudo().equals(pseudo) && p.getPerso().getServer() == srv) return p;
		return null;
	}

	//account test Marine-Lpn eratz
	// account view bratva-nazar henual
	// whoami henual bratva-nazar
	// bucheron henual bratva-nazar
	// fight henual bratva-nazar
	// goto 4,16 bratva-nazar henual
	// goto 14,25 bratva-nazar henual
	// goto 21,-30 bratva-nazar henual
	// crash party bratva-nazar henual Ethylind
	// account connect bratva-nazar henual
	@Override
	public void onEnable() {
		instance = this;
		injectSyso();
		Variables.ACCOUNTS.forEach(p -> p.getPersos().forEach(pe -> {
			Perso perso = Accounts.registerPerso(pe.getPseudo(), p.getAccountName(), p.getPassword(), pe.getDofusServer());
			persos.put(pe.getUUID(), new BotPerso((ManchouPerso) perso));
		}));
		DbAccessor<BotMapDao> acc = DbAccessor.create(Manchou.getDatabase(), "maps", BotMapDao.class);
		MAPS_DB = acc.get();
		Executors.FIXED.execute(MapView::main);
		Accounts.registerAccount("SceatSifu");
		Accounts.registerAccount("SceatDrop3");
		new ConnectionListener();
		MapViewListener.register();
		FightListener.register();
		MapsDataListener.register();
		MapsManager.init();
		Manchou.registerCommand(new WhoamiCommand());
		Manchou.registerCommand(new HastebinCommand());
		Manchou.registerCommand(new AccountCommand());
		Manchou.registerCommand(new GotoCommand());
		Manchou.registerCommand(new FightCommand());
		Manchou.registerCommand(new BucheronCommand());
		Manchou.registerCommand(new CrashCommand());
	}

	private void injectSyso() {
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
