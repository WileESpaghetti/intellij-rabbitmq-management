package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.components.panels.NonOpaquePanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

// com.intellij.database.view.ui.DatabaseConfigEditorImpl
public class RabbitmqConfigEditorImpl<Settings> extends SettingsEditor<Settings> {
    private JPanel myRootPanel;
    private JPanel myRightPanel;

    RabbitmqConfigEditorImpl() {
        super();

        this.myRootPanel = new NonOpaquePanel(new BorderLayout());
        this.myRightPanel = new NonOpaquePanel(new BorderLayout(0, 5));
        myRightPanel.add(createLeftPanel());

        OnePixelSplitter sidebarContainer = new OnePixelSplitter(false, 0.3F);
        sidebarContainer.setFirstComponent(this.createLeftPanel());
        sidebarContainer.setSecondComponent(this.myRightPanel);
        this.myRootPanel.add(sidebarContainer, BorderLayout.CENTER);

        Dimension rootPanelPreferredSize = this.myRootPanel.getPreferredSize();
        rootPanelPreferredSize.width = Math.max(rootPanelPreferredSize.width, 800);
        rootPanelPreferredSize.height = Math.max(rootPanelPreferredSize.height, 600);
        this.myRootPanel.setPreferredSize(rootPanelPreferredSize);
    }

    @Override
    protected void resetEditorFrom(@NotNull Settings s) {

    }

    private JComponent createLeftPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Hello World");
        label.setPreferredSize(new Dimension(100, 100));
        dialogPanel.add(label, BorderLayout.CENTER);

        return dialogPanel;
    }

    @Override
    protected void applyEditorTo(@NotNull Settings s) throws ConfigurationException {

    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myRootPanel;
    }

    // com.intellij.database.view.ui.DatabaseConfigEditorImpl$DataSourceSettings
    public static class ManagementApiSettings {
    }
}
