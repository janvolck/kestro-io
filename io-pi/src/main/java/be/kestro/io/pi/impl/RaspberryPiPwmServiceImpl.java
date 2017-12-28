package be.kestro.io.pi.impl;

import java.util.concurrent.atomic.AtomicReference;

import be.kestro.io.core.api.IOService;
import org.osgi.service.component.ComponentException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import be.kestro.io.core.api.PwmService;
import be.kestro.io.pi.api.RaspberryPiService;

@ObjectClassDefinition(name = "Raspberry Pi PWM Pin Configuration", description = "Configures a new Raspberry Pi PWM Service with to control a PWM pin. Please verify your pin id at http://www.pi4j.com/")
@interface PiPwmConfig {

	@AttributeDefinition(name = "GPIO Pin", required = true, description = "The GPIO Pin id as described at http://www.pi4j.com")
	int pinId() default 0;

	@AttributeDefinition(name = "Logical name", required = true, description = "A logical name like Gate Controller or Green LED that makes it easier to identify the purpose of the pin.")
	String name() default "";
}

@Component(
		configurationPolicy = ConfigurationPolicy.REQUIRE,
		service = {PwmService.class, IOService.class}
)
@Designate(ocd = PiPwmConfig.class, factory = true)
public class RaspberryPiPwmServiceImpl implements PwmService, IOService {

	private AtomicReference<RaspberryPiService> pi = new AtomicReference<>();
	private GpioPinPwmOutput pwmPin;
	private String name;

	@Activate
	public void activate(PiPwmConfig config) {

		Pin pin = RaspiPin.getPinByAddress(config.pinId());

		RaspberryPiService raspberryPiService = pi.get();
		if (raspberryPiService == null) {
			throw new ComponentException("RaspberryPiService not set");
		}
		
		pwmPin = raspberryPiService.gpio().provisionPwmOutputPin(pin, config.name());
		name = config.name();
	}

	@Deactivate
	public void deactivate() {

		RaspberryPiService raspberryPiService = pi.get();
		if (raspberryPiService == null) {
			return;
		}

		raspberryPiService.gpio().unprovisionPin(pwmPin);
		pwmPin = null;
	}

	@Reference(policy = ReferencePolicy.STATIC, cardinality = ReferenceCardinality.MANDATORY, unbind = "unsetRaspberryPiService")
	public void setRaspberryPiService(RaspberryPiService service) {
		pi.compareAndSet(null, service);
	}

	public void unsetRaspberryPiService(RaspberryPiService service) {
		pi.compareAndSet(service, null);
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public void setPulsePercentage(int percentage) {
		pwmPin.setPwm(percentage);
	}

	@Override
	public int getPulsePercentage() {
		return pwmPin.getPwm();
	}
	
	@Override
	public void increase() {
		int pwm = pwmPin.getPwm();
		pwmPin.setPwm(pwm + 5);
	}
	
	@Override
	public void decrease() {
		int pwm = pwmPin.getPwm();
		pwmPin.setPwm(pwm - 5);		
	}
}
