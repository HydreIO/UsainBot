package fr.aresrpg.eratz.domain.ia.connection;

import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.structures.server.DofusServer;
import fr.aresrpg.dofus.structures.server.ServerState;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.Mind.MindState;
import fr.aresrpg.eratz.domain.util.BotConfig;
import fr.aresrpg.tofumanchou.domain.Manchou;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class ConnectionRunner extends Info {

	private int loginCount;
	private long banTime;

	/**
	 * @param perso
	 */
	public ConnectionRunner(BotPerso perso) {
		super(perso);
	}

	public void runConnection(Connector connector, CompletableFuture<Connector> promise) {
		getPerso().getMind().publishState(MindState.LOGIN, interrupt -> {
			switch (interrupt) {
				case CONNECTED:
					promise.complete(connector);
					break;
				case DISCONNECT:
				case SAVE:
				case CLOSED:
				case LOGIN_ERROR:
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
					connector.setTime(time);
					connector.setUnit(unit);
					getPerso().getPerso().disconnect();
					Executors.FIXED.execute(() -> runConnection(connector, promise));
					break;
			}
			getPerso().getMind().getStates().remove(MindState.LOGIN);
		});
		connector.connect();
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
	 * @param banTime
	 *            the banTime to set
	 */
	public void setBanTime(long banTime) {
		this.banTime = banTime;
	}

	@Override
	public void shutdown() {

	}

}
