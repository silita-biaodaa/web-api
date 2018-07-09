package test;

import base.ConfigTest;
import com.silita.common.es.ElaticsearchUtils;
import com.silita.common.es.indexes.IdxExample;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dh on 2018/7/5.
 */
public class TestEs extends ConfigTest{

    @Autowired
    private ElaticsearchUtils elaticsearchUtils;

    @Test
    public void insertESzhaobiao() {
        elaticsearchUtils.deleteIndex("test_es");
        elaticsearchUtils.createIndex(IdxExample.class);
        elaticsearchUtils.createMapping(IdxExample.class);
        List list = new LinkedList();
        for (int i = 0; i < 5; i++) {
            IdxExample ex = new IdxExample();
            ex.setSnatchId("111"+i);
            ex.setTitle("标题"+i);
            ex.setGsDate("2018-7-9");
            list.add(ex);
        }
        elaticsearchUtils.multipleIndexing(list);
    }
}
