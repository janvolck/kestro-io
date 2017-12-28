package be.kestro.io.core.api;


/**
 * Service definition to monitor GPIO pins.
 */
public interface InputService extends IOService {

    /**
     * Returns true when the state of the input is high.
     *
     * @return true when the input is high; otherwise false.
     */
    boolean isHigh();

    /**
     * Returns true when the state of the input is low.
     *
     * @return true when the input is low; otherwise false.
     */
    boolean isLow();

    /**
     * Adds a new InputEventListener that will be called when the state of the InputService changes.
     *
     * @param listener the InputEventListener that needs to be called.
     */
    void addInputEventListener(InputEventListener listener);

    /**
     * Removes the InputEventListener from the InputService.
     * Any subsequent state changes of the InputService will no longer call the listener.
     *
     * @param listener the InputEventListener to be removed.
     */
    void removeInputEventListener(InputEventListener listener);
}
