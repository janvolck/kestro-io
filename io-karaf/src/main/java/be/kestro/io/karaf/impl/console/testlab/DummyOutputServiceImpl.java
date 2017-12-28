package be.kestro.io.karaf.impl.console.testlab;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import be.kestro.io.core.api.OutputService;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.util.concurrent.Future;

@ObjectClassDefinition(name = "Dummy Output Service Configuration", description = "Configures a new Dummy Output Service")
@interface DummyConfig {

	@AttributeDefinition(name = "Logical name", description = "A logical name like Gate Controller or Green LED that makes it easier to identify the purpose of the pin.")
	String name() default "";
}

@Component(configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd=DummyConfig.class, factory = true)
public class DummyOutputServiceImpl implements OutputService {

	private boolean state;
	private String name;

	@Activate
	public void activate(DummyConfig config){
	    name = config.name();
    }

	@Override
	public String name() {
		return name;
	}

	@Override
	public void toggle() {
		state = !state;
		System.out.printf("DummyOutput is %s", state ? "on" : "off");
		System.out.println();
		state = !state;
	}

    @Override
    public Future<?> pulse(long duration) {
        return null;
    }

    @Override
    public Future<?> blink(long duration, long interval) {
        return null;
    }

    @Override
	public void setState(boolean on) {
		state = on;
		System.out.printf("DummyOutput is %s", state ? "on" : "off");
		System.out.println();
	}

	@Override
	public void setHigh() {
		state = true;
		System.out.printf("DummyOutput is on");
		System.out.println();
	}

	@Override
	public void setLow() {
		state = false;
		System.out.printf("DummyOutput is off");
		System.out.println();
	}

	@Override
	public boolean isHigh() {
		return state;
	}

	@Override
	public boolean isLow() {
		return !state;
	}
}
