package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import fr.aresrpg.dofus.protocol.item.server.ItemAddErrorPacket.AddResult;
import fr.aresrpg.dofus.protocol.item.server.ItemDropErrorPacket.DropResult;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.dofus.structures.job.Jobs;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.std.item.ItemServerHandler;

import java.util.Set;

/**
 * 
 * @since
 */
public class BotItemServerHandler extends BotHandlerAbstract implements ItemServerHandler {

	/**
	 * @param perso
	 */
	public BotItemServerHandler(Perso perso) {
		super(perso);
	}

	@Override
	public void onItemsAdd(Set<Item> items) {

	}

	@Override
	public void onItemAddError(AddResult error) {
		// TODO

	}

	@Override
	public void onItemDropError(DropResult error) {
		// TODO

	}

	@Override
	public void onItemRemove(int itemUid) {
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();

	}

	@Override
	public void onItemQuantityUpdate(int itemUid, int quantity) {
		// TODO

	}

	@Override
	public void onItemMove(int itemUid, int position) {
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onItemToolEquip(Jobs job) {
	}

	@Override
	public void onPodsUpdate(int used, int max) {
		// TODO

	}

}
