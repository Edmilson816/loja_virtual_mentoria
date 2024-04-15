package jdev.mentoria.lojavirtual.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExceptionMentoriaJava;
import jdev.mentoria.lojavirtual.model.Endereco;
import jdev.mentoria.lojavirtual.model.ItemVendaLoja;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.StatusRastreio;
import jdev.mentoria.lojavirtual.model.VendaCompraLojaVirtual;
import jdev.mentoria.lojavirtual.model.dto.ItemVendaDTO;
import jdev.mentoria.lojavirtual.model.dto.VendaCompraLojaVirtualDTO;
import jdev.mentoria.lojavirtual.repository.EnderecoRepository;
import jdev.mentoria.lojavirtual.repository.NotaFiscalVendaRepository;
import jdev.mentoria.lojavirtual.repository.StatusRastreioRepository;
import jdev.mentoria.lojavirtual.repository.Vd_Cp_Loja_virt_repository;
import jdev.mentoria.lojavirtual.service.VendaService;

@RestController
public class Vd_Cp_loja_Virt_Controller {
	
	@Autowired
	private Vd_Cp_Loja_virt_repository vd_Cp_Loja_virt_repository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private NotaFiscalVendaRepository notaFiscalVendaRepository;
	
	@Autowired
	private StatusRastreioRepository statusRastreioRepository;
	
	@Autowired
	private VendaService vendaService;
	
	@ResponseBody
	@PostMapping(value = "**/salvarVendaLoja")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaLoja(@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionMentoriaJava {
		
		vendaCompraLojaVirtual.getPessoa().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		PessoaFisica pessoaFisica = pessoaController.salvarPF(vendaCompraLojaVirtual.getPessoa()).getBody();
		vendaCompraLojaVirtual.setPessoa(pessoaFisica);
		
		vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
		vendaCompraLojaVirtual.getEnderecoCobranca().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
		vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);
		
		vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
		vendaCompraLojaVirtual.getEnderecoEntrega().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);
		
		vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		
		/*Salvado os itens*/
		for(int i = 0; i < vendaCompraLojaVirtual.getItemVendaLojas().size(); i++) {
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
			
		}
		
		/*Salva primeiro a venda e todo os dados*/
		vendaCompraLojaVirtual = vd_Cp_Loja_virt_repository.saveAndFlush(vendaCompraLojaVirtual);
		
		StatusRastreio statusRastreio = new StatusRastreio();
		statusRastreio.setCentroDistribuicao("Loja Local");
		statusRastreio.setCidade("Local");
		statusRastreio.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		statusRastreio.setEstado("Local");
		statusRastreio.setStatus("Inicio Compra");
		statusRastreio.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		
		statusRastreioRepository.save(statusRastreio);
		
		/*Associa a venda gravada no banco com a nota fiscal*/
		vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		
		/*Persiste novamente as nota fiscal novamente pra ficar amarrada na venda*/
		notaFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());
		
		VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
		compraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		compraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
		compraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
		compraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
		compraLojaVirtualDTO.setValorDesc(vendaCompraLojaVirtual.getValorDesconto());
		compraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
		compraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());		
		
		for (ItemVendaLoja Item : vendaCompraLojaVirtual.getItemVendaLojas()) {
			ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
			itemVendaDTO.setQuantidade(Item.getQuantidade());
			itemVendaDTO.setProduto(Item.getProduto());
			
			compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
		}
		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaVendaId/{id}")
	public ResponseEntity<VendaCompraLojaVirtualDTO>consultaVendaId(@PathVariable("id") Long idVenda){
		
		//Retorno do banco 
		VendaCompraLojaVirtual vendaCompraLojaVirtual = vd_Cp_Loja_virt_repository.findByIdExclusao(idVenda);
		
		if (vendaCompraLojaVirtual == null) { // Se o objeto for null ele carrega um objeto vazio semelhando ao orElse(null)
			vendaCompraLojaVirtual = new VendaCompraLojaVirtual();
			
		}
		
		//Preencho o DTO
		VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
		compraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		compraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
		compraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
		compraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
		compraLojaVirtualDTO.setValorDesc(vendaCompraLojaVirtual.getValorDesconto());
		compraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
		
		for (ItemVendaLoja Item : vendaCompraLojaVirtual.getItemVendaLojas()) {
			ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
			itemVendaDTO.setQuantidade(Item.getQuantidade());
			itemVendaDTO.setProduto(Item.getProduto()); //cRIADO PARA TRAZER TODOS OS DADOS DO PRODUTO
			
			compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
		}		
		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@DeleteMapping("**/deletaTotalBanco/{idVenda}")
	public ResponseEntity<String> deletaTotalBanco(@PathVariable(value = "idVenda") Long idVenda){
		
		vendaService.exclusaoTotalVendaBanco(idVenda); //Exclui direto do banco 
		
		return new ResponseEntity<String>("Venda excluida com sucesso.", HttpStatus.OK);
		
	}
	
	@ResponseBody
	@DeleteMapping("**/deletaTotalBanco2/{idVenda}")
	public ResponseEntity<String> deletaTotalBanco2(@PathVariable(value = "idVenda") Long idVenda){
		
		vendaService.exclusaoTotalVendaBanco2(idVenda); //altera apenas o status para não aparecer ou seja é uma exclusão logica
		return new ResponseEntity<String>("Venda excluida logicamene com sucesso.", HttpStatus.OK);
		
	}
	
	@ResponseBody
	@PutMapping("**/ativaRegistroVendaBanco/{idVenda}")
	public ResponseEntity<String> ativaRegistroVendaBanco(@PathVariable(value = "idVenda") Long idVenda){
		
		vendaService.ativaRegistroVendaBanco(idVenda);
		return new ResponseEntity<String>("Venda ativada com sucesso.", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaVendaPorProdutoId/{id}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>>consultaVendaPorProdutoId(@PathVariable("id") Long idProd){
		
		//Retorno do banco 
		List<VendaCompraLojaVirtual> vendaCompraLojaVirtual = vd_Cp_Loja_virt_repository.vendaPorProduto(idProd);
		
		if (vendaCompraLojaVirtual == null) { // Se o objeto for null ele carrega um objeto vazio semelhando ao orElse(null)
			vendaCompraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
			
		}
		
		List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vcl: vendaCompraLojaVirtual) {
		
		  //Preencho o DTO
		  VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
		  compraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
		  compraLojaVirtualDTO.setPessoa(vcl.getPessoa());
		  compraLojaVirtualDTO.setCobranca(vcl.getEnderecoCobranca());
		  compraLojaVirtualDTO.setEntrega(vcl.getEnderecoEntrega());
		  compraLojaVirtualDTO.setValorDesc(vcl.getValorDesconto());
		  compraLojaVirtualDTO.setValorFrete(vcl.getValorFrete());
		
		  for (ItemVendaLoja Item : vcl.getItemVendaLojas()) {
			   ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
			   itemVendaDTO.setQuantidade(Item.getQuantidade());
			   itemVendaDTO.setProduto(Item.getProduto()); //cRIADO PARA TRAZER TODOS OS DADOS DO PRODUTO
			
			compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
		  }		
		  
		  compraLojaVirtualDTOList.add(compraLojaVirtualDTO);
		  
		}
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);
		
	}
	

}