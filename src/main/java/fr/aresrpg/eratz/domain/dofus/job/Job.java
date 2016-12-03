/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 *  
 * Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.dofus.job;

/**
 * 
 * @since
 */
public enum Job {

	ALCHIMISTE(JobType.RECOLTE),
	BUCHERON(JobType.RECOLTE),
	CHASSEUR(JobType.RECOLTE),
	MINEUR(JobType.RECOLTE),
	PAYSAN(JobType.RECOLTE),
	PECHEUR(JobType.RECOLTE),
	BOUCHER(JobType.CRAFT),
	POISSONNIER(JobType.CRAFT),
	BOULANGER(JobType.CRAFT),
	BRICOLEUR(JobType.CRAFT),
	BIJOUTIER(JobType.CRAFT),
	TAILLEUR(JobType.CRAFT),
	CORDONNIER(JobType.CRAFT),
	FORGEUR_DE_BOUCLIER(JobType.CRAFT),
	FORGEUR_DE_DAGUES(JobType.CRAFT),
	FORGEUR_EPEES(JobType.CRAFT),
	FORGEUR_DE_HACHES(JobType.CRAFT),
	FORGEUR_DE_MARTEAUX(JobType.CRAFT),
	FORGEUR_DE_PELLES(JobType.CRAFT),
	SCULPTEUR_ARCS(JobType.CRAFT),
	SCULPTEUR_DE_BAGUETTES(JobType.CRAFT),
	SCULPTEUR_DE_BATONS(JobType.CRAFT);

	private JobType type;

	private Job(JobType type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public JobType getType() {
		return type;
	}

	public static enum JobType {
		CRAFT,
		RECOLTE
	}

}
