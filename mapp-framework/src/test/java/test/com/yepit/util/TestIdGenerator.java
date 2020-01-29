package test.com.yepit.util;

import com.yepit.mapp.framework.util.idgenerate.DefaultIdGenerator;
import com.yepit.mapp.framework.util.idgenerate.SnowflakeIdWorker;
import org.junit.Test;

import java.util.HashMap;

public class TestIdGenerator {

    @Test
    public void testGetId() {
        DefaultIdGenerator idGenerator = new DefaultIdGenerator();
        for (int i=0; i <= 10; i++) {
            System.out.println(idGenerator.next());
        }
    }

    @Test
    public void testSnowflakeIdWorker(){
        HashMap<String,String> idList = new HashMap<String,String>();
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        for (int i = 0; i < 1000; i++) {
            long id = idWorker.nextId();
            idList.put(String.valueOf(id),String.valueOf(id));
//            System.out.println(Long.toBinaryString(id));
//            System.out.println(id);
        }
        System.out.println(idList.size());
    }
}
