package fr.aresrpg.eratz.domain.io.handler.std.craft;

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
