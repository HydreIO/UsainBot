package fr.aresrpg.eratz.domain.std.job;

import fr.aresrpg.dofus.structures.job.*;

/**
 * 
 * @since
 */
public interface JobServerHandler {

	void onPlayerJobInfo(Job jobs);

	void onJobXp(JobInfo infos);

	void onJobLvl(Jobs job, int lvl);

}
