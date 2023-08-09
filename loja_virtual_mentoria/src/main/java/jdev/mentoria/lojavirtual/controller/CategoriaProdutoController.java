package jdev.mentoria.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.status.Status;
import jdev.mentoria.lojavirtual.ExceptionMentoriaJava;
import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.model.CategoriaProduto;
import jdev.mentoria.lojavirtual.model.dto.CategoriaProdutoDTO;
import jdev.mentoria.lojavirtual.repository.CategoriaProdutoRepository;

@RestController
public class CategoriaProdutoController {
	
	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository;
	
	@ResponseBody
	@PostMapping(value = "**/deleteCategoria")
	public ResponseEntity<?> deleteCategoria(@RequestBody CategoriaProduto categoriaProduto){
	
		if (categoriaProdutoRepository.findById(categoriaProduto.getId()).isPresent() == false) {
			return new ResponseEntity("Categoria já foi Removida", HttpStatus.OK);	
		}
		
		categoriaProdutoRepository.deleteById(categoriaProduto.getId());
		
		return new ResponseEntity("Categoria Removida", HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarPorDescCategoria/{desc}")
	public ResponseEntity<List<CategoriaProduto>> buscarPorDescCategoria(@PathVariable("desc") String desc){
		
		List<CategoriaProduto> categoriaProduto = categoriaProdutoRepository.buscarCategoriaDesc(desc.toUpperCase());
		
		return new ResponseEntity<List<CategoriaProduto>>(categoriaProduto, HttpStatus.OK);
		
	}
	
	
	@ResponseBody
	@PostMapping(value = "**/salvarCategoria")
	public ResponseEntity<CategoriaProdutoDTO> salvarCategoria(@RequestBody CategoriaProduto categoriaProduto) throws ExceptionMentoriaJava{
		
	    if (categoriaProduto.getEmpresa().getId() == null || categoriaProduto.getEmpresa().getId() <= 0) {
	    	throw new ExceptionMentoriaJava("A empresa deve ser informada!");
	    }
	    
	    if (categoriaProduto.getId() == null && categoriaProdutoRepository.existeCategoria(categoriaProduto.getNomeDesc().toUpperCase())) {
	    	throw new ExceptionMentoriaJava("Não pode cadastrar categoria com mesmo nome!");
	    }

		CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoriaProduto); 
	    
	    /*Foi adicionado o DTO pois estava dando erro ao salvar com o id da empresa. Porém o erro foi resolvido 
	     * direto no atributo do Model. De que forma ? Instanciando a pessoa direto pelo atributo*/
	    
	    CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();
	    categoriaProdutoDTO.setId(categoriaSalva.getId());
	    categoriaProdutoDTO.setNomeDesc(categoriaSalva.getNomeDesc());
	    categoriaProdutoDTO.setEmpresa(categoriaSalva.getEmpresa().getId().toString());	    
	    
	    return new ResponseEntity<CategoriaProdutoDTO>(categoriaProdutoDTO, HttpStatus.OK);
	}

}
