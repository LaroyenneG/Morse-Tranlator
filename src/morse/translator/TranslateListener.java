package morse.translator;

import java.util.EventListener;

public interface TranslateListener extends EventListener {

    void translate(TranslateEvent event);
}
