package com.shinny.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAT"),
        @Index(columnList = "createdBy")
})
@Entity
public class Article extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    private UserAccount userAccount;

    // Setter 를 필드에 따로 세팅하는 이유는 특정 데이터만 client 가 값을 변경하도록 하기 위함.
    @Setter
    @Column(nullable = false)
    private String title; // 제목
    @Setter
    @Column(nullable = false, length = 10000)
    private String content; // 내용

    // 옵션 설정이 없을 경우 @Column 붙이지 않아도 자동으로 스프링이 붙여준다.
    @Setter private String hashtag; // 해시태그

    @ToString.Exclude
    @OrderBy("createdAt DESC") // id 기준으로 정렬
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    protected Article() {}

    private Article(UserAccount userAccount, String title, String content, String hashtag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    // 생성자를 private 으로 막고 팩토리 메소드를 이용해 제공.
    public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
        return new Article(userAccount, title, content, hashtag);
    }

    // 동일성 동등성 검사를 위한 equals/hashCode 메서드 오버라이딩
    // 클래스에 @EqualsANndHashCode 를 사용하면 필드값 전부를 검사하는데,
    // 사실상 유니크키인 id 값만 비교해도 되기 때문에 필드에다 따로 설정.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id!=null && id.equals(article.id);
        // 영속화를 하지 않았을 때(아직 db에 데이터를 insert 하지 않았을 때) entity 는 id를 부여 밭지 않았다.
        // 따라서 id 가 null 이다. id!=null 를 추가해서 null 체크를 해야한다.
        // 양속화 되지 않은 엔티티는 모두 동등성 검사를 탈락한다.
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
