package tr.org.liderahenk.resourceusage.tabs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import tr.org.liderahenk.liderconsole.core.exceptions.ValidationException;
import tr.org.liderahenk.liderconsole.core.ldap.enums.DNType;
import tr.org.liderahenk.liderconsole.core.rest.requests.TaskRequest;
import tr.org.liderahenk.liderconsole.core.rest.utils.TaskRestUtils;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;
import tr.org.liderahenk.resourceusage.constants.ResourceUsageConstants;
import tr.org.liderahenk.resourceusage.i18n.Messages;
import tr.org.liderahenk.resourceusage.model.ResourceUsageTableItem;

public class DataListTab implements IUsageTab {

	private String pluginVersion;
	private String pluginName;
	private Set<String> dnSet;
	public String getPluginVersion() {
		return pluginVersion;
	}

	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public Set<String> getDnSet() {
		return dnSet;
	}

	public void setDnSet(Set<String> dnSet) {
		this.dnSet = dnSet;
	}

	private Label lblmemCpuUsageMonitoring;
	private Label lblDataCollectionInterval;
	private Text txtDataCollectionInterval;
	private Label lblRules;
	private Label lblRules1;
	private Label lblRules2;
	private Label lblRules3;
	private Label lblRules4;
	private Label lblRules5;
	private Label lblRules6;
	private Label lblRules7;
	private Label lblRules8;
	private Label lblRules9;
	private Label lblRules10;
	private Text txtRules1;
	private Text txtRules2;
	private Text txtRules3;
	private Text txtRules4;
	private Text txtRules5;
	private Text txtRules6;
	private Combo cmb1;
	private Combo cmb2;
	private Combo cmb3;
	private Combo cmb4;
	private Label lblMailAddress;
	private Text txtMailAddress;
	private Button btnFloatingAverage;
	private Button btnFixedAverage;
	private Label lblMemoryUsagePattern;
	private Text txtMemoryUsagePattern;
	private Label lblCpuUsagePattern;
	private Text txtCpuUsagePattern;
	private Label lblWindowLength;
	private Text txtWindowLength;
	private TableViewer tableViewer;
	
	private final String[] sendMailArray = new String[] { "SEND_MAIL", "SEND_CRITICAL_MAIL" };
	
	public void createInputs(Composite tabComposite) throws Exception {
		
		Composite group = new Composite(tabComposite, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(gridData);
		
		Composite  memoryCpuUsageMonitoring = new Composite(group, SWT.BORDER);
		GridData memCpuGd = new GridData(SWT.FILL, SWT.FILL, true, true);
		memCpuGd.widthHint = 900;
		memoryCpuUsageMonitoring.setLayoutData(memCpuGd);
		memoryCpuUsageMonitoring.setLayout(new GridLayout(1, false));
		
		FormData fd = new FormData();
		fd.width = 50;
		
		lblmemCpuUsageMonitoring = new Label(memoryCpuUsageMonitoring, SWT.NONE);
		lblmemCpuUsageMonitoring.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
		lblmemCpuUsageMonitoring.setText(Messages.getString("MEM_CPU_USAGE_MONITORING"));

		
		Composite  dataCollectionIntervalComposite = new Composite(memoryCpuUsageMonitoring, SWT.NONE);
		dataCollectionIntervalComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		dataCollectionIntervalComposite.setLayout(new GridLayout(2, false));
		
		lblDataCollectionInterval = new Label(dataCollectionIntervalComposite, SWT.NONE);
		lblDataCollectionInterval.setText(Messages.getString("DATA_COLLECTION_INTERVAL"));

		txtDataCollectionInterval = new Text(dataCollectionIntervalComposite, SWT.BORDER);
		txtDataCollectionInterval.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Composite  rulesComposite = new Composite(memoryCpuUsageMonitoring, SWT.BORDER);
		rulesComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		rulesComposite.setLayout(new GridLayout(1, false));
		
		lblRules = new Label(rulesComposite, SWT.NONE);
		lblRules.setText(Messages.getString("RULES"));
		
		Composite  rules1Composite = new Composite(rulesComposite, SWT.NONE);
		rules1Composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		rules1Composite.setLayout(new GridLayout(4, false));
		
		lblRules1 = new Label(rules1Composite, SWT.RIGHT);
		lblRules1.setText(Messages.getString("RULES1"));

		txtRules1 = new Text(rules1Composite, SWT.BORDER);
		txtRules1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		lblRules2 = new Label(rules1Composite, SWT.LEFT);
		lblRules2.setText(Messages.getString("RULES2"));

		GridData cmbGridData = new GridData(SWT.END, SWT.FILL, true, false);
		cmbGridData.widthHint = 190;
		cmb1 = new Combo(rules1Composite, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		cmb1.setLayoutData(cmbGridData);
		for (int i = 0; i < sendMailArray.length; i++) {
			String i18n = Messages.getString(sendMailArray[i]);
			if (i18n != null && !i18n.isEmpty()) {
				cmb1.add(i18n);
				cmb1.setData(i + "", sendMailArray[i]);
			}
		}
		cmb1.select(0);
		
		Composite  rules2Composite = new Composite(rulesComposite, SWT.NONE);
		rules2Composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		rules2Composite.setLayout(new GridLayout(6, false));
		
		lblRules3 = new Label(rules2Composite, SWT.RIGHT);
		lblRules3.setText(Messages.getString("RULES3"));

		txtRules2 = new Text(rules2Composite, SWT.BORDER);
		txtRules2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		lblRules4 = new Label(rules2Composite, SWT.NONE);
		lblRules4.setText(Messages.getString("RULES4"));

		txtRules3 = new Text(rules2Composite, SWT.BORDER);
		txtRules3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		lblRules5 = new Label(rules2Composite, SWT.LEFT);
		lblRules5.setText(Messages.getString("RULES5"));

		cmb2 = new Combo(rules2Composite, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		cmb2.setLayoutData(cmbGridData);
		for (int i = 0; i < sendMailArray.length; i++) {
			String i18n = Messages.getString(sendMailArray[i]);
			if (i18n != null && !i18n.isEmpty()) {
				cmb2.add(i18n);
				cmb2.setData(i + "", sendMailArray[i]);
			}
		}
		cmb2.select(0);
		
		Composite  rules3Composite = new Composite(rulesComposite, SWT.NONE);
		rules3Composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		rules3Composite.setLayout(new GridLayout(4, false));
		
		lblRules6 = new Label(rules3Composite, SWT.RIGHT);
		lblRules6.setText(Messages.getString("RULES6"));

		txtRules4 = new Text(rules3Composite, SWT.BORDER);
		txtRules4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		lblRules7 = new Label(rules3Composite, SWT.LEFT);
		lblRules7.setText(Messages.getString("RULES7"));

		cmb3 = new Combo(rules3Composite, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		cmb3.setLayoutData(cmbGridData);
		for (int i = 0; i < sendMailArray.length; i++) {
			String i18n = Messages.getString(sendMailArray[i]);
			if (i18n != null && !i18n.isEmpty()) {
				cmb3.add(i18n);
				cmb3.setData(i + "", sendMailArray[i]);
			}
		}
		cmb3.select(0);
		
		Composite  rules4Composite = new Composite(rulesComposite, SWT.NONE);
		rules4Composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		rules4Composite.setLayout(new GridLayout(6, false));
		
		lblRules8 = new Label(rules4Composite, SWT.RIGHT);
		lblRules8.setText(Messages.getString("RULES8"));

		txtRules5 = new Text(rules4Composite, SWT.BORDER);
		txtRules5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		lblRules9 = new Label(rules4Composite, SWT.NONE);
		lblRules9.setText(Messages.getString("RULES9"));

		txtRules6 = new Text(rules4Composite, SWT.BORDER);
		txtRules6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		lblRules10 = new Label(rules4Composite, SWT.LEFT);
		lblRules10.setText(Messages.getString("RULES10"));
		cmb4 = new Combo(rules4Composite, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		cmb4.setLayoutData(cmbGridData);
		for (int i = 0; i < sendMailArray.length; i++) {
			String i18n = Messages.getString(sendMailArray[i]);
			if (i18n != null && !i18n.isEmpty()) {
				cmb4.add(i18n);
				cmb4.setData(i + "", sendMailArray[i]);
			}
		}
		cmb4.select(0);
		
		Composite  actionInfoComposite = new Composite(memoryCpuUsageMonitoring, SWT.BORDER);
		actionInfoComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		actionInfoComposite.setLayout(new GridLayout(2, false));
		
		lblRules1 = new Label(actionInfoComposite, SWT.NONE);
		lblRules1.setText(Messages.getString("ACTION_INFORMATION"));
		
		lblRules1 = new Label(actionInfoComposite, SWT.NONE);
		
		lblMailAddress = new Label(actionInfoComposite, SWT.NONE);
		lblMailAddress.setText(Messages.getString("MAIL_ADDRESS"));

		txtMailAddress = new Text(actionInfoComposite, SWT.BORDER);
		txtMailAddress.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Composite  optionsComposite = new Composite(memoryCpuUsageMonitoring, SWT.NONE);
		optionsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		optionsComposite.setLayout(new GridLayout(2, false));

		btnFloatingAverage = new Button(optionsComposite, SWT.NONE);
		btnFloatingAverage.setText(Messages.getString("FLOATING_AVERAGE"));
		GridData btnGridData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
		btnGridData.widthHint = 150;
		btnFloatingAverage.setLayoutData(btnGridData);
		btnFloatingAverage.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validateBeforeSave();
				Map<String, Object> parameters = getParameterMap(ResourceUsageConstants.START_TIMER);
				executeTask(parameters);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		btnFixedAverage = new Button(optionsComposite, SWT.NONE);
		btnFixedAverage.setText(Messages.getString("FIXED_AVERAGE"));
		btnFixedAverage.setLayoutData(btnGridData);
		btnFixedAverage.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Map<String, Object> parameters = getParameterMap(ResourceUsageConstants.STOP_TIMER);
				executeTask(parameters);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		
		
		Composite informationObjects = new Composite(group, SWT.BORDER);
		memoryCpuUsageMonitoring.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		informationObjects.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		informationObjects.setLayout(new GridLayout(1, false));

		
		lblmemCpuUsageMonitoring = new Label(informationObjects, SWT.NONE);
		lblmemCpuUsageMonitoring.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
		lblmemCpuUsageMonitoring.setText(Messages.getString("INFORMATION_OBJECTS"));
		
		Composite  informationObjectsComposite = new Composite(informationObjects, SWT.NONE);
		informationObjectsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		informationObjectsComposite.setLayout(new GridLayout(2, false));
		
		lblMemoryUsagePattern = new Label(informationObjectsComposite, SWT.NONE);
		lblMemoryUsagePattern.setText(Messages.getString("MEMORY_USAGE_PATTERN"));

		txtMemoryUsagePattern = new Text(informationObjectsComposite, SWT.BORDER);
		txtMemoryUsagePattern.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		txtMemoryUsagePattern.setEnabled(false);
		
		lblCpuUsagePattern = new Label(informationObjectsComposite, SWT.NONE);
		lblCpuUsagePattern.setText(Messages.getString("CPU_USAGE_PATTERN"));

		txtCpuUsagePattern = new Text(informationObjectsComposite, SWT.BORDER);
		txtCpuUsagePattern.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		txtCpuUsagePattern.setEnabled(false);
		
		lblWindowLength = new Label(informationObjectsComposite, SWT.NONE);
		lblWindowLength.setText(Messages.getString("WINDOW_LENGTH"));

		txtWindowLength = new Text(informationObjectsComposite, SWT.BORDER);
		txtWindowLength.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		createTable(informationObjectsComposite);

		((ScrolledComposite) tabComposite).setContent(group);
		group.setSize(group.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		((ScrolledComposite) tabComposite).setExpandVertical(true);
		((ScrolledComposite) tabComposite).setExpandHorizontal(true);
		((ScrolledComposite) tabComposite).setMinSize(group.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}

	private void createTable(final Composite parent) {
		tableViewer = new TableViewer(parent,
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		// Create table columns
		createTableColumns();

		// Configure table layout
		final Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.getVerticalBar().setEnabled(true);
		table.getVerticalBar().setVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.heightHint = 140;
		gridData.horizontalAlignment = GridData.FILL;
		tableViewer.getControl().setLayoutData(gridData);
	}

	private void createTableColumns() {

		String[] titles = { Messages.getString("RECORD_DATE"), Messages.getString("MEM_USED"), Messages.getString("CPU_USED") };
		int[] bounds = { 140, 140, 140};

		TableViewerColumn recordDateColumn = createTableViewerColumn(titles[0], bounds[0]);
		recordDateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ResourceUsageTableItem) {
					return ((ResourceUsageTableItem) element).getRecordDate();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn memUsedColumn = createTableViewerColumn(titles[1], bounds[1]);
		memUsedColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ResourceUsageTableItem) {
					return ((ResourceUsageTableItem) element).getMemUsed();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn cpuUsedColumn = createTableViewerColumn(titles[2], bounds[2]);
		cpuUsedColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ResourceUsageTableItem) {
					return ((ResourceUsageTableItem) element).getCpuUsed();
				}
				return Messages.getString("UNTITLED");
			}
		});
	}


	private TableViewerColumn createTableViewerColumn(String title, int bound) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(false);
		column.setAlignment(SWT.LEFT);
		return viewerColumn;
	}
	
	
	@Override
	public void validateBeforeSave() throws ValidationException {
		if(txtDataCollectionInterval == null || txtDataCollectionInterval.getText().isEmpty())
			throw new ValidationException(Messages.getString("FILL_ALL_FIELDS"));
		if(txtRules1 == null || txtRules1.getText().isEmpty())
			throw new ValidationException(Messages.getString("FILL_ALL_FIELDS"));
		if(txtRules2 == null || txtRules2.getText().isEmpty())
			throw new ValidationException(Messages.getString("FILL_ALL_FIELDS"));
		if(txtRules3 == null || txtRules3.getText().isEmpty())
			throw new ValidationException(Messages.getString("FILL_ALL_FIELDS"));
		if(txtRules4 == null || txtRules4.getText().isEmpty())
			throw new ValidationException(Messages.getString("FILL_ALL_FIELDS"));
		if(txtRules5 == null || txtRules5.getText().isEmpty())
			throw new ValidationException(Messages.getString("FILL_ALL_FIELDS"));
		if(txtRules6 == null || txtRules6.getText().isEmpty())
			throw new ValidationException(Messages.getString("FILL_ALL_FIELDS"));
		if(txtMailAddress == null || txtMailAddress.getText().isEmpty())
			throw new ValidationException(Messages.getString("FILL_ALL_FIELDS"));
	}
	

	private void executeTask(Map<String, Object> parameters) {
		try {
			TaskRequest task = new TaskRequest(new ArrayList<String>(getDnSet()), DNType.AHENK, getPluginName(),
					getPluginVersion(), "RESOURCE_INFO_ALERT", parameters, null, new Date());
			TaskRestUtils.execute(task);
		} catch (Exception e1) {
			Notifier.error(null, Messages.getString("ERROR_ON_EXECUTE"));
		}
	}

	private Map<String, Object> getParameterMap(String averageTypeSelection) {
		Map<String, Object> taskData = new HashMap<String, Object>();
		try {
			if(averageTypeSelection.equals(ResourceUsageConstants.START_TIMER)){
				taskData.put(ResourceUsageConstants.DATA_LIST_PARAMETERS.DATA_COLLECTION_INTERVAL, txtDataCollectionInterval.getText());
				taskData.put(ResourceUsageConstants.DATA_LIST_PARAMETERS.MEMORY_USAGE_PERCENTAGE, txtRules1.getText());
				taskData.put(ResourceUsageConstants.DATA_LIST_PARAMETERS.MEMORY_ALERT_COUNT, txtRules2.getText());
				taskData.put(ResourceUsageConstants.DATA_LIST_PARAMETERS.MEMORY_ALERT_TIME_PERIOD, txtRules3.getText());
				taskData.put(ResourceUsageConstants.DATA_LIST_PARAMETERS.CPU_USAGE_PERCENTAGE, txtRules4.getText());
				taskData.put(ResourceUsageConstants.DATA_LIST_PARAMETERS.CPU_ALERT_COUNT, txtRules5.getText());
				taskData.put(ResourceUsageConstants.DATA_LIST_PARAMETERS.CPU_ALERT_TIME_PERIOD, txtRules6.getText());
				taskData.put(ResourceUsageConstants.DATA_LIST_PARAMETERS.MEMORY_USAGE_DECISION, cmb1.getItem(cmb1.getSelectionIndex()));
				taskData.put(ResourceUsageConstants.DATA_LIST_PARAMETERS.MEMORY_ALERT_ANALYSIS_DECISION, cmb2.getItem(cmb2.getSelectionIndex()));
				taskData.put(ResourceUsageConstants.DATA_LIST_PARAMETERS.CPU_USAGE_DECISION, cmb3.getItem(cmb3.getSelectionIndex()));
				taskData.put(ResourceUsageConstants.DATA_LIST_PARAMETERS.CPU_ALERT_ANALYSIS_DECISION, cmb4.getItem(cmb4.getSelectionIndex()));
				taskData.put(ResourceUsageConstants.DATA_LIST_PARAMETERS.MAIL_ADDRESS, txtMailAddress.getText());
			}
			taskData.put(ResourceUsageConstants.DATA_LIST_PARAMETERS.AVERAGE_TYPE_SELECTION, averageTypeSelection);
		} catch (Exception e1) {
			Notifier.error(null, Messages.getString("ERROR_ON_EXECUTE"));
		}
		return taskData;
	}

	@Override
	public void createTab(Composite tabComposite, Set<String> dnSet, String pluginName, String pluginVersion)
			throws Exception {
		setDnSet(dnSet);
		setPluginName(pluginName);
		setPluginVersion(pluginVersion);
		createInputs(tabComposite);
	}
}
