package jdev.mentoria.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExceptionMentoriaJava;
import jdev.mentoria.lojavirtual.enums.TipoPessoa;
import jdev.mentoria.lojavirtual.model.Endereco;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.model.dto.CepDTO;
import jdev.mentoria.lojavirtual.model.dto.ConsultaCnpjDTO;
import jdev.mentoria.lojavirtual.repository.EnderecoRepository;
import jdev.mentoria.lojavirtual.repository.PessoaFisicaRepository;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import jdev.mentoria.lojavirtual.service.PessoaUserService;
import jdev.mentoria.lojavirtual.service.ServiceContagemAcessoApi;
import jdev.mentoria.lojavirtual.util.ValidaCNPJ;
import jdev.mentoria.lojavirtual.util.ValidaCPF;

@RestController
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaUserService pessoaUserService;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Autowired
	private ServiceContagemAcessoApi serviceContagemAcessoApi;
	
	@ResponseBody
	@GetMapping(value = "**/consultaPfNome/{nome}")
	public ResponseEntity<List<PessoaFisica>> consultaPfNome(@PathVariable("nome") String nome){
		
		List<PessoaFisica> fisica = pessoaFisicaRepository.pesquisaPorNomePF(nome.trim().toUpperCase());
		
		serviceContagemAcessoApi.atualizaAcessoEndPointPF();
		
		return new ResponseEntity<List<PessoaFisica>>(fisica, HttpStatus.OK); 
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaPfCpf/{cpf}")
	public ResponseEntity<List<PessoaFisica>> consultaPfCpf(@PathVariable("cpf") String cpf){
		
		List<PessoaFisica> fisica = pessoaFisicaRepository.pesquisaPorCpfPF(cpf);
		
		return new ResponseEntity<List<PessoaFisica>>(fisica, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaNomePJ/{nome}")
	public ResponseEntity<List<PessoaJuridica>> consultaNomePJ(@PathVariable("nome") String nome){
		
		List<PessoaJuridica> juridica = pessoaRepository.pesquisaPorNome(nome.trim().toUpperCase());
		
		return new ResponseEntity<List<PessoaJuridica>>(juridica, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaCnpjPJ/{cnpj}")
	public ResponseEntity<List<PessoaJuridica>> consultaCnpjPJ(@PathVariable("cnpj") String cnpj){
		
		List<PessoaJuridica> juridica = pessoaRepository.existeCnpjCadastradoList(cnpj.trim().toUpperCase());
		
		return new ResponseEntity<List<PessoaJuridica>>(juridica, HttpStatus.OK);
		
	}
	
	
	@ResponseBody
	@GetMapping(value = "**/consultaCep/{cep}")
	public ResponseEntity<CepDTO> consultaCep(@PathVariable("cep") String cep){
		
		CepDTO cepDTO = pessoaUserService.consultaCep(cep);
		
		return new ResponseEntity<CepDTO>(cepDTO, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaCnpjReceitaWs/{cnpj}")
	public ResponseEntity<ConsultaCnpjDTO> consultaCnpjDTO(@PathVariable("cnpj") String cnpj){
		
		/*Esta API é publica e por isso so permite 3 consultas por minuto. Se passar dará erro*/
		
		ConsultaCnpjDTO consultaCnpjDTO = pessoaUserService.consultaCnpjReceitaWS(cnpj);
		
		return new ResponseEntity<ConsultaCnpjDTO>(consultaCnpjDTO, HttpStatus.OK);
		
	}	
	
	
	@ResponseBody
	@PostMapping(value = "**/salvarPJ") /*Depois de criado o metodo posso ir ate TestePessoUsuario para testar o metodo*/
	public ResponseEntity<PessoaJuridica> salvarPJ(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionMentoriaJava{
		
		if (pessoaJuridica == null) {
		  throw new ExceptionMentoriaJava("Pessoa juridica nao pode ser NULL");
		}
		
	    if (pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
	      throw new ExceptionMentoriaJava("Já existe um cnpj cadastrado com o número: "+pessoaJuridica.getCnpj());	
	    } 
	    
	    if (pessoaJuridica.getTipoPessoa() == null) {
	      throw new ExceptionMentoriaJava("Informe o tipo de Juridico ou Fornecedor Juridico da loja");
	    }
		
	    if (pessoaJuridica.getId() == null && pessoaRepository.existeInsEstadualCadastrado(pessoaJuridica.getInscEstadual()) != null) {
		  throw new ExceptionMentoriaJava("Já existe uma pessoa cadastrada com o número: "+pessoaJuridica.getInscEstadual());	
		}
	    
	    if (!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
	      throw new ExceptionMentoriaJava("Cnpj: "+ pessoaJuridica.getCnpj() +" está inválido");	
	    	
	    }
	    
	    if (pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0) {
	      
	    	for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {/*Realizo a pesquisa através do for pq tem 2 endereços. Entrega e Comercial*/
	    		CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
	    		
	    		pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
	    		pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
	    		pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
	    		pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDTO.getLogradouro());
	    		pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());	    		
	    	}
	    	
		}else {
			
			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
				
				Endereco enderecoTemp =  enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();
				    //Se não for igual ao cep salvo no banco eu atualizo o endereco
				if (!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())) {
					
					CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
					
					pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
					pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
					pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
					pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDTO.getLogradouro());
					pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
				}
			}
		}

	    pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica); //Cria primeiramene o service
		 
	    return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	
	}
	
	@ResponseBody
	@PostMapping(value = "**/salvarPF") /*Depois de criado o metodo posso ir ate TestePessoUsuario para testar o metodo*/
	public ResponseEntity<PessoaFisica> salvarPF(@RequestBody @Valid PessoaFisica pessoaFisica) throws ExceptionMentoriaJava{
		/*comentado para utilizar o @validato @NonBlank @NotNull*/
	    /*if (pessoaFisica.getNome() == null || pessoaFisica.getNome().isEmpty()) {
	      throw new ExceptionMentoriaJava("Informe o campo nome");	
	    }*/
	    
	    if (pessoaFisica == null) {
	      throw new ExceptionMentoriaJava("Pessoa fisica não pode ser null");	    	
	    }
	    
	    
	    if (pessoaFisica.getId() == null && pessoaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
	      throw new ExceptionMentoriaJava("Já existe um cnpj cadastrado com o número: "+pessoaFisica.getCpf());	
	    } 
		    
	    if (!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
	      throw new ExceptionMentoriaJava("Cnpj: "+ pessoaFisica.getCpf() +" está inválido");	
	    	
	    }
	    
        if (pessoaFisica.getTipoPessoa() == null) {
        	
        	pessoaFisica.setTipoPessoa(TipoPessoa.FISICA.name()); //Pega o valor do enum
        }	    

	    pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica); //Cria primeiramene o service
		 
	    return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	
	}
	
	
}
