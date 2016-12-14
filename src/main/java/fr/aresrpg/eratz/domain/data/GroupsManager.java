package fr.aresrpg.eratz.domain.data;

import fr.aresrpg.eratz.domain.data.player.object.Group;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @since
 */
public class GroupsManager {

	private static GroupsManager instance = new GroupsManager();
	private Set<Group> groups = new HashSet<>();

	/**
	 * @return the instance
	 */
	public static GroupsManager getInstance() {
		return instance;
	}

	/**
	 * @return the groups
	 */
	public Set<Group> getGroups() {
		return groups;
	}

}
