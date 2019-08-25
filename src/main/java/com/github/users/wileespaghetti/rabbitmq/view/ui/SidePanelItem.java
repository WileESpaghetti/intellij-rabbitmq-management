package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.github.users.wileespaghetti.rabbitmq.connectionType.AbstractRabbitmqConfigurable;
import com.github.users.wileespaghetti.rabbitmq.connectionType.ConnectionType;
import com.github.users.wileespaghetti.rabbitmq.connectionType.ConnectionTypeImpl;
import com.intellij.openapi.options.Configurable;
import com.intellij.ui.navigation.Place;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

// com.intellij.database.view.ui.SidePanelItem
abstract class SidePanelItem<MyConfigurable extends Configurable> {
    private static final String ELEMENT = "element";
    private final Object myRepresented;
    private Place myPlace;
    private MyConfigurable myConfigurable;
    private JComponent myComponent;
    protected RabbitmqConfigEditorImpl myEditor;

    SidePanelItem(@NotNull RabbitmqConfigEditorImpl editor, @NotNull Object represented) {
        super();
        this.myEditor = editor;
        this.myRepresented = represented;
    }

    @Nullable
    public static Object getObject(@Nullable SidePanelItem sidePanelItem) {
        return sidePanelItem == null ? null : sidePanelItem.getObject();
    }

    @NotNull
    public Place createPlace() {
        if (this.myPlace == null) {
            this.myPlace = createPlaceFor(this);
        }

        return this.myPlace;
    }

    private static Place createPlaceFor(@NotNull SidePanelItem item) {
        return setItem(new Place(), item);
    }

    @NotNull
    public static Place setItem(@NotNull Place itemPlace, @Nullable SidePanelItem item) {
        return itemPlace.putPath(ELEMENT, item);
    }

    @Nullable
    public static SidePanelItem getItem(@Nullable Place itemPlace) {
        return itemPlace == null ? null : (SidePanelItem)itemPlace.getPath(ELEMENT);
    }

    @Nullable
    public Place getPlace() {
        return this.myPlace;
    }

    @NotNull
    public Object getObject() {
        return this.myRepresented;
    }

    @Nullable
    public Configurable getConfigurable() {
        return this.myConfigurable;
    }

    @Nullable
    public JComponent getComponent() {
        return this.myComponent;
    }

    @NotNull
    public Configurable createConfigurable() {
        if (this.myConfigurable == null) {
            this.myConfigurable = this.createConfigurableImpl();
            this.myComponent = this.myConfigurable.createComponent();
            if (this.myConfigurable instanceof AbstractRabbitmqConfigurable) {
                ((AbstractRabbitmqConfigurable)this.myConfigurable).init();
            } else {
                this.myConfigurable.reset();
            }
        }

        return this.myConfigurable;
    }

    @NotNull
    public abstract MyConfigurable createConfigurableImpl();

    public abstract String getName();

    //////////\\\\\\\\\\

    // com.intellij.database.view.ui.SidePanelItem$DriverItem
    static class ConnectionTypeItem extends SidePanelItem<ConnectionTypeConfigurable> {
        ConnectionTypeItem(@NotNull RabbitmqConfigEditorImpl editor, @NotNull ConnectionType represented) {
            super(editor, represented);
        }

        @NotNull
        @Override
        public ConnectionTypeConfigurable createConfigurableImpl() {
            return new ConnectionTypeConfigurable((ConnectionTypeImpl) this.getObject(), this.myEditor);
        }

        @Override
        public String getName() {
            return ((ConnectionType)this.getObject()).getName();
        }
    }
}
