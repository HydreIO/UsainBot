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
public enum Interractable {

	PUITS(7519, null),
	PICHON(7544, null),
	POUBELLE(7352, null),
	OMBRE_ETRANGE(7543, null),
	MOULE(7002, null),
	FM_CAC(7020, null),
	TABLE_A_PATATE(7006, null),
	PECHE_CANARD_(7549, null),
	TABLE_MAGIQUE(7037, null),
	ATELIER_MAGIQUE(7038, null),
	MARMITE(7017, null),
	LISTE_ARTISANS(7035, null),
	MACHINE_A_COUDRE_MAGIQUE(7036, null),
	CONCASSEUR(7021, null),
	SOURCE_DE_JOUVENCE(0, null),
	TAS_DE_PATATES(7510, null),
	MEULE(7005, null),
	ETABLI_PYROTECHNIQUE(7028, null),
	MACHINE_BOUCLIER(7027, Job.FORGEUR_DE_BOUCLIER),
	MACHINE_PAYSAN(7007, Job.PAYSAN),
	MACHINE_PECHEUR(7024, Job.PECHEUR),
	MACHINE_BOUCHER(7025, Job.BOUCHER),
	MACHINE_POISSONIER(7022, Job.POISSONNIER),
	MACHINE_CHASSEUR(7023, Job.CHASSEUR),
	MACHINE_BUCHERON(7003, Job.BUCHERON),
	MACHINE_ALCHI(7019, Job.ALCHIMISTE),
	MACHINE_FORGERON(7012, Job.FORGEUR_DE_DAGUES, Job.FORGEUR_DE_HACHES, Job.FORGEUR_DE_MARTEAUX, Job.FORGEUR_DE_PELLES, Job.FORGEUR_EPEES),
	MACHINE_BRICOLEUR(7039, Job.BRICOLEUR),
	MACHINE_BOULANGER(7001, Job.BOULANGER),
	MACHINE_SCULPTEUR(7013, Job.SCULPTEUR_ARCS, Job.SCULPTEUR_DE_BAGUETTES, Job.SCULPTEUR_DE_BATONS),
	MACHINE_CORDONIER(7011),
	FLEUR_LIN(7513, Job.ALCHIMISTE),
	TREFLE(7533, Job.ALCHIMISTE),
	FEUILLE_MENTHE(7534, Job.ALCHIMISTE),
	ORCHIDEE(7535, Job.ALCHIMISTE),
	EDELWEISS(7536, Job.ALCHIMISTE),
	GRAINE_PANDOUILLE(7551, Job.ALCHIMISTE),
	FRENE(7500, Job.BUCHERON),
	CHATAIGNIER(7501, Job.BUCHERON),
	NOYER(7502, Job.BUCHERON),
	CHENE(7503, Job.BUCHERON),
	BOMBU(7541, Job.BUCHERON),
	OLIVIOLET(7542, Job.BUCHERON),
	ERABLE(7504, Job.BUCHERON),
	IF(7505, Job.BUCHERON),
	BAMBOU(7553, Job.BUCHERON),
	MERISIER(7506, Job.BUCHERON),
	EBENE(7506, Job.BUCHERON),
	KALIPTUS(7557, Job.BUCHERON),
	CHARME(7508, Job.BUCHERON),
	BAMBOU_SOMBRE(7554, Job.BUCHERON),
	ORME(7509, Job.BUCHERON),
	BAMBOU_SACREE(7552, Job.BUCHERON),
	FER(7520, Job.MINEUR),
	CUIVRE(7522, Job.MINEUR),
	BRONZE(7523, Job.MINEUR),
	KOBALTE(443, Job.MINEUR),
	MANGANESE(7524, Job.MINEUR),
	ETAIN(7521, Job.MINEUR),
	SILICATE(7556, Job.MINEUR),
	ARGENT(7526, Job.MINEUR),
	BAUXITE(7528, Job.MINEUR),
	OR(313, Job.MINEUR), // ID
	DOLOMITE(7555, Job.MINEUR),
	BLE(7511, Job.PAYSAN),
	ORGE(7515, Job.PAYSAN),
	AVOINE(7517, Job.PAYSAN),
	HOUBLON(7512, Job.PAYSAN),
	LIN(7513, Job.PAYSAN),
	RIZ(7550, Job.PAYSAN),
	SEIGLE(7516, Job.PAYSAN),
	MALT(7518, Job.PAYSAN),
	CHANVRE(7514, Job.PAYSAN),
	PETITS_POISSONS_MER(7530, Job.PECHEUR),
	PETITS_POISSONS_RIVIERE(7529, Job.PECHEUR),
	POISSONS_MER(7531, Job.PECHEUR),
	POISSONS_RIVIERE(7532, Job.PECHEUR),
	GROS_POISSONS_MER(7538, Job.PECHEUR),
	GROS_POISSONS_RIVIERE(7537, Job.PECHEUR),
	POISSONS_GEANTS_MER(7540, Job.PECHEUR),
	POISSONS_GEANTS_RIVIERE(7539, Job.PECHEUR);

	private int id;
	private Job[] requiredJob;
	public static final int ENCLOS[] = { 6766, 6767, 6763, 6772 };
	public static final int ZAAP[] = { 7000, 7026, 7029, 4287 };
	public static final int ATELIER[] = { 7008, 7009, 7010 };
	public static final int MACHINE_TAILLEUR[] = { 7014, 7015, 7016 };
	public static final int ZAAPI[] = { 7030, 7031 };
	public static final int PORTE[] = { 6700, 6701, 6702, 6703, 6704, 6705, 6706, 6707, 6708, 6709, 6710, 6711, 6712, 6713, 6714, 6715, 6716, 6717, 6718, 6719, 6720, 6721, 6722, 6723, 6724, 6725,
			6726, 6727, 6728, 6729, 6730, 6731, 6732, 6733, 6734, 6735, 6736, 6737, 6738, 6739, 6740, 6741, 6742, 6743, 6744, 6745, 6746, 6747, 6748, 6749, 6750, 6751, 6752, 6753, 6754, 6755, 6756,
			6757, 6758, 6759, 6760, 6761, 6762, 6763, 6764, 6765, 6766, 6767, 6768, 6769, 6770, 6771, 6772, 6773, 6774, 6775, 6776 };
	public static final int COFFRE[] = { 7350, 7351, 7353 };
	public static final int LEVIER[] = { 7041, 7042, 7043, 7044, 7045 };
	public static final int STATUE_CLASSE[] = { 1853, 1854, 1855, 1856, 1857, 1858, 1859, 1860, 1861, 1862, 1845, 2319 };
	public static final int MACHINE_FORCE[] = { 7546, 7547 };

	private Interractable(int id, Job... job) {
		this.requiredJob = job;
		this.id = id;
	}

	public static Interractable fromId(int id) {
		for (Interractable i : values())
			if (i.getId() == id) return i;
		return null;
	}

	public static boolean isMachineForce(int id) {
		return contains(id, MACHINE_FORCE);
	}

	public static boolean isStatueClasse(int id) {
		return contains(id, STATUE_CLASSE);
	}

	public static boolean isLevier(int id) {
		return contains(id, LEVIER);
	}

	public static boolean isPorte(int id) {
		return contains(id, PORTE);
	}

	public static boolean isZaapi(int id) {
		return contains(id, ZAAPI);
	}

	public static boolean isMachineTailleur(int id) {
		return contains(id, MACHINE_TAILLEUR);
	}

	public static boolean isAtelier(int id) {
		return contains(id, ATELIER);
	}

	public static boolean isZaap(int id) {
		return contains(id, ZAAP);
	}

	public static boolean isEnclo(int id) {
		return contains(id, ENCLOS);
	}

	private static boolean contains(int id, int[] array) {
		for (int i : array)
			if (i == id) return true;
		return false;
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
	public Job[] getRequiredJob() {
		return requiredJob;
	}
}
