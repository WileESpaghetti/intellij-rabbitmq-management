package com.github.users.wileespaghetti.rabbitmq.actions.testing;

import com.github.users.wileespaghetti.rabbitmq.view.ui.RabbitmqConnectionManagerDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class OpenRabbitmqConfigurationDialog extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        RabbitmqConnectionManagerDialog.showDialog();
    }
}
