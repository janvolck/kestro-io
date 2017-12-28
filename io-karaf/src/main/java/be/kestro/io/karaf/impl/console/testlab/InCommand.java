package be.kestro.io.karaf.impl.console.testlab;

import java.util.Collection;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.osgi.framework.BundleContext;

import be.kestro.io.core.api.InputService;


@Command(scope = "testlab", name = "in", description = "Controls an InputService")
@Service
public class InCommand extends TestlabSupportAction<InputService> {

    @Reference
    BundleContext bundleContext;

    @Argument(index = 0, name = "name", description = "The unique name of the input service", required = true)
    @Completion(InputServiceNameCompleter.class)
    String name = null;

    @Argument(index = 1, name = "command", description = "The command to execute on the InputService", required = true)
    @Completion(InCommandNameCompleter.class)
    String command = null;

    public InCommand() {
        super(InputService.class);
    }

    @Override
    protected Object testlabExecute(Collection<InputService> services) throws Exception {

        InputService service = services.stream()
                .filter(s -> s.name().equals(name))
                .findFirst()
                .orElse(null);


        if (service != null) {

            System.out.printf("InputService: %s command: %s", service.name(), command);
            System.out.println();

            switch (command) {
                case "monitor":
                    boolean isHigh = service.isHigh();
                    while(true){
                        if (isHigh != service.isHigh()){

                            System.out.printf("InputService %s changed from %s to %s",
                                    service.name(),
                                    isHigh ? "high" : "low",
                                    service.isHigh() ? "high" : "low");
                            System.out.println();

                            isHigh = service.isHigh();
                            Thread.yield();
                        }
                    }
            }
        }
        return null;
    }

}
