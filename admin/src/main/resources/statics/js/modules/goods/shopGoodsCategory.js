$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'goods/shopGoodsCategory/list',
        datatype: "json",
        postData: {
            "parentId": 0
        },
        colModel: [
            {label: 'id', name: 'id', index: 'id', width: 50, key: true},
            {label: '默认标题', name: 'defaultTitle', index: 'default_title', width: 100},
            {
                label: '默认图标',
                name: 'defaultIconUrl',
                index: 'default_IconUrl',
                width: 80,
                formatter: function (value) {
                    if (value != null) {
                        return '<img src="' + value + '"/>';
                    }
                    return '<span></span>'
                }
            },
            {label: '语言标题', name: 'languageTitle', index: 'language_title', width: 80},
            {label: '所属语言', name: 'name', index: 'name', width: 80},
            {
                label: '是否删除', name: 'isDel', index: 'is_del', width: 80, formatter: function (value) {
                    return value === 0 ?
                        '<span class="label label-success">正常</span>' :
                        '<span class="label label-danger">已删除</span>';
                }
            },
            {label: '创建时间', name: 'createTime', index: 'create_time', width: 80},
            {label: '更新时间', name: 'updateTime', index: 'update_time', width: 80}
        ],
        viewrecords: true,
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
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        q: {},
        shopGoodsCategory: {},
        languages: [{title: '中文', id: 0}, {title: '英文', id: 1},],
        selected: 0,
        parentId: 0,
        isParent:0,
        parentList:[{title: '电商', id: 0}],
        parentSelected:0
    },
    created: function () {
        $.ajax({
            url: baseURL + 'goods/shopLanguage/list',
            type: "GET",
            contentType: "application/json",
            success: function (r) {
                vm.languages = [];
                for (let i = 0; i < r.page.list.length; i++) {
                    vm.languages.push({
                        title: r.page.list[i].name, id: r.page.list[i].id
                    })
                }
            }
        });
        $.ajax({
            url:  baseURL + 'goods/shopGoodsCategory/list',
            type: "GET",
            contentType: "application/json",
            success: function (r) {
                vm.parentList = [];
                for (let i = 0; i < r.page.list.length; i++) {
                    vm.parentList.push({
                        title: r.page.list[i].defaultTitle, id: r.page.list[i].id
                    })
                }
            }
        });
    },
    methods: {
        languageCheck: function (event) {
            //查询方法
            this.query();
        },
        categoryChek: function (event) {
            //查询方法
            console.log(event.target.value);
            this.query();
        },

        query: function () {
            vm.showList = true;
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    "key": vm.q.key,
                    "languageId": vm.languages[vm.selected].id,
                    "parentId": vm.parentId
                },
                page: 1
            }).trigger("reloadGrid");
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.shopGoodsCategory = {};
        },
        update: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
        },
        saveOrUpdate: function (event) {
            $('#btnSaveOrUpdate').button('loading').delay(300).queue(function () {
                var url = vm.shopGoodsCategory.id == null ? "goods/shopGoodsCategory/save" : "goods/shopGoodsCategory/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.shopGoodsCategory),
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
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }
            var lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                if (!lock) {
                    lock = true;
                    $.ajax({
                        type: "POST",
                        url: baseURL + "goods/shopGoodsCategory/delete",
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
            $.get(baseURL + "goods/shopGoodsCategory/info/" + id, function (r) {
                vm.shopGoodsCategory = r.shopGoodsCategory;
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    "key": vm.q.key,
                },
                page: page
            }).trigger("reloadGrid");
        }
    }
});
