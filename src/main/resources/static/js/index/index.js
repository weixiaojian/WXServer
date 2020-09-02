$(function () {
    var data4Vue = {
        pageUrl: 'http://m23j177109.iok.la/WXServer/index/index', //回调地址
        auth: '../index/auth', //根据获取用户信息方法
        configUrl: '../msg/getConfig', //获取appid等配置
        appid: '',
        wxUserInfo: {} //保存用户信息
    };
    var vue = new Vue({
        el: '#workingArea',
        data: data4Vue,
        mounted: function () { //mounted　表示这个 Vue 对象加载成功了
            this.getConfig();
        },
        methods: {
            //获取后台appid配置
            getConfig: function () {
                var url = this.configUrl;
                axios.get(url).then(function (res) {
                    if (res.data.code == 0) {
                        vue.appid = res.data.data.appId;
                        vue.enterWxAuthor();
                    }
                });
            },
            //首页初始化
            init: function () {
                var wxUserInfo = localStorage.getItem("wxUserInfo");
                if (wxUserInfo) {
                    this.wxUserInfo = wxUserInfo;
                }
                // 渲染首页
                console.log("渲染首页");
            },
            //判断是否授权
            enterWxAuthor: function () {
                var wxUserInfo = localStorage.getItem("wxUserInfo");
                if (!wxUserInfo) {
                    var code = getUrlParam('code');
                    if (code) {
                        this.getWxUserInfo(code);
                    } else {
                        //没有微信用户信息，没有授权-->> 需要授权，跳转授权页面
                        var authorizeUrl = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=' + this.appid + '&redirect_uri=' + this.pageUrl + '&response_type=code&scope=snsapi_userinfo#wechat_redirect';
                        window.location.href = authorizeUrl;
                    }
                } else {
                    this.init();
                }
            },
            //获取用户信息
            getWxUserInfo: function (par) {
                var code = getUrlParam("code");
                if (par) code = par;
                var url = this.auth + "?code=" + code;
                axios.get(url).then(function (res) {
                    //保证写入的wxUserInfo是正确的
                    if (res.data.code == 0) {
                        console.log(res.data);
                        localStorage.setItem('wxUserInfo', JSON.stringify(res.data.data));//写缓存--微信用户信息
                        vue.init();//重新渲染首页
                    }
                });
            }
        }
    })
});