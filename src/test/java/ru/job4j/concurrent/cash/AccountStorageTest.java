package ru.job4j.concurrent.cash;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class AccountStorageTest {

    @Test
    void whenAdd() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(100);
    }

    @Test
    void whenUpdate() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        boolean updateSuccess = storage.update(new Account(1, 200));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(200);
        assertThat(updateSuccess).isTrue();
    }

    @Test
    void whenNoUpdate() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        boolean updateSuccess = storage.update(new Account(1, 100));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(100);
        assertThat(updateSuccess).isTrue();
    }

    @Test
    void whenDelete() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.delete(1);
        assertThat(storage.getById(1)).isEmpty();
    }

    @Test
    void whenTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        storage.transfer(1, 2, 100);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 2"));
        assertThat(firstAccount.amount()).isEqualTo(0);
        assertThat(secondAccount.amount()).isEqualTo(200);
    }

    @Test
    void whenFailTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        boolean transferSuccess = storage.transfer(1, 2, 200);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 2"));
        assertThat(firstAccount.amount()).isEqualTo(100);
        assertThat(secondAccount.amount()).isEqualTo(100);
        assertThat(transferSuccess).isFalse();
    }

    @Test
    void whenTransferMinusAmount() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        boolean transferSuccess = storage.transfer(1, 2, -100);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 2"));
        assertThat(firstAccount.amount()).isEqualTo(100);
        assertThat(secondAccount.amount()).isEqualTo(100);
        assertThat(transferSuccess).isFalse();
    }

    @Test
    void whenTransferZero() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        boolean transferSuccess = storage.transfer(1, 2, 0);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 2"));
        assertThat(firstAccount.amount()).isEqualTo(100);
        assertThat(secondAccount.amount()).isEqualTo(100);
        assertThat(transferSuccess).isFalse();
    }

    @Test
    void whenTryTransferInOneAccount() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        boolean transferSuccess = storage.transfer(1, 1, 100);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(100);
        assertThat(transferSuccess).isFalse();
    }
}