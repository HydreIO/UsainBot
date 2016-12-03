package fr.aresrpg.eratz.domain.dofus.item;

/**
 * 
 * @since
 */
public enum Items {

	BLE(289, 34, "Blé", 1, "", 2, -1, 0),
	EAU(311, 15, "Eau", 1, "", 1, -1, 0),
	LEVURE_BOULANGER(286, 48, "Levure de Boulanger", 1, "", 1, -1, 0),
	FARINE_DE_BLE(285, 52, "Farine de Blé", 1, "", 2, -1, 0),
	PAIN_AMAKNA(468, 33, "Pain d'Amakna", 1, "6e#a#0#0#0d0+10", 1, -1, 0, new Recipe().addItem(FARINE_DE_BLE, 1).addItem(EAU, 1)),
	PAIN_BLE_COMPLET(528, 33, "Pain au Blé Complet", 20, "6e#32#0#0#0d0+50", 2, -1, 0, new Recipe().addItem(LEVURE_BOULANGER, 1).addItem(BLE, 1).addItem(FARINE_DE_BLE, 1).addItem(EAU, 1));

	private int id;
	private int typeId;
	private String name;
	private int lvl;
	private String statsTemplate;
	private int pod;
	private int panoplieId;
	private int price;
	private String condition;
	private String armesInfos;
	private int sold;
	private int avgPrice;
	private Recipe recipe;

	private Items(int id, int typeid, String name, int lvl, String statstemplate, int pod, int panoplie, int npcPrice, Recipe recipe) {
		this.id = id;
		this.recipe = recipe;
		this.typeId = typeid;
		this.name = name;
		this.lvl = lvl;
		this.statsTemplate = statstemplate;
		this.pod = pod;
		this.panoplieId = panoplie;
		this.price = npcPrice;
	}

	private Items(int id, int typeid, String name, int lvl, String statstemplate, int pod, int panoplie, int npcPrice) {
		this(id, typeid, name, lvl, statstemplate, pod, panoplie, npcPrice, null);
	}

	/**
	 * @return the recipe
	 */
	public Recipe getRecipe() {
		return recipe;
	}

	/**
	 * @return the armesInfos
	 */
	public String getArmesInfos() {
		return armesInfos;
	}

	/**
	 * @return the avgPrice
	 */
	public int getAvgPrice() {
		return avgPrice;
	}

	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the lvl
	 */
	public int getLvl() {
		return lvl;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the panoplieId
	 */
	public int getPanoplieId() {
		return panoplieId;
	}

	/**
	 * @return the pod
	 */
	public int getPod() {
		return pod;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @return the sold
	 */
	public int getSold() {
		return sold;
	}

	/**
	 * @return the statsTemplate
	 */
	public String getStatsTemplate() {
		return statsTemplate;
	}

	/**
	 * @return the typeId
	 */
	public int getTypeId() {
		return typeId;
	}

}
