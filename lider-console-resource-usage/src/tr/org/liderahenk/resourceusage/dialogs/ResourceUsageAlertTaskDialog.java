package tr.org.liderahenk.resourceusage.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.dialogs.DefaultTaskDialog;
import tr.org.liderahenk.liderconsole.core.exceptions.ValidationException;
import tr.org.liderahenk.liderconsole.core.utils.SWTResourceManager;
import tr.org.liderahenk.liderconsole.core.widgets.Notifier;
import tr.org.liderahenk.liderconsole.core.xmpp.notifications.TaskStatusNotification;
import tr.org.liderahenk.resourceusage.constants.ResourceUsageConstants;
import tr.org.liderahenk.resourceusage.i18n.Messages;
import tr.org.liderahenk.resourceusage.model.ResourceUsageAlertItem;

/**
 * Task execution dialog for resource-usage plugin.
 * 
 */
public class ResourceUsageAlertTaskDialog extends DefaultTaskDialog{

	private static final Logger logger = LoggerFactory.getLogger(ResourceUsageAlertTaskDialog.class);

	private Composite tableComposite;
	private TableViewer tableViewer;
	private Button btnAdd;
	private Button btnDelete;
	private Button btnEdit;
	String upperCase = "";

	private ResourceUsageAlertItem item;

	private IEventBroker eventBroker = (IEventBroker) PlatformUI.getWorkbench().getService(IEventBroker.class);

	public ResourceUsageAlertTaskDialog(Shell parentShell, String dnString) {
		super(parentShell, dnString);
		upperCase = getPluginName().toUpperCase(Locale.ENGLISH);
		eventBroker.subscribe(getPluginName().toUpperCase(Locale.ENGLISH), eventHandler);

	}

	private EventHandler eventHandler = new EventHandler() {
		@Override
		public void handleEvent(final Event event) {
			Job job = new Job("TASK") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("RESOURCE_USAGE_ALERT", 100);
					try {
						TaskStatusNotification taskStatus = (TaskStatusNotification) event
								.getProperty("org.eclipse.e4.data");
						byte[] data = taskStatus.getResult().getResponseData();
						final Map<String, Object> responseData = new ObjectMapper().readValue(data, 0, data.length,
								new TypeReference<HashMap<String, Object>>() {
						});
						Display.getDefault().asyncExec(new Runnable() {

							@Override
							public void run() {
									if(responseData.containsKey("Result")){
										List<String> result = (List<String>) (responseData.get("Result"));
										for(int i = 0 ; i < result.size()/2 ; i=i+2 ){
											String resultMessage = result.get(i);
											String emailAddress = result.get(i+1);
											String subject = "Ahenkte Kaynak Kullanımı Uyarısı";
										}
									}
							}
						});
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						Notifier.error("", Messages.getString("UNEXPECTED_ERROR_ACCESSING_RESOURCE_USAGE"));
					}
					monitor.worked(100);
					monitor.done();

					return Status.OK_STATUS;
				}
			};

			job.setUser(true);
			job.schedule();
		}
	};

	@Override
	public String createTitle() {
		return Messages.getString("RESOURCE_USAGE_ALERT");
	}

	@Override
	public Control createTaskDialogArea(Composite parent) {

		createTableArea(parent);

		return null;
	}

	private void createTableArea(Composite parent) {
		tableComposite = new Composite(parent, SWT.BORDER);
		tableComposite.setLayout(new GridLayout(1, false));
		createButtons(tableComposite);
		createTable(tableComposite);
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

		// Hook up listeners
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				Object firstElement = selection.getFirstElement();
				firstElement = (ResourceUsageAlertItem) firstElement;
				if (firstElement instanceof ResourceUsageAlertItem) {
					setItem((ResourceUsageAlertItem) firstElement);
				}
				btnEdit.setEnabled(true);
				btnDelete.setEnabled(true);
			}
		});
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				ResourceUsageAlertItemDialog dialog = new ResourceUsageAlertItemDialog(parent.getShell(), getItem(),
						tableViewer);
				dialog.open();
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

	private void createTableColumns() {

		String[] titles = { Messages.getString("TYPE"), Messages.getString("VALUE"), Messages.getString("EMAIL") };
		int[] bounds = { 200, 200 };

		TableViewerColumn isLocalColumn = createTableViewerColumn(titles[0], bounds[0]);
		isLocalColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ResourceUsageAlertItem) {
					return ((ResourceUsageAlertItem) element).getType();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn recordDescriptionColumn = createTableViewerColumn(titles[1], bounds[0]);
		recordDescriptionColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ResourceUsageAlertItem) {
					return ((ResourceUsageAlertItem) element).getLimit();
				}
				return Messages.getString("UNTITLED");
			}
		});

		TableViewerColumn logFilePathColumn = createTableViewerColumn(titles[2], bounds[1]);
		logFilePathColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ResourceUsageAlertItem) {
					return ((ResourceUsageAlertItem) element).getEmail();
				}
				return Messages.getString("UNTITLED");
			}
		});
	}

	private void createButtons(final Composite parent) {
		final Composite tableButtonComposite = new Composite(parent, SWT.NONE);
		tableButtonComposite.setLayout(new GridLayout(3, false));

		btnAdd = new Button(tableButtonComposite, SWT.NONE);
		btnAdd.setText(Messages.getString("ADD"));
		btnAdd.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnAdd.setImage(SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/add.png"));
		btnAdd.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ResourceUsageAlertItemDialog dialog = new ResourceUsageAlertItemDialog(
						Display.getDefault().getActiveShell(), tableViewer);
				dialog.create();
				dialog.open();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		btnEdit = new Button(tableButtonComposite, SWT.NONE);
		btnEdit.setText(Messages.getString("EDIT"));
		btnEdit.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/edit.png"));
		btnEdit.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnEdit.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (null == getItem()) {
					Notifier.warning(null, Messages.getString("PLEASE_SELECT_ITEM"));
					return;
				}
				ResourceUsageAlertItemDialog dialog = new ResourceUsageAlertItemDialog(tableButtonComposite.getShell(),
						getItem(), tableViewer);
				dialog.open();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		btnDelete = new Button(tableButtonComposite, SWT.NONE);
		btnDelete.setText(Messages.getString("DELETE"));
		btnDelete.setImage(
				SWTResourceManager.getImage(LiderConstants.PLUGIN_IDS.LIDER_CONSOLE_CORE, "icons/16/delete.png"));
		btnDelete.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		btnDelete.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (null == getItem()) {
					Notifier.warning(null, Messages.getString("PLEASE_SELECT_ITEM"));
					return;
				}
				@SuppressWarnings("unchecked")
				List<ResourceUsageAlertItemDialog> items = (List<ResourceUsageAlertItemDialog>) tableViewer.getInput();
				items.remove(tableViewer.getTable().getSelectionIndex());
				tableViewer.setInput(items);
				tableViewer.refresh();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void validateBeforeExecution() throws ValidationException {
		if ( tableViewer.getInput() == null || ((List<ResourceUsageAlertItem>) tableViewer.getInput()).isEmpty()) {
			throw new ValidationException(Messages.getString("ADD_ITEM"));
		}
		
	}

	@Override
	public Map<String, Object> getParameterMap() {
		Map<String, Object> taskData = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		List<ResourceUsageAlertItem> items = (List<ResourceUsageAlertItem>) tableViewer.getInput();
		if (items != null) {
			taskData.put(ResourceUsageConstants.PARAMETERS.LIST_ITEMS, items);
		}
		return taskData;
	}

	@Override
	public String getCommandId() {
		return "RESOURCE_INFO_ALERT";
	}

	@Override
	public String getPluginName() {
		return ResourceUsageConstants.PLUGIN_NAME;
	}

	@Override
	public String getPluginVersion() {
		return "1.0.0";
	}

	public ResourceUsageAlertItem getItem() {
		return item;
	}

	public void setItem(ResourceUsageAlertItem item) {
		this.item = item;
	}

}
