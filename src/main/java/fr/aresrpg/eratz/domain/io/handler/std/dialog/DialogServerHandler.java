package fr.aresrpg.eratz.domain.io.handler.std.dialog;

/**
 * 
 * @since
 */
public interface DialogServerHandler {

	void onDialogCreate(int npcId);

	void onQuestion(int question, int[] params, int[] responses);

	void onDialogPause(); // ??

}
