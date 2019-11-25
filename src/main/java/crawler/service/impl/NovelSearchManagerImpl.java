package crawler.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import crawler.dao.HibernateSearch;
import crawler.entity.Novel;
import crawler.service.NovelSearchManager;

/**
 * 小説の情報を検索する.
 */
@Service("novelSearchManager")
public class NovelSearchManagerImpl implements NovelSearchManager {

    /** NovelのHibernate Search DAO */
    @Autowired
    @Qualifier("novelSearch")
    HibernateSearch<Novel> novelSearch;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<Novel> search(String searchTerm) {
        return ((Stream<Novel>) novelSearch.search(searchTerm).getResultStream()).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reindexAll(boolean async) {
        novelSearch.reindexAll(async);
    }
}
