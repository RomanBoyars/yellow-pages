package org.yellow.pages.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.yellow.pages.YellowPagesApplication;
import org.yellow.pages.persistance.dto.PersonDTO;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {YellowPagesApplication.class})
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@Sql(scripts = "/jdbc/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/jdbc/clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PersonControllerTest {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // language=JSON
    public static final String UPDATE_PERSON_DTO = "{" +
            " \"id\": 1," +
            "  \"firstName\": \"Ivan\"," +
            "  \"middleName\": \"Ivan\"," +
            "  \"lastName\": \"Stepanov\"," +
            "  \"address\": \"Moskva\"," +
            "  \"phones\": [" +
            "    {" +
            "      \"number\": \"8 929 22 33 111\"" +
            "    }," +
            "    {" +
            "      \"number\": \"8 929 22 33 222\"" +
            "    }," +
            "    {" +
            "      \"number\": \"8 929 22 33 333\"" +
            "    }" +
            "  ]" +
            "}";

    @Autowired
    protected WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createPerson() throws Exception {
        String url = "/api/persons";
        String resultsJsonPath = "/results/rest/createPerson.json";

        // language=JSON
        String newPersonDTO = "{" +
                "  \"firstName\": \"Stepan\"," +
                "  \"middleName\": \"Stepanovich\"," +
                "  \"lastName\": \"Stepanov\"," +
                "  \"address\": \"Moskva\"," +
                "  \"phones\": [" +
                "    {" +
                "      \"number\": \"8 929 22 33 111\"" +
                "    }," +
                "    {" +
                "      \"number\": \"8 929 22 33 222\"" +
                "    }," +
                "    {" +
                "      \"number\": \"8 929 22 33 333\"" +
                "    }" +
                "  ]" +
                "}";

        String stringResponseBody = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(newPersonDTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertSingleResult(resultsJsonPath, stringResponseBody);
    }

    @Test
    public void getPersons() throws Exception {
        String url = "/api/persons";
        String resultsJsonPath = "/results/rest/getAllPersons.json";

        String stringResponseBody = mockMvc.perform(
                MockMvcRequestBuilders.get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertResults(resultsJsonPath, stringResponseBody, new TypeReference<List<PersonDTO>>() {
        });
    }

    @Test
    public void deletePerson() throws Exception {
        String url = "/api/persons/96bd67d0-ceb7-11eb-b8bc-0242ac130003";
        String resultsJsonPath = "/results/rest/deletedPerson.json";

        mockMvc.perform(
                MockMvcRequestBuilders.delete(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String getUrl = "/api/persons";

        String stringResponseBody = mockMvc.perform(
                MockMvcRequestBuilders.get(getUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertResults(resultsJsonPath, stringResponseBody, new TypeReference<List<PersonDTO>>() {
        });
    }

    @Test
    public void updatePerson() throws Exception {
        String url = "/api/persons";
        String resultsJsonPath = "/results/rest/updatePerson.json";

        // language=JSON
        String updatedPersonDTO = "{" +
                "    \"id\": \"96bd615e-ceb7-11eb-b8bc-0242ac130003\"," +
                "    \"firstName\": \"Ivan\"," +
                "    \"middleName\": \"Stepanovich\"," +
                "    \"lastName\": \"Ivanov\"," +
                "    \"address\": \"Saint-Petersburg\"," +
                "    \"phones\": [" +
                "      {" +
                "        \"id\": 1," +
                "        \"number\": \"8 999 11 11 111\"" +
                "      }," +
                "      {" +
                "        \"id\": 2," +
                "        \"number\": \"8 999 22 22 222\"" +
                "      }," +
                "      {" +
                "        \"id\": 3," +
                "        \"number\": \"8 999 33 33 333\"" +
                "      }" +
                "    ]" +
                "  }";

        String stringResponseBody = mockMvc.perform(
                MockMvcRequestBuilders.put(url)
                        .content(updatedPersonDTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertSingleResult(resultsJsonPath, stringResponseBody);
    }

    @Test
    public void updatePersonAddNumber() throws Exception {
        String url = "/api/persons";
        String resultsJsonPath = "/results/rest/updatePersonAddNumber.json";

        // language=JSON
        String updatedPersonDTO = "{" +
                "    \"id\": \"96bd615e-ceb7-11eb-b8bc-0242ac130003\"," +
                "    \"firstName\": \"Ivan\"," +
                "    \"middleName\": \"Stepanovich\"," +
                "    \"lastName\": \"Ivanov\"," +
                "    \"address\": \"Saint-Petersburg\"," +
                "    \"phones\": [" +
                "      {" +
                "        \"id\": 1," +
                "        \"number\": \"8 999 11 11 111\"" +
                "      }," +
                "      {" +
                "        \"id\": 2," +
                "        \"number\": \"8 999 22 22 222\"" +
                "      }," +
                "      {" +
                "        \"id\": 3," +
                "        \"number\": \"8 999 33 33 333\"" +
                "      }," +
                "      {" +
                "        \"number\": \"8 999 44 44 444\"" +
                "      }" +
                "    ]" +
                "  }";

        String stringResponseBody = mockMvc.perform(
                MockMvcRequestBuilders.put(url)
                        .content(updatedPersonDTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertSingleResult(resultsJsonPath, stringResponseBody);
    }

    @Test
    public void updatePersonDeleteNumber() throws Exception {
        String url = "/api/persons";
        String resultsJsonPath = "/results/rest/updatePersonRemoveNumber.json";

        // language=JSON
        String updatedPersonDTO = "{" +
                "    \"id\": \"96bd615e-ceb7-11eb-b8bc-0242ac130003\"," +
                "    \"firstName\": \"Ivan\"," +
                "    \"middleName\": \"Stepanovich\"," +
                "    \"lastName\": \"Ivanov\"," +
                "    \"address\": \"Saint-Petersburg\"," +
                "    \"phones\": [" +
                "      {" +
                "        \"id\": 1," +
                "        \"number\": \"8 999 11 11 111\"" +
                "      }," +
                "      {" +
                "        \"id\": 2," +
                "        \"number\": \"8 999 22 22 222\"" +
                "      }" +
                "    ]" +
                "  }";

        String stringResponseBody = mockMvc.perform(
                MockMvcRequestBuilders.put(url)
                        .content(updatedPersonDTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertSingleResult(resultsJsonPath, stringResponseBody);
    }

    private <T> void assertResults(String resultsJsonPath,
                                   String stringResponseBody, TypeReference<T> resultsType) throws java.io.IOException {
        T expectedResults = OBJECT_MAPPER.readValue(
                new ClassPathResource(resultsJsonPath).getFile(),
                resultsType);

        T actualResults = OBJECT_MAPPER.readValue(
                stringResponseBody,
                resultsType);

        assertEquals(expectedResults, actualResults);
    }

    private void assertSingleResult(String resultsJsonPath, String stringResponseBody) throws java.io.IOException {
        PersonDTO expectedResult = OBJECT_MAPPER.readValue(
                new ClassPathResource(resultsJsonPath).getFile(),
                PersonDTO.class);

        PersonDTO actualResult = OBJECT_MAPPER.readValue(
                stringResponseBody,
                PersonDTO.class);

        assertEquals(expectedResult.getAddress(), actualResult.getAddress());
        assertEquals(expectedResult.getFirstName(), actualResult.getFirstName());
        assertEquals(expectedResult.getMiddleName(), actualResult.getMiddleName());
        assertEquals(expectedResult.getLastName(), actualResult.getLastName());
        expectedResult.getPhones().forEach(
                phoneNumberDTO -> assertTrue(actualResult.getPhones()
                        .stream()
                        .anyMatch(phoneNumberDTO1 -> phoneNumberDTO.getNumber().equals(phoneNumberDTO1.getNumber())))
        );
        assertEquals(expectedResult.getPhones().size(), actualResult.getPhones().size());
    }
}