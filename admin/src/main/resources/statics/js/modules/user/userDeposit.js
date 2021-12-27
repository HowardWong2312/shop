$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'user/userDeposit/list',
        datatype: "json",
        colModel: [			
			{ label: '订单号', name: 'orderCode', index: 'order_code', width: 80, key: true },
			{ label: '用户id', name: 'userId', index: 'user_id', width: 50 },
			{ label: '用户名称', name: 'userName', width: 80 },
			{ label: '支付方式', name: 'paymentName', index: 'payment_id', width: 80 },
			{ label: '提现金额', name: 'amount', index: 'amount', width: 80 }, 
            { label: '状态', name: 'status', index: 'status', width: 80, formatter:function(value, options, row){
                    return '<span class="label ' + row.statusColor + '">' + row.statusValue + '</span>';
            }},
            { label: '充值时间', name: 'createTime', index: 'create_time', width: 80 }
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
        statusList: [],
		q: {},
		userDeposit: {}
	},
    created:function () {
        this.getStatusList();
    },
	methods: {
        query: function () {
            vm.showList = true;
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "key":vm.q.key,
                    "status":vm.q.status
                },
                page:1
            }).trigger("reloadGrid");
        },
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.userDeposit = {};
		},
		update: function (event) {
			var orderCode = getSelectedRow();
			if(orderCode == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(orderCode)
		},
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(300).queue(function() {
                var url = vm.userDeposit.orderCode == null ? "user/userDeposit/save" : "user/userDeposit/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.userDeposit),
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
			var orderCodes = getSelectedRows();
			if(orderCodes == null){
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
                        url: baseURL + "user/userDeposit/delete",
                        contentType: "application/json",
                        data: JSON.stringify(orderCodes),
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
		getInfo: function(orderCode){
			$.get(baseURL + "user/userDeposit/info/"+orderCode, function(r){
                vm.userDeposit = r.userDeposit;
            });
		},
        getStatusList: function () {
            $.get(baseURL + "sys/dict/list/user_deposit_status", function (r) {
                vm.statusList = r.list;
            });
        },
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "key":vm.q.key,
                    "status":vm.q.status
                },
                page:page
            }).trigger("reloadGrid");
		}
	}
});