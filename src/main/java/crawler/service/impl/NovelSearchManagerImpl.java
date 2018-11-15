package crawler.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
    @Override
    public List<Novel> search(String searchTerm) {
        return novelSearch.search(searchTerm).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reindexAll(boolean async) {
        novelSearch.reindexAll(async);
    }
}
