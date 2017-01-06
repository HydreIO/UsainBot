package fr.aresrpg.eratz.domain.util.serialization;

import fr.aresrpg.commons.domain.reflection.ParametrizedClass;
import fr.aresrpg.commons.domain.serialization.FieldNamer;
import fr.aresrpg.commons.domain.serialization.Serializer;
import fr.aresrpg.commons.domain.serialization.adapters.Adapter;
import fr.aresrpg.commons.domain.serialization.factory.SerializationFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class BotSerializationFactory implements SerializationFactory {

	private final Map<ParametrizedClass<?>, Adapter<?, Map>> adapters = new HashMap<>();
	private final Map<Class<?>, Serializer<?>> serializers = new HashMap<>();

	@Override
	public <T> Serializer<T> createSerializer(Class<T> clazz) {
		Serializer<T> serializer = new BotSerializer<>(clazz, this);
		serializers.put(clazz, serializer);
		return serializer;
	}

	@Override
	public <T> Serializer<T> createOrGetSerializer(Class<T> clazz) {
		Serializer<?> serializer = serializers.get(clazz);
		if (serializer == null) return createSerializer(clazz);
		return (Serializer<T>) serializer;
	}

	@Override
	public List<Adapter<?, ?>> getAdapters() {
		return adapters.values().stream().collect(Collectors.toList());
	}

	@Override
	public void addAdapter(Adapter<?, ?> adapter) {
		if (adapter.getOutType().getType() != Map.class) throw new IllegalArgumentException("This serializer only use Map adapters");
		adapters.put(adapter.getInType(), (Adapter<?, Map>) adapter);
	}

	@Override
	public void removeAdapter(Adapter<?, ?> adapter) {
		adapters.remove(adapter.getInType(), adapter);
	}

	@Override
	public <T> Adapter<T, ?> getAdapter(ParametrizedClass<T> clazz) {
		Adapter<?, Map> adapter = adapters.get(clazz);
		if (adapter == null) return null;
		if (!adapter.getInType().equals(clazz)) throw new IllegalArgumentException("The type of the adapter mapped with " + clazz + " hasn't the valid type");
		return (Adapter<T, ?>) adapter;
	}

	@Override
	public FieldNamer getFieldNamer() {
		// unused
		return null;
	}

	@Override
	public void setFieldNamer(FieldNamer namer) {
		// unused
	}

}
