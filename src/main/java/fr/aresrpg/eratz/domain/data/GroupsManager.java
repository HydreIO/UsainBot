package fr.aresrpg.eratz.domain.data;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.Group;
import fr.aresrpg.tofumanchou.infra.config.Variables;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @since
 */
public class GroupsManager {

	private static final GroupsManager instance = new GroupsManager();
	private Map<String, Group> groups = new HashMap<>();

	private GroupsManager() {
		fetchGroup();
	}

	public void updateGroups(BotPerso p) {
		groups.values().forEach(g -> {
			if (g.getBoss().equals(p) || g.getMembers().contains(p)) p.setGroup(g);
		});
	}

	/**
	 * Permet d'update les groupes en fonction des comptes initialisés
	 */
	public void fetchGroup() {
		Variables.GROUPS.forEach(g -> {
			BotPerso boss = BotFather.getPerso(g.getChef(), g.getServerObject());
			if (boss == null) LOGGER.warning("Unable to fetch group '" + g.getLabel() + "' | Boss '" + g.getChef() + "' doesn't exist !");
			else {
				Group gr = new Group(g.getLabel(), boss);
				for (String s : g.getMembers()) {
					BotPerso memb = BotFather.getPerso(s, g.getServerObject());
					if (memb == null) LOGGER.warning("Unable to add '" + s + "' in group '" + gr.getLabel() + "' | Perso doesn't exist !");
					else {
						gr.getMembers().add(memb);
						memb.setGroup(gr);
					}
				}
				groups.put(gr.getLabel(), gr);
				LOGGER.success("Group '" + gr.getLabel() + "' initialized ! " + gr);
			}
		});
	}

	/**
	 * @return the instance
	 */
	public static GroupsManager getInstance() {
		return instance;
	}

	/**
	 * @return the groups
	 */
	public Map<String, Group> getGroups() {
		return groups;
	}
}
