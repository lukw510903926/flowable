$.namespace("biz.show");

biz.show = {
		data:{
			tr:$("<tr></tr>")
		},
		getView:function(option){
			for(var key in biz.edit.data){
				delete biz.show.data[key];
			}
			for(var key in option){
                biz.show.data[key] = option[key];
                if(option.list)
			        biz.show.data.list = biz.show.table.listByTreatment(option.list);
            }
			biz.show.data.tr = $("<tr></tr>");
			return biz.show.table;
		}
};


biz.show.table = {
		setDynamic:function(option){
			if(option!=undefined){
				if(option.table!=undefined){
					biz.show.data.table = option.table;
				}
				if(option.tr!=undefined){
					biz.show.data.tr = option.tr;
				}
			}else{
				option = {};
			}
			if(option.list==undefined){
				option.list = biz.show.data.list;
			}
			if(option.list!=null&&option.list.length>0){
				for(var j=0;j<option.list.length;j++){
					switch(option.list[j].viewComponent){
						case "TEXTAREA":
							tr = biz.show.table.addTextarea(option.list[j]);
							break;
						case "TEXT":
							tr = biz.show.table.addTextField(option.list[j]);							
							break;
						case "MCMGRID":
							tr = biz.show.table.addConfigItem(option.list[j]);
							break;
                        case "ORGCOMBOBOX":
                        	biz.edit.form.addorgComBobox(option.list[j]);
                        	break;
                        case "REQUIREDFILE":
                        	tr = biz.show.table.addQuiredFile(option.list[j]);
                        	break;
                        case "GROUPHEAD":
                        	tr = biz.show.table.addGroupHead(option.list[j]);
                        	break;
						default:
							tr = biz.show.table.addTextField(option.list[j]);	
					}
				}
				if(option.end||option.end==undefined)
					biz.show.table.appendTd(option.table);				
			}
			return biz.show.data.tr;
		},
		addMemberLinkage:function(data,list,table,tr,type){
            
            biz.edit.form.memberLinkage.loadSectorBox();
            if(table==undefined){
                table = biz.show.data.table;
            }
            if(tr!=undefined){
                biz.show.data.tr = tr;
            }
            biz.show.table.appendTd();
            var th = $("<th></th>");
            var td = $("<td colspan='3'></td>");
            th.append(data.alias+":");
            var handleUser = "ActualApplicant";
            var input = $("<input type='hidden' name='"+handleUser+"' class='fslTextBox' />");
            var input2 = $("<input type='hidden' name='applicantMobile' class='fslTextBox'></input>");
            handleUser = 'ActualApplicantName';
            var chInput = $("<input type='text' name='"+handleUser+"' class='fslTextBox' style='width:40%;' readonly='readonly'/>");
            biz.edit.form.addCkeckEmpty(data,th,input);
            var add = "<a class='btn btn-y' onclick='biz.edit.form.memberLinkage.openMemberContainer()'>人员</a>";
            var remove = "<a class='btn btn-n' onclick='biz.edit.form.memberLinkage.clearMember()'>清空</a>";
            td.append(input);
            td.append(input2);
            td.append(chInput);
            td.append(add);
            td.append(remove);
            biz.edit.form.memberLinkage.data.inputName = "ActualApplicant";
            //加入联动，注意联系方式参数是否设置联动
            biz.edit.form.memberLinkage.data.mobileName = "applicantMobile";
            
            var container = biz.edit.form.memberLinkage.createMemberContainer(data.name);
            td.append(container);
            var div=$('<div id="roleMenuContent" class="menuContent" style="display:none; position: absolute;"></div>');
            var ul = $('<ul id="roleTree" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>');
            div.append(ul);
            td.append(div);
            biz.show.data.tr.append(th);
            biz.show.data.tr.append(td);
            table.append(biz.show.data.tr);
            biz.show.data.tr = $("<tr></tr>");
            biz.edit.form.memberLinkage.roleTree.loadRoleTree();
            return biz.show.data.tr;
        },
		addTextarea:function(data,table,tr){
			if(table==undefined){
				table = biz.show.data.table;
			}
			if(tr!=undefined){
				biz.show.data.tr = tr;
			}
			biz.show.table.appendTd();
			var th = $("<th></th>");
			th.text(data.alias+":");
			var td = $("<td colspan='3'></td>");
			var name = data.id?data.id+"&"+biz.show.data.taskId:"";
			td.html("<span class='fslTextBoxR' name='"+name+"'></span>");
			biz.show.data.tr.append(th);
			biz.show.data.tr.append(td);
			table.append(biz.show.data.tr);
			biz.show.data.tr = $("<tr></tr>");
			return td.children("span");
		},
		addQuiredFile:function(data,table,tr){
			
			if(table==undefined){
				table = biz.show.data.table;
			}
			if(tr!=undefined){
				biz.show.data.tr = tr;
			}
			biz.show.table.appendTd();
			var th = $("<th></th>");
			th.text(data.alias+":");
			var td = $("<td colspan='3'></td>");
			var name = data.id?data.id+"&"+biz.show.data.taskId:"";
			td.html("<span class='fslTextBoxR' name='"+name+"'></span>");
			biz.show.data.tr.append(th);
			biz.show.data.tr.append(td);
			table.append(biz.show.data.tr);
			biz.show.data.tr = $("<tr></tr>");
			return td.children("span");
		},
        //添加个分组头部
        addGroupHead:function(data,table,tr){
            if(table==undefined){
            	table = biz.show.data.table;
            }
            if(tr!=undefined){
            	biz.show.data.tr = tr;
            }
            biz.show.table.appendTd();
            var hidden = $("<input type='hidden' name='"+data.name+"' value='true'>");
            var th = $("<th>");
            var td = $("<td colspan='5' style='padding:0'>");
            var div = $("<h5 style='padding:6px 5px;background:#f7f7f7' >" +data.alias+"："+data.groupName+"</h5>");
            td.append(hidden,div);
			biz.show.data.tr.append(td);
            table.append(biz.show.data.tr);
            biz.show.data.tr = $("<tr>");
            return biz.show.data.tr;
        },
		addMessage:function(data,table,tr){
            
            if(table==undefined){
                table = biz.edit.data.table;
            }
            if(tr!=undefined){
                biz.edit.data.tr = tr;
            }
            biz.edit.form.appendTd();
            var th = $("<th></th>");
            var td = $("<td colspan='3'></td>");
            th.append(data.alias+":");
            var textarea = "<textarea name='base.handleMessage' rows='2' cols='20' class='fslTextBox' style='height:81px;width:90%;'></textarea>";
            td.html(textarea);
            biz.edit.form.addCkeckEmpty(data,th,td.children("textarea"));
            biz.edit.data.tr.append(th);
            biz.edit.data.tr.append(td);
            table.append(biz.edit.data.tr);
            biz.edit.data.tr = $("<tr></tr>");
            return biz.edit.data.tr;
        },
		addTextField:function(data,table,tr){
			if(table==undefined){
				table = biz.show.data.table;
			}
			if(tr!=undefined){
				biz.show.data.tr = tr;
			}
			var th = $("<th></th>");
			th.text(data.alias+":");
			var td = $("<td></td>");				
			var name = data.id?data.id+"&"+biz.show.data.taskId:"";
			if(biz.detail&&biz.detail.workInfo.bizType=="事件管理"&&biz.detail.workInfo.taskDefKey=="已完成"&&biz.detail.reSetWorkTime){
				if(data.name&&data.name.lastIndexOf("Workload")!=-1){
					td.html("<input class='fslTextBox workTime' style='height: 24px;' name='"+name+"'></input>");
				}else if(data.name&&data.name.lastIndexOf("projectContract")!=-1){
					var select = $("<select id='"+data.name+"' name='"+name+"' class='fslTextBox workTime'  style='width:100px'></select>");
					select.addClass("js-example-basic-single");
					select.attr('data-width','60%');
					biz.edit.form.combobox.loadDictComboBox(select,data.viewParams);
					td.append(select);
				}else if (data.name&&data.name.lastIndexOf("actualOccurrenceTime")!=-1){
					var input = $("<input type='text' style='height: 26px;' readonly='readonly' name='"+name+"' class='fslTextBox workTime Wdate'/>");
					input.focus(function(){WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss'})});
					td.append(input);
				}else{
					td.html("<span class='fslTextBoxR' name='"+name+"'></span>");
				}
			}else{
				td.html("<span class='fslTextBoxR' name='"+name+"'></span>");
			}
			biz.show.data.tr.append(th);
			biz.show.data.tr.append(td);
			if(biz.show.data.tr.children("td").length==2){
				table.append(biz.show.data.tr);
				biz.show.data.tr = $("<tr></tr>");
			};
			return td.children("span");
		},
		
		addComboBox:function(data,table,tr){
			
			if(table==undefined){
				table = biz.show.data.table;
			}
			if(tr!=undefined){
				biz.show.data.tr = tr;
			}
			var th = $("<th></th>");
			th.text(data.alias+":");
			var td = $("<td></td>");				
			var name = data.id?data.id+"&"+biz.show.data.taskId:"";
			td.html("<span class='fslTextBoxR' name='"+name+"'></span>");
			biz.show.data.tr.append(th);
			biz.show.data.tr.append(td);
			if(biz.show.data.tr.children("td").length==2){
				table.append(biz.show.data.tr);
				biz.show.data.tr = $("<tr></tr>");
			};
			return td.children("span");
		},	
		
		addUserInfo:function(data,table,tr){
			
			if(table==undefined){
				table = biz.show.data.table;
			}
			if(tr!=undefined){
				biz.show.data.tr = tr;
			}
			var th = $("<th></th>");
			th.text(data.alias+":");
			var td = $("<td></td>");									
			var name = data.id?data.id+"&"+biz.show.data.taskId:"";
			td.html("<span class='fslTextBoxR' name='"+name+"'></span>");
			biz.show.data.tr.append(th);
			biz.show.data.tr.append(td);
			if(biz.show.data.tr.children("td").length==2){
				table.append(biz.show.data.tr);
				biz.show.data.tr = $("<tr></tr>");
			};
			return td.children("span");
		},
		appendTd:function(table,tr){
			if(table==undefined){
				table = biz.show.data.table;
			}
			if(tr!=undefined){
				biz.show.data.tr = tr;
			}
			if(biz.show.data.tr.children("td").length==1&&biz.show.data.tr.children("td").attr("colspan")!=3){
				var th = $("<th></th>");
				var td = $("<td></td>");
				biz.show.data.tr.append(th);
				biz.show.data.tr.append(td);
				table.append(biz.show.data.tr);
				biz.show.data.tr = $("<tr></tr>");
			}
			return biz.show.data.tr;
		},
		/**
		 * 处理方式
		 */
		listByTreatment:function(list){
			var treatmentid = null;
			for(var i=0;i<list.length;i++){
				if(list[i].name=="treatment"){
					treatmentid = list[i].id;
					break;
				}
			}
			var array = [];
			if(treatmentid!=null){
				var treatment = null;
				var serviceInfo = biz.detail.serviceInfo;
				for(var i in serviceInfo){
					if(treatmentid==serviceInfo[i].variable.id){
						treatment = serviceInfo[i].value;
						break;
					}
				}
				if(treatment==null){
					return list;
				}
				for(var i=0;i<list.length;i++){
					if(list[i].variableGroup==undefined){
						return list;
					}
					var groups = list[i].variableGroup.split(",");
					for(var j=0;j<groups.length;j++){
						if(groups[j].trim()==treatment.trim()||list[i].name=="treatment"){
							array.push(list[i]);
							break;
						}
					}
				}
			}else{
				array = list;
			}
			return array;
		},
		/**
		 * 交维工作
		 */
		addJwWork:function(data,table,tr){
			if(table==undefined){
				table = biz.show.data.table;
			}
			if(tr!=undefined){
				biz.show.data.tr = tr;
			}
			var th = $("<th></th>");
			th.html("交维工作:");
			var td = $("<td colspan='3'></td>");
			var labels = {
					"功能答疑":["台账","常见问题FAQ","用户操作手册","系统需求规格说明书","系统概要设计说明书","系统详细设计方案","版本发布说明","数据字典","接口清单","第三方支撑说明","故障处理知识库","安装部署手册"],
					"数据维护":["台账","运维操作手册","第三方支撑说明","安装部署手册","日志解读说明书","接口清单","接口规范说明书"],
					"接口接入":["台账","运维操作手册","第三方支撑说明","安装部署手册","接口清单","接口规范说明书"],
					"例行检查":["系统健康检查操作指南","健康检查报告","日志解读说明书"],
					"故障处理":["台账","故障处理知识库","运维操作手册","第三方支撑说明","安装部署手册","故障处理报告","日志解读说明书"],
					"应急演练":["台账","应急演练方案","第三方支撑说明","应急演练报告","日志解读说明书"],
					"节日保障":["台账","节日保障方案","第三方支撑说明"],
					"系统备份":["台账","节日保障方案","第三方支撑说明"],
					"发布部署":["部署升级方案","版本发布说明","第三方支撑说明","日志解读说明书"],
					"容量管理":["第三方支撑说明"],
					"安全整改":["第三方支撑说明"],
					"培训计划":["培训计划"]
			};
			var trs = biz.show.table.jwWork.addCheckBox(td,labels,"jwWork");			
			biz.show.data.tr.append(th);
			biz.show.data.tr.append(td);
			table.append(biz.show.data.tr);
			for(var i=0;i<trs.length;i++){
				table.append(trs[i]);
			}
			biz.show.data.tr = $("<tr></tr>");
			return biz.show.data.tr;
		},
		/**
		 * 子单
		 */
		addSonBiz:function(data,table,tr){
			if(table==undefined){
				table = biz.show.data.table;
			}
			if(tr!=undefined){
				biz.show.data.tr = tr;
			}
			biz.show.table.appendTd();
			var th = $("<th></th>");
			th.text(data.alias+":");
			var td = $("<td colspan='3'></td>");
			var div = $("<div></div>");
			td.append(div);
			biz.show.data.tr.append(th);
			biz.show.data.tr.append(td);
			table.append(biz.show.data.tr);
			biz.show.data.tr = $("<tr></tr>");
			td = $("<td colspan='4' style='padding:0;'></td>");
			var biztable = $("<table style='width:100%;border:hidden;' class='table base-table table-striped'></table>");
			td.append(biztable);
			biz.show.data.tr.append(td);
			table.append(biz.show.data.tr);
			biz.show.data.tr = $("<tr></tr>");
			biz.show.table.sonBiz.loadSonBiz(biztable,div,data.data);
			return biz.show.data.tr;
		},
		/**
		 * 附件
		 */
		addFile:function(annexs,table,tr){
		    if(annexs)
		        biz.show.data.annexs = annexs;
	        if(!table)
                table = biz.show.data.table;
            if(tr!=undefined){
                biz.show.data.tr = tr;
            }
		    biz.show.table.appendTd();
            var th = $("<th></th>");
            th.text("相关附件:");
            var td = $("<td colspan='3'></td>");   
            if(biz.show.data.annexs&&biz.show.data.annexs!="")
                for(var i=0;i<biz.show.data.annexs.length;i++){
                	if(annexs[i].fileCatalog==null||annexs[i].fileCatalog==""||annexs[i].fileCatalog=="uploadFile"){
                		td.append("<span style='margin-right:10px;display:block;'><a href='"+path+"/biz/download?id="+annexs[i].id+"'>"+annexs[i].name+"</a></span>");
                	}
                }
            if(td.children().length==0)
                td.text("无");                            
            biz.show.data.tr.append(th);
            biz.show.data.tr.append(td);
            table.append(biz.show.data.tr);
		},
		
		addReqFiles : function(data){
			
			$.ajax({
				url : path +'/biz/getBizFile',
				type :'post',
				data : {
					bizId : bizId,
					taskId : data.taskId,
					name : data.variable.name
				},
				success : function(result){
					if(result){
						for(var i=0;i<result.length;i++){
							var span = $("<span style='margin-right:10px;display:block;'><a href='"+path+"/biz/download?id="+result[i].id+"'>"+result[i].name+"</a></span>");
							$("span[name='"+data.variable.id+"&"+data.taskId+"']").append(span);
						}
					}else{
						$("span[name='"+data.variable.id+"&"+data.taskId+"']").html('无');
					}
				}
			});
		},
		/**
		 * 必传附件
		 */
		addreqFile:function(list,annexs){
			for(var i=0;i<list.length;i++){
				if(list[i].viewComponent=="REQUIREDFILE"){
					var s = new Set();
					if(annexs){
						for(var j=0;j<annexs.length;j++){
							if(list[i].name==annexs[j].fileCatalog){
								var span = $("<span style='margin-right:10px;display:block;'><a href='"+path+"/biz/download?id="+annexs[j].id+"'>"+annexs[j].name+"</a></span>");
								var taskId = (annexs[j].taskId==null)?"undefined":annexs[j].taskId;
								var name = list[i].id+"&"+taskId;
								s.add(name);
								$("body").find("span[name='"+name+"']").append(span);
							}
						}
					}
					s.forEach(function (item) {
						if($("body").find("span[name='"+item+"']").children().length==0)
							$("body").find("span[name='"+item+"']").append("无");
					});
				}			
			}
		},
		/**
		 * 确认人信息
		 */
        addConfirmUser:function(data,table,tr){
            if(table==undefined){
                table = biz.show.data.table;
            }
            if(tr!=undefined){
                biz.show.data.tr = tr;
            }
            var list = [
            {
                text:"处理人",
                name:"user"
            },{
                text:"联系方式",
                name:"mobile"
            },{
                text:"邮箱",
                name:"email"
            },{
                text:"确认时间",
                name:"date"
            },{
                text:"确认部门",
                name:"dep"
            }];
            for(var i=0;i<list.length;i++){
                var th = $("<th></th>");
                th.html(list[i].text);
                var td = $("<td></td>");
                var span = $("<span class='fslTextBoxR'></span>");
                span.attr("name",data.id+"&"+biz.show.data.taskId);
                span.attr("title",list[i].name);
                td.append(span);
                biz.show.data.tr.append(th);
                biz.show.data.tr.append(td);
                if(biz.show.data.tr.children("td").length==2){
                    table.append(biz.show.data.tr);
                    biz.show.data.tr = $("<tr></tr>");
                };
            }
        }
};

biz.show.table.confirmUser = {
    setConfirmUserValue:function(data){
        var value = eval("("+data.value+")");
        for(var key in value){
            $("span[name='"+data.variable.id+"&"+data.taskId+"'][title='"+key+"']").html(value[key]);
        }
    }
};
/**
 * 用户信息相关
 */
biz.show.table.userInfo = {
		
	setUserName : function(serviceInfo){
		$.ajax({
			url : path +'/actBizConf/loadUsersByUserName',
			type : 'post',
			async : false,
			data : {
				userName : serviceInfo.value
			},
			success : function(data){
				$("span[name='"+serviceInfo.variable.id+"&"+serviceInfo.taskId+"']").html(data.fullname);
				$("input[name='"+serviceInfo.variable.name+"Name']").val(data.fullname);
				$("input[name='"+serviceInfo.variable.name+"']").val(data.username);
			}
		});
	},

	setUserNames : function(serviceInfo){
		var userNames = serviceInfo.value.split(",");
		var userName = "";
		var fullName = "";
		for(var i=0;i<userNames.length;i++){
			if(userNames[i]!=""){
				$.ajax({
					url : path +'/actBizConf/loadUsersByUserName',
					type : 'post',
					async : false,
					data : {
						userName : userNames[i]
					},
					success : function(data){
						fullName += data.fullname+",";
						userName += data.username+",";
					}
				});
			}
		}
		$("span[name='"+serviceInfo.variable.id+"&"+serviceInfo.taskId+"']").html(fullName);
		$("input[name='"+serviceInfo.variable.name+"Name']").val(fullName);
		$("input[name='"+serviceInfo.variable.name+"']").val(userName);
	}
};

biz.show.table.jwWork = {
		addCheckBox:function(td,labels,name){
			var trs = [];
			var num = 1;
			for(var key in labels){
				var checkbox = $("<input type='checkbox' disabled='disabled'>");
				checkbox.attr("name",name);
				checkbox.val(num);
				td.append(checkbox);
				td.append(key);
				if(i%6==5){
					td.append("<br>");
				}
				var tr = $("<tr>");
				var th = $("<th>");
				th.text(key+":");
				var td2 = $("<td colspan='3'>");
				for(var i=0;i<labels[key].length;i++){
					checkbox = $("<input type='checkbox' disabled='disabled'>");
					checkbox.attr("name",name+num);
					checkbox.val(i+1);
					td2.append(checkbox);
					td2.append(labels[key][i]);
					if(i%6==5){
						td2.append("<br>");
					}
				}
				tr.append(th);
				tr.append(td2);
				trs.push(tr);
				num++;
			}
			return trs;
		},
		setJwWorkValue:function(value){
			$(":checked[name^='jwWork']").attr("checked",false);
			if(value==null||value==undefined||value=="")
				return;
			var jwWork = value.split(";");
			for(var i=0;i<jwWork.length;i++){
				var temp = jwWork[i].split(":");
				$("[name='jwWork'][value='"+temp[0]+"']")[0].checked = true;
				var detail = temp[1].split(",");
				for(var j=0;j<detail.length;j++){
					$("[name='jwWork"+temp[0]+"'][value='"+detail[j]+"']")[0].checked = true;
				}
			}
		}
};

biz.show.table.sonBiz = {
		loadSonBiz:function(table,div,data){
			table.bootstrapTable({
				data : data,
				classes:"table-no-bordered",
				columns : [ {
					field : "bizId",
					title : "工单号",
					align : "left",
					formatter : function(value, row, index) {
						var url = path + "/biz/" + row.id;
						return "<a onclick=\"window.open('"+url+"');\">"+value+"</a>";
					}
				}, {
					field : "bizType",
					title : "工单类型",
					align : "left"
				}, {
					field : "title",
					title : "工单标题",
					align : "left"
				}, {
					field : "createUser",
					title : "创建人",
					align : "left"
				}, {
					field : "createTime",
					title : "创建时间",
					align : "left"
				}, {
					field : "status",
					title : "工单状态",
					align : "left"
				}, {
					field : "taskAssignee",
					title : "当前处理人",
					align : "left"
				}],
				onLoadSuccess:function(data){
					for(var i=0;i<data.rows.length;i++){
						var checkbox = $("<input type='checkbox'>");
						var span = $("<span style='display: inline-block;'>");
						span.append(checkbox);
						span.append(data.rows[i].bizId);
						div.append(span);
					}
				}
			});
			for(var i=0;i<data.length;i++){
				var checkbox = $("<input type='checkbox'>");
				var span = $("<span style='display: inline-block;'>");
				span.append(checkbox);
				span.append(data[i].bizId);
				div.append(span);
			}
		}
};