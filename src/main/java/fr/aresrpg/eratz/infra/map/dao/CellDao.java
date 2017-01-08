package fr.aresrpg.eratz.infra.map.dao;

/**
 * 
 * @since
 */
public class CellDao {

	private int id;
	private boolean layerObject2Interactive;
	private int layerObject2Num;
	private boolean lineOfSight;
	private int layerGroundRot;
	private int groundLevel;
	private int movement;
	private int layerGroundNum;
	private int groundSlope;
	private boolean layerGroundFlip;
	private int layerObject1Num;
	private int layerObject1Rot;
	private boolean layerObject1Flip;
	private boolean layerObject2Flip;

	public CellDao(int id, boolean lineOfSight, int layerGroundRot, int groundLevel, int movement, int layerGroundNum, int groundSlope, boolean layerGroundFlip,
		int layerObject1Num, int layerObject1Rot, boolean layerObject1Flip, boolean layerObject2Flip, boolean layerObject2Interactive, int layerObject2Num) {
		this.id = id;
		this.lineOfSight = lineOfSight;
		this.layerGroundRot = layerGroundRot;
		this.groundLevel = groundLevel;
		this.movement = movement;
		this.layerGroundNum = layerGroundNum;
		this.groundSlope = groundSlope;
		this.layerGroundFlip = layerGroundFlip;
		this.layerObject1Num = layerObject1Num;
		this.layerObject1Rot = layerObject1Rot;
		this.layerObject1Flip = layerObject1Flip;
		this.layerObject2Flip = layerObject2Flip;
		this.layerObject2Interactive = layerObject2Interactive;
		this.layerObject2Num = layerObject2Num;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the layerObject2Interactive
	 */
	public boolean isLayerObject2Interactive() {
		return layerObject2Interactive;
	}

	/**
	 * @param layerObject2Interactive
	 *            the layerObject2Interactive to set
	 */
	public void setLayerObject2Interactive(boolean layerObject2Interactive) {
		this.layerObject2Interactive = layerObject2Interactive;
	}

	/**
	 * @return the layerObject2Num
	 */
	public int getLayerObject2Num() {
		return layerObject2Num;
	}

	/**
	 * @param layerObject2Num
	 *            the layerObject2Num to set
	 */
	public void setLayerObject2Num(int layerObject2Num) {
		this.layerObject2Num = layerObject2Num;
	}

	/**
	 * @return the lineOfSight
	 */
	public boolean isLineOfSight() {
		return lineOfSight;
	}

	/**
	 * @param lineOfSight
	 *            the lineOfSight to set
	 */
	public void setLineOfSight(boolean lineOfSight) {
		this.lineOfSight = lineOfSight;
	}

	/**
	 * @return the layerGroundRot
	 */
	public int getLayerGroundRot() {
		return layerGroundRot;
	}

	/**
	 * @param layerGroundRot
	 *            the layerGroundRot to set
	 */
	public void setLayerGroundRot(int layerGroundRot) {
		this.layerGroundRot = layerGroundRot;
	}

	/**
	 * @return the groundLevel
	 */
	public int getGroundLevel() {
		return groundLevel;
	}

	/**
	 * @param groundLevel
	 *            the groundLevel to set
	 */
	public void setGroundLevel(int groundLevel) {
		this.groundLevel = groundLevel;
	}

	/**
	 * @return the movement
	 */
	public int getMovement() {
		return movement;
	}

	/**
	 * @param movement
	 *            the movement to set
	 */
	public void setMovement(int movement) {
		this.movement = movement;
	}

	/**
	 * @return the layerGroundNum
	 */
	public int getLayerGroundNum() {
		return layerGroundNum;
	}

	/**
	 * @param layerGroundNum
	 *            the layerGroundNum to set
	 */
	public void setLayerGroundNum(int layerGroundNum) {
		this.layerGroundNum = layerGroundNum;
	}

	/**
	 * @return the groundSlope
	 */
	public int getGroundSlope() {
		return groundSlope;
	}

	/**
	 * @param groundSlope
	 *            the groundSlope to set
	 */
	public void setGroundSlope(int groundSlope) {
		this.groundSlope = groundSlope;
	}

	/**
	 * @return the layerGroundFlip
	 */
	public boolean isLayerGroundFlip() {
		return layerGroundFlip;
	}

	/**
	 * @param layerGroundFlip
	 *            the layerGroundFlip to set
	 */
	public void setLayerGroundFlip(boolean layerGroundFlip) {
		this.layerGroundFlip = layerGroundFlip;
	}

	/**
	 * @return the layerObject1Num
	 */
	public int getLayerObject1Num() {
		return layerObject1Num;
	}

	/**
	 * @param layerObject1Num
	 *            the layerObject1Num to set
	 */
	public void setLayerObject1Num(int layerObject1Num) {
		this.layerObject1Num = layerObject1Num;
	}

	/**
	 * @return the layerObject1Rot
	 */
	public int getLayerObject1Rot() {
		return layerObject1Rot;
	}

	/**
	 * @param layerObject1Rot
	 *            the layerObject1Rot to set
	 */
	public void setLayerObject1Rot(int layerObject1Rot) {
		this.layerObject1Rot = layerObject1Rot;
	}

	/**
	 * @return the layerObject1Flip
	 */
	public boolean isLayerObject1Flip() {
		return layerObject1Flip;
	}

	/**
	 * @param layerObject1Flip
	 *            the layerObject1Flip to set
	 */
	public void setLayerObject1Flip(boolean layerObject1Flip) {
		this.layerObject1Flip = layerObject1Flip;
	}

	/**
	 * @return the layerObject2Flip
	 */
	public boolean isLayerObject2Flip() {
		return layerObject2Flip;
	}

	/**
	 * @param layerObject2Flip
	 *            the layerObject2Flip to set
	 */
	public void setLayerObject2Flip(boolean layerObject2Flip) {
		this.layerObject2Flip = layerObject2Flip;
	}

	@Override
	public String toString() {
		return "CellDao [id=" + id + ", layerObject2Interactive=" + layerObject2Interactive + ", layerObject2Num=" + layerObject2Num + ", lineOfSight=" + lineOfSight + ", layerGroundRot="
				+ layerGroundRot + ", groundLevel=" + groundLevel + ", movement=" + movement + ", layerGroundNum=" + layerGroundNum + ", groundSlope=" + groundSlope + ", layerGroundFlip="
				+ layerGroundFlip + ", layerObject1Num=" + layerObject1Num + ", layerObject1Rot=" + layerObject1Rot + ", layerObject1Flip=" + layerObject1Flip + ", layerObject2Flip="
				+ layerObject2Flip + "]";
	}

}
