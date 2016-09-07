package com.shopping.view.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shopping.core.mv.JModelAndView;
import com.shopping.core.security.support.SecurityUserHolder;
import com.shopping.core.tools.CommUtil;
import com.shopping.core.tools.Md5Encrypt;
import com.shopping.foundation.domain.Goods;
import com.shopping.foundation.domain.GoodsBrand;
import com.shopping.foundation.domain.GoodsCart;
import com.shopping.foundation.domain.GoodsClass;
import com.shopping.foundation.domain.GoodsFloor;
import com.shopping.foundation.domain.Group;
import com.shopping.foundation.domain.Store;
import com.shopping.foundation.domain.StoreCart;
import com.shopping.foundation.domain.SysConfig;
import com.shopping.foundation.domain.User;
import com.shopping.foundation.service.IAccessoryService;
import com.shopping.foundation.service.IArticleClassService;
import com.shopping.foundation.service.IArticleService;
import com.shopping.foundation.service.IBargainGoodsService;
import com.shopping.foundation.service.IDeliveryGoodsService;
import com.shopping.foundation.service.IGoodsBrandService;
import com.shopping.foundation.service.IGoodsCartService;
import com.shopping.foundation.service.IGoodsClassService;
import com.shopping.foundation.service.IGoodsFloorService;
import com.shopping.foundation.service.IGoodsService;
import com.shopping.foundation.service.IGroupGoodsService;
import com.shopping.foundation.service.IGroupService;
import com.shopping.foundation.service.IMessageService;
import com.shopping.foundation.service.INavigationService;
import com.shopping.foundation.service.IPartnerService;
import com.shopping.foundation.service.IRoleService;
import com.shopping.foundation.service.IStoreCartService;
import com.shopping.foundation.service.IStoreService;
import com.shopping.foundation.service.ISysConfigService;
import com.shopping.foundation.service.IUserConfigService;
import com.shopping.foundation.service.IUserService;
import com.shopping.manage.admin.tools.MsgTools;
import com.shopping.view.web.tools.GoodsFloorViewTools;
import com.shopping.view.web.tools.GoodsViewTools;
import com.shopping.view.web.tools.NavViewTools;
import com.shopping.view.web.tools.StoreViewTools;

@Controller
public class WxIndexViewAction {

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private IUserConfigService userConfigService;

	@Autowired
	private IGoodsClassService goodsClassService;

	@Autowired
	private IGoodsBrandService goodsBrandService;

	@Autowired
	private IPartnerService partnerService;

	@Autowired
	private IRoleService roleService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IArticleClassService articleClassService;

	@Autowired
	private IArticleService articleService;

	@Autowired
	private IAccessoryService accessoryService;

	@Autowired
	private IMessageService messageService;

	@Autowired
	private IStoreService storeService;

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private INavigationService navigationService;

	@Autowired
	private IGroupGoodsService groupGoodsService;

	@Autowired
	private IGroupService groupService;

	@Autowired
	private IGoodsFloorService goodsFloorService;

	@Autowired
	private IBargainGoodsService bargainGoodsService;

	@Autowired
	private IDeliveryGoodsService deliveryGoodsService;

	@Autowired
	private IStoreCartService storeCartService;

	@Autowired
	private IGoodsCartService goodsCartService;

	@Autowired
	private NavViewTools navTools;

	@Autowired
	private GoodsViewTools goodsViewTools;

	@Autowired
	private StoreViewTools storeViewTools;

	@Autowired
	private MsgTools msgTools;

	@Autowired
	private GoodsFloorViewTools gf_tools;
	
	/** wx首页业务逻辑begin */
	
	/**
	 * wx首页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping( { "/wx/*.htm" } )
	public ModelAndView wapindex( HttpServletRequest request, HttpServletResponse response ) {
		String target = "wx/index.html";
		String url = request.getRequestURI();
		// TODO 此处处理暂时的，以后要改
		if (!url.endsWith("index.htm")) {
			target = url.replace("/yopiao", "") + "l";
		}
		
		ModelAndView mv = new JModelAndView(target, this.configService.getSysConfig(), this.userConfigService.getUserConfig(), 1, request, response );
//		//设置为wap访问
//		request.getSession().setAttribute("shopping_view_type", "wx");
//		Map params = new HashMap();
//		params.put( "display", Boolean.valueOf( true ) );
//		List gcs = this.goodsClassService.query( "select obj from GoodsClass obj where obj.parent.id is null and obj.display=:display order by obj.sequence asc", params, 0, 15 );
//		mv.addObject( "gcs", gcs );
//		params.clear();
//		params.put( "audit", Integer.valueOf( 1 ) );
//		params.put( "recommend", Boolean.valueOf( true ) );
//		List gbs = this.goodsBrandService.query( "select obj from GoodsBrand obj where obj.audit=:audit and obj.recommend=:recommend order by obj.sequence", params, -1, -1 );
//		mv.addObject( "gbs", gbs );
//		params.clear();
//		List img_partners = this.partnerService.query( "select obj from Partner obj where obj.image.id is not null order by obj.sequence asc", params, -1, -1 );
//		mv.addObject( "img_partners", img_partners );
//		List text_partners = this.partnerService.query( "select obj from Partner obj where obj.image.id is null order by obj.sequence asc", params, -1, -1 );
//		mv.addObject( "text_partners", text_partners );
//		params.clear();
//		params.put( "mark", "news" );
//		List acs = this.articleClassService.query( "select obj from ArticleClass obj where obj.parent.id is null and obj.mark!=:mark order by obj.sequence asc", params, 0, 9 );
//		mv.addObject( "acs", acs );
//		params.clear();
//		params.put( "store_recommend", Boolean.valueOf( true ) );
//		params.put( "goods_status", Integer.valueOf( 0 ) );
//		List store_reommend_goods_list = this.goodsService.query( "select obj from Goods obj where obj.store_recommend=:store_recommend and obj.goods_status=:goods_status order by obj.store_recommend_time desc", params, 0, 6 );
//		List store_reommend_goods = new ArrayList();
//		int max = store_reommend_goods_list.size() >= 21 ? 20 : store_reommend_goods_list.size() - 1;
//		for( int i = 0; i <= max; i++ ) {
//			store_reommend_goods.add( (Goods)store_reommend_goods_list.get( i ) );
//		}
//		mv.addObject( "store_reommend_goods", store_reommend_goods );
//		
//		mv.addObject( "store_reommend_goods_count", Double.valueOf( Math.ceil( CommUtil.div( Integer.valueOf( store_reommend_goods_list.size() ), Integer.valueOf( 5 ) ) ) ) );
//		mv.addObject( "goodsViewTools", this.goodsViewTools );
//		mv.addObject( "storeViewTools", this.storeViewTools );
//		if( SecurityUserHolder.getCurrentUser() != null ) {
//			mv.addObject( "user", this.userService.getObjById( SecurityUserHolder.getCurrentUser().getId() ) );
//		}
//		params.clear();
//		params.put( "beginTime", new Date() );
//		params.put( "endTime", new Date() );
//		List groups = this.groupService.query( "select obj from Group obj where obj.beginTime<=:beginTime and obj.endTime>=:endTime", params, -1, -1 );
//		if( groups.size() > 0 ) {
//			params.clear();
//			params.put( "gg_status", Integer.valueOf( 1 ) );
//			params.put( "gg_recommend", Integer.valueOf( 1 ) );
//			params.put( "group_id", ((Group)groups.get( 0 )).getId() );
//			List ggs = this.groupGoodsService.query( "select obj from GroupGoods obj where obj.gg_status=:gg_status and obj.gg_recommend=:gg_recommend and obj.group.id=:group_id order by obj.gg_recommend_time desc", params, 0, 1 );
//			if( ggs.size() > 0 )
//				mv.addObject( "group", ggs.get( 0 ) );
//		}
//		params.clear();
//		params.put( "bg_time", CommUtil.formatDate( CommUtil.formatShortDate( new Date() ) ) );
//		params.put( "bg_status", Integer.valueOf( 1 ) );
//		List bgs = this.bargainGoodsService.query( "select obj from BargainGoods obj where obj.bg_time=:bg_time and obj.bg_status=:bg_status", params, 0, 5 );
//		mv.addObject( "bgs", bgs );
//		params.clear();
//		params.put( "d_status", Integer.valueOf( 1 ) );
//		params.put( "d_begin_time", new Date() );
//		params.put( "d_end_time", new Date() );
//		List dgs = this.deliveryGoodsService.query( "select obj from DeliveryGoods obj where obj.d_status=:d_status and obj.d_begin_time<=:d_begin_time and obj.d_end_time>=:d_end_time order by obj.d_audit_time desc", params, 0, 3 );
//		mv.addObject( "dgs", dgs );
//		
//		List msgs = new ArrayList();
//		if( SecurityUserHolder.getCurrentUser() != null ) {
//			params.clear();
//			params.put( "status", Integer.valueOf( 0 ) );
//			params.put( "reply_status", Integer.valueOf( 1 ) );
//			params.put( "from_user_id", SecurityUserHolder.getCurrentUser().getId() );
//			params.put( "to_user_id", SecurityUserHolder.getCurrentUser().getId() );
//			msgs = this.messageService.query( "select obj from Message obj where obj.parent.id is null and (obj.status=:status and obj.toUser.id=:to_user_id) or (obj.reply_status=:reply_status and obj.fromUser.id=:from_user_id) ", params, -1, -1 );
//		}
//		Store store = null;
//		if( SecurityUserHolder.getCurrentUser() != null )
//			store = this.storeService.getObjByProperty( "user.id", SecurityUserHolder.getCurrentUser().getId() );
//		mv.addObject( "store", store );
//		mv.addObject( "navTools", this.navTools );
//		mv.addObject( "msgs", msgs );
//		List<GoodsCart> list = new ArrayList<GoodsCart>();
//		List<StoreCart> cart = new ArrayList<StoreCart>();
//		List<StoreCart> user_cart = new ArrayList<StoreCart>();
//		List<StoreCart> cookie_cart = new ArrayList<StoreCart>();
//		User user = null;
//		if( SecurityUserHolder.getCurrentUser() != null ) {
//			user = this.userService.getObjById( SecurityUserHolder.getCurrentUser().getId() );
//		}
//		String cart_session_id = "";
//		params.clear();
//		Cookie[] cookies = request.getCookies();
//		if( cookies != null ) {
//			for( Cookie cookie : cookies ) {
//				if( cookie.getName().equals( "cart_session_id" ) ) {
//					cart_session_id = CommUtil.null2String( cookie.getValue() );
//				}
//			}
//		}
//		if( user != null ) {
//			if( !cart_session_id.equals( "" ) ) {
//				if( user.getStore() != null ) {
//					params.clear();
//					params.put( "cart_session_id", cart_session_id );
//					params.put( "user_id", user.getId() );
//					params.put( "sc_status", Integer.valueOf( 0 ) );
//					params.put( "store_id", user.getStore().getId() );
//					List<StoreCart> store_cookie_cart = this.storeCartService.query( "select obj from StoreCart obj where (obj.cart_session_id=:cart_session_id or obj.user.id=:user_id) and obj.sc_status=:sc_status and obj.store.id=:store_id",
//					    params, -1, -1 );
//					for( StoreCart sc : store_cookie_cart ) {
//						for( GoodsCart gc : ((StoreCart)sc).getGcs() ) {
//							gc.getGsps().clear();
//							this.goodsCartService.delete( gc.getId() );
//						}
//						this.storeCartService.delete( ((StoreCart)sc).getId() );
//					}
//				}
//
//				params.clear();
//				params.put( "cart_session_id", cart_session_id );
//				params.put( "sc_status", Integer.valueOf( 0 ) );
//				cookie_cart = this.storeCartService.query( "select obj from StoreCart obj where obj.cart_session_id=:cart_session_id and obj.sc_status=:sc_status", params, -1, -1 );
//
//				params.clear();
//				params.put( "user_id", user.getId() );
//				params.put( "sc_status", Integer.valueOf( 0 ) );
//				user_cart = this.storeCartService.query( "select obj from StoreCart obj where obj.user.id=:user_id and obj.sc_status=:sc_status", params, -1, -1 );
//			}
//			else {
//				params.clear();
//				params.put( "user_id", user.getId() );
//				params.put( "sc_status", Integer.valueOf( 0 ) );
//				user_cart = this.storeCartService.query( "select obj from StoreCart obj where obj.user.id=:user_id and obj.sc_status=:sc_status", params, -1, -1 );
//			}
//
//		}
//		else if( !cart_session_id.equals( "" ) ) {
//			params.clear();
//			params.put( "cart_session_id", cart_session_id );
//			params.put( "sc_status", Integer.valueOf( 0 ) );
//			cookie_cart = this.storeCartService.query( "select obj from StoreCart obj where obj.cart_session_id=:cart_session_id and obj.sc_status=:sc_status", params, -1, -1 );
//		}
//
//		for( StoreCart sc : user_cart ) {
//			boolean sc_add = true;
//			for( StoreCart sc1 : cart ) {
//				if( sc1.getStore().getId().equals( sc.getStore().getId() ) ) {
//					sc_add = false;
//				}
//			}
//			if( sc_add )
//				cart.add( sc );
//		}
//		boolean sc_add;
//		for( StoreCart sc : cookie_cart ) {
//			sc_add = true;
//			for( StoreCart sc1 : cart ) {
//				if( sc1.getStore().getId().equals( sc.getStore().getId() ) ) {
//					sc_add = false;
//					for( GoodsCart gc : sc.getGcs() ) {
//						gc.setSc( sc1 );
//						//gc.setSc( sc );
//						this.goodsCartService.update( gc );
//					}
//					this.storeCartService.delete( sc.getId() );
//				}
//			}
//			if( sc_add ) {
//				cart.add( sc );
//			}
//		}
//		if( cart != null ) {
//			for( StoreCart sc : cart ) {
//				if( sc != null ) {
//					list.addAll( sc.getGcs() );
//				}
//			}
//		}
//		float total_price = 0.0F;
//		for( GoodsCart gc : list ) {
//			Goods goods = this.goodsService.getObjById( gc.getGoods().getId() );
//			if( CommUtil.null2String( gc.getCart_type() ).equals( "combin" ) )
//				total_price = CommUtil.null2Float( goods.getCombin_price() );
//			else {
//				total_price = CommUtil.null2Float( Double.valueOf( CommUtil.mul( Integer.valueOf( gc.getCount() ), goods.getGoods_current_price() ) ) ) + total_price;
//			}
//		}
//		mv.addObject( "total_price", Float.valueOf( total_price ) );
//		mv.addObject( "cart", list );
		
		return mv;
	}


	 public String showLoadFloorAjaxHtml(List lists, int i, String url, Map<String, Object> map) {

		   String img = null;
		   String loadimg = map.get("imageWebServer")+"/resources/style/common/images/loader.gif";
		   String errorimg = map.get("webPath")+"/"+map.get("goodsImagePath")+"/"+map.get("goodsImageName");
		   String goods_url = null;
		   String goods_class_url = null;
		   String child_goods_class_url = null;
				   
		   GoodsFloor floor = (GoodsFloor)lists.get(0);
				   
		   img = null;
		   
		   StringBuffer sb = new StringBuffer(1000);
		   sb.append("<div class='floor "+floor.getGf_css()+"'>").append("<div class='floor_box' id='floor_"+i+"'>");
		   sb.append("<div class='floor_menu'>").append("<div class='title'>").append("<div class='txt-type'>").append("<span>").append(i).append("</span>");
		   sb.append("<h2 title='").append(floor.getGf_name()).append("'>").append(floor.getGf_name()).append("</h2></div></div><div class='flr_m_details'><ul class='flr_m_du'>");
		   List<GoodsClass> gcs = this.gf_tools.generic_gf_gc(floor.getGf_gc_list());
		   for(GoodsClass gc : gcs){
			   goods_class_url = map.get("webPath")+"/store_goods_list_"+gc.getId()+".htm";
			   sb.append("<li><h4><a href='").append(goods_class_url).append("'>").append(gc.getClassName()).append("</a></h4><p>");
			   for(GoodsClass c_gc : gc.getChilds()){
				   child_goods_class_url = map.get("webPath")+"/store_goods_list_"+c_gc.getId()+".htm";
				   sb.append("<span><a href='").append(child_goods_class_url).append("' target='_blank'>").append(c_gc.getClassName()).append("</a></span>");
			   }
			   sb.append("</p></li>");
		   }
		   sb.append("</ul><div class='flr_advertisment'>");
		   //拼接左侧广告
		   sb.append(gf_tools.generic_adv(url,floor.getGf_left_adv()));
		   
		   sb.append("</div></div></div><div class='floorclass'><ul class='floorul'>");
		   
		   int num = 0;
		   for(GoodsFloor info : floor.getChilds()){
			   num++;
			   sb.append("<li ");
			   if(num==1){
				   sb.append("class='this'");
			   }
			   sb.append("style='cursor:pointer;' id='").append(info.getId()).append("' store_gc='").append(floor.getId()).append("' >");
			   sb.append(info.getGf_name()).append("<s></s></li>");
		   }
		   sb.append("</ul>");
		   
		   int count = 0;
		   
		   for(GoodsFloor info : floor.getChilds()){
			   
			   count++;
			   sb.append("<div id='").append(info.getId()).append("' store_gc='").append(floor.getId()).append("' class='ftab'");
			   if(count>1){
				   sb.append("style='display:none;'");
			   }
			   sb.append("><div class='ftabone'><div class='classpro'>");
			   for(Goods goods : this.gf_tools.generic_goods(info.getGf_gc_goods())){
				   if(goods!=null){
					   if(goods.getGoods_main_photo()!=null)
				           img=map.get("imageWebServer")+"/"+goods.getGoods_main_photo().getPath()+"/"+goods.getGoods_main_photo().getName()+"_small."+goods.getGoods_main_photo().getExt();
				       else
				    	   img=map.get("imageWebServer")+"/"+map.get("goodsImagePath")+"/"+map.get("goodsImageName");
				       
				       goods_url=map.get("webPath")+"/goods_"+goods.getId()+".htm";
				       
				       if((Boolean)map.get("IsSecondDomainOpen")) {
				    	   goods_url="http://"+goods.getGoods_store().getStore_second_domain()+"."+map.get("domainPath")+"/goods_"+goods.getId()+".htm";
				       }
				       sb.append("<div class='productone'><ul class='this'><li><span class='center_span'>");
				       //sb.append("<p><a href='").append(goods_url).append("' target='_blank' ><img src='").append(loadimg).append("' original='");
				       //sb.append(img).append("' onerror=\"this.src=").append(errorimg).append(";\" width='28' height='28'/></a></p></span></li>");
				       sb.append("<p><a href='").append(goods_url).append("' target='_blank' ><img src='").append(img).append("' original='");
				       sb.append(img).append("' onerror=\"this.src=").append(errorimg).append(";\" /></a></p></span></li>");
				       sb.append("<li class='pronames'><a href='").append(goods_url).append("' target='_blank'>").append(goods.getGoods_name()).append("</a></li>");
				       sb.append("<li><span class=\"hui2\">市场价：</span><span class=\"through hui\">¥").append(goods.getGoods_price());
				       sb.append("</span></li><li><span class=\"hui2\">商城价：</span><strong class=\"red\">¥").append(goods.getGoods_current_price());
				       sb.append("</strong></li></ul></div>");
				   }
			   }
			   sb.append("</div></div></div>");
		   }
		   sb.append("</div><div class='ranking'>");
		   Map<String, Object> mmap = gf_tools.generic_goods_list(floor.getGf_list_goods());
		   sb.append("<h1>").append(mmap.get("list_title")).append("</h1>");
		   
		   if(mmap.get("goods1")!=null){
			   Goods goods=(Goods)mmap.get("goods1");
			   if(goods.getGoods_main_photo()!=null)
		           img=map.get("imageWebServer")+"/"+goods.getGoods_main_photo().getPath()+"/"+goods.getGoods_main_photo().getName()+"_small."+goods.getGoods_main_photo().getExt();
		       else
		    	   img=map.get("imageWebServer")+"/"+map.get("goodsImagePath")+"/"+map.get("goodsImageName");
			   
			   goods_url=map.get("webPath")+"/goods_"+goods.getId()+".htm";
			   
			   sb.append("<ul class=\"rankul\"><li class=\"rankimg\"><a href='").append(goods_url).append("' target=\"_blank\">");
			   //sb.append("<img src='").append(loadimg).append("' original='").append(img).append("' onerror=\"this.src='").append(errorimg).append("';\" width='28' height='28'/>");
			   sb.append("<img src='").append(img).append("' original='").append(img).append("' onerror=\"this.src='").append(errorimg).append("';\"  width='73' height='73'/>");
			   sb.append("</a><span class=\"rankno1\"></span></li><li class=\"rankhui\"><strong><a href='").append(goods_url).append("' target=\"_blank\">");
			   sb.append(CommUtil.substring(goods.getGoods_name(),12)).append("</a></strong></li><li class=\"rankmoney\">¥").append(goods.getGoods_current_price());
			   sb.append("</li></ul>");
		   }
		   
		   if(mmap.get("goods2")!=null){
			   Goods goods=(Goods)mmap.get("goods2");
			   if(goods.getGoods_main_photo()!=null)
		           img=map.get("imageWebServer")+"/"+goods.getGoods_main_photo().getPath()+"/"+goods.getGoods_main_photo().getName()+"_small."+goods.getGoods_main_photo().getExt();
		       else
		    	   img=map.get("imageWebServer")+"/"+map.get("goodsImagePath")+"/"+map.get("goodsImageName");
			   
			   goods_url=map.get("webPath")+"/goods_"+goods.getId()+".htm";
			   
			   sb.append("<ul class=\"rankul\"><li class=\"rankimg\"><a href='").append(goods_url).append("' target=\"_blank\">");
			 //sb.append("<img src='").append(loadimg).append("' original='").append(img).append("' onerror=\"this.src='").append(errorimg).append("';\" width='28' height='28'/>");
			   sb.append("<img src='").append(img).append("' original='").append(img).append("' onerror=\"this.src='").append(errorimg).append("';\"  width='73' height='73'/>");
			   sb.append("</a><span class=\"rankno1\"></span></li><li class=\"rankhui\"><strong><a href='").append(goods_url).append("' target=\"_blank\">");
			   sb.append(CommUtil.substring(goods.getGoods_name(),12)).append("</a></strong></li><li class=\"rankmoney\">¥").append(goods.getGoods_current_price());
			   sb.append("</li></ul>");
		   }
		   
		   if(mmap.get("goods3")!=null){
			   Goods goods=(Goods)mmap.get("goods3");
			   if(goods.getGoods_main_photo()!=null)
		           img=map.get("imageWebServer")+"/"+goods.getGoods_main_photo().getPath()+"/"+goods.getGoods_main_photo().getName()+"_small."+goods.getGoods_main_photo().getExt();
		       else
		    	   img=map.get("imageWebServer")+"/"+map.get("goodsImagePath")+"/"+map.get("goodsImageName");
			   
			   goods_url=map.get("webPath")+"/goods_"+goods.getId()+".htm";
			   
			   sb.append("<ul class=\"rankul\"><li class=\"rankimg\"><a href='").append(goods_url).append("' target=\"_blank\">");
			 //sb.append("<img src='").append(loadimg).append("' original='").append(img).append("' onerror=\"this.src='").append(errorimg).append("';\" width='28' height='28'/>");
			   sb.append("<img src='").append(img).append("' original='").append(img).append("' onerror=\"this.src='").append(errorimg).append("';\"  width='73' height='73'/>");
			   sb.append("</a><span class=\"rankno1\"></span></li><li class=\"rankhui\"><strong><a href='").append(goods_url).append("' target=\"_blank\">");
			   sb.append(CommUtil.substring(goods.getGoods_name(),12)).append("</a></strong></li><li class=\"rankmoney\">¥").append(goods.getGoods_current_price());
			   sb.append("</li></ul>");
		   }
		   
		   sb.append("<ul class=\"rankul2\">");
		   if(mmap.get("goods4")!=null){
			   Goods goods=(Goods)mmap.get("goods4");
			   goods_url=map.get("webPath")+"/goods_"+goods.getId()+".htm";
			   sb.append("<li><a href='").append(goods_url).append("' target='_blank'>").append(CommUtil.substring(goods.getGoods_name(),14)).append("</a></li>");
		   }
		   if(mmap.get("goods5")!=null){
			   Goods goods=(Goods)mmap.get("goods5");
			   goods_url=map.get("webPath")+"/goods_"+goods.getId()+".htm";
			   sb.append("<li><a href='").append(goods_url).append("' target='_blank'>").append(CommUtil.substring(goods.getGoods_name(),14)).append("</a></li>");
		   }
		   if(mmap.get("goods6")!=null){
			   Goods goods=(Goods)mmap.get("goods6");
			   goods_url=map.get("webPath")+"/goods_"+goods.getId()+".htm";
			   sb.append("<li><a href='").append(goods_url).append("' target='_blank'>").append(CommUtil.substring(goods.getGoods_name(),14)).append("</a></li>");
		   }
		   sb.append("</ul><div class=\"rank_advertisment\">");
		   //拼接右侧广告
		   sb.append(this.gf_tools.generic_adv(url, floor.getGf_right_adv()));
		   sb.append("</div></div></div><div class=\"floor_brand\"><span class=\"fl_brand_sp\"></span><span class=\"flr_sp_brand\">");
		   
		   for(GoodsBrand brand : this.gf_tools.generic_brand(floor.getGf_brand_list())){
			   String brand_url = map.get("webPath")+"/brand_goods_"+brand.getId()+".htm";
			   String brand_img = map.get("imageWebServer") + "/" + brand.getBrandLogo().getPath() + "/" + brand.getBrandLogo().getName();
			   sb.append("<a href='").append(brand_url).append("' target='_blank'><img src='").append(brand_img).append("' width='98' height='35' /></a>");
		   }
		   sb.append("</span></div></div>");
	 
	     return sb.toString();
	   }
	   
	
	
	
}
