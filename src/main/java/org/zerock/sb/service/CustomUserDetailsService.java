package org.zerock.sb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.sb.dto.MemberDTO;
import org.zerock.sb.entity.Member;
import org.zerock.sb.repository.MemberRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //로그인버튼 눌렀을 때 동작하는지 확인 먼저
        log.info("--------------------------------------------------------");
        log.info("loadUserByUsername: " + username);
        log.info("--------------------------------------------------------");

        Optional<Member> optionalMember = memberRepository.getMemberEager(username);

        //사용자의 계정이 존재하지 않을 경우 예외 던지도록 설정
        Member member = optionalMember.orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));

        log.info("Member: " + member);
        log.info("--------------------------------------------------------");

        MemberDTO memberDTO = MemberDTO.builder()
                .mid(member.getMid())
                .mpw(member.getMpw())
                .mname(member.getMname())
                .roles(member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_"+memberRole.name())).collect(Collectors.toSet()))
                .build();

        log.info(memberDTO);
        log.info("--------------------------------------------------------");

        return memberDTO;
    }

}
