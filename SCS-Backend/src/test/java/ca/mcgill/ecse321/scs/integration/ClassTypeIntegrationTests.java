package ca.mcgill.ecse321.scs.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.scs.model.ClassType;
import ca.mcgill.ecse321.scs.dto.ErrorDto;
import ca.mcgill.ecse321.scs.dto.ClassTypeListDto;
import ca.mcgill.ecse321.scs.dto.ClassTypeRequestDto;
import ca.mcgill.ecse321.scs.dto.ClassTypeResponseDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ClassTypeIntegrationTests {
    @Autowired
    private TestRestTemplate client;

    private final String CLASSNAME = "yoga";
    private final String DESCRIPTION = "strech your body";
    private final boolean ISAPPROVED = true;

    @AfterAll
    public void cleanUp() {
        client.exchange("/classTypes", HttpMethod.DELETE, null, Void.class);
    }

    @Test
    @Order(1)
    public void testCreateClassType() {
        // set up
        ClassTypeRequestDto request = new ClassTypeRequestDto(new ClassType(CLASSNAME, DESCRIPTION, ISAPPROVED));

        // act
        ResponseEntity<ClassTypeResponseDto> response = client.postForEntity("/classType", request, ClassTypeResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        ClassTypeResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(CLASSNAME, body.getClassName());
        assertEquals(DESCRIPTION, body.getDescription());
        assertEquals(ISAPPROVED, body.getIsApproved());
    }

    @Test
    @Order(2)
    public void testCreateClassTypeClassNameExists() {
        // set up
        ClassTypeRequestDto request = new ClassTypeRequestDto(new ClassType(CLASSNAME, DESCRIPTION, ISAPPROVED));

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/classType", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Class type with name " + CLASSNAME + " already exists.", body.getErrors().get(0));
    }

    @Test
    @Order(3)
    public void testCreateClassTypeInvalidClassName() {
        // set up
        ClassTypeRequestDto request = new ClassTypeRequestDto(new ClassType(new String(""), DESCRIPTION, ISAPPROVED));

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/classType", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(4)
    public void testCreateClassTypeInvalidDescription() {
        // set up
        ClassTypeRequestDto request = new ClassTypeRequestDto(CLASSNAME, new String(""), ISAPPROVED);

        // act
        ResponseEntity<ErrorDto> response = client.postForEntity("/classType", request, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Description cannot be empty.", body.getErrors().get(0));
    }

    @Test
    @Order(5)
    public void testGetClassType() {
        // act
        ResponseEntity<ClassTypeResponseDto> response = client.getForEntity("/classType/" + CLASSNAME, ClassTypeResponseDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        ClassTypeResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(CLASSNAME, body.getClassName());
        assertEquals(DESCRIPTION, body.getDescription());
        assertEquals(ISAPPROVED, body.getIsApproved());
    }

    @Test
    @Order(6)
    public void testGetClassTypeClassNameDoesNotExist() {
        // tests that a schedule cannot be retrieved if the year does not exist

        // act
        String className = "swim";
        ResponseEntity<ErrorDto> response = client.getForEntity("/classType/" + className, ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Class type with name " + className + " does not exist.", body.getErrors().get(0));
    }

    @Test
    @Order(7)
    public void testGetAllClassTypes() {
        // act
        ResponseEntity<ClassTypeListDto> response = client.getForEntity("/classTypes", ClassTypeListDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ClassTypeListDto body = response.getBody();
        assertNotNull(body);
        assertTrue(body.getClassTypes().size() > 0);
        assertEquals(CLASSNAME, body.getClassTypes().get(0).getClassName());
        assertEquals(DESCRIPTION, body.getClassTypes().get(0).getDescription());
        assertEquals(ISAPPROVED, body.getClassTypes().get(0).getIsApproved());
    }

    @SuppressWarnings("null")
    @Test
    @Order(8)
    public void testDeleteClassType() {
        // Perform DELETE request
        ResponseEntity<Void> response = client.exchange("/classType/{className}", HttpMethod.DELETE, null, Void.class, CLASSNAME);

        // Assert the response status code
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Assert that the schedule was deleted
        ResponseEntity<ErrorDto> getResponse = client.getForEntity("/classType/" + CLASSNAME, ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
        assertEquals("Class type with name " + CLASSNAME + " does not exist.", getResponse.getBody().getErrors().get(0));
    }

    @Test
    @Order(9)
    public void testDeleteClassTypeClassNameDoesNotExist() {
        // act
        String className = "swim";
        ResponseEntity<ErrorDto> response = client.exchange("/classType/{className}", HttpMethod.DELETE, null, ErrorDto.class, className);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody(); 
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Class type with name " + className + " does not exist.", body.getErrors().get(0));
    }

    @SuppressWarnings("null")
    @Test
    @Order(10)
    public void testDeleteAllSchedules() {
        // create schedules
        String className = "swim";
        String description = "under the water";
        boolean isApproved = true;
        client.postForEntity("/classType", new ClassTypeRequestDto(new ClassType(CLASSNAME, DESCRIPTION, ISAPPROVED)), ClassTypeResponseDto.class);
        client.postForEntity("/classType", new ClassTypeRequestDto(new ClassType(className, description, isApproved)), ClassTypeResponseDto.class);

        // Perform DELETE request
        ResponseEntity<Void> response = client.exchange("/classTypes", HttpMethod.DELETE, null, Void.class);

        // Assert the response status code
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Assert that all schedules were deleted
        ResponseEntity<ClassTypeListDto> getResponse = client.getForEntity("/classTypes", ClassTypeListDto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(0, getResponse.getBody().getClassTypes().size());
    }

    @Test
    @Order(11)
    public void testGetAllClassTypesByIsApproved() {
        // create schedules
        String className = "swim";
        String description = "under the water";
        boolean isApproved = true;
        client.postForEntity("/classType", new ClassTypeRequestDto(new ClassType(CLASSNAME, DESCRIPTION, ISAPPROVED)), ClassTypeResponseDto.class);
        client.postForEntity("/classType", new ClassTypeRequestDto(new ClassType(className, description, isApproved)), ClassTypeResponseDto.class);

        // act
        ResponseEntity<ClassTypeListDto> response = client.getForEntity("/classTypes/approved/" + isApproved, ClassTypeListDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ClassTypeListDto body = response.getBody();
        assertNotNull(body);
        assertTrue(body.getClassTypes().size() > 0);
        assertEquals(CLASSNAME, body.getClassTypes().get(0).getClassName());
        assertEquals(DESCRIPTION, body.getClassTypes().get(0).getDescription());
        assertEquals(ISAPPROVED, body.getClassTypes().get(0).getIsApproved());
    }

    @Test
    @Order(12)
    public void testGetAllClassTypesByIsApprovedIsFalse() {
        // create schedules
        String className = "swim2";
        String description = "under the water";
        boolean isApproved = false;
        client.postForEntity("/classType", new ClassTypeRequestDto(new ClassType(CLASSNAME, DESCRIPTION, ISAPPROVED)), ClassTypeResponseDto.class);
        client.postForEntity("/classType", new ClassTypeRequestDto(new ClassType(className, description, isApproved)), ClassTypeResponseDto.class);

        // act
        ResponseEntity<ClassTypeListDto> response = client.getForEntity("/classTypes/approved/" + isApproved, ClassTypeListDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ClassTypeListDto body = response.getBody();
        assertNotNull(body);
        assertTrue(body.getClassTypes().size() > 0);
        assertEquals(className, body.getClassTypes().get(0).getClassName());
        assertEquals(description, body.getClassTypes().get(0).getDescription());
        assertEquals(isApproved, body.getClassTypes().get(0).getIsApproved());
    }

    @Test
    @Order(12)
    public void testGetAllClassTypesByIsApprovedIsInvalid() {
        // act
        ResponseEntity<ErrorDto> response = client.getForEntity("/classTypes/approved/invalid", ErrorDto.class);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(13)
    public void testChangeClassTypeApprovedStatusToFalse() {
        // create schedules
        String className = "swim";
        String description = "under the water";
        boolean isApproved = true;
        client.postForEntity("/classType", new ClassTypeRequestDto(new ClassType(CLASSNAME, DESCRIPTION, ISAPPROVED)), ClassTypeResponseDto.class);
        client.postForEntity("/classType", new ClassTypeRequestDto(new ClassType(className, description, isApproved)), ClassTypeResponseDto.class);

        // act
        ResponseEntity<ClassTypeResponseDto> response = client.exchange("/classTypes/" + CLASSNAME + "/approved/false", HttpMethod.PUT, null, ClassTypeResponseDto.class, className);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ClassTypeResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(CLASSNAME, body.getClassName());
        assertEquals(DESCRIPTION, body.getDescription());
        assertEquals(!ISAPPROVED, body.getIsApproved());
    }

    @Test
    @Order(14)
    public void testChangeClassTypeApprovedStatusToTrue() {
        // create schedules
        String className = "swim";
        String description = "under the water";
        boolean isApproved = false;
        client.postForEntity("/classType", new ClassTypeRequestDto(new ClassType(CLASSNAME, DESCRIPTION, ISAPPROVED)), ClassTypeResponseDto.class);
        client.postForEntity("/classType", new ClassTypeRequestDto(new ClassType(className, description, isApproved)), ClassTypeResponseDto.class);

        // act
        ResponseEntity<ClassTypeResponseDto> response = client.exchange("/classTypes/" + className + "/approved/true", HttpMethod.PUT, null, ClassTypeResponseDto.class, className);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ClassTypeResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(className, body.getClassName());
        assertEquals(description, body.getDescription());
        assertEquals(!isApproved, body.getIsApproved());
    }

    @Test
    @Order(15)
    public void testChangeClassTypeApprovedStatusClassNameDoesNotExist() {
        // act
        String className = "no swimming";
        ResponseEntity<ErrorDto> response = client.exchange("/classTypes/" + className + "/approved/true", HttpMethod.PUT, null, ErrorDto.class, className);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Class type with name " + className + " does not exist.", body.getErrors().get(0));
    }

    @Test
    @Order(16)
    public void testChangeClassTypeApprovedStatusInvalidBoolean() {
        // act
        ResponseEntity<ErrorDto> response = client.exchange("/classTypes/swim/approved/invalid", HttpMethod.PUT, null, ErrorDto.class, CLASSNAME);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}