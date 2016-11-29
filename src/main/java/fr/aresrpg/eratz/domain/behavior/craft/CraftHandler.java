package fr.aresrpg.eratz.domain.behavior.craft;

import fr.aresrpg.eratz.domain.dofus.job.JobAction;

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
