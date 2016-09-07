package com.zx.stlife.tools;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Created by micheal on 15/9/8.
 */
public class BigDecimalUtils {
    public static final BigDecimal BIG_DECIMAL_0 = new BigDecimal(0);

    public static final BigDecimal BIG_DECIMAL_100 = new BigDecimal(100);

    public static final MathContext MATH_CONTEXT = new MathContext(2, RoundingMode.HALF_UP);


    /**
     * 加法
     * @param num
     * @param nums
     * @return
     */
    public static BigDecimal add(BigDecimal num, BigDecimal... nums){
        if(num == null ||
                num.compareTo(BIG_DECIMAL_0) == -1 ){
            num = BIG_DECIMAL_0;
        }

        if(nums != null){
            for(BigDecimal n : nums){
                if(n == null ||
                        n.compareTo(BIG_DECIMAL_0) < 1 ) {
                    continue;
                }

                num = num.add(n);
            }
        }

        return num;
    }

    /**
     * 减法
     * @param num
     * @param nums
     * @return
     */
    public static BigDecimal subtract(BigDecimal num, BigDecimal... nums){
        if(num == null ||
                num.compareTo(BIG_DECIMAL_0) == -1 ){
            num = BIG_DECIMAL_0;
        }

        if(nums != null){
            for(BigDecimal n : nums){
                if(n == null ||
                        n.compareTo(BIG_DECIMAL_0) < 1 ) {
                    continue;
                }

                num = num.subtract(n);
            }
        }

        if(num.compareTo(BIG_DECIMAL_0) == -1){
            return BIG_DECIMAL_0;
        }
        return num;
    }

    /**
     * 乘法
     * @param num
     * @param nums
     * @return
     */
    public static BigDecimal multiply(BigDecimal num, BigDecimal... nums){
        if(num == null){
            return BIG_DECIMAL_0;
        }

        if(nums != null){
            for(BigDecimal n : nums){
                if(n == null)
                    continue;
                num = num.multiply(n);
            }
        }

        return num.round(MATH_CONTEXT);
    }

    /**
     * 除法
     * @param num
     * @param nums
     * @return
     */
    public static BigDecimal divide(BigDecimal num, BigDecimal... nums){
        if(num == null){
            return BIG_DECIMAL_0;
        }

        if(nums != null){
            for(BigDecimal n : nums){
                if(n == null)
                    continue;
                num = num.divide(n, 2, BigDecimal.ROUND_HALF_UP);
            }
        }

        return num;
    }
}
