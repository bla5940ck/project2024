package com.kucw.security.dao.impl;

import com.kucw.security.dao.RoleDao;
import com.kucw.security.model.role.MemberHasRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void createMemberHasRole(List<MemberHasRole> memberHasRoleList) {

        String sql = """
                INSERT INTO member_has_role (member_id, role_id)
                VALUES (:memberId, :roleId) 
                """;

        List<MapSqlParameterSource> parameterSources = new ArrayList<>();
        for (MemberHasRole memberHasRole : memberHasRoleList) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("memberId", memberHasRole.getMemberId());
            params.addValue("roleId", memberHasRole.getRoleId());
            parameterSources.add(params);
        }

        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources.toArray(new MapSqlParameterSource[0]));

    }
}
