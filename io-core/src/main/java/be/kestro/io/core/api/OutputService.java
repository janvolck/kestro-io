package be.kestro.io.core.api;


import java.util.concurrent.Future;

/**
 * Service definition to control GPIO pins.
 */
public interface OutputService extends IOService {

    /**
     * Toggles the current state of the output.
     * The toggle function will return as soon as possible to the original state.
     */
    void toggle();

    /**
     * Changes the current state of the output for a certain amount of time before returning to the original state.
     *
     * @param duration the duration of the pulse expressed in milli-seconds.
     */
    Future<?> pulse(long duration);

    /**
     * Updates the state of the output for a certain amount of time every given interval.
     *
     * @param duration the duration of the state needs to be changed expressed in milli-seconds.
     * @param interval the duration after which the state needs to be changed again expressed in milli-seconds.
     */
    Future<?> blink(long duration, long interval);

    /**
     * Sets the output to the given state.
     *
     * @param high true to set the state of the output on, otherwise the state is off.
     */
    void setState(boolean high);

    /**
     * Sets the state of the output to on.
     */
    void setHigh();

    /**
     * Sets the state of the output off.
     */
    void setLow();

    /**
     * Returns true when the state of the output is on.
     *
     * @return true when the output is open; otherwise false.
     */
    boolean isHigh();

    /**
     * Returns true when the state of the output is closed.
     *
     * @return true when the output is closed; otherwise false.
     */
    boolean isLow();
}
