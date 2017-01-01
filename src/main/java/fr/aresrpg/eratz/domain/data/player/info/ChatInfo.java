package fr.aresrpg.eratz.domain.data.player.info;

import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.util.chat.*;

import java.util.*;

/**
 * 
 * @since
 */
public class ChatInfo extends Info {

	private Map<String, Long> lastMsgByPlayer = new HashMap<>();
	private final ChatterBotFactory factory = new ChatterBotFactory();
	private ChatterBot voice;
	private ChatterBotSession session;
	private long lastSpeak;

	/**
	 * @param perso
	 */
	public ChatInfo(BotPerso perso) {
		super(perso);
		try {
			this.voice = factory.create(ChatterBotType.CLEVERBOT);
			this.session = voice.createSession(Locale.FRANCE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getResponse(String s) {
		try {
			return session.think(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void notifySpeak(String player) {
		lastMsgByPlayer.put(player, System.currentTimeMillis());
	}

	public void notifySpeak() {
		this.lastSpeak = System.currentTimeMillis();
	}

	public long getLastSpeak(String player) {
		return lastMsgByPlayer.getOrDefault(player, 0L);
	}

	public long getLastSpeak() {
		return lastSpeak;
	}

	@Override
	public void shutdown() {

	}

}
