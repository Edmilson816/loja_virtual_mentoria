package jdev.mentoria.lojavirtual;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jdev.mentoria.lojavirtual.controller.PessoaController;
import jdev.mentoria.lojavirtual.enums.TipoEndereco;
import jdev.mentoria.lojavirtual.model.Endereco;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.repository.PessoaRepository;
import junit.framework.TestCase;

//@Profile("dev") Utilizado para apontar para qual arquivo .properties irá apontar neste caso é teste 
@SpringBootTest(classes = LojaVirtualMentoriaApplication.class) //Nome do projeto 
public class TestePessoaUsuario extends TestCase {
	
	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Test
	public void testCadPessoaJuridica() throws ExceptionMentoriaJava {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setCnpj(""+ Calendar.getInstance().getTimeInMillis());//Calendar apenas para não ficar mudando o numero por ja existir
		pessoaJuridica.setNome("Edmilson Carvalho");
		pessoaJuridica.setEmail("edmilson.tectec.@gmail.com");
		pessoaJuridica.setTelefone("8787548787");
		pessoaJuridica.setInscEstadual("87454852210001");
		pessoaJuridica.setInscMunicipal("907855214001");
		pessoaJuridica.setNomeFantasia("Nome Fantasia");
		pessoaJuridica.setRazaoSocial("Razão social");
		
		Endereco endereco1 = new Endereco();
		endereco1.setBairro("Parque Julio cezar");
		endereco1.setCep("41340100");
		endereco1.setComplemento("Proximo a beneda");
		endereco1.setEmpresa(pessoaJuridica);
		endereco1.setNumero("10");
		endereco1.setPessoa(pessoaJuridica);
		endereco1.setRuaLogra("Alameida carrara");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("BA");
		endereco1.setCidade("Salvador");
		
		Endereco endereco2 = new Endereco();
		endereco2.setBairro("Parque Julio cesar");
		endereco2.setCep("41340100");
		endereco2.setComplemento("Proximo ao bar do andrey");
		endereco2.setEmpresa(pessoaJuridica);
		endereco2.setNumero("10");
		endereco2.setPessoa(pessoaJuridica);
		endereco2.setRuaLogra("Alameida");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("BA");
		endereco2.setCidade("Salvador");
		
		pessoaJuridica.getEnderecos().add(endereco1);
		pessoaJuridica.getEnderecos().add(endereco2);
		
		pessoaJuridica = pessoaController.salvarPJ(pessoaJuridica).getBody();
		
		assertEquals(true, pessoaJuridica.getId() > 0);
		
		/*Valido se foi gerado id de endereco*/
		for(Endereco endereco : pessoaJuridica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);			
		}
		
		/*Verifica o tamanho da lista*/
		assertEquals(2, pessoaJuridica.getEnderecos().size());
			
		
	}
	
	@Test
	public void testCadPessoaFisica() throws ExceptionMentoriaJava {

		PessoaJuridica pessoaJuridica = pessoaRepository.existeCnpjCadastrado("1687990321645");
		
		PessoaFisica pessoaFisica = new PessoaFisica();
		pessoaFisica.setCpf("634.803.580-80");//Calendar apenas para não ficar mudando o numero por ja existir
		pessoaFisica.setNome("Edmilson Carvalho");
		pessoaFisica.setEmail("edmilghhgson.teRTOctec.@gmail.com");
		pessoaFisica.setTelefone("8787548787");
		pessoaFisica.setEmpresa(pessoaJuridica);		
		
		Endereco endereco1 = new Endereco();
		endereco1.setBairro("Parque Julio cezar");
		endereco1.setCep("41340100");
		endereco1.setComplemento("Proximo a beneda");
		endereco1.setEmpresa(pessoaFisica);
		endereco1.setNumero("10");
		endereco1.setPessoa(pessoaFisica);
		endereco1.setRuaLogra("Alameida carrara");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("BA");
		endereco1.setCidade("Salvador");
		endereco1.setEmpresa(pessoaJuridica);
		
		Endereco endereco2 = new Endereco();
		endereco2.setBairro("Parque Julio cesar");
		endereco2.setCep("41340100");
		endereco2.setComplemento("Proximo ao bar do andrey");
		endereco2.setEmpresa(pessoaFisica);
		endereco2.setNumero("10");
		endereco2.setPessoa(pessoaFisica);
		endereco2.setRuaLogra("Alameida");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("BA");
		endereco2.setCidade("Salvador");
		endereco2.setEmpresa(pessoaJuridica);
		
		pessoaFisica.getEnderecos().add(endereco1);
		pessoaFisica.getEnderecos().add(endereco2);
		
		pessoaFisica = pessoaController.salvarPF(pessoaFisica).getBody();
		
		assertEquals(true, pessoaFisica.getId() > 0);
		
		/*Valido se foi gerado id de endereco*/
		for(Endereco endereco : pessoaFisica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);			
		}
		
		/*Verifica o tamanho da lista*/
		assertEquals(2, pessoaFisica.getEnderecos().size());
			
		
	}
	

}
