import java.io.Serializable;

import net.bndy.cache.CacheKey;

public class TestModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@CacheKey
	public int id;
    public String name;
}
