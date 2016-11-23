package fr.aresrpg.eratz.domain.dofus;

import fr.aresrpg.eratz.domain.dofus.ressource.Ressources;

/**
 * 
 * @since
 */
public enum Poissons {

	GOUJON(LieuPeche.RIVIERE, Ressources.PETITS_POISSONS),
	TRUITE(LieuPeche.RIVIERE, Ressources.PETITS_POISSONS, Ressources.POISSONS),
	POISSON_CHATON(LieuPeche.RIVIERE, Ressources.PETITS_POISSONS, Ressources.POISSONS, Ressources.GROS_POISSONS),
	BROCHET(LieuPeche.RIVIERE, Ressources.POISSONS, Ressources.GROS_POISSONS, Ressources.POISSONS_GEANTS),
	CARPE_DIEM(LieuPeche.RIVIERE, Ressources.POISSONS, Ressources.GROS_POISSONS, Ressources.POISSONS_GEANTS),
	BAR_RIKAIN(LieuPeche.RIVIERE, Ressources.GROS_POISSONS, Ressources.POISSONS_GEANTS),
	PERCHE(LieuPeche.RIVIERE, Ressources.POISSONS_GEANTS),
	GREU_VETTE(LieuPeche.MER, Ressources.PETITS_POISSONS),
	CRABE_SOURIMI(LieuPeche.MER, Ressources.PETITS_POISSONS, Ressources.POISSONS),
	POISSON_PANE(LieuPeche.MER, Ressources.PETITS_POISSONS, Ressources.POISSONS, Ressources.GROS_POISSONS),
	SARDINE_BRILLANTE(LieuPeche.MER, Ressources.POISSONS, Ressources.GROS_POISSONS, Ressources.POISSONS_GEANTS),
	KRALAMOURE(LieuPeche.MER, Ressources.POISSONS, Ressources.GROS_POISSONS, Ressources.POISSONS_GEANTS),
	RAIE_BLEUE(LieuPeche.MER, Ressources.GROS_POISSONS, Ressources.POISSONS_GEANTS),
	REQUIN_MARTEAU(LieuPeche.MER, Ressources.POISSONS_GEANTS);

	private LieuPeche lieu;
	private Ressources[] ressource;

	private Poissons(LieuPeche lieu, Ressources... ressource) {
		this.ressource = ressource;
		this.lieu = lieu;
	}

	/**
	 * @return the lieu
	 */
	public LieuPeche getLieu() {
		return lieu;
	}

	/**
	 * @return the ressource
	 */
	public Ressources[] getRessource() {
		return ressource;
	}

	public static enum LieuPeche {
		RIVIERE,
		MER
	}

}
