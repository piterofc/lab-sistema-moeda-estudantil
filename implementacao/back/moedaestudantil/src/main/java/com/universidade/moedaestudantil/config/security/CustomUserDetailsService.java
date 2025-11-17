package com.universidade.moedaestudantil.config.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.universidade.moedaestudantil.model.Usuario;
import com.universidade.moedaestudantil.repository.UsuarioRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = this.repository.findByEmail(username)
          .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
       
          return new org.springframework.security.core.userdetails.User(
              user.getEmail(),
              user.getPassword(),
              Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()))
          );
    }
}