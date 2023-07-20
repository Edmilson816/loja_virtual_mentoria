package jdev.mentoria.lojavirtual.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jdev.mentoria.lojavirtual.model.PessoaJuridica;

@Repository
@Transactional
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {

	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public PessoaJuridica existeCnpjCadastrado(String cnpj);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.insEstadual = ?1")
	public PessoaJuridica existeInsEstadualCadastrado(String insEstadual);
	
	
}
