
let vm = new Vue({
    el: '#main',
    data: {
        user: {},
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
    },
});
