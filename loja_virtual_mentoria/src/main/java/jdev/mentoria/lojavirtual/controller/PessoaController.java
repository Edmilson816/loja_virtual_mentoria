package jdev.mentoria.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExceptionMentoriaJava;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import jdev.mentoria.lojavirtual.service.PessoaUserService;

@RestController
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaUserService pessoaUserService; 
	
	
	@ResponseBody
	@PostMapping(value = "**/salvarPJ") /*Depois de criado o metodo posso ir ate TestePessoUsuario para testar o metodo*/
	public ResponseEntity<PessoaJuridica> salvarPJ(@RequestBody PessoaJuridica pessoaJuridica) throws ExceptionMentoriaJava{
		
	    if (pessoaJuridica == null) {
	      throw new ExceptionMentoriaJava("Pessoa juridica não pode ser null");	    	
	    }
	    
	    if (pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
	      throw new ExceptionMentoriaJava("Já existe um cnpj cadastrado com o número: "+pessoaJuridica.getCnpj());	
	    } 
		
		pessoaJuridica = pessoaUserService.salvarPessoJuridica(pessoaJuridica); //Cria primeiramene o service
		 
	    return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	
	}
	
}
