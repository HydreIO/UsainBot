package fr.aresrpg.eratz.domain.util.functionnal;

import fr.aresrpg.eratz.domain.util.BotNode;

import java.util.function.Function;

@FunctionalInterface
public interface NodePricer {
	int getPrice(BotNode node);

	static Function<BotNode, BotNode> zaapPrice() {
		return n -> {
			n.setCost(10);
			return n;
		};
	}

	static Function<BotNode, BotNode> zaapiPrice() {
		return n -> {
			n.setCost(2);
			return n;
		};
	}
}