package fr.aresrpg.eratz.domain.ia.navigation;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.event.EventBus;
import fr.aresrpg.commons.domain.event.Listener;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.Interrupt;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityPlayerJoinMapEvent;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 
 * @since
 */
public class Navigator implements Supplier<Interrupt>, Listener {

	private BotPerso perso;
	private BotMap destination;
	private boolean end;

	/**
	 * @param perso
	 * @param destination
	 */
	public Navigator(BotPerso perso, BotMap destination) {
		Objects.requireNonNull(destination);
		Objects.requireNonNull(perso);
		this.perso = perso;
		this.destination = destination;
		ManchouMap mMap = perso.getPerso().getMap();
		BotMap current = MapsManager.getOrCreateMap(mMap);
		if (current.equals(destination)) end = true;
		else {
			EventBus.getBus(EntityPlayerJoinMapEvent.class).subscribe(this::onMap, 0);
			// TODO get the path
		}
	}

	void onMap(EntityPlayerJoinMapEvent e) {
		BotPerso p = BotFather.getPerso(e.getClient());
		if (!perso.equals(p)) return; // if null or not equals
		// TODO go to next node
	}

	@Override
	public Interrupt get() {
		while (!end)
			Threads.uSleep(100, TimeUnit.MILLISECONDS);
		return Interrupt.PATH_END;
	}

}
