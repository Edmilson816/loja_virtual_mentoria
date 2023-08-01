package jdev.mentoria.lojavirtual.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.Endereco;

@Transactional
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

}
