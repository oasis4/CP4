package com.github.oasis.craftprotect.storage;

import com.github.oasis.craftprotect.api.CraftProtectUser;

import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class AsyncUserStorage extends UserStorage {

    public AsyncUserStorage(Supplier<Connection> connectionSupplier) {
        super(connectionSupplier);
    }

    public CompletableFuture<Void> persistAsync(CraftProtectUser user) {
        return CompletableFuture.runAsync(() -> persist(user));
    }

    public CompletableFuture<Optional<CraftProtectUser>> findUserAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> findUser(id));
    }

    public CompletableFuture<Stream<CraftProtectUser>> getUsersAsync() {
        return CompletableFuture.supplyAsync(this::getUsers);
    }

    public CompletableFuture<Void> deleteAsync(CraftProtectUser user) {
        return CompletableFuture.runAsync(() -> delete(user));
    }

    public CompletableFuture<Void> deleteAsync(UUID id) {
        return CompletableFuture.runAsync(() -> delete(id));
    }

}
