package com.zx.stlife.tools;

import com.zx.stlife.base.exception.BizException;

import java.util.Random;

/**
 * Created by micheal on 15/12/17.
 */
public class RandomUitls {

    private static Random random = new Random();

    private RandomUitls(){}

    /**
     * 产生指定范围内的数字
     * @param maxInt 小于这个最大数值
     * @return
     */
    public static int randomInt(int maxInt){
        return random.nextInt(maxInt);
    }

    public static void main(String[] args) {

        int[] nums = generateNums(true, 300, 10);
        int total = 0;
        for (int i = 0; i < nums.length; i++) {
            System.out.println( i + ": " + nums[i]);
            total += nums[i];
        }
        //检查生成的数值的总额是否正确
        System.out.println("total:" + total);
    }

    public static int[] generateNums(boolean isTenYuan, int total, int amount) throws BizException{
        try {
            if (isTenYuan) {
                total /= 10;
            }
            int max = total / amount * 6;
            int min = 1;

            int[] nums = generateNums(total, amount, max, min);
            if (isTenYuan) {
                for (int i = 0; i < nums.length; i++) {
                    nums[i] = nums[i] * 10;
                }
            }

            return nums;
        }catch (Exception ex){
            throw new BizException();
        }
    }

    /**
     * 生产min和max之间的随机数，但是概率不是平均的，从min到max方向概率逐渐加大。
     * 先平方，然后产生一个平方值范围内的随机数，再开方，这样就产生了一种“膨胀”再“收缩”的效果。
     *
     * @param min
     * @param max
     * @return
     */
    static int xRandom(int min, int max) {
        return sqrt(randomInt(sqr(max - min)));
    }

    /**
     *
     * @param total
     *            总数
     * @param count
     *            个数
     * @param max
     *            最大值
     * @param min
     *            最小值
     * @return 存放生成的每个小数值的值的数组
     */
    public static int[] generateNums(int total, int count, int max, int min) {
        int[] result = new int[count];

        int average = total / count;

        int a = average - min;
        int b = max - min;

        //这样的随机数的概率实际改变了，产生大数的可能性要比产生小数的概率要小。
        //这样就实现了大部分数值的值在平均数附近。大数值和小数值比较少。
        int range1 = sqr(average - min);
        int range2 = sqr(max - average);

        for (int i = 0; i < result.length; i++) {
            //因为小数值的数量通常是要比大数值的数量要多的，因为这里的概率要调换过来。
            //当随机数>平均值，则产生小数值
            //当随机数<平均值，则产生大数值
            if (nextint(min, max) > average) {
                // 在平均线上减钱
//              int temp = min + sqrt(nextint(range1));
                int temp = min + xRandom(min, average);
                result[i] = temp;
                total -= temp;
            } else {
                // 在平均线上加钱
//              int temp = max - sqrt(nextint(range2));
                int temp = max - xRandom(average, max);
                result[i] = temp;
                total -= temp;
            }
        }
        // 如果还有余钱，则尝试加到小数值里，如果加不进去，则尝试下一个。
        while (total > 0) {
            for (int i = 0; i < result.length; i++) {
                if (total > 0 && result[i] < max) {
                    result[i]++;
                    total--;
                }
            }
        }
        // 如果钱是负数了，还得从已生成的小数值中抽取回来
        while (total < 0) {
            for (int i = 0; i < result.length; i++) {
                if (total < 0 && result[i] > min) {
                    result[i]--;
                    total++;
                }
            }
        }
        return result;
    }

    static int sqrt(int n) {
        // 改进为查表？
        return (int) Math.sqrt(n);
    }

    static int sqr(int n) {
        // 查表快，还是直接算快？
        return n * n;
    }

    static int nextint(int min, int max) {
        return randomInt(max - min + 1) + min;
    }
}
