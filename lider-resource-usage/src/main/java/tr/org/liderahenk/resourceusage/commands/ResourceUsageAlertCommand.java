package tr.org.liderahenk.resourceusage.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.lider.core.api.mail.IMailService;
import tr.org.liderahenk.lider.core.api.persistence.entities.ICommandExecutionResult;
import tr.org.liderahenk.lider.core.api.rest.requests.ITaskRequest;
import tr.org.liderahenk.lider.core.api.plugin.IPluginInfo;
import tr.org.liderahenk.lider.core.api.plugin.ITaskAwareCommand;
import tr.org.liderahenk.lider.core.api.plugin.ICommand;
import tr.org.liderahenk.lider.core.api.service.ICommandContext;
import tr.org.liderahenk.lider.core.api.service.ICommandResult;
import tr.org.liderahenk.lider.core.api.service.ICommandResultFactory;
import tr.org.liderahenk.lider.core.api.service.enums.CommandResultStatus;

public class ResourceUsageAlertCommand implements ICommand, ITaskAwareCommand{

	private Logger logger = LoggerFactory.getLogger(ResourceUsageAlertCommand.class);
	
	private ICommandResultFactory resultFactory;
	private IPluginInfo pluginInfo;
	private IMailService mailService;

	@Override
	public ICommandResult execute(ICommandContext context) {
		
		// TODO Modify parameter map before sending it to agent(s).
		ITaskRequest req = context.getRequest();
		Map<String, Object> parameterMap = req.getParameterMap();
		parameterMap.put("dummy-param", "dummy-param-value");
		
		logger.debug("Parameter map updated.");
		
		// TODO Modify entity objects related to plugin command via DB service
		//Object entity = new Object();
		//pluginDbService.save(entity);
		logger.debug("Entity saved successfully.");
		
		// TODO Modify result map to provide additional parameters or info before sending it back to console.
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("dummy-param", "dummy-param-value");
		
		ICommandResult commandResult = resultFactory.create(CommandResultStatus.OK, new ArrayList<String>(), this, resultMap);

		// TODO logService
		
		return commandResult;
	}

	@Override
	public ICommandResult validate(ICommandContext context) {
		// TODO Validate before command execution
		return resultFactory.create(CommandResultStatus.OK, null, this, null);
	}

	@Override
	public String getCommandId() {
		// TODO Unique command ID used to match incoming REST requests to this Command class.
		return "RESOURCE_INFO_ALERT";
	}

	@Override
	public Boolean executeOnAgent() {
		// TODO True if we need to send a task to agent(s), false otherwise.
		return true;
	}
	
	@Override
	public String getPluginName() {
		return pluginInfo.getPluginName();
	}

	@Override
	public String getPluginVersion() {
		return pluginInfo.getPluginVersion();
	}

	public void setResultFactory(ICommandResultFactory resultFactory) {
		this.resultFactory = resultFactory;
	}
	
	public void setPluginInfo(IPluginInfo pluginInfo) {
		this.pluginInfo = pluginInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onTaskUpdate(ICommandExecutionResult result) {
		Map<String, Object> responseData;
		try {
			responseData = new ObjectMapper().readValue(result.getResponseData(), 0, result.getResponseData().length,
					new TypeReference<HashMap<String, Object>>() {
			});
		    List<String> resultList = (List<String>) responseData.get("Result");
		    for(int i = 0 ; i < resultList.size()/2 ; i=i+2){
				List<String> to = new ArrayList<String>();
				to.add(resultList.get(i+1));
				getMailService().sendMail(to, "Ahenk Makinada Limit Değerler Aşıldı!", resultList.get(i));
		    }
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public IMailService getMailService() {
		return mailService;
	}

	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}
	
}
