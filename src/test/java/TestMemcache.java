import net.bndy.cache.MemcachedCache;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TestMemcache {

    @Test public void ping() {
        try {
            MemcachedClient mc = new MemcachedClient(AddrUtil.getAddresses("127.0.0.1:11211"));
            mc.set("1", 2, 1);
            Assert.assertEquals(mc.get("1"), 1);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Assert.assertEquals(mc.get("1"), null);
            mc.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test public void factory() {
        MemcachedCache mem = new MemcachedCache ("127.0.0.1:11211,127.0.0.1:11212");
        TestModel tm = new TestModel();
        tm.setId(1);
        tm.setName("Bing");
        mem.set(tm, 0);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(mem.get(tm.getClass().getName(), TestModel.class).getName(), "Bing");
    }
}
