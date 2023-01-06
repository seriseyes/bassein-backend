package com.seris.bassein.service.security.service;

import com.seris.bassein.service.component.user.repository.UserRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j
public class JwtUserDetailsService implements UserDetailsService {
    @Resource
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.seris.bassein.entity.user.User> authUser = userRepository.findFirstByUsername(username);
        if (authUser.isPresent()) {
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add((GrantedAuthority) () -> authUser.get().getRole().name());
            roles.add((GrantedAuthority) () -> "id-" + authUser.get().getUuid());
            return new User(authUser.get().getUsername(), authUser.get().getPassword(), roles);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }


}
