package console;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Pair<K, V> {
    public K key;
    public V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <T> Stream<Pair<Integer, T>> fromArray(T[] values) {
        return IntStream.range(0, values.length)
            .mapToObj(i -> new Pair<Integer, T>(i, values[i]));
    }
}
