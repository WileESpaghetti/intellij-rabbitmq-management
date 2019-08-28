package com.github.users.wileespaghetti.rabbitmq.connectionType;

import com.github.users.wileespaghetti.rabbitmq.view.ui.RabbitmqConfigEditor;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ExpandableEditorSupport;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.fields.ExtendableTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
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

    public RabbitmqNameComponent(@NotNull AbstractRabbitmqConfigurable configurable, @NotNull RabbitmqConfigEditor controller, boolean isColorable) {
        this.myConfigurable = configurable;
        this.myController = controller;
        this.$$$setupUI$$$();
        this.myName.getDocument().addDocumentListener(new DocumentAdapter() {
            protected void textChanged(@NotNull DocumentEvent e) {
                String name = getNameValue();
                if (!myController.isObjectNameUnique(myConfigurable.getTarget(), name)) {
                    myController.showErrorNotification(myConfigurable, NON_UNIQUE_NAME, "Name is Not Unique", String.format("Object with the name '%s' already exists. Please choose another name.", name));
                } else {
                    myController.showErrorNotification(myConfigurable, NON_UNIQUE_NAME, null);
                }

            }
        });

        this.myPanel.setBorder(new EmptyBorder(UIUtil.PANEL_REGULAR_INSETS));
        JComponent resetAction = this.myController.createResetAction(this.myConfigurable);
        if (resetAction != null) {
            this.myResetPanel.add(resetAction);
        }

        new ExpandableEditorSupport(this.myComment);
        this.myComment.setBackground(UIUtil.getPanelBackground());
        this.myComment.setForeground(UIUtil.getActiveTextColor());
    }

    @NotNull
    public JComponent getComponent() {
        return this.myPanel;
    }

    public void reset(@NotNull ConnectionTypeImpl target, @Nullable Computable computable) {
        this.reset(target.getName(), target.getComment(), computable);
    }

    private void reset(String name, String comment, @Nullable Computable computable) {
        String targetName = !StringUtil.isEmptyOrSpaces(name) ? name : (computable != null ? (String)computable.compute() : null);
        this.setNameValue(targetName);
        this.setCommentValue(comment);
    }

    private String getCommentValue() {
        return StringUtil.nullize(this.myComment.getText(), true);
    }

    @NotNull
    public String getNameValue() {
        System.out.println("name value: " + this.myName.getText());
        return this.myName.getText().trim();
    }

    public void setGeneratedName(String name) {
        if (!Comparing.equal(name, this.myName.getText())) {
            this.myName.setText(name);
        }
    }

    public JComponent getPreferredFocusedComponent() {
        return this.myName;
    }

    public void setNameValue(@Nullable String name) {
        this.myName.setText(StringUtil.notNullize(name));
    }

    private void setCommentValue(@Nullable String comment) {
        String filteredComment = StringUtil.notNullize(comment);
        this.myComment.setText(filteredComment);
        this.myComment.setToolTipText(StringUtil.shortenTextWithEllipsis(filteredComment, 2000, 0, true));
    }

    AbstractRabbitmqConfigurable getConfigurable() {
        return this.myConfigurable;
    }

    ExtendableTextField getNameField() {
        return this.myName;
    }

    private void $$$setupUI$$$() {
        JPanel contentPane = new JPanel();
        this.myPanel = contentPane;
        contentPane.setLayout(new GridLayoutManager(3, 5, new Insets(0, 0, 0, 0), -1, -1, false, false));
        JBLabel nameLabel = new JBLabel();
        nameLabel.setHorizontalTextPosition(SwingConstants.TRAILING);
        nameLabel.setText("Name:");
        contentPane.add(nameLabel, new GridConstraints(0, 0, 1, 1, 0, 1, 0, 0, null, null, (Dimension)null));
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
