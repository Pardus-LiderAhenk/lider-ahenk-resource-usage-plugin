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
import tr.org.liderahenk.lider.core.api.plugin.ICommand;
import tr.org.liderahenk.lider.core.api.plugin.IPluginInfo;
import tr.org.liderahenk.lider.core.api.plugin.ITaskAwareCommand;
import tr.org.liderahenk.lider.core.api.rest.requests.ITaskRequest;
import tr.org.liderahenk.lider.core.api.service.ICommandContext;
import tr.org.liderahenk.lider.core.api.service.ICommandResult;
import tr.org.liderahenk.lider.core.api.service.ICommandResultFactory;
import tr.org.liderahenk.lider.core.api.service.enums.CommandResultStatus;

public class SendMailCommand implements ICommand, ITaskAwareCommand{

	private Logger logger = LoggerFactory.getLogger(SendMailCommand.class);
	
	private ICommandResultFactory resultFactory;
	private IPluginInfo pluginInfo;
	private IMailService mailService;

	@Override
	public ICommandResult execute(ICommandContext context) {
		
		ITaskRequest req = context.getRequest();
		Map<String, Object> parameterMap = req.getParameterMap();
		parameterMap.put("dummy-param", "dummy-param-value");
		
		logger.debug("Parameter map updated.");
		
		//Object entity = new Object();
		//pluginDbService.save(entity);
		logger.debug("Entity saved successfully.");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("dummy-param", "dummy-param-value");
		
		ICommandResult commandResult = resultFactory.create(CommandResultStatus.OK, new ArrayList<String>(), this, resultMap);

		return commandResult;
	}

	@Override
	public ICommandResult validate(ICommandContext context) {
		return resultFactory.create(CommandResultStatus.OK, null, this, null);
	}

	@Override
	public String getCommandId() {
		return "SEND_MAIL";
	}

	@Override
	public Boolean executeOnAgent() {
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

	public IMailService getMailService() {
		return mailService;
	}

	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}

	@Override
	public void onTaskUpdate(ICommandExecutionResult result) {
		Map<String, Object> responseData;
		try {
			responseData = new ObjectMapper().readValue(result.getResponseData(), 0, result.getResponseData().length,
					new TypeReference<HashMap<String, Object>>() {
			});
			System.out.println(responseData);
		    List<String> to = new ArrayList<String>();
			to.add(responseData.get("to").toString());
				getMailService().sendMail(to, "Ahenk Makinada Limit Değerler Aşıldı!", responseData.get("body").toString());
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	
}
