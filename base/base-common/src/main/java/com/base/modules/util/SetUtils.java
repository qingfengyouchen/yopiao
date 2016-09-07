package com.base.modules.util;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by micheal on 15/7/15.
 */
public class SetUtils {

    public static <T> TreeSet<T> transferToTreeSet(Set<T> set){
        if(SimpleUtils.isNullList(set)){
            return null;
        }
        if(set instanceof TreeSet){
            return (TreeSet<T>)set;
        }

        TreeSet<T> treeSet = new TreeSet<>();
        treeSet.addAll(set);

        return treeSet;
    }
}
