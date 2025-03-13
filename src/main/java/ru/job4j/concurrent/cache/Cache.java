package ru.job4j.concurrent.cache;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) throws OptimisticException {
        return memory.putIfAbsent(model.id(), model) == null;
    }

    public boolean update(Base model) throws OptimisticException {
        /* TODO impl */
        return false;
    }

    public void delete(int id) {
        /* TODO impl */
    }

    public Optional<Base> findById(int id) {
        return Stream.of(memory.get(id))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
