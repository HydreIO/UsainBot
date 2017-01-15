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

	static PathContext provided(int origin, int originCell, int dest, int destCell, String context) {
		return new PathContext() {

			@Override
			public String print() {
				return context;
			}

			@Override
			public int getOriginMapId() {
				return origin;
			}

			@Override
			public int getOriginCellId() {
				return originCell;
			}

			@Override
			public int getDestinationMapId() {
				return dest;
			}

			@Override
			public int getDestinationCellId() {
				return destCell;
			}
		};
	}

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
