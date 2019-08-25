package com.github.users.wileespaghetti.rabbitmq.connectionType;

import com.github.users.wileespaghetti.rabbitmq.view.ui.RabbitmqConfigEditor;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ExpandableEditorSupport;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.fields.ExtendableTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// com.intellij.database.dataSource.DatabaseNameComponent
public class RabbitmqNameComponent {
    private static final String NON_UNIQUE_NAME = "NON_UNIQUE_NAME";
    private JComponent myPanel;
    private ExtendableTextField myName;
    private JPanel myResetPanel;
    private EditorTextField myComment;
    private final AbstractRabbitmqConfigurable myConfigurable;
    private final RabbitmqConfigEditor myController;

    public RabbitmqNameComponent(@NotNull AbstractRabbitmqConfigurable configurable, @NotNull RabbitmqConfigEditor controller, boolean var3) {
        this.myConfigurable = configurable;
        this.myController = controller;
        this.$$$setupUI$$$();
        this.myPanel.setBorder(new EmptyBorder(UIUtil.PANEL_REGULAR_INSETS));

        new ExpandableEditorSupport(this.myComment);
        this.myComment.setBackground(UIUtil.getPanelBackground());
        this.myComment.setForeground(UIUtil.getActiveTextColor());
    }

    @NotNull
    public JComponent getComponent() {
        return this.myPanel;
    }
    private void $$$setupUI$$$() {
        JPanel contentPane = new JPanel();
        this.myPanel = contentPane;
        contentPane.setLayout(new GridLayoutManager(3, 5, new Insets(0, 0, 0, 0), -1, -1, false, false));
        JBLabel nameLabel = new JBLabel();
        nameLabel.setHorizontalTextPosition(11);
        nameLabel.setText("Name:");
        contentPane.add(nameLabel, new GridConstraints(0, 0, 1, 1, 0, 1, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
        Spacer spacer = new Spacer();
        contentPane.add(spacer, new GridConstraints(2, 0, 1, 1, 0, 2, 1, 6, (Dimension)null, (Dimension)null, (Dimension)null));
        JPanel resetPanel = new JPanel();
        this.myResetPanel = resetPanel;
        resetPanel.setLayout(new BorderLayout(0, 0));
        contentPane.add(resetPanel, new GridConstraints(0, 4, 1, 1, 0, 3, 3, 3, (Dimension)null, (Dimension)null, (Dimension)null));
        ExtendableTextField nameValue = new ExtendableTextField();
        this.myName = nameValue;
        nameValue.setColumns(30);
        contentPane.add(nameValue, new GridConstraints(0, 1, 1, 2, 0, 3, 2, 0, (Dimension)null, (Dimension)null, (Dimension)null));
        JBLabel commentLabel = new JBLabel();
        commentLabel.setText("Comment:");
        contentPane.add(commentLabel, new GridConstraints(1, 0, 1, 1, 0, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null));
        EditorTextField commentValue = new EditorTextField();
        this.myComment = commentValue;
        contentPane.add(commentValue, new GridConstraints(1, 1, 1, 2, 0, 1, 3, 3, (Dimension)null, (Dimension)null, (Dimension)null));
        Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(0, 3, 1, 1, 0, 1, 6, 1, (Dimension)null, (Dimension)null, (Dimension)null));
        nameLabel.setLabelFor(nameValue);
    }
}
