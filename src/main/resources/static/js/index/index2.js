$(document).ready(function() {
    center.getConfig();
    scan();
})
// 自己的那个微信appid
var WX_APPID = "";
// 此路径需和微信接口配置一样
var pageUrl = "http://m23j177109.iok.la/WXServer/index/index2";
var center = {
    getConfig: function () {
        $.get('../msg/getConfig',function (res) {
            if(res.code == 0){
                WX_APPID = res.data.appId;
                center.enterWxAuthor();
            }
        });
    },
    init: function(){
        var wxUserInfo = localStorage.getItem("wxUserInfo");
        if (wxUserInfo) {
            $("#workingArea").text(wxUserInfo);
            $("#workingArea").append("WX_APPID：" + WX_APPID);
        }
        // 渲染首页
        console.log("渲染首页");
    },
    enterWxAuthor: function(){
        var wxUserInfo = localStorage.getItem("wxUserInfo");
        if (!wxUserInfo) {
            var code = getUrlParam('code');
            if (code) {
                getWxUserInfo(code);
            }else{
                //没有微信用户信息，没有授权-->> 需要授权，跳转授权页面
                var authorizeUrl ='https://open.weixin.qq.com/connect/oauth2/authorize?appid='+ WX_APPID+'&redirect_uri='+ pageUrl+'&response_type=code&scope=snsapi_userinfo#wechat_redirect';
                window.location.href =authorizeUrl;
            }
        }else{
            center.init();
        }
    }
}

/**
 * 后台授权 并获取用户信息
 * @param par
 */
function getWxUserInfo(par){
    var code = getUrlParam("code");
    if (par) code = par;
    var authorizationUrl = "../user/auth";
    $.get(authorizationUrl,{code:code},function (res) {
        //保证写入的wxUserInfo是正确的
        if(res.code == 0){
            localStorage.setItem('wxUserInfo',JSON.stringify(res.data));//写缓存--微信用户信息
            center.init();//渲染首页
        }
    },"json");
}

/**
 * 微信扫一扫功能
 */
function scan() {
    $.get('../msg/getJsConfig?url=' + window.location.href, function (res) {
        wx.config({
            debug: false,
            appId: res.appId,
            timestamp: res.timestamp,
            nonceStr: res.nonceStr,
            signature: res.signature,
            jsApiList: ['checkJsApi', 'scanQRCode', 'chooseImage',
                'previewImage', 'uploadImage', 'downloadImage', 'getLocation', 'openLocation', 'chooseLocation'
                , 'startRecord', 'stopRecord', 'playVoice', 'uploadVoice', 'updateAppMessageShareData']
        });
        wx.ready(function () {
            var voice = {
                localId: '',
                serverId: ''
            };
            wx.checkJsApi({
                jsApiList: ['checkJsApi', 'scanQRCode', 'chooseImage', 'previewImage',
                    'uploadImage', 'downloadImage', 'uploadVoice', 'updateAppMessageShareData'],
                success: function (res) {
                }
            });
            $("#scandate").click(function () {
                wx.scanQRCode({
                    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
                    scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
                    success: function (res) {
                        console.log(res);
                    }
                });
            })

        });
    }, "json");
}