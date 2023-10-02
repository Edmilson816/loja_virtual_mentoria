package jdev.mentoria.lojavirtual.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="produto")
@SequenceGenerator(name = "seq_produto", sequenceName = "seq_produto", allocationSize = 1, initialValue = 1)
public class Produto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "seq_produto", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@NotNull(message = "O Tipo da Unidade deve ser informado!")
	@Column(nullable = false)
	private String tipoUnidade;
	
	@NotNull(message = "Produto deve ser informado!")
	@Size(min = 2, message = "Nome do Produto deve conter mais de 2 letras!")
	@Column(nullable = false)
	private String nome;
	
	@NotNull(message = "A Descrição do produto deve ser informado!")
	@Column(columnDefinition = "text", length = 2000, nullable = false)
	private String descricao;
	
	@Column(nullable = false)
	private Boolean ativo = Boolean.TRUE;
	
	@NotNull(message = "O Peso do produto deve ser informado!")
	private Double peso;
	
	@NotNull(message = "A Largura do produto deve ser informado!")	
	private Double largura;
	
	@NotNull(message = "A altura do produto deve ser informado!")
	private Double altura;
	
	private Double profundidade;
	
	@NotNull(message = "O Valor do produto deve ser informado!")
	@Column(nullable = false)
	private BigDecimal valorVenda = BigDecimal.ZERO;
	
	@Column(nullable = false)
	private Integer QtdEstoque = 0;
	
	private Integer QtdeAlertaEstoque = 0;
	
	private String linkYoutube;
	
	private Boolean alertaQtdeEstoque = Boolean.FALSE;
	
	private Integer qtdeClique = 0;
	
	@NotNull(message = "A empresa deve ser informada!")
	@ManyToOne(targetEntity = Pessoa.class)
	@JoinColumn(name = "empresa_id", nullable = false,
	foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
	private PessoaJuridica empresa;

	@NotNull(message = "A Categoria do produto deve ser informada!")
	@ManyToOne(targetEntity = CategoriaProduto.class)
	@JoinColumn(name = "categoria_produto_id", nullable = false,
	foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "categoria_produto_id_fk"))
	private CategoriaProduto categoriaProduto = new CategoriaProduto();
	
	@NotNull(message = "A Marca do produto deve ser informada!")
	@ManyToOne(targetEntity = MarcaProduto.class)
	@JoinColumn(name = "marca_produto_id", nullable = false,
	foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "marca_produto_id_fk"))
	private MarcaProduto marcaProduto = new MarcaProduto();
	
	@OneToMany(mappedBy = "produto", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ImagemProduto> imagens = new ArrayList<ImagemProduto>();	
	
	public PessoaJuridica getEmpresa() {
		return empresa;
	}

	public void setEmpresa(PessoaJuridica empresa) {
		this.empresa = empresa;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoUnidade() {
		return tipoUnidade;
	}

	public void setTipoUnidade(String tipoUnidade) {
		this.tipoUnidade = tipoUnidade;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public Double getLargura() {
		return largura;
	}

	public void setLargura(Double largura) {
		this.largura = largura;
	}

	public Double getAltura() {
		return altura;
	}

	public void setAltura(Double altura) {
		this.altura = altura;
	}

	public Double getProfundidade() {
		return profundidade;
	}

	public void setProfundidade(Double profundidade) {
		this.profundidade = profundidade;
	}

	public BigDecimal getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
	}

	public Integer getQtdEstoque() {
		return QtdEstoque;
	}

	public void setQtdEstoque(Integer qtdEstoque) {
		QtdEstoque = qtdEstoque;
	}

	public Integer getQtdeAlertaEstoque() {
		return QtdeAlertaEstoque;
	}

	public void setQtdeAlertaEstoque(Integer qtdeAlertaEstoque) {
		QtdeAlertaEstoque = qtdeAlertaEstoque;
	}

	public String getLinkYoutube() {
		return linkYoutube;
	}

	public void setLinkYoutube(String linkYoutube) {
		this.linkYoutube = linkYoutube;
	}

	public Boolean getAlertaQtdeEstoque() {
		return alertaQtdeEstoque;
	}

	public void setAlertaQtdeEstoque(Boolean alertaQtdeEstoque) {
		this.alertaQtdeEstoque = alertaQtdeEstoque;
	}

	public Integer getQtdeClique() {
		return qtdeClique;
	}

	public void setQtdeClique(Integer qtdeClique) {
		this.qtdeClique = qtdeClique;
	}	
	
	public CategoriaProduto getCategoriaProduto() {
		return categoriaProduto;
	}

	public void setCategoriaProduto(CategoriaProduto categoriaProduto) {
		this.categoriaProduto = categoriaProduto;
	}

	public MarcaProduto getMarcaProduto() {
		return marcaProduto;
	}

	public void setMarcaProduto(MarcaProduto marcaProduto) {
		this.marcaProduto = marcaProduto;
	}
	
	public List<ImagemProduto> getImagens() {
		return imagens;
	}
	
	public void setImagens(List<ImagemProduto> imagens) {
		this.imagens = imagens;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
	

}
