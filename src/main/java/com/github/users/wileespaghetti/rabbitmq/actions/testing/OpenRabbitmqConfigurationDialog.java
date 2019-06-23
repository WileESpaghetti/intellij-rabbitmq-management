package com.github.users.wileespaghetti.rabbitmq.actions.testing;

import com.github.users.wileespaghetti.rabbitmq.view.ui.RabbitmqConnectionManagerDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

// com.intellij.database.actions.ManageDataSourcesAction
public class OpenRabbitmqConfigurationDialog extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();

        if (project != null) {
            RabbitmqConnectionManagerDialog.showDialog(project);
        }

    }
}
