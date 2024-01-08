package jdev.mentoria.lojavirtual.model.dto;

import jdev.mentoria.lojavirtual.model.Produto;

public class ItemVendaDTO {
	
	private double quantidade;
	
	private Produto produto;

	public double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	

}
