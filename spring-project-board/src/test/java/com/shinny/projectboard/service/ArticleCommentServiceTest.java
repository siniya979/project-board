package com.shinny.projectboard.service;

import com.shinny.projectboard.domain.Article;
import com.shinny.projectboard.domain.ArticleComment;
import com.shinny.projectboard.dto.ArticleCommentDto;
import com.shinny.projectboard.repository.ArticleCommentRepository;
import com.shinny.projectboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks private ArticleCommentService sut;
    @Mock private ArticleCommentRepository articleCommentRepository;
    @Mock private ArticleRepository articleRepository;

    @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트 반환")
    @Test
    public void givenArticleId_whenSearchingComments_thenReturnsComments() throws Exception{
        // given
        Long articleId = 1L;
        given(articleRepository.findById(articleId)).willReturn(Optional.of(
                Article.of("title", "content", "#java")
        ));
        // when
        List<ArticleCommentDto> articleComments = sut.searchArticleComment(1L);
        // then
        assertThat(articleComments).isNotNull();
        then(articleRepository).should().findById(articleId);

     }

    @DisplayName("댓글 정보를 입력하면, 댓글 저장")
    @Test
    public void givenCommentInfo_whenaSavingComments_thenReturnsComments() throws Exception{
        // given
        Long articleId = 1L;
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

        // when
        sut.saveArticleComment(ArticleCommentDto.of(LocalDateTime.now(),"Uno", LocalDateTime.now(), "Uno", "comment"));

        // then
        then(articleCommentRepository).should().save(any(ArticleComment.class));

    }
}