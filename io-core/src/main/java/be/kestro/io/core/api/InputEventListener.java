package be.kestro.io.core.api;

import java.util.EventListener;

/**
 * An EventListener that must be registered with the InputService in order for the
 * handleInputEventChanged to be called.
 */
public interface InputEventListener extends EventListener {

    /**
     * Event that is called when the state of an InputService to which this
     * EventListener is registered changes.
     *
     * @param source the InputService of which the state has changed.
     * @param event the EventInput information.
     */
    void handleInputEventChanged(InputService source, InputEvent event);
}
