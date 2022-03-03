$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'user/userWithdraw/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 30, key: true },
            { label: '部门', name: 'sysDeptName', width: 35 },
            { label: '代理商', name: 'sysUserName', width: 35 },
            { label: '用户ID', name: 'userId', index: 'user_id', width: 35 },
            { label: '用户名称', name: 'userName', width: 35 },
            { label: '收款方式', name: 'paymentName', index: 'payment_name', width: 80 },
			{ label: '账户名称', name: 'accountName', index: 'account_name', width: 50 },
			{ label: '账户号码', name: 'accountNumber', index: 'account_number', width: 50 },
			{ label: '提现金额', name: 'amount', index: 'amount', width: 40 },
			{ label: '拒绝原因', name: 'remark', index: 'remark', width: 50 },
            { label: '状态', name: 'status', index: 'status', width: 40, formatter:function(value, options, row){
                return '<span class="label ' + row.statusColor + '">' + row.statusValue + '</span>';
            }},
			{ label: '申请时间', name: 'createTime', index: 'create_time', width: 80 },
			{ label: '处理时间', name: 'updateTime', index: 'update_time', width: 80 }
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
        statusList: [],
        sysDeptList: [],
        sysUserList: [],
		q: {},
		userWithdraw: {}
	},
    created:function () {
        this.getSysDeptList();
        this.getStatusList();
    },
	methods: {
        query: function () {
            vm.showList = true;
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "key":vm.q.key,
                    "status":vm.q.status,
                    "sysDeptId":vm.q.sysDeptId,
                    "sysUserId":vm.q.sysUserId,
                },
                page:1
            }).trigger("reloadGrid");
        },
        check: function () {
            let id = getSelectedRow();
            if(id == null){
                return ;
            }
            vm.getInfo(id);
            $("#myModal").modal('show');
        },
        success: function () {
            vm.userWithdraw.status = 1;
            vm.subCheck("通过")
        },
        refuse: function () {
            vm.userWithdraw.status = 2;
            vm.subCheck("拒绝")
        },
        subCheck: function (title) {
            let lock = false;
            layer.confirm('确定要对该活动申请执行 '+title+' 操作？', {
                btn: ['确定','取消'] //按钮
            }, function(){
                if(!lock) {
                    lock = true;
                    $.ajax({
                        type: "POST",
                        url: baseURL + "user/userWithdraw/check",
                        contentType: "application/json",
                        data: JSON.stringify(vm.userWithdraw),
                        success: function(r){
                            if(r.code == 200){
                                layer.msg(r.msg, {icon: 1});
                                $("#myModal").modal('hide');
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
                        url: baseURL + "user/userWithdraw/delete",
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
			$.get(baseURL + "user/userWithdraw/info/"+id, function(r){
                vm.userWithdraw = r.userWithdraw;
            });
		},
        getStatusList: function () {
            $.get(baseURL + "sys/dict/list/user_withdraw_status", function (r) {
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
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "key":vm.q.key,
                    "status":vm.q.status,
                    "sysDeptId":vm.q.sysDeptId,
                    "sysUserId":vm.q.sysUserId,
                },
                page:page
            }).trigger("reloadGrid");
		}
	}
});