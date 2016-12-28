package fr.aresrpg.eratz.domain.data.player.object;

import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.data.dofus.map.BotMap;

import java.util.List;

public class Ressource extends Cell {

	private Interractable type;

	public Ressource(Interractable type, int id, int mapWidth, boolean lineOfSight, int layerGroundRot, int groundLevel, int movement, int layerGroundNum, int groundSlope, int x, int y,
		boolean layerGroundFlip,
		int layerObject1Num, int layerObject1Rot, boolean layerObject1Flip, boolean layerObject2Flip, boolean layerObject2Interactive, int layerObject2Num) {
		super(id, mapWidth, lineOfSight, layerGroundRot, groundLevel, movement, layerGroundNum, groundSlope, x, y, layerGroundFlip, layerObject1Num, layerObject1Rot, layerObject1Flip,
				layerObject2Flip,
				layerObject2Interactive, layerObject2Num);
		this.type = type;
	}

	public Ressource(Interractable type, Cell c) {
		this(type, c.getId(), c.getMapWidth(), c.isLineOfSight(), c.getLayerGroundRot(), c.getGroundLevel(), c.getMovement(), c.getLayerGroundNum(), c.getGroundSlope(), c.getX(), c.getY(),
				c.isLayerGroundFlip(), c.getLayerObject1Num(), c.getLayerObject1Rot(),
				c.isLayerObject1Flip(), c.isLayerObject2Flip(), c.isLayerObject2Interactive(), c.getLayerObject2Num());
	}

	/** * @return the type */
	public Interractable getType() {
		return type;
	}

	/**
	 * Gat all cells around the ressource
	 * 
	 * @param map
	 *            the map
	 * @param diagonale
	 *            use diagonale cells
	 * @param avoid
	 *            a list of cell to avoid
	 * @return
	 */
	public int getNeighborCell(BotMap map, boolean diagonale, List<Integer> avoid) {
		Node[] neighbors = diagonale ? Pathfinding.getNeighbors(new Node(getX(), getY())) : Pathfinding.getNeighborsWithoutDiagonals(new Node(getX(), getY()));
		for (Node n : neighbors) {
			int id = Maps.getId(n.getX(), n.getY(), map.getDofusMap().getWidth());
			if (avoid.contains(id)) continue;
			Cell cell = null;
			try {
				cell = map.getDofusMap().getCell(id);
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			}
			if (cell.isWalkeable(i -> !map.hasMobOn(i))) return id;
		}
		return -1;
	}

	/** * @return the spawned */
	public boolean isSpawned() {
		return getFrame() == 0 || getFrame() == 5;
	}

	@Override
	public String toString() {
		return "Ressource [type=" + type + "]" + super.toString();
	}

}