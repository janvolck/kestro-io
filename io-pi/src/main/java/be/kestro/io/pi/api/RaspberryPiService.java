package be.kestro.io.pi.api;

import com.pi4j.io.gpio.GpioController;

/**
 * Service that will instantiate a connection to the Pi4J library 
 * and expose the hardware controllers available on the current
 * Raspberry Pi.
 */
public interface RaspberryPiService {

	/**
	 * The GpioController that is linked to the current Raspberry Pi.
	 * 
	 * @return the GpioController of the Raspberry Pi.
	 */
	GpioController gpio();
	
	
}
