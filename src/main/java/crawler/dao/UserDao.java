package crawler.dao;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

import crawler.entity.User;

/**
 * ユーザーのDAOのインターフェイス.
 */
public interface UserDao extends JpaRepository<User, String> {

    /**
     * ユーザーの未読小説の一覧を取得する.
     *
     * @return 小説の一覧
     */
    Stream<User> findByUnreadTrueOrderByTitleAndNovelChapterId();
}
