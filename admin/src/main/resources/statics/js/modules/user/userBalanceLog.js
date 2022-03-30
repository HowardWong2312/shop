$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'user/userBalanceLog/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
            { label: '部门', name: 'sysDeptName', width: 40 },
            { label: '代理商', name: 'sysUserName', width: 40 },
            { label: '用户ID', name: 'userId', index: 'user_id', width: 40 },
            { label: '用户手机号', name: 'userPhone', width: 60 },
			{ label: '变动金额', name: 'amount', index: 'amount', width: 50, formatter:function(value){
                if(value > 0){
                    return '<span class="label label-success">+'+value+'</span>';
                }else{
                    return '<span class="label label-danger">'+value+'</span>';
                }
            }},
			{ label: '变后余额', name: 'balance', index: 'balance', width: 50 },
            { label: '类型', name: 'statusValue', width: 50},
            { label: '方向', name: 'type', index: 'type', width: 50 ,formatter:function(value){
                if(value === 1){
                    return '<span class="label label-success">收入</span>';
                }
                if(value === 2){
                    return '<span class="label label-danger">支出</span>';
                }
            }},
			{ label: '记录时间', name: 'createTime', index: 'create_time', width: 80 },
			{ label: '描述', name: 'desc', index: 'desc', width: 60 },
        ],
		viewrecords: true,
        height: 600,
        rowNum: 20,
		rowList : [20,30,50],
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        loadComplete: function (data) {
            vm.totalAmount = data.totalAmount;
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
        totalAmount: 0.00,
        sysDeptList: [],
        sysUserList: [],
        statusList: [],
		q: {},
		userBalanceLog: {}
	},
    created:function () {
        this.getSysDeptList();
        this.getStatusList();
    },
	methods: {
        query: function () {
            vm.showList = true;
            vm.q.beginTime = $("#beginTime").val();
            vm.q.endTime = $("#endTime").val();
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "key":vm.q.key,
                    "status":vm.q.status,
                    "type":vm.q.type,
                    "sysDeptId":vm.q.sysDeptId,
                    "sysUserId":vm.q.sysUserId,
                    "beginTime":vm.q.beginTime,
                    "endTime":vm.q.endTime
                },
                page:1
            }).trigger("reloadGrid");
        },
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.userBalanceLog = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(300).queue(function() {
                var url = vm.userBalanceLog.id == null ? "user/userBalanceLog/save" : "user/userBalanceLog/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.userBalanceLog),
                    success: function(r){
                        if(r.code == 200){
                             layer.msg("操作成功", {icon: 1});
                             vm.reload();
                             $('#btnSaveOrUpdate').button('reset');
                             $('#btnSaveOrUpdate').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }
                    }
                });
			});
		},
		del: function () {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			var lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
               if(!lock) {
                    lock = true;
		            $.ajax({
                        type: "POST",
                        url: baseURL + "user/userBalanceLog/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function(r){
                            if(r.code == 200){
                                layer.msg("操作成功", {icon: 1});
                                vm.reload();
                            }else{
                                layer.alert(r.msg);
                            }
                        }
				    });
			    }
             }, function(){
             });
		},
		getInfo: function(id){
			$.get(baseURL + "user/userBalanceLog/info/"+id, function(r){
                vm.userBalanceLog = r.userBalanceLog;
            });
		},
        getStatusList: function () {
            $.get(baseURL + "sys/dict/list/user_balance_log_status", function (r) {
                vm.statusList = r.list;
            });
        },
        getSysDeptList: function(){
            $.get(baseURL + "sys/dept/listForSelect", function(r){
                vm.sysDeptList = r.list;
            });
        },
        getSysUserListByDeptId: function(){
            vm.q.sysUserId = null;
            if(vm.q.sysDeptId != null  && vm.q.sysDeptId != ''){
                $.get(baseURL + "sys/user/listForSelectByDeptId/"+vm.q.sysDeptId, function(r){
                    vm.sysUserList = r.list;
                });
            }else{
                vm.sysUserList = null;
            }
        },
		reload: function (event) {
			vm.showList = true;
            vm.q.beginTime = $("#beginTime").val();
            vm.q.endTime = $("#endTime").val();
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "key":vm.q.key,
                    "status":vm.q.status,
                    "type":vm.q.type,
                    "sysDeptId":vm.q.sysDeptId,
                    "sysUserId":vm.q.sysUserId,
                    "beginTime":vm.q.beginTime,
                    "endTime":vm.q.endTime
                },
                page:page
            }).trigger("reloadGrid");
		}
	}
});