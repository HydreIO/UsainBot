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
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public abstract class FightBehavior extends Info {

	private CompletableFuture<Void> waiterStat;
	private CompletableFuture<Void> waiterPa;
	private CompletableFuture<Void> waiterPm;

	/**
	 * @param perso
	 */
	public FightBehavior(BotPerso perso) {
		super(perso);
	}

	protected void useSpell(ManchouSpell spell, int cell) {
		if (spell == null) return;
		LOGGER.debug("using " + spell.getLangspell().getName() + " on " + cell);
		getPerso().getPerso().launchSpell(spell, cell);
		waitUntilPaReceive();
	}

	protected void run(int cellid) {
		util().runTo(cellid);
		waitUntilPmReceive();
	}

	protected boolean hasPaToLaunch(ManchouSpell spell) {
		LOGGER.warning("PA = " + player().getPa());
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

	protected List<Entity> ennemiesAccessibles(ManchouSpell spell) {
		List<Integer> accessibleCells = util().getAccessibleCells(util().getMaxPoFor(spell));
		List<Entity> entities = new ArrayList<>();
		for (int ceid : accessibleCells) {
			ManchouCell cell = map().getCells()[ceid];
			if (cell.hasLivingEntityOn()) entities.addAll(cell.getEntitiesOn().stream().filter(e -> !util().isAlly(e)).collect(Collectors.toList()));
		}
		return entities;
	}

	protected List<Entity> getCacEntities() {
		return Arrays.stream(player().getNeighborsWithoutDiagonals()).filter(ManchouCell::hasLivingEntityOn).filter(c -> {
			if (util().isAlly(c.getEntitiesOn().iterator().next())) return false;
			return true;
		}).map(c -> c.getEntitiesOn().stream().findFirst().get())
				.collect(Collectors.toList());
	}

	protected boolean hasCacEntities() {
		return !getCacEntities().isEmpty();
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

	protected Set<Entity> ennemies() {
		return entities().stream().filter(e -> !e.isDead() && !util().isAlly(e)).collect(Collectors.toSet());
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
		waiterStat = new CompletableFuture<>();
		waiterStat.join();
	}

	public void notifyStatsReceive() {
		LOGGER.debug("stats received !");
		if (waiterStat != null) waiterStat.complete(null);
	}

	private void waitUntilPaReceive() {
		LOGGER.debug("waiting until pa !");
		waiterPa = new CompletableFuture<>();
		waiterPa.join();
	}

	public void notifyPaReceive() {
		LOGGER.debug("pa received !");
		if (waiterPa != null) waiterPa.complete(null);
	}

	private void waitUntilPmReceive() {
		LOGGER.debug("waiting until pm !");
		waiterPm = new CompletableFuture<>();
		waiterPm.join();
	}

	public void notifyPmReceive() {
		LOGGER.debug("pm received !");
		if (waiterPm != null) waiterPm.complete(null);
	}

	protected int po() {
		return player().getStat(Stat.PO).getTotal();
	}

	protected boolean canLaunch(ManchouSpell spell, int cell) {
		if (spell == null) return false;
		return spell.getRelance() == 0 && hasPaToLaunch(spell) && hasPorteToLaunch(spell, cell) && hasPaToLaunch(spell) && spell.getProperty().getMinPlayerLvl() <= getPerso().getPerso().getLevel();
	}

	protected boolean tryToHide() {
		if (pm() < 1) return false;
		Set<Integer> all = new HashSet<>();
		entities().forEach(e -> all.addAll(util().getAccessibleCells(e.getCellId(), 63)));
		Set<ManchouCell> cellsAwayFromMobs = util().getCellsAwayFromMobs(pm());
		cellsAwayFromMobs.removeIf(c -> all.contains(c.getId()));
		if (cellsAwayFromMobs.isEmpty()) return false;
		int id = cellsAwayFromMobs.stream().findAny().get().getId();
		if (util().pathValidFor(id)) run(id);
		else return false;
		return true;
	}

	protected boolean tryToRunAway() {
		if (pm() < 1 || !util().runAwayFromMobs()) return false;
		waitUntilPmReceive();
		return true;
	}

	public void playTurn() {
		decrementRelance();
		turn();
		player().endTurn();
	}

	@Override
	public void shutdown() {
	}

	protected abstract void turn();

	protected abstract void decrementRelance();

}
