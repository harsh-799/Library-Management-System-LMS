package com.harsh.lms.repository;

import com.harsh.lms.model.MemberCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCredentialsRepository extends JpaRepository<MemberCredentials, String>{

    void deleteByMember_MemberId(int memberId);

    boolean existsByMember_MemberId(int memberId);
}
