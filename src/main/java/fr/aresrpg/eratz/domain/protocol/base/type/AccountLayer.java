package fr.aresrpg.eratz.domain.protocol.base.type;

import fr.aresrpg.eratz.domain.player.DofusAccount;
import fr.aresrpg.eratz.domain.protocol.PacketParser;

/**
 * 
 * @since
 */
public class AccountLayer implements PacketParser {

	@Override
	public void parse(DofusAccount proxy, String packet) {
		switch (packet.charAt(1)) {
			case 'f':
				parseAfPacket(packet.substring(2));
				return;
			case 'd':
				parseAdPacket(packet.substring(2));
				return;
			case 'c':
				parseAcPacket(packet.substring(2));
				return;
			case 'H':
				parseAhPacket(packet.substring(2));
				return;
			case 'l':
				switch (packet.charAt(2)) {
					case 'E':
						parseAlePacket(packet.substring(3));
						return;
					case 'K':
						parseAlkPacket(packet.substring(3));
						return;
					default:
						return;
				}
			case 'L':
				parseALKPacket(packet.substring(3));
				return;
			case 'Q':
				parseAqPacket(packet.substring(2));
				return;
			case 'x':
				parseAxkPacket(packet.substring(3));
				return;
			case 'Y':
				parseAykPacket(packet.substring(3));
				return;
			case 'X':
				parseAxkPacket(packet.substring(3));
				return;
			case 'T':
				if (packet.charAt(2) == 'E') parseAtePacket(packet.substring(3));
				else parseAtkPacket(packet.substring(3));
				return;
			case 'V':
				return;
			case 'P':
				if (packet.charAt(2) == 'E') parseApePacket(packet.substring(3));
				else parseApkPacket(packet.substring(3));
				return;
			case 'A':
				if (packet.charAt(2) == 'E') parseAaePacket(packet.substring(3));
				else parseAakPacket(packet.substring(3));
				return;
			case 'S':
				if (packet.charAt(2) == 'E') parseAsePacket(packet.substring(3));
				else parseAskPacket(packet.substring(3));
				return;
			case 'R':
				parseArPacket(packet.substring(2));
				return;
			case 'N':
				parseAnPacket(packet.substring(2));
				return;
			default:
				break;
		}
	}

	/**
	 * @param substring
	 */
	private void parseAcPacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseAdPacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseAfPacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseAnPacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseArPacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseAskPacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseAsePacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseAakPacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseAaePacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseApkPacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseApePacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseAtkPacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseAtePacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseAykPacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseAxkPacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseAqPacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseALKPacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseAlkPacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseAlePacket(String substring) {
		// TODO

	}

	/**
	 * @param substring
	 */
	private void parseAhPacket(String substring) {
		// TODO

	}

}
