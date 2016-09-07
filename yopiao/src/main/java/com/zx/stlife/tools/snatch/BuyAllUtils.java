package com.zx.stlife.tools.snatch;

import com.base.modules.cache.memcached.SpyMemcachedClient;
import com.fasterxml.jackson.databind.JavaType;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.memcached.MemcachedObjectType;
import org.springframework.beans.factory.annotation.Autowired;

import static com.zx.stlife.constant.Const.jsonMapper;

/**
 * Created by micheal on 16/1/17.
 */
public class BuyAllUtils {

    private BuyAllUtils() {
    }

    public static BuyAllParams getBuyAllParams(Integer goodsTimesId,
                                               SpyMemcachedClient spyMemcachedClient){
        String key = getKey(goodsTimesId);
        String jsonStr = spyMemcachedClient.get(key);
        if(jsonStr == null)
            return null;

        BuyAllParams buyAllParams = jsonMapper.fromJson(jsonStr, BuyAllParams.class);
        return buyAllParams;
    }

    public static void saveBuyAllParamsInCache(BuyAllParams buyAllParams,
                                               SpyMemcachedClient spyMemcachedClient){
        String key = getKey(buyAllParams.getGoodsTimesId());
        buyAllParams.setFullTime(
                DateUtils.addMinutes(DateUtils.getNow(), buyAllParams.getTakeTime()));
        String jsonStr = jsonMapper.toJson(buyAllParams);
        spyMemcachedClient.safeSet(key, buyAllParams.getTakeTime() * 60, jsonStr);
    }


    private static String getKey(Integer goodsTimesId){
        return GoodsTimes.class.getSimpleName() + ":" + goodsTimesId;
    }
}
