package com.github.users.wileespaghetti.rabbitmq.connectionType;

import org.jetbrains.annotations.NotNull;

// com.intellij.database.dataSource.LocalDataSource
public class LocalDataSource extends AbstractDataSource {
    @NotNull
    public static LocalDataSource fromDriver(@NotNull ConnectionType connectionType, boolean isTemporary) {
        LocalDataSource dataSource = new LocalDataSource();

        return dataSource;
    }
}
