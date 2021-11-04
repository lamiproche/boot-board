package org.zerock.sb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.sb.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    //기본을 LAZY로 두고 필요할 때마다 설정
    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid = :mid")
    Optional<Member> getMemberEager(String mid);

}