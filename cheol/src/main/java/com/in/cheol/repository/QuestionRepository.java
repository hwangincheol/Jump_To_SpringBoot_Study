package com.in.cheol.repository;

import com.in.cheol.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Question findBySubject(String subject);

    Question findBySubjectAndContent(String subject, String content);

    List<Question> findBySubjectLike(String subject);

//    @Query("select o from Question o order by o.id desc")
//    Page<Question> findAll(Pageable pageable);

//    // Specification 사용해서 검색조건 처리
//    Page<Question> findAll(Specification<Question> spec, Pageable pageable);

    // JPQL 사용해서 검색조건 처리
    // (복잡한 @Query에서는 스프링이 자동으로 count 쿼리를 잘 못 만듦)
    // (특히 join, distinct가 있으면 오답된 개수가 나올 수 있어서 직접 countQuery를 작성해야 함)
    // (@Query 없이 findAll(Pageable pageable)처럼 간단하게 Pageable을 사용하면, 스프링 데이터 JPA가 자동으로 countQuery를 만들어 줌)
    @Query(
            value = "select distinct q " +
                    "from Question q " +
                    "left join q.author u1 " +
                    "left join q.answerList a " +
                    "left join a.author u2 " +
                    "where " +
                    "   q.subject like %:kw% " +
                    "   or q.content like %:kw% " +
                    "   or u1.username like %:kw% " +
                    "   or a.content like %:kw% " +
                    "   or u2.username like %:kw%",
            countQuery = "select count(distinct q) from Question q " +
                    "left join q.author u1 " +
                    "left join q.answerList a " +
                    "left join a.author u2 " +
                    "where " +
                    "   q.subject like %:kw% " +
                    "   or q.content like %:kw% " +
                    "   or u1.username like %:kw% " +
                    "   or a.content like %:kw% " +
                    "   or u2.username like %:kw%"
    )
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

}
