package fr.aresrpg.eratz.domain.data.player.info;

import fr.aresrpg.eratz.domain.data.dofus.player.BotJob;
import fr.aresrpg.eratz.domain.data.player.Perso;

import java.awt.Point;

/**
 * 
 * @since
 */
public class BotInfo extends Info {

	private BotJob botJob;
	private boolean sit;
	private Point followedCoords;
	private int blockedOn; //(cellid) dans le pathfinder quand le chemin n'est pas trouvé la pos est stockée içi, si le chemin est trouvé alors elle est mis a -1
	private long lastMove; // ms de la derniere fois ou le bot a bougé, utile pour detecter si il est blocké

	/**
	 * @param perso
	 */
	public BotInfo(Perso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {
		sit = false;
	}

	/**
	 * @return the lastMove
	 */
	public long getLastMove() {
		return lastMove;
	}

	/**
	 * @return the followedCoords
	 */
	public Point getFollowedCoords() {
		return followedCoords;
	}

	/**
	 * @param followedCoords
	 *            the followedCoords to set
	 */
	public void setFollowedCoords(Point followedCoords) {
		this.followedCoords = followedCoords;
	}

	/**
	 * @param lastMove
	 *            the lastMove to set
	 */
	public void setLastMove(long lastMove) {
		this.lastMove = lastMove;
	}

	/**
	 * @return the blockedOn
	 */
	public int getBlockedOn() {
		return blockedOn;
	}

	/**
	 * @param blockedOn
	 *            the blockedOn to set
	 */
	public void setBlockedOn(int blockedOn) {
		this.blockedOn = blockedOn;
	}

	public boolean isBlockedOnACell() {
		return blockedOn != -1;
	}

	/**
	 * @return the sit
	 */
	public boolean isSit() {
		return sit;
	}

	/**
	 * @param sit
	 *            the sit to set
	 */
	public void setSit(boolean sit) {
		this.sit = sit;
	}

	/**
	 * @return the botJob
	 */
	public BotJob getBotJob() {
		return botJob;
	}

	/**
	 * @param botJob
	 *            the botJob to set
	 */
	public void setBotJob(BotJob botJob) {
		this.botJob = botJob;
	}

	@Override
	public String toString() {
		return "BotInfo [botJob=" + botJob + super.toString() + "]";
	}

}
