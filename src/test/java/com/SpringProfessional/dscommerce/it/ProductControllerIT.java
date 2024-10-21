package com.SpringProfessional.dscommerce.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Propagation;

import com.SpringProfessional.dscommerce.dto.ProductDTO;
import com.SpringProfessional.dscommerce.entities.Category;
import com.SpringProfessional.dscommerce.entities.Product;
import com.SpringProfessional.dscommerce.services.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private String adminUsername, adminPassword, clientUsername, clientPassword;
	private String adminToken, clientToken, invalidToken;
	private Product product;
	private ProductDTO dto;	
	private String productName;
	
	private Long dependentId, existId, noExistId;
	
	
	
	
	
	@BeforeEach
	void set() throws Exception {
		
		adminUsername="alex@gmail.com";
		adminPassword="123456";
		clientUsername="maria@gmail.com";
		clientPassword="123456";
		
		existId=2L;
		noExistId=26L;
		dependentId=3L;
		
		adminToken= tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		clientToken= tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
		invalidToken= adminToken + "Xlap";//simulates wrong password
		
		Category category=new Category(2L, "Eletro");
		product=new Product(null, "Playstation 5", "Lorem ipsum, dolor sit amet consectetur adipisicing elit", 4000.00, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg");
		product.getCategories().add(category);
		dto=new ProductDTO(product);
		
		productName="Macbook";

	}
	
	
	@Test
	public void findAllShoulReturnPageWhenNameParamIsEmpty() throws Exception {
		
		ResultActions result=
				mockMvc.perform(get("/products")
				.contentType(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(1L));
		result.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
		result.andExpect(jsonPath("$.content[0].price").value(90.5));
		result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));
		
	}
	
	@Test
	public void findAllShoulReturnPageWhenNameParamIsNotEmpty() throws Exception {
		
		ResultActions result=
				mockMvc.perform(get("/products?name={productName}", productName)
				.contentType(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(3L));
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[0].price").value(1250.0));
		result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));
		
	}
	
	@Test
	public void insertShouldReturnProductDTOWhenValidDataAndAdminLogged() throws Exception {
		
		String jsonBody=objectMapper.writeValueAsString(dto);
		
		ResultActions result=mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").value(26L));
		result.andExpect(jsonPath("$.name").value("Playstation 5"));
		result.andExpect(jsonPath("$.description").value("Lorem ipsum, dolor sit amet consectetur adipisicing elit"));
		result.andExpect(jsonPath("$.price").value(4000.00));
		result.andExpect(jsonPath("$.imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));
		result.andExpect(jsonPath("$.categories[0].id").value(2L));
		
		
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidName() throws Exception {
		
		product.setName("");
		dto=new ProductDTO(product);
		String jsonBody=objectMapper.writeValueAsString(dto);
		
		ResultActions result=mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));		
		
		result.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidDescritpion() throws Exception {
		
		product.setDescription("");
		dto=new ProductDTO(product);
		String jsonBody=objectMapper.writeValueAsString(dto);
		
		ResultActions result=mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnprocessableEntity());

	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndPriceIsNegative() throws Exception {
		
		product.setPrice(-1.0);
		dto=new ProductDTO(product);
		String jsonBody=objectMapper.writeValueAsString(dto);
		
		ResultActions result=mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));		
		
		result.andExpect(status().isUnprocessableEntity());

	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndPriceIsZero() throws Exception {
		
		product.setPrice(0.0);
		dto=new ProductDTO(product);
		String jsonBody=objectMapper.writeValueAsString(dto);
		
		ResultActions result=mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));		
		
		result.andExpect(status().isUnprocessableEntity());

	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndProductHasNotCategory() throws Exception {
		
		product.getCategories().clear();
		dto=new ProductDTO(product);
		String jsonBody=objectMapper.writeValueAsString(dto);
		
		ResultActions result=mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));		
		
		result.andExpect(status().isUnprocessableEntity());

	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
	
		String jsonBody=objectMapper.writeValueAsString(dto);
		
		ResultActions result=mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + clientToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));		
		
		result.andExpect(status().isForbidden());

	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
	
		String jsonBody=objectMapper.writeValueAsString(dto);
		
		ResultActions result=mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + invalidToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));		
		
		result.andExpect(status().isUnauthorized());

	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIdExistAndAdminLogged() throws Exception {
			
		ResultActions result=mockMvc.perform(delete("/products/{id}", existId)
				.header("Authorization", "Bearer " + adminToken));
				
		result.andExpect(status().isNoContent());

	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenNoExistIdAndAdminLogged() throws Exception {
			
		ResultActions result=mockMvc.perform(delete("/products/{id}", noExistId)
				.header("Authorization", "Bearer " + adminToken));
				
		result.andExpect(status().isNotFound());

	}
	
	@Test
	@Transactional(propagation = Propagation.SUPPORTS)
	public void deleteShouldReturnBadRequestWhenDependentIdAndAdminLogged() throws Exception {
			
		ResultActions result=mockMvc.perform(delete("/products/{id}", dependentId)
				.header("Authorization", "Bearer " + adminToken)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isBadRequest());

	}
	
	@Test
	public void deleteShouldReturnForbiddenWhenIdExistAndClientLogged() throws Exception {
			
		ResultActions result=mockMvc.perform(delete("/products/{id}", existId)
				.header("Authorization", "Bearer " + clientToken)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isForbidden());

	}
	
	@Test
	public void deleteShouldReturnUnauthorizedWhenIdExistAndInvalidToken() throws Exception {
			
		ResultActions result=mockMvc.perform(delete("/products/{id}", existId)
				.header("Authorization", "Bearer " + invalidToken)
				.accept(MediaType.APPLICATION_JSON));
				
		result.andExpect(status().isUnauthorized());

	}
	
	

}
