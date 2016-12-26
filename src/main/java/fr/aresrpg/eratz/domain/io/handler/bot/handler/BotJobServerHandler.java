package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import fr.aresrpg.dofus.structures.job.*;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.std.job.JobServerHandler;

/**
 * 
 * @since
 */
public class BotJobServerHandler extends BotHandlerAbstract implements JobServerHandler {

	/**
	 * @param perso
	 */
	public BotJobServerHandler(Perso perso) {
		super(perso);
	}

	@Override
	public void onPlayerJobInfo(Job jobs) {

	}

	@Override
	public void onJobXp(JobInfo infos) {

	}

	@Override
	public void onJobLvl(Jobs job, int lvl) {

	}

}
