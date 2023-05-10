package pro.sky.sockswarehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pro.sky.sockswarehouse.model.SocksDto;
import pro.sky.sockswarehouse.model.SocksEntity;
import pro.sky.sockswarehouse.repository.SocksRepository;
import pro.sky.sockswarehouse.service.SocksService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class SocksControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    SocksRepository socksRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    SocksService socksService;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:14.7-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setup() {
        socksRepository.deleteAll();
    }

    @Test
    void addSocksWhenNotExists() throws Exception {
        SocksDto socksDto = new SocksDto("green", 50, 15);
        mvc.perform(MockMvcRequestBuilders
                        .post("/socks/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(socksDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                });

        mvc.perform(MockMvcRequestBuilders
                        .get("/socks")
                        .param("color", "green")
                        .param("operation", "equal")
                        .param("cottonPart", "50")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    Integer expected = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), objectMapper.getTypeFactory().constructType(Integer.class));
                    assertThat(expected).isEqualTo(15);
                });
    }

    @Test
    void addSocksWhenExists() throws Exception {
        SocksDto socksDto = new SocksDto("green", 50, 15);
        SocksEntity greenSocks = new SocksEntity(1, "green", 50, 15);
        socksRepository.save(greenSocks);
        mvc.perform(MockMvcRequestBuilders
                        .post("/socks/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(socksDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                });

        mvc.perform(MockMvcRequestBuilders
                        .get("/socks")
                        .param("color", "green")
                        .param("operation", "equal")
                        .param("cottonPart", "50")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    Integer expected = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), objectMapper.getTypeFactory().constructType(Integer.class));
                    assertThat(expected).isEqualTo(30);
                });
    }

    @Test
    void removeSocks() throws Exception {
        SocksEntity redSocks = new SocksEntity(1, "red", 50, 15);
        SocksDto redSocksDto = new SocksDto("red", 50, 5);
        socksRepository.save(redSocks);

        mvc.perform(MockMvcRequestBuilders
                        .post("/socks/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(redSocksDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                });

        mvc.perform(MockMvcRequestBuilders
                        .get("/socks")
                        .param("color", "red")
                        .param("operation", "equal")
                        .param("cottonPart", "50")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    Integer expected = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), objectMapper.getTypeFactory().constructType(Integer.class));
                    assertThat(expected).isEqualTo(10);
                });
    }

    @Test
    void removeSocksNotEnough() throws Exception {
        SocksEntity redSocks = new SocksEntity(1, "red", 50, 15);
        SocksDto redSocksDto = new SocksDto("red", 50, 20);
        socksRepository.save(redSocks);

        mvc.perform(MockMvcRequestBuilders
                        .post("/socks/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(redSocksDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                });

        mvc.perform(MockMvcRequestBuilders
                        .get("/socks")
                        .param("color", "red")
                        .param("operation", "equal")
                        .param("cottonPart", "50")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    Integer expected = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), objectMapper.getTypeFactory().constructType(Integer.class));
                    assertThat(expected).isEqualTo(15);
                });
    }

    @Test
    void getAmountSocks() throws Exception {
        List<SocksEntity> socks = List.of(
                new SocksEntity(1, "red", 50, 10),
                new SocksEntity(2, "red", 30, 20),
                new SocksEntity(3, "red", 70, 30),
                new SocksEntity(4, "blue", 30, 40),
                new SocksEntity(5, "black", 30, 50),
                new SocksEntity(6, "black", 60, 60)
        );
        socksRepository.saveAll(socks);

        mvc.perform(MockMvcRequestBuilders
                        .get("/socks")
                        .param("color", "red")
                        .param("operation", "lessThan")
                        .param("cottonPart", "60")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    Integer expected = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), objectMapper.getTypeFactory().constructType(Integer.class));
                    assertThat(expected).isEqualTo(30);
                });
        mvc.perform(MockMvcRequestBuilders
                        .get("/socks")
                        .param("color", "black")
                        .param("operation", "moreThan")
                        .param("cottonPart", "50")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                    Integer expected = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), objectMapper.getTypeFactory().constructType(Integer.class));
                    assertThat(expected).isEqualTo(60);
                });
    }
}