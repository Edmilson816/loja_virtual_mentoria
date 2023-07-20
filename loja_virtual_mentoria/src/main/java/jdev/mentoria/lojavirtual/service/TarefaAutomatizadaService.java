package jdev.mentoria.lojavirtual.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jdev.mentoria.lojavirtual.model.Usuario;
import jdev.mentoria.lojavirtual.repository.UsuarioRepository;

@Component // Esta anotação serve para o spring identificar o servico em segundo plano sem que precise ter um metodo para chamar
@Service // Esta anotação serve para o spring identificar o servico em segundo plano sem que precise ter um metodo para chamar 
public class TarefaAutomatizadaService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail; 

	
	/*
	 * A informacao dentro do cron significa 
	 * cron = "segundo, minuto, hora, dia do mes, Mes, dia da semana"
	 * */
	//@Scheduled(initialDelay = 2000, fixedDelay = 86400000) Teste  
	@Scheduled(cron = "0 0 11 * * *", zone = "America/Sao_Paulo") //Vai rodar todos os dias ás 11hrs da manhã no horario de sao paulo
	public void notificarUserTrocaSenha() throws UnsupportedEncodingException, MessagingException, InterruptedException {
	
		List<Usuario> usuarios = usuarioRepository.usuarioSenhaVencida();
		
		for (Usuario usuario : usuarios) {
			StringBuilder msg = new StringBuilder();
			msg.append("Olá, ").append(usuario.getPessoa().getNome()).append("<br/>");
			msg.append("Está na hora de trocar a sua senha pois ja passou 15 dias de validade").append("<br/>");
			msg.append("Troque a senha da loja virtual Edmilson");
			
			serviceSendEmail.enviarEmailHtml("Troca de Senha", msg.toString(), usuario.getLogin());
		    
			Thread.sleep(3000); // Da um tempo de 3 segundos após enviar o email para evitar acumular muito email e estourar a memoria
			
		} 
		
	}
	
}
