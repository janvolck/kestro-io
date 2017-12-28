package be.kestro.io.karaf.impl.console.testlab;

import be.kestro.io.core.api.IOService;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.ArrayList;
import java.util.Collection;

public abstract class TestlabSupportAction<T extends IOService> implements Action {

    @Reference
    BundleContext bundleContext;
    private Class<T> classType;

    protected TestlabSupportAction(Class<T> classType) {
        this.classType = classType;
    }

    @Override
    public Object execute() throws Exception {

        Collection<ServiceReference<T>> serviceReferences = bundleContext.getServiceReferences(classType, null);
        try {
            ArrayList<T> services = new ArrayList<>();
            serviceReferences.forEach(reference -> services.add(bundleContext.getService(reference)));
            return testlabExecute(services);
        } finally {
            serviceReferences.forEach(reference -> bundleContext.ungetService(reference));
        }
    }


    protected abstract Object testlabExecute(Collection<T> services) throws Exception;
}
