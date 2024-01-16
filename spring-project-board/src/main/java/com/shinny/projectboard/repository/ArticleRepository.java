package com.shinny.projectboard.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.shinny.projectboard.domain.Article;
import com.shinny.projectboard.domain.QArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>, // 모든 필드들에 대한 검색이 열려있음
        QuerydslBinderCustomizer<QArticle> {

    // java8 이후로 인터페이스에 default 메소드가 가능해짐
    @Override
    default void customize(QuerydslBindings bindings, QArticle root){
        bindings.excludeUnlistedProperties(true); // 리스팅하지 않은 프로퍼티는 검색에서 제외
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy); // 허용 필드 설정
        // bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // like '${v}'
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%${v}%'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // 동일검사, 적절한 대안이 없다. 시분초까지 동일하게 넣어줘야 함으로 부적절함.
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }

}