package crawler.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.search.query.facet.Facet;

import crawler.dao.GenericDao;
import crawler.service.GenericManager;

/**
 * 一般的なCRUD POJOsの実装クラス.
 */
public class GenericManagerImpl<T, PK extends Serializable> implements GenericManager<T, PK> {

    /** ログ出力クラス */
    protected final Log log = LogFactory.getLog(getClass());

    /** 一般的なCRUD DAOのインターフェース */
    protected GenericDao<T, PK> dao;

    /**
     * デフォルト・コンストラクタ.
     */
    public GenericManagerImpl() {
    }

    /**
     * コンストラクタ.
     *
     * @param genericDao
     *            一般的なCRUD DAOのインターフェース
     */
    public GenericManagerImpl(GenericDao<T, PK> genericDao) {
        this.dao = genericDao;
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.GenericManager#getAll()
     */
    @Override
    public List<T> getAll() {
        return dao.getAll();
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.GenericManager#get(java.io.Serializable)
     */
    @Override
    public T get(PK id) {
        return dao.get(id);
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.GenericManager#exists(java.io.Serializable)
     */
    @Override
    public boolean exists(PK id) {
        return dao.exists(id);
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.GenericManager#save(java.lang.Object)
     */
    @Override
    public T save(T object) {
        return dao.save(object);
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.GenericManager#remove(java.lang.Object)
     */
    @Override
    public void remove(T object) {
        dao.remove(object);
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.GenericManager#remove(java.io.Serializable)
     */
    @Override
    public void remove(PK id) {
        dao.remove(id);
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.GenericManager#search(java.lang.String)
     */
    @Override
    public List<T> search(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().length() == 0) {
            return getAll();
        }

        return dao.search(searchTerm);
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.GenericManager#facet(java.lang.String, int)
     */
    @Override
    public List<Facet> facet(String field, int max) {
        return dao.facet(field, max);
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.GenericManager#reindex()
     */
    @Override
    public void reindex() {
        dao.reindex();
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.service.GenericManager#reindexAll(boolean)
     */
    @Override
    public void reindexAll(boolean async) {
        dao.reindexAll(async);
    }
}
