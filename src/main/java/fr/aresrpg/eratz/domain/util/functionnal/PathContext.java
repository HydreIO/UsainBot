package fr.aresrpg.eratz.domain.util.functionnal;

/**
 * A context which represent the execution of a path
 * 
 * @since
 */
public interface PathContext {

	String print();

	int getOriginMapId();

	int getOriginCellId();

	int getDestinationMapId();

	int getDestinationCellId();

	static PathContext empty() {
		return new PathContext() {

			@Override
			public String print() {
				return "Undefined context";
			}

			@Override
			public int getOriginMapId() {
				return -1;
			}

			@Override
			public int getOriginCellId() {
				return -1;
			}

			@Override
			public int getDestinationMapId() {
				return -1;
			}

			@Override
			public int getDestinationCellId() {
				return -1;
			}
		};
	}

}
