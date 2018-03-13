package receiveTask;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ReceiveTaskDemo {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
  /**
   * 接受任务
   * ReceiceTask任务，机器自动完成的任务
   * 只会在act_ru_execution表中产生一条数据
   */
	@Test
	public void testExecution() throws Exception {
		// 1 发布流程
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("receiveTaskDemo.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("receiveTaskDemo.png");
		processEngine.getRepositoryService()//
						.createDeployment()//
						.addInputStream("receiveTaskDemo.bpmn", inputStreamBpmn)//
						.addInputStream("receiveTaskDemo.png", inputStreamPng)//
						.deploy();

		// 2 启动流程
		ProcessInstance pi = processEngine.getRuntimeService()//
							.startProcessInstanceByKey("ReceiveTask");
		System.out.println("pid:" + pi.getId());
		String pid = pi.getId();
		
		// 3查询是否有一个执行对象在描述receivetask1
		Execution e1 = processEngine.getRuntimeService()//
						.createExecutionQuery()//
						.processInstanceId(pid)//
						.activityId("receivetask1")//
						.singleResult();

		// 4执行一堆逻辑，并设置流程变量
		Map<String,Object> vars = new HashMap<String, Object>();
		vars.put("报销金额", 10000);
		//  5流程向后执行一步：往后推移e1,使用signal给流程引擎信号，告诉他当前任务已经完成了，可以往后执行
		processEngine.getRuntimeService()
				.signal(e1.getId(),vars);
		
		// 6判断当前流程是否在receivetask2节点
		Execution e2 = processEngine.getRuntimeService()//
						.createExecutionQuery()//
						.processInstanceId(pid)//
						.activityId("receivetask2")//
						.singleResult();
		
		// 7获取流程变量
		Integer money = (Integer) processEngine.getRuntimeService()//
								.getVariable(e2.getId(), "报销金额");
		System.out.println("报销金额" +money);
		// 8向后执行一步：任务完成，往后推移”给老板发短信“任务
		processEngine.getRuntimeService()//
				.signal(e2.getId());
		
		
		// 9查询流程状态
	    pi = processEngine.getRuntimeService()//
	    				.createProcessInstanceQuery()//
	    				.processInstanceId(pid)//
	    				.singleResult();
	    if(pi==null){
	    	    System.out.println("流程正常执行！！！，已经结束了");
	    }
	}

}
