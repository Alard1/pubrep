package gui.panel.buttons;

import java.util.Collection;

public interface Observer {

    public default void update() {}

    public default void update(String string) {}

    public default void update(Collection<String> strings) {}
}
