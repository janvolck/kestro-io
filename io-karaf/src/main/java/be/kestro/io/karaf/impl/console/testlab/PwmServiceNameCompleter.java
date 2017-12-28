package be.kestro.io.karaf.impl.console.testlab;

import be.kestro.io.core.api.PwmService;
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
public class PwmServiceNameCompleter implements Completer {

	@Reference
    BundleContext bundleContext;
		
	@Override
	public int complete(Session session, CommandLine commandLine, List<String> candidates) {
		
		StringsCompleter delegate = new StringsCompleter();
		
		Collection<ServiceReference<PwmService>> serviceReferences;
		try {
			serviceReferences = bundleContext.getServiceReferences(PwmService.class, null);
			serviceReferences.forEach((reference) -> {
				PwmService service = bundleContext.getService(reference);
				delegate.getStrings().add(service.name());
			});
	        
		} catch (InvalidSyntaxException e) {
			System.out.println("Failed to complete");
		}		
		return delegate.complete(session, commandLine, candidates);
	}

}
