package jdev.mentoria.lojavirtual.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jdev.mentoria.lojavirtual.model.Endereco;
import jdev.mentoria.lojavirtual.model.Pessoa;

public class VendaCompraLojaVirtualDTO {

    private Long Id;
	
	private BigDecimal valorTotal;
	
	private Pessoa pessoa;
	
	private Endereco cobranca;
	
	private Endereco entrega;
	
	private BigDecimal  valorDesc;
	
	private BigDecimal  valorFrete;
	
	private List<ItemVendaDTO> itemVendaLoja = new ArrayList<ItemVendaDTO>();
	
		
	public List<ItemVendaDTO> getItemVendaLoja() {
		return itemVendaLoja;
	}

	public void setItemVendaLoja(List<ItemVendaDTO> itemVendaLoja) {
		this.itemVendaLoja = itemVendaLoja;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public void setValorDesc(BigDecimal valorDesc) {
		this.valorDesc = valorDesc;
	}
	
	public BigDecimal getValorDesc() {
		return valorDesc;
	}
	
	public void setCobranca(Endereco cobranca) {
		this.cobranca = cobranca;
	}
	
	public void setEntrega(Endereco entrega) {
		this.entrega = entrega;
	}
	
	public Endereco getCobranca() {
		return cobranca;
	}
	
	public Endereco getEntrega() {
		return entrega;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	
	public BigDecimal getValorTotal() {
		return valorTotal;
	}	
	
}
