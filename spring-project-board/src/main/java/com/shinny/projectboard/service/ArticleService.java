package com.shinny.projectboard.service;

import com.shinny.projectboard.domain.Article;
import com.shinny.projectboard.domain.type.SerachType;
import com.shinny.projectboard.dto.ArticleDto;
import com.shinny.projectboard.dto.ArticleWithCommentsDto;
import com.shinny.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SerachType serachType, String searchKeyword, Pageable pageable) {
        if(searchKeyword == null || searchKeyword.isBlank()){
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        switch (serachType){
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword,pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword,pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword,pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag(searchKeyword,pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining("#"+searchKeyword,pageable).map(ArticleDto::from);
        };

        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(()-> new EntityNotFoundException("게시글이 없습니다. - articleId: "+articleId));
    }

    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(dto.id());
            if(dto.title() != null){article.setTitle(dto.title());}
            if(dto.content() != null){article.setContent(dto.content());}
            article.setHashtag(dto.hashtag());
        } catch (EntityNotFoundException e){
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다. - dto: {}", dto);
        }
        // articleRepository.save(article);
        /* save 메서드는 불필요하다. 이미 클래스로 @Transactional 설정이 되어있어,
           이 메서드는 하나의 트랜잭션으로 묶여있고 트랜잭션이 끝날 떄, 영속성 컨텍스트는
           article 이 변한 것을 감지해낸다. 그리고 수정 사항에 대해 update 쿼리가 실행되기 때문.
        */
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
