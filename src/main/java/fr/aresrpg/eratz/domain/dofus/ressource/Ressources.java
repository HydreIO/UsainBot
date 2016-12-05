/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.dofus.ressource;

import fr.aresrpg.eratz.domain.dofus.job.Job;

/**
 * 
 * @since
 */
public enum Ressources {

	FLEUR_LIN(421, Job.ALCHIMISTE),
	FLEUR_CHANVRE(428, Job.ALCHIMISTE),
	TREFLE(395, Job.ALCHIMISTE),
	FEUILLE_MENTHE(380, Job.ALCHIMISTE),
	ORCHIDEE(593, Job.ALCHIMISTE),
	EDELWEISS(594, Job.ALCHIMISTE),
	GRAINE_PANDOUILLE(58, Job.ALCHIMISTE),
	FRENE(303, Job.BUCHERON),
	CHATAIGNIER(473, Job.BUCHERON),
	NOYER(476, Job.BUCHERON),
	CHENE(460, Job.BUCHERON),
	BOMBU(2358, Job.BUCHERON),
	OLIVIOLET(2358, Job.BUCHERON),
	ERABLE(471, Job.BUCHERON),
	IF(461, Job.BUCHERON),
	BAMBOU(7013, Job.BUCHERON),
	MERISIER(474, Job.BUCHERON),
	EBENE(448, Job.BUCHERON),
	KALIPTUS(7925, Job.BUCHERON),
	CHARME(472, Job.BUCHERON),
	BAMBOU_SOMBRE(7016, Job.BUCHERON),
	ORME(470, Job.BUCHERON),
	BAMBOU_SACREE(7014, Job.BUCHERON),
	FER(312, Job.MINEUR),
	CUIVRE(441, Job.MINEUR),
	BRONZE(442, Job.MINEUR),
	KOBALTE(443, Job.MINEUR),
	MANGANESE(445, Job.MINEUR),
	ETAIN(444, Job.MINEUR),
	SILICATE(7032, Job.MINEUR),
	ARGENT(350, Job.MINEUR),
	BAUXITE(446, Job.MINEUR),
	OR(313, Job.MINEUR),
	DOLOMITE(7033, Job.MINEUR),
	BLE(289, Job.PAYSAN),
	ORGE(400, Job.PAYSAN),
	AVOINE(533, Job.PAYSAN),
	HOUBLON(401, Job.PAYSAN),
	LIN(423, Job.PAYSAN),
	RIZ(7018, Job.PAYSAN),
	SEIGLE(532, Job.PAYSAN),
	MALT(405, Job.PAYSAN),
	CHANVRE(425, Job.PAYSAN),
	PETITS_POISSONS(-1, Job.PECHEUR),
	POISSONS(-1, Job.PECHEUR),
	GROS_POISSONS(-1, Job.PECHEUR),
	POISSONS_GEANTS(-1, Job.PECHEUR);

	private int id;
	private Job requiredJob;

	private Ressources(int id, Job job) {
		this.requiredJob = job;
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the requiredJob
	 */
	public Job getRequiredJob() {
		return requiredJob;
	}
}
