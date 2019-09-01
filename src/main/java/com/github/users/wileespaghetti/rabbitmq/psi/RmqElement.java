package com.github.users.wileespaghetti.rabbitmq.psi;

import org.jetbrains.annotations.NotNull;

// com.intellij.database.psi.DbElement
public interface RmqElement {
    @NotNull
    Object getDelegate();
}
