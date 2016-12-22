package fr.aresrpg.eratz.domain.data.player.info;

import fr.aresrpg.eratz.domain.util.Closeable;

/**
 * 
 * @since
 */
public class LogFightInfo implements Closeable {

	private int totalFight;

	private long totalFightTime;
	private int minFightTime;
	private int maxFightTime;

	private long totalKamaDrop;
	private int minKamaDrop;
	private int maxKamaDrop;

	private int kamaPerHour;

	/**
	 * @param perso
	 */
	public LogFightInfo() {
		restartLog();
	}

	@Override
	public void shutdown() {

	}

	public void endFight(int fightTime, int kamaDrop) {
		totalFight++;
		totalFightTime += fightTime;
		if (fightTime < minFightTime) minFightTime = fightTime;
		if (fightTime > maxFightTime) maxFightTime = fightTime;
		if (kamaDrop < minKamaDrop) minKamaDrop = kamaDrop;
		if (kamaDrop > maxKamaDrop) maxKamaDrop = kamaDrop;
	}

	public long getMoyenneTimePerFight() {
		return totalFightTime / totalFight;
	}

	public long getMoyenneKamasDropPerFight() {
		return totalKamaDrop / totalFight;
	}

	public void restartLog() {
		totalFightTime = 0;
		totalFight = 0;
		minFightTime = 0;
		maxFightTime = 0;
		totalKamaDrop = 0;
		minKamaDrop = 0;
		maxKamaDrop = 0;
		kamaPerHour = 0;
	}

	/**
	 * @return the totalFight
	 */
	public int getTotalFight() {
		return totalFight;
	}

	/**
	 * @return the minFightTime
	 */
	public int getMinFightTime() {
		return minFightTime;
	}

	/**
	 * @return the maxFightTime
	 */
	public int getMaxFightTime() {
		return maxFightTime;
	}

	/**
	 * @return the totalFightTime
	 */
	public long getTotalFightTime() {
		return totalFightTime;
	}

	/**
	 * @return the totalKamaDrop
	 */
	public long getTotalKamaDrop() {
		return totalKamaDrop;
	}

	/**
	 * @return the minKamaDrop
	 */
	public int getMinKamaDrop() {
		return minKamaDrop;
	}

	/**
	 * @return the maxKamaDrop
	 */
	public int getMaxKamaDrop() {
		return maxKamaDrop;
	}

	/**
	 * @return the kamaPerHour
	 */
	public int getKamaPerHour() {
		return kamaPerHour;
	}

	@Override
	public String toString() {
		return "LogFightInfo [totalFight=" + totalFight + ", totalFightTime=" + totalFightTime + ", minFightTime=" + minFightTime + ", maxFightTime=" + maxFightTime + ", totalKamaDrop="
				+ totalKamaDrop + ", minKamaDrop=" + minKamaDrop + ", maxKamaDrop=" + maxKamaDrop + ", kamaPerHour=" + kamaPerHour + "]";
	}

}
