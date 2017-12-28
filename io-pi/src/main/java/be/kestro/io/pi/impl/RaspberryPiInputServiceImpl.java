package be.kestro.io.pi.impl;

import be.kestro.io.core.api.IOService;
import be.kestro.io.core.api.InputEvent;
import be.kestro.io.core.api.InputEventListener;
import be.kestro.io.core.api.InputService;
import be.kestro.io.pi.api.RaspberryPiService;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.osgi.service.component.ComponentException;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

@ObjectClassDefinition(name = "Raspberry Pi Input Pin Configuration", description = "Configures a new Raspberry Pi Input Service to the configured GPIO Pin. Please verify your pin id at http://www.pi4j.com/")
@interface PiInputConfig {

    @AttributeDefinition(name = "GPIO Pin", description = "The GPIO Pin id as described at http://www.pi4j.com")
    int pinId() default 0;

    @AttributeDefinition(name = "Logical name", description = "A logical name like Gate Controller or Doorbell that makes it easier to identify the purpose of the pin.")
    String name() default "";
}

@Component(
        configurationPolicy = ConfigurationPolicy.REQUIRE,
        service = {InputService.class, IOService.class}
)
@Designate(ocd = PiInputConfig.class, factory = true)
public class RaspberryPiInputServiceImpl implements InputService, IOService, GpioPinListenerDigital {

    private AtomicReference<RaspberryPiService> pi = new AtomicReference<>();
    private CopyOnWriteArrayList<InputEventListener> listeners = new CopyOnWriteArrayList<>();
    private GpioPinDigitalInput inputPin;
    private String name;

    @Activate
    public void activate(PiInputConfig config) {

        Pin pin = RaspiPin.getPinByAddress(config.pinId());

        RaspberryPiService raspberryPiService = pi.get();
        if (raspberryPiService == null) {
            throw new ComponentException("RaspberryPiService not set");
        }

        inputPin = raspberryPiService.gpio().provisionDigitalInputPin(pin, config.name());
        raspberryPiService.gpio().addListener(this, inputPin);
        name = config.name();
    }

    @Deactivate
    public void deactivate() {

        RaspberryPiService raspberryPiService = pi.get();
        if (raspberryPiService == null) {
            return;
        }

        raspberryPiService.gpio().removeListener(this, inputPin);
        raspberryPiService.gpio().unprovisionPin(inputPin);
        inputPin = null;
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
    public boolean isHigh() {
        return inputPin.isHigh();
    }

    @Override
    public boolean isLow() {
        return inputPin.isLow();
    }

    @Override
    public void addInputEventListener(InputEventListener listener) {
        listeners.addIfAbsent(listener);
    }

    @Override
    public void removeInputEventListener(InputEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

        InputEvent e = new InputEvent(event.getPin().getName(), event.getState() == PinState.HIGH);
        listeners.forEach((l) -> l.handleInputEventChanged(this, e));
    }
}
