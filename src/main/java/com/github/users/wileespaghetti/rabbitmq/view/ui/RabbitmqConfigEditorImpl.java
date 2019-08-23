package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.github.users.wileespaghetti.rabbitmq.connectionType.ConnectionType;
import com.github.users.wileespaghetti.rabbitmq.connectionType.ConnectionTypeManager;
import com.github.users.wileespaghetti.rabbitmq.psi.ConnectionTypePresentation;
import com.github.users.wileespaghetti.rabbitmq.view.ui.ConnectionsSidePanel.ComponentConfigurator;
import com.github.users.wileespaghetti.rabbitmq.view.ui.SidePanelItem.ConnectionTypeItem;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.components.panels.NonOpaquePanel;
import com.intellij.ui.navigation.Place;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

// com.intellij.database.view.ui.DatabaseConfigEditorImpl
public class RabbitmqConfigEditorImpl<Settings> extends SettingsEditor<Settings> {
    private JPanel myRootPanel;
    private JPanel myRightPanel;
    private ConnectionsSidePanel mySidePanel;
    private final ManagementApiSettings mySettings;
    private static final Logger LOG = Logger.getInstance(RabbitmqConfigEditorImpl.class);
    private final Map mySidePanelItems;

    RabbitmqConfigEditorImpl(@NotNull ManagementApiSettings settings) {
        super();

        this.mySidePanelItems = ContainerUtil.newIdentityHashMap();

        int connectionTypeCount = settings.newConnectionTypes.size();
        if (connectionTypeCount > 10) {
            LOG.info("Connection Types: " + connectionTypeCount);
        }

        this.mySettings = settings;

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

        this.resetTreeModel(settings);

        this.setEmptyRightPanel();
    }

    private void resetTreeModel(ManagementApiSettings settings) {
        this.refillSidePanel(settings);
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
        this.mySidePanel = new ConnectionsSidePanel(new MyComponentConfigurator());

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(this.mySidePanel.getComponent(), "Center");
        return leftPanel;
    }

    @Override
    protected void applyEditorTo(@NotNull Settings s) throws ConfigurationException {

    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myRootPanel;
    }

    private void refillSidePanel(@NotNull ManagementApiSettings settings) {
        boolean isValueAdjusting = false;

        try {
            isValueAdjusting = true;
            this.mySidePanel.getList().getSelectionModel().setValueIsAdjusting(isValueAdjusting);
            this.mySidePanel.clear();

            HashSet<SidePanelItem> sidebarItems = ContainerUtil.newHashSet();

            Iterator<ConnectionType> connectionTypes = this.getAllConnectionTypes(settings).iterator();

            while(true) {
                if (!connectionTypes.hasNext()) {
                    isValueAdjusting = false;
                    break;
                }

                ConnectionType connectionType = connectionTypes.next();
                ConnectionTypeItem sidePanelItem = this.createItem(connectionType);

                sidebarItems.add(this.addToSidePanel(sidePanelItem));
            }
        } finally {
            if (isValueAdjusting) {
                this.mySidePanel.getList().getSelectionModel().setValueIsAdjusting(false);
            }
        }

        this.mySidePanel.getList().getSelectionModel().setValueIsAdjusting(false);
    }

    @NotNull
    private ConnectionTypeItem createItem(@NotNull ConnectionType connectionType) {
        ConnectionTypeItem item = (ConnectionTypeItem)this.mySidePanelItems.get(connectionType);
        if (item == null) {
            item = new ConnectionTypeItem(connectionType);
        }

        return (ConnectionTypeItem) this.createItem(item);
    }

    private SidePanelItem createItem(@NotNull SidePanelItem item) {
        this.mySidePanelItems.put(item.getObject(), item);
        return item;
    }

    @NotNull
    private SidePanelItem addToSidePanel(@NotNull SidePanelItem item) {
        Place itemPlace = item.createPlace();
        this.mySidePanel.addPlace(itemPlace);
        return item;
    }

    private Collection<ConnectionType> getAllConnectionTypes(ManagementApiSettings settings) {
        ConnectionTypeManager connectionTypeManager = ConnectionTypeManager.getInstance();
        ArrayList<ConnectionType> connectionTypes = ContainerUtil.newArrayList(connectionTypeManager.getConnectionTypes());
        connectionTypes.addAll(settings.newConnectionTypes);
        Collections.sort(connectionTypes, Comparator.comparing(ConnectionType::getName, ConnectionTypePresentation.NAMES_COMPARATOR));
        return connectionTypes;
    }

    // com.intellij.database.view.ui.DatabaseConfigEditorImpl$DataSourceSettings
    public static class ManagementApiSettings {
        final List<ConnectionType> newConnectionTypes;

        ManagementApiSettings() {
            super();
            this.newConnectionTypes = ContainerUtil.newArrayList();
        }
    }

    //////////\\\\\\\\\\

    class MyComponentConfigurator implements ComponentConfigurator {
        public void configure(@NotNull SimpleColoredComponent component, @NotNull Place itemPlace, boolean isSelected) {
            SidePanelItem sidePanelItem = SidePanelItem.getItem(itemPlace);
            if (sidePanelItem != null) {
                String connectionTypeName = sidePanelItem.getName();
                if (connectionTypeName != null) {
                    component.append(connectionTypeName);
                }
            }
        }
    }
}
