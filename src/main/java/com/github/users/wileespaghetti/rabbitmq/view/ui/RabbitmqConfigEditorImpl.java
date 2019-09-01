package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.github.users.wileespaghetti.rabbitmq.connectionType.AbstractRabbitmqConfigurable;
import com.github.users.wileespaghetti.rabbitmq.connectionType.ConnectionType;
import com.github.users.wileespaghetti.rabbitmq.connectionType.ConnectionTypeManager;
import com.github.users.wileespaghetti.rabbitmq.connectionType.LocalDataSourceManager;
import com.github.users.wileespaghetti.rabbitmq.psi.ConnectionTypePresentation;
import com.github.users.wileespaghetti.rabbitmq.psi.RmqElement;
import com.github.users.wileespaghetti.rabbitmq.util.RmqUiUtil;
import com.github.users.wileespaghetti.rabbitmq.view.ui.ConnectionsSidePanel.ComponentConfigurator;
import com.github.users.wileespaghetti.rabbitmq.view.ui.SidePanelItem.ConnectionTypeItem;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.*;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.components.panels.NonOpaquePanel;
import com.intellij.ui.navigation.Place;
import com.intellij.util.Function;
import com.intellij.util.IconUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.JBIterable;
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
    private AddAction myAddAction;
    private final List myRemovedObjects;

    RabbitmqConfigEditorImpl(@NotNull ManagementApiSettings settings) {
        super();

        this.mySidePanelItems = ContainerUtil.newIdentityHashMap();
        this.myRemovedObjects = ContainerUtil.newArrayList();

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
        this.myAddAction = new AddAction();

        JPanel leftPanel = new JPanel(new BorderLayout()); // var14
        JPanel toolbar = new JPanel(new BorderLayout()); // var13
        toolbar.add(createToolbarComponent(this.myAddAction), BorderLayout.CENTER);
        leftPanel.add(toolbar, BorderLayout.NORTH);
        leftPanel.add(this.mySidePanel.getComponent(), BorderLayout.CENTER);
        return leftPanel;
    }

    @NotNull
    private JComponent createToolbarComponent(@NotNull AnAction... actions) {
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.addAll(actions);
        ActionToolbar var3 = ActionManager.getInstance().createActionToolbar("ProjectViewToolbar", actionGroup, true);
        var3.setReservePlaceAutoPopupIcon(false);
        var3.setTargetComponent(this.myRootPanel);
        JComponent var4 = var3.getComponent();
        var4.setBackground(UIUtil.SIDE_PANEL_BACKGROUND);

        return var4;
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

    @Override
    public boolean isObjectNameUnique(@Nullable Object target, String name) {
        Kind targetKind = Kind.kindOf(target);
        if (targetKind == Kind.OTHER) {
            return true;
        } else {
            Iterator sidePanelItems = this.mySidePanelItems.values().iterator();

            SidePanelItem sidePanelItem;
            Object sidePanelItemRepresented;
            do {
                    do {
                        do {
                            if (!sidePanelItems.hasNext()) {
                                return true;
                            }

                            sidePanelItem = (SidePanelItem)sidePanelItems.next();
                            sidePanelItemRepresented = sidePanelItem.getObject();
                        } while(Kind.kindOf(sidePanelItemRepresented) != targetKind);
                    } while(sidePanelItemRepresented == target);
            } while(!Comparing.equal(sidePanelItem.getName(), name));

            return false;
        }
    }

    @Override
    public void showErrorNotification(@NotNull Configurable configurable, @NotNull Object problemId, @Nullable Exception e) {

    }

    @Override
    public void showErrorNotification(@NotNull Configurable configurable, @NotNull Object problemId, @Nullable String message, @Nullable String description, @NotNull Object... problemOrigin) {

    }

    @Nullable
    public JComponent createResetAction(@Nullable Configurable configurable) {
        Ref itemRef = Ref.create();
        ContainerUtil.process(this.mySidePanelItems.values(), (item) -> {
            if (item.getConfigurable() == configurable) {
                itemRef.set(item);
                return false;
            } else {
                return true;
            }
        });
        SidePanelItem item = (SidePanelItem)itemRef.get();
        if (item == null) {
            return null;
        } else {
            JPanel resetPanel = new JPanel(new BorderLayout());
            resetPanel.add(item.getResetComponent(), BorderLayout.EAST);
            Dimension resetPreferredSize = resetPanel.getPreferredSize();
            Dimension resetMinimumSize = new Dimension((int)resetPreferredSize.getWidth(), (int)resetPreferredSize.getHeight());
            resetPanel.setMinimumSize(resetMinimumSize);
            resetPanel.setPreferredSize(resetMinimumSize);
            return resetPanel;
        }
    }
    @NotNull
    public Getter getTempTargetOrTarget(@NotNull Object represented) {
        Getter var10000 = () -> {
            AbstractRabbitmqConfigurable var2 = this.getConfigurable(represented, false, AbstractRabbitmqConfigurable.class);
            return var2 == null ? represented : var2.getTempTarget();
        };
        return var10000;
    }

    @Nullable
    public AbstractRabbitmqConfigurable getConfigurable(@Nullable Object var1, boolean var2, @NotNull Class var3) {
        JBIterable<SidePanelItem> var4 = JBIterable.from(this.mySidePanelItems.values());
        Condition var5 = (var1x) -> {
            return var1x == var1 || var1x instanceof RmqElement && ((RmqElement)var1x).getDelegate() == var1;
        };
        SidePanelItem var6 = var4.find((sidePanelItem) -> {
            return var5.value(sidePanelItem.getObject());
        });
        if (var6 != null) {
            if (var6.getConfigurable() == null && var2) {
                var6.createConfigurable();
            }

            return (AbstractRabbitmqConfigurable)ObjectUtils.tryCast(var6.getConfigurable(), var3);
        } else {
            SidePanelItem var7 = var4.find((var3x) -> {
                return !var5.value(var3x.getObject()) && var3.isInstance(var3x.getConfigurable()) && ((AbstractRabbitmqConfigurable)var3x.getConfigurable()).getTempTarget() == var1;
            });
            return var7 == null ? null : (AbstractRabbitmqConfigurable)ObjectUtils.tryCast(var7.getConfigurable(), var3);
        }
    }

    private List getAddActions() {
        ArrayList var1 = ContainerUtil.newArrayList();
        LocalDataSourceManager localDataSourceManager = LocalDataSourceManager.getInstance(ProjectManager.getInstance().getOpenProjects()[0]/*;this.mySettings.facade.getProject()*/);
        Function<ConnectionType, ConnectionType> var3 = (var1x) -> {
            return this.myRemovedObjects.contains(var1x) ? null : (ConnectionType)this.getTempTargetOrTarget(var1x).get();
        };
        JBIterable<ConnectionType> var5 = JBIterable.from(ConnectionTypeManager.getInstance().getConnectionTypes()).filterMap(var3);
        var1.addAll(localDataSourceManager.getCreateDataSourceActions(var5.toList(), (var2x) -> {
        }));
        return var1;
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

    // com.intellij.database.view.ui.DatabaseConfigEditorImpl$MyComponentConfigurator
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

    //////////\\\\\\\\\\

    enum Kind {
        CONNECTION_DETAILS,
        CONNECTION_TYPE,
        OTHER;

        private Kind() {
        }

        static Kind kindOf(Object target) {
            return target instanceof ConnectionType ? CONNECTION_TYPE : OTHER;
        }
    }

    //////////\\\\\\\\\\

    class AddAction extends ActionGroup implements DumbAware, AlwaysVisibleActionGroup {
        private AddAction() {
            super("Add", null, IconUtil.getAddIcon());
        }

        public boolean isPopup() {
            return true;
        }

        public boolean canBePerformed(@NotNull DataContext context) {
            return true;
        }

        public void actionPerformed(@NotNull AnActionEvent e) {
            ListPopup var2 = JBPopupFactory.getInstance().createActionGroupPopup("", this, e.getDataContext(), JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false, (Runnable)null, 20);
            RmqUiUtil.showPopup(var2, (Editor)null, e);
        }

        @NotNull
        @Override
        public AnAction[] getChildren(@Nullable AnActionEvent e) {
            List var2 = RabbitmqConfigEditorImpl.this.getAddActions();
            return (AnAction[])var2.toArray(AnAction.EMPTY_ARRAY);
        }
    }

}
