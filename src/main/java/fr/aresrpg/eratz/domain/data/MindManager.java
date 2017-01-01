package fr.aresrpg.eratz.domain.data;

import static fr.aresrpg.eratz.domain.BotFather.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.behavior.fight.type.CraFeuFightBehavior;
import fr.aresrpg.eratz.domain.ia.behavior.fight.type.PassTurnBehavior;
import fr.aresrpg.tofumanchou.domain.data.enums.Bank;
import fr.aresrpg.tofumanchou.domain.data.enums.DofusItems2;

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

	public void followPlayer(BotPerso p, int tofollow) {
		LOGGER.success(p.getPseudo() + " va suivre " + tofollow + " !");
		p.getFightInfos().setCurrentFightBehavior(new PassTurnBehavior(p));
		p.getAbilities().getBaseAbility().followGroupMember(tofollow);
		Threads.uSleep(1, TimeUnit.SECONDS);
		p.getMind().thenFollow(tofollow);
	}

	public void lvlUpPaysan(BotPerso p) {
		LOGGER.success(p.getPseudo() + " va monter son métier paysan !");
		p.getFightInfos().setCurrentFightBehavior(new PassTurnBehavior(p));
		p.getMind().thenHarvest(Path.BLE).thenDepositToBank(Bank.ASTRUB).thenRestart();
	}

	public void lvlUpBucheron(BotPerso p) {
		LOGGER.success(p.getPseudo() + " va monter son métier bucheron !");
		p.getFightInfos().setCurrentFightBehavior(new CraFeuFightBehavior(p));
		int hache = DofusItems2.HACHE_DE_L_APPRENTI_BÛCHERON.getId();
		p.setCanDestroyItems(true);
		p.getMind().keepItems(hache).thenHarvest(Path.BUCHERON_AMAKNA).thenDepositToNearestBank().thenRestart();
	}

}
