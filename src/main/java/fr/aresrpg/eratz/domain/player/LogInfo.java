package fr.aresrpg.eratz.domain.player;

/**
 * 
 * @since
 */
public class LogInfo extends Info {

	private final LogFightInfo fight = new LogFightInfo();

	/**
	 * @param perso
	 */
	public LogInfo(Perso perso) {
		super(perso);
	}

	/**
	 * @return the fight
	 */
	public LogFightInfo getFight() {
		return fight;
	}

}