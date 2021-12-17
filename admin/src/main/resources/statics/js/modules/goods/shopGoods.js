$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'goods/shopGoods/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '国家ID,0为不限', name: 'countryId', index: 'country_id', width: 80 }, 
			{ label: '用户ID-代表商家', name: 'userId', index: 'user_id', width: 80 }, 
			{ label: '分类ID', name: 'categoryId', index: 'category_id', width: 80 }, 
			{ label: '商品名称(默认)', name: 'title', index: 'title', width: 80 }, 
			{ label: '备注(默认)', name: 'description', index: 'description', width: 80 }, 
			{ label: '产品logo(默认)', name: 'logoUrl', index: 'logo_url', width: 80 }, 
			{ label: '库存', name: 'stock', index: 'stock', width: 80 }, 
			{ label: '价格', name: 'price', index: 'price', width: 80 }, 
			{ label: '外链', name: 'linkUrl', index: 'link_url', width: 80 }, 
			{ label: '联系电话', name: 'phone', index: 'phone', width: 80 }, 
			{ label: '联系地址', name: 'address', index: 'address', width: 80 }, 
			{ label: '是否拼团', name: 'isGroup', index: 'is_group', width: 80 }, 
			{ label: '是否免费抢', name: 'isRush', index: 'is_rush', width: 80 }, 
			{ label: '是否一元购', name: 'isOneBuy', index: 'is_one_buy', width: 80 }, 
			{ label: '0:下架，1:上架', name: 'status', index: 'status', width: 80 }, 
			{ label: '软删除', name: 'isDel', index: 'is_del', width: 80 }, 
			{ label: '乐观锁', name: 'version', index: 'version', width: 80 }, 
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

let vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q: {},
		shopGoods: {}
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
			vm.shopGoods = {};
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
                let url = vm.shopGoods.id == null ? "goods/shopGoods/save" : "goods/shopGoods/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.shopGoods),
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
                        url: baseURL + "goods/shopGoods/delete",
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
			$.get(baseURL + "goods/shopGoods/info/"+id, function(r){
                vm.shopGoods = r.shopGoods;
            });
		},
		reload: function (event) {
			vm.showList = true;
			let page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "key":vm.q.key,
                },
                page:page
            }).trigger("reloadGrid");
		}
	}
});