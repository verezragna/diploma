package com.verezragna.academy.repository;

import com.verezragna.academy.domain.News;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the News entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {

    @Query("select news from News news where news.user_news.login = ?#{principal.username}")
    List<News> findByUser_newsIsCurrentUser();

}
