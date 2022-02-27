$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'other/showVideo/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '用户ID', name: 'userId', index: 'userId', width: 80 },
			{ label: '用户昵称', name: 'userName', index: 'userId', width: 80 },
            { label: '缩略图', name: 'firstImage', index: 'firstImage', width: 30, formatter: function(value, options, row){
                return '<img src="'+value+'" style="width:100%" />';
            }},
			{ label: '视频时长（秒）', name: 'videoDuration', index: 'videoDuration', width: 80 },
			{ label: '点赞数量', name: 'supportNum', index: 'supportNum', width: 80 },
			{ label: '浏览数量', name: 'viewNum', index: 'viewNum', width: 80 }
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
        showVideo: {}
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
                        url: baseURL + "other/showVideo/delete",
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
        check: function () {
            let id = getSelectedRow();
            if(id == null){
                return ;
            }
            vm.getInfo(id)
            $("#myModal").modal('show');

        },
		getInfo: function(id){
			$.get(baseURL + "other/showVideo/info/"+id, function(r){
                vm.showVideo = r.showVideo;
                if(Hls.isSupported()) {
                    var video = document.getElementById('video'); // 获取 video 标签
                    var hls = new Hls(); // 实例化 Hls 对象
                    hls.loadSource(vm.showVideo.videoUrl); // 传入路径
                    hls.attachMedia(video);
                    hls.on(Hls.Events.MANIFEST_PARSED,function() {
                        video.play(); // 调用播放 API
                    });
                }
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