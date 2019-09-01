package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

// com.intellij.database.view.ui.DatabaseConfigEditor
public interface RabbitmqConfigEditor extends UserDataHolder {
    Key DO_NOT_WATCH = Key.create("DO_NOT_WATCH");
    Key SELECTED_TAB_KEY = Key.create("DataSourceConfigurable.defaultTabIndex");

    boolean isObjectNameUnique(@Nullable Object target, String name);

    void showErrorNotification(@NotNull Configurable configurable, @NotNull Object problemId, @Nullable Exception e);

    void showErrorNotification(@NotNull Configurable configurable, @NotNull Object problemId, @Nullable String message, @Nullable String description, @NotNull Object... problemOrigin);
    @Nullable
    JComponent createResetAction(@Nullable Configurable configurable);

}
