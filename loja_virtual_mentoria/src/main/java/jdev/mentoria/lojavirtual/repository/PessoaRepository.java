package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;

@Repository
@Transactional
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {

	
	@Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.nome)) like %?1%")
	public List<PessoaJuridica> pesquisaPorNome(String nome);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public PessoaJuridica existeCnpjCadastrado(String cnpj);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public List<PessoaJuridica> existeCnpjCadastradoList(String cnpj);
	
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
	public PessoaFisica existeCpfCadastrado(String cpf);

	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
	public List<PessoaFisica> existeCpfCadastradoList(String cpf);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.inscEstadual = ?1")
	public PessoaJuridica existeInsEstadualCadastrado(String inscEstadual);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.inscEstadual = ?1")
	public List<PessoaJuridica> existeInsEstadualCadastradoList(String inscEstadual);
	
}
