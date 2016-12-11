package fr.aresrpg.eratz.domain.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.StringJoiner;

public class Hastebin {

	public static final ByteArrayOutputStream stream = new ByteArrayOutputStream();

	public static String post() {
		return post(stream.toByteArray());
	}

	public static String post(List<String> l) {
		return post(l.stream().collect(() -> new StringJoiner("\n"), StringJoiner::add, StringJoiner::merge).toString());
	}

	public static String post(byte[] s) {
		return post(new String(s));
	}

	public static String post(String s) {
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL("https://paste.aresrpg.fr/documents").openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/plain");
			conn.setDoOutput(true);
			conn.getOutputStream().write(s.getBytes());
			conn.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			String key = sb.toString();
			key = key.substring(key.lastIndexOf("\"key\":\"") + 7);
			key = key.substring(0, key.lastIndexOf("\"}"));
			return "https://paste.aresrpg.fr/" + key + ".hs";
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}