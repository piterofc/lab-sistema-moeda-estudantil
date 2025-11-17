package com.universidade.moedaestudantil.config.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.universidade.moedaestudantil.model.Usuario;
import com.universidade.moedaestudantil.model.Aluno;
import com.universidade.moedaestudantil.model.EmpresaParceira;
import com.universidade.moedaestudantil.repository.UsuarioRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Usuario user = this.repository.findByEmail(username)
          .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

      String role = "USER";
      if (user instanceof Aluno) role = "ALUNO";
      else if (user instanceof EmpresaParceira) role = "EMPRESA";

      return new org.springframework.security.core.userdetails.User(
          user.getEmail(),
          user.getSenha(),
          Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
      );
    }
}