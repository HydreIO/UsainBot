package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.commons.domain.log.Log;
import fr.aresrpg.commons.domain.log.handler.Handler;
import fr.aresrpg.commons.domain.log.handler.formatters.BasicFormatter;

import java.io.IOException;

/**
 * 
 * @since
 */
public class HastebinLoggerHandler implements Handler {

	@Override
	public void handle(Log log) throws IOException {
		Logs.stream
				.write(("[" + BasicFormatter.DEFAULT_FORMAT.format(new java.util.Date(log.getMillis())) + "][" + log.getThread().getName() + "][" + log.getLevel() + "]: " + log.getMessage() + "\n")
						.getBytes());
	}

}
