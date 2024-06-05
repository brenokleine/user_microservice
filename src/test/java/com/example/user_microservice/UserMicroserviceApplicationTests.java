package com.example.user_microservice;

import com.example.user_microservice.dtos.UserRecordDto;
import com.example.user_microservice.models.UserModel;
import com.example.user_microservice.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
class UserMicroserviceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	@Test
	void testCreateUserSuccess() throws Exception {
		UserRecordDto userRecordDto = new UserRecordDto("Test user", "testuser@example.com");
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(userRecordDto, userModel);

		//mocks the user doesnt already exists
		Mockito.when(userService.getUserByEmail(userRecordDto.email())).thenReturn(null);

		//mocks the success of saving user and returns its userModel
		Mockito.when(userService.saveUser(Mockito.any(UserModel.class))).thenReturn(userModel);

		mockMvc.perform(MockMvcRequestBuilders.post("/users")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(userRecordDto)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userRecordDto.name()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userRecordDto.email()));
	}

	@Test
	void testCreateUserFailure() throws Exception {
		UserRecordDto userRecordDto = new UserRecordDto("Test user", "");
		mockMvc.perform(MockMvcRequestBuilders.post("/users")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(userRecordDto)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}


}
