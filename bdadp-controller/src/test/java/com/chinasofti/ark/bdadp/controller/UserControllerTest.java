//package com.chinasofti.ark.bdadp.controller;
//
//import org.junit.Before;  
//import org.junit.Test;  
//import org.junit.runner.RunWith;  
//import org.mockito.InjectMocks;  
//import org.mockito.Mock;  
//import org.mockito.MockitoAnnotations;  
//import org.springframework.test.context.ContextConfiguration;  
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;  
//import org.springframework.test.context.web.WebAppConfiguration;  
//import org.springframework.test.web.servlet.MockMvc;  
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;  
//
//import static org.junit.Assert.assertEquals;  
//import static org.mockito.Mockito.verify;  
//import static org.mockito.Mockito.when;  
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;  
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;  
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;  
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;  
//
///** 
//* @Author : water  
//* @Date   : 2016年9月25日 
//* @Desc   : TODO
//* @version: V1.0
//*/ 
//@RunWith(SpringJUnit4ClassRunner.class)  
//@WebAppConfiguration  
//@ContextConfiguration(classes=MockServletContext.class) 
//public class UserControllerTest {
//	
//  private MockMvc mockMvc;  
//
//  @Mock  
//  private UserService userService;  
//
//  @InjectMocks  
//  private UserController userController;  
//
//  @Before  
//  public void setup() {  
//      MockitoAnnotations.initMocks(this);  
//      this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();  
//  }  
//
//
//  @Test  
//  public void testAdd() {  
//      int id = 1;  
//      String name = "邓海波";  
//      String password = "123456";  
//
//      User user = new User();  
//      user.setId(id);  
//      user.setName(name);  
//      user.setPassword(password);  
//      when(userService.addUser(user)).thenReturn(1);  
//
//      int restUser = userController.addUserInfo(user);  
//      assertEquals(1, restUser);  
//
//      verify(userService).addUser(user);  
//  }  
//
//  @Test  
//  public void testGetUserInfo() throws Exception {  
//      int userId = 1;  
//      String userName = "邓海波";  
//      String userPassword = "123456";  
//
//      User user = new User();  
//      user.setId(userId);  
//      user.setName(userName);  
//      user.setPassword(userPassword);  
//      when(userService.findOneUser(userId)).thenReturn(user);  
//
//      mockMvc.perform(get("/springmvc/api/getUser/{id}", userId))  
//              .andDo(print())  
//              .andExpect(status().isOk())  
//              .andExpect(jsonPath("id").value(userId))  
//              .andExpect(jsonPath("name").value(userName))  
//              .andExpect(jsonPath("password").value(userPassword));  
//
//      verify(userService).findOneUser(userId);  
//  }  
//
//}  
