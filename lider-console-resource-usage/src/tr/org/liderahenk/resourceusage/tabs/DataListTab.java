package tr.org.liderahenk.resourceusage.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import tr.org.liderahenk.liderconsole.core.exceptions.ValidationException;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;
import tr.org.liderahenk.resourceusage.i18n.Messages;

public class DataListTab implements IUsageTab {

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
	
	private final String[] sendMailArray = new String[] { "SEND_MAIL", "SEND_CRITICAL_MAIL" };
	
	@Override
	public void createInputs(Composite tabComposite) throws Exception {
		Composite group = new Composite(tabComposite, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		
		Composite  memoryCpuUsageMonitoring = new Composite(group, SWT.BORDER);
		memoryCpuUsageMonitoring.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		memoryCpuUsageMonitoring.setLayout(new GridLayout(1, false));
		
		lblmemCpuUsageMonitoring = new Label(memoryCpuUsageMonitoring, SWT.NONE);
		lblmemCpuUsageMonitoring.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
		lblmemCpuUsageMonitoring.setText(Messages.getString("MEM_CPU_USAGE_MONITORING"));

		
		Composite  dataCollectionIntervalComposite = new Composite(memoryCpuUsageMonitoring, SWT.BORDER);
		dataCollectionIntervalComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dataCollectionIntervalComposite.setLayout(new GridLayout(2, false));
		
		lblDataCollectionInterval = new Label(dataCollectionIntervalComposite, SWT.NONE);
		lblDataCollectionInterval.setText(Messages.getString("DATA_COLLECTION_INTERVAL"));

		txtDataCollectionInterval = new Text(dataCollectionIntervalComposite, SWT.BORDER);
		txtDataCollectionInterval.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Composite  rulesComposite = new Composite(memoryCpuUsageMonitoring, SWT.BORDER);
		rulesComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		rulesComposite.setLayout(new GridLayout(1, false));
		
		lblRules = new Label(rulesComposite, SWT.NONE);
		lblRules.setText(Messages.getString("RULES"));
		
		Composite  rules1Composite = new Composite(rulesComposite, SWT.NONE);
		rules1Composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		rules1Composite.setLayout(new GridLayout(4, false));
		GridData gridDataText = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridDataText.widthHint = 40;
		
		lblRules1 = new Label(rules1Composite, SWT.RIGHT);
		lblRules1.setText(Messages.getString("RULES1"));
		lblRules1.setBounds(lblRules1.getBounds().x, lblRules1.getBounds().y, 70, lblRules1.getBounds().height);

		txtRules1 = new Text(rules1Composite, SWT.BORDER);
		txtRules1.setLayoutData(gridDataText);
		txtRules1.setBounds(txtRules1.getBounds().x, txtRules1.getBounds().y, 30, txtRules1.getBounds().height);
		
		lblRules2 = new Label(rules1Composite, SWT.LEFT);
		lblRules2.setText(Messages.getString("RULES2"));
		lblRules2.setBounds(lblRules2.getBounds().x, lblRules2.getBounds().y, 70, lblRules2.getBounds().height);

		cmb1 = new Combo(rules1Composite,SWT.RIGHT | SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		cmb1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		for (int i = 0; i < sendMailArray.length; i++) {
			String i18n = Messages.getString(sendMailArray[i]);
			if (i18n != null && !i18n.isEmpty()) {
				cmb1.add(i18n);
				cmb1.setData(i + "", sendMailArray[i]);
			}
		}
		cmb1.select(0);
		cmb1.setBounds(cmb1.getBounds().x, cmb1.getBounds().y, 70, cmb1.getBounds().height);
		
		Composite  rules2Composite = new Composite(rulesComposite, SWT.NONE);
		rules2Composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		rules2Composite.setLayout(new GridLayout(6, false));
		
		lblRules3 = new Label(rules2Composite, SWT.RIGHT);
		lblRules3.setText(Messages.getString("RULES3"));
		lblRules3.setBounds(lblRules3.getBounds().x, lblRules3.getBounds().y, 70, lblRules3.getBounds().height);

		txtRules2 = new Text(rules2Composite, SWT.BORDER);
		txtRules2.setLayoutData(gridDataText);
		txtRules2.setBounds(txtRules2.getBounds().x, txtRules2.getBounds().y, 30, txtRules2.getBounds().height);
		
		lblRules4 = new Label(rules2Composite, SWT.NONE);
		lblRules4.setText(Messages.getString("RULES4"));
		lblRules4.setBounds(lblRules4.getBounds().x, lblRules4.getBounds().y, 30, lblRules4.getBounds().height);

		txtRules3 = new Text(rules2Composite, SWT.BORDER);
		txtRules3.setLayoutData(gridDataText);
		txtRules3.setBounds(txtRules3.getBounds().x, txtRules3.getBounds().y, 30, txtRules3.getBounds().height);
		
		lblRules5 = new Label(rules2Composite, SWT.LEFT);
		lblRules5.setText(Messages.getString("RULES5"));
		lblRules5.setBounds(lblRules5.getBounds().x, lblRules5.getBounds().y, 70, lblRules5.getBounds().height);

		cmb2 = new Combo(rules2Composite,SWT.RIGHT | SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		cmb2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		for (int i = 0; i < sendMailArray.length; i++) {
			String i18n = Messages.getString(sendMailArray[i]);
			if (i18n != null && !i18n.isEmpty()) {
				cmb2.add(i18n);
				cmb2.setData(i + "", sendMailArray[i]);
			}
		}
		cmb2.select(0);
		cmb2.setBounds(cmb2.getBounds().x, cmb2.getBounds().y, 70, cmb2.getBounds().height);
		
		Composite  rules3Composite = new Composite(rulesComposite, SWT.NONE);
		rules3Composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		rules3Composite.setLayout(new GridLayout(4, false));
		
		lblRules6 = new Label(rules3Composite, SWT.RIGHT);
		lblRules6.setText(Messages.getString("RULES6"));
		lblRules6.setBounds(lblRules6.getBounds().x, lblRules6.getBounds().y, 70, lblRules6.getBounds().height);

		txtRules4 = new Text(rules3Composite, SWT.BORDER);
		txtRules4.setLayoutData(gridDataText);
		txtRules4.setBounds(txtRules4.getBounds().x, txtRules4.getBounds().y, 30, txtRules4.getBounds().height);
		
		lblRules7 = new Label(rules3Composite, SWT.LEFT);
		lblRules7.setText(Messages.getString("RULES7"));
		lblRules7.setBounds(lblRules7.getBounds().x, lblRules7.getBounds().y, 70, lblRules7.getBounds().height);

		cmb3 = new Combo(rules3Composite,SWT.RIGHT | SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		cmb3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		for (int i = 0; i < sendMailArray.length; i++) {
			String i18n = Messages.getString(sendMailArray[i]);
			if (i18n != null && !i18n.isEmpty()) {
				cmb3.add(i18n);
				cmb3.setData(i + "", sendMailArray[i]);
			}
		}
		cmb3.select(0);
		cmb3.setBounds(cmb3.getBounds().x, cmb3.getBounds().y, 70, cmb3.getBounds().height);
		
		Composite  rules4Composite = new Composite(rulesComposite, SWT.NONE);
		rules4Composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		rules4Composite.setLayout(new GridLayout(6, false));
		
		lblRules8 = new Label(rules4Composite, SWT.RIGHT);
		lblRules8.setText(Messages.getString("RULES8"));
		lblRules8.setBounds(lblRules8.getBounds().x, lblRules8.getBounds().y, 70, lblRules8.getBounds().height);

		txtRules5 = new Text(rules4Composite, SWT.BORDER);
		txtRules5.setLayoutData(gridDataText);
		txtRules5.setBounds(txtRules5.getBounds().x, txtRules5.getBounds().y, 30, txtRules5.getBounds().height);
		
		lblRules9 = new Label(rules4Composite, SWT.NONE);
		lblRules9.setText(Messages.getString("RULES9"));
		lblRules9.setBounds(lblRules9.getBounds().x, lblRules9.getBounds().y, 30, lblRules9.getBounds().height);

		txtRules6 = new Text(rules4Composite, SWT.BORDER);
		txtRules6.setLayoutData(gridDataText);
		txtRules6.setBounds(txtRules6.getBounds().x, txtRules6.getBounds().y, 30, txtRules6.getBounds().height);
		
		lblRules10 = new Label(rules4Composite, SWT.LEFT);
		lblRules10.setText(Messages.getString("RULES10"));
		lblRules10.setBounds(lblRules10.getBounds().x, lblRules10.getBounds().y, 70, lblRules10.getBounds().height);

		cmb4 = new Combo(rules4Composite, SWT.RIGHT | SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		cmb4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		for (int i = 0; i < sendMailArray.length; i++) {
			String i18n = Messages.getString(sendMailArray[i]);
			if (i18n != null && !i18n.isEmpty()) {
				cmb4.add(i18n);
				cmb4.setData(i + "", sendMailArray[i]);
			}
		}
		cmb4.select(0);
		cmb4.setBounds(cmb4.getBounds().x, cmb4.getBounds().y, 70, cmb4.getBounds().height);
		
		Composite  actionInfoComposite = new Composite(memoryCpuUsageMonitoring, SWT.BORDER);
		actionInfoComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		actionInfoComposite.setLayout(new GridLayout(2, false));
		
		lblRules1 = new Label(actionInfoComposite, SWT.NONE);
		lblRules1.setText(Messages.getString("ACTION_INFORMATION"));
		
		lblRules1 = new Label(actionInfoComposite, SWT.NONE);
		
		lblMailAddress = new Label(actionInfoComposite, SWT.NONE);
		lblMailAddress.setText(Messages.getString("MAIL_ADDRESS"));

		txtMailAddress = new Text(actionInfoComposite, SWT.BORDER);
		txtMailAddress.setLayoutData(gridDataText);
		
		Composite  optionsComposite = new Composite(memoryCpuUsageMonitoring, SWT.NONE);
		optionsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		optionsComposite.setLayout(new GridLayout(2, false));

		btnFloatingAverage = new Button(optionsComposite, SWT.NONE);
		btnFloatingAverage.setText(Messages.getString("FLOATING_AVERAGE"));
		GridData btnGridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		btnGridData.widthHint = 120;
		btnFloatingAverage.setLayoutData(btnGridData);
		btnFloatingAverage.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				

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
				

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		
		
		Composite informationObjects = new Composite(group, SWT.BORDER);
		informationObjects.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		informationObjects.setLayout(new GridLayout(1, false));

		
		lblmemCpuUsageMonitoring = new Label(memoryCpuUsageMonitoring, SWT.NONE);
		lblmemCpuUsageMonitoring.setFont(SWTResourceManager.getFont("Sans", 9, SWT.BOLD));
		lblmemCpuUsageMonitoring.setText(Messages.getString("INFORMATION_OBJECTS"));

		((ScrolledComposite) tabComposite).setContent(group);
		group.setSize(group.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		((ScrolledComposite) tabComposite).setExpandVertical(true);
		((ScrolledComposite) tabComposite).setExpandHorizontal(true);
		((ScrolledComposite) tabComposite).setMinSize(group.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}

	@Override
	public void validateBeforeSave() throws ValidationException {
		
	}

}
