$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'other/shopBanner/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
            { label: '图片', name: 'imgUrl', index: 'img_url', width: 50, formatter: function(value, options, row){
                return '<img src="'+value+'" style="width:100%" />';
            }},
            { label: '链接类型', name: 'linkType', index: 'link_type', width: 60 ,formatter:function(value){
                if(value === 1){
                    return '<span class="label label-primary">商品</span>';
                }else if(value === 2){
                        return '<span class="label label-primary">资讯</span>';
                }else if(value === 3){
                    return '<span class="label label-primary">外链</span>';
                }else{
                    return '<span class="label label-default">空</span>';
                }
            }},
			{ label: '链接', name: 'linkUrl', index: 'link_url', width: 80 },
			{ label: '排序', name: 'orderNum', index: 'order_num', width: 50 },
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

let vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q: {},
		shopBanner: {
            imgUrl:"https://base-assets.oss-accelerate.aliyuncs.com/20210815/f00cb9c03ae74af4891959ffc481e426.jpg",
        }
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
			vm.shopBanner = {
                imgUrl:"https://base-assets.oss-accelerate.aliyuncs.com/20210815/f00cb9c03ae74af4891959ffc481e426.jpg"
            };
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
                let url = vm.shopBanner.id == null ? "other/shopBanner/save" : "other/shopBanner/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.shopBanner),
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
                        type: "DELETE",
                        url: baseURL + "other/shopBanner/delete",
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
			$.get(baseURL + "other/shopBanner/info/"+id, function(r){
                vm.shopBanner = r.shopBanner;
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
new AjaxUpload('#upLoadPhoto', {
    action: baseURL + "sys/oss/upload",
    name: 'file',
    autoSubmit:true,
    responseType:"json",
    onSubmit:function(file, extension){
        if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))){
            alert('只支持jpg、png、gif格式的图片！');
            return false;
        }
    },
    onComplete : function(file, r){
        if(r.code == 200){
            vm.shopBanner.imgUrl = r.url;
        }else{
            alert(r.msg);
        }
    }

});