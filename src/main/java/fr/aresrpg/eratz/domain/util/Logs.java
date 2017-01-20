package fr.aresrpg.eratz.domain.util;

import java.io.*;

public class Logs {

	public static FileOutputStream stream;

	static {
		try {
			File f = new File("logs.yml");
			if (!f.exists()) f.createNewFile();
			stream = new FileOutputStream(f, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}