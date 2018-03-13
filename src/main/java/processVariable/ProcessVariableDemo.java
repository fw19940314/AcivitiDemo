package processVariable;

import java.util.Date;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.junit.Test;

/**
 * 流程变量
 * @author jerry.feng
 */
public class ProcessVariableDemo {
  
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	/**
	 * 步骤： 1.部署流程定义 2.启动流程实例 3.set设置流程变量 4.get获取流程变量
	 */
	/**
	 * 设置流程变量
	 */
	@Test
	public void setProcessVariable() {
		TaskService taskService = processEngine.getTaskService();// 正在执行的任务
		taskService.setVariableLocal("504", "申请请假天数", 2);//local与不带Local区别，带Local设置值时候，与任务Id绑定
		taskService.setVariable("504", "请假日期", new Date());
		taskService.setVariable("504", "请假原因", "事假");
		System.out.println("--流程变量设置成功--");
	}
	
	/**
	 * 获取流程变量
	 */
	@Test
	public void getProcessVariable(){
		TaskService taskService = processEngine.getTaskService();// 正在执行的任务
		int day = (int) taskService.getVariable("504", "申请请假天数");
		Date date = (Date) taskService.getVariable("504", "请假日期");
		String resenan = (String) taskService.getVariable("504", "请假原因");
		System.out.println("请假天数："+day);
		System.out.println("请假日期："+date);
		System.out.println("请假原因："+resenan);
	}
}
