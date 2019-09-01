package com.github.users.wileespaghetti.rabbitmq.connectionType;

import com.intellij.openapi.util.Iconable;
import org.jetbrains.annotations.Nullable;

// com.intellij.database.dataSource.DatabaseDriver
public interface ConnectionType extends Iconable {
    String getId();
    String getName();
    boolean isPredefined();
    LocalDataSource createDataSource(@Nullable String name);
}
