package tr.org.liderahenk.resourceusage.handlers;

import org.eclipse.swt.widgets.Display;

import tr.org.liderahenk.liderconsole.core.handlers.SingleSelectionHandler;
import tr.org.liderahenk.resourceusage.dialogs.ResourceUsageTaskDialog;

public class ResourceUsageTaskHandler extends SingleSelectionHandler {

	@Override
	public void executeWithDn(String dn) {
		ResourceUsageTaskDialog dialog = new ResourceUsageTaskDialog(Display.getDefault().getActiveShell(), dn, false);
		dialog.create();
		dialog.openWithEventBroker();
	}
}
