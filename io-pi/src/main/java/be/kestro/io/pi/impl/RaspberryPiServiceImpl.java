package be.kestro.io.pi.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

import be.kestro.io.pi.api.RaspberryPiService;

@Component
public class RaspberryPiServiceImpl implements RaspberryPiService {

	private GpioController gpioController;

	@Activate
	public void activate() {
		gpioController = GpioFactory.getInstance();
		
	}
	
	@Deactivate
	public void deactivate() {
		if(gpioController!=null) {
			gpioController.shutdown();
		}
	}

	@Override
	public GpioController gpio() {
		return gpioController;
	}

}
