$.namespace("biz.edit.form");
$.namespace("biz.config");

//变更配置项的树图
biz.config.tree = {
		data:{},
		init:function(option){
		    
			biz.config.tree.data.ele = option==undefined?"":option.ele==undefined?"":option.ele;
			biz.config.tree.data.iframe = option==undefined?"":option.iframe==undefined?"":option.iframe;
			biz.config.tree.createTree();
			$(biz.config.tree.data.ele+" ul li").click(function() {
				$(this).next("ul").toggle();
			});
			
			$(biz.config.tree.data.ele+" ul.submenu2 li").click(function() {
				if($(this).attr("class")!="on"){
					$(biz.config.tree.data.ele+" ul.submenu2 li").removeAttr("class");
					$(this).attr("class","on");
				}
			});
				
			$(biz.config.tree.data.ele+" ul.submenu>li>a").click(function(){
				if($(this).children("span").attr("class")=="down"){
					$(this).children("span").attr("class","up");
				}else{
					$(this).children("span").attr("class","down");
				}
			});
		},
		createTree:function(){
			$.ajax({
				url:path+"/workflow/modelconfigList",
				async:false,
				success:function(data){
					var tree = $("<ul class='submenu'>");
					$(biz.config.tree.data.ele).append(tree);
					var normal = biz.config.tree.createType(tree,"一般设备配置");
					var basis = biz.config.tree.createType(tree,"基础数据配置");
					var list = data.list;
					for(var i=0;i<list.length;i++){
						var li;
						if(list[i].mcType){
							li = biz.config.tree.createNode(basis,list[i]);
						}else{
							li = biz.config.tree.createNode(normal,list[i]);
						}
						if(i==0){
							li.addClass("on");
							parent[biz.config.tree.data.iframe].location.href = path+"/biz/selectConfigItem?variableId="+biz.edit.form.config.vid+"&configId="+list[i].id;
						}
					}
					if(normal.children("li").length==0){
						normal.prev("li").remove();
						normal.remove();
					}
					if(basis.children("li").length==0){
						basis.prev("li").remove();
						basis.remove();
					}
				}
			});
		},
		createType:function(tree,text){//创建一般设备配置等分类结点返回ul
			var li = $("<li>");
			var a = "<a>"+text+"<span class='up'></span></a>";
			li.append(a);
			var ul = $("<ul class='submenu2'>");
			tree.append(li);
			tree.append(ul);
			return ul;
		},
		createNode:function(type,data){//创建配置项结点返回ul
			var li = $("<li>");
			li.attr("id","treeLi_"+data.id);
			var a = "<a href='"+path+"/biz/selectConfigItem?variableId="+biz.edit.form.config.vid+"&configId="+data.id+"' target='"+biz.config.tree.data.iframe+"'>"+data.name+"</a>";
			li.append(a);
			type.append(li);
			return li;
		}
};

//人员组件相关方法
biz.edit.form.memberbox ={
		data :{},
		createMemberContainer:function(){
			var container = $("<div id='memberContainer'>");
			container.hide();
			var import_form = $("<div class='import_form'>");
			var title = "<h2 class='white_tit'><span class='white_tit_icon'></span>查询条件"+
			"<a role='button' data-toggle='collapse' href='#memberContainerQuerycollapse' aria-expanded='true' class='gray_drop'></a></h2>";
			import_form.html(title);
			var listtable_wrap = $("<div class='listtable_wrap panel-collapse collapse in' id='memberContainerQuerycollapse' role='tabpanel'>");
			var table = $("<table cellpadding='0' cellspacing='0' class='listtable'>");
			var tr = $("<tr>");
			var showDept = biz.edit.form.memberbox.data.showDept;
			if(showDept){
				var th = $("<th>");
				th.text("部门：");
				tr.append(th);
				var td = $("<td>");
				var sectorCombo = $("<input id='sectorCombo' type='text' readonly value='' class='fslTextBox' style='width:120px;' onclick='biz.edit.form.memberbox.showMenu();' />"),
				sectorComboVal = $("<input id='sectorComboVal' type='hidden'/>");
				td.append(sectorCombo);
				td.append(sectorComboVal);
				tr.append(td);
				th = $("<th>");
				th.text("姓名：");
				tr.append(th);
				td = $("<td colspan='1'>");
				input = $("<input name='cnname' type='text' class='fslTextBox'>");
				td.append(input);
				tr.append(td);
				table.append(tr);
				td = $("<td colspan='1' style='text-align:left;'>");
				var a = "<a onclick='biz.edit.form.memberbox.search()' class='dt_btn'>查询</a>";
				td.append(a);
				tr.append(td);
				table.append(tr);
			}else{
				var th = $("<th>");
				th.text("姓名：");
				tr.append(th);
				td = $("<td colspan='1'>");
				input = $("<input name='cnname' type='text' class='fslTextBox'>");
				td.append(input);
				var a = "<a onclick='biz.edit.form.memberbox.search()' class='dt_btn'>查询</a>";
				td.append(a);
				tr.append(td);
				table.append(tr);
				table.append(tr);
			}
			listtable_wrap.append(table);
			container.append(import_form);
			container.append(listtable_wrap);
	
			import_form = $("<div class='import_form'>");
			title = "<h2 class='white_tit'><span class='white_tit_icon'></span>查询结果"+
			"<a role='button' data-toggle='collapse' href='#memberResconfigcollapse' aria-expanded='true' class='gray_drop'></a></h2>";
			import_form.html(title);
			listtable_wrap = $("<div class='listtable_wrap panel-collapse collapse in' id='memberResconfigcollapse' role='tabpanel'>");
			div = $("<div id='member_table_div'>");
			table = $("<table id='member_table' />");
			div.append(table);
			listtable_wrap.append(div);
			container.append(import_form);
			container.append(listtable_wrap);
			
			div=$('<div id="sectorMenuContent" class="menuContent" style="display:none; position: absolute;">');
			var ul = $('<ul id="sectorTree" class="ztree" style="margin-top:0; width:180px; height: 300px;">');
			div.append(ul);
			container.append(div);
			return container;
		},
		openMemberContainer:function(){
			var inputname = biz.edit.form.memberbox.data.inputname;
			if($('#memberContainer').is(":hidden")){
				var width = $("[name='"+inputname+"']").parent('td').css('width');
				width = width.substring(0,width.length-2)-10;
				$('#memberContainer').css('width',(parseInt(width)+2));
				$('#memberContainer').show();
				biz.edit.form.memberbox.sectorInit();
				biz.edit.form.memberbox.queryMember();
			}else{
				$('#memberContainer').hide();
			}
		},
		setting:{
			view: {
				dblClickExpand: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeClick: function(treeId, treeNode) {
					var check = (treeNode && !treeNode.isParent);
					},
				onClick: function(e, treeId, treeNode){
					var zTree = $.fn.zTree.getZTreeObj("sectorTree");
					nodes = zTree.getSelectedNodes(),
					vId="",
					v = "";
					nodes.sort(function compare(a,b){return a.id-b.id;});
					for (var i=0, l=nodes.length; i<l; i++) {
						v += nodes[i].name + ",";
						vId+=nodes[i].id+",";
					}
					if (v.length > 0 ) v = v.substring(0, v.length-1);
					if (vId.length > 0 ) vId = vId.substring(0, vId.length-1);
					var cityObj = $("#sectorCombo"),cityValue = $('#sectorComboVal');
					if(vId==""){
                        cityObj.val("");
                        cityValue.val("");
                    }else{
                        cityObj.val(v);
                        cityValue.val(vId);
                    } 
					biz.edit.form.memberbox.hideMenu();
				}
			}
		},
		zNodes:null,
		showMenu:function() {
			var cityObj = $("#sectorCombo");
			var cityOffset = $("#sectorCombo").offset();
			$("#sectorMenuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

			$("body").bind("mousedown", biz.edit.form.memberbox.onBodyDown);
		},
		hideMenu:function() {
			$("#sectorMenuContent").fadeOut("fast");
			$("body").unbind("mousedown", biz.edit.form.memberbox.onBodyDown);
		},
		onBodyDown:function(event) {
			if (!(event.target.id == "sectorCombo" || event.target.id == "sectorMenuContent" || $(event.target).parents("#sectorMenuContent").length>0)) {
				biz.edit.form.memberbox.hideMenu();
			}
		},
		sectorInit:function(){
			$.fn.zTree.init($("#sectorTree"), biz.edit.form.memberbox.setting, biz.edit.form.memberbox.zNodes);
			
			//初始化选择第一个
			var zTree = $.fn.zTree.getZTreeObj("sectorTree");
			zTree.addNodes(null,0,{
                id : "",
                name : "选空",
                nocheck : true
            });
			var nodes = zTree.getNodes();    
			zTree.selectNode(nodes[0]); 
			zTree.setting.callback.onClick(null, zTree.setting.treeId, nodes[0]);
		},
		loadSectorBox:function(){
			$.ajax({
				type:"post",
				url:path+"/bizHandle/loadSectors",
				async:false,
				success:function(data){
					if(data!=null && data.success && data.obj != null){
						biz.edit.form.memberbox.zNodes = data.obj;
					}else{
					}
				}
			});
		},
		queryMember:function(){
			var isSingle = biz.edit.form.memberbox.data.isSingle;
			$("#member_table").bootstrapTable({
				url : path + '/bizHandle/loadMembers',
				method : 'get',
				striped: true,
				queryParams : function queryParams(params) {
					params.sector = $('#memberContainer #sectorComboVal').val();
					params.portalname = $('#memberContainer input[name="portalname"]').val();
					params.cnname = encodeURI($('#memberContainer input[name="cnname"]').val());
					params.bizId = bizId;
					if(!biz.edit.form.memberbox.data.showDept){
						params.headQuarter = 'headQuarter';
					}
					return params;
				},
				sidePagination : 'server',
				clickToSelect : true,
				pagination : true,
				singleSelect: isSingle,
				pageSize : 10,
				columns : [
				           	{"field":"state",checkbox : true},
				           	{"field":"fullname","title":"用户名称" ,"align": "center"},
				           	{"field":"username","title":"账号" ,"align": "center"},
                            {field:"structure_name",title:"部门名称",align:"center"}
				      ]
				,onCheck:this.onCheck,
				onUncheck: function(row){
					biz.edit.form.memberbox.onUnCheck(row);
				}
			});
		},
		
	    onCheck:function(){//表格选择人员，人员值加上选择人员，隔开
	    	
	    	var rows = $("#member_table").bootstrapTable('getSelections');
	    	var username = $("[name='"+biz.edit.form.memberbox.data.input+"']").val();;
	    	var fullname = $("[name='"+biz.edit.form.memberbox.data.inputname+"']").val();
	    	if(rows.length>0){
	    		for(var i = 0; i<rows.length ; i++){
	    			if(username.indexOf(rows[i].username+',')==-1){
		    			username+=rows[i].username+',';
		    			fullname+=rows[i].fullname+',';
	    			}
	    		}
	    	}
	        $("[name='"+biz.edit.form.memberbox.data.input+"']").val(username);
	        $("[name='"+biz.edit.form.memberbox.data.inputname+"']").val(fullname);
	    },
	    
	    onUnCheck:function(row){//表格选择人员，人员值加上选择人员，隔开
	    	
    	    var oldusername = $("[name='"+biz.edit.form.memberbox.data.input+"']").val();
	        var oldfullname = $("[name='"+biz.edit.form.memberbox.data.inputname+"']").val();
	        $("[name='"+biz.edit.form.memberbox.data.input+"']").val(oldusername.replace(row.username+",",""));
	        $("[name='"+biz.edit.form.memberbox.data.inputname+"']").val(oldfullname.replace(row.fullname+",",""));
	        this.onCheck();
	    },
		
		search:function(){
			var sector = $('#memberContainer #sectorComboVal').val();
			$('#member_table').bootstrapTable('refresh',{method:'post',url : path + '/bizHandle/loadMembers',queryParams : function queryParams(params) {
				params.sector = $('#memberContainer #sectorComboVal').val();
				params.portalname = $('#memberContainer input[name="portalname"]').val();
				params.cnname = encodeURI($('#memberContainer input[name="cnname"]').val());
				return params;
			}});
		},
		checkMember:function(){
			var rows = $("#member_table").bootstrapTable("getSelections");
			if(rows.length<1||rows.length>1){
				bsAlert("提示","请选择一个选项");
			}
			var row = rows[0];
			$("[name='"+biz.edit.form.memberbox.data.input+"']").val(row.id);
		    $("[name='"+biz.edit.form.memberbox.data.inputname+"']").val(row.fullname);
		},
		clearMember:function(){
			$("[name='"+biz.edit.form.memberbox.data.input+"']").val('');
		    $("[name='"+biz.edit.form.memberbox.data.inputname+"']").val('');
		}
};
//人员及联系方式联动相关方法
biz.edit.form.memberLinkage ={
        data:{},
        currentNode:null,
        createMemberContainer:function(containerName){//创建容器html代码
            var container = $("<div id='"+containerName+"memberLinkageContainer'>");
            container.hide();
            var import_form = $("<div class='panel panel-box' style='margin:10px 0 5px'>");
            var title = "<div class='panel-heading'>查询条件"+
            "<a role='button' data-toggle='collapse' href='javascript:void(0)' onclick='biz.edit.form.memberLinkage.openMemberContainer(\""+containerName+"\")' aria-expanded='true' class='gray_drop'></a></div>";
            import_form.html(title);
            var listtable_wrap = $("<div class='panel-body panel-collapse collapse in' id='"+containerName+"memberLinkageContainerQuerycollapse' role='tabpanel'>");
            var table = $("<table cellpadding='0' cellspacing='0' class='sea-form mr5'>");
            var tr = $("<tr>");
            var th = $("<th>");
            th.text("部门：");
            tr.append(th);
            var td = $("<td>");
            var sectorCombo = $("<input id='sectorCombo' type='text' readonly value='' class='fslTextBox' style='width:120px;' onclick='biz.edit.form.memberLinkage.showMenu(this);' />"),
            sectorComboVal = $("<input id='sectorComboVal' type='hidden'/>");
            td.append(sectorCombo);
            td.append(sectorComboVal);
            tr.append(td);
            th = $("<th>");
            th.text("姓名：");
            tr.append(th);
            td = $("<td colspan='1'>");
            input = $("<input style='margin-right: 20px;' name='cnname' type='text' class='fslTextBox'>");
            var a = $("<a onclick='biz.edit.form.memberLinkage.search(\""+containerName+"\")' class='btn btn-y'>查询</a>");
            td.append(input,a);
            tr.append(td);
            table.append(tr);
            listtable_wrap.append(table);
            import_form.append(listtable_wrap);
            container.append(import_form);
    
            import_form = $("<div class='panel panel-ex'>");
            title = "<div class='panel-heading'>查询结果</div>";
            import_form.html(title);
            listtable_wrap = $("<div class='panel-body panel-collapse collapse in' id='memberLinkageResconfigcollapse' role='tabpanel'>");
            div = $("<div id='memberLinkage_table_div' class='base-table-wrap'>");
            table = $("<table id='"+containerName+"memberLinkage_table' class='base-table table-striped' />");
            div.append(table);
            listtable_wrap.append(div);
            import_form.append(listtable_wrap);
            container.append(import_form);
            
            //部门树图
            if($("#sectorMenuContent").length==0){
                div=$('<div id="sectorMenuContent" class="menuContent" style="display:none; position: absolute;">');
                var ul = $('<ul id="sectorTree" class="ztree" style="margin-top:0; width:180px; height: 300px;">');
                div.append(ul);
                $("body").append(div);
            }
           
            return container;
        },
        openMemberContainer:function(containerName){//显示容器，带适应
            if($("#"+containerName+"memberLinkageContainer").is(":hidden")){
                var width = $("#"+containerName+"memberLinkageContainer").parent('td').css('width');
                width = width.substring(0,width.length-2)-10;
                $("#"+containerName+"memberLinkageContainer").css('width',(parseInt(width)+2));
                $("#"+containerName+"memberLinkageContainer").show();
                biz.edit.form.memberLinkage.sectorInit();
                
                biz.edit.form.memberLinkage.queryMember(containerName);
            }else{
                $("#"+containerName+"memberLinkageContainer").hide();
            }
        },
        openRoleContainer:function(){//显示角色树图
             biz.edit.form.memberLinkage.roleTree.showMenu();
        },
        setting:{
            view: {
                dblClickExpand: false
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                beforeClick: function(treeId, treeNode) {
                    var check = (treeNode && !treeNode.isParent);
                    },
                onClick: function(e, treeId, treeNode){
                    //点击部门树多选，隔开
                    var zTree = $.fn.zTree.getZTreeObj("sectorTree");
                    nodes = zTree.getSelectedNodes(),
                    vId="",
                    v = "";
                    nodes.sort(function compare(a,b){return a.id-b.id;});
                    for (var i=0, l=nodes.length; i<l; i++) {
                        v += nodes[i].name + ",";
                        vId+=nodes[i].id+",";
                    }
                    if (v.length > 0 ) v = v.substring(0, v.length-1);
                    if (vId.length > 0 ) vId = vId.substring(0, vId.length-1);
                    var cityObj = biz.edit.form.memberLinkage.currentNode,cityValue = cityObj.next();
                    if(vId==""){
                        cityObj.val("");
                        cityValue.val("");
                    }else{
                        cityObj.val(v);
                        cityValue.val(vId); 
                        
                    } 
                    biz.edit.form.memberLinkage.hideMenu();
                }
            }
        },
        zNodes:null,
        showMenu:function(element) {//显示树图
        	biz.edit.form.memberLinkage.currentNode = $(element);
            var cityObj = biz.edit.form.memberLinkage.currentNode;
            var cityOffset = biz.edit.form.memberLinkage.currentNode.offset();
            $("#sectorMenuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

            $("body").bind("mousedown", biz.edit.form.memberLinkage.onBodyDown);
        },
        hideMenu:function() {//隐藏树图
            $("#sectorMenuContent").fadeOut("fast");
            $("body").unbind("mousedown", biz.edit.form.memberLinkage.onBodyDown);
        },
        onBodyDown:function(event) {//点击其他区域隐藏树图
            if (!(event.target.id == "sectorCombo" || event.target.id == "sectorMenuContent" || $(event.target).parents("#sectorMenuContent").length>0)) {
                biz.edit.form.memberLinkage.hideMenu();
            }
        },
        sectorInit:function(){//初始化树图
            $.fn.zTree.init($("#sectorTree"), biz.edit.form.memberLinkage.setting, biz.edit.form.memberLinkage.zNodes);
            
            //初始化选择第一个
            var zTree = $.fn.zTree.getZTreeObj("sectorTree");
            zTree.addNodes(null,0,{
                id : "",
                name : "选空",
                nocheck : true
            });
            var nodes = zTree.getNodes();    
            zTree.selectNode(nodes[0]); 
        },
        loadSectorBox:function(){
            $.ajax({
                type:"post",
                url:path+"/bizHandle/loadSectors",
                async:false,
                success:function(data){
                    if(data!=null && data.success && data.obj != null){
                        biz.edit.form.memberLinkage.zNodes = data.obj;
                    }else{
                    }
                }
            });
        },
        queryMember:function(containerName){//定义表格
            $("#"+containerName+"memberLinkage_table").bootstrapTable({
                url : path + '/bizHandle/loadMembers',
                method : 'get',
                striped: true,
                queryParams : function queryParams(params) {
                    params.sector = $("#"+containerName+"memberLinkageContainer #sectorComboVal").val();
                    params.portalname = $("#"+containerName+"memberLinkageContainer input[name='portalname']").val();
                    params.cnname = encodeURI($("#"+containerName+"memberLinkageContainer input[name='cnname']").val());
                    return params;
                },
                sidePagination : 'server',
                clickToSelect : true,
                pagination : true,
                singleSelect: true,
                pageSize : 10,
                columns : [
                            {field:"state","checkbox" : true,width:"20px"},
                            {field:"fullname","title":"用户名称" ,"align": "center"},
                            {"field":"username","title":"账号" ,"align": "center"},
                            {field:"structure_name",title:"部门名称",align:"center"}
                      ]
                ,onCheck:function(row){
                    var inputName = biz.edit.form.memberLinkage.data[containerName+"inputName"];
                    $("input[name='"+inputName+"']").val(row.username);
                    inputName = inputName+'Name';
                    $("input[name='"+inputName+"']").val(row.fullname);
                    $("input[name='"+biz.edit.form.memberLinkage.data[containerName+"mobileName"]+"']").val(row.mobile);
                    $("input[name='"+biz.edit.form.memberLinkage.data[containerName+"department"]+"']").val(row.structure_name);
                    $("input[name='"+biz.edit.form.memberLinkage.data[containerName+"email"]+"']").val(row.email);
                },
                onUncheck:function(){
                    biz.edit.form.memberLinkage.clearMember(name);
                }
            });
            
        },
        search:function(containerName){//查询
            var sector = $("#"+containerName+"memberLinkageContainer #sectorComboVal").val();
            $("#"+containerName+"memberLinkage_table").bootstrapTable('refresh',{method:'post',url : path + '/bizHandle/loadMembers',queryParams : function queryParams(params) {
                params.sector = $("#"+containerName+"memberLinkageContainer #sectorComboVal").val();
                params.portalname = $("#"+containerName+"memberLinkageContainer input[name='portalname']").val();
                params.cnname = encodeURI($("#"+containerName+"memberLinkageContainer input[name='cnname']").val());
                return params;
            }});
        },
        checkMember:function(){//选择人员
            var rows = $("#"+containerNam+"memberLinkage_table").bootstrapTable("getSelections");
            if(rows.length<1||rows.length>1){
                bsAlert("提示","请选择一个选项");
            }
            var row = rows[0];
            $("input[name='"+biz.edit.form.memberLinkage.data.inputName+"']").val(row.fullname);
            $("input[name='"+biz.edit.form.memberLinkage.data.mobileName+"']").val(row.mobile);
        },
        clearMember:function(containerName){//清除人员
            var inputName = biz.edit.form.memberLinkage.data[containerName+"inputName"];
            $("input[name='"+inputName+"']").val('');
            inputName = inputName+'Name';
            $("input[name='"+inputName+"']").val('');
            $("input[name='"+biz.edit.form.memberLinkage.data[containerName+"mobileName"]+"']").val('');
            $("input[name='"+biz.edit.form.memberLinkage.data[containerName+"department"]+"']").val('');
            $("input[name='"+biz.edit.form.memberLinkage.data[containerName+"email"]+"']").val('');
        },
        roleTree:{//角色树相关属性
            treeId : null,
            curAsyncCount : 0,
            getTree : function() {
                return $.fn.zTree.getZTreeObj(this.treeId);
            },
            init : function(id) {
                this.treeId = id;
                // 初始化角色树
                $.fn.zTree.init($('#' + id), this.setting, this.zNodes);
                // 初始化树图后展开根节点
                var zTree = $.fn.zTree.getZTreeObj(id);
                var rootNode = zTree.getNodeByParam('id', 'root', null);
                zTree.expandNode(rootNode, true);
            },
            setting : {
                async : {
                    dataType : 'json',
                    url : path + '/serviceRoleConf/getRoleTree',
                    autoParam : [ 'id', 'name', 'attributes' ],
                    otherParam : {},
                    enable : true
                },
                view : {
                    fontCss : function(treeId, treeNode) {
                        return (!!treeNode.highlight) ? {
                            color : "#A60000",
                            "font-weight" : "bold"
                        } : {
                            color : "#333",
                            "font-weight" : "normal"
                        };
                    }
                },
                callback : {
                    onAsyncSuccess : function(event, treeId, treeNode, msg) {
                    },
                    onClick: function(e, treeId, treeNode){//点击选择设置值为GROUP:选择角色，联系方式设空
                        var inputName = biz.edit.form.memberLinkage.data.inputName;
                        $("input[name='"+inputName+"']").val("GROUP:"+treeNode.name);
                        inputName = inputName+'Name';
                        $("input[name='"+inputName+"']").val("GROUP:"+treeNode.name);
                        $("input[name='"+biz.edit.form.memberLinkage.data.mobileName+"']").val("");
                        $("#"+biz.edit.form.memberLinkage.data.name+"memberLinkage_table").bootstrapTable("uncheckAll");
                        biz.edit.form.memberLinkage.roleTree.hideMenu();
                    }
                }
            },
            zNodes : {
                id : 'root',
                pId : 0,
                name : "角色列表",
                nocheck : true,
                attributes : {
                    showItfDescr : false,
                    hidedown : false
                },
                isParent : true
            },
            showMenu:function() {
                var inputName = biz.edit.form.memberLinkage.data.inputName+'Name';
                var input = $(":input[name='"+inputName+"']");
                var inputOffset = input.offset();
                $("#roleMenuContent").css({left:inputOffset.left + "px", top:inputOffset.top + input.outerHeight() + "px"}).slideDown("fast");
                $("body").bind("mousedown", biz.edit.form.memberLinkage.roleTree.onBodyDown);
            },
            hideMenu:function() {
                $("#roleMenuContent").fadeOut("fast");
                $("body").unbind("mousedown", biz.edit.form.memberLinkage.roleTree.onBodyDown);
            },
            onBodyDown:function(event) {
                var inputName = biz.edit.form.memberLinkage.data.inputName+'Name';
                if (!(event.target.name == inputName || event.target.id == "roleMenuContent" || $(event.target).parents("#roleMenuContent").length>0)) {
                    biz.edit.form.memberLinkage.roleTree.hideMenu();
                }
            },
            loadRoleTree:function(){
                var inputName = biz.edit.form.memberLinkage.data.inputName+'Name';
                $("#roleMenuContent .ztree").width($("[name='"+inputName+"']").innerWidth()-10);
                biz.edit.form.memberLinkage.roleTree.init("roleTree");
            }
        }
};

//下拉框相关方法
biz.edit.form.combobox = {
		data :{},
		loadComboBox:function(select,params){//一般下拉框
			if(params) {
				var paramlist = params.split(",");
				biz.edit.form.combobox.addOption(select,paramlist);
			}
		},
		loadDictComboBox:function(select,params){//数据字典下拉框
			$.ajax({
				url:path+"/workflow/dictComboBoxList",
				type:"post",
				async:false,
				data:{dictName:params},
				success:function(data){
				    var defaultOption = $("<option>");
                        defaultOption.val('');
                        defaultOption.text('');
                        select.append(defaultOption);
					for(var i=0;i<data.length;i++){
						var option = $("<option>");
						option.val(data[i].NAME);
						option.text(data[i].NAME);
						select.append(option);
					}
				}
			});
		
		},
		//处理人下拉
		terminaluser:function(){
			biz.edit.form.combobox.data.select.empty();
			$.ajax({
				url:path+"/actBizConf/findBizSystemUserConf",
				type:"post",
				async:false,
				data : {
					bizType : key,
					bizId : bizId,
					userType : biz.edit.form.combobox.data.terminalUserType,
					systemType : biz.edit.form.combobox.data.systemType,
					systemName:$("[name='"+biz.edit.form.combobox.data.systemName+"']").val()
				},
				success:function(data){
					var option2 = $("<option>");
					for(var i=0;i<data.rows.length;i++){
						var option = $("<option>");
						option.val(data.rows[i].USERNAME);
						option.text(data.rows[i].FULLNAME);
						biz.edit.form.combobox.data.select.append(option);
					}
				}
			});
		},
		loadUserInfo : function(dataId,type){
			
			var userInfo = '';
			$.ajax({
				url : path + '/actBizModel/getUserInfo',
				type : 'post',
				data : {dataId:dataId,type:type},
				async:false,
				success : function(result){
					userInfo = result;
				}
			});
			return userInfo;
		},
		
		addOption:function(select,data){//根据数组生成下拉框选项
			for(var i=0;i<data.length;i++){
				var option = $("<option>");
				option.val(data[i]);
				option.text(data[i]);
				select.append(option);
			}
		},
		showTurnSend:function(ele){//服务厂商与转派方式联动
			$.ajax({
				url:path+"/serviceRoleConf/getRolesBySysName",
				type:"post",
				data:{
					sysName:$(ele).val()
				},
				success:function(data){
					var select = $("[name='serviceVendor']");
					select.empty();
					for(var i=0;i<data.length;i++){
						var option = $("<option>");
						option.val(data[i].roleCName);
						option.text(data[i].roleCName);
						select.append(option);
					}
				}
			});			
		},
        limitTimechange:function(){
        	var newtime = $dp.cal.getNewDateStr();
    		$("#onLineTime").attr('onFocus',"WdatePicker({dateFmt:\"yyyy-MM-dd HH:mm:ss\",minDate:\""+newtime+"\"})");
        },
		otherUrgencyLevel:function(ele){//紧急级别与最后解决时间联动
		    if(ele==undefined){
                ele = this;
            }else if(ele.originalEvent){
                ele = this;
            }
			if($(ele).val().replace(/(^\s*)|(\s*$)/g, "")=="其他"){
				$("input[name='base.limitTime']").attr("onfocus","WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:biz.edit.form.combobox.limitTimechange})");
			}else{
				var num = 0;
				switch($(ele).val().replace(/(^\s*)|(\s*$)/g, "")){
					case "低(7天)": num=7;break;
					case "中(3天)": num=3;break;
					case "高(1天)": num=1;break;
					default:var value = $(ele).val().replace(/(^\D*)|(\D*$)/g, "");num = parseInt(value?value:0);
				}
				var now = new Date();
				now.setDate(now.getDate()+num);  
				var input = $("<input type='text' class='fslTextBox' readonly='readonly'>");
				input.attr("name","base.limitTime");
				input.addClass("Wdate");
				input.val(now.Format("yyyy-MM-dd hh:mm:ss"));
				$("input[name='base.limitTime']").replaceWith(input);
	    		$("#onLineTime").attr('onFocus',"WdatePicker({dateFmt:\"yyyy-MM-dd HH:mm:ss\",minDate:\""+now.Format("yyyy-MM-dd hh:mm:ss")+"\"})");
			}
		}
};

//附件组件相关方法
biz.edit.form.file = {
		
		handleDiv:null,
		data : {},
		addFileInput:function(element,inputName){//打开一次附件上传框，生成一个文件域
			biz.edit.fileNumber++;
			if(!inputName)
				inputName = "uploadFile";
			$("#fileTd").children(":file").hide();
			var file = $("<input type='file' name='"+inputName+"' class='uploadFile left'/>");
			file.attr("id","file"+biz.edit.fileNumber);
			$("#fileTd").append(file);
			biz.edit.form.file.handleDiv = $(element).parent().parent().parent();
		},
		selectFile:function(){//触发隐藏文件域的点击事件，弹窗选择框
			$("#file"+(biz.edit.fileNumber)).click();
		},
		uploadFile:function(){//在附件单元格显示附件名，未入库
			
			var path = $("#file"+(biz.edit.fileNumber)).val();
			if(path!=""&&path!=undefined){
				var filename = path.substring(path.lastIndexOf("\\")+1);
				var span = "<span style='margin-right:10px;display:block;' id='spanfile"+biz.edit.fileNumber+"'>"+
				"<input type='checkbox'/>"+filename+"</span>";
				biz.edit.form.file.handleDiv.parent().append(span);
				biz.edit.form.file.handleDiv.parent().find("input:hidden").val("附件不为空");
			}
		},
		
		downLoadFile : function(){
		
			var fileName = encodeURI(biz.edit.form.file.data.downLoadFile);
			window.location.href = path + "/bizTemplateFile/downloadTemplate?&fileName="+encodeURIComponent(fileName)+"&bizType="+key+"&bizId="+bizId;
		},
		removeFile:function(element){//删除附件
			var checkbox = $(element).parent().parent().parent().parent().find(":checked");
			for(var i=0;i<checkbox.length;i++){
				var spanfile = checkbox.eq(i).parent().attr("id");
				$("#"+spanfile.substring(4)).remove();
				if(checkbox.eq(i).next("a").length>0){
				     if(confirm("是否删除已有附件"+checkbox.eq(i).next("a").text())){
                           //删除已入库附件没加上
				    	 var fileId = checkbox.eq(i).next("a").attr("id");
				    	 if(fileId&&fileId!=""){
				    		 $.ajax({
				    			 url:path+"/actBizConf/deleteFile",
				    		 	 data:{id:fileId}
				    		 });
				    	 }
                     }else{
                         checkbox = checkbox.not(checkbox[i]);
                         continue;
                     }
				}
			}
			checkbox.parent().remove();
			biz.edit.form.file.handleDiv = $(element).parent().parent().parent();
			var existCheckBox = biz.edit.form.file.handleDiv.parent().find(":checkbox");
			if(existCheckBox.length==0)
				biz.edit.form.file.handleDiv.parent().find("input:hidden").val("");
		},
		creatFileWindow:function(){//创建容器html代码
			var modal = $("<div class='modal fade' id='selectFile' tabindex='-1' role='dialog' aria-labelledby='myModalLabel'>");
			var dialog = $("<div class='modal-dialog'>");
			var content = $("<div class='modal-content'>");
			var header = $("<div class='modal-header'>");
			header.append('<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>');
			var title = "<h5 class='modal-title'>选择</h5>";
			header.append(title);
			var body = $("<div class='modal-body'>");
			var table = $("<table cellpadding='0' cellspacing='0' style='margin:50px auto'>");
			var tr = $("<tr>");
			var td = $("<td>");
			td.text("请选择文件：");
			tr.append(td);
			td = $("<td id='fileTd'>");
			tr.append(td);
			td = $("<td>");
			td.html("<a onclick='biz.edit.form.file.uploadFile()' data-dismiss='modal' class='dt_btn' style='margin-left:5px;'>上传</a>");
			tr.append(td);
			table.append(tr);
			tr = $("<tr>");
			td1 = $("<td>");
			td2 = $("<td>");
			td3 = $("<td>");
			tr.append(td1,td2,td3);
			table.append(tr);
			var p = $("<p class='help-block'>文件大小不能超过10M</p>");
			td2.append(p);
			tr = $("<tr>");
			td = $("<td colspan=3>");
			div = $("<div class='btn_list'>");
			div.html("<a data-dismiss='modal' onclick='history.go(-1)'>返回</a>");
			td.append(div);
			tr.append(td);
			table.append(tr);
			body.append(table);
			content.append(header);
			content.append(body);
			dialog.append(content);
			modal.append(dialog);
			$("#form").append(modal);
		}
};

//交维工作组件相关方法
biz.edit.form.jwWork = {
		addCheckBox:function(td,labels,name,top){//加入多选按钮
			var trs = [];
			var num = 1;
			//可选标题
			for(var key in labels){
				var checkbox = $("<input type='checkbox' checked='checked'>");
				checkbox.attr("name",name);
				checkbox.attr("onchange","biz.edit.form.jwWork.changeDetailJwWork(this)");
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
				//不可选具体工作
				for(var i=0;i<labels[key].length;i++){
					checkbox = $("<input type='checkbox' checked='checked' disabled='disabled'>");
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
		changeDetailJwWork:function(ele){//选中，排除
			if(ele.checked){
				var jwWork = $("[name='jwWork"+$(ele).val()+"']");
				for(var i=0;i<jwWork.length;i++){
					jwWork[i].checked=true;
				}
			}else{
				$("[name='jwWork"+$(ele).val()+"']").attr("checked",false);
			}
		}
};

//联动属性相关方法
biz.edit.form.refVariable = {
		setRefVariable:function(list,data){
			var refDom = null;//联动父节点dom
			var dom = $('#form [name="'+data.name+'"]');
			var refConfigValiable,refConfigParam;
			
			if(data.refVariable){
				if(list!=null && list.length>0){
					for(var i=0;i<list.length;i++){
						if(list[i].id == data.refVariable){
							refDom = $('#form [name="'+list[i].name+'"]');
							if(data.refParam){
								refConfigValiable = list[i].viewParams;
								refConfigParam = data.refParam;
							}
						}
					}
				}
			}
			
			if(data.refVariable && data.refParam){//联动父节点为配置项
				refDom.change(function(){
					var selectVal = refDom.val();
					var paramVal = null;
					$.ajax({
			            type : 'POST',
			            url : path + "/workflow/getConfigItemListByName",
			            data:{name:refConfigValiable},
			            dataType:'json',
			            async:false,
			            success : function(result){
		            		//获取对应配置字段的属性值
		            		if(result && result.length>0){
		            			for(var n=0;n<result.length;n++){
		            				if(result[n].list && result[n].list.length>0){
				                		for(var i=0;i<result[n].list.length;i++){
					                		if(result[n].list[i].name == selectVal){
					                			paramVal = result[n].list[i][refConfigParam];
					                		}
					                	}
				                	}
				                	//设置联动属性
				                	if(dom.prop("tagName") == "SELECT"){
				                		dom.empty();
				                		
				                		if(data.viewComponent=="DICTCOMBOBOX"){
				            				biz.edit.form.combobox.loadDictComboBox(dom,paramVal);
				            			}else if(data.viewComponent=="MCMCOMBOBOX"){
				            				biz.edit.form.combobox.loadMcmComboBox(dom,paramVal);
				            			}else{
				            				biz.edit.form.combobox.loadComboBox(dom,paramVal);
				            			}
				                	}else{
				                		dom.val(paramVal);
				                	}
				                	
		            			}
		            		}
			            }
			        });
				});
			}else if(data.refVariable && !data.refParam){//联动父节点为其他组件
				refDom.change(function(){
					if(dom.prop("tagName") == "SELECT"){
						dom.empty();
						if(data.viewComponent=="DICTCOMBOBOX"){
            				biz.edit.form.combobox.loadDictComboBox(dom,refDom.val());
            			}else if(data.viewComponent=="MCMCOMBOBOX"){
            				biz.edit.form.combobox.loadMcmComboBox(dom,refDom.val());
            			}else{
            				biz.edit.form.combobox.loadComboBox(dom,refDom.val());
            			}
					}else{
						dom.val(refDom.val());
					}
				});
			}
		}
		
};

//角色人员列表组件相关方法
biz.edit.form.memberList = {
    inputName:"",
    gridSelected:[],
    createWindow:function(data){//创建容器html代码
        var inputName = data.name;
        var viewParams = data.viewParams;
        biz.edit.form.memberList.inputName = inputName;
        var container = $("<div id='memberListContainer'>");
            container.hide();
            var import_form = $("<div class='import_form'>");
            var table = $("<table cellpadding='0' cellspacing='0' class='listtable'>");
            tr = $("<tr>");
            td = $("<td colspan='4' style='text-align:center;'>");
            var a = "<a onclick='biz.edit.form.memberList.queryGrid()' class='dt_btn' style='margin-left:0;'>查询</a>";
            td.append(a);
            tr.append(td);
            table.append(tr);
            import_form = $("<div class='import_form'>");
            title = "<h2 class='white_tit'><span class='white_tit_icon'></span>人员列表"+
            "<a role='button' data-toggle='collapse' href='#memberListcollapse' aria-expanded='true' class='gray_drop'></a></h2>";
            import_form.html(title);
            listtable_wrap = $("<div class='listtable_wrap panel-collapse collapse in' id='memberListcollapse' role='tabpanel' style='border-left:1px solid #bdc6cf;'>");
            table = $("<table id='memberList_table' />");
            listtable_wrap.append(table);
            container.append(import_form);
            container.append(listtable_wrap);
            
            var div=$('<div id="memberListRoleMenuContent" class="menuContent" style="display:none; position: absolute;">');
            var ul = $('<ul id="memberListRoleTree" class="ztree" style="margin-top:0; width:180px; height: 300px;">');
            div.append(ul);
            container.append(div);
            this.loadGrid(table,viewParams);
            return container;
    },
    loadGrid:function(table,roleName){
        
        table.bootstrapTable({
            url:path+"/serviceRoleConf/loadUsersByRole",
            method : "post",
            pagination : true,
            queryParams : function queryParams(params) {
                params.roleName = roleName;
                return params;
            },
            contentType:"application/x-www-form-urlencoded",
            clickToSelect : true,
            columns : [
                {
                    field : "state",
                    checkbox : true,
                    align : "center"
                },{
                    field : "username",
                    title : "用户名",
                    align : "center"
                },{
                    field : "fullname",
                    title : "名称",
                    align : "center"
                },{
                    field : "description",
                    title : "部门",
                    align : "center"
                }],
                onCheck:this.onCheck,
                onUncheck:this.onUncheck,
                onCheckAll:this.onCheck,
                onUncheckAll:this.onUncheck,
                onLoadSuccess:function(){//翻页选中之前选中的人员
                    var selected = biz.edit.form.memberList.gridSelected;
                    var params = [];
                    for(var i=0;i<selected.length;i++){
                        params.push(selected[i].username);
                    }
                    $("#memberList_table").bootstrapTable("checkBy",{field:"username", values:params});
                }
        });
    },
    queryGrid:function(){
        biz.edit.form.memberList.gridSelected = [];
        $("#memberList_table").bootstrapTable("refresh");
    },
    onCheck:function(data){//表格选择人员，人员值加上选择人员，隔开
        var rows = biz.edit.form.memberList.gridSelected;
        if(data)//判断全选单选
            if(data instanceof Array){
                for(var j=0;j<data.length;j++){
                    var flag = true;
                    for(var i=0;i<rows.length;i++){
                        if(rows[i].username==data[j].username)
                            flag = false;
                    }
                    if(flag)
                        rows.push(data[j]);
                }
            }else{
                var flag = true;
                for(var i=0;i<rows.length;i++){
                    if(rows[i].username==data.username)
                        flag = false;
                }
                if(flag)
                        rows.push(data);
            }
        var value = "";
        var value2 = "";
        for(var i=0;i<rows.length;i++){
            value += rows[i].fullname;
            value2 += rows[i].username;
            if(i<rows.length-1){
                value += ",";
                value2 += ",";
            }
        }
        $("#form #"+biz.edit.form.memberList.inputName).val(value);
        $("#form :hidden[name='"+biz.edit.form.memberList.inputName+"']").val(value2);
    },
    onUncheck:function(data){//表格去除选中人员，人员值减去去除人员，隔开
        var rows = biz.edit.form.memberList.gridSelected;
        var selected = [];
        if(data)//判断全选单选
            if(data instanceof Array){
                for(var i=0;i<rows.length;i++){
                    var flag = true;
                    for(var j=0;j<data.length;j++){
                        if(rows[i].username==data[j].username)
                            flag = false;
                    }
                    if(flag)
                        selected.push(rows[i]);
                }
            }else{
                for(var i=0;i<rows.length;i++){
                    if(rows[i].username!=data.username)
                        selected.push(rows[i]);
                }
            }
        rows = biz.edit.form.memberList.gridSelected = selected;
        var value = "";
        var value2 = "";
        for(var i=0;i<rows.length;i++){
            value += rows[i].fullname;
            value2 += rows[i].username;
            if(i<rows.length-1){
                value += ",";
                value2 += ",";
            }
        }
        $("#form #"+biz.edit.form.memberList.inputName).val(value);
        $("#form :hidden[name='"+biz.edit.form.memberList.inputName+"']").val(value2);
    },
    openWindow:function(){
        var container = $("#memberListContainer");
        if(container.is(":hidden")){
            container.show();
        }else{
            container.hide();
        }
    },
    treeId : null,
    curAsyncCount : 0,
    getTree : function() {
        return $.fn.zTree.getZTreeObj(this.treeId);
    },
    initTree : function(id) {
        this.treeId = id;
        // 初始化角色树
        $.fn.zTree.init($('#' + id), this.setting, this.zNodes);
        // 初始化树图后展开根节点
        var zTree = $.fn.zTree.getZTreeObj(id);
        var rootNode = zTree.getNodeByParam('id', 'root', null);
        zTree.expandNode(rootNode, true);
    },
    setting : {
        async : {
            dataType : 'json',
            url : path + '/serviceRoleConf/getRoleTree',
            autoParam : [ 'id', 'name', 'attributes' ],
            otherParam : {},
            enable : true
        },
        view : {
            fontCss : function(treeId, treeNode) {
                return (!!treeNode.highlight) ? {
                    color : "#A60000",
                    "font-weight" : "bold"
                } : {
                    color : "#333",
                    "font-weight" : "normal"
                };
            }
        },
        callback : {
           onAsyncSuccess : function(event, treeId, treeNode, msg) {
                var zTree = $.fn.zTree.getZTreeObj(treeId);
                var nodes = zTree.transformToArray(zTree.getNodes());
                //全展开
                for (var i = 0, len = nodes.length; i < len; i++) {
                    zTree.expandNode(nodes[i], true);
                }
            },
            onClick: function(e, treeId, treeNode){
                $("#memberListContainer input[name='roleName']").val(treeNode.name);
                biz.edit.form.memberList.hideMenu();
            }
        }
    },
    zNodes : {
        id : 'root',
        pId : 0,
        name : "角色列表",
        nocheck : true,
        attributes : {
            showItfDescr : false,
            hidedown : false
        },
        isParent : true
    },
    showMenu:function() {
        var input = $("#memberListContainer input[name='roleName']");
        var inputOffset = input.offset();
        $("#memberListRoleMenuContent").css({left:inputOffset.left + "px", top:inputOffset.top + input.outerHeight() + "px"}).slideDown("fast");

        $("body").bind("mousedown", biz.edit.form.memberList.onBodyDown);
    },
    hideMenu:function() {
        $("#memberListRoleMenuContent").fadeOut("fast");
        $("body").unbind("mousedown", biz.edit.form.memberList.onBodyDown);
    },
    onBodyDown:function(event) {
        if (!(event.target.name == "roleName" || event.target.id == "memberListRoleMenuContent" || $(event.target).parents("#memberListRoleMenuContent").length>0)) {
            biz.edit.form.memberList.hideMenu();
        }
    },
    loadRoleTree:function(){
        $("#memberListRoleMenuContent .ztree").width($("[name='roleName']").innerWidth()-10);
        biz.edit.form.memberList.roleTree.init("memberListRoleTree");
    }
};
