package fr.aresrpg.eratz.domain.ia.ability.harvest;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.protocol.basic.client.BasicUseSmileyPacket;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.dofus.structures.job.Jobs;
import fr.aresrpg.eratz.domain.TheBotFather;
import fr.aresrpg.eratz.domain.data.dofus.player.Smiley;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.object.Ressource;
import fr.aresrpg.eratz.domain.data.player.state.PlayerState;
import fr.aresrpg.eratz.domain.util.Constants;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class HarvestAbilityImpl implements HarvestAbility {

	private Perso perso;
	private Ressource lastRess;
	private long lastHarvest;
	private int lastCellToHarvest;
	private final ScheduledFuture unblocker;

	public HarvestAbilityImpl(Perso perso) {
		this.perso = perso;
		this.unblocker = Executors.SCHEDULER.register(this::unblock, 3, TimeUnit.SECONDS);
	}

	@Override
	public void shutdown() {
		unblocker.cancel(true);
	}

	public void unblock() {
		if (getPerso().isInFight()) return;
		if (lastHarvest + 15000 > System.currentTimeMillis()) return;
		if (lastCellToHarvest == getPerso().getMapInfos().getCellId() && lastRess != null) {
			TheBotFather.LOGGER.error("Le bot était bloqué sur la ressource [" + lastRess.getType() + "][" + lastRess + "] ! | Débloquage");
			getPerso().setState(PlayerState.IDLE);
			getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
		}
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	@Override
	public void harvest(Ressource r, Skills skill) {
		while (getPerso().getState() == PlayerState.HARVESTING)
			Threads.uSleep(50, TimeUnit.MILLISECONDS);
		Jobs[] rj = r.getType().getRequiredJob();
		boolean useDiagonale = true; // ArrayUtils.contains(Jobs.JOB_PAYSAN, rj) || ArrayUtils.contains(Jobs.JOB_ALCHIMISTE, rj);
		int cl = -1;
		List<Integer> tested = new ArrayList<>();
		do {
			cl = r.getNeighborCell(getPerso().getMapInfos().getMap(), useDiagonale, tested);
			if (cl == -1) {
				TheBotFather.LOGGER.severe("Impossible de trouver une cellule pour la ressource " + r);
				getPerso().getNavigation().moveToRandomCell();
				return;
			}
			if (getPerso().getMapInfos().getCellId() != cl)
				getPerso().getNavigation().moveToCell(cl);
			else break;
			tested.add(cl);
			TheBotFather.LOGGER.error("Impossible d'acceder à la cellule pour récolter '" + r.getType() + "' ! Switch de cellule");
		} while (getPerso().getMapInfos().getCellId() != cl); // path non trouvé (par exemple a cause de mob agressifs)
		getPerso().setState(PlayerState.HARVESTING);
		if (getPerso().getMapInfos().getMap().getPlayers().size() > 1 && Randoms.nextBool())
			getPerso().sendPacketToServer(new BasicUseSmileyPacket().setSmileyId(Smiley.getRandomTrollSmiley().getId()));
		if (Randoms.nextInt(4) == 1 && Instant.ofEpochMilli(getPerso().getChatInfos().getLastSpeak()).plusSeconds(20).isAfter(Instant.now()))
			getPerso().getAbilities().getBaseAbility().speak(Chat.COMMON, Constants.RANDOM_HARVEST.get(Randoms.nextInt(Constants.RANDOM_HARVEST.size())));
		this.lastRess = r;
		this.lastCellToHarvest = getPerso().getMapInfos().getCellId();
		this.lastHarvest = System.currentTimeMillis();
		getPerso().getAbilities().getBaseAbility().interract(skill, r.getId());
		this.lastRess = null;
	}

}
