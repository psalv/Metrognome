package Events;

import java.util.EventListener;


public interface PanelListener extends EventListener{
    void panelEventOccurred(PanelEvent event);
}
