package fr.aresrpg.eratz.domain.util.functionnal;

import fr.aresrpg.eratz.domain.data.map.Destination;
import fr.aresrpg.tofumanchou.infra.data.ManchouCell;

/**
 * 
 * @since
 */
@FunctionalInterface
public interface PathDestination {

	Destination getDestination(ManchouCell cell);

}
