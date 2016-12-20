package fr.aresrpg.eratz.domain.util.chat;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import java.util.Locale;
/**
 * 
 * @since
 */
public class Snippet {
	public static void main(String[] args) throws Exception {
		ChatterBotFactory factory = new ChatterBotFactory();

		ChatterBot bot1 = factory.create(ChatterBotType.CLEVERBOT);
		ChatterBotSession bot1session = bot1.createSession(Locale.FRANCE);

		ChatterBot bot2 = factory.create(ChatterBotType.CLEVERBOT); // factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
		ChatterBotSession bot2session = bot2.createSession(Locale.FRANCE);

		String s = "je vote Donald Trump";
		while (true) {
			Thread.sleep(1000);
			LOGGER.info("bot1> " + s);

			s = bot2session.think(s);
			LOGGER.info("bot2> " + s);

			s = bot1session.think(s);
		}
	}
}
