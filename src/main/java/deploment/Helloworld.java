package deploment;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class Helloworld {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	/**
	 * 部署流程定义
	 */
	@Test
	public void deploymentProcessDfinition(){
		Deployment deployment=	processEngine.getRepositoryService()//流程定义和部署对象相关的service
		.createDeployment()//创建部署对象
		.name("第一条流程")
		.addClasspathResource("diagrams/Helloword.bpmn")//从classpath文件夹下加载部署文件
		.addClasspathResource("diagrams/Helloword.png")
		.deploy();//完成部署
		System.out.println("部署对象名称:" + deployment.getName() +"id" + deployment.getId());
	}
	
	/**
	 * 部署流程定义（zip）
	 */
	@Test
	public void deploymentProcessDefinition_zip(){
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/Helloworld.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		Deployment deployment=	processEngine.getRepositoryService()//流程定义和部署对象相关的service
		.createDeployment()//创建部署对象
		.name("第一条流程")
		.addZipInputStream(zipInputStream)
		.deploy();//完成部署
		System.out.println("部署对象名称:" + deployment.getName() +"id" + deployment.getId());
	}
	
	/**
	 *启动流程实例
	 */
	@Test
	public void startProcessInstance(){
		ProcessInstance processInstance =  processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的service
		.startProcessInstanceByKey("HelloWorld");//使用流程定义Key启动，key对应bpmn文件中的id，key启动默认是最新版本的流程定义
	    System.out.println("流程实例id"+ processInstance.getId());
	    System.out.println("流程定义ID"+processInstance.getProcessDefinitionId());//流程定义ID
	}
	
	/**
	 *查询当前人的个人任务
	 */
	@Test
	public void findMypersonTask(){
	List<Task> list = processEngine.getTaskService()//与正在执行任务相关的service
		.createTaskQuery()//创建任务查询对象
		.taskAssignee("李四")
		.list();
	if(list!=null&&list.size()>0){
	for (Task personTask: list) {
		System.out.println("任务ID："+personTask.getId());
		System.out.println("任务NAME："+personTask.getName());
		System.out.println("任务当前处理人："+personTask.getAssignee());
		System.out.println("任务创建时间："+personTask.getCreateTime());
		System.out.println("流程实例ID："+personTask.getProcessInstanceId());
		System.out.println("流程定义ID："+personTask.getProcessDefinitionId());
	}
	}
	}
	
	/**
	 *完成当前人的个人任务
	 */
	@Test
	public void completeMypersonTask(){
		processEngine.getTaskService()//任务相关service
		.complete("108");
		System.out.println("---任务已完成---");
	}
	/**
	 * 查询流程是否结束（流程部署启动后，在正在执行的表中（act_ru_execution）会有正在启动的流程实例信息）
	 */
	@Test
	public void isProcessExecution(){
		ProcessInstance processInstance =  processEngine.getRuntimeService()//正在执行的流程
		.createProcessInstanceQuery()//创建流程实例查询对象
		.processInstanceId("501")
		.singleResult();
		//如果为空，流程已结束
		if(processInstance==null){
		   	System.out.println("流程已结束");
		}else{
			System.out.println("流程未结束");
		}
	}
	
}
