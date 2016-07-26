package tr.org.liderahenk.resourceusage.dialogs;

import java.util.Map;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.dialogs.DefaultTaskDialog;
import tr.org.liderahenk.liderconsole.core.exceptions.ValidationException;
import tr.org.liderahenk.resourceusage.constants.ResourceUsageConstants;
import tr.org.liderahenk.resourceusage.i18n.Messages;
import tr.org.liderahenk.resourceusage.tabs.AlarmListTab;
import tr.org.liderahenk.resourceusage.tabs.DataListTab;

/**
 * Task execution dialog for resource-usage plugin.
 * 
 */
public class ResourceUsageAlertTaskDialog extends DefaultTaskDialog{
	
	private static final Logger logger = LoggerFactory.getLogger(ResourceUsageAlertTaskDialog.class);


	private DataListTab dataList;
	private AlarmListTab alarmList;
	
	public ResourceUsageAlertTaskDialog(Shell parentShell, String dnString) {
		super(parentShell, dnString);
		this.dataList = new DataListTab();
		this.alarmList = new AlarmListTab();

	}
	
	@Override
	public String createTitle() {
		return Messages.getString("COMPLEX_EVENT_PROCESSING");
	}
	
	@Override
	public Control createTaskDialogArea(Composite parent) {
		try {

			Composite composite = new Composite(parent, SWT.NONE);
			GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
			gridData.widthHint = 800;
			gridData.heightHint = 1000;
			composite.setLayoutData(gridData);
			composite.setLayout(new GridLayout(1, false));
			CTabFolder tabFolder = createTabFolder(composite);
			tabFolder.setSize(600,600);
			dataList.createInputs(createInputTab(tabFolder, Messages.getString("DATA_LIST"), true));

			alarmList.createInputs(createInputTab(tabFolder, Messages.getString("ALERT_LIST"), true));
			
			tabFolder.setSelection(0);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	private CTabFolder createTabFolder(final Composite composite) {
		CTabFolder tabFolder = new CTabFolder(composite, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setSelectionBackground(
				Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		return tabFolder;
	}
	
	private Composite createInputTab(CTabFolder tabFolder, String label, boolean isScrolledComposite) {
		CTabItem tab = new CTabItem(tabFolder, SWT.NONE);
		tab.setText(label);
		Composite composite = isScrolledComposite ? new ScrolledComposite(tabFolder, SWT.V_SCROLL)
				: new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		tab.setControl(composite);
		return composite;
	}
	@Override
	public void validateBeforeExecution() throws ValidationException {
	}
	@Override
	public Map<String, Object> getParameterMap() {
		return null;
	}
	@Override
	public String getCommandId() {
		return null;
	}
	@Override
	public String getPluginName() {
		return ResourceUsageConstants.PLUGIN_NAME;
	}
	@Override
	public String getPluginVersion() {
		return ResourceUsageConstants.PLUGIN_VERSION;
	}
//
//	@Override
	protected void createButtonsForButtonBar(Composite parent) {

	}
}
