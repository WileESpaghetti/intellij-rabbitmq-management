package com.github.users.wileespaghetti.rabbitmq.psi;

import com.intellij.openapi.util.text.StringUtil;

import java.util.Comparator;

public class ConnectionTypePresentation {
    public static final Comparator NAMES_COMPARATOR = (connectionTypeName1, connectionTypeName2) -> {
        return StringUtil.compare(connectionTypeName1.toString(), connectionTypeName2.toString(), true);
    };
}
