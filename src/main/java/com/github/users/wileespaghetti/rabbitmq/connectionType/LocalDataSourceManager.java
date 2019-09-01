package com.github.users.wileespaghetti.rabbitmq.connectionType;

import com.github.users.wileespaghetti.rabbitmq.psi.DefaultRmqPsiManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Conditions;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Consumer;
import com.intellij.util.Function;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.JBIterable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

// com.intellij.database.dataSource.LocalDataSourceManager
public class LocalDataSourceManager extends DefaultRmqPsiManager {
    @NotNull
    public static LocalDataSourceManager getInstance(@NotNull Project project) {
        return ObjectUtils.notNull(ContainerUtil.findInstance(EP_NAME.getExtensions(project), LocalDataSourceManager.class));
    }
    @NotNull
    public AnAction getCreateDataSourceAction(@NotNull Consumer consumer) {
        List dataSourceActions = this.getCreateDataSourceActions(ConnectionTypeManager.getInstance().getConnectionTypes(), consumer);
        DefaultActionGroup actionGroup = new DefaultActionGroup("Data Source", true);
        actionGroup.addAll(dataSourceActions);

        return actionGroup;
    }

    @NotNull
    public List getCreateDataSourceActions(@NotNull Collection<ConnectionType> connectionTypes, @NotNull Consumer consumer) {
        List var3 = ContainerUtil.emptyList();
        Function<ConnectionType, String> getId = (connectionType) -> {
            return connectionType.isPredefined() ? StringUtil.notNullize(StringUtil.substringBefore(connectionType.getId(), "."), connectionType.getId()) : connectionType.getId();
        };
        Function<ConnectionType, String> getName = (connectionType) -> {
            return connectionType.getName();
        };
        JBIterable<ConnectionType> uniqueConnectionTypes = JBIterable.from(connectionTypes).unique(getId).collect();
        Condition<? super ConnectionType> isPredefined = ConnectionType::isPredefined;
        Condition<ConnectionType> hasConnectionType = (var2x) -> {
            return var3.contains(getId.fun(var2x));
        };
        getName.getClass();
        Comparator<ConnectionType> nameMatches = Comparator.comparing(getName::fun);
        Comparator byName = Comparator.comparingInt((var2x) -> {
            return var3.indexOf(getId.fun((ConnectionType) var2x));
        }).thenComparing((var1x) -> {
            return getName.fun((ConnectionType) var1x);
        });
        Function<ConnectionType, DumbAwareAction> createDataSource = (var4x) -> {
            return new DumbAwareAction(getName.fun(var4x), null, var4x.getIcon(0)) {
                @Override
                public void actionPerformed(@NotNull AnActionEvent e) {
                    consumer.consume(var4x.createDataSource(null));
                }
            };
        };
        List var10000 = uniqueConnectionTypes.filter(hasConnectionType).sort(byName).map(createDataSource).append(Separator.getInstance()).append(uniqueConnectionTypes.filter(isPredefined).filter(Conditions.not(hasConnectionType)).sort(nameMatches).map(createDataSource)).append(Separator.getInstance()).append(uniqueConnectionTypes.filter(Conditions.not(isPredefined)).filter(Conditions.not(hasConnectionType)).sort(nameMatches).map(createDataSource)).toList();
        return var10000;
    }
}
