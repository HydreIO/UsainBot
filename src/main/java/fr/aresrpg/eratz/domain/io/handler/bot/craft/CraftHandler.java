package fr.aresrpg.eratz.domain.io.handler.bot.craft;

import fr.aresrpg.eratz.domain.data.dofus.job.JobAction;

/**
 * 
 * @since
 */
public interface CraftHandler {

	void onOpenGui(JobAction a);

	void onCloseGui();

	void onStartCraft();

	void onFinishCraft();

}
