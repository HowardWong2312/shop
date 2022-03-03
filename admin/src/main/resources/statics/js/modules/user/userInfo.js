$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'user/userInfo/list',
        datatype: "json",
        colModel: [			
			{ label: '用户ID', name: 'userId', index: 'userId', width: 40, key: true },
            { label: '部门', name: 'sysDeptName', width: 35 },
            { label: '代理商', name: 'sysUserName', width: 35 },
            { label: '上级用户', name: 'fatherName', index: 'fatherId', width: 50, formatter: function(value, options, row){
                if(row.fatherId == 0){
                    return '<span class="label label-primary">业务员</span>';
                }else{
                    return row.fatherName == null ? '' : row.fatherName;
                }
            }},
			{ label: '昵称', name: 'nickName', index: 'nickName', width: 50 },
            { label: '头像', name: 'header', index: 'header', width: 30, formatter: function(value, options, row){
                return '<img src="'+value+'" style="width:100%" />';
            }},
            { label: '手机号', name: 'searchPhone', index: 'searchPhone', width: 70 },

            { label: 'bibi号', name: 'bibiCode', index: 'bibiCode', width: 50 },
			{ label: '当前余额', name: 'balance', index: 'balance', width: 40 },
            { label: '当前积分', name: 'credits', index: 'credits', width: 40 },
            { label: '用户类型', name: 'isMerchant', index: 'isMerchant', width: 50 ,formatter:function(value){
                if(value === 1){
                    return '<span class="label label-primary">认证商家</span>';
                }else{
                    return '<span class="label label-default">未认证</span>';
                }
            }},
            { label: '用户等级', name: 'userLevelId', index: 'userLevelId', width: 40 },
            { label: '直属下级', name: 'directCount',  width: 40 },
            { label: '裂变人数', name: 'fissionCount', width: 40 }
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
        sysDeptList: [],
        sysUserList: [],
		q: {},
		userInfo: {
            header:"http://oss.capitalfloatcredit.com/20220119/2a30b55d8d174d10b63d3395ed7576dc.png",
        }
	},
    created:function () {
        this.getSysDeptList();
	    this.getUserLevelList();
    },
	methods: {
        query: function () {
            vm.showList = true;
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "key":vm.q.key,
                    "fatherId":vm.q.fatherId,
                    "userLevelId":vm.q.userLevelId,
                    "isMerchant":vm.q.isMerchant,
                    "sysDeptId":vm.q.sysDeptId,
                    "sysUserId":vm.q.sysUserId,
                },
                page:1
            }).trigger("reloadGrid");
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.userInfo = {
                header:"http://oss.capitalfloatcredit.com/20220119/2a30b55d8d174d10b63d3395ed7576dc.png",
            };
        },
		update: function (event) {

			var userid = getSelectedRow();
			if(userid == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.sysUserList = null;
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
        rechargeCredits: function(){
            vm.rechargeOrDeduction("充值积分","user/userInfo/rechargeCredits");
        },
        rechargeBalance: function(){
            vm.rechargeOrDeduction("充值余额","user/userInfo/rechargeBalance");
        },
        rechargeOrDeduction: function(title,uri){
            let ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            layer.prompt({title: '请输入'+title+'数量'}, function(amount, index){
                if(!isAmount(amount)){
                    if(!amount < 0){
                        layer.msg('请给数字一个面子!', {icon: 5});
                        return ;
                    }
                }
                let data={"ids":ids,"amount":amount};
                let lock = false;
                layer.close(index);
                layer.confirm('确定要为选中的用户'+title+amount+'？', {
                    btn: ['确定','取消'] //按钮
                }, function(){
                    if(!lock) {
                        lock = true;
                        $.ajax({
                            type: "POST",
                            url: baseURL + uri,
                            contentType: "application/json",
                            data: JSON.stringify(data),
                            success: function(r){
                                if(r.code == 200){
                                    layer.msg(r.msg, {icon: 1});
                                    vm.reload();
                                }else{
                                    layer.alert(r.msg);
                                }
                            }
                        });
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
            $.get(baseURL + "user/userLevel/listForSelect", function(r){
                vm.userLevelList = r.list;
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
        getSysUserListByDeptIdForFrom: function(){
            vm.userInfo.sysUserId = null;
            if(vm.userInfo.sysDeptId != null  && vm.userInfo.sysDeptId != ''){
                $.get(baseURL + "sys/user/listForSelectByDeptId/"+vm.userInfo.sysDeptId, function(r){
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
                    "fatherId":vm.q.fatherId,
                    "isMerchant":vm.q.isMerchant,
                    "userLevelId":vm.q.userLevelId,
                    "sysDeptId":vm.q.sysDeptId,
                    "sysUserId":vm.q.sysUserId,
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
            vm.userInfo.header = r.url;
        }else{
            alert(r.msg);
        }
    }

});