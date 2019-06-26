package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.github.users.wileespaghetti.rabbitmq.view.ui.RabbitmqConfigEditorImpl.ManagementApiSettings;
import com.intellij.openapi.options.SettingsEditorConfigurable;
import com.intellij.openapi.options.ex.SingleConfigurableEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// com.intellij.database.view.ui.DataSourceManagerDialog
public class RabbitmqConnectionManagerDialog extends SingleConfigurableEditor {
    @NotNull
    @Override
    protected DialogStyle getStyle() {
        return DialogStyle.COMPACT;
    }

    private RabbitmqConnectionManagerDialog(@Nullable Project project, @NotNull ManagementApiSettings settings) {
        super(project, new MyConfigurable(settings));
    }

    public static void showDialog(@NotNull Project project) {
        ManagementApiSettings apiSettings = new ManagementApiSettings();

        RabbitmqConnectionManagerDialog dialog = new RabbitmqConnectionManagerDialog(project, apiSettings);
        dialog.show();
    }

    // com.intellij.database.view.ui.DataSourceManagerDialog$MyConfigurable
    private static class MyConfigurable extends SettingsEditorConfigurable<ManagementApiSettings> {
        public MyConfigurable(@NotNull ManagementApiSettings settings) {
            super(new RabbitmqConfigEditorImpl<>(), settings);
        }

        @Nls(capitalization = Nls.Capitalization.Title)
        @Override
        public String getDisplayName() {
            return "RabbitMQ";
        }
    }
}
