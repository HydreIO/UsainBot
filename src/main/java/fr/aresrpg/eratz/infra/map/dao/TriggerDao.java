package fr.aresrpg.eratz.infra.map.dao;

import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;

import java.util.Map;

/**
 * 
 * @since
 */
public class TriggerDao {

	private String type;
	private int cell;
	private Map<String, Object> datas;

	/**
	 * @param type
	 * @param cell
	 * @param datas
	 */
	public TriggerDao(TriggerType type, int cell, Map<String, Object> datas) {
		this.type = type.name();
		this.cell = cell;
		this.datas = datas;
	}

	/**
	 * @return the type
	 */
	public TriggerType getType() {
		return TriggerType.valueOf(type);
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(TriggerType type) {
		this.type = type.name();
	}

	/**
	 * @return the cell
	 */
	public int getCell() {
		return cell;
	}

	/**
	 * @param cell
	 *            the cell to set
	 */
	public void setCell(int cell) {
		this.cell = cell;
	}

	/**
	 * @return the datas
	 */
	public Map<String, Object> getDatas() {
		return datas;
	}

	/**
	 * @param datas
	 *            the datas to set
	 */
	public void setDatas(Map<String, Object> datas) {
		this.datas = datas;
	}

}
