package fr.aresrpg.eratz.domain.data.player.info;

import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.util.BotConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.RequestBodyEntity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @since
 */
public class ChatUtilities extends Info {

	private Map<String, Long> lastMsgByPlayer = new HashMap<>();
	private JCA jca;
	private long lastSpeak;

	/**
	 * @param perso
	 */
	public ChatUtilities(BotPerso perso) {
		super(perso);
		this.jca = new JCABuilder().setUser(BotConfig.CHAT_USER).setKey(BotConfig.CHAT_KEY).buildBlocking();
	}

	public void respondTo(String msg, Chat c) {
		if (!BotConfig.AUTO_SPEAK) return;
		String resp = getResponse(msg);
		if (resp != null) getPerso().getPerso().speak(c, resp);
	}

	public String getResponse(String s) {
		if (s.toLowerCase().contains("bot")) s = "Tu est un bot";
		String response = jca.getResponse(s);
		if (response.toLowerCase().contains("bot") || response.toLowerCase().contains("robot")) response = ":p";
		return response;
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

	/**
	 * Return true if the bot has speaked the last X seconds
	 * 
	 * @param seconds
	 * @return
	 */
	public boolean hasSpeaked(int seconds, String player) {
		return System.currentTimeMillis() <= getLastSpeak(player) + (seconds * 1000);
	}

	static class JCA {

		private String user;
		private String key;
		private String nick;

		protected JCA(String user, String key, String nick) {
			this.user = user;
			this.key = key;
			this.nick = nick;
		}

		public String getResponse(String query) {
			String status = "";
			try {
				JSONObject jsonOut = new JSONObject();
				jsonOut.put("user", user)
						.put("key", key)
						.put("nick", nick)
						.put("text", query);

				RequestBodyEntity post = Unirest.post("https://cleverbot.io/1.0/ask").header("Content-Type", "application/json")
						.body(jsonOut.toString());

				JSONObject json = post.asJson().getBody().getObject();
				status = json.getString("status");
				String response = json.getString("response");
				return response;
			} catch (JSONException | UnirestException ex) {
				throw new RuntimeException(status, ex);
			}
		}

	}

	static class JCABuilder {

		private String user;
		private String key;
		private String nick;

		public JCABuilder setUser(String user) {
			this.user = user;
			return this;
		}

		public JCABuilder setKey(String key) {
			this.key = key;
			return this;
		}

		public JCABuilder setNick(String nick) {
			this.nick = nick;
			return this;
		}

		public JCA buildBlocking() {
			try {
				JSONObject jsonOut = new JSONObject()
						.put("user", user)
						.put("key", key);

				if (nick != null) {
					jsonOut.put("nick", nick);
				}
				RequestBodyEntity post = Unirest.post("https://cleverbot.io/1.0/create").header("Content-Type", "application/json").body(jsonOut.toString());

				JSONObject json = post.asJson().getBody().getObject();
				String status = json.getString("status");

				if (!"success".equals(status)) { throw new IOException("Cleverbot responded with unexpected status: " + status); }

				nick = json.getString("nick");
				JCA jca = new JCA(user, key, nick);
				return jca;
			} catch (UnirestException | JSONException | IOException ex) {
				throw new RuntimeException(ex);
			}
		}

	}

	@Override
	public void shutdown() {

	}

}
