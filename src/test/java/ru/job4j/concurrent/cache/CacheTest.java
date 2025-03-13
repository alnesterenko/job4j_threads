package ru.job4j.concurrent.cache;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CacheTest {
    @Test
    public void whenAddFind() throws OptimisticException {
        var base = new Base(1,  "Base", 1);
        var cache = new Cache();
        cache.add(base);
        var find = cache.findById(base.id());
        assertThat(find.get().name())
                .isEqualTo("Base");
    }

    @Test
    public void whenAddUpdateFind() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(new Base(1, "Base updated", 1));
        var find = cache.findById(base.id());
        assertThat(find.get().name())
                .isEqualTo("Base updated");
    }

    @Test
    public void whenAddDeleteFind() throws OptimisticException {
        var base = new Base(1,   "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.delete(1);
        var find = cache.findById(base.id());
        assertThat(find.isEmpty()).isTrue();
    }

    @Test
    public void whenMultiUpdateThrowException() throws OptimisticException {
        var base = new Base(1,  "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(base);
        assertThatThrownBy(() -> cache.update(base))
                .isInstanceOf(OptimisticException.class);
    }

    /* Да, при многопоточке, следующий тест не сработает. */
    @Test
    public void whenMultiUpdateThenOk() throws OptimisticException {
        var firstBase = new Base(1,  "Base", 1);
        var cache = new Cache();
        cache.add(firstBase);
        var currentBase = cache.findById(1).get();
        cache.update(new Base(currentBase.id(), "secondBase", currentBase.version()));
        currentBase = cache.findById(1).get();
        cache.update(new Base(currentBase.id(), "thirdBase", currentBase.version()));
        currentBase = cache.findById(1).get();
        cache.update(new Base(currentBase.id(), "fourthBase", currentBase.version()));
        currentBase = cache.findById(1).get();
        assertThat(currentBase.version()).isEqualTo(4);
        assertThat(currentBase.name()).isEqualTo("fourthBase");
    }
}