package fr.aresrpg.eratz.domain.data;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.fight.type.PassTurnBehavior;

/**
 * 
 * @since
 */
public class MindManager {

	private static final MindManager INSTANCE = new MindManager();

	private MindManager() {
	}

	/**
	 * @return the instance
	 */
	public static MindManager getInstance() {
		return INSTANCE;
	}

	public void followPlayer(Perso p, String tofollow) {
		LOGGER.success(p.getPseudo() + " va suivre " + tofollow + " !");
		p.getFightInfos().setCurrentFightBehavior(new PassTurnBehavior(p));
		p.getAbilities().getBaseAbility().followGroupMember(tofollow);
		p.getMind().thenFollow(tofollow).thenRestart();
	}

}
