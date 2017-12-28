package be.kestro.io.karaf.impl.console.testlab;

import be.kestro.io.core.api.InputService;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.console.CommandLine;
import org.apache.karaf.shell.api.console.Completer;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.support.completers.StringsCompleter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import java.util.Collection;
import java.util.List;

@Service
public class InputServiceNameCompleter implements Completer {

	@Reference
    BundleContext bundleContext;
		
	@Override
	public int complete(Session session, CommandLine commandLine, List<String> candidates) {
		
		StringsCompleter delegate = new StringsCompleter();
		
		Collection<ServiceReference<InputService>> serviceReferences;
		try {
			serviceReferences = bundleContext.getServiceReferences(InputService.class, null);
			serviceReferences.forEach((reference) -> {
				InputService service = bundleContext.getService(reference);
				delegate.getStrings().add(service.name());
			});
	        
		} catch (InvalidSyntaxException e) {
			System.out.println("Failed to complete");
		}		
		return delegate.complete(session, commandLine, candidates);
	}

}
