package fr.aresrpg.eratz.domain.util.serialization;

import fr.aresrpg.commons.domain.reflection.ParametrizedClass;
import fr.aresrpg.commons.domain.serialization.*;
import fr.aresrpg.commons.domain.serialization.adapters.Adapter;
import fr.aresrpg.commons.domain.serialization.factory.SerializationFactory;
import fr.aresrpg.commons.domain.types.TypeEnum;
import fr.aresrpg.commons.domain.unsafe.UnsafeAccessor;
import fr.aresrpg.commons.infra.serialization.BasicSerializationContext;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import sun.misc.Unsafe;

/**
 * 
 * @since
 */
public class BotSerializer<T> implements Serializer<T> {
	private static final Unsafe UNSAFE = UnsafeAccessor.getUnsafe();

	private final SerializationFactory factory;
	private final SerializationContext context;
	private final Class<T> clazz;

	public BotSerializer(Class<T> clazz, SerializationFactory factory) {
		this.factory = factory;
		this.clazz = clazz;
		this.context = new BasicSerializationContext(factory);
	}

	@Override
	public <O> void serialize(O output, T object, Format<?, O> format) throws IOException {
		Map<String, Object> in = null;
		if (object instanceof Map) in = (Map<String, Object>) object;
		else {
			Adapter<Object, Map<String, Object>> adapter = (Adapter<Object, Map<String, Object>>) factory.getAdapter(new ParametrizedClass<>(object.getClass()));
			if (adapter == null) throw new NullPointerException("Serialization error | The type " + object.getClass() + " doesn't have an adapter !");
			in = adapter.adaptTo(object);
		}
		format.writeBegin(output);
		for (Entry<String, Object> i : in.entrySet()) {
			format.writeBeginObject(output);
			TypeEnum type = TypeEnum.getType(i.getValue());
			format.writeValue(output, i.getKey(), type, i.getValue(), context);
			format.writeEndObject(output);
		}
		format.writeEnd(output);
	}

	@Override
	public <I> void deserialize(I input, T object, Format<I, ?> format) throws IOException {
		Object o = format.read(input);
		if (o instanceof Map)
			deserialize((Map<String, Object>) o, object);
		else
			throw new IllegalStateException("Trying to deserialize non object type into object");
	}

	@Override
	public <I> T deserialize(I input, Format<I, ?> format) throws IOException {
		Object o = format.read(input);
		if (o instanceof Map)
			try {
			T object = (T) UNSAFE.allocateInstance(clazz);
			deserialize((Map<String, Object>) o, object);
			return object;
		} catch (InstantiationException e) {
			throw new IOException(e);
		}
		else
			return (T) o;
	}

	@Override
	public T deserialize(Map<String, Object> values) throws IOException {
		try {
			T object = (T) UNSAFE.allocateInstance(clazz);
			deserialize(values, object);
			return object;
		} catch (InstantiationException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void deserialize(Map<String, Object> values, T object) throws IOException {
		Adapter<Object, Map<String, Object>> adapter = (Adapter<Object, Map<String, Object>>) factory.getAdapter(new ParametrizedClass<>(object.getClass()));
		if (adapter == null) throw new NullPointerException("Deserialization error | The type " + object.getClass() + " doesn't have an adapter !");
		Object result = adapter.adaptFrom(values);
		// t baisé morray
	}

}
