package com.compomics.sigpep.impl;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.Config;
import com.opensymphony.oscache.base.persistence.PersistenceListener;
import com.opensymphony.oscache.plugins.diskpersistence.HashDiskPersistenceListener;
import com.compomics.sigpep.CacheFactory;
import com.compomics.sigpep.Configuration;

import java.util.Properties;

/**
 * Cache factory implementation.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 09-Jul-2008<br/>
 * Time: 14:43:29<br/>
 */
public class CacheFactoryImpl extends CacheFactory {

    /**
     * provides access
     */
    private Configuration config = Configuration.getInstance();

    /**
     * Creates an OS cache.
     *
     * @return cache the cache
     */
    public Cache createCache() {

        // configure cache
        Properties p = new Properties();
        p.setProperty("cache.memory", config.getString("sigpep.app.cache.memory"));
        p.setProperty("cache.unlimited.disk", config.getString("sigpep.app.cache.unlimited.disk"));
        p.setProperty("cache.persistence.overflow.only", config.getString("sigpep.app.cache.persistence.overflow.only"));
        p.setProperty("cache.blocking", config.getString("sigpep.app.cache.blocking"));
        //p.setProperty("cache.persistence.class", config.getString("sigpep.app.cache.persistence.class"));
        p.setProperty("cache.algorithm", config.getString("sigpep.app.cache.algorithm"));
        p.setProperty("cache.path", config.getString("sigpep.app.cache.path"));

        Config osCacheConfig = new Config(p);

        // create cache
        Cache retVal = new Cache(config.getBoolean("sigpep.app.cache.memory"),
                config.getBoolean("sigpep.app.cache.unlimited.disk"),
                config.getBoolean("sigpep.app.cache.persistence.overflow.only"),
                config.getBoolean("sigpep.app.cache.blocking"),
                config.getString("sigpep.app.cache.algorithm"),
                config.getInt("sigpep.app.cache.capacity"));

        PersistenceListener pl = new HashDiskPersistenceListener();

        retVal.setPersistenceListener(pl.configure(osCacheConfig));

        return retVal;
    }
}
