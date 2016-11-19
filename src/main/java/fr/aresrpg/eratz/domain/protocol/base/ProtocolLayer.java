package fr.aresrpg.eratz.domain.protocol.base;

import fr.aresrpg.eratz.domain.protocol.PacketParser;
import fr.aresrpg.eratz.domain.protocol.base.type.*;

/**
 * 
 * @since
 */
public enum ProtocolLayer { // client -> server

	ACCOUNT('A', new AccountLayer()),
	BASIC('B', new BasicLayer()),
	CHANNEL('c', new ChannelLayer()),
	DIALOG('D', new DialogLayer()),
	EXCHANGE('E', new ExchangeLayer()),
	ENVIRONNEMENT('e', new EnvironnementLayer()),
	FRIEND('F', new FriendLayer()),
	FIGHT('f', new FightLayer()),
	GAME('G', new GameLayer()),
	GUILD('g', new GuildLayer()),
	HOUSE('h', new HouseLayer()),
	ENEMY('i', new EnemyLayer()),
	HOUSE_CODE('k', new HouseCode()),
	OBJECT('O', new ObjectLayer()),
	GROUP('P', new GroupLayer()),
	MOUNT('R', new MountLayer()),
	SPELL('S', new SpellLayer()),
	HELLO('H', new HelloLayer()),
	ALIGNEMENT('a', new AlignementLayer()),
	WAYPOINT('W', new WaypointLayer());

	private char key;
	private PacketParser handler;

	private ProtocolLayer(char key, PacketParser handler) {
		this.key = key;
		this.handler = handler;
	}

	/**
	 * @return the key
	 */
	public char getKey() {
		return key;
	}

}
