package fr.aresrpg.eratz.domain.io.handler.impl;

import fr.aresrpg.dofus.protocol.account.server.AccountLoginErrPacket.Error;
import fr.aresrpg.dofus.structures.Community;
import fr.aresrpg.dofus.structures.character.AvailableCharacter;
import fr.aresrpg.dofus.structures.character.Character;
import fr.aresrpg.dofus.structures.server.DofusServer;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.io.handler.std.aproach.AccountServerHandler;

import java.util.Map;

/**
 * 
 * @since
 */
public class BaseAccountHandler implements AccountServerHandler {

	private Perso perso;

	/**
	 * @param perso
	 */
	public BaseAccountHandler(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	@Override
	public void onHelloConnection(String key) {
		getPerso().setTicket(key);
	}

	@Override
	public void onHelloServer() {
		// TODO

	}

	@Override
	public void onKey(int key, String data) {
		// TODO

	}

	@Override
	public void onRegion() {
		// TODO

	}

	@Override
	public void onCharacterList(long subscription, AvailableCharacter... characters) {
		// TODO

	}

	@Override
	public void onCommunity(Community commu) {
		// TODO

	}

	@Override
	public void onServers(DofusServer... servers) {
		// TODO

	}

	@Override
	public void onLoginError(Error err, int minutes, String version) {
		// TODO

	}

	@Override
	public void onLogged(boolean isAdmin) {
		// TODO

	}

	@Override
	public void onPseudo(String pseudo) {
		// TODO

	}

	@Override
	public void onSecretQuestion(String question) {
		// TODO

	}

	@Override
	public void onQueueRealmPosition(int position, int totalSub, int totalNoSub, boolean sub, int positionInQueue) {
		// TODO

	}

	@Override
	public void onCharacterSelect(Character p) {
		// TODO

	}

	@Override
	public void onReceiveServerHost(String ip, int port, String ticket) {
		// TODO

	}

	@Override
	public void onReceiveServerPersoCount(long subtime, Map<Integer, Integer> srvIdAndCount) {
		// TODO

	}

}
