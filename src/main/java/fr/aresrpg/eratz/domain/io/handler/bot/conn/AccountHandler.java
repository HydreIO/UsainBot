package fr.aresrpg.eratz.domain.io.handler.bot.conn;

import fr.aresrpg.dofus.protocol.account.server.AccountLoginErrPacket.Error;
import fr.aresrpg.dofus.structures.Community;
import fr.aresrpg.dofus.structures.character.AvailableCharacter;
import fr.aresrpg.dofus.structures.server.DofusServer;

/**
 * 
 * @since
 */
public interface AccountHandler {

	void onHelloConnection(String key);

	void onHelloServer();

	void onKey(int key, String data);

	void onRegion();

	void onCharacterList(int subscription, AvailableCharacter... characters);

	void onCommunity(Community commu);

	void onServers(DofusServer... servers);

	void onLoginError(Error err, int minutes, int version);

	boolean onLogged(boolean isAdmin); // return bool to set

	void onPseudo(String pseudo);

	void onSecretQuestion(String question);

	void onQueueRealmPosition(int position, int totalSub, int totalNoSub, boolean sub, int positionInQueue);

}
