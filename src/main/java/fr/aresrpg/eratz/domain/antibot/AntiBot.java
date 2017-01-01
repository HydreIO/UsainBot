package fr.aresrpg.eratz.domain.antibot;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.commons.domain.util.schedule.Schedule;
import fr.aresrpg.commons.domain.util.schedule.Scheduled;
import fr.aresrpg.eratz.domain.data.AccountsManager;
import fr.aresrpg.eratz.domain.data.dofus.player.BotJob;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class AntiBot implements Scheduled {

	private static final AntiBot INSTANCE = new AntiBot();
	public static final boolean ENABLED = false;

	private AntiBot() {
		if (ENABLED) Executors.SCHEDULER.register(this);
	}

	/**
	 * @return the instance
	 */
	public static AntiBot getInstance() {
		return INSTANCE;
	}

	@Schedule(rate = 15, unit = TimeUnit.SECONDS)
	public void friendList() {
		AccountsManager.getInstance().getAccounts().values().forEach(a -> {
			if (a.isBotOnline() && a.getCurrentPlayed().getBotInfos().getBotJob() == BotJob.CRASHER) {
				a.getCurrentPlayed().getAbilities().getBaseAbility().getFriendList();
			}
		});
	}

	public void notifyCrash(Perso p, String name) {
		LOGGER.severe("Bot detected ! '" + name + "'");
		//p.getMind().forceBehavior(new GroupCrashBehavior(p, name));
	}

}
