$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'user/userInfo/list',
        datatype: "json",
        colModel: [			
			{ label: '用户ID', name: 'userId', index: 'userId', width: 40, key: true },
			{ label: '昵称', name: 'nickName', index: 'nickName', width: 80 },
            { label: '头像', name: 'header', index: 'header', width: 30, formatter: function(value, options, row){
                    return '<img src="'+value+'" style="width:100%" />';
            }},
            { label: '手机号', width: 80, formatter: function(value, options, row){
                    return '<span>'+row.countryCode+row.phone+'</span>';
                }},
			{ label: '性别', name: 'sex', index: 'sex', width: 50 },
			{ label: '年龄', name: 'age', index: 'age', width: 50 },
			{ label: 'bibi号', name: 'bibiCode', index: 'bibiCode', width: 70 },
			{ label: '邀请人', name: 'fatherName', width: 60 },
			{ label: '当前余额', name: 'balance', index: 'balance', width: 60 },
			{ label: '当前积分', name: 'credits', index: 'credits', width: 60 },
            { label: '用户类型', name: 'isMerchant', index: 'isMerchant', width: 60 ,formatter:function(value){
                if(value === 1){
                    return '<span class="label label-primary">认证商家</span>';
                }else{
                    return '<span class="label label-default">未认证</span>';
                }
            }},
			{ label: '用户等级', name: 'userLevelId', index: 'userLevelId', width: 50 },
			{ label: '剩余抽奖次数', name: 'lotteryTimes', index: 'lotteryTimes', width: 50 }
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
        userLevelList: [],
		q: {},
		userInfo: {}
	},
    created:function () {
	    this.getUserLevelList();
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
			vm.userInfo = {};
		},
		update: function (event) {
			var userid = getSelectedRow();
			if(userid == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(userid)
		},
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(300).queue(function() {
                var url = vm.userInfo.userId == null ? "user/userInfo/save" : "user/userInfo/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.userInfo),
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
		getInfo: function(userid){
			$.get(baseURL + "user/userInfo/info/"+userid, function(r){
                vm.userInfo = r.userInfo;
            });
		},
        getUserLevelList: function(){
            $.get(baseURL + "user/userLevel/list", function(r){
                vm.userLevelList = r.list;
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