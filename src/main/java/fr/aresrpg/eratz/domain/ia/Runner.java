package fr.aresrpg.eratz.domain.ia;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.tofumanchou.domain.data.enums.Bank;
import fr.aresrpg.tofumanchou.domain.data.enums.Smiley;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.ManchouPerso;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public abstract class Runner extends Info {

	private int errorCount;

	/**
	 * @param perso
	 */
	public Runner(BotPerso perso) {
		super(perso);
	}

	protected CompletableFuture<?> onFullPod() {
		getPerso().getUtilities().useRessourceBags();
		Threads.uSleep(1, TimeUnit.SECONDS);
		getPerso().getUtilities().destroyHeaviestRessource();
		Threads.uSleep(1, TimeUnit.SECONDS);
		return getPerso().getMind().moveToMap(MapsManager.getMap(Bank.BONTA.getMapId())).thenRunAsync(() -> {
			getPerso().getUtilities().openBank();
			Threads.uSleep(1, TimeUnit.SECONDS);
			getPerso().getUtilities().depositBank();
			Threads.uSleep(1, TimeUnit.SECONDS);
		}, Executors.FIXED);
	}

	protected CompletableFuture<?> recoverAfterActionError(CompletableFuture<?> next) {
		errorCount++;
		ManchouPerso p = getPerso().getPerso();
		Threads.uSleep(Randoms.nextBetween(1, 3), TimeUnit.SECONDS);
		if (p.isDefied()) p.cancelDefiInvit();
		if (p.isInvitedExchange()) p.cancelExchangeInvit();
		if (p.isInvitedGrp()) p.cancelGroupInvit();
		if (p.isInvitedGuild()) p.cancelGuildInvit();
		Threads.uSleep(1, TimeUnit.SECONDS);
		if (Randoms.nextBool()) getPerso().getPerso().sendSmiley(Smiley.getRandomBadSmiley());
		else getPerso().speakIfAnnoyed();
		p.cancelRunner();
		p.endAction();
		Threads.uSleep(1, TimeUnit.SECONDS);
		getPerso().refreshMap();
		Threads.uSleep(2, TimeUnit.SECONDS);
		if (errorCount > 20) {
			errorCount = 0;
			getPerso().getPerso().disconnect();
			return getPerso().getMind().connect(10, TimeUnit.SECONDS);
		}
		return next;
	}
}
