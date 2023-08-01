package jdev.mentoria.lojavirtual.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.model.Usuario;
import jdev.mentoria.lojavirtual.model.dto.CepDTO;
import jdev.mentoria.lojavirtual.repository.PessoaFisicaRepository;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import jdev.mentoria.lojavirtual.repository.UsuarioRepository;

@Service
public class PessoaUserService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	private JdbcTemplate jdbcTemplate;
	
	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica Juridica) {
		
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
			
			usuarioRepository.insereAcessoUserPj(usuarioPJ.getId(), "ROLE_USER");
			usuarioRepository.insereAcessoUserPj(usuarioPJ.getId(), "ROLE_ADMIN"); // Passando parametro de forma dinamica
			
			
			StringBuilder menssagemHtml = new StringBuilder();
			menssagemHtml.append("Segue abaixo seus dados de acesso <br/> para a loja virtual<br/>");
			menssagemHtml.append("Login: "+Juridica.getEmail()+"<br/>");
			menssagemHtml.append("Senha: ").append(senha).append("<br/>");
			menssagemHtml.append("Obrigado!");
			try {
			  serviceSendEmail.enviarEmailHtml("Acesso Gerado para Loja Virtual", menssagemHtml.toString(),Juridica.getEmail());
			}catch (Exception e) {
				e.printStackTrace();
			}
						
		}
		
		return Juridica;
		
		
	}
	
	
	public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {
		
		for (int i=0;  i < pessoaFisica.getEnderecos().size(); i++){
			pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);
			//pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);
		}
		
		pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);
		
		Usuario usuarioPJ = usuarioRepository.findUserByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());
		
		if(usuarioPJ == null) {
		
			String constraint = usuarioRepository.consultaConstraintAcesso();
			
			if(constraint != null) { /*Acessa direto o banco para apagar uma constraint gerada indevidamente*//*jdbcTemplate Utilizado para monstra sql nativo*/
				jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint +"; commit;");
			}
			
			usuarioPJ = new Usuario();
			usuarioPJ.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPJ.setEmpresa(pessoaFisica.getEmpresa());
			usuarioPJ.setPessoa(pessoaFisica);
			usuarioPJ.setLogin(pessoaFisica.getEmail());
			
			String senha = ""+ Calendar.getInstance().getTimeInMillis();
			String senhaCript = new BCryptPasswordEncoder().encode(senha);
			
			usuarioPJ.setSenha(senhaCript);
			usuarioPJ = usuarioRepository.save(usuarioPJ);
			
			usuarioRepository.insereAcessoUser(usuarioPJ.getId());// Passando parametro de forma dinamica
			
			
			StringBuilder menssagemHtml = new StringBuilder();
			menssagemHtml.append("Segue abaixo seus dados de acesso <br/> para a loja virtual<br/>");
			menssagemHtml.append("Login: "+pessoaFisica.getEmail()+"<br/>");
			menssagemHtml.append("Senha: ").append(senha).append("<br/>");
			menssagemHtml.append("Obrigado!");
			try {
			  serviceSendEmail.enviarEmailHtml("Acesso Gerado para Loja Virtual", menssagemHtml.toString(),pessoaFisica.getEmail());
			}catch (Exception e) {
				e.printStackTrace();
			}
						
		}
		
		return pessoaFisica;
				
	}
	
    /*Acessa um API externa para retornar o CEP*/
	public CepDTO consultaCep(String cep) {
		return new RestTemplate().getForEntity("https://viacep.com.br/ws/"+cep+"/json", CepDTO.class).getBody();
	}
	

}
