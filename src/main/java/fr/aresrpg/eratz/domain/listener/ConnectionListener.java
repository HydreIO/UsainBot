package fr.aresrpg.eratz.domain.listener;

import fr.aresrpg.commons.domain.event.Listener;
import fr.aresrpg.commons.domain.event.Subscribe;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.tofumanchou.domain.Manchou;
import fr.aresrpg.tofumanchou.domain.data.Account;
import fr.aresrpg.tofumanchou.domain.event.*;
import fr.aresrpg.tofumanchou.domain.event.player.PersoSelectEvent;
import fr.aresrpg.tofumanchou.infra.data.ManchouPerso;

import java.util.Collection;

/**
 * 
 * @since
 */
public class ConnectionListener implements Listener {

	public ConnectionListener() {
		Manchou.registerEvent(this);
	}

	@Subscribe
	public void onConnect(PersoSelectEvent e) {
		ManchouPerso perso = (ManchouPerso) e.getPerso();
		if (!perso.isMitm()) BotFather.getPerso(perso.getUUID()).setOnline(true);
		else {
			BotPerso botPerso = new BotPerso(perso);
			botPerso.setOnline(true);
			BotFather.getInstance().getPersos().put(perso.getUUID(), botPerso);
		}
	}

	@Subscribe
	public void onDisconnect(BotDisconnectEvent e) {
		ManchouPerso perso = (ManchouPerso) e.getPerso();
		BotFather.getPerso(perso.getUUID()).setOnline(false);
	}

	@Subscribe
	public void onCrash(ClientCrashEvent e) {
		Account client = e.getClient();
		Collection<BotPerso> values = BotFather.getInstance().getPersos().values();
		for (BotPerso p : values)
			if (p.getPerso().getAccount().equals(client)) p.setOnline(false);
	}

	@Subscribe
	public void onAtk(PacketSpamEvent e) {
		if (e.getCount() < 10) return;
		BotFather.broadcastServerMsg("Overflow detected ! Mitigation..");
		BotFather.broadcastServerMsg(e.getCount() + " of [" + e.getClazz().getSimpleName() + "] were dropped.");
	}

}
