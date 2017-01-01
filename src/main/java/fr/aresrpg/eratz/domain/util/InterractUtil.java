package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.dofus.structures.job.Jobs;

/**
 * 
 * @since
 */
public class InterractUtil {

	public static Skills getSkillFor(Interractable i, Jobs j) {
		if (!ArrayUtils.contains(j, i.getRequiredJob())) return null;
		for (Skills s : Skills.values())
			if (s.getType() == i) return s;
		return null;
	}

}
