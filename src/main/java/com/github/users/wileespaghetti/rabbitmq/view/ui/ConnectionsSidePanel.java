package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.intellij.openapi.roots.ui.configuration.SidePanelCountLabel;
import com.intellij.openapi.roots.ui.configuration.SidePanelSeparator;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.CellRendererPanel;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.SeparatorWithText;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.components.GradientViewport;
import com.intellij.ui.components.JBList;
import com.intellij.ui.navigation.Place;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.EmptyIcon;
import com.intellij.util.ui.JBEmptyBorder;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

// com.intellij.database.view.ui.DataSourceSidePanel
public class ConnectionsSidePanel {
    private final List<Pair <String, Integer>> mySeparators = ContainerUtil.newArrayList();
    private final JPanel myPanel = new JPanel(new BorderLayout());
    private final MyGroupedItemsListRenderer myRenderer;
    private final JScrollPane myScroll;
    private final JList myList;
    private final DefaultListModel myModel = new DefaultListModel();

    public ConnectionsSidePanel(ComponentConfigurator configurator) {
        this.myList = new JBList(this.myModel);
        this.myScroll = ScrollPaneFactory.createScrollPane(null, true);

        this.myScroll.setViewport(new GradientViewport(myList, JBUI.insetsTop(5), true));
        this.myScroll.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, null);
        this.myScroll.setCorner(ScrollPaneConstants.LOWER_LEFT_CORNER, null);
        this.myScroll.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, null);
        this.myScroll.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, null);
        this.myList.setBorder(JBUI.Borders.emptyTop(5));
        this.myRenderer = new MyGroupedItemsListRenderer(this.myList, configurator);
        this.myList.setCellRenderer(this.myRenderer);

        this.myPanel.add(myScroll, BorderLayout.CENTER);

        this.myList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        UIUtil.setBackgroundRecursively(this.myPanel, UIUtil.SIDE_PANEL_BACKGROUND);
    }

    public JComponent getComponent() {
        return this.myPanel;
    }

    public JList getList() {
        return this.myList;
    }

    public void clear() {
        this.myModel.clear();
    }

    public void addPlace(Place itemPlace) {
        this.myModel.addElement(itemPlace);
        this.myPanel.revalidate();
        this.myPanel.repaint();
    }

    @NotNull
    public SeparatorWithText doCreateSeparator() {
        SeparatorWithText separator = new SidePanelSeparator(){
            public Dimension getMaximumSize() {
                Dimension maxSize = super.getMaximumSize();
                int scrollWidth = (int)ConnectionsSidePanel.this.myScroll.getViewport().getExtentSize().getWidth();
                return new Dimension(scrollWidth, (int)maxSize.getHeight());
            }
        };
        separator.setBorder(new JBEmptyBorder(0, 0, 12, 0));
        separator.setAlignmentX(0.0F);

        return separator;
    }

    //////////\\\\\\\\\\

    // com.intellij.database.view.ui.DataSourceSidePanel$MyGroupedItemsListRenderer
    public class MyGroupedItemsListRenderer extends MultiSeparatorListCellRenderer {
        private final SidePanelCountLabel myCountLabel;
        private final ComponentConfigurator myConfigurator;
        private final JLabel myIconComponent;
        private final CellRendererPanel myItemComponent;
        private final SimpleColoredComponent myTextComponent;
        private final Component mySpacer;
        private final Icon myEmptyIcon;

        MyGroupedItemsListRenderer(@NotNull JList<Place> list, ComponentConfigurator configurator) {
            super(list);
            this.myCountLabel = new SidePanelCountLabel();
            this.myIconComponent = new JLabel();
            this.myItemComponent = new CellRendererPanel();
            this.myTextComponent = new SimpleColoredComponent();
            this.mySpacer = Box.createHorizontalGlue();
            this.myEmptyIcon = JBUI.scale(EmptyIcon.create(16, 16));
            this.myConfigurator = configurator;
            this.myIconComponent.setBorder(JBUI.Borders.empty(0, 15, 0, 3));
            this.myItemComponent.setLayout(new BoxLayout(this.myItemComponent, BoxLayout.X_AXIS));
            this.myItemComponent.add(this.myIconComponent);
            this.myItemComponent.add(this.myTextComponent);
            this.myItemComponent.add(this.myCountLabel);
            this.myItemComponent.add(this.mySpacer);
            this.myItemComponent.setAlignmentX(0.0F);
            this.myTextComponent.setBorder(JBUI.Borders.empty(2));
        }

        @Override
        @NotNull
        public SeparatorWithText createSeparator() {
            return ConnectionsSidePanel.this.doCreateSeparator();
        }

        // merge any consecutive "--" or "---"
        java.util.List merge(boolean isFirst, java.util.List captions) {
            if (captions.size() > 1) {
                Object lastCaption = captions.get(captions.size() - 1);
                if (!lastCaption.equals("--")) {
                    Object nextToLastCaption = captions.get(captions.size() - 2);
                    return !isFirst && nextToLastCaption.equals("--") && !lastCaption.equals("---") ? Arrays.asList("--", this.canonize(lastCaption)) : Collections.singletonList(this.canonize(lastCaption));
                } else {
                    for(int captionIndex = captions.size() - 2; captionIndex >= 0; --captionIndex) {
                        Object currentCaption = captions.get(captionIndex);
                        if (!currentCaption.equals("--")) {
                            return Collections.singletonList(this.canonize(currentCaption));
                        }
                    }

                    return Collections.singletonList(this.canonize(lastCaption));
                }
            } else {
                return captions;
            }
        }

        @Override
        protected Collection getSeparatorsAbove(int index) {
            ArrayList separatorCaptions = ContainerUtil.newArrayList();
            Iterator<Pair<String, Integer>> sidePanelSeparators = ConnectionsSidePanel.this.mySeparators.iterator();

            while(sidePanelSeparators.hasNext()) {
                Pair<String, Integer> var4 = sidePanelSeparators.next();
                if (var4.getSecond() >= index && var4.getSecond() < index + 1) {
                    separatorCaptions.add(var4.getFirst()); // key name for connectiontype?
                }
            }

            List separatorsAbove = this.merge(index == 0, separatorCaptions);
            if (!separatorsAbove.isEmpty() && separatorsAbove.get(separatorsAbove.size() - 1).equals("--") && index == 0) {
                return Collections.emptyList();
            } else {
                return separatorsAbove;
            }
        }

        private Object canonize(Object separator) {
            return separator.equals("---") ? "--" : separator;
        }

        @Override
        protected void configureSeparator(@NotNull SeparatorWithText separator, @NotNull Object value) {
            separator.setCaption(String.valueOf(this.canonize(value)));
        }

        @Override
        protected JComponent getItemCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            return this.getItemCellRendererComponent(list, (Place)value, index, isSelected, cellHasFocus);
        } // FIXME probably a generics thing <T> = <Place>

//        @Override
        protected JComponent getItemCellRendererComponent(JList list, Place place, int index, boolean isSelected, boolean cellHasFocus) {
            Color background = isSelected ? UIUtil.getListSelectionBackground() : UIUtil.SIDE_PANEL_BACKGROUND;
            Color foreground = isSelected ? UIUtil.getListSelectionForeground() : UIUtil.getLabelForeground();
            this.myCountLabel.setVisible(false);
            this.myTextComponent.clear();
            this.myTextComponent.setBackground(background);
            this.myTextComponent.setForeground(foreground);
            this.myConfigurator.configure(this.myTextComponent, place, isSelected);
            this.myIconComponent.setIcon(ObjectUtils.chooseNotNull(this.myTextComponent.getIcon(), this.myEmptyIcon));
            this.myTextComponent.setIcon(null);
            this.myTextComponent.setMaximumSize(null);
            this.myTextComponent.setMaximumSize(this.myTextComponent.getPreferredSize());
            this.myCountLabel.setSelected(isSelected);
            this.myItemComponent.setSelected(isSelected);
            this.myItemComponent.setBackground(background);
            return this.myItemComponent;
        }
    }

    public void select(Place place) {
        this.myList.setSelectedValue(place, true);
        int selectedIndex = this.myList.getSelectedIndex();
        if (selectedIndex >= 0) {
            int visibleRowCount = this.myList.getVisibleRowCount();
            this.myList.scrollRectToVisible(this.myList.getCellBounds(Math.max(0, selectedIndex - visibleRowCount / 2), selectedIndex));
        }
    }


    //////////\\\\\\\\\\

    // com.intellij.database.view.ui.DataSourceSidePanel$ComponentConfigurator
    public interface ComponentConfigurator {
        void configure(@NotNull SimpleColoredComponent component, @NotNull Place itemPlace, boolean isSelected);
    }

}
