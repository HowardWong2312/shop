$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'user/userCreditsLog/list',
        datatype: "json",
        colModel: [
            { label: 'id', name: 'id', index: 'id', width: 50, key: true },
            { label: '用户昵称', name: 'userName', index: 'userName', width: 80 },
            { label: '变动积分', name: 'amount', index: 'amount', width: 80 },
            { label: '变后余额', name: 'balance', index: 'balance', width: 80 },
            { label: '类型', name: 'statusValue', width: 35},
            { label: '方向', name: 'type', index: 'type', width: 60 ,formatter:function(value){
                if(value === 1){
                    return '<span class="label label-success">收入</span>';
                }
                if(value === 2){
                    return '<span class="label label-danger">支出</span>';
                }
            }},
            { label: '记录时间', name: 'createTime', index: 'create_time', width: 80 },
            { label: '描述', name: 'desc', index: 'desc', width: 80 },
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
		userCreditsLog: {}
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
                    "status":vm.q.status,
                    "type":vm.q.type,
                },
                page:1
            }).trigger("reloadGrid");
        },
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.userCreditsLog = {};
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
                var url = vm.userCreditsLog.id == null ? "user/userCreditsLog/save" : "user/userCreditsLog/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.userCreditsLog),
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
                        url: baseURL + "user/userCreditsLog/delete",
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
			$.get(baseURL + "user/userCreditsLog/info/"+id, function(r){
                vm.userCreditsLog = r.userCreditsLog;
            });
		},
        getStatusList: function () {
            $.get(baseURL + "sys/dict/list/user_credits_log_status", function (r) {
                vm.statusList = r.list;
            });
        },
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "key":vm.q.key,
                    "status":vm.q.status,
                    "type":vm.q.type,
                },
                page:page
            }).trigger("reloadGrid");
		}
	}
});