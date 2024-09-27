package com.kucw.security.dao.impl;

import com.kucw.security.dao.MemberDao;
import com.kucw.security.dto.MemberRegisterRequest;
import com.kucw.security.model.member.Member;
import com.kucw.security.model.role.Role;
import com.kucw.security.rowmapper.MemberRowMapper;
import com.kucw.security.rowmapper.RoleRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberDaoImpl implements MemberDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private MemberRowMapper memberRowMapper;

    @Autowired
    private RoleRowMapper roleRowMapper;


    @Override
    public Member getMemberByEmail(String email) {
        String sql = """
                SELECT member_id, email, name, age, password, created_date, last_modified_date  
                FROM member
                WHERE email = :email
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<Member> memberList = namedParameterJdbcTemplate.query(sql, map, memberRowMapper);

        if (memberList.size() > 0) {
            return memberList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer createMember(MemberRegisterRequest memberRegisterRequest) {

        // SQL
        String sql = "INSERT INTO member (email, password, age, name, created_date, last_modified_date) VALUES (:email, :password, :age, :name, :createdDate, :lastModifiedDate)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", memberRegisterRequest.getEmail());
        params.addValue("password", memberRegisterRequest.getPassword());
        params.addValue("age", memberRegisterRequest.getAge());
        params.addValue("name", memberRegisterRequest.getName());
        Date now = new Date();
        params.addValue("createdDate", now);
        params.addValue("lastModifiedDate", now);

        // 用来存储生成的主鍵
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, params, keyHolder);
        int memberId = keyHolder.getKey().intValue();

        return memberId;
    }


    @Override
    public Member getMemberById(Integer memberId) {
        String sql = """
                SELECT member_id, email, name, age, password, created_date, last_modified_date  
                FROM member
                WHERE member_id = :memberId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);

        List<Member> memberList = namedParameterJdbcTemplate.query(sql, map, memberRowMapper);

        if (memberList.size() > 0) {
            return memberList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Role> getRolesByMemberId(Integer memberId) {
        String sql = """
                SELECT role.role_id, role.role_name  
                FROM role JOIN member_has_role  ON
                role.role_id = member_has_role.role_id
                WHERE member_has_role.member_id = :memberId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);

        List<Role> roleList = namedParameterJdbcTemplate.query(sql, map, roleRowMapper);

        return roleList;
    }
}
