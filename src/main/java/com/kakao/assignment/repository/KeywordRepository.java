package com.kakao.assignment.repository;

import com.kakao.assignment.entity.KeywordManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<KeywordManagement, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "javax.persistence.lock.timeout", value = "3000")
    })
    @Query("select k from KeywordManagement k where k.keyword = :keyword")
    Optional<KeywordManagement> findByIdForUpdate(String keyword);
}
