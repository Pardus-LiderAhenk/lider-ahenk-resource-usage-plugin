package tr.org.liderahenk.resourceusage.tabs;

import org.eclipse.swt.widgets.Composite;

import tr.org.liderahenk.liderconsole.core.exceptions.ValidationException;

public interface IUsageTab {
	public void createInputs(Composite tabComposite) throws Exception;


	public void validateBeforeSave() throws ValidationException;
}
