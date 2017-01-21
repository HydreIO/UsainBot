package fr.aresrpg.eratz.domain.ia.fight.behavior;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.dofus.structures.stat.Stat;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.FightUtilities;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.tofumanchou.domain.data.entity.Entity;
import fr.aresrpg.tofumanchou.infra.data.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * 
 * @since
 */
public abstract class FightBehavior extends Info {

	private CompletableFuture<Void> waiter;

	/**
	 * @param perso
	 */
	public FightBehavior(BotPerso perso) {
		super(perso);
	}

	protected void useSpell(ManchouSpell spell, int cell) {
		getPerso().getPerso().launchSpell(spell, cell);
		waitUntilStatsReceive();
	}

	protected void run(int cellid) {
		util().runTo(cellid);
		waitUntilStatsReceive();
	}

	protected boolean hasPaToLaunch(ManchouSpell spell) {
		return player().getPa() >= spell.getPaCost();
	}

	protected boolean hasPorteToLaunch(ManchouSpell spell, int cible) {
		return map().getCells()[playerCell()].distanceManathan(cible) <= spell.getMaxPo() + (spell.getProperty().isPoModifiable() ? po() : 0);
	}

	protected boolean isCac(Entity e) {
		return map().getCells()[playerCell()].distanceManathan(e.getCellId()) == 1;
	}

	protected boolean isAccessible(ManchouSpell spell, Entity e) {
		return isAccessible(spell, e.getCellId());
	}

	protected boolean isAccessible(ManchouSpell spell, int cell) {
		return util().getAccessibleCells(util().getMaxPoFor(spell)).contains(cell);
	}
	
	protected List<Entity> getCacEntities() {
		player().getCell();
	}

	protected FightUtilities util() {
		return getPerso().getFightUtilities();
	}

	protected ManchouMap map() {
		return getPerso().getPerso().getMap();
	}

	protected ManchouPerso player() {
		return getPerso().getPerso();
	}

	protected Collection<Entity> entities() {
		return getPerso().getPerso().getMap().getEntities().values();
	}

	protected int playerCell() {
		return getPerso().getPerso().getCellId();
	}

	protected int pm() {
		return player().getPm();
	}

	protected int pa() {
		return player().getPa();
	}

	private void waitUntilStatsReceive() {
		LOGGER.debug("waiting until stats !");
		waiter = new CompletableFuture<>();
		waiter.join();
	}

	public void notifyStatsReceive() {
		LOGGER.debug("stats received !");
		if (waiter != null) waiter.complete(null);
	}

	protected int po() {
		return player().getStat(Stat.PO).getTotal();
	}

	protected boolean canLaunch(ManchouSpell spell, int cell) {
		return spell.getRelance() == 0 && hasPaToLaunch(spell) && hasPorteToLaunch(spell, cell) && spell.getProperty().getMinPlayerLvl() <= getPerso().getPerso().getLevel();
	}

	protected boolean tryToHide() {
		if (pm() < 1) return false;
		Set<Integer> all = new HashSet<>();
		entities().forEach(e -> all.addAll(util().getAccessibleCells(e.getCellId(), 63)));
		Set<ManchouCell> cellsAwayFromMobs = util().getCellsAwayFromMobs(pm());
		cellsAwayFromMobs.removeIf(c -> all.contains(c.getId()));
		if (cellsAwayFromMobs.isEmpty()) return false;
		run(all.stream().findAny().get());
		return true;
	}

	protected boolean tryToRunAway() {
		if (pm() < 1) return false;
		util().runAwayFromMobs();
		waitUntilStatsReceive();
		return true;
	}

	public void playTurn() {
		decrementRelance();
		turn();
	}

	@Override
	public void shutdown() {
	}

	protected abstract void turn();

	protected abstract void decrementRelance();

}
