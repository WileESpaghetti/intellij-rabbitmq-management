package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.panels.NonOpaquePanel;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
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

        this.setEmptyRightPanel();
    }

    @Override
    protected void resetEditorFrom(@NotNull Settings s) {

    }

    private void setEmptyRightPanel() {
        this.myRightPanel.removeAll();

        JPanel emptyRightPanelText = new JPanel(new FlowLayout(0, 0, 0));
        emptyRightPanelText.setBorder(JBUI.Borders.empty(10));
        emptyRightPanelText.add(new JLabel("Please add a connection to configure"));

        JScrollPane var4 = ScrollPaneFactory.createScrollPane(emptyRightPanelText, true);
        this.myRightPanel.add(var4, BorderLayout.CENTER);

        UIUtil.setupEnclosingDialogBounds(this.myRootPanel);
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
