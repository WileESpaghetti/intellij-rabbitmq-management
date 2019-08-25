package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.github.users.wileespaghetti.rabbitmq.connectionType.ConnectionType;
import com.github.users.wileespaghetti.rabbitmq.connectionType.ConnectionTypeManager;
import com.github.users.wileespaghetti.rabbitmq.psi.ConnectionTypePresentation;
import com.github.users.wileespaghetti.rabbitmq.view.ui.ConnectionsSidePanel.ComponentConfigurator;
import com.github.users.wileespaghetti.rabbitmq.view.ui.SidePanelItem.ConnectionTypeItem;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.util.ActionCallback;
import com.intellij.openapi.util.Key;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.components.panels.NonOpaquePanel;
import com.intellij.ui.navigation.Place;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.JBUI.Borders;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.update.UiNotifyConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;
import java.util.*;

// com.intellij.database.view.ui.DatabaseConfigEditorImpl
public class RabbitmqConfigEditorImpl<Settings> extends SettingsEditor<Settings> implements RabbitmqConfigEditor {
    private JPanel myRootPanel;
    private JPanel myRightPanel;
    private ConnectionsSidePanel mySidePanel;
    private final ManagementApiSettings mySettings;
    private static final Logger LOG = Logger.getInstance(RabbitmqConfigEditorImpl.class);
    private final Map<Object, SidePanelItem> mySidePanelItems;
    private SidePanelItem mySelectedItem;

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
        emptyRightPanelText.setBorder(Borders.empty(10));
        emptyRightPanelText.add(new JLabel("Please add a connection to configure"));

        JScrollPane var4 = ScrollPaneFactory.createScrollPane(emptyRightPanelText, true);
        this.myRightPanel.add(var4, BorderLayout.CENTER);

        UIUtil.setupEnclosingDialogBounds(this.myRootPanel);
    }

    private JComponent createLeftPanel() {
        this.mySidePanel = new ConnectionsSidePanel(new MyComponentConfigurator());
        this.mySidePanel.getList().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Object selectedValue = ((JList)e.getSource()).getSelectedValue();
                    Object place = SidePanelItem.getObject(SidePanelItem.getItem((Place) ObjectUtils.tryCast(selectedValue, Place.class)));
                    navigateTo(place, false);
                }
            }
        });

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
            item = new ConnectionTypeItem(this, connectionType);
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

    @NotNull
    public ActionCallback navigateTo(@Nullable Object place, boolean var2) {
        return this.navigateTo(this.getPlace(place), var2);
    }

    public ActionCallback navigateTo(@Nullable Place place, boolean var2) {
        SidePanelItem item = this.getItem(place);
        if (item != this.mySelectedItem) {
            this.setSelectedEditor(item);
            if (this.mySelectedItem != null) {
                if (var2) {
                    Configurable configurable = this.mySelectedItem.getConfigurable();
                    JComponent configurableComponent = configurable == null ? null : configurable.getPreferredFocusedComponent();
                    if (configurableComponent != null) {
                        UiNotifyConnector.doWhenFirstShown(configurableComponent, () -> {
                            UIUtil.requestFocus(configurableComponent);
                        });
                    }
                }
            }

            this.mySidePanel.select(place);
            this.updateSidePanelImpl();
        }

        return ActionCallback.DONE;
    }

    private void updateSidePanelImpl() {
        SidePanelItem item = this.mySelectedItem;
        if (item != null) {
            this.mySidePanel.getList().invalidate();
            this.mySidePanel.getList().repaint();
        }
    }

    private void setSelectedEditor(@Nullable SidePanelItem item) {
        if (item != this.mySelectedItem || this.myRightPanel.getComponentCount() == 0) {
            this.myRightPanel.removeAll();
            this.mySelectedItem = item;
            createEditorIfNeeded(item);
            if (item != null && item.getConfigurable() != null) {

                this.myRightPanel.add(ObjectUtils.assertNotNull(item.getComponent()), BorderLayout.CENTER);
            }

            UIUtil.setupEnclosingDialogBounds(this.myRootPanel);
        }
    }

    private static Configurable createEditorIfNeeded(@Nullable SidePanelItem item) {
        if (item != null && item.getConfigurable() == null) {
            item.createConfigurable();
        }

        return item == null ? null : item.getConfigurable();
    }

    @Nullable
    private Place getPlace(@Nullable Object key) {
        SidePanelItem item = this.mySidePanelItems.get(key);
        return item == null ? null : item.getPlace();
    }

    @Nullable
    private SidePanelItem getItem(@Nullable Place place) {
        SidePanelItem item = SidePanelItem.getItem(place);
        if (item != null) {
            item = this.mySidePanelItems.get(item.getObject());
        }

        return item;
    }

    @Nullable
    @Override
    public <T> T getUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {

    }

    //////////\\\\\\\\\\

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
