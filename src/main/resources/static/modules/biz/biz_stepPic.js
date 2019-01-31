$.namespace("biz");

var stepMap;
var nameMap = {'发起工单':'发起申请','业务部门':'审批','项目负责人':'审批','运维部门':'审批','公司主管':'审批','分公司领导审批':'审批'
	,'分公司总经理审批':'审批','分公司主管':'审批','部门会签':'审批','项目主管评估':'审批','运维主管评估':'审批','实施主管评估':'审批'
	,'需求评估':'审批','厂家评估':'审批','部门主管':'审批','工单审批':'审批','实施反馈':'处理','需求管理人确认':'处理','服务台':'处理'
	,'二线处理':'处理','三线处理':'处理','工单配置':'处理'
	,'质量确认':'确认关闭','用户确认':'确认关闭','确认签收':'确认关闭'};;

biz.stepPic = {
	//循环stepMap并画步骤图
	loadStepPic : function(logs,type){
		if(type=="事件管理")
			biz.stepPic.eventStepPic(logs);
		else if(type=="交维管理")
			biz.stepPic.maintainStepPic(logs);
		else if(type=="问题管理")
			biz.stepPic.problemStepPic(logs);
		else if(type=="变更管理")
			biz.stepPic.projectStepPic(logs);
		else if(type=="故障管理")
			biz.stepPic.faultStepPic(logs);
		else
			biz.stepPic.permissionStepPic1(logs,type);
	},
	loopStepMap  : function(logs){
		var stepUl = $("<ul class='list-unstyled step' id='step_log'>");
		var stepUl2 = $("<ul class='list-unstyled step-text' id='step_text'>");

		$("#stepPic").append(stepUl);
		$("#stepPic2").append(stepUl2);
		
		$.each(stepMap.map,function(key, value) {
			var stepLi = $("<li><em class='step-num'></em><span class='step-mask'></span></li>");
			var stepLi2 = $("<li><div></div><span></span></li>");
			if (value != null) {
				if(value.indexOf("back")!=-1){
					stepLi.addClass("step-over");
					stepLi.addClass("step-return");
					stepLi2.find("span").append(value.substring(value.indexOf("back")+4,value.length-1));
				}else{
					stepLi.addClass("step-over");
					stepLi2.find("span").append(value);
				}
			}
			stepLi2.find("div").append(key);
			stepUl.append(stepLi);
			stepUl2.append(stepLi2);
			if (stepMap.last == key)
				stepLi.addClass("last-step");
			else if(value != null) {
				if (stepMap.newest == key || 
						(stepMap.newest == key && logs[logs.length - 1].taskName=="延期申请确认"))
					stepLi.find("span").attr({"style" : "width:83px"});			
				else
					stepLi.find("span").attr({"style" : "width:100px"});
			}
		});
		var em = stepUl.find("em");
		for ( var i = 0; i < em.length; i++) {
			$(em[i]).append(i + 1);
		}
		$("#stepPic").css({width:140*(em.length-1)});
	},
	// 事件管理步骤图
	eventStepPic : function(logs) {
		stepMap = {map : {'发起工单' : null,'服务台处理' : null,'厂商处理' : null,'用户反馈' : null,'服务台关闭' : null,'关闭' : null},'last' : '关闭','newest':null};
		nameMap = {'发起工单' : '发起工单','提交':'发起工单','服务台处理' : '服务台处理','厂商处理' : '厂商处理'
			,'用户反馈' : '用户反馈','服务台关闭' : '服务台关闭','关闭' : '关闭'}
		
		stepMap.newest = logs[logs.length - 1].taskName;
		for ( var i = 0; i < logs.length; i++) {
			if (logs[i].taskName == "服务台关闭" && logs[i].handleResult == "提交"){
				stepMap.map["关闭"] = logs[i].createTime;
				stepMap.newest = "关闭";
			}
			else if(logs[i].handleResult == "退回")
				stepMap.map[logs[i].taskName] = "back"+logs[i].createTime;
			else if(logs[i].taskName == "用户反馈"&&biz.detail.workInfo.status=="已完成"){
				delete stepMap.map["服务台关闭"];		
				stepMap.map[nameMap[logs[i].taskName]] = logs[i].createTime;
				stepMap.map["关闭"] = logs[i].createTime;
				stepMap.newest = "关闭";
			}
			else if(logs[i].taskName in nameMap)
				stepMap.map[nameMap[logs[i].taskName]] = logs[i].createTime;
		}
		if (stepMap.map["厂商处理"] == null&&stepMap.map["用户反馈"] != null){
			delete stepMap.map["厂商处理"];
		}else if (stepMap.map["服务台处理"] == null){
			delete stepMap.map["服务台处理"];
		}
		biz.stepPic.loopStepMap(logs);
	},
	// 交维管理步骤图
	maintainStepPic : function(logs) {
		stepMap = {map : {'发起工单' : null,'项目负责人' : null,'维护负责人' : null,'第三方维护' : null,'确认文档入库' : null,'用户关闭' : null},'last' : '用户关闭','newest':null};
		nameMap = {'发起工单' : '发起工单','项目负责人' : '项目负责人','维护负责人' : '维护负责人'
			,'第三方维护' : '第三方维护','确认文档入库' : '确认文档入库','用户关闭' : '用户关闭'}
		
		stepMap.newest = nameMap[logs[logs.length - 1].taskName];
		for ( var i = 0; i < logs.length; i++) {
			if (logs[i].taskName in nameMap)
				stepMap.map[nameMap[logs[i].taskName]] = logs[i].createTime;
			if(logs[i].handleResult == "驳回")
				stepMap.map[logs[i].taskName] = "back"+logs[i].createTime;
		}
		biz.stepPic.loopStepMap(logs);
	},
	// 问题管理步骤图
	problemStepPic : function(logs) {
		stepMap = {map : {'发起工单' : null,'问题经理审核' : null,'问题专家会签' : null,'经理汇总方案' : null,'用户确认方案' : null,'问题经理关闭' : null},'last' : '问题经理关闭','newest':null};
		nameMap = {'发起工单' : '发起工单','问题经理审核' : '问题经理审核','问题专家会签' : '问题专家会签'
			,'经理汇总方案' : '经理汇总方案','用户确认方案' : '用户确认方案','问题经理关闭' : '问题经理关闭'}
		
		stepMap.newest = nameMap[logs[logs.length - 1].taskName];
		for ( var i = 0; i < logs.length; i++) {
			if (logs[i].taskName in stepMap.map)
				stepMap.map[logs[i].taskName] = logs[i].createTime;
			if(logs[i].handleResult == "驳回"||logs[i].handleResult == "重新分派")
				stepMap.map[logs[i].taskName] = "back"+logs[i].createTime;
		}
		if (stepMap.map["问题专家会签"] == null && stepMap.map["用户确认方案"] != null)
			delete stepMap.map["问题专家会签"];
		if (stepMap.map["经理汇总方案"] == null && stepMap.map["用户确认方案"] != null)
			delete stepMap.map["经理汇总方案"];
		biz.stepPic.loopStepMap(logs);
	},
	permissionStepPic1 : function(logs,type){
		stepMap = {map: {'发起申请' : null,'审批':null,'处理':null,'确认关闭':null},'last':'确认关闭','newest':null};
		stepMap.newest = nameMap[logs[logs.length - 1].taskName];
		for ( var i = 0; i < logs.length; i++) {
			if (logs[i].taskName in nameMap)
				stepMap.map[nameMap[logs[i].taskName]] = logs[i].createTime;
			if(logs[i].taskName in nameMap&&logs[i].handleResult == "驳回")
				stepMap.map[nameMap[logs[i].taskName]] = "back"+logs[i].createTime;
		}
		if(type=="办公支持流程-分公司"||type=="办公支持流程-总部"||type=="应用故障管理")
			delete stepMap.map['审批'];
		else if(type=="版本发布管理"&&logs[logs.length-1].handleResult=="提交"&&logs[logs.length-1].taskName=="三线处理"){
			stepMap.map["确认关闭"] = logs[logs.length-1].createTime;
			stepMap.newest = "确认关闭";
		}
		biz.stepPic.loopStepMap(logs);
	},
	projectStepPic : function(logs){
		stepMap = {map: {'发起申请':null,'审批':null,'处理':null,'确认关闭':null},'last':'确认关闭','newest':null};
		nameMap = {'发起工单':'发起申请','变更经理审批':'审批','变更操作':'处理','用户确认':'确认关闭'};
		stepMap.newest = nameMap[logs[logs.length - 1].taskName];
		for ( var i = 0; i < logs.length; i++) {
			if (logs[i].taskName == "变更风险评估" && logs[i].handleResult == "通过"){
				stepMap.map["审批"] = logs[i].createTime;
			}
			else if (logs[i].taskName in nameMap)
				stepMap.map[nameMap[logs[i].taskName]] = logs[i].createTime;
		}
		biz.stepPic.loopStepMap(logs);
	},
	faultStepPic : function(logs){
		stepMap = {map: {'发起申请':null,'处理':null,'用户反馈':null,'项目负责人评价':null},'last':'项目负责人评价','newest':null};
		nameMap = {'发起工单':'发起申请','提交':'发起申请','用户反馈':'用户反馈','项目负责人评价':'项目负责人评价'};
		stepMap.newest = nameMap[logs[logs.length - 1].taskName];
		for ( var i = 0; i < logs.length; i++) {
			if ((logs[i].taskName == "服务台处理"||logs[i].taskName == "服务厂商") && logs[i].handleResult == "解决"){
				stepMap.map["处理"] = logs[i].createTime;
			}
			else if (logs[i].taskName in nameMap)
				stepMap.map[nameMap[logs[i].taskName]] = logs[i].createTime;
			if(logs[i].taskName in {'服务台处理':'','服务厂商':""}&&logs[i].handleResult == "驳回")
				stepMap.map['处理'] = "back"+logs[i].createTime;
			else if(logs[i].taskName == "用户反馈"&&logs[i].handleResult == "驳回")
				stepMap.map['处理'] = "back"+logs[i].createTime;
		}
		biz.stepPic.loopStepMap(logs);
	}
}