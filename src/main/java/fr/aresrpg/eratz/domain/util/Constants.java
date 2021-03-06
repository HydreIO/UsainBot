/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.commons.domain.util.Randoms;

import java.util.Arrays;
import java.util.List;

public class Constants {
	public static final int PORT = 443;
	public static String VERSION = "1.29.1";
	public static final String IP = "80.239.173.166";
	public static final char DELIMITER = 0x00;
	public static final int SLEEP = 5000;
	public static List<String> ANNOYED_SENTENCE = Arrays.asList(
			"bon..",
			"-_-",
			"tin",
			"ptin",
			"hey !",
			"pfff",
			"mais ho",
			"u_u",
			"--'",
			"bon heu",
			"stop",
			"grrrr",
			"quoi ptin",
			"?");
	public static List<String> NO_BOT_SENTENCE = Arrays.asList(
			"heu..",
			"att",
			"trop de bot ici",
			"mdr",
			"excellent",
			"nice",
			"ouai",
			"x)",
			"narmol",
			"rt morray",
			"u_u",
			"stp..",
			"ok",
			"?",
			"haha");
	public static List<String> RANDOM_HARVEST = Arrays.asList(
			"ptinn",
			"ah ouai u_u",
			"casse couille tout ces bots",
			"genre",
			"excellent",
			"nice",
			"humhum",
			".. x)",
			"mon gout n'est pas fatal mais je fais parfois mal, souvent je suis dressé et sens bon la marrée, qui suis'je ?",
			"saleelul sawarim nashidum ubah",
			"jajajajajaja",
			"u_u",
			"T_T",
			"ok",
			"?",
			"haha",
			"tt la journée je ne fait que twavailler",
			"ah vwaiment",
			"jsuis un pgm de la récolte morray",
			"jkiff parler tout seul",
			"tkt",
			"jveut etre riche putain",
			"hhhhhhhhhhhhhh",
			"frahiko jle baise",
			"oops fail canal",
			"bot a la con");

	public static String getRandomHarvestSpeach() {
		return RANDOM_HARVEST.get(Randoms.nextInt(RANDOM_HARVEST.size() - 1));
	}

	public static String getRandomAnnoyedSpeach() {
		return ANNOYED_SENTENCE.get(Randoms.nextInt(ANNOYED_SENTENCE.size() - 1));
	}
}
