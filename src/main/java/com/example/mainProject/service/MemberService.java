package com.example.mainProject.service;

import com.example.mainProject.domain.Board;
import com.example.mainProject.domain.Role;
import com.example.mainProject.domain.entity.MemberEntity;
import com.example.mainProject.domain.repository.MemberRepository;
//import com.example.mainProject.dto.MemberDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class MemberService implements UserDetailsService {
    private MemberRepository memberRepository;

//    @Transactional
//    public Long joinUser(MemberDto memberDto) {
//        // 비밀번호 암호화
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
//
//        return memberRepository.save(memberDto.toEntity()).getId();
//    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<MemberEntity> userEntityWrapper = memberRepository.findByEmail(userEmail);
        MemberEntity userEntity = userEntityWrapper.get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (("admin@example.com").equals(userEmail)) {
            authorities.add(new SimpleGrantedAuthority(Role.ROLE_ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.ROLE_MEMBER.getValue()));
        }

        return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
    }

    public MemberEntity memberView(MemberEntity member) {
        return memberRepository.findById(member.getId()).get();
    }

    public void updateMember(MemberEntity member) {
        memberRepository.save(member);
    }
}
