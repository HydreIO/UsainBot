package fr.aresrpg.eratz.domain.data;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.object.Group;
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

	public void updateGroups(Perso p) {
		Variables.GROUPS.forEach(g -> {
			if (g.getChef().equalsIgnoreCase(p.getPseudo())) p.setGroup(groups.get(g.getLabel()));
			else
				for (String s : g.getMembers()) {
				if (s.equalsIgnoreCase(p.getPseudo())) {
					Group group = groups.get(g.getLabel());
					group.getMembers().add(p);
					p.setGroup(group);
				}
			}
		});
	}

	/**
	 * Permet d'update les groupes en fonction des comptes initialisés
	 */
	public void fetchGroup() {
		Variables.GROUPS.forEach(g -> {
			Perso boss = AccountsManager.getInstance().getPerso(g.getChef());
			if (boss == null) LOGGER.warning("Unable to fetch group '" + g.getLabel() + "' | Boss '" + g.getChef() + "' doesn't exist !");
			else {
				Group gr = new Group(g.getLabel(), boss);
				for (String s : g.getMembers()) {
					Perso memb = AccountsManager.getInstance().getPerso(s);
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
