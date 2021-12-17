$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'goods/shopGoodsLang/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '商品ID', name: 'goodsId', index: 'goods_id', width: 80 }, 
			{ label: '商品名称', name: 'title', index: 'title', width: 80 }, 
			{ label: 'logo', name: 'logoUrl', index: 'logo_url', width: 80 }, 
			{ label: '描述', name: 'description', index: 'description', width: 80 }, 
			{ label: '语言ID', name: 'languageId', index: 'language_id', width: 80 }, 
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
		shopGoodsLang: {}
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
			vm.shopGoodsLang = {};
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
                let url = vm.shopGoodsLang.id == null ? "goods/shopGoodsLang/save" : "goods/shopGoodsLang/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.shopGoodsLang),
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
                        url: baseURL + "goods/shopGoodsLang/delete",
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
			$.get(baseURL + "goods/shopGoodsLang/info/"+id, function(r){
                vm.shopGoodsLang = r.shopGoodsLang;
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