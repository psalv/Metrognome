package Events;


import java.util.EventListener;


public interface DeleteListener extends EventListener{
    void deleteEventOccurred(DeleteEvent event);
}
