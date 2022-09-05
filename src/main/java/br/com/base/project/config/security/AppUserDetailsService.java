package br.com.base.project.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.com.base.project.repository.UsuarioRepository;

@Repository
public class AppUserDetailsService implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		
		var usuario = usuarioRepository.findByLogin(login);
		
		if (usuario.isPresent()) {
			return usuario.get();
		}else {
			throw new UsernameNotFoundException("Usuario não encontrado!");
		}
	}

}
