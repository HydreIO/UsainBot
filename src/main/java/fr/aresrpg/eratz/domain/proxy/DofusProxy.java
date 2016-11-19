package fr.aresrpg.eratz.domain.proxy;

import java.io.InputStream;
import java.io.OutputStream;

public interface DofusProxy {
    /**
     * @return Local output stream (client)
     */
    InputStream getLocalInputStream();

    /**
     * @return Local output stream (client)
     */
    OutputStream getLocalOutputStream();

    /**
     * @return Remote input stream (server)
     */
    InputStream getRemoteInputStream();

    /**
     * @return Remote output stream (server)
     */
    OutputStream getRemoteOutputStream();
}
