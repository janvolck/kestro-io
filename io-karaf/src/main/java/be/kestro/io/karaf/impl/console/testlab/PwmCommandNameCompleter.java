package be.kestro.io.karaf.impl.console.testlab;

import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.console.CommandLine;
import org.apache.karaf.shell.api.console.Completer;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.support.completers.StringsCompleter;
import org.osgi.framework.BundleContext;

import java.util.Arrays;
import java.util.List;

@Service
public class PwmCommandNameCompleter implements Completer {

    @Reference
    BundleContext bundleContext;

    @Override
    public int complete(Session session, CommandLine commandLine, List<String> candidates) {

        StringsCompleter delegate = new StringsCompleter();
        delegate.getStrings().addAll(Arrays.asList("increase", "decrease", "set"));

        return delegate.complete(session, commandLine, candidates);
    }

}
