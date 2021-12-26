$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'goods/shopGoodsOrder/list',
        datatype: "json",
        colModel: [			
			{ label: '订单号', name: 'orderCode', index: 'a.order_code', width: 80, key: true },
			{ label: '商家', name: 'merchantName', index: 'm.nickname', width: 80 },
			{ label: '商品', name: 'goodsName', index: 'a.goods_id', width: 80 },
			{ label: '买家', name: 'userName', index: 'u.nickname', width: 80 },
			{ label: '数量', name: 'quantity', index: 'a.quantity', width: 50 },
			{ label: '订单金额', name: 'amount', index: 'a.amount', width: 50 },
			{ label: '状态', name: 'statusValue', index: 'a.status', width: 50 },
			{ label: '下单时间', name: 'createTime', index: 'a.create_time', width: 80 }
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
		shopGoodsOrder: {}
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
                    "userKey":vm.q.userKey,
                    "merchantKey":vm.q.merchantKey,
                    "status":vm.q.status,
                },
                page:1
            }).trigger("reloadGrid");
        },
		check: function () {
			let orderCode = getSelectedRow();
			if(orderCode == null){
				return ;
			}
            vm.getInfo(orderCode)
            $("#myModal").modal('show');
		},
		getInfo: function(orderCode){
			$.get(baseURL + "goods/shopGoodsOrder/info/"+orderCode, function(r){
                vm.shopGoodsOrder = r.shopGoodsOrder;
            });
		},
        getStatusList: function () {
            $.get(baseURL + "sys/dict/list/goods_order_status", function (r) {
                vm.statusList = r.list;
            });
        },
		reload: function (event) {
			vm.showList = true;
			let page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "key":vm.q.key,
                    "userKey":vm.q.userKey,
                    "merchantKey":vm.q.merchantKey,
                    "status":vm.q.status,
                },
                page:page
            }).trigger("reloadGrid");
		}
	}
});