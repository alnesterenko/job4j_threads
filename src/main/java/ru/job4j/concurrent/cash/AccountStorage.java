package ru.job4j.concurrent.cash;

import java.util.HashMap;
import java.util.Optional;

public class AccountStorage {
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public boolean add(Account account) {
        synchronized (accounts) {
            accounts.put(account.id(), account);
            return accounts.get(account.id()).equals(account);
        }
    }

    public boolean update(Account account) {
        synchronized (accounts) {
            Account tempAccount = accounts.get(account.id());
            accounts.put(account.id(), account);
            return !accounts.get(account.id()).equals(tempAccount);
        }
    }

    public void delete(int id) {
        synchronized (accounts) {
            accounts.remove(id);
        }
    }

    public Optional<Account> getById(int id) {
        synchronized (accounts) {
            return Optional.ofNullable(accounts.get(id));
        }
    }

    public boolean transfer(int fromId, int toId, int amount) {
        synchronized (accounts) {
            boolean result = true;
            Optional<Account> from = getById(fromId);
            Optional<Account> to = getById(toId);
            if (from.isPresent() && to.isPresent() && from.get().amount() >= amount && amount > 0 && fromId != toId) {
                update(new Account(fromId, from.get().amount() - amount));
                update(new Account(toId, to.get().amount() + amount));
            } else {
                result = false;
            }
            return result;
        }
    }
}

