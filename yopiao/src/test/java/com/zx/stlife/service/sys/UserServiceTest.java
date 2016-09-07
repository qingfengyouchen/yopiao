package com.zx.stlife.service.sys;

import com.base.modules.util.SimpleUtils;
import com.zx.stlife.entity.goods.GoodsInfo;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.member.MemberLevel;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.goods.GoodsService;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.member.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Random;

/**
 * Created by micheal on 15/7/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class UserServiceTest {

    private static Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private AccountService accountService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ConfigService configService;

    @Test
    public void createAccount(){
        try{
            FileReader fr = new FileReader("/micheal/project/zx/nicknamelist.txt");
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

                    Member member = new Member(line, 0, 0, memberLevel, user, true, true);
                    memberService.saveMember(member);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void testGetRandom1VirtualUser(){
        User user = accountService.getRandom1VirtualUser();
        logger.info("nickName: {}", user.getNickName());
    }
}
