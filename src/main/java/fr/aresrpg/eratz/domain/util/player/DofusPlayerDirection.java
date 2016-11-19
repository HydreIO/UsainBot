package fr.aresrpg.eratz.domain.util.player;

public enum DofusPlayerDirection {
	UP('c'),
	DOWN('g'),
	LEFT('e'),
	RIGHT('a'),
	UP_LEFT('f'),
	UP_RIGHT('h'),
	DOWN_LEFT('d'),
	DOWN_RIGHT('b');

	private char id;

	DofusPlayerDirection(char id) {
		this.id = id;
	}

	public char getId() {
		return id;
	}

	public static DofusPlayerDirection fromChar(char c) {
		for (DofusPlayerDirection d : values())
			if (d.getId() == c) return d;
		throw new IllegalArgumentException("Direction inconnue");
	}
}
