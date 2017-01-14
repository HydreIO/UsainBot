package fr.aresrpg.eratz.domain.ia.connection;

import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.structures.server.DofusServer;
import fr.aresrpg.dofus.structures.server.ServerState;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.util.BotConfig;
import fr.aresrpg.tofumanchou.domain.Manchou;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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

	public CompletableFuture<Connector> runConnection(Connector connector) {
		if (getPerso().isOnline()) return CompletableFuture.completedFuture(connector);
		CompletableFuture<CompletableFuture<Connector>> promise = new CompletableFuture<>();
		getPerso().getMind().publishState(interrupt -> {
			switch (interrupt) {
				case CONNECTED:
					promise.complete(CompletableFuture.completedFuture(connector));
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
					promise.complete(CompletableFuture.completedFuture(connector).thenComposeAsync(this::runConnection));
					break;
				default:
					return; // avoid reset if non handled
			}
			getPerso().getMind().resetState();
		});
		connector.connect();
		return promise.thenCompose(Function.identity());
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
