package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.eratz.domain.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ability.craft.CraftAbility;
import fr.aresrpg.eratz.domain.ability.craft.CraftAbilityImpl;
import fr.aresrpg.eratz.domain.ability.fight.FightAbility;
import fr.aresrpg.eratz.domain.ability.fight.FightAbilityImpl;
import fr.aresrpg.eratz.domain.ability.harvest.HarvestAbility;
import fr.aresrpg.eratz.domain.ability.harvest.HarvestAbilityImpl;
import fr.aresrpg.eratz.domain.ability.sell.SellAbility;
import fr.aresrpg.eratz.domain.ability.sell.SellAbilityImpl;

/**
 * 
 * @since
 */
public class AbilityInfo extends Info {

	private final BaseAbility baseAbility = null;
	private final HarvestAbility harvestAbility = new HarvestAbilityImpl(getPerso());
	private final CraftAbility craftAbility = new CraftAbilityImpl(getPerso());
	private final FightAbility fightAbility = new FightAbilityImpl(getPerso());
	private final SellAbility sellAbility = new SellAbilityImpl(getPerso());

	/**
	 * @param perso
	 */
	public AbilityInfo(Perso perso) {
		super(perso);
	}

	/**
	 * @return the baseAbility
	 */
	public BaseAbility getBaseAbility() {
		return baseAbility;
	}

	/**
	 * @return the harvestAbility
	 */
	public HarvestAbility getHarvestAbility() {
		return harvestAbility;
	}

	/**
	 * @return the craftAbility
	 */
	public CraftAbility getCraftAbility() {
		return craftAbility;
	}

	/**
	 * @return the fightAbility
	 */
	public FightAbility getFightAbility() {
		return fightAbility;
	}

	/**
	 * @return the sellAbility
	 */
	public SellAbility getSellAbility() {
		return sellAbility;
	}

}
