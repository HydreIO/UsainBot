package fr.aresrpg.eratz.domain.listener;

import fr.aresrpg.commons.domain.event.Listener;
import fr.aresrpg.commons.domain.event.Subscribe;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.gui.MapView;
import fr.aresrpg.tofumanchou.domain.Manchou;
import fr.aresrpg.tofumanchou.domain.data.Account;
import fr.aresrpg.tofumanchou.domain.data.map.Carte;
import fr.aresrpg.tofumanchou.domain.event.BotStartMoveEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.*;
import fr.aresrpg.tofumanchou.domain.event.player.MapJoinEvent;

import java.awt.Point;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class MapViewListener implements Listener {

	private static MapViewListener instance = new MapViewListener();

	private MapViewListener() {
		Manchou.registerEvent(this);
	}

	/**
	 * @return the instance
	 */
	public static MapViewListener getInstance() {
		return instance;
	}

	private BotPerso get(Account client) {
		return BotFather.getPerso(client);
	}

	@Subscribe
	public void onBotMove(BotStartMoveEvent e) {
		Carte map = e.getClient().getPerso().getMap();
		get(e.getClient()).getView()
				.setPath(e.getPath().stream()
						.map(frag -> new Point(Maps.getXRotated(frag.getCellId(), map.getWidth(), map.getHeight()), Maps.getYRotated(frag.getCellId(), map.getWidth(), map.getHeight())))
						.collect(Collectors.toList()));
	}

	@Subscribe
	public void onJoinMap(MapJoinEvent e) {
		BotPerso botPerso = get(e.getClient());
		System.out.println(botPerso);
		MapView.getInstance().setTitle(botPerso.getPerso().getMap().getInfos());

	}

	@Subscribe
	public void onPlayerAdd(EntityPlayerJoinMapEvent e) {
		BotPerso botPerso = get(e.getClient());
		botPerso.getView().addEntity((int) e.getPlayer().getUUID(), e.getPlayer().getCellId());
	}

	@Subscribe
	public void onMobAdd(MonsterGroupSpawnEvent e) {
		get(e.getClient()).getView().addMob((int) e.getGroup().getUUID(), e.getGroup().getCellId());
	}

	@Subscribe
	public void onMobAdd(MonsterJoinMapEvent e) {
		get(e.getClient()).getView().addMob((int) e.getMob().getUUID(), e.getMob().getCellId());
	}

	@Subscribe
	public void onNpcAdd(NpcJoinMapEvent e) {
		get(e.getClient()).getView().addNpc((int) e.getNpc().getUUID(), e.getNpc().getCellId());
	}

	@Subscribe
	public void onMove(EntityMoveEvent e) {
		get(e.getClient())
				.getView()
				.addEntity((int) e.getEntity().getUUID(), e.getEntity().getCellId());
	}

}
