package com.ou.services;

import com.ou.pojos.Member;
import com.ou.repositories.MemberRepositoryForTest;

import java.sql.SQLException;

public class MemberServiceForTest {
    private final static MemberRepositoryForTest MEMBER_REPOSITORY_FOR_TEST;
    static {
        MEMBER_REPOSITORY_FOR_TEST = new MemberRepositoryForTest();
    }
    // Lấy thông tin thành viên dựa vào id
    public Member getMemberById(int memId) throws SQLException {
        if(memId <1 )
            return null;
        return MEMBER_REPOSITORY_FOR_TEST.getMemberById(memId);
    }
}
