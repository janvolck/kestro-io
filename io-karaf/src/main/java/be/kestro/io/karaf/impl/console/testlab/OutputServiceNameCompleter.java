package be.kestro.io.karaf.impl.console.testlab;

import java.util.Collection;
import java.util.List;

import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.console.CommandLine;
import org.apache.karaf.shell.api.console.Completer;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.support.completers.StringsCompleter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import be.kestro.io.core.api.OutputService;

@Service
public class OutputServiceNameCompleter implements Completer {

	@Reference
    BundleContext bundleContext;
		
	@Override
	public int complete(Session session, CommandLine commandLine, List<String> candidates) {
		
		StringsCompleter delegate = new StringsCompleter();
		
		Collection<ServiceReference<OutputService>> serviceReferences;
		try {
			serviceReferences = bundleContext.getServiceReferences(OutputService.class, null);
			serviceReferences.forEach((reference) -> {
				OutputService service = bundleContext.getService(reference);
				delegate.getStrings().add(service.name());
			});
	        
		} catch (InvalidSyntaxException e) {
			System.out.println("Failed to complete");
		}		
		return delegate.complete(session, commandLine, candidates);
	}

}
