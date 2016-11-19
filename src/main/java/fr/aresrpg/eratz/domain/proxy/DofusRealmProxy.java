package fr.aresrpg.eratz.domain.proxy;

import fr.aresrpg.eratz.domain.util.concurrent.Executors;
import fr.aresrpg.eratz.domain.util.encryption.CryptHelper;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class DofusRealmProxy implements DofusProxy {
	private Socket remote;
	private InputStream localInputStream;
	private OutputStream localOutputStream;
	private InputStream remoteInputStream;
	private OutputStream remoteOutputStream;

	public DofusRealmProxy(InputStream localInputStream, OutputStream localOutputStream) throws IOException {
		this.remote = new Socket(InetAddress.getByName("80.239.173.166"), 443);

		this.localInputStream = localInputStream;
		this.localOutputStream = localOutputStream;

		this.remoteInputStream = remote.getInputStream();
		this.remoteOutputStream = remote.getOutputStream();

		Executors.FIXED.execute(() -> {
			while (true) {
				try {
					String data = read(localInputStream, remoteOutputStream);
					if (data != null) {
						System.out.println("[REALM] -> " + data);
					}
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
					e.printStackTrace();
					break;
				}
			}
		});
		Executors.FIXED.execute(() -> {
			while (true) {
				try {
					String data = read(remoteInputStream, localOutputStream);
					if (data != null) {
						System.out.println("[REALM] <- " + data);
					}
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
					e.printStackTrace();
					break;
				}
			}
		});
	}

	@Override
	public InputStream getLocalInputStream() {
		return localInputStream;
	}

	@Override
	public OutputStream getLocalOutputStream() {
		return localOutputStream;
	}

	@Override
	public InputStream getRemoteInputStream() {
		return remoteInputStream;
	}

	@Override
	public OutputStream getRemoteOutputStream() {
		return remoteOutputStream;
	}

	private String read(InputStream inputStream, OutputStream outputStream) throws IOException {
		if (inputStream.available() > 0) {
			byte[] buffer = new byte[inputStream.available()];
			int read = 0;

			while (read != buffer.length) {
				read += inputStream.read(buffer, read, buffer.length - read);
			}

			String data = new String(buffer);

			if (data.contains("AXK")) {
				String ip = CryptHelper.decryptIp(data.substring(3, 11));
				DofusGameProxy proxy = new DofusGameProxy(InetAddress.getByName(ip), 443);
				data = "AYK127.0.0.1:" + proxy.getLocal().getLocalPort() + ";" + data.substring(14);
			}

			outputStream.write(data.getBytes("UTF-8"));
			return data;
		}
		return null;
	}
}
