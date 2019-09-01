package com.github.users.wileespaghetti.rabbitmq.connectionType;

import org.jetbrains.annotations.NotNull;

public class AbstractDataSource {
    private String myName = "";
    public void setName(@NotNull String name) {
        this.myName = name;
    }
}
