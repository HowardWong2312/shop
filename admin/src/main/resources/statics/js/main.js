let vm = new Vue({
    el: '#main',
    data: {
        user: {},
        statistical: {},
    },
    methods: {
        getUser: function () {
            $.getJSON("sys/user/info?_" + $.now(), function (r) {
                vm.user = r.user;
            });
        },

    },
    created: function () {
        this.getUser();
        $.getJSON("user/userInfo/getTodayFundsAndUserData", function (res) {
            vm.statistical = res.data;
        });
    },
});
