package morse.translator;

import java.util.EventListener;

public interface EndPlayListener extends EventListener {

    void endPlay(EndPlayEvent event);
}
