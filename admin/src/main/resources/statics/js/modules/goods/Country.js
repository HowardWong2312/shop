var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url: "nourl"
        }
    }
};
var ztree;

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        menu: {
            parentName: null,
            parentId: 0,
            type: 1,
            orderNum: 0
        }
    },
    methods: {
        getMenu: function (menuId) {
            //加载菜单树
            // $.get(baseURL + "sys/menu/select", function (r) {
            //     ztree = $.fn.zTree.init($("#menuTree"), setting, r.menuList);
            //     var node = ztree.getNodeByParam("id", vm.menu.parentId);
            //     ztree.selectNode(node);
            //
            //     vm.menu.parentName = node.name;
            // })
        },
        addCountry: function () {

            layer.prompt({
                formType: 0,
                value: name,
                title: '新增国家',
                area: ['300px', '28px'] //自定义文本域宽高
            }, function (value, index, elem) {
                layer.load();

                $.ajax({
                    type: "POST",
                    url: baseURL + 'goods/Country/save',
                    contentType: "application/json",
                    data: JSON.stringify({
                        name: value,
                        type:0,
                        parentId:0
                    }),
                    success: function (r) {
                        if (r.code == 200) {
                            alert('操作成功', function () {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                        layer.closeAll('loading');
                        layer.close(index);
                    }
                });
            });
        },
        addProvince: function () {
        //选择国家
        },
        addCity: function () {
        //选择省份
        },
        saveOrUpdate: function () {
            if (vm.validator()) {
                return;
            }

            var url = vm.menu.menuId == null ? "sys/menu/save" : "sys/menu/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.menu),
                success: function (r) {
                    if (r.code == 200) {
                        alert('操作成功', function () {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        menuTree: function () {
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择菜单",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#menuLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级菜单
                    vm.menu.parentId = node[0].menuId;
                    vm.menu.parentName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            Menu.table.refresh();
        },
        validator: function () {
            if (isBlank(vm.menu.name)) {
                alert("菜单名称不能为空");
                return true;
            }

            //菜单
            if (vm.menu.type === 1 && isBlank(vm.menu.url)) {
                alert("菜单URL不能为空");
                return true;
            }
        }
    }
});


var Menu = {
    id: "menuTable",
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Menu.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle', width: '50px'},
        {title: '名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '200px'},
        {
            field: 'operate',
            title: '操作',
            width: '50px',
            align: 'center',
            valign: 'middle',
            formatter: actionFormatter,
        },
        // {title: '归属地', field: 'parentName', align: 'center', valign: 'middle', sortable: true, width: '80px'},

        // {
        //     title: '类型',
        //     field: 'type',
        //     align: 'center',
        //     valign: 'middle',
        //     sortable: true,
        //     width: '100px',
        //     formatter: function (item, index) {
        //         if (item.type === 0) {
        //             return '<span class="label label-primary">目录</span>';
        //         }
        //         if (item.type === 1) {
        //             return '<span class="label label-success">菜单</span>';
        //         }
        //         if (item.type === 2) {
        //             return '<span class="label label-warning">按钮</span>';
        //         }
        //     }
        // },
    ]
    return columns;
};

function actionFormatter(value, row, index) {
    var id = index;
    var result = "";
    result += `<div style="display: flex;justify-content: space-evenly;padding: 0px 35px;"><a  href='javascript:;' onclick="update(${value.id},'${value.name}',${value.type})"  style="text-decoration:none;"><span class="label label-success"  style="padding: 5px 15px;">修改</span></a>`;
    result += `<a href='javascript:;' onclick="del(${value.id},${value.type})"  style="text-decoration:none"><span class="label label-danger" style="padding: 5px 15px;">删除</span></a></div>`;
    return result;
}

function del(id, type) {
    let url = 'goods/Country/delete';
    if (type === 1) {
        url = 'goods/tProvince/delete'
    } else if (type === 2) {
        url = 'goods/tCity/delete'
    }

    layer.confirm('确定要删除选中的记录？', {
        icon: 3, title: '提示',
        zIndex: layer.zIndex
    }, function (index) {
        layer.load(0, {
            zIndex: layer.zIndex + 1
        });
        $.ajax({
            type: "delete",
            url: baseURL + url,
            contentType: "application/json",
            data: JSON.stringify({
                id: id,
                type: type,
            }),
            success: function (r) {
                layer.closeAll();
                if (r.code == 200) {
                    alert('操作成功', function () {
                        vm.reload();
                    });
                } else {
                    alert(r.msg);
                }
            }
        });
    });
}

function update(id, name, type) {

    layer.prompt({
        formType: 0,
        value: name,
        title: '修改',
        area: ['300px', '28px'] //自定义文本域宽高
    }, function (value, index, elem) {
        layer.load();
        let url = 'goods/Country/update';
        if (type === 1) {
            url = 'goods/tProvince/update'
        } else if (type === 2) {
            url = 'goods/tCity/update'
        }
        $.ajax({
            type: "POST",
            url: baseURL + url,
            contentType: "application/json",
            data: JSON.stringify({
                id: id,
                name: value,
                type: type,
            }),
            success: function (r) {
                if (r.code == 200) {
                    alert('操作成功', function () {
                        vm.reload();
                    });
                } else {
                    alert(r.msg);
                }
                layer.closeAll('loading');
                layer.close(index);
            }
        });
    });

}


function getMenuId() {
    var selected = $('#menuTable').bootstrapTreeTable('getSelections');
    if (selected.length === 0) {
        alert("请选择一条记录");
        return null;
    } else {
        console.log(selected)
        return selected[0].id;
    }
}


$(function () {
    var colunms = Menu.initColumn();
    var table = new TreeTable(Menu.id, baseURL + "goods/Country/list", colunms, "id");
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("parentId");
    table.setExpandAll(false);
    table.init();
    Menu.table = table;
});
