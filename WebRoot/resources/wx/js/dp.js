//选项卡切换
$(document).ready(function () {  
 	$(".tab-nav ul li").navtabs("","","");	
}); 
(function($){
	//选项卡切换
	$.fn.navtabs=function(dpevent, activecls, boxcls){
		
		$(this).bind(dpevent,function(){   
			$(this).addClass(activecls).siblings().removeClass(activecls);
			$(boxcls).eq($(this).index()).show().siblings().hide();	
		});
	
	}
})(jQuery);  

//返回顶部
$(function () {
    $(window).scroll(function () {
        var scrollTop = $(window).scrollTop();
        scrollTop > 100 ? $("#gotop").fadeIn(1000).css("display", "block") : $("#gotop").fadeOut(1000);
    });
    $('#gotop').click(function (e) {
        e.preventDefault();
        $('html,body').animate({ scrollTop: 0 });
    });
});

