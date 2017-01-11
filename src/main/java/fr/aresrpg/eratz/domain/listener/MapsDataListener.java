package fr.aresrpg.eratz.domain.listener;

import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction.PathFragment;
import fr.aresrpg.dofus.protocol.game.server.GameCellUpdatePacket;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.map.trigger.*;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.infra.map.DestinationImpl;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;
import fr.aresrpg.tofumanchou.domain.event.BotDisconnectEvent;
import fr.aresrpg.tofumanchou.domain.event.ClientCrashEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityMoveEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityPlayerJoinMapEvent;
import fr.aresrpg.tofumanchou.domain.event.player.MapJoinEvent;
import fr.aresrpg.tofumanchou.infra.data.ManchouCell;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.*;

/**
 * 
 * @since
 */
public class MapsDataListener implements Listener {

	private static MapsDataListener instance = new MapsDataListener();
	private Map<BotPerso, TriggerSniffer> sniffers = new HashMap<>();
	private static List<Pair<EventBus, Subscriber>> subs = new ArrayList<>();

	private MapsDataListener() {
	}

	public static void register() {
		try {
			subs = Events.register(instance);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void unRegister() {
		subs.forEach(p -> p.getFirst().unsubscribe(p.getSecond()));
	}

	@Subscribe
	public void chechMap(MapJoinEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		MapsManager.checkUpdate((ManchouMap) e.getMap(), perso);
	}

	@Subscribe
	public void checkCrash(ClientCrashEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		sniffers.remove(perso);
	}

	@Subscribe
	public void checkDeco(BotDisconnectEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		sniffers.remove(perso);
	}

	@Subscribe
	public void onJoin(EntityPlayerJoinMapEvent e) {
		if (e.getClient().getPerso().getUUID() != e.getPlayer().getUUID()) return;
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		BotMap map = MapsManager.getOrCreateMap(perso.getPerso().getMap());
		Set<Trigger> triggers = map.getTriggers(TriggerType.TELEPORT);
		for (ManchouCell c : map.getMap().getCells()) {
			if (!c.isTeleporter() || triggerExist(c.getId(), triggers)) continue;
			GameCellUpdatePacket pkt = new GameCellUpdatePacket();
			Cell origin = c.serialize();
			Cell clone = origin.clone();
			clone.setLayerObject2Num(1202);
			pkt.addCell(clone, origin);
			perso.sendPacketToClient(pkt);
		}
		TriggerSniffer sniffer = sniffers.get(perso);
		if (sniffer == null) return;
		sniffer.complete(new DestinationImpl(perso.getPerso().getMap().getMapId(), perso.getPerso().getCellId()));
	}

	private boolean triggerExist(int cellid, Set<Trigger> triggers) {
		if (triggers == null) return false;
		for (Trigger t : triggers)
			if (t.getCellId() == cellid) return true;
		return false;
	}

	@Subscribe
	public void endMove(EntityMoveEvent e) {
		if (!(e.getEntity() instanceof Perso)) return;
		BotPerso perso = BotFather.getPerso(e.getEntity().getUUID());
		if (perso == null) return;
		PathFragment pathFragment = e.getPath().get(e.getPath().size() - 1);
		ManchouCell cell = perso.getPerso().getMap().getCells()[pathFragment.getCellId()];
		if (cell.isTeleporter()) sniffers.put(perso, TriggerSniffer.start(perso.getPerso().getMap().getMapId(), cell.getId()));
		else sniffers.remove(perso);
	}

}
