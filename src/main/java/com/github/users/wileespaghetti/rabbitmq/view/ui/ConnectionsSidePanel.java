package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.SeparatorWithText;
import com.intellij.ui.components.GradientViewport;
import com.intellij.ui.components.JBList;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// com.intellij.database.view.ui.DataSourceSidePanel
public class ConnectionsSidePanel {
    private final JPanel myPanel = new JPanel(new BorderLayout());
    private final JScrollPane myScroll;
    private final JList myList;
    private final DefaultListModel myModel = new DefaultListModel();
    private final List<SeparatorWithText> mySeparators = ContainerUtil.newArrayList();

    public ConnectionsSidePanel() {
        this.myList = new JBList(this.myModel);
        this.myScroll = ScrollPaneFactory.createScrollPane(null, true);

        this.myScroll.setViewport(new GradientViewport(new JLabel("Gradient Content"), JBUI.insetsTop(5), true));
        this.myScroll.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, null);
        this.myScroll.setCorner(ScrollPaneConstants.LOWER_LEFT_CORNER, null);
        this.myScroll.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, null);
        this.myScroll.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, null);

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
        this.mySeparators.clear();
    }
}
