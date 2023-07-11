package jdev.mentoria.lojavirtual;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jdev.mentoria.lojavirtual.controller.PessoaController;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import junit.framework.TestCase;

//@Profile("dev") Utilizado para apontar para qual arquivo .properties irá apontar neste caso é teste 
@SpringBootTest(classes = LojaVirtualMentoriaApplication.class) //Nome do projeto 
public class TestePessoaUsuario extends TestCase {
	
	@Autowired
	private PessoaController pessoaController;
	
	@Test
	public void testCadPessoa() throws ExceptionMentoriaJava {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setCnpj(""+ Calendar.getInstance().getTimeInMillis());//Calendar apenas para não ficar mudando o numero por ja existir
		pessoaJuridica.setNome("Edmilson Carvalho");
		pessoaJuridica.setEmail("edmilson.@gmail.com");
		pessoaJuridica.setTelefone("8787548787");
		pessoaJuridica.setInscEstadual("87454852210001");
		pessoaJuridica.setInscMunicipal("907855214001");
		pessoaJuridica.setNomeFantasia("Nome Fantasia");
		pessoaJuridica.setRazaoSocial("Razão social");
		
		pessoaController.salvarPJ(pessoaJuridica);
		
	
		
	}

}
