package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.eratz.domain.util.Hastebin;
import fr.aresrpg.tofumanchou.domain.command.Command;

/**
 * 
 * @since
 */
public class HastebinCommand implements Command {

	@Override
	public String getCmd() {
		return "hastebin";
	}

	@Override
	public void trigger(String[] args) {
		LOGGER.info(Hastebin.post());
		if (args.length > 0 && args[0].equalsIgnoreCase("exit")) System.exit(0);
	}

}
