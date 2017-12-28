package be.kestro.io.pi.impl;

import java.util.concurrent.Future;
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

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import be.kestro.io.core.api.OutputService;
import be.kestro.io.pi.api.RaspberryPiService;

@ObjectClassDefinition(name = "Raspberry Pi Output Pin Configuration", description = "Configures a new Raspberry Pi Output Service with to toggle the configured GPIO Pin. Please verify your pin id at http://www.pi4j.com/")
@interface PiOutputConfig {

    @AttributeDefinition(name = "GPIO Pin", description = "The GPIO Pin id as described at http://www.pi4j.com")
    int pinId() default 0;

    @AttributeDefinition(name = "Logical name", description = "A logical name like Gate Controller or Green LED that makes it easier to identify the purpose of the pin.")
    String name() default "";

    @AttributeDefinition(name = "Signal low", description = "Set to false when signal low means turning off the output, true when signal high means turning off the output.")
    boolean signalLow() default false;
}

@Component(
        configurationPolicy = ConfigurationPolicy.REQUIRE,
        service = {OutputService.class, IOService.class}

)
@Designate(ocd = PiOutputConfig.class, factory = true)
public class RaspberryPiOutputServiceImpl implements OutputService, IOService {

    private AtomicReference<RaspberryPiService> pi = new AtomicReference<>();
    private GpioPinDigitalOutput outputPin;
    private String name;

    @Activate
    public void activate(PiOutputConfig config) {

        Pin pin = RaspiPin.getPinByAddress(config.pinId());
        PinState pinState = config.signalLow() ? PinState.LOW : PinState.HIGH;

        RaspberryPiService raspberryPiService = pi.get();
        if (raspberryPiService == null) {
            throw new ComponentException("RaspberryPiService not set");
        }

        outputPin = raspberryPiService.gpio().provisionDigitalOutputPin(pin, config.name(), pinState);
        name = config.name();
    }

    @Deactivate
    public void deactivate() {

        RaspberryPiService raspberryPiService = pi.get();
        if (raspberryPiService == null) {
            return;
        }

        raspberryPiService.gpio().unprovisionPin(outputPin);
        outputPin = null;
    }

    @Reference(policy = ReferencePolicy.STATIC, cardinality = ReferenceCardinality.MANDATORY, unbind = "unsetRaspberryPiService")
    public void setRaspberryPiService(RaspberryPiService service) {
        pi.compareAndSet(null, service);
    }

    @SuppressWarnings("unused") // referenced via reflection through setRaspberryPiService.
    public void unsetRaspberryPiService(RaspberryPiService service) {
        pi.compareAndSet(service, null);
    }


    @Override
    public String name() {
        return name;
    }

    @Override
    public void toggle() {
        outputPin.toggle();
    }

    @Override
    public Future<?> pulse(long duration) {
        return outputPin.pulse(duration);
    }

    @Override
    public Future<?> blink(long duration, long interval) {
        return outputPin.blink(interval, duration);
    }

    @Override
    public void setState(boolean on) {
        if (on) {
            outputPin.high();
        } else {
            outputPin.low();
        }
    }

    @Override
    public void setHigh() {
        outputPin.high();
    }

    @Override
    public void setLow() {
        outputPin.low();
    }

    @Override
    public boolean isHigh() {
        return outputPin.isHigh();
    }

    @Override
    public boolean isLow() {
        return outputPin.isLow();
    }
}
