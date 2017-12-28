package be.kestro.io.karaf.impl.console.testlab;

import be.kestro.io.core.api.OutputService;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.osgi.framework.BundleContext;

import java.util.Collection;


@Command(scope = "testlab", name = "out", description = "Controls an OutputService")
@Service
public class OutCommand extends TestlabSupportAction<OutputService> {

    @Reference
    BundleContext bundleContext;

    @Argument(index = 0, name = "name", description = "The unique name of the output service", required = true, multiValued = false)
    @Completion(OutputServiceNameCompleter.class)
    String name = null;

    @Argument(index = 1, name = "command", description = "The command to execute on the OutputService", required = true)
    @Completion(OutCommandNameCompleter.class)
    String command = null;

    public OutCommand() {
        super(OutputService.class);
    }

    @Override
    protected Object testlabExecute(Collection<OutputService> services) throws Exception {

        OutputService service = services.stream()
                .filter(s -> s.name().equals(name))
                .findFirst()
                .orElse(null);

        if (service != null) {

            switch (command) {
                case "high":
                    service.setHigh();
                    break;

                case "low":
                    service.setLow();
                    break;

                case "toggle":
                    service.toggle();
                    break;
            }

            System.out.printf("OutputService: %s command: %s", service.name(), command);
            System.out.println();
        }
        return null;
    }

}
