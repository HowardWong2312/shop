let countryMap;
let categoryMap;

function fetchCountry() {
    let promise = new Promise((resolve, reject) => {
        let data = {};
        $.ajax({
            url: baseURL + 'goods/Country/list',
            type: 'GET',
            dataType: 'json',
            timeout: 5000,
            success: function (res) {
                countryMap = new Map();
                countryMap.set(0, '全部');
                vm.countryList.push({id: 0, name: '全部'});
                for (let i = 0; i < res.page.list.length; i++) {
                    vm.countryList.push({id: res.page.list[i].id, name: res.page.list[i].name});
                    countryMap.set(res.page.list[i].id, res.page.list[i].name);
                }
                resolve(res);
            },
            error: function () {
                reject(new Error('返回错误'))
            }
        })
    })
    return promise
}

function fetchCategory() {
    let promise = new Promise((resolve, reject) => {
        let data = {};
        $.ajax({
            url: baseURL + 'goods/shopGoodsCategory/list',
            type: 'GET',
            dataType: 'json',
            timeout: 5000,
            data: {
                "languageId": 1,
                "page": 1,
                "limit": 1000
            },
            success: function (res) {
                categoryMap = new Map();
                vm.categoryList.push({id: null, name: '全部'});
                for (let i = 0; i < res.page.list.length; i++) {
                    if (res.page.list[i].defaultTitle == null || res.page.list[i].defaultTitle === "") res.page.list[i].defaultTitle = res.page.list[i].languageTtile
                    vm.categoryList.push({id: res.page.list[i].id, name: res.page.list[i].defaultTitle});
                    categoryMap.set(res.page.list[i].id, res.page.list[i].defaultTitle);
                }
                resolve(res);
            },
            error: function () {
                reject(new Error('返回错误'))
            }
        })
    })
    return promise
}

let vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        q: {},
        shopGoods: {},
        countryList: [],
        countrySelected: 0,
        categoryList: [],
        categorySelected: 0,
    },
    created: async function () {
        await fetchCountry();
        await fetchCategory();
        $("#jqGrid").jqGrid({
            url: baseURL + 'goods/shopGoods/list',
            datatype: "json",
            colModel: [
                {label: 'id', name: 'id', index: 'id', width: 30, key: true},
                {
                    label: '国家',
                    name: 'countryId',
                    index: 'country_id',
                    width: 40,
                    formatter: function (value) {
                        return '<p>' + countryMap.get(value) + '</p>';
                    }
                },
                {label: '用户ID-代表商家', name: 'userId', index: 'user_id', width: 40},
                {
                    label: '分类', name: 'categoryId', index: 'category_id', width: 60, formatter: function (value) {
                        return '<p>' + categoryMap.get(value) + '</p>';
                    }
                },
                {label: '商品名称(默认)', name: 'title', index: 'title', width: 150},
                // {label: '备注(默认)', name: 'description', index: 'description', width: 80},
                {
                    label: '产品logo(默认)',
                    width: 80,
                    align: "center",
                    formatter: function (item, index, data) {
                        console.log(data);
                        if (data.logoUrl != null) {
                            return `<a href="javascript:openImg('` + data.logoUrl+"','"+ data.title+ `')"><img  style="width: 32px" src="` + data.logoUrl + `"/></a>`;
                        }
                        return '<span></span>'
                    }
                },

                {
                    label: '外链', name: 'linkUrl', index: 'link_url', width: 80, formatter: function (value) {
                        return value != null && value !== "" ?
                            '<a href="' + value + '" target="_blank" style="text-decoration:none"><span class="label label-success">预览</span></a>' :
                            '<span class="label label-danger">暂无外链</span>';
                    }
                },
                {label: '库存', name: 'stock', index: 'stock', width: 50},
                {label: '价格', name: 'price', index: 'price', width: 60},
                {label: '联系电话', name: 'phone', index: 'phone', width: 80},
                {label: '联系地址', name: 'address', index: 'address', width: 80},
                {
                    label: '拼团',
                    name: 'isGroup',
                    index: 'is_group',
                    align: "center",
                    width: 40,
                    formatter: function (value) {
                        return value === 1 ?
                            '<i class="layui-icon" style="font-size: 24px; color: #21e300;">&#xe605;</i>' :
                            '<i class="layui-icon" style="font-size: 24px; color: #e30000;">&#x1006;</i>';
                    }
                },
                {
                    label: '免费抢', name: 'isRush', index: 'is_rush', align: "center",
                    width: 40, formatter: function (value) {
                        return value === 1 ?
                            '<i class="layui-icon" style="font-size: 24px; color: #21e300;">&#xe605;</i>' :
                            '<i class="layui-icon" style="font-size: 24px; color: #e30000;">&#x1006;</i>';
                    }
                },
                {
                    label: '一元购', name: 'isOneBuy', index: 'is_one_buy', align: "center",
                    width: 40, formatter: function (value) {
                        return value === 1 ?
                            '<i class="layui-icon" style="font-size: 24px; color: #21e300;">&#xe605;</i>' :
                            '<i class="layui-icon" style="font-size: 24px; color: #e30000;">&#x1006;</i>';
                    }
                },
                {
                    label: '商品状态', name: 'status', index: 'status', align: "center",
                    width: 60, formatter: function (value) {
                        return value === 1 ?
                            '<span class="label label-success">上架</span>' :
                            '<span class="label label-danger">下架</span>';
                    }
                },
                {label: '创建时间', name: 'createTime', index: 'create_time', width: 100},
            ],
            viewrecords: true,
            postData: {
                "key": vm.q.key,
                "countryId": vm.countrySelected,
                categoryId: null
            },
            height: 600,
            rowNum: 20,
            rowList: [20, 30, 50],
            rownumWidth: 25,
            autowidth: true,
            multiselect: true,
            pager: "#jqGridPager",
            jsonReader: {
                root: "page.list",
                page: "page.currPage",
                total: "page.totalPage",
                records: "page.totalCount"
            },
            prmNames: {
                page: "page",
                rows: "limit",
                order: "order"
            },
            gridComplete: function () {
                //隐藏grid底部滚动条
                $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
            }
        });
    },
    methods: {
        countryCheck(event) {
            vm.query();
        },
        categoryCheck(event) {
            vm.query();
        },
        groupChange(b) {
            vm.shopGoods.isGroup = b ? 1 : 0;
        },
        rushChange(b) {
            vm.shopGoods.isRush = b ? 1 : 0;
        },
        oneBuyChange(b) {
            vm.shopGoods.isOneBuy = b ? 1 : 0;
        },
        statusChange(b) {
            vm.shopGoods.status = b ? 1 : 0;
        },
        query: function () {
            vm.showList = true;
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    "key": vm.q.key,
                    "countryId": vm.countryList[vm.countrySelected].id,
                    "categoryId": vm.categoryList[vm.categorySelected].id
                },
                page: 1
            }).trigger("reloadGrid");
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.shopGoods = {};
        },
        update: function (event) {
            let id = getSelectedRow();
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
        },
        saveOrUpdate: function (event) {
            $('#btnSaveOrUpdate').button('loading').delay(300).queue(function () {
                let url = vm.shopGoods.id == null ? "goods/shopGoods/save" : "goods/shopGoods/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.shopGoods),
                    success: function (r) {
                        if (r.code == 200) {
                            layer.msg("操作成功", {icon: 1});
                            vm.reload();
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        } else {
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
            if (ids == null) {
                return;
            }
            let lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                if (!lock) {
                    lock = true;
                    $.ajax({
                        type: "delete",
                        url: baseURL + "goods/shopGoods/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function (r) {
                            if (r.code == 200) {
                                layer.msg("操作成功", {icon: 1});
                                vm.reload();
                            } else {
                                layer.alert(r.msg);
                            }
                        }
                    });
                }
            }, function () {
            });
        },
        getInfo: function (id) {
            $.get(baseURL + "goods/shopGoods/info/" + id, function (r) {
                vm.shopGoods = r.shopGoods;
            });
        },
        reload: function (event) {
            vm.showList = true;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    "key": vm.q.key,
                },
                page: page
            }).trigger("reloadGrid");
        }
    }
});
