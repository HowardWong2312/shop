$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'user/userInfo/list',
        datatype: "json",
        colModel: [			
			{ label: 'userid', name: 'userid', index: 'userId', width: 50, key: true },
			{ label: '国家编码', name: 'countrycode', index: 'countryCode', width: 80 }, 
			{ label: '手机号', name: 'phone', index: 'phone', width: 80 }, 
			{ label: '昵称', name: 'nickname', index: 'nickName', width: 80 }, 
			{ label: '头像', name: 'header', index: 'header', width: 80 }, 
			{ label: '性别 男/女', name: 'sex', index: 'sex', width: 80 }, 
			{ label: '生日', name: 'birthday', index: 'birthday', width: 80 }, 
			{ label: '年龄', name: 'age', index: 'age', width: 80 }, 
			{ label: 'bibi号（8位纯数字）', name: 'bibicode', index: 'bibiCode', width: 80 }, 
			{ label: '邀请码', name: 'invitecode', index: 'inviteCode', width: 80 }, 
			{ label: '邀请人', name: 'fatherid', index: 'fatherId', width: 80 }, 
			{ label: '当前积分', name: 'credits', index: 'credits', width: 80 }, 
			{ label: '累计收益积分', name: 'incomecredits', index: 'incomeCredits', width: 80 }, 
			{ label: '环信id', name: 'easemobid', index: 'easemobId', width: 80 }, 
			{ label: '环信密码', name: 'easemobpwd', index: 'easemobPwd', width: 80 }, 
			{ label: '个性签名', name: 'signature', index: 'signature', width: 80 }, 
			{ label: '肤色', name: 'skincolor', index: 'skinColor', width: 80 }, 
			{ label: '财富值', name: 'wealth', index: 'wealth', width: 80 }, 
			{ label: '财富等级', name: 'wealthlevel', index: 'wealthLevel', width: 80 }, 
			{ label: '背景图', name: 'backimage', index: 'backImage', width: 80 }, 
			{ label: '国家id', name: 'countryid', index: 'countryId', width: 80 }, 
			{ label: '国家', name: 'country', index: 'country', width: 80 }, 
			{ label: '省id', name: 'provinceid', index: 'provinceId', width: 80 }, 
			{ label: '省级名称', name: 'provincename', index: 'provinceName', width: 80 }, 
			{ label: '市id', name: 'cityid', index: 'cityId', width: 80 }, 
			{ label: '市级名称', name: 'cityname', index: 'cityName', width: 80 }, 
			{ label: '二维码', name: 'ecode', index: 'eCode', width: 80 }, 
			{ label: '手机号修改时间', name: 'phonechangetime', index: 'phoneChangeTime', width: 80 }, 
			{ label: 'bibi号修改时间', name: 'bibicodechangetime', index: 'bibiCodeChangeTime', width: 80 }, 
			{ label: '添加好友是否需要验证0否1是', name: 'verifyfriend', index: 'verifyFriend', width: 80 }, 
			{ label: '是否他可以通过手机号添加好友', name: 'isphoneaddfriend', index: 'isPhoneAddFriend', width: 80 }, 
			{ label: '是否可以通过bibi号添加好友', name: 'isbibicodeaddfriend', index: 'isBiBiCodeAddFriend', width: 80 }, 
			{ label: '是否可以通过群聊添加好友', name: 'isgroupaddfriend', index: 'isGroupAddFriend', width: 80 }, 
			{ label: '是否可以通过二维码添加好友', name: 'isecodeaddfriend', index: 'isECodeAddFriend', width: 80 }, 
			{ label: '0.未认证1.已认证2黑名单', name: 'isapprove', index: 'isApprove', width: 80 }, 
			{ label: '语言（默认中文）', name: 'language', index: 'language', width: 80 }, 
			{ label: '是否弃用 0否 1是', name: 'del', index: 'del', width: 80 }, 
			{ label: '创建时间', name: 'createtime', index: 'createTime', width: 80 }, 
			{ label: '是否可以创建群0否1是', name: 'iscancreategroup', index: 'isCanCreateGroup', width: 80 }, 
			{ label: '手机号搜索（国家编码+手机号）', name: 'searchphone', index: 'searchPhone', width: 80 }, 
			{ label: '是否商家', name: 'ismerchant', index: 'isMerchant', width: 80 }, 
			{ label: '余额', name: 'balance', index: 'balance', width: 80 }, 
			{ label: '支付密码', name: 'password', index: 'password', width: 80 }, 
			{ label: '等级(id就等于等级数)', name: 'userlevelid', index: 'userLevelId', width: 80 }, 
			{ label: '免费抽奖次数', name: 'lotterytimes', index: 'lotteryTimes', width: 80 }
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
		q: {},
		userInfo: {}
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
                var url = vm.userInfo.userid == null ? "user/userInfo/save" : "user/userInfo/update";
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
		del: function () {
			var userids = getSelectedRows();
			if(userids == null){
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
                        url: baseURL + "user/userInfo/delete",
                        contentType: "application/json",
                        data: JSON.stringify(userids),
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
		getInfo: function(userid){
			$.get(baseURL + "user/userInfo/info/"+userid, function(r){
                vm.userInfo = r.userInfo;
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