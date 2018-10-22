package morse.translator;

import java.util.EventObject;

public class TranslatedEvent extends EventObject {

    public TranslatedEvent(Object source) {
        super(source);
    }
}
