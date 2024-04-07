package ca.mcgill.ecse321.scs.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.scs.dto.InstructorResponseDto;
import ca.mcgill.ecse321.scs.dto.InstructorRequestDto;
import ca.mcgill.ecse321.scs.dto.ErrorDto;
import ca.mcgill.ecse321.scs.dto.InstructorListDto;

/**
 * This class contains integration tests for the Instructor entity.
 * It tests the creation, retrieval, updating, and deletion of instructors.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class InstructorIntegrationTests {
    @Autowired
    private TestRestTemplate client;

    private int instructorId;
    private final String NAME = "John Doe";
    private final String EMAIL = "john.doe@gmail.com";
    private final String PASSWORD = "password";
    private final byte[] IMAGE = new byte[1024];

    @Test
    @Order(1)
    public void testCreateInstructor() {
        // set up
        InstructorRequestDto instructorRequestDto = new InstructorRequestDto(-1, NAME, EMAIL, PASSWORD, IMAGE);

        // act
        ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors", instructorRequestDto, InstructorResponseDto.class);
        
        // assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        instructorId = response.getBody().getId();
        assertEquals(NAME, response.getBody().getName());
        assertEquals(EMAIL, response.getBody().getEmail());
        assertEquals(PASSWORD, response.getBody().getPassword());
    }

    @Test
    @Order(2)
    public void testCreateInstructorInvalidEmail() {
        // set up
        InstructorRequestDto instructorRequestDto = new InstructorRequestDto(-1, NAME, "invalid email", PASSWORD, IMAGE);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/instructors", instructorRequestDto, ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Email is not valid.", body.getErrors().get(0));
            }

            @Test
            @Order(3)
            public void testCreateInstructorInvalidPassword() {
                // set up
                InstructorRequestDto instructorRequestDto = new InstructorRequestDto(-1, NAME, EMAIL, "", null);

                // act
                ResponseEntity<ErrorDto> response = client.postForEntity("/instructors", instructorRequestDto, ErrorDto.class);
                
                // assert
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

                ErrorDto body = response.getBody(); 
                assertEquals(1, body.getErrors().size());
                assertEquals("Password cannot be empty.", body.getErrors().get(0));
            }

            @Test
            @Order(4)
            public void testCreateInstructorInvalidName() {
                // set up
                InstructorRequestDto instructorRequestDto = new InstructorRequestDto(-1, "", EMAIL, PASSWORD, null);

                // act
                ResponseEntity<ErrorDto> response = client.postForEntity("/instructors", instructorRequestDto, ErrorDto.class);
                
                // assert
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

                ErrorDto body = response.getBody(); 
                assertEquals(1, body.getErrors().size());
                assertEquals("Name cannot be empty.", body.getErrors().get(0));
            }

            @Test
            @Order(4)
            public void testCreateInstructorDuplicateEmail() {
                // set up
                InstructorRequestDto instructorRequestDto = new InstructorRequestDto(-1, "any name", EMAIL, PASSWORD, null);

                // act
                ResponseEntity<ErrorDto> response = client.postForEntity("/instructors", instructorRequestDto, ErrorDto.class);
                
                // assert
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

                ErrorDto body = response.getBody(); 
                assertEquals(1, body.getErrors().size());
                assertEquals("An account with this email already exists.", body.getErrors().get(0));
            }

            @Test
            @Order(5)
            public void testGetInstructorById() {
                // set up
                ResponseEntity<InstructorResponseDto> response = client.getForEntity("/instructors/" + instructorId, InstructorResponseDto.class);
                
                // assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(NAME, response.getBody().getName());
                assertEquals(EMAIL, response.getBody().getEmail());
            }

            @Test
            @Order(6)
            public void testGetInstructorByIdNotFound() {
                // set up
                ResponseEntity<ErrorDto> response = client.getForEntity("/instructors/12083", ErrorDto.class);
                
                // assert
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                ErrorDto body = response.getBody(); 
                assertEquals(1, body.getErrors().size());
                assertEquals("Instructor not found.", body.getErrors().get(0));
            }

            @Test
            @Order(7)
            public void testUpdateInstructor() {
                // set up
                InstructorRequestDto instructorRequestDto = new InstructorRequestDto(-1, "Jane Doe", "newemail@gmail.com", "newpassword", null);

                // act
                ResponseEntity<InstructorResponseDto> response = client.exchange("/instructors/" + instructorId, HttpMethod.PUT, new HttpEntity<>(instructorRequestDto), InstructorResponseDto.class);

                // assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(instructorId, response.getBody().getId());
                assertEquals("Jane Doe", response.getBody().getName());
                assertEquals("newemail@gmail.com", response.getBody().getEmail());
                assertEquals("newpassword", response.getBody().getPassword());
            }

            @Test
            @Order(8)
            public void testUpdateInstructorInvalidEmail() {
                // set up
                InstructorRequestDto instructorRequestDto = new InstructorRequestDto(-1, "Jane Doe", "invalid email", "newpassword", null);

                // act
                ResponseEntity<ErrorDto> response = client.exchange("/instructors/" + instructorId, HttpMethod.PUT, new HttpEntity<>(instructorRequestDto), ErrorDto.class);
                
                // assert
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

                ErrorDto body = response.getBody(); 
                assertEquals(1, body.getErrors().size());
                assertEquals("Email is not valid.", body.getErrors().get(0));
            }

            @Test
            @Order(9)
            public void testUpdateInstructorInvalidPassword() {
                // set up
                InstructorRequestDto instructorRequestDto = new InstructorRequestDto(-1, "Jane Doe", EMAIL, "", null);

                // act
                ResponseEntity<ErrorDto> response = client.exchange("/instructors/" + instructorId, HttpMethod.PUT, new HttpEntity<>(instructorRequestDto), ErrorDto.class);

                // assert
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

                ErrorDto body = response.getBody(); 
                assertEquals(1, body.getErrors().size());
                assertEquals("Password cannot be empty.", body.getErrors().get(0));

            }

            @Test
            @Order(10)
            public void testUpdateInstructorInvalidName() {
                // set up
                InstructorRequestDto instructorRequestDto = new InstructorRequestDto(-1, "", EMAIL, PASSWORD, null);

                // act
                ResponseEntity<ErrorDto> response = client.exchange("/instructors/" + instructorId, HttpMethod.PUT, new HttpEntity<>(instructorRequestDto), ErrorDto.class);
                
                // assert
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

                ErrorDto body = response.getBody(); 
                assertEquals(1, body.getErrors().size());
                assertEquals("Name cannot be empty.", body.getErrors().get(0));
            }

            @Test
            @Order(11)
            public void testUpdateInstructorEmailExists() {
                // set up
                InstructorRequestDto existingInstructorRequestDto = new InstructorRequestDto(-1, "Jane Doe", "newnewemail@gmail.com", "random password", null);
                ResponseEntity<InstructorResponseDto> existingInstructorResponse = client.postForEntity("/instructors", existingInstructorRequestDto, InstructorResponseDto.class);

                InstructorRequestDto instructorRequestDto = new InstructorRequestDto(-1, "Jane Doe", existingInstructorResponse.getBody().getEmail(), "newpassword", null);

                // act
                ResponseEntity<ErrorDto> response = client.exchange("/instructors/" + instructorId, HttpMethod.PUT, new HttpEntity<>(instructorRequestDto), ErrorDto.class);

                // assert
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

                ErrorDto body = response.getBody();
                assertEquals(1, body.getErrors().size());
                assertEquals("An account with this email already exists.", body.getErrors().get(0));
            }

            @Test
            @Order(12)
            public void testDeleteInstructor() {
                // act
                ResponseEntity<Void> response = client.exchange("/instructors/" + instructorId, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
                
                // assert
                assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

                // check if instructor was deleted
                ResponseEntity<ErrorDto> response2 = client.getForEntity("/instructors/" + instructorId, ErrorDto.class);
                assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
                ErrorDto body = response2.getBody();
                assertEquals(1, body.getErrors().size());
                assertEquals("Instructor not found.", body.getErrors().get(0));
            }

            @Test
            @Order(13)
            public void testDeleteInstructorNotFound() {
                // act
                ResponseEntity<ErrorDto> response = client.getForEntity("/instructors/12083", ErrorDto.class);
                
                // assert
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                ErrorDto body = response.getBody(); 
                assertEquals(1, body.getErrors().size());
                assertEquals("Instructor not found.", body.getErrors().get(0));
            }

            @Test
            @Order(14)
            public void testDeleteAllInstructorsAndGetAllInstructors() {
                // set up
                InstructorRequestDto instructorRequestDto = new InstructorRequestDto(-1, NAME, EMAIL, PASSWORD, null);
                client.postForEntity("/instructors", instructorRequestDto, InstructorResponseDto.class);
                instructorRequestDto = new InstructorRequestDto(-1, NAME, "ranodom@gmail.com", PASSWORD, null);
                client.postForEntity("/instructors", instructorRequestDto, InstructorResponseDto.class);
                instructorRequestDto = new InstructorRequestDto(-1, NAME, "raaornstm@gmail.com", PASSWORD, null);
                client.postForEntity("/instructors", instructorRequestDto, InstructorResponseDto.class);

                // assert that there are >= 3 instructors
                ResponseEntity<InstructorListDto> response = client.getForEntity("/instructors", InstructorListDto.class);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertTrue(response.getBody().getInstructors().size() >= 3);

                // act
                ResponseEntity<Void> response2 = client.exchange("/instructors", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
                
                // assert
                assertEquals(HttpStatus.NO_CONTENT, response2.getStatusCode());

                // assert that there are no instructors
                ResponseEntity<InstructorListDto> response3 = client.getForEntity("/instructors", InstructorListDto.class);
                assertEquals(HttpStatus.OK, response3.getStatusCode());
                assertNotNull(response3.getBody());
                assertEquals(0, response3.getBody().getInstructors().size());
            }
        }
