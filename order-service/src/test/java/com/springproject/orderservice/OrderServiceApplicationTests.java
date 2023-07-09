//package com.springproject.orderservice;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.springproject.orderservice.dto.OrderRequest;
//import com.springproject.orderservice.repository.OrderRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//
//import java.math.BigDecimal;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class OrderServiceApplicationTests {
//	@Container
//	private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.26");
//	@Autowired
//	private MockMvc mockMvc;
//	@Autowired
//	private ObjectMapper objectMapper;
//	@Autowired
//	private OrderRepository orderRepository;
//
//	static {
//		mySQLContainer.start();
//	}
//
//	@DynamicPropertySource
//	static void setProperties(DynamicPropertyRegistry registry) {
//		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
//		registry.add("spring.datasource.username", mySQLContainer::getUsername);
//		registry.add("spring.datasource.password", mySQLContainer::getPassword);
//	}
//
//	@Test
//	void shouldCreateOrder() throws Exception {
//		OrderRequest orderRequest = getOrderRequest();
//		String orderRequestString = objectMapper.writeValueAsString(orderRequest);
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(orderRequestString))
//				.andExpect(status().isCreated());
//		Assertions.assertEquals(1, orderRepository.findAll().size());
//	}
//
//	private OrderRequest getOrderRequest() {
//		return OrderRequest.builder()
//				.name("iPhone 13")
//				.description("iPhone 13")
//				.price(BigDecimal.valueOf(1200))
//				.build();
//	}
//
//}
