package crawler.dao.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.facet.Facet;

import crawler.dao.GenericDao;
import crawler.dao.SearchException;

/**
 * 一般的なCRUD DAOの実装クラス.
 */
public class GenericDaoJpa<T, PK extends Serializable> implements GenericDao<T, PK> {

    /** ログ出力クラス */
    protected final Logger log = LogManager.getLogger(getClass());

    /** Entity Managerクラス名 */
    public static final String PERSISTENCE_UNIT_NAME = "ApplicationEntityManager";

    /** Entity Managerクラス */
    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    protected EntityManager entityManager;

    /** エンティティクラス */
    private Class<T> persistentClass;

    /** 形態解析クラス */
    private Analyzer defaultAnalyzer;

    /**
     * コンストラクタ.
     *
     * @param persistentClass
     *            エンティティクラス
     */
    public GenericDaoJpa(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
        defaultAnalyzer = new StandardAnalyzer();
    }

    /**
     * コンストラクタ.
     *
     * @param persistentClass
     *            エンティティクラス
     * @param entityManager
     *            Entity Managerクラス
     */
    public GenericDaoJpa(Class<T> persistentClass, EntityManager entityManager) {
        this.persistentClass = persistentClass;
        this.entityManager = entityManager;
        defaultAnalyzer = new StandardAnalyzer();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAll() {
        return entityManager.createQuery("select obj from " + persistentClass.getName() + " obj").getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getAllDistinct() {
        return new ArrayList<T>(new LinkedHashSet<T>(getAll()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get(PK id) {
        T entity = entityManager.find(persistentClass, id);

        if (entity == null) {
            String msg = "Uh oh, '" + persistentClass + "' object with id '" + id + "' not found...";
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(PK id) {
        return entityManager.find(persistentClass, id) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T save(T object) {
        return entityManager.merge(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(T object) {
        entityManager.remove(entityManager.contains(object) ? object : entityManager.merge(object));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(PK id) {
        entityManager.remove(get(id));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams) {
        Query namedQuery = entityManager.createNamedQuery(queryName);

        if (queryParams != null) {
            for (String s : queryParams.keySet()) {
                namedQuery.setParameter(s, queryParams.get(s));
            }
        }

        return namedQuery.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> search(String searchTerm) throws SearchException {
        org.apache.lucene.search.Query qry = HibernateSearchJpaTools.generateQuery(searchTerm, persistentClass, entityManager, defaultAnalyzer);

        return Search.getFullTextEntityManager(entityManager).createFullTextQuery(qry, persistentClass).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Facet> facet(String field, int maxCount) {
        return HibernateSearchJpaTools.generateFacet(field, maxCount, persistentClass, entityManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reindex() {
        HibernateSearchJpaTools.reindex(persistentClass, getEntityManager());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reindexAll(boolean async) {
        HibernateSearchJpaTools.reindexAll(async, getEntityManager());
    }
}