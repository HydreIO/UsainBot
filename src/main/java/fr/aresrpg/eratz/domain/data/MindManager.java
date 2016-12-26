package fr.aresrpg.eratz.domain.data;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.eratz.domain.data.dofus.map.Path;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.fight.type.PassTurnBehavior;

import java.util.concurrent.TimeUnit;

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

	public void followPlayer(Perso p, int tofollow) {
		LOGGER.success(p.getPseudo() + " va suivre " + tofollow + " !");
		p.getFightInfos().setCurrentFightBehavior(new PassTurnBehavior(p));
		p.getAbilities().getBaseAbility().followGroupMember(tofollow);
		Threads.uSleep(1, TimeUnit.SECONDS);
		p.getMind().thenFollow(tofollow);
	}

	public void lvlUpPaysan(Perso p) {
		LOGGER.success(p.getPseudo() + " va monter son métier paysan !");
		p.getFightInfos().setCurrentFightBehavior(new PassTurnBehavior(p));
		p.getMind().thenHarvest(Path.BLE).thenDepositToBank().thenRestart();
	}

}
