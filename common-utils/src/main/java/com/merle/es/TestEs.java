package com.merle.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by merle on 2016/7/1.
 */
public class TestEs {

    @Test
    public void testCreatIndex() throws UnknownHostException, JsonProcessingException {
        List<Goods> goodsList = new ArrayList<>();

        String[] r123 = {"r1","r2","r3"};
        String[] r23 = {"r2","r3"};
        goodsList.add(new Goods(1L,"雀巢咖啡", r123));
        goodsList.add(new Goods(2L,"雀巢咖啡", r23));

        goodsList.add(new Goods(3L, "kele", r123));
        goodsList.add(new Goods(4L,"可口可乐", r123));

        ESUtils.createIndex(goodsList);
    }
}
