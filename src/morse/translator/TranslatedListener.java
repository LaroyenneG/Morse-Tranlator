package morse.translator;

import java.util.EventListener;

public interface TranslatedListener extends EventListener {

    void translated(TranslatedEvent event);
}
