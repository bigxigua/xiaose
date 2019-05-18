package org.b3log.symphony.cache;

import org.b3log.latke.cache.Cache;
import org.b3log.latke.cache.CacheFactory;
import org.b3log.latke.ioc.Singleton;

/**
 * <p>Create Time: 2019年05月14日</p>
 * <p>@author tangxd</p>
 **/
@Singleton
public class CommunalCache {

    /**
     * Communal cache.
     */
    private static final Cache cache = CacheFactory.getCache("PUBLIC_CACHE");

    public Cache get() {
        return cache;
    }
}
