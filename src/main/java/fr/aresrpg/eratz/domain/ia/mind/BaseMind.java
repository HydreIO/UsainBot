package fr.aresrpg.eratz.domain.ia.mind;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.functional.suplier.Supplier;
import fr.aresrpg.commons.domain.util.exception.NotImplementedException;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.eratz.domain.data.Path;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;
import fr.aresrpg.eratz.domain.ia.behavior.move.*;
import fr.aresrpg.eratz.domain.util.ThreadBlocker;
import fr.aresrpg.tofumanchou.domain.data.enums.Bank;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * 
 * @since
 */
public class BaseMind implements Mind {

	private final BotPerso perso;
	private Queue<Supplier<BehaviorStopReason>> actions = new LinkedList<>();
	private boolean infinite;
	private Set<Integer> itemsToKeep = new HashSet<>();
	private boolean running;
	private ThreadBlocker blocker;

	public BaseMind(BotPerso perso) {
		this.perso = perso;

	}

	public void shutdown() {
		running = false;
		actions.clear();
	}

	@Override
	public void process() throws InterruptedException, ExecutionException {
		if (running) return;
		running = true;
		this.blocker = new ThreadBlocker(Thread.currentThread());// on set le thread comme ça on pourra pause le behavior a n'importe quel moment
		while (getPerso().getAccount().isActive()) {
			Threads.uSleep(50, TimeUnit.MILLISECONDS); // gentil cpu ! pas cramer !
			if (getActions().isEmpty()) continue;
			Supplier<BehaviorStopReason> next = getActions().poll();
			switch (next.get()) { // possibilité d'effectuer des actions selon le type de retour
				case QUANTITY_REACHED:
					getPerso().getAbilities().getBaseAbility().speak(Chat.ADMIN, "Récolte terminée.");
					break;
			}
			if (infinite) getActions().add(next); // si infinite loop ajout a la queue
		}
		running = false;
	}

	@Override
	public ThreadBlocker getBlocker() {
		return this.blocker;
	}

	@Override
	public Mind keepItems(int... itemsType) {
		Arrays.stream(itemsType).forEach(itemsToKeep::add);
		return this;
	}

	@Override
	public Mind thenFollow(int toFollow) {
		getActions().add(new FollowPlayerBehavior(getPerso(), toFollow));
		return this;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	/**
	 * @return the actions
	 */
	public Queue<Supplier<BehaviorStopReason>> getActions() {
		return actions;
	}

	/**
	 * @return the perso
	 */
	public BotPerso getPerso() {
		return perso;
	}

	@Override
	public Mind thenHarvest(Path path) {
		getActions().add(path.getHarvestBehavior(getPerso()));
		return this;
	}

	@Override
	public Mind thenDrop(int item, int quantity) {
		throw new NotImplementedException();
	}

	@Override
	public Mind thenCraft(Path path, int quantity) {
		getActions().add(path.getCraftBehavior(getPerso(), quantity));
		return this;
	}

	@Override
	public Mind thenDepositToBank(Bank bank) {
		Behavior b = null;
		switch (bank) {
			case ASTRUB:
				b = new BankAstrubDepositPath(getPerso(), getItemToKeep().stream().mapToInt(Integer::intValue).toArray());
				break;
			case SUFOKIA:
				b = new BankSufokiaDepositPath(getPerso(), getItemToKeep().stream().mapToInt(Integer::intValue).toArray());
			case AMAKNA:
				b = new BankAmaknaDepositPath(getPerso(), getItemToKeep().stream().mapToInt(Integer::intValue).toArray());
				break;
		}
		getActions().add(b);
		return this;
	}

	@Override
	public Mind thenFight(Path path) {
		throw new NotImplementedException();
	}

	@Override
	public Mind thenFeedPets() {
		throw new NotImplementedException();
	}

	@Override
	public Mind thenSell(Path path) {
		throw new NotImplementedException();
	}

	@Override
	public void thenRestart() {
		this.infinite = true;
	}

	@Override
	public Mind thenDisconnectIf(String reason, Predicate<BotPerso> condition) {
		getActions().add(() -> {
			if (condition.test(getPerso())) getPerso().disconnect(reason);
			while (!getPerso().getAccount().isOffline())
				Threads.uSleep(50, TimeUnit.MILLISECONDS); // gentil cpu ! pas cramer !
			return BehaviorStopReason.FINISHED;
		});
		return this;
	}

	@Override
	public Set<Integer> getItemToKeep() {
		return this.itemsToKeep;
	}

	@Override
	public Mind thenConnect(BotPerso perso) {
		getActions().add(() -> {
			getPerso().connect();
			return BehaviorStopReason.FINISHED;
		});
		return this;
	}

	@Override
	public Mind thenReconnect() {
		getActions().add(() -> {
			getPerso().connect();
			return BehaviorStopReason.FINISHED;
		});
		return this;
	}

	@Override
	public Mind thenWait(long time, TimeUnit unit) {
		getActions().add(() -> {
			Threads.uSleep(time, unit);
			return BehaviorStopReason.FINISHED;
		});
		return this;
	}

}
