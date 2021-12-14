$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'user/userWithdraw/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '用户id', name: 'userId', index: 'user_id', width: 80 }, 
			{ label: '银行卡名称', name: 'bankName', index: 'bank_name', width: 80 }, 
			{ label: '', name: 'accountName', index: 'account_name', width: 80 }, 
			{ label: '', name: 'accountNumber', index: 'account_number', width: 80 }, 
			{ label: '', name: 'branchName', index: 'branch_name', width: 80 }, 
			{ label: '', name: 'iban', index: 'iban', width: 80 }, 
			{ label: '', name: 'ifsc', index: 'ifsc', width: 80 }, 
			{ label: '', name: 'upi', index: 'upi', width: 80 }, 
			{ label: '提现金额', name: 'amount', index: 'amount', width: 80 }, 
			{ label: '拒绝原因', name: 'remark', index: 'remark', width: 80 }, 
			{ label: '0:等待审核,1:审核成功,2:审核失败', name: 'status', index: 'status', width: 80 }, 
			{ label: '', name: 'isDel', index: 'is_del', width: 80 }, 
			{ label: '', name: 'version', index: 'version', width: 80 }, 
			{ label: '', name: 'createTime', index: 'create_time', width: 80 }, 
			{ label: '', name: 'updateTime', index: 'update_time', width: 80 }
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
		q: {},
		userWithdraw: {}
	},
    created:function () {
    },
	methods: {
        query: function () {
            vm.showList = true;
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "key":vm.q.key
                },
                page:1
            }).trigger("reloadGrid");
        },
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.userWithdraw = {};
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
                var url = vm.userWithdraw.id == null ? "user/userWithdraw/save" : "user/userWithdraw/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.userWithdraw),
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
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "key":vm.q.key,
                },
                page:page
            }).trigger("reloadGrid");
		}
	}
});