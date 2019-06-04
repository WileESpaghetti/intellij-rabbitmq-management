package icons;

import com.intellij.openapi.util.IconLoader;
import javax.swing.Icon;

public final class RabbitmqIcons {
    public static final Icon ToolWindowRabbitmq = load("/icons/ToolWindowRabbitmq.svg");

    public RabbitmqIcons() {
    }

    private static Icon load(String path) {
        return IconLoader.getIcon(path, RabbitmqIcons.class);
    }

    private static Icon load(String path, Class clazz) {
        return IconLoader.getIcon(path, clazz);
    }
}

