package sequenceFlow;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class SequenceFlowDemo {
	// 获取流程引擎
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 1.部署流程
	 */
	@Test
	public void deploymentProcess() {
		// 获取流程资源
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream(
				"sequenceFlowDemo.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream(
				"sequenceFlowDemo.png");
		processEngine
				.getRepositoryService()
				.createDeployment()
				// 创建部署流程对象
				.name("请假流程")
				.addInputStream("sequenceFlowDemo.bpmn", inputStreamBpmn)
				.addInputStream("sequenceFlowDemo.png", inputStreamPng)
				.deploy();// 完成部署
		System.out.println("--完成部署--");
	}

	/**
	 * 2.启动流程实例
	 */
	@Test
	public void startProcessInstance() {
		ProcessInstance processInstance = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的service
				.startProcessInstanceByKey("sequenceFlow");// 使用流程定义Key启动，key对应bpmn文件中的id，key启动默认是最新版本的流程定义
		System.out.println("流程实例id" + processInstance.getId());
		System.out.println("流程定义ID" + processInstance.getProcessDefinitionId());// 流程定义ID
	}

	/**
	 * 3.产看个人任务
	 */
	@Test
	public void finfMyTask() {
		List<Task> list = processEngine.getTaskService().createTaskQuery()// 查询任务相关
				.taskAssignee("李四").list();
		if (list != null && list.size() > 0) {
			for (Task personTask : list) {
				System.out.println("任务ID：" + personTask.getId());
				System.out.println("任务NAME：" + personTask.getName());
				System.out.println("任务当前处理人：" + personTask.getAssignee());
				System.out.println("任务创建时间：" + personTask.getCreateTime());
				System.out.println("流程实例ID："
						+ personTask.getProcessInstanceId());
				System.out.println("流程定义ID："
						+ personTask.getProcessDefinitionId());
			}
		}
	}

	/**
	 * 4.完成任务
	 */
	@Test
	public void completeMytask() {
		//完成任务时，设置流程变量来指定下一个流程节点（连线），对应的是sequenceFlow.bpmn文件中的${message=='重要'}
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("message", "重要");
		processEngine.getTaskService().complete("1403", variables);
		System.out.println("--审批通过--");
	}

}
