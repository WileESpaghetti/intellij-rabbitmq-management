package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolder;

// com.intellij.database.view.ui.DatabaseConfigEditor
public interface RabbitmqConfigEditor extends UserDataHolder {
    Key DO_NOT_WATCH = Key.create("DO_NOT_WATCH");
    Key SELECTED_TAB_KEY = Key.create("DataSourceConfigurable.defaultTabIndex");
}
