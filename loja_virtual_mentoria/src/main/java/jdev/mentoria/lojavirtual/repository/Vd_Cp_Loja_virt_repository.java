package jdev.mentoria.lojavirtual.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.VendaCompraLojaVirtual;

@Repository
@Transactional
public interface Vd_Cp_Loja_virt_repository extends JpaRepository<VendaCompraLojaVirtual, Long> {
	
}


