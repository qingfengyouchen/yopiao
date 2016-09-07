package com.zx.stlife.service.goods;

import com.base.jpa.query.Page;

import com.base.jpa.query.Query;
import com.base.modules.util.*;
import com.google.common.collect.Maps;
import com.zx.stlife.base.exception.BizException;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.goods.GoodsInfo;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.record.BuyRecord;
import com.zx.stlife.repository.jpa.goods.GoodsInfoDao;
import com.zx.stlife.repository.jpa.goods.GoodsTimesDao;
import com.zx.stlife.service.record.BuyRecordService;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.FreeMarkerUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.zx.stlife.constant.Const.*;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class GoodsTimesService {

    private static Logger logger = LoggerFactory
            .getLogger(GoodsTimesService.class);

    private static final String HQL_FIND_TOP_GOODSTIMES =
            "select g from GoodsTimes g where g.state=?1 order by g.openTime desc";

    private static final String HQL_QUERY_LAST_OPEN =
            "select g from GoodsTimes g where g.fullTime<=?1 and g.state=?2 order by g.fullTime desc";

    private static final String QL_FIND_TOP_BY_TOTAL_TIMES_DESC =
            "select t from GoodsTimes t where t.state=?1 order by t.totalTimes desc";

    private static final String QL_UPDATE_THUMBNAIL = "update GoodsTimes set thumbnail=?2 where goodsInfo.id=?1";

    private static final String QL_UPDATE_GOODS_NAME = "update GoodsTimes set goodsName=?2 where goodsInfo.id=?1";

    private static final String QL_UPDATE_GOODS_STATE = "update GoodsTimes set exchangeState=?2 where id=?1";
    
    private static final String QL_UPDATE_GOODS_LOGISTICS = "update GoodsTimes set exchangeState=?2, logisticsInfo=?3 where id=?1";

    @Autowired
    private GoodsTimesDao goodsTimesDao;

    @Autowired
    private ConfigService configService;

    @Autowired
    private GoodsTimesNumService goodsTimesNumService;

    @Autowired
    private BuyRecordService buyRecordService;
    @Autowired
    private GoodsInfoDao goodsInfoDao;

    public GoodsTimes get(Integer id) {
        return goodsTimesDao.findOne(id);
    }

    @Transactional
    public void save(GoodsTimes entity) {
        entity = goodsTimesDao.save(entity);
    }

    @Transactional(readOnly = true)
    public void findGoodsTimesByCategory(Page<GoodsTimes> page, Date date,
                                         Integer tenYuan, Integer goodsCategoryId) {
        Query query = new Query();
        query.table("select t from GoodsTimes t");

        if (tenYuan != null) {
            Boolean isTenYuan = false;
            if (tenYuan.intValue() == 1) {
                isTenYuan = true;
            }
            query.eq("t.isTenYuan", isTenYuan);
        }

        if (goodsCategoryId != null) {
            query.eq("t.goodsInfo.goodsCategory.id", goodsCategoryId);
        }

        query.eq("t.state", Const.GoodsTimesState.GOING);
        query.le("t.createTime", date);
        query.orderBy("t.totalTimes desc");

        goodsTimesDao.queryPage(page, query.getQLString(), query.getValues());
    }

    @Transactional(readOnly = true)
    public void findGoodsTimesByAttr(Page<GoodsTimes> page, Date date,
                                     Integer category, String order) {
        Query query = new Query();
        query.table("select t from GoodsTimes t");
        query.eq("t.state", Const.GoodsTimesState.GOING);
        query.le("createTime", date);

        if (StringUtils.isBlank(order)) {
            order = "desc";
        }

        if(category != null ) {
            int type = category.intValue();
            if (type == 1) {// 按商品人气排序
                query.orderBy("t.goodsInfo.salesAmount " + order);
            } else if (type == 2) {// 按商品最新排序
                query.orderBy("t.goodsInfo.createTime " + order);
            } else if (type == 3) {// 按夺宝进度排序
                query.orderBy("t.snatchProgress " + order);
            } else if (type == 4) {// 按夺宝总需人次排序
                query.orderBy("t.totalTimes " + order);
            }
        }else{
            query.orderBy("t.totalTimes desc");
        }
        
        System.out.println(query.getQLString());
        Set set = query.getValues().keySet();
        for(Iterator iter = set.iterator(); iter.hasNext();) {
        	String key = (String)iter.next();
        	//String value = (String)query.getValues().get(key);
        	System.out.println(key);
        }
        
        
        goodsTimesDao.queryPage(page, query.getQLString(), query.getValues());
    }

    @Transactional(readOnly = true)
    public void findGoodsTimesByAttrAndName(Page<GoodsTimes> page, Date date,
                                     Integer category, String order, String goodName) {
        Query query = new Query();
        query.table("select t from GoodsTimes t");
        query.eq("t.state", Const.GoodsTimesState.GOING);
        query.le("createTime", date);
        if (StringUtils.isNotEmpty(goodName)) {
            query.like("t.goodsName", goodName);
        }

        if (StringUtils.isBlank(order)) {
            order = "desc";
        }

        if(category != null ) {
            int type = category.intValue();
            if (type == 1) {// 按商品人气排序
                query.orderBy("t.goodsInfo.salesAmount " + order);
            } else if (type == 2) {// 按商品最新排序
                query.orderBy("t.goodsInfo.createTime " + order);
            } else if (type == 3) {// 按夺宝进度排序
                query.orderBy("t.snatchProgress " + order);
            } else if (type == 4) {// 按夺宝总需人次排序
                query.orderBy("t.totalTimes " + order);
            }
        }else{
            query.orderBy("t.totalTimes desc");
        }
        goodsTimesDao.queryPage(page, query.getQLString(), query.getValues());
    }

    @Transactional(readOnly = true)
    public void getRecord(Date date, Page<GoodsTimes> page, Integer goodsId) {
        Query query = new Query();
        query.table("select g from GoodsTimes g ")
                .ne("g.state", Const.GoodsTimesState.GOING)
                .eq("g.goodsInfo.id", goodsId).le("g.createTime", date)
                .orderBy("g.createTime desc");
        goodsTimesDao.queryPage(page, query.getQLString(), query.getValues());
    }

    public void search(Page page, Map<String, String> params, boolean isExcludeOverState) {
        Query query = new Query();
        query.table("select t from GoodsTimes t")
                .eq("t.goodsInfo.goodsCategory.id",
                        SimpleUtils.stringToInteger(params.get("goodsCategoryId")))
                .like("t.goodsName", params.get("goodsName"))
                .eq("t.state", SimpleUtils.stringToByte(params.get("state")));
        if(isExcludeOverState){
            query.ne("t.state", GoodsTimesState.OVER);
        }
        query.orderBy("t.createTime desc,t.snatchProgress desc");
        goodsTimesDao.queryPage(page, query.getQLString(), query.getValues());
    }

    public void searchWinng(Page page, Map<String, String> params) {
        Query query = new Query();
        query.table("select t from GoodsTimes t")
                .eq("t.times", SimpleUtils.stringToInteger(params.get("times")))
                .like("t.goodsName", params.get("goodsName"))
                .eq("t.exchangeState", SimpleUtils.stringToInteger(params.get("exchangeState")))
                .eq("t.state", Const.GoodsTimesState.OVER)
                .orderBy("t.times desc");
        goodsTimesDao.queryPage(page, query.getQLString(), query.getValues());
    }

    @Transactional
    public void saveGoodsTimes(GoodsTimes entity) {
        goodsTimesDao.save(entity);
    }


    /**
     * 5.5. 获取最新中奖用户列表
     */
    public List<GoodsTimes> winngUser() {
        List<GoodsTimes> goodsTimes = goodsTimesDao.findTop(10, HQL_FIND_TOP_GOODSTIMES, Const.GoodsTimesState.OVER);
        return goodsTimes;
    }

    /**
     * 获取最新一期
     * @param gid
     * @return
     */
    public GoodsTimes getLastByGoods(Integer gid){
        return goodsTimesDao.getLastByGoods(gid, Const.GoodsTimesState.GOING);
    }
    
    /**
     * 根据期号times获取goodsTimes
     * @param times
     * @return
     */
    public GoodsTimes getByTimes(Integer times){
        return goodsTimesDao.getByTimes(times);
    }

    /**
     * 是否有最新一期
     * @param gid
     * @return
     */
    public boolean hasLastByGoods(Integer gid){
        Integer a = goodsTimesDao.hasLastByGoods(gid, Const.GoodsTimesState.GOING);
        return a != null && a > 0;
    }
    /**
     * 根据中奖用户查询商品期号
     * @return
     */
    public void searchGoodsTimeList(Page<GoodsTimes> page,Date date,Integer winngUserId) {
        Query query = new Query();
        query.table("select t from GoodsTimes t")
                .eq("t.winngUser.id",winngUserId)
                //.eq("t.winngState", WinngState.OVER)
                .eq("t.hasShareGoods", true)
                .le("t.openTime", date)
                .orderBy("t.hasShareGoods,t.openTime desc");
        goodsTimesDao.queryPage(page, query.getQLString(), query.getValues());
    }
    /**
     * 根据中奖用户查询商品期号
     * @return
     */
    public void searchGoodsTimeListWithoutState(Page<GoodsTimes> page,Date date,Integer winngUserId) {
        Query query = new Query();
        query.table("select t from GoodsTimes t")
                .eq("t.winngUser.id",winngUserId)
                .le("t.openTime", date)
                .orderBy("t.hasShareGoods,t.openTime desc");
        goodsTimesDao.queryPage(page, query.getQLString(), query.getValues());
    }
    /**
     * 是否有最新一期
     * @param gid
     * @return
     */
    public boolean hasLast(Integer gid){
        Integer amount = goodsTimesDao.getLastCount(gid, Const.GoodsTimesState.GOING);
        return amount != null && amount > 0;
    }
    /**
     * 获取最新一期的Timesid
     * @param gid
     * @return
     */
    public Integer getNewestTimesid(Integer gid){
        Integer timesId = goodsTimesDao.getNewestTimesid(gid, Const.GoodsTimesState.GOING);
        return timesId;
    }

    /**
     * 产生下一期
     * @param goodsTimes
     */
    @Transactional
    public void createNext(GoodsTimes goodsTimes){
        GoodsInfo goodsInfo = goodsTimes.getGoodsInfo();
        if(goodsInfo.getState() == Const.GoodsState.ENABLE) {
            GoodsTimes newGoodsTimes = new GoodsTimes();
            newGoodsTimes.setTimes(configService.getGoodsTimesNo());
            newGoodsTimes.setGoodsInfo(goodsInfo);
            newGoodsTimes.setIsTenYuan(goodsInfo.getIsTenYuan());
            newGoodsTimes.setGoodsName(goodsInfo.getName());
            newGoodsTimes.setGoodsTip(goodsInfo.getTip());
            newGoodsTimes.setTotalTimes(goodsInfo.getTotalTimes());
            newGoodsTimes.setThumbnail(goodsInfo.getThumbnail());
            save(newGoodsTimes);

            goodsTimesNumService.createNums(newGoodsTimes);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void computeAndSaveBuyTimes(GoodsTimes goodsTimes, int actualBuyTimes)
            throws BizException, Exception{
        int totalBuyTimes = goodsTimes.getTotalBuyTimes() + actualBuyTimes;

        if(totalBuyTimes > goodsTimes.getTotalTimes()){
            throw new BizException(101, "期号id"+goodsTimes.getId()
                    + "，已超过总需人次，不能购买" + actualBuyTimes + "人次。");
        }
        int snatchProgress = 0;
        if (totalBuyTimes == goodsTimes.getTotalTimes()) { // 已买满
            snatchProgress = 100;
            goodsTimes.setState(Const.GoodsTimesState.WAITING);
        } else {
            snatchProgress = Math.round(100.0f * totalBuyTimes / goodsTimes.getTotalTimes());
            if (snatchProgress == 100) {
                snatchProgress = 99;
            }else if(snatchProgress == 0){
                snatchProgress = 1;
            }
        }
        goodsTimes.setSnatchProgress(snatchProgress);
        goodsTimes.setTotalBuyTimes(totalBuyTimes);

        save(goodsTimes);
    }
    
    /**
	 * 修改用户头像
	 * 
	 * @param userHeadImg
	 * @param userId
	 */
	@Transactional
	public void updateUserHeadImg(String userHeadImg, Integer userId) {
		goodsTimesDao.updateUserHeadImg(userHeadImg, userId);
	}


    @Transactional
    public void createHtml(GoodsTimes goodsTimes){
        String htmlUrl = goodsTimes.getComputeDetailUri();
        if(StringUtils.isBlank(htmlUrl)){
            htmlUrl = goodsTimes.getGoodsInfo().getId() + "/" + configService.getStaticHtmlNo()+".html";
            goodsTimes.setComputeDetailUri(htmlUrl);
        }
        String htmlPath = StringUtils.substringBefore(htmlUrl, "?");
        goodsTimes.setComputeDetailUri(htmlPath + "?" + DateUtilsEx.getCurrentMMDDHHMMSS());
        save(goodsTimes);

        List<BuyRecord> buyRecordList = buyRecordService.findByGoodsTimes(goodsTimes.getId());
        List<Map<String, Object>> mapList = ConvertUtils.convertCollectionToListMap(
                buyRecordList, "snatchTimeStr", "timeValue", "userNickName");
        Map<String, Object> model = Maps.newHashMap();
        model.put("ctx", ROOT_URI);
        model.put("goodsTimes", goodsTimes);
        model.put("buyRecordList", mapList);
        model.put("cqsscDateYYYYMMDD",
                DateUtilsEx.formatDate(goodsTimes.getOpenTime(), DateUtilsEx.FORMAT_YYYYMMDD));

        FreeMarkerUtils.createHtml("compute-detail.ftl", model, GOODS_TIMES_HTML_ROOT_PATH, htmlPath);
    }

    public List<GoodsTimes> findByState(Byte state){
        return goodsTimesDao.findByState(state);
    }

    /**
     * 查询最新揭晓
     * @param page
     * @param date
     */
    public void findLastOpen(Page<GoodsTimes> page, Date date){
        goodsTimesDao.queryPage(page, HQL_QUERY_LAST_OPEN, date.getTime(), GoodsTimesState.OVER);
    }
    
    
    /**
     * 微信版首页查询前10个商品期号
     */
    public List<GoodsTimes> findTop(){
    	List<GoodsTimes> goodsTimesList = goodsTimesDao.findTop(
                10, QL_FIND_TOP_BY_TOTAL_TIMES_DESC,Const.GoodsTimesState.GOING);
    	return goodsTimesList;
    }

    /**产生商品期号及期号夺宝号码*/
    public void createByGoods(GoodsInfo goodsInfo) {
        if (!hasLastByGoods(goodsInfo.getId())){
            // 生成商品期号实体
            GoodsTimes goodsTimes = new GoodsTimes(
                    configService.getGoodsTimesNo(), goodsInfo, goodsInfo.getName(),
                    goodsInfo.getTip(), goodsInfo.getThumbnail(), goodsInfo.getIsTenYuan(),
                    goodsInfo.getTotalTimes(), 0, 0);
            save(goodsTimes);
            goodsTimesNumService.createNums(goodsTimes);
        }
    }

    /**
     * 获取商品期号的状态
     * @param id
     * @return
     */
    public Byte getState(Integer id){
        return goodsTimesDao.getState(id);
    }

    @Transactional
    public void updateHasShareGoods(Integer id, boolean hasShareGoods){
        goodsTimesDao.updateHasShareGoods(hasShareGoods, id);
    }

    /**
     * 按 商品id/期号id分目录
     * @param id
     * @return
     */
    public String getFoldName(Integer id){
        Integer goodsId = goodsInfoDao.getIdByGoodsTimes(id);
        String node = FileUtilsEx.joinPaths(String.valueOf(goodsId), String.valueOf(id));
        return node;
    }

    public boolean isWinner(Integer id, Integer userId){
        Integer amount = goodsTimesDao.countByIdAndUser(id, userId);
        return amount != null && amount > 0;
    }

    @Transactional
    public void updateThumbnail(Integer goodsId, String thumbnail){
        goodsTimesDao.executeUpdate(QL_UPDATE_THUMBNAIL, goodsId, thumbnail);
    }

    @Transactional
    public void updateGoodsName(Integer goodsId, String goodsName){
        goodsTimesDao.executeUpdate(QL_UPDATE_GOODS_NAME, goodsId, goodsName);
    }
    
    @Transactional
    public void updateGoodsState(Integer goodsId, Integer state){
        goodsTimesDao.executeUpdate(QL_UPDATE_GOODS_STATE, goodsId, state);
    }
    
    @Transactional
    public void updateGoodsLogistics(Integer goodsId, Integer state, String logistics){
        goodsTimesDao.executeUpdate(QL_UPDATE_GOODS_LOGISTICS, goodsId, state, logistics);
    }
}
