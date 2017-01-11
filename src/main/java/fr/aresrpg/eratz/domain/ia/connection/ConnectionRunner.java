package fr.aresrpg.eratz.domain.ia.connection;

import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.protocol.account.server.AccountLoginErrPacket.Error;
import fr.aresrpg.dofus.structures.server.DofusServer;
import fr.aresrpg.dofus.structures.server.ServerState;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.Mind.MindState;
import fr.aresrpg.eratz.domain.util.BotConfig;
import fr.aresrpg.tofumanchou.domain.Manchou;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class ConnectionRunner extends Info {

	private int loginCount;
	private long banTime;
	private Error loginError;

	/**
	 * @param perso
	 */
	public ConnectionRunner(BotPerso perso) {
		super(perso);
	}

	public CompletableFuture<CompletableFuture<?>> runConnection(Connector connector) {
		CompletableFuture<CompletableFuture<?>> actions = new CompletableFuture<>();
		getPerso().getMind().publishState(MindState.LOGIN, interrupt -> {
			switch (interrupt) {
				case DISCONNECT:
				case BANNED:
				case SAVE:
				case CLOSED:
				case LOGIN_ERROR:
					actions.complete(CompletableFuture.<Connector>completedFuture(connector).thenCompose(c -> {
						DofusServer server = Manchou.getServer(getPerso().getPerso().getServer());
						long time = Randoms.nextBetween(BotConfig.RECONNECT_MIN, BotConfig.RECONNECT_MAX);
						TimeUnit unit = TimeUnit.MILLISECONDS;
						if (server.getState() == ServerState.OFFLINE) {
							time = 1;
							unit = TimeUnit.HOURS;
						} else if (server.getState() == ServerState.SAVING) {
							time = 10;
							unit = TimeUnit.MINUTES;
						}
						if (isBanned()) {
							time = banTime;
							unit = TimeUnit.MILLISECONDS;
						}
						loginCount++;
						c.setTime(time);
						c.setUnit(unit);
						return runConnection(c);
					}));
					break;
			}
			getPerso().getMind().getStates().remove(MindState.LOGIN);
		});
		connector.connect();
		return actions;
	}

	private boolean isBanned() {
		return System.currentTimeMillis() < banTime;
	}

	/**
	 * @return the banTime
	 */
	public long getBanTime() {
		return banTime;
	}

	/**
	 * @return the loginCount
	 */
	public int getLoginCount() {
		return loginCount;
	}

	/**
	 * @return the loginError
	 */
	public Error getLoginError() {
		return loginError;
	}

	@Override
	public void shutdown() {

	}

}
