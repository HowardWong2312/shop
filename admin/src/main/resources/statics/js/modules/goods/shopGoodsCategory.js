$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'goods/shopGoodsCategory/list',
        datatype: "json",
        postData: {
            "parentId": 0,
            "languageId": 1
        },
        colModel: [
            {label: 'id', name: 'id', index: 'sgc.id', width: 50, key: true},
            {label: '父类id', name: 'parentId', index: 'sgc.parent_id', width: 50},
            {label: '默认标题', name: 'defaultTitle', index: 'sgc.title', width: 100},
            {
                label: '默认图标',
                name: 'defaultIconUrl',
                align: "center",
                index: 'sgc.icon_url',
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
                index: 'sgcl.icon_url',
                align: "center",
                width: 32,
                formatter: function (value) {
                    if (value != null) {
                        return '<img style="width: 24px" src="' + value + '"/>';
                    }
                    return '<span></span>'
                }
            },
            {label: '语言标题', name: 'languageTitle', index: 'sgcl.title', width: 80},
            {label: '所属语言', name: 'name', index: 'name', width: 80},
            {label: '排序', name: 'orderNum', index: 'sgc.order_num', width: 50},
            {
                label: '是否删除', name: 'isDel', index: 'sgc.is_del', width: 80, formatter: function (value) {
                    return value === 0 ?
                        '<span class="label label-success">正常</span>' :
                        '<span class="label label-danger">已删除</span>';
                }
            },
            {label: '创建时间', name: 'createTime', index: 'sgc.create_time', width: 80},
            {label: '更新时间', name: 'updateTime', index: 'sgc.create_time', width: 80}
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
        showCategory: false,
        title: null,
        q: {},
        shopGoodsCategory: {},
        languages: [{title: '中文', id: 0}, {title: '英文', id: 1},],
        selected: 0,
        parentId: 0,
        isParent: 0,
        parentList: [{title: '电商', id: 0}],
        parentSelected: 0,
        parentTitle: '无父级分类'
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
        add: async function () {
            vm.showList = false;
            vm.showCategory = false;
            vm.title = "新增";
            vm.shopGoodsCategory = {};
            await vm.getFirstCategoryList();
            vm.shopGoodsCategory.languageId = vm.languages[vm.selected].id;
            vm.shopGoodsCategory.isParent = 0;
            vm.parentSelected = 0;
        },
        addLanguage: async function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.showAddLanguage = false;
            vm.shopGoodsCategory = {};
            await vm.getInfo(id);
            vm.shopGoodsCategory.languageId = vm.languages[vm.selected].id;
        },
        fromParentCheck: function (event) {
            vm.shopGoodsCategory.parentId = vm.parentList[event.target.value].id;
        }
        , fromLanguageCheck: async function (event, reload) {
            if (reload) {
                await vm.getFirstCategoryList();
            }
            vm.shopGoodsCategory.languageId = vm.languages[event.target.value].id;
        },
        update: async function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            await vm.getInfo(id)
            await vm.getFirstCategoryList();
            vm.parentTitle = '';
            let rowData = $("#jqGrid").getRowData(id);
            vm.shopGoodsCategory.defaultTitle = rowData.defaultTitle;
            vm.shopGoodsCategory.languageTitle = rowData.languageTitle;
            vm.shopGoodsCategory.orderNum = rowData.orderNum;
            vm.parentList.forEach(e => {
                if(e.id.toString() === rowData.parentId.toString()){
                    vm.parentTitle = e.title;
                    vm.showCategory = true;
                    return;
                }
            });
            vm.isParent = vm.shopGoodsCategory.parentId;
            vm.shopGoodsCategory.languageId = vm.languages[vm.selected].id;
            vm.showList = false;
            vm.title = "修改";
        },
        async getFirstCategoryList() {
            await $.ajax({
                url: baseURL + `goods/shopGoodsCategory/list?languageId=${vm.languages[vm.selected].id}&parentId=0`,
                type: "GET",
                async: false,
                contentType: "application/json",
                success: function (r) {
                    vm.parentList = [];
                    for (let i = 0; i < r.page.list.length; i++) {
                        vm.parentList.push({
                            title: r.page.list[i].defaultTitle, id: r.page.list[i].id
                        })
                    }
                    if (vm.parentList.length > 0) {
                        vm.shopGoodsCategory.parentId = vm.parentList[0].id;
                    }
                }
            });
        },
        saveOrUpdate: function (event) {
            if (vm.isParent === 0 || vm.isParent === '0') {
                vm.shopGoodsCategory.parentId = 0;
            } else if (vm.isParent === '1' && vm.parentId == null) {
                layer.msg('请选择一级目录');
                return;
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
        AddLanguageOrUpdate(event) {
            console.log(vm.shopGoodsCategory);
            if (vm.shopGoodsCategory.languageId == null) {
                layer.msg('请重新选择语言');
                return;
            }

            $('#btnAddLanguageOrUpdate').button('loading').delay(300).queue(function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + 'goods/shopGoodsCategory/addShopCategoryLang',
                    contentType: "application/json",
                    data: JSON.stringify(vm.shopGoodsCategory),
                    success: function (r) {
                        if (r.code == 200) {
                            layer.msg("操作成功", {icon: 1});
                            vm.reload();
                            $('#btnAddLanguageOrUpdate').button('reset');
                            $('#btnAddLanguageOrUpdate').dequeue();
                        } else {
                            layer.alert(r.msg);
                            $('#btnAddLanguageOrUpdate').button('reset');
                            $('#btnAddLanguageOrUpdate').dequeue();
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
                        type: "delete",
                        url: baseURL + "goods/shopGoodsCategory/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function (r) {
                            if (r.code === 200) {
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
        getInfo: async function (id) {
            await $.get(baseURL + "goods/shopGoodsCategory/info/" + id, function (r) {
                vm.shopGoodsCategory = r.shopGoodsCategory;
                vm.shopGoodsCategory.goodsCategoryId = vm.shopGoodsCategory.id;
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
            if (res.code === 200) {
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
            if (res.code === 200) {
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

    layui.upload.render({
        elem: '#upload3',
        accept: 'images',
        acceptMime: 'image/*',//只显示图片
        url: baseURL + '/sys/oss/upload',
        field: 'file',
        before: function (obj) {
            layer.load();
        },
        done: function (res) {
            layer.closeAll('loading');
            if (res.code === 200) {
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
