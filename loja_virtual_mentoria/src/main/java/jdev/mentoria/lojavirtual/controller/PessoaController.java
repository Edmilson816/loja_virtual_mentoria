package jdev.mentoria.lojavirtual.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExceptionMentoriaJava;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import jdev.mentoria.lojavirtual.service.PessoaUserService;
import jdev.mentoria.lojavirtual.util.ValidaCNPJ;
import jdev.mentoria.lojavirtual.util.ValidaCPF;

@RestController
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaUserService pessoaUserService; 
	
	
	@ResponseBody
	@PostMapping(value = "**/salvarPJ") /*Depois de criado o metodo posso ir ate TestePessoUsuario para testar o metodo*/
	public ResponseEntity<PessoaJuridica> salvarPJ(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionMentoriaJava{
		
	    if (pessoaJuridica == null) {
	      throw new ExceptionMentoriaJava("Pessoa juridica não pode ser null");	    	
	    }
	    
	    if (pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
	      throw new ExceptionMentoriaJava("Já existe um cnpj cadastrado com o número: "+pessoaJuridica.getCnpj());	
	    } 
		
	    if (pessoaJuridica.getId() == null && pessoaRepository.existeInsEstadualCadastrado(pessoaJuridica.getInscEstadual()) != null) {
		  throw new ExceptionMentoriaJava("Já existe uma pessoa cadastrada com o número: "+pessoaJuridica.getInscEstadual());	
		}
	    
	    if (!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
	      throw new ExceptionMentoriaJava("Cnpj: "+ pessoaJuridica.getCnpj() +" está inválido");	
	    	
	    }

	    pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica); //Cria primeiramene o service
		 
	    return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	
	}
	
	@ResponseBody
	@PostMapping(value = "**/salvarPF") /*Depois de criado o metodo posso ir ate TestePessoUsuario para testar o metodo*/
	public ResponseEntity<PessoaFisica> salvarPF(@RequestBody @Valid PessoaFisica pessoaFisica) throws ExceptionMentoriaJava{
		/*comentado para utilizar o @validato @NonBlank @NotNull*/
	    /*if (pessoaFisica == null) {
	      throw new ExceptionMentoriaJava("Pessoa fisica não pode ser null");	    	
	    }*/
	    
	    if (pessoaFisica.getNome() == null || pessoaFisica.getNome().isEmpty()) {
	      throw new ExceptionMentoriaJava("Informe o campo nome");	
	    }
	    
	    if (pessoaFisica.getId() == null && pessoaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
	      throw new ExceptionMentoriaJava("Já existe um cnpj cadastrado com o número: "+pessoaFisica.getCpf());	
	    } 
		    
	    if (!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
	      throw new ExceptionMentoriaJava("Cnpj: "+ pessoaFisica.getCpf() +" está inválido");	
	    	
	    }

	    pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica); //Cria primeiramene o service
		 
	    return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	
	}
	
	
}
