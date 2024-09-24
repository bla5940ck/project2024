package com.kucw.security.security;

import com.kucw.security.dao.MemberDao;
import com.kucw.security.model.Member;
import com.kucw.security.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberDao memberDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 從資料庫查詢Member資料
        Member member = memberDao.getMemberByEmail(username);

        if (member != null) {
            String email = member.getEmail();
            String password = member.getPassword();

            // 權限取得
            List<Role> roleList = memberDao.getRolesByMemberId(member.getMemberId());

            List<GrantedAuthority> authorities = convertToAuthorities(roleList);

            // 回傳spring security User格式
            return new User(email, password, authorities);
        }
        else {
            throw new UsernameNotFoundException("member not found for : " + username);
        }
    }

    private List<GrantedAuthority> convertToAuthorities(List<Role> roleList) {
        // 權限設定
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roleList) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }

        return authorities;
    }
}
