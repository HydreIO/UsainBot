package fr.aresrpg.eratz.domain.proxy;

import fr.aresrpg.eratz.domain.util.concurrent.Executors;
import fr.aresrpg.eratz.domain.util.encryption.CryptHelper;
import fr.aresrpg.eratz.domain.util.player.DofusPlayerDirection;

import java.io.*;
import java.net.*;
import java.util.Random;

public class DofusGameProxy implements DofusProxy {
	private boolean isBot;
	private ServerSocket local;
	private Socket remote;
	private InputStream localInputStream;
	private OutputStream localOutputStream;
	private InputStream remoteInputStream;
	private OutputStream remoteOutputStream;
	private int currentCell;

	public DofusGameProxy(InetAddress address, int port) throws IOException {
		this.local = new ServerSocket(0);
		this.remote = new Socket(address, port);

		Executors.FIXED.execute(() -> {
			try {
				Socket client = local.accept();

				localInputStream = client.getInputStream();
				localOutputStream = client.getOutputStream();

				remoteInputStream = remote.getInputStream();
				remoteOutputStream = remote.getOutputStream();

				Executors.FIXED.execute(() -> {
					while (true) {
						try {
							String data = read(localInputStream, remoteOutputStream);
							if (data != null) {
								// System.out.println("[GAME] -> " + data);

								if (data.startsWith("BM*|")) {
									String message = data.substring(4, data.length() - 1);
									System.out.println("[GAME] [CHAT] " + message);

									if (message.equals("lol")) {
										isBot = !isBot;

										System.out.println("Bot mode " + (isBot ? "enabled" : "disabled"));
									}

									if (message.equals("mdr")) {
										System.out.println("GA001" + DofusPlayerDirection.UP.getId() + CryptHelper.cryptCellId(250) + "\n\0");
										remoteOutputStream.write(("GA001" + DofusPlayerDirection.UP.getId() + CryptHelper.cryptCellId(250) + "\n\0").getBytes());
									}
								} else if (data.startsWith("GA001")) {
									String content = data.substring(5);

									for (int i = 0; i < content.length(); i += 3) {
										String path = content.substring(i, i + 3);
										char direction = path.charAt(0);
										System.out
												.println("[MOVE] CellId: " + CryptHelper.decryptCellId(path.substring(1)) + " Direction: " + direction/* + DofusPlayerDirection.fromChar(direction) */);
									}
								}

								// si on est en mode bot on installe php à chaque packet
								if (isBot) {
									// GA => Game Action packet
									// 001 => Action ID pour le déplacement

									// format du packet : GA TYPE_DACTION (DIRECTION CELLID)...

									Random random = new Random();
									if (random.nextBoolean()) {
										System.out.println("Random move!");
									}
								}
							}
						} catch (Exception e) {
							System.out.println("Error: " + e.getMessage());
							break;
						}
					}
				});
				Executors.FIXED.execute(() -> {
					while (true) {
						try {
							String data = read(remoteInputStream, localOutputStream);
							if (data != null) {
								// System.out.println("[GAME] <- " + data);
							}
						} catch (Exception e) {
							System.out.println("Error: " + e.getMessage());
							break;
						}
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	public ServerSocket getLocal() {
		return local;
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
			outputStream.write(buffer);

			return data.replace("\n", "").replace("\0", "");
		}
		return null;
	}
}
