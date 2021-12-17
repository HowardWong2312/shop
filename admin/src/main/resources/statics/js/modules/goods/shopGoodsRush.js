$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'goods/shopGoodsRush/list',
        datatype: "json",
        colModel: [
            { label: 'id', name: 'id', index: 'a.id', width: 40, key: true },
            { label: '商品名称', name: 'goodsName', width: 120 },
            { label: '商家名称', name: 'merchantName', width: 80 },
            { label: '参与份数', name: 'quantity', index: 'a.quantity', width: 80 },
            { label: '结束时间', name: 'expireTime', index: 'a.expire_time', width: 80 },
            { label: '状态', name: 'status', width: 35, formatter: function (value, options, row) {
                return '<span class="label ' + row.statusColor + '">' + row.statusValue + '</span>';
            }},
            { label: '申请时间', name: 'createTime', index: 'a.create_time', width: 80 },
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

let vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
        statusList: [],
		q: {},
		shopGoodsRush: {}
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
                },
                page:1
            }).trigger("reloadGrid");
        },
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.shopGoodsRush = {};
		},
		update: function (event) {
			let id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(300).queue(function() {
                let url = vm.shopGoodsRush.id == null ? "goods/shopGoodsRush/save" : "goods/shopGoodsRush/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.shopGoodsRush),
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
        check: function () {
            let id = getSelectedRow();
            if(id == null){
                return ;
            }
            vm.getInfo(id);
            $("#myModal").modal('show');
        },
        success: function () {
            vm.shopGoodsRush.status = 1;
            vm.subCheck("通过")
        },
        refuse: function () {
            vm.shopGoodsRush.status = 2;
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
                        url: baseURL + "goods/shopGoodsRush/check",
                        contentType: "application/json",
                        data: JSON.stringify(vm.shopGoodsRush),
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
			let ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			let lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
               if(!lock) {
                    lock = true;
		            $.ajax({
                        type: "POST",
                        url: baseURL + "goods/shopGoodsRush/delete",
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
			$.get(baseURL + "goods/shopGoodsRush/info/"+id, function(r){
                vm.shopGoodsRush = r.shopGoodsRush;
            });
		},
        getStatusList: function () {
            $.get(baseURL + "sys/dict/list/goods_rush_status", function (r) {
                vm.statusList = r.list;
            });
        },
		reload: function (event) {
			vm.showList = true;
			let page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "key":vm.q.key,
                    "status":vm.q.status,
                },
                page:page
            }).trigger("reloadGrid");
		}
	}
});