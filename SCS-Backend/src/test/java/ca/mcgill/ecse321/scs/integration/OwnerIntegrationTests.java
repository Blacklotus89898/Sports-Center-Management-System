package ca.mcgill.ecse321.scs.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import ca.mcgill.ecse321.scs.dto.OwnerResponseDto;
import ca.mcgill.ecse321.scs.dto.OwnerRequestDto;
import ca.mcgill.ecse321.scs.dto.ErrorDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class OwnerIntegrationTests {
    @Autowired
    private TestRestTemplate client;

    private int ownerId;
    private final String NAME = "Sunny Doe";
    private final String EMAIL = "sunny.doe@gmail.com";
    private final String PASSWORD = "password";

    @Test
    @Order(1)
    public void testCreateOwner() {
        // set up
        OwnerRequestDto ownerRequestDto = new OwnerRequestDto(-1, NAME, EMAIL, PASSWORD);

        // act
        ResponseEntity<OwnerResponseDto> response = client.postForEntity("/owner", ownerRequestDto, OwnerResponseDto.class);
        
        // assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        ownerId = response.getBody().getId();
        assertEquals(NAME, response.getBody().getName());
        assertEquals(EMAIL, response.getBody().getEmail());
        assertEquals(PASSWORD, response.getBody().getPassword());
    }

    @Test
    @Order(2)
    public void testCreateOwnerInvalidEmail() {
        // set up
        OwnerRequestDto ownerRequestDto = new OwnerRequestDto(-1, NAME, "invalid email", PASSWORD);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/owner", ownerRequestDto, ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Email is not valid.", body.getErrors().get(0));
    }

    @Test
    @Order(3)
    public void testCreateOwnerInvalidPassword() {
        // set up
        OwnerRequestDto ownerRequestDto = new OwnerRequestDto(-1, NAME, EMAIL, "");

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/owner", ownerRequestDto, ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Password cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(4)
    public void testCreateOwnerInvalidName() {
        // set up
        OwnerRequestDto ownerRequestDto = new OwnerRequestDto(-1, "", EMAIL, PASSWORD);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/owner", ownerRequestDto, ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Name cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(4)
    public void testCreateOwnerDuplicateEmail() {
        // set up
        OwnerRequestDto ownerRequestDto = new OwnerRequestDto(-1, "any name", EMAIL, PASSWORD);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/owner", ownerRequestDto, ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("An account with this email already exists.", body.getErrors().get(0));
    }

    @Test
    @Order(5)
    public void testGetOwnerById() {
        // set up
        ResponseEntity<OwnerResponseDto> response = client.getForEntity("/owner/" + ownerId, OwnerResponseDto.class);
        
        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(NAME, response.getBody().getName());
        assertEquals(EMAIL, response.getBody().getEmail());
    }

    @Test
    @Order(6)
    public void testGetOwnerByIdNotFound() {
        // set up
        ResponseEntity<ErrorDto> response = client.getForEntity("/owner/12083", ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Owner not found.", body.getErrors().get(0));
    }

    @Test
    @Order(7)
    public void testUpdateOwner() {
        // set up
        OwnerRequestDto ownerRequestDto = new OwnerRequestDto(-1, "Jane Doe", "newemail@gmail.com", "newpassword");

        // act
        ResponseEntity<OwnerResponseDto> response = client.exchange("/owner/" + ownerId, HttpMethod.PUT, new HttpEntity<>(ownerRequestDto), OwnerResponseDto.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ownerId, response.getBody().getId());
        assertEquals("Jane Doe", response.getBody().getName());
        assertEquals("newemail@gmail.com", response.getBody().getEmail());
        assertEquals("newpassword", response.getBody().getPassword());
    }

    @Test
    @Order(8)
    public void testUpdateOwnerInvalidEmail() {
        // set up
        OwnerRequestDto ownerRequestDto = new OwnerRequestDto(-1, "Jane Doe", "invalid email", "newpassword");

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/owner/" + ownerId, HttpMethod.PUT, new HttpEntity<>(ownerRequestDto), ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Email is not valid.", body.getErrors().get(0));
    }

    @Test
    @Order(9)
    public void testUpdateOwnerInvalidPassword() {
        // set up
        OwnerRequestDto ownerRequestDto = new OwnerRequestDto(-1, "Jane Doe", EMAIL, "");

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/owner/" + ownerId, HttpMethod.PUT, new HttpEntity<>(ownerRequestDto), ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Password cannot be empty.", body.getErrors().get(0));

    }

    @Test
    @Order(10)
    public void testUpdateOwnerInvalidName() {
        // set up
        OwnerRequestDto ownerRequestDto = new OwnerRequestDto(-1, "", EMAIL, PASSWORD);

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/owner/" + ownerId, HttpMethod.PUT, new HttpEntity<>(ownerRequestDto), ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Name cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(11)
    public void testUpdateOwnerEmailExists() {
        // set up
        OwnerRequestDto existingOwnerRequestDto = new OwnerRequestDto(-1, "Jane Doe", "newnewemail@gmail.com", "random password");
        ResponseEntity<OwnerResponseDto> existingOwnerResponse = client.postForEntity("/owner", existingOwnerRequestDto, OwnerResponseDto.class);

        OwnerRequestDto ownerRequestDto = new OwnerRequestDto(-1, "Jane Doe", existingOwnerResponse.getBody().getEmail(), "newpassword");

        // act
        ResponseEntity<ErrorDto> response = client.exchange("/owner/" + ownerId, HttpMethod.PUT, new HttpEntity<>(ownerRequestDto), ErrorDto.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertEquals(1, body.getErrors().size());
        assertEquals("An account with this email already exists.", body.getErrors().get(0));

        // clean up
        client.delete("/owner/" + existingOwnerResponse.getBody().getId());
    }

    @Test
    @Order(12)
    public void testDeleteOwner() {
        // act
        client.delete("/owner/" + ownerId);

        // assert
        ResponseEntity<ErrorDto> response = client.getForEntity("/owner/" + ownerId, ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Owner not found.", body.getErrors().get(0));
    }

    @Test
    @Order(13)
    public void testDeleteOwnerNotFound() {
        // act
        int badId = -1978;
        ResponseEntity<ErrorDto> response = client.exchange("/owner/" + badId, HttpMethod.DELETE, HttpEntity.EMPTY, ErrorDto.class);
        
        // assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto body = response.getBody(); 
        assertEquals(1, body.getErrors().size());
        assertEquals("Owner not found.", body.getErrors().get(0));
    }
}
