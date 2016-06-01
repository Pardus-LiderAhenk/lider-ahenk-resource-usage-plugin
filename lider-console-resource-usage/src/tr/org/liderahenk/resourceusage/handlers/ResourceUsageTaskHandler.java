package tr.org.liderahenk.resourceusage.handlers;

import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.handlers.SingleSelectionHandler;
import tr.org.liderahenk.resourceusage.dialogs.ResourceUsageTaskDialog;

//TODO use MultipleSelectionHandler if this task support multiple LDAP entries/DNs otherwise use SingleSelectionHandler.
public class ResourceUsageTaskHandler extends SingleSelectionHandler {
	
	private Logger logger = LoggerFactory.getLogger(ResourceUsageTaskHandler.class);
	
	@Override
	public void executeWithDn(String dn) {
		// TODO dnSet contains distinguished names (DN) of the selected LDAP entries.
		ResourceUsageTaskDialog dialog = new ResourceUsageTaskDialog(Display.getDefault().getActiveShell(), dn);
		dialog.create();
		dialog.open();
	}
	
}
