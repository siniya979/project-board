package com.shinny.projectboard.service;

import com.shinny.projectboard.domain.Article;
import com.shinny.projectboard.domain.type.SerachType;
import com.shinny.projectboard.dto.ArticleDto;
import com.shinny.projectboard.dto.ArticleUpdateDto;
import com.shinny.projectboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks private ArticleService sut;
    @Mock private ArticleRepository repository;

    @DisplayName("게시글 검색하면, 게시글 리스트 반환")
    @Test
    public void givenSearchParameters_whenSearchingArticles_thenReturnArticleList() throws Exception{
        // given

        // when
        Page<ArticleDto> articles = sut.searchArticles(SerachType.TITLE, "search keyword"); // 제목, 본문, 아이디, 닉네임, 해시태그

        // then
        assertThat(articles).isNotNull();
     }

    @DisplayName("게시글 조회하면, 게시글을 반환")
    @Test
    public void givenId_whenSearchingArticle_thenReturnArticle() throws Exception{
        // given

        // when
        ArticleDto article = sut.searchArticle(1L); // 제목, 본문, 아이디, 닉네임, 해시태그

        // then
        assertThat(article).isNotNull();
    }

    @DisplayName("게시글 정보를 입력하면 게시글 생성")
    @Test
    public void givenArticleInfo_whenSavingArticle_thenSavesArticle() throws Exception{
        // given
        given(repository.save(any(Article.class))).willReturn(null);
        // when
        sut.saveArticle(ArticleDto.of(LocalDateTime.now(),"Uno", "title", "content", "hashtag"));

        // then
        then(repository).should().save(any(Article.class));
     }

    @DisplayName("게시글의 ID와 수정 정보를 입력하면, 게시글 수정")
    @Test
    public void givenArticleIDAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() throws Exception{
        // given
        given(repository.save(any(Article.class))).willReturn(null);
        // when
        sut.updateArticle(1L, ArticleUpdateDto.of("title", "content", "hashtag"));

        // then
        then(repository).should().save(any(Article.class));
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글 삭제")
    @Test
    public void givenArticleID_whenDeletingArticle_thenDeletesArticle() throws Exception{
        // given
        willDoNothing().given(repository).delete(any(Article.class));
        // when
        sut.deleteArticle(1L);

        // then
        then(repository).should().delete(any(Article.class));
    }
}