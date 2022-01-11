$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'news/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 35, key: true },
			{ label: '作者名字', name: 'author', index: 'author', width: 60 },
            { label: '作者头像', name: 'headUrl', index: 'head_url', width: 50, formatter: function(value, options, row){
                return '<img src="'+value+'" style="width:100%" />';
            }},
			{ label: '标题', name: 'title', index: 'title', width: 150 ,search:true},
			{ label: '资讯类型', name: 'dict.value', index: 'news_type', width: 50 },
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 },
			{ label: '修改时间', name: 'updateTime', index: 'update_time', width: 80 }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
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

var ue = UE.getEditor('content',{
    autoHeight: false,
    toolbars: [
        ['source','fullscreen', 'bold', 'italic', 'underline', 'fontborder', 'strikethrough','redo', 'bold','simpleupload','insertimage','emotion','forecolor']
    ]
});
ue.ready(function() {
    ue.setHeight(150);
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
        newsTypeList:[],
        news :{
            headUrl:"https://base-assets.oss-accelerate.aliyuncs.com/20210815/f00cb9c03ae74af4891959ffc481e426.jpg",
        },
        q:{}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
            ue.setContent("");
			vm.showList = false;
			vm.title = "新增";
			vm.news = {
                headUrl:"https://base-assets.oss-accelerate.aliyuncs.com/20210815/f00cb9c03ae74af4891959ffc481e426.jpg",
            };
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.news = {
            };
            vm.getInfo(id)
		},
		saveOrUpdate: function () {
		    $('#btnSaveOrUpdate').button('loading').delay(300).queue(function() {
                vm.news.body = ue.getContent();
                vm.news.summary = ue.getContentTxt();
                if(vm.news.summary.length > 64){
                    vm.news.summary = vm.news.summary.substring(0,64);
                }
                delete vm.news.updateTime;
                delete vm.news.dict;
                var url = vm.news.id == null ? "news/save" : "news/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    data: vm.news,
                    success: function(r){
                        layer.msg(r.message, {icon: 1});
                        vm.reload();
                        $('#btnSaveOrUpdate').button('reset');
                        $('#btnSaveOrUpdate').dequeue();

                    },
                    error: function (e) {
                        $('#btnSaveOrUpdate').button('reset');
                        $('#btnSaveOrUpdate').dequeue();
                        layer.alert(e.responseJSON.message);
                    }
                });
			});
		},
		del: function (event) {
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
                        url: baseURL + "news/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function(r){
                            if(r.status == 200){
                                layer.msg("操作成功", {icon: 1});
                                vm.reload();
                            }else{
                                layer.alert(r.message);
                            }
                        },
                        error: function (e) {
                            layer.alert(e.responseJSON.message);
                        }
				    });
			    }
             }, function(){
             });
		},
        getInfo: function(id){
            $.get(baseURL + "news/info/"+id, function(r){
                vm.news = r.news;
                ue.setContent(vm.news.body);
            });
        },
        getDictList: function(id){
            $.get(baseURL + "sys/dict/list/"+id, function(r){
                vm.newsTypeList = r.list;
            });
        },
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
			    postData:{"title":vm.q.title,"newsType":vm.q.newsType},
                page:page
            }).trigger("reloadGrid");
		},

	},

    created:function () {
        this.getDictList("news_type");
    },

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
        if(r.status == 200){
            vm.news.headUrl = r.url;
        }else{
            alert(r.msg);
        }
    }

});
