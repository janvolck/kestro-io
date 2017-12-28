package be.kestro.io.karaf.impl.console.testlab;

import java.util.Collection;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import be.kestro.io.core.api.PwmService;


@Command(scope = "testlab", name = "pwm", description = "Update the pwm value of a PwmService")
@Service
public class PwmCommand extends TestlabSupportAction<PwmService> {

    @Argument(index = 0, name = "name", description = "The unique name of the output service", required = true)
    @Completion(PwmServiceNameCompleter.class)
    String name = null;

    @Argument(index = 1, name = "command", description = "The command to execute on the PwmService", required = true)
    @Completion(PwmCommandNameCompleter.class)
    String command = null;

    @Argument(index = 2, name = "value", description = "The pwm value to set on the PwmService")
    int value;

    public PwmCommand() {
        super(PwmService.class);
    }

    @Override
    protected Object testlabExecute(Collection<PwmService> services) throws Exception {

        PwmService service = services.stream()
                .filter(s -> s.name().equals(name))
                .findFirst()
                .orElse(null);

        if (service != null) {

            switch (command){
                case "increase":
                    service.increase();
                    break;

                case "decrease":
                    service.decrease();
                    break;

                case "set":
                    service.setPulsePercentage(value);
                    break;
            }

            System.out.printf("PwmService: %s command: %s value: %d", service.name(), command, value);
            System.out.println();
        }

        return null;
    }
}
