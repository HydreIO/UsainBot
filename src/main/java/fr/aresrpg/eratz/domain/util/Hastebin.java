package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.dofus.util.Lang;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Hastebin {

	public static ByteArrayOutputStream stream = new ByteArrayOutputStream();

	public static void main(String[] args) throws IOException {
		Map<String, Object> datas = Lang.getDatas("fr", "maps");
		AtomicInteger index = new AtomicInteger();
		datas.forEach((a, b) -> {
			try {
				stream.write((a + "|" + b + "\n").getBytes());
				if (index.incrementAndGet() > 1000) {
					System.out.println(post());
					stream = new ByteArrayOutputStream();
					index.set(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		System.out.println(post());

	}

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