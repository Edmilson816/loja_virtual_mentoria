package jdev.mentoria.lojavirtual;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jdev.mentoria.lojavirtual.controller.AcessoController;
import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.repository.AcessoRepository;
import junit.framework.TestCase;

@SpringBootTest(classes = LojaVirtualMentoriaApplication.class) //Nome do projeto 
class LojaVirtualMentoriaApplicationTests extends TestCase {
	
	@Autowired
	private AcessoController acessoController; 
	
	@Autowired
	private AcessoRepository acessoRepository;	
	
	@Autowired
	private WebApplicationContext wac; /*Para utilização do Mokito*/
	
	@Test
	public void testRestApiCadastroAcesso() throws JsonProcessingException, Exception {
		
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_COMPRADOR");
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoAPI = mockMvc
				.perform(MockMvcRequestBuilders.post("/salvarAcesso") /*Irá realizar o post em nosso mapeamento*/
						.content(objectMapper.writeValueAsString(acesso)) /*Escreve o Conteúdo que será passado*/
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
				); 
		
		System.out.println("Retorno da API: "+ retornoAPI.andReturn().getResponse().getContentAsString());//Retorna o resultado da API
		
		Acesso objetoRetorno = objectMapper.readValue(
				retornoAPI.andReturn().getResponse().getContentAsString(), /*Retorno da API*/
				Acesso.class); /*Tipo da Classe*/
		
		assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao()); /*Comparo o que mandei salvar com o que foi salvo*/
		
		
	}
	
	@Test
	public void testRestApiDeleteAcesso() throws JsonProcessingException, Exception {
		
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_TESTE_DELETE");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoAPI = mockMvc
				.perform(MockMvcRequestBuilders.post("/deleteAcesso") /*Irá realizar o post em nosso mapeamento*/
						.content(objectMapper.writeValueAsString(acesso)) /*Escreve o Conteúdo que será passado*/
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
				); 
		
		System.out.println("Retorno da API: "+ retornoAPI.andReturn().getResponse().getContentAsString());//Retorna o resultado da API
		System.out.println("Status de Retorno: "+ retornoAPI.andReturn().getResponse().getStatus());
		
		assertEquals("Acesso Removido", retornoAPI.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoAPI.andReturn().getResponse().getStatus());
	
		
	}
	
	@Test
	public void testRestApiDeletePorIDAcesso() throws JsonProcessingException, Exception {
		
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_TESTE_DELETE_ID");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoAPI = mockMvc
				.perform(MockMvcRequestBuilders.delete("/deleteAcessoPorId/"+ acesso.getId()) /*Irá realizar o post em nosso mapeamento*/
						.content(objectMapper.writeValueAsString(acesso)) /*Escreve o Conteúdo que será passado*/
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
				); 
		
		System.out.println("Retorno da API: "+ retornoAPI.andReturn().getResponse().getContentAsString());//Retorna o resultado da API
		System.out.println("Status de Retorno: "+ retornoAPI.andReturn().getResponse().getStatus());
		
		assertEquals("Acesso Removido", retornoAPI.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoAPI.andReturn().getResponse().getStatus());
	
		
	}
	
	@Test
	public void testRestApiObterAcessoID() throws JsonProcessingException, Exception {
		
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_OBTER_ID");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoAPI = mockMvc
				.perform(MockMvcRequestBuilders.get("/obterAcesso/"+ acesso.getId()) /*Irá realizar o post em nosso mapeamento*/
						.content(objectMapper.writeValueAsString(acesso)) /*Escreve o Conteúdo que será passado*/
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
				); 
		
		assertEquals(200, retornoAPI.andReturn().getResponse().getStatus());
		
		Acesso acessoRetorno = objectMapper.readValue(retornoAPI.andReturn().getResponse().getContentAsString(), Acesso.class);
	   
		assertEquals(acesso.getDescricao(), acessoRetorno.getDescricao());
		
	}
	

	@Test
	public void testRestApiObterAcessoDesc() throws JsonProcessingException, Exception {
		
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_TESTE_OBTER_LIST");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoAPI = mockMvc
				.perform(MockMvcRequestBuilders.get("/buscarPorDesc/OBTER_LIST") /*Irá realizar o post em nosso mapeamento*/
						.content(objectMapper.writeValueAsString(acesso)) /*Escreve o Conteúdo que será passado*/
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
				); 
		
		assertEquals(200, retornoAPI.andReturn().getResponse().getStatus());
		
		//Acesso acessoRetorno = objectMapper.readValue(retornoAPI.andReturn().getResponse().getContentAsString(), Acesso.class);
	   
		//assertEquals(acesso.getDescricao(), acessoRetorno.getDescricao());
		
		
		List<Acesso> retornoApiList = objectMapper.
				readValue(retornoAPI.andReturn()
						.getResponse().getContentAsString(), 
				new TypeReference<List<Acesso>>() {
				});
		
		assertEquals(1, retornoApiList.size());
		
		assertEquals(acesso.getDescricao(), retornoApiList.get(0).getDescricao());
		
		acessoRepository.deleteById(acesso.getId());
		
		
		
	}
	
	
	
	@Test
	public void testCadastraAcesso() {
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ADMIN");

		assertEquals(true, acesso.getId() == null);
		
		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		assertEquals(true, acesso.getId() > 0);
		
		assertEquals(true, acesso.getDescricao() == "ROLE_ADMIN");
		
		/*Carrega os dados salvos para testar*/
		Acesso acesso2 = acessoRepository.findById(acesso.getId()).get();
		
		assertEquals(acesso.getId(), acesso2.getId());
		
		/*Teste de Delete*/
		
		acessoRepository.deleteById(acesso2.getId());
		
		acessoRepository.flush();
		
		Acesso acesso3 = acessoRepository.findById(acesso.getId()).orElse(null);//OrElse retorna null se não existir
		
		assertEquals(true, acesso3 == null);
		
		/*Teste de Query*/
		
		acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ALUNO");
		
		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		List<Acesso> acessos = acessoRepository.buscarAcessoDesc("ALUNO".trim().toUpperCase());
		
		assertEquals(1, acessos.size());
		
		acessoRepository.deleteById(acesso.getId());
	}

}
