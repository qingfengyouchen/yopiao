/**
 * Created by micheal on 16/1/26.
 */
var browserUtils={
    versions:function(){
        var u = navigator.userAgent, app = navigator.appVersion;
        return {         //移动终端浏览器版本信息
            isTrident: u.indexOf('Trident') > -1, //IE内核
            isPresto: u.indexOf('Presto') > -1, //opera内核
            isWebKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
            isGecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
            isMobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
            isIos: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            isAndroid: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
            isIPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
            isIPad: u.indexOf('iPad') > -1, //是否iPad
            isWebApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
        };
    }(),
    language:(navigator.browserLanguage || navigator.language).toLowerCase()
};