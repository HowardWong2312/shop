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
                width: 32,
                formatter: function (value) {
                    if (value != null) {
                        return '<img style="width: 24px" src="' + value + '"/>';
                    }
                    return '<span></span>'
                }
            },
            {
                label: '语言图标',
                name: 'languageIconUrl',
                index: 'language_iconUrl',
                width: 32,
                formatter: function (value) {
                    if (value != null) {
                        return '<img style="width: 24px" src="' + value + '"/>';
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
            // {
            //     label: '操作',
            //     width: 60,
            //     editable:true,
            //     formatter: function (item, index, data) {
            //         return '<span><button type="button"  class="layui-btn layui-btn-sm" @click="addLanguage">增加语言</button><button type="button" class="layui-btn layui-btn-sm layui-btn-warm" >恢复</button></span>'
            //     }
            // }

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

const vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        showAddLanguage: true,
        title: null,
        q: {},
        shopGoodsCategory: {},
        languages: [{title: '中文', id: 0}, {title: '英文', id: 1},],
        selected: 0,
        parentId: 0,
        isParent: 0,
        parentList: [{title: '电商', id: 0}],
        parentSelected: 0
    },
    created: function () {
        this.initLanguageAndCategory();
    },
    methods: {
        languageCheck: function (event) {
            this.query();
        },
        categoryChek: function (event) {
            //查询方法
            this.query();
        },
        initLanguageAndCategory() {
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
                url: baseURL + 'goods/shopGoodsCategory/list',
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
            vm.initLanguageAndCategory();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.shopGoodsCategory = {};
            vm.shopGoodsCategory.languageId = vm.languages[0].id;
            vm.shopGoodsCategory.parentId = vm.parentList[0].id;
        },
        addLanguage: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.showAddLanguage = false;
            vm.shopGoodsCategory = {};
            vm.shopGoodsCategory.languageId = vm.languages[0].id;
            vm.getInfo(id);
        },
        fromParentCheck: function (event) {
            vm.shopGoodsCategory.parentId = vm.parentList[event.target.value].id;
        }
        , fromLanguageCheck: function (event) {
            vm.shopGoodsCategory.languageId = vm.languages[event.target.value].id;
        },
        update: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            vm.shopGoodsCategory.languageId = vm.languages[0].id;
            vm.shopGoodsCategory.parentId = vm.parentList[0].id;
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
        },
        saveOrUpdate: function (event) {
            if (vm.isParent === 0) {
                vm.shopGoodsCategory.parentId = 0;
            }

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
        AddLanguageOrUpdate(event){

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
                        type: "delete",
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
            vm.showAddLanguage = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    "key": vm.q.key,
                },
                page: page
            }).trigger("reloadGrid");
        },

    }
});


layui.use(['layer', 'upload'], function () {
    layui.upload.render({
        elem: '#upload1',
        accept: 'images',
        acceptMime: 'image/*',//只显示图片
        url: baseURL + '/sys/oss/upload',
        field: 'file',
        before: function (obj) {
            layer.load();
        },
        done: function (res) {
            layer.closeAll('loading');
            if (res.code == 200) {
                vm.$set(vm.shopGoodsCategory, 'defaultIconUrl', res.url)
                console.log(vm.shopGoodsCategory);
                return;
            }
            layui.layer.msg('上传失败', {icon: 5});
        },
        error: function () {
            layer.closeAll('loading');
            layui.layer.msg('上传失败', {icon: 5});
        }
    });
    layui.upload.render({
        elem: '#upload2',
        accept: 'images',
        acceptMime: 'image/*',//只显示图片
        url: baseURL + '/sys/oss/upload',
        field: 'file',
        before: function (obj) {
            layer.load();
        },
        done: function (res) {
            layer.closeAll('loading');
            if (res.code == 200) {
                vm.$set(vm.shopGoodsCategory, 'languageIconUrl', res.url)
                console.log(vm.shopGoodsCategory);
                return;
            }
            layui.layer.msg('上传失败', {icon: 5});
        },
        error: function () {
            layer.closeAll('loading');
            layui.layer.msg('上传失败', {icon: 5});
        }
    });
});
