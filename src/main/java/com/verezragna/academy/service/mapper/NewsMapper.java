package com.verezragna.academy.service.mapper;

import com.verezragna.academy.domain.*;
import com.verezragna.academy.service.dto.NewsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity News and its DTO NewsDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface NewsMapper extends EntityMapper<NewsDTO, News> {

    @Mapping(source = "user_news.id", target = "user_newsId")
    @Mapping(source = "user_news.login", target = "user_newsLogin")
    NewsDTO toDto(News news);

    @Mapping(source = "user_newsId", target = "user_news")
    News toEntity(NewsDTO newsDTO);

    default News fromId(Long id) {
        if (id == null) {
            return null;
        }
        News news = new News();
        news.setId(id);
        return news;
    }
}
