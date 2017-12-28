package be.kestro.io.karaf.impl.console.testlab;

import java.util.Collection;

import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.support.table.ShellTable;

import be.kestro.io.core.api.IOService;


@Command(scope = "testlab", name = "list", description = "list all avaiable GPIO services that can be toggled")
@Service
public class ListCommand extends TestlabSupportAction<IOService> {

    public ListCommand() {
        super(IOService.class);
    }

    @Override
    protected Object testlabExecute(Collection<IOService> services) throws Exception {
        System.out.println("Registered IOServices");

        ShellTable shellTable = new ShellTable();
        shellTable.column("name");
        shellTable.column("class");

        services.forEach((service) -> {
            shellTable.addRow().addContent(service.name(), service.getClass().getName());
        });

        shellTable.print(System.out);
        return null;
    }

}
