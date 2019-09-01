package com.github.users.wileespaghetti.rabbitmq.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.ui.InplaceButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;

// com.intellij.database.util.DbUIUtil
public class RmqUiUtil {
    private RmqUiUtil() {
    }

    public static void showPopup(@NotNull JBPopup popup, @Nullable Editor editor, @Nullable AnActionEvent action) {
        InputEvent inputEvent = action == null ? null : action.getInputEvent();
        Object inputEventSource = inputEvent == null ? null : inputEvent.getSource();
        if (editor != null && editor.getComponent().isShowing()) {
            popup.showInBestPositionFor(editor);
        } else if (action != null) {
            if (!(inputEventSource instanceof InplaceButton) && !(inputEventSource instanceof ActionButton)) {
                popup.showInBestPositionFor(action.getDataContext());
            } else {
                popup.setMinimumSize(((JComponent)inputEventSource).getSize());
                popup.showUnderneathOf((Component)inputEventSource);
            }
        }

    }
}
