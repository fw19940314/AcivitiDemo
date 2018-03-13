package processDefinition;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProcessDefinitionDemo {
	// 流程引擎
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 查询流程定义
	 */
	@Test
	public void findProcessDefinition() {
		List<ProcessDefinition> list = processEngine.getRepositoryService()// 流程定义和部署对象相关的service
				.createProcessDefinitionQuery()// 创建流程定义查询
				// .deploymentId(deploymentId)//使用流程部署ID查询
				.orderByProcessDefinitionKey().desc()// 使用流程定义KEY查询（排序）
				.list();// 返回一个流程定义集合

		if (list != null && list.size() > 0) {
			for (ProcessDefinition pd : list) {
				System.out.println("流程定义ID:" + pd.getId());// 流程定义的key:版本:随机生成数
				System.out.println("流程定义的名称:" + pd.getName());// 对应helloworld.bpmn文件中的name属性值
				System.out.println("流程定义的key:" + pd.getKey());// 对应helloworld.bpmn文件中的id属性值
				System.out.println("流程定义的版本:" + pd.getVersion());// 当流程定义的key值相同的相同下，版本升级，默认1
				System.out.println("资源名称bpmn文件:" + pd.getResourceName());
				System.out.println("资源名称png文件:" + pd.getDiagramResourceName());
				System.out.println("部署对象ID：" + pd.getDeploymentId());
				System.out.println("=============================");
			}
		}
	}

	/**
	 * 删除流程定义
	 */
	@Test
	public void deleteProcessDefinition() {
		processEngine.getRepositoryService()// 与流程定义相关
				/**
				 * 普通不带级联删除只能删除已经启动的流程，如果删除的流程未启动会抛出异常
				 */
				// .deleteDeployment(deploymentId);//使用部署ID删除
				/**
				 * 级联删除能删除未启动的流程，及流程相关联的信息(例如部署表中信息，资源文件表中信息)
				 */
				.deleteDeployment("1", true);// 级联删除

	}

	/**
	 * 查看流程图
	 * 
	 * @throws IOException
	 */
	@Test
	public void findProcessPic() throws IOException {
		// 获取资源名称resourceName
		List<String> list = processEngine.getRepositoryService()
				.getDeploymentResourceNames("301");
		// 定义资源名称
		String resourceName = "";
		if (list != null && list.size() > 0) {
			for (String name : list) {
				if (name.indexOf(".png") > 0) {
					resourceName = name;
				}
			}
		}

		// 获取图片的输入流
		InputStream in = processEngine.getRepositoryService()//
				.getResourceAsStream("301", resourceName);
		File file = new File("F:/" + resourceName);
		FileUtils.copyInputStreamToFile(in, file);
	}

	/**
	 * 查询最新版本的流程定义
	 */
	@Test
	public void findLastVersionProcessDefinition() {
		List<ProcessDefinition> list = processEngine.getRepositoryService()// 流程定义和部署对象相关的service
				.createProcessDefinitionQuery()// 创建流程定义查询
				.orderByProcessDefinitionVersion().asc().list();
		Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();

		if (list != null && list.size() > 0) {
			for (ProcessDefinition pd : list) {
				map.put(pd.getKey(), pd);
			}
		}
		List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(
				map.values());
		if (pdList != null && pdList.size() > 0) {
			for (ProcessDefinition pd : pdList) {
				System.out.println("流程定义ID:" + pd.getId());// 流程定义的key+版本+随机生成数
				System.out.println("流程定义的名称:" + pd.getName());// 对应helloworld.bpmn文件中的name属性值
				System.out.println("流程定义的key:" + pd.getKey());// 对应helloworld.bpmn文件中的id属性值
				System.out.println("流程定义的版本:" + pd.getVersion());// 当流程定义的key值相同的相同下，版本升级，默认1
				System.out.println("资源名称bpmn文件:" + pd.getResourceName());
				System.out.println("资源名称png文件:" + pd.getDiagramResourceName());
				System.out.println("部署对象ID：" + pd.getDeploymentId());
				System.out
						.println("#########################################################");
			}
		}
	}

}
