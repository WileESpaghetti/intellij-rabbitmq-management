package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.intellij.openapi.options.SettingsEditorConfigurable;
import com.intellij.openapi.options.ex.SingleConfigurableEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// com.intellij.database.view.ui.DataSourceManagerDialog
public class RabbitmqConnectionManagerDialog extends SingleConfigurableEditor {
    private RabbitmqConnectionManagerDialog(@Nullable Project project, @NotNull RabbitmqConfigEditorImpl.ManagementApiSettings settings) {
        super(project, new MyConfigurable(settings));
    }

    public static void showDialog(@NotNull Project project) {
        RabbitmqConfigEditorImpl.ManagementApiSettings apiSettings = new RabbitmqConfigEditorImpl.ManagementApiSettings();

        RabbitmqConnectionManagerDialog dialog = new RabbitmqConnectionManagerDialog(project, apiSettings);
        dialog.show();
    }

    // com.intellij.database.view.ui.DataSourceManagerDialog$MyConfigurable
    private static class MyConfigurable extends SettingsEditorConfigurable<RabbitmqConfigEditorImpl.ManagementApiSettings> {
        public MyConfigurable(@NotNull RabbitmqConfigEditorImpl.ManagementApiSettings settings) {
            super(new RabbitmqConfigEditorImpl<>(), settings);
        }

        @Nls(capitalization = Nls.Capitalization.Title)
        @Override
        public String getDisplayName() {
            return "RabbitMQ";
        }
    }
}
