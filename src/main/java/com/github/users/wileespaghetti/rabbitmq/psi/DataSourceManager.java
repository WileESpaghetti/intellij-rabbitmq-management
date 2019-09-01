package com.github.users.wileespaghetti.rabbitmq.psi;

import com.intellij.openapi.extensions.ProjectExtensionPointName;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// com.intellij.database.psi.DataSourceManager
public class DataSourceManager {
    public static final ProjectExtensionPointName EP_NAME = new ProjectExtensionPointName("com.github.users.wileespaghetti.rabbitmq.dataSourceManager");
    public static final Topic TOPIC = Topic.create("RABBITMQ_DATASOURCE_TOPIC", Listener.class);

    @NotNull
    public static List getManagers(@NotNull Project project) {
        return EP_NAME.getExtensions(project);
    }

    //////////\\\\\\\\\\

    // com.intellij.database.psi.DataSourceManager$Listener
    static class Listener {
    }
}

