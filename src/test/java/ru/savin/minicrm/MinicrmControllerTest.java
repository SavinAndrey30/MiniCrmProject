package ru.savin.minicrm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("alena")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/create-employee-list-before.sql"}, executionPhase =
        Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-employee-list-after.sql", "/create-user-after.sql"}, executionPhase =
        Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MinicrmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void crmPageTest() throws Exception {
        mockMvc.perform(get("/crm"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div[1]/span[1]").string("alena"));

    }

    @Test
    public void employeeListTest() throws Exception {
        mockMvc.perform(get("/crm"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/table/tbody/tr").nodeCount(4));
    }

    @Test
    public void searchEmployeeTest() throws Exception {
        mockMvc.perform(get("/crm/search").param("employeeName", "Savin"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='1']").exists());
    }

    @Test
    public void addEmployeeToListTest() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/crm/save")
                .file("fileImage", "123".getBytes())
                .param("firstName", "Employee1")
                .param("lastName", "Employee1")
                .param("email", "employee@mail.ru")
                .with(csrf());


        mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/crm"));

        mockMvc.perform(get("/crm"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/table/tbody/tr").nodeCount(5))
                .andExpect(xpath("//*[@id='10']").exists())
                .andExpect(xpath("//*[@id='10']/td[1]").string("Employee1"))
                .andExpect(xpath("//*[@id='10']/td[2]").string("Employee1"))
                .andExpect(xpath("//*[@id='10']/td[3]").string("employee@mail.ru"));
    }

    @Test
    public void updateEmployeeToListTest() throws Exception {
        mockMvc.perform(post("/crm/update")
                .param("id", "4")
                .param("firstName", "Employee4")
                .param("lastName", "Employee4")
                .param("email", "employee4@mail.ru")
                .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/crm"));

        mockMvc.perform(get("/crm"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='4']/td[1]").string("Employee4"))
                .andExpect(xpath("//*[@id='4']/td[2]").string("Employee4"))
                .andExpect(xpath("//*[@id='4']/td[3]").string("employee4@mail.ru"));
    }

    @Test
    public void deleteEmployeeTest() throws Exception {
        mockMvc.perform(get("/crm/delete")
                .param("id", "4"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/crm"));

        mockMvc.perform(get("/crm"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='4']").doesNotExist());

    }

    @Test
    public void showEmployeeDetailsTest() throws Exception {
        mockMvc.perform(get("/crm/showEmployeeDetails")
                .param("id", "1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }
}

