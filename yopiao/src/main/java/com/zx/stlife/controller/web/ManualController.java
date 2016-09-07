package com.zx.stlife.controller.web;

import com.base.modules.util.SimpleUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.constant.Const.GoodsImageCategory;
import com.zx.stlife.entity.goods.GoodsImage;
import com.zx.stlife.entity.goods.GoodsInfo;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.member.MemberLevel;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.goods.GoodsService;
import com.zx.stlife.service.goods.GoodsTimesNumService;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.order.SnatchNumService;
import com.zx.stlife.service.order.SnatchRecordService;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.service.sys.ConfigService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by micheal on 15/6/25.
 */
@Controller
@RequestMapping(value = "/manual")
public class ManualController {

    private static Logger logger = LoggerFactory.getLogger(ManualController.class);

    @Autowired
    private GoodsTimesService goodsTimesService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsTimesNumService goodsTimesNumService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private SnatchRecordService snatchRecordService;

    @RequiresRoles(value = Const.ROLE_NAME_SYS_ADMIN)
    @RequestMapping(value = "/createGoodsHtml")
    @ResponseBody
    public String createGoodsHtml() {
        List<GoodsInfo> goodsList = goodsService.findAll();
        List<GoodsImage> goodsDetailUrlList = new ArrayList<GoodsImage>();
        for(GoodsInfo goodsInfo: goodsList) {
            List<GoodsImage> imageList = goodsService.findByGoodsAndCategory(
                    goodsInfo.getId(), Const.GoodsImageCategory.DETAILS);
			for(GoodsImage img : imageList){
				if (img.getUrl().indexOf("http") >= 0) {
					goodsDetailUrlList.add(img);
				}
			}
            goodsService.createHtml(goodsInfo, imageList, goodsDetailUrlList);
        }
        return "success";
    }


    @RequiresRoles(value = Const.ROLE_NAME_SYS_ADMIN)
    @RequestMapping(value = "/createComputeHtml")
    @ResponseBody
    public String createComputeHtml() {
        List<GoodsTimes> goodsTimesList = goodsTimesService.findByState(Const.GoodsTimesState.OVER);
        for(GoodsTimes goodsTimes: goodsTimesList) {
            goodsTimesService.createHtml(goodsTimes);
        }
        return "success";
    }

    @RequiresRoles(value = Const.ROLE_NAME_SYS_ADMIN)
    @RequestMapping(value = "/createAllNotSalesGoods")
    @ResponseBody
    public String createAllNotSalesGoods() {
        List<GoodsInfo> goodsList = goodsService.findByNotSales();
        if(SimpleUtils.isNotNullList(goodsList)){
            for(GoodsInfo goodsInfo : goodsList){
                logger.info("goods name: {}", goodsInfo.getName());
                // 生成商品期号实体
                GoodsTimes goodsTimes = new GoodsTimes(
                        configService.getGoodsTimesNo(), goodsInfo, goodsInfo.getName(),
                        goodsInfo.getTip(), goodsInfo.getThumbnail(), goodsInfo.getIsTenYuan(),
                        goodsInfo.getTotalTimes(), 0, 0);
                goodsTimesService.save(goodsTimes);

                goodsTimesNumService.createNums(goodsTimes);
            }
        }
        System.out.println("over...");

        return "success";
    }

    @RequiresRoles(value = Const.ROLE_NAME_SYS_ADMIN)
    @RequestMapping(value = "/createVirtualUser")
    @ResponseBody
    public String createVirtualUser() {
        try{
            FileReader fr = new FileReader("/data/app/stlife/webapps/nicknamelist.txt");
            BufferedReader br = new BufferedReader(fr);
            String password = "abc123456";
            Random random = new Random();
            MemberLevel memberLevel = new MemberLevel(1);
            while (br.ready()) {
                // 读取一行
                String line = br.readLine();
                if(StringUtils.isNotBlank(line)){
                    if(accountService.isExistsUserName(line, null)){
                        continue;
                    }

                    User user = new User(configService.getUserAccount(),
                            line, password, (byte)random.nextInt(3), true);
                    accountService.saveUser(user, null);

                    Member member = new Member(line, 0, 0, memberLevel, user, true, true,null,null,null,null,null);
                    memberService.saveMember(member);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("over...");

        return "success";
    }

    /*@RequiresRoles(value = Const.ROLE_NAME_SYS_ADMIN)
    @RequestMapping(value = "/getRandom1VirtualUser")
    @ResponseBody
    public String getRandom1VirtualUser() {
        logger.info("user: {}", snatchRecordService.hasVirtualUserBuy(8));
        return "success";
    }*/
}
