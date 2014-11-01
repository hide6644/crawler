package crawler.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.Search;
import org.hibernate.search.query.facet.Facet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.ObjectRetrievalFailureException;

import crawler.dao.GenericDao;
import crawler.dao.SearchException;

/**
 * 一般的なCRUD DAOの実装クラス.
 */
public class GenericDaoHibernate<T, PK extends Serializable> implements GenericDao<T, PK> {

    /** ログ出力クラス */
    protected final Log log = LogFactory.getLog(getClass());

    /** エンティティクラス */
    private Class<T> persistentClass;

    /** DBセッション生成クラス */
    @Resource
    private SessionFactory sessionFactory;

    /**
     * コンストラクタ.
     *
     * @param persistentClass
     *            エンティティクラス
     */
    public GenericDaoHibernate(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    /**
     * コンストラクタ.
     *
     * @param persistentClass
     *            エンティティクラス
     * @param sessionFactory
     *            セッション生成クラス
     */
    public GenericDaoHibernate(final Class<T> persistentClass, SessionFactory sessionFactory) {
        this.persistentClass = persistentClass;
        this.sessionFactory = sessionFactory;
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.GenericDao#getAll()
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return getSession().createCriteria(persistentClass).list();
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.GenericDao#getAllDistinct()
     */
    @Override
    public List<T> getAllDistinct() {
        Collection<T> result = new LinkedHashSet<T>(getAll());
        return new ArrayList<T>(result);
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.GenericDao#get(java.io.Serializable)
     */
    @Override
    public T get(PK id) {
        IdentifierLoadAccess byId = getSession().byId(persistentClass);
        @SuppressWarnings("unchecked")
        T entity = (T) byId.load(id);

        if (entity == null) {
            log.warn("Uh oh, '" + this.persistentClass + "' object with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(this.persistentClass, id);
        }

        return entity;
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.GenericDao#exists(java.io.Serializable)
     */
    @Override
    public boolean exists(PK id) {
        IdentifierLoadAccess byId = getSession().byId(persistentClass);
        return byId.load(id) != null;
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.GenericDao#save(java.lang.Object)
     */
    @Override
    public T save(T object) {
        getSession().saveOrUpdate(object);
        getSession().flush();
        return object;
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.GenericDao#remove(java.lang.Object)
     */
    @Override
    public void remove(T object) {
        getSession().delete(object);
        getSession().flush();
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.GenericDao#remove(java.io.Serializable)
     */
    @Override
    public void remove(PK id) {
        Session sess = getSession();
        IdentifierLoadAccess byId = sess.byId(persistentClass);
        sess.delete(byId.load(id));
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.GenericDao#findByNamedQuery(java.lang.String,
     * java.util.Map)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams) {
        Query namedQuery = getSession().getNamedQuery(queryName);

        if (queryParams != null) {
            for (String s : queryParams.keySet()) {
                namedQuery.setParameter(s, queryParams.get(s));
            }
        }

        return namedQuery.list();
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.GenericDao#search(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<T> search(String searchTerm) throws SearchException {
        Session sess = getSession();
        FullTextQuery hibQuery = Search.getFullTextSession(sess).createFullTextQuery(
                HibernateSearchTools.generateQuery(searchTerm, persistentClass, sess), persistentClass);

        return hibQuery.list();
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.GenericDao#facet(java.lang.String, int)
     */
    @Override
    public List<Facet> facet(String field, int maxCount) {
        return HibernateSearchTools.generateFacet(field, maxCount, persistentClass, getSession());
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.GenericDao#reindex()
     */
    @Override
    public void reindex() {
        HibernateSearchTools.reindex(persistentClass, getSessionFactory().getCurrentSession());
    }

    /*
     * (非 Javadoc)
     *
     * @see crawler.dao.GenericDao#reindexAll(boolean)
     */
    @Override
    public void reindexAll(boolean async) {
        HibernateSearchTools.reindexAll(async, getSessionFactory().getCurrentSession());
    }

    /**
     * DBセッション生成クラスを取得する.
     *
     * @return DBセッション生成クラス
     */
    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    /**
     * DBセッション生成クラスを設定する.
     *
     * @param sessionFactory
     *            DBセッション生成クラス
     */
    @Autowired
    @Required
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * DBセッションを取得する.
     *
     * @return DBセッション
     * @throws HibernateException
     */
    public Session getSession() throws HibernateException {
        Session sess = getSessionFactory().getCurrentSession();

        if (sess == null) {
            sess = getSessionFactory().openSession();
        }

        return sess;
    }
}
