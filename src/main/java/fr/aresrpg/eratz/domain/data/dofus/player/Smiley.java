package fr.aresrpg.eratz.domain.data.dofus.player;

/**
 * 
 * @since
 */
public enum Smiley {

	CONTENT,
	PAS_CONTENT,
	BLESSE,
	TIRE_LANQUE,
	YEUX_ROUGE,
	PLEURE_RIRE,
	APEURE,
	BOUCHE_W,
	CRANE,
	ETONE,
	AMOUREUX,
	PLEURE,
	SUEUR,
	X_X,
	MOQUEUR;

	/**
	 * @return the id
	 */
	public int getId() {
		return ordinal() + 1;
	}

	public static Smiley valueOf(int code) {
		for (Smiley e : values())
			if (e.getId() == code) return e;
		return null;
	}

}
