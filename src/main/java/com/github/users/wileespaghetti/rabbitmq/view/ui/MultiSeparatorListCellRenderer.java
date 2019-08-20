package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.intellij.ui.AbstractExpandableItemsHandler;
import com.intellij.ui.CellRendererPanel;
import com.intellij.ui.ExpandableItemsHandler;
import com.intellij.ui.SeparatorWithText;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

// com.intellij.database.view.ui.MultiSeparatorListCellRenderer
public abstract class MultiSeparatorListCellRenderer extends CellRendererPanel implements ListCellRenderer {
    private final List<SeparatorWithText> mySeparators = ContainerUtil.newArrayList();
    private final Container myValidationParent = new CellRendererPane();

    public MultiSeparatorListCellRenderer(JList list) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        list.add(this.myValidationParent);
    }

    protected abstract JComponent getItemCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus);

    protected abstract Collection getSeparatorsAbove(int index);

    protected abstract void configureSeparator(@NotNull SeparatorWithText separator, @NotNull Object value);

    // ListCellRenderer
    @Override
    public Component getListCellRendererComponent(JList  list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Collection separatorsAbove = this.getSeparatorsAbove(index);
        JComponent rendererComponent = this.getItemCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        this.rebuildChildren(separatorsAbove, rendererComponent);
        this.setPreferredSize(null);
        this.validate();
        boolean isExpanded = UIUtil.isClientPropertyTrue(list, ExpandableItemsHandler.EXPANDED_RENDERER);
        UIUtil.putClientProperty(rendererComponent, ExpandableItemsHandler.USE_RENDERER_BOUNDS, isExpanded ? true : null);
        if (isExpanded) {
            Rectangle cellBounds = list.getCellBounds(index, index);
            cellBounds.setSize(this.getPreferredSize().width, cellBounds.height);
            AbstractExpandableItemsHandler.setRelativeBounds(this, cellBounds, rendererComponent, this.myValidationParent);
            return rendererComponent;
        } else {
            return this;
        }
    }

    @NotNull
    protected SeparatorWithText createSeparator() {
        SeparatorWithText separator = new SeparatorWithText();
        separator.setCaptionCentered(false);

        return separator;
    }

    private void rebuildChildren(@NotNull Collection<SeparatorWithText> separatorsAbove, @NotNull JComponent rendererComponent) {
        int separatorCount = separatorsAbove.size();

        while(this.mySeparators.size() < separatorCount) {
            this.mySeparators.add(this.createSeparator());
        }

        int separatorsProcessed = 0;

        Iterator separators;
        for(separators = separatorsAbove.iterator(); separators.hasNext(); ++separatorsProcessed) {
            Object separator = separators.next();
            this.configureSeparator(this.mySeparators.get(separatorsProcessed), separator);
        }

        if (!separatorsAbove.isEmpty() || this.getComponentCount() != 1 || this.getComponent(0) != rendererComponent) {
            this.removeAll();
            separators = this.mySeparators.subList(0, separatorCount).iterator();

            while(separators.hasNext()) {
                SeparatorWithText nextSeparator = (SeparatorWithText) separators.next();
                this.add(nextSeparator);
            }

            this.add(rendererComponent);
        }
    }
}
