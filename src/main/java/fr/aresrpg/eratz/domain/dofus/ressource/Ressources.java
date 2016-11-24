/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 *  
 * Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.dofus.ressource;

import fr.aresrpg.eratz.domain.dofus.player.Job;

/**
 * 
 * @since
 */
public enum Ressources {

	FLEUR_LIN(Job.ALCHIMISTE),
	FLEUR_CHANVRE(Job.ALCHIMISTE),
	TREFLE(Job.ALCHIMISTE),
	FEUILLE_MENTHE(Job.ALCHIMISTE),
	ORCHIDEE(Job.ALCHIMISTE),
	EDELWEISS(Job.ALCHIMISTE),
	GRAINE_PANDOUILLE(Job.ALCHIMISTE),
	PERCE_NEIGE(Job.ALCHIMISTE),
	ETOILE_NEIGES(Job.ALCHIMISTE),
	FRENE(Job.BUCHERON),
	CHATAIGNIER(Job.BUCHERON),
	NOYER(Job.BUCHERON),
	CHENE(Job.BUCHERON),
	BOMBU(Job.BUCHERON),
	OLIVIOLET(Job.BUCHERON),
	ERABLE(Job.BUCHERON),
	IF(Job.BUCHERON),
	BAMBOU(Job.BUCHERON),
	MERISIER(Job.BUCHERON),
	EBENE(Job.BUCHERON),
	KALIPTUS(Job.BUCHERON),
	CHARME(Job.BUCHERON),
	BAMBOU_SOMBRE(Job.BUCHERON),
	ORME(Job.BUCHERON),
	BAMBOU_SACREE(Job.BUCHERON),
	TREMBLE(Job.BUCHERON),
	FER(Job.MINEUR),
	CUIVRE(Job.MINEUR),
	BRONZE(Job.MINEUR),
	KOBALTE(Job.MINEUR),
	MANGANESE(Job.MINEUR),
	ETAIN(Job.MINEUR),
	SILICATE(Job.MINEUR),
	ARGENT(Job.MINEUR),
	BAUXITE(Job.MINEUR),
	OR(Job.MINEUR),
	DOLOMITE(Job.MINEUR),
	OBSIDIENNE(Job.MINEUR),
	BLE(Job.PAYSAN),
	ORGE(Job.PAYSAN),
	AVOINE(Job.PAYSAN),
	HOUBLON(Job.PAYSAN),
	LIN(Job.PAYSAN),
	RIZ(Job.PAYSAN),
	SEIGLE(Job.PAYSAN),
	MALT(Job.PAYSAN),
	CHANVRE(Job.PAYSAN),
	PETITS_POISSONS(Job.PECHEUR),
	POISSONS(Job.PECHEUR),
	GROS_POISSONS(Job.PECHEUR),
	POISSONS_GEANTS(Job.PECHEUR);

	private Job requiredJob;

	private Ressources(Job job) {
		this.requiredJob = job;
	}

	/**
	 * @return the requiredJob
	 */
	public Job getRequiredJob() {
		return requiredJob;
	}
}