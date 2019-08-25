package com.github.users.wileespaghetti.rabbitmq.connectionType;

import com.github.users.wileespaghetti.rabbitmq.view.ui.RabbitmqConfigEditor;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

// com.intellij.database.dataSource.AbstractDatabaseConfigurable
public abstract class AbstractRabbitmqConfigurable<Target> implements Disposable, Configurable {
    protected final Object myTarget;
    protected RabbitmqConfigEditor myController;

    protected AbstractRabbitmqConfigurable(@NotNull Target target) {
        this.myTarget = target;
    }

    @Override
    public void dispose() {

    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return null;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    public void init() {
        this.reset();
    }

    @NotNull
    public abstract Target getTempTarget();

    public void onUserActivity() {
    }
}
