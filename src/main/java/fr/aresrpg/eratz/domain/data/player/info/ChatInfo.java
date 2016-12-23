package fr.aresrpg.eratz.domain.data.player.info;

import fr.aresrpg.dofus.protocol.chat.ChatSubscribeChannelPacket;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.eratz.domain.data.player.Perso;

import java.util.*;

/**
 * 
 * @since
 */
public class ChatInfo extends Info {

	private Map<Chat, Boolean> chats = new HashMap<>();

	/**
	 * @param perso
	 */
	public ChatInfo(Perso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {

	}

	/**
	 * @return the chats
	 */
	public Map<Chat, Boolean> getChats() {
		return chats;
	}

	public void activate(Chat... chts) {
		Arrays.stream(chts).forEach(c -> chats.put(c, true));
		ChatSubscribeChannelPacket pkt = new ChatSubscribeChannelPacket();
		pkt.setAdd(true);
		pkt.setChannels(chts);
		getPerso().sendPacketToServer(pkt);
	}

	public void desactivate(Chat... chts) {
		Arrays.stream(chts).forEach(c -> chats.put(c, false));
		ChatSubscribeChannelPacket pkt = new ChatSubscribeChannelPacket();
		pkt.setAdd(false);
		pkt.setChannels(chts);
		getPerso().sendPacketToServer(pkt);
	}

}
