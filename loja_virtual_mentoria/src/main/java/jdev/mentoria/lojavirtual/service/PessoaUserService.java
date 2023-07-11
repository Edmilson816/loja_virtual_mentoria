package jdev.mentoria.lojavirtual.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.model.Usuario;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import jdev.mentoria.lojavirtual.repository.UsuarioRepository;

@Service
public class PessoaUserService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	private JdbcTemplate jdbcTemplate;
	
	public PessoaJuridica salvarPessoJuridica(PessoaJuridica Juridica) {
		
		for (int i=0;  i < Juridica.getEnderecos().size(); i++){
			Juridica.getEnderecos().get(i).setPessoa(Juridica);
			Juridica.getEnderecos().get(i).setEmpresa(Juridica);
		}
		
		Juridica = pessoaRepository.save(Juridica);
		
		Usuario usuarioPJ = usuarioRepository.findUserByPessoa(Juridica.getId(), Juridica.getEmail());
		
		if(usuarioPJ == null) {
		
			String constraint = usuarioRepository.consultaConstraintAcesso();
			
			if(constraint != null) { /*Acessa direto o banco para apagar uma constraint gerada indevidamente*//*jdbcTemplate Utilizado para monstra sql nativo*/
				jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint +"; commit;");
			}
			
			usuarioPJ = new Usuario();
			usuarioPJ.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPJ.setEmpresa(Juridica);
			usuarioPJ.setPessoa(Juridica);
			usuarioPJ.setLogin(Juridica.getEmail());
			
			String senha = ""+ Calendar.getInstance().getTimeInMillis();
			String senhaCript = new BCryptPasswordEncoder().encode(senha);
			
			usuarioPJ.setSenha(senhaCript);
			usuarioPJ = usuarioRepository.save(usuarioPJ);
			
			usuarioRepository.insereAcessoUserPj(usuarioPJ.getId());
			
			if (constraint != null) {
				jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint +"; commit;");
			}			
						
		}
		
		return Juridica;
		
		
	}

}
