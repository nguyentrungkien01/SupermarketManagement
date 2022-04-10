package com.ou.services;

import com.ou.pojos.Member;
import com.ou.repositories.MemberRepository;

import java.sql.SQLException;

public class MemberService {
    private final static MemberRepository MEMBER_REPOSITORY;
    static {
        MEMBER_REPOSITORY = new MemberRepository();
    }
    //Lấy thông tin của người
    public Member getMemberById(Integer memId) throws SQLException {
        if(memId==null)
            return null;
        return MEMBER_REPOSITORY.getMemberById(memId);
    }
}
