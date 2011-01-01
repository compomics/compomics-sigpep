package com.compomics.sigpep;

import com.opensymphony.oscache.base.Cache;

/**
 * Factory to create object caches.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 09-Jul-2008<br/>
 * Time: 14:41:09<br/>
 */
public abstract class CacheFactory {

    /** the singleton instance  */
    private static CacheFactory ourInstance;

    /**
     * Returns the singleton instance of this cache factory object.
     *
     * @return the cache factory instance
     */
    public static CacheFactory getInstance() {

        Configuration config = Configuration.getInstance();
        String cacheFactoryClass = config.getString("sigpep.app.cache.factory.class");

        if (ourInstance == null) {

            try {
                ourInstance = (CacheFactory)Class.forName(cacheFactoryClass).newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return ourInstance;
    }

    /**
     * Creates an OS cache.
     *
     * @return cache the cache
     */
    public abstract Cache createCache();
}
