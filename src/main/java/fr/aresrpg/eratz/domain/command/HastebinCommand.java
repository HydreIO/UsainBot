package fr.aresrpg.eratz.domain.command;

import fr.aresrpg.eratz.domain.util.Hastebin;
import fr.aresrpg.tofumanchou.domain.command.Command;

import java.io.*;

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
		Hastebin.post();
		if (args.length > 0 && args[0].equalsIgnoreCase("exit")) System.exit(0);
	}

}
