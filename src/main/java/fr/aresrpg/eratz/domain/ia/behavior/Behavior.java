/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia.behavior;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.functional.suplier.Supplier;
import fr.aresrpg.eratz.domain.data.player.Perso;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public abstract class Behavior implements Supplier<BehaviorStopReason> {

	private Perso perso;
	private boolean isDone;
	private boolean isCancelled;
	private Queue<Runnable> moves = new LinkedList<>();
	private Queue<Runnable> path = new LinkedList<>();
	private boolean pathEnd;

	public Behavior(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	public void reset() {
		isDone = true;
	}

	/**
	 * State machine to know if the current path is part of the behavior or is just the road to reach the behavior zone
	 */
	protected void switchMove() {
		this.pathEnd = !pathEnd;
	}

	public boolean isOnZone() {
		return this.pathEnd;
	}

	protected void moveUp() {
		if (isOnZone()) moves.add(getPerso().getNavigation()::moveUp);
		else path.add(getPerso().getNavigation()::moveUp);
	}

	protected void moveUp(int count) {
		if (isOnZone()) moves.add(() -> getPerso().getNavigation().moveUp(count));
		else path.add(() -> getPerso().getNavigation().moveUp(count));
	}

	protected void moveDown() {
		if (isOnZone()) moves.add(getPerso().getNavigation()::moveDown);
		else path.add(getPerso().getNavigation()::moveDown);
	}

	protected void moveDown(int count) {
		if (isOnZone()) moves.add(() -> getPerso().getNavigation().moveDown(count));
		else path.add(() -> getPerso().getNavigation().moveDown(count));
	}

	protected void moveLeft() {
		if (isOnZone()) moves.add(getPerso().getNavigation()::moveLeft);
		else path.add(getPerso().getNavigation()::moveLeft);
	}

	protected void moveLeft(int count) {
		if (isOnZone()) moves.add(() -> getPerso().getNavigation().moveLeft(count));
		else path.add(() -> getPerso().getNavigation().moveLeft(count));
	}

	protected void moveRight() {
		if (isOnZone()) moves.add(getPerso().getNavigation()::moveRight);
		else path.add(getPerso().getNavigation()::moveRight);
	}

	protected void moveRight(int count) {
		if (isOnZone()) moves.add(() -> getPerso().getNavigation().moveRight(count));
		else path.add(() -> getPerso().getNavigation().moveRight(count));
	}

	protected Runnable nextZoneMove() {
		if (!getPerso().getMind().getForcedActions().isEmpty()) return () -> {
			for (int i = 0; i < getPerso().getMind().getForcedActions().size(); i++)
				getPerso().getMind().getForcedActions().poll().run();
			moves.poll();
		};
		return moves.poll();
	}

	protected Runnable nextPathMove() {
		if (!getPerso().getMind().getForcedActions().isEmpty()) return () -> {
			for (int i = 0; i < getPerso().getMind().getForcedActions().size(); i++)
				getPerso().getMind().getForcedActions().poll().run();
			path.poll();
		};
		return path.poll();
	}

	protected int zoneMoveCount() {
		return moves.size();
	}

	protected int pathMoveCount() {
		return path.size();
	}

	public <T extends Behavior> T botWait(int time, TimeUnit unit) {
		try {
			Threads.sleep(time, unit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return (T) this;
	}

	public <T extends Behavior> T waitSec(int sec) {
		return botWait(sec, TimeUnit.SECONDS);
	}

	public abstract BehaviorStopReason start();

	private BehaviorStopReason run() {
		BehaviorStopReason result = start();
		reset();
		return result;
	}

	public boolean cancel() {
		getPerso().disconnect("Cancel du behavior"); // seul moyen de stop immédiatement n'importe quel action
		getPerso().connectIn(3, TimeUnit.SECONDS);
		return true;
	}

	@Override
	public BehaviorStopReason get() {
		getPerso().setCurrentBehavior(this);
		return run();
	}

}
