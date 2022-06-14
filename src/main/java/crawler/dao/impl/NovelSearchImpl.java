package crawler.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.search.engine.search.aggregation.AggregationKey;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.stereotype.Repository;

import crawler.dao.NovelSearch;
import crawler.dao.jpa.HibernateSearchImpl;
import crawler.entity.Novel;

/**
 * NovelのHibernate Search DAOの実装クラス.
 */
@Repository("novelSearch")
public class NovelSearchImpl extends HibernateSearchImpl<Novel> implements NovelSearch {

    /**
     * デフォルト・コンストラクタ.
     */
    public NovelSearchImpl() {
        super(Novel.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Novel> search(String searchTerm) {
        return Search.session(entityManager).search(Novel.class)
                .where(f -> {
                    if (searchTerm == null) {
                        return f.matchAll();
                    } else {
                        return f.match().fields("title", "writername", "description", "body")
                                    .matching(searchTerm);
                    }
                })
                .sort(f -> f.composite()
                        .add(f.field("titleSort"))
                        .add(f.field("writernameSort"))
                        .add(f.field("descriptionSort")))
                .fetchHits(20);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Long> facet(String field, int maxCount) {
        var searchSession = Search.session(entityManager);
        AggregationKey<Map<String, Long>> countByKey = AggregationKey.of(field);

        SearchResult<Novel> result = searchSession.search(Novel.class)
                .where(f -> f.matchAll())
                .aggregation(countByKey, f -> f.terms()
                        .field(field, String.class)
                        .orderByCountDescending()
                        .minDocumentCount(1)
                        .maxTermCount(maxCount))
                .fetch(20);

        result.hits();

        return result.aggregation(countByKey);
    }
}
