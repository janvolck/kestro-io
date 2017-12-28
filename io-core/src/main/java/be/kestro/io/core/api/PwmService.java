package be.kestro.io.core.api;


/**
 * Service definition to control PWM pins. (Pulse-Width-Modulation)
 * 
 */
public interface PwmService extends IOService {

	/**
	 * Sets the percentage by which pulses will be sent.
	 * 
	 * @param percentage value from 0 to 100 to control the PWM.
	 */
	void setPulsePercentage(int percentage);
	
	/**
	 * Gets the last set PWM percentage.
	 * 
	 * @return the PWM percentage.
	 */
	int getPulsePercentage();
	
	/**
	 * Increases the current PWM percentage.
	 */
	void increase();
	
	/**
	 * Decreases the current PWM percentage.
	 */
	void decrease();
}
