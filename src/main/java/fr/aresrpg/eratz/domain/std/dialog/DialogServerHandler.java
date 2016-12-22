package fr.aresrpg.eratz.domain.std.dialog;

/**
 * 
 * @since
 */
public interface DialogServerHandler {

	void onDialogCreate(int npcId);

	void onQuestion(int question, int[] params, int[] responses);

	void onDialogPause(); // ??

}
