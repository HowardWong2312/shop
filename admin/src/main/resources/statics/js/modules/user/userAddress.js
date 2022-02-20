$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'user/userAddress/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
            { label: '部门', name: 'sysDeptName', width: 35 },
            { label: '代理商', name: 'sysUserName', width: 35 },
            { label: '用户ID', name: 'userId', index: 'user_id', width: 35 },
            { label: '用户昵称', name: 'userName', index: 'user_id', width: 80 },
			{ label: '收货人姓名', name: 'name', index: 'name', width: 80 }, 
			{ label: '国家代码', name: 'countryCode', index: 'country_code', width: 80 }, 
			{ label: '电话', name: 'phone', index: 'phone', width: 80 }, 
			{ label: '国家ID', name: 'countryId', index: 'country_id', width: 80 }, 
			{ label: '省', name: 'province', index: 'province', width: 80 }, 
			{ label: '市', name: 'city', index: 'city', width: 80 }, 
			{ label: '区', name: 'area', index: 'area', width: 80 }, 
			{ label: '详细地址', name: 'address', index: 'address', width: 80 }, 
            { label: '是否默认', name: 'isDefault', index: 'is_default', width: 80, formatter: function(value, options, row){
                    return row.isDefault == 1 ? '是' : '否';
            }},
            { label: '添加时间', name: 'createTime', index: 'create_time', width: 80 }
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
        sysDeptList: [],
        sysUserList: [],
		q: {},
		userAddress: {}
	},
    created:function () {
        this.getSysDeptList();
    },
	methods: {
        query: function () {
            vm.showList = true;
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "key":vm.q.key,
                    "sysDeptId":vm.q.sysDeptId,
                    "sysUserId":vm.q.sysUserId,
                },
                page:1
            }).trigger("reloadGrid");
        },
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.userAddress = {};
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
                var url = vm.userAddress.id == null ? "user/userAddress/save" : "user/userAddress/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.userAddress),
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
                        url: baseURL + "user/userAddress/delete",
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
			$.get(baseURL + "user/userAddress/info/"+id, function(r){
                vm.userAddress = r.userAddress;
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
                    "sysDeptId":vm.q.sysDeptId,
                    "sysUserId":vm.q.sysUserId,
                },
                page:page
            }).trigger("reloadGrid");
		}
	}
});