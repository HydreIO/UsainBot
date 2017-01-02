package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.commons.domain.log.AnsiColors;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.util.Maps;

public class AnsiMapDrawer {

	public static String drawMap(DofusMap map) {
		Cell[] cells = map.getCells();
		String[][] buffer = new String[map.getHeight() * 4 - 1][map.getWidth() * 4];
		for (int i = 0; i < cells.length; i++) {
			Cell c = cells[i];
			AnsiColors.AnsiColor color;
			switch (c.getMovement()) {
				case 0:
					color = AnsiColors.AnsiColor.BLACK;
					break;
				case 2:
					color = AnsiColors.AnsiColor.BLUE;
					break;
				case 4:
					color = AnsiColors.AnsiColor.GREEN;
					break;
				case 6:
					color = AnsiColors.AnsiColor.YELLOW;
					break;
				case 1:
					color = AnsiColors.AnsiColor.RED;
					break;
				case 3:
					color = AnsiColors.AnsiColor.CYAN;
					break;
				case 5:
					color = AnsiColors.AnsiColor.PURPLE;
					break;
				default:
					throw new IllegalStateException("Unknown movement " + c.getMovement());
			}
			int xp = Maps.getXRotated(i, map.getWidth(), map.getHeight()) * 2 + 1;
			int yp = Maps.getYRotated(i, map.getWidth(), map.getHeight()) * 2 + 1;
			buffer[yp][xp] = buffer[yp + 1][xp] = buffer[yp][xp + 1] = buffer[yp - 1][xp] = buffer[yp][xp - 1] = AnsiColors.getCode(color, color, false) + " ";

		}
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < buffer.length; y++) {
			for (int x = 0; x < buffer[y].length; x++)
				sb.append(buffer[y][x] == null ? AnsiColors.ANSI_RESET + " " : buffer[y][x]);

			sb.append('\n').append(AnsiColors.ANSI_RESET);
		}
		sb.append(AnsiColors.ANSI_RESET);
		return sb.toString();
	}
}