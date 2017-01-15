package fr.aresrpg.eratz.domain.data.player.info;

import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.util.chat.*;

import java.util.*;

/**
 * 
 * @since
 */
public class ChatUtilities extends Info {

	private Map<String, Long> lastMsgByPlayer = new HashMap<>();
	private final ChatterBotFactory factory = new ChatterBotFactory();
	private ChatterBot voice;
	private ChatterBotSession session;
	private long lastSpeak;

	/**
	 * @param perso
	 */
	public ChatUtilities(BotPerso perso) {
		super(perso);
		try {
			this.voice = factory.create(ChatterBotType.CLEVERBOT);
			this.session = voice.createSession(Locale.FRANCE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void respondTo(String msg, Chat c) {
		String resp = getResponse(msg);
		if (resp != null) getPerso().getPerso().speak(c, resp);
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

	/**
	 * Return true if the bot has speaked the last X seconds
	 * 
	 * @param seconds
	 * @return
	 */
	public boolean hasSpeaked(int seconds) {
		return System.currentTimeMillis() <= lastSpeak + (seconds * 1000);
	}

	@Override
	public void shutdown() {

	}

}
