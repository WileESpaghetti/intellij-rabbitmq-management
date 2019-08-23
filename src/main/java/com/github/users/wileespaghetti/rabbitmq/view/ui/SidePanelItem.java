package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.github.users.wileespaghetti.rabbitmq.connectionType.ConnectionType;
import com.intellij.ui.navigation.Place;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// com.intellij.database.view.ui.SidePanelItem
abstract class SidePanelItem {
    private static final String ELEMENT = "element";
    private final Object myRepresented;
    private Place myPlace;

    SidePanelItem(@NotNull Object represented) {
        super();
        this.myRepresented = represented;
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

    @NotNull
    public Object getObject() {
        return this.myRepresented;
    }

    public abstract String getName();

    //////////\\\\\\\\\\

    // com.intellij.database.view.ui.SidePanelItem$DriverItem
    static class ConnectionTypeItem extends SidePanelItem {
        ConnectionTypeItem(@NotNull Object represented) {
            super(represented);
        }

        @Override
        public String getName() {
            return ((ConnectionType)this.getObject()).getName();
        }
    }
}
