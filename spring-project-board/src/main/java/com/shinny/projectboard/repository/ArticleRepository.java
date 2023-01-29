package com.shinny.projectboard.repository;

import com.shinny.projectboard.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ArticleRepository extends JpaRepository<Article, Long> {

}