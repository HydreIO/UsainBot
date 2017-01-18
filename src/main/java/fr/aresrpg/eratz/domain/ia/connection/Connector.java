package fr.aresrpg.eratz.domain.ia.connection;

import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.util.BotConfig;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class Connector extends Info {

	private long time;
	private TimeUnit unit;
	private boolean connecting;

	/**
	 * @param perso
	 */
	public Connector(BotPerso perso, long time, TimeUnit unit) {
		super(perso);
		this.time = time;
		this.unit = unit;
	}

	public void connect() {
		if (!BotConfig.AUTO_RECONNECT || connecting) return;
		connecting = true;
		getPerso().connectIn(time, unit);
	}

	public void setConnecting(boolean connecting) {
		this.connecting = connecting;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}

	@Override
	public void shutdown() {

	}

}
