package fr.aresrpg.eratz.domain.io.handler.std.aproach;

import fr.aresrpg.dofus.protocol.account.server.AccountLoginErrPacket.Error;
import fr.aresrpg.dofus.structures.Community;
import fr.aresrpg.dofus.structures.character.AvailableCharacter;
import fr.aresrpg.dofus.structures.character.Character;
import fr.aresrpg.dofus.structures.server.DofusServer;

import java.util.Map;

/**
 * 
 * @since
 */
public interface AccountServerHandler {

	void onHelloConnection(String key);

	void onHelloServer();

	void onKey(int key, String data);

	void onRegion();

	void onCharacterList(long subscription, AvailableCharacter... characters);

	void onCommunity(Community commu);

	void onServers(DofusServer... servers);

	void onLoginError(Error err, int minutes, String version);

	void onLogged(boolean isAdmin);

	void onPseudo(String pseudo);

	void onSecretQuestion(String question);

	void onQueueRealmPosition(int position, int totalSub, int totalNoSub, boolean sub, int positionInQueue);

	void onCharacterSelect(Character p);

	void onReceiveServerHost(String ip, int port, String ticket);

	void onReceiveServerPersoCount(long subtime, Map<Integer, Integer> srvIdAndCount);

	void onStatsUpdate(); // le base handler applique les stats et notifi ici qu'elles ont été update

	void onNewLvl(int lvl); // je suppose qu'on reçoit un packet stat juste apres alors inutile de set le lvl ici

	void onServerQueue(int currentPos);

}
