package ca.mcgill.ecse321.scs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.ClassType;
import ca.mcgill.ecse321.scs.dao.ClassTypeRepository;

@SuppressWarnings("null")
@SpringBootTest
public class ClassTypeServiceTests {
    @Mock
    private ClassTypeRepository classTypeRepository;
    @InjectMocks
    private ClassTypeService classTypeService;

    @Test
    public void testCreateValidClassType() {
        // set up
        String className = "yoga";
        String description = "strech your body";
        boolean isApproved = true;
        when(classTypeRepository.save(any(ClassType.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        ClassType classType = classTypeService.createClassType(className, description, isApproved);

        // assert
        assertNotNull(classType);
        assertEquals(className, classType.getClassName());
        assertEquals(description, classType.getDescription());
        assertEquals(isApproved, classType.getIsApproved());
        verify(classTypeRepository, times(1)).save(any(ClassType.class));
    }

    @Test
    public void testCreateInvalidClassTypeNullName() {
        // set up
        String className = "";
        String description = "strech your body";
        boolean isApproved = true;
        when(classTypeRepository.save(any(ClassType.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            classTypeService.createClassType(className, description, isApproved);
        });

        // assert
        assertEquals("Class name cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateInvalidClassTypeNullDescription() {
        // set up
        String className = "yoga";
        String description = "";
        boolean isApproved = true;
        when(classTypeRepository.save(any(ClassType.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            classTypeService.createClassType(className, description, isApproved);
        });

        // assert
        assertEquals("Description cannot be empty.", exception.getMessage());
    }

    @Test
    public void testCreateClassTypeExists() {
        // set up
        String className = "yoga";
        String description = "strech your body";
        boolean isApproved = true;
        ClassType existingClassType = new ClassType(className, description, isApproved);
        when(classTypeRepository.findClassTypeByClassName(className)).thenReturn(existingClassType);

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            classTypeService.createClassType(className, description, isApproved);
        });

        // assert
        assertEquals("Class type with name " + className + " already exists.", exception.getMessage());
    }

    @Test
    public void testUpdateClassType() {
        // set up
        String className = "yoga";
        String description = "strech your body.";
        boolean isApproved = false;
        ClassType classType = new ClassType(className, description, isApproved);
        when(classTypeRepository.findClassTypeByClassName(className)).thenReturn(classType);
        when(classTypeRepository.save(any(ClassType.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        String description1 = "strech your body. haha";
        ClassType classType1 = classTypeService.updateClassType(className, description1, isApproved);

        // assert
        assertNotNull(classType);
        assertEquals(className, classType1.getClassName());
        assertEquals(description1, classType1.getDescription());
        assertEquals(isApproved, classType1.getIsApproved());
        verify(classTypeRepository, times(1)).save(any(ClassType.class));
    }

    @Test
    public void testUpdateClassTypeInvalidDescription() {
        // set up
        String className = "yoga";
        String description = "strech your body";
        boolean isApproved = true;
        ClassType classType = new ClassType(className, description, isApproved);
        when(classTypeRepository.findClassTypeByClassName(className)).thenReturn(classType);
        when(classTypeRepository.save(any(ClassType.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        String description1 = "strech only";
        ClassType classType1 = classTypeService.updateClassType(className, description1, isApproved);

        // assert
        assertNotNull(classType1);
        assertEquals(className, classType1.getClassName());
        assertEquals(description1, classType1.getDescription());
        assertEquals(isApproved, classType1.getIsApproved());
        verify(classTypeRepository, times(1)).save(any(ClassType.class));
    }

    @Test
    public void testUpdateClassTypeApproved() {
        // set up
        String className = "yoga";
        String description = "strech your body";
        boolean isApproved = true;
        ClassType classType = new ClassType(className, description, isApproved);
        when(classTypeRepository.findClassTypeByClassName(className)).thenReturn(classType);
        when(classTypeRepository.save(any(ClassType.class))).thenAnswer( (invocation) -> {
            return invocation.getArgument(0);
        });

        // act
        boolean isApproved1 = false;
        ClassType classType1 = classTypeService.updateClassType(className, isApproved1);

        // assert
        assertNotNull(classType1);
        assertEquals(className, classType1.getClassName());
        assertEquals(isApproved1, classType1.getIsApproved());
        verify(classTypeRepository, times(1)).save(any(ClassType.class));
    }

    @Test
    public void testGetClassType() {
        // set up
        String className = "yoga";
        String description = "strech your body";
        boolean isApproved = true;
        ClassType classType = new ClassType(className, description, isApproved);
        when(classTypeRepository.findClassTypeByClassName(className)).thenReturn(classType);

        // act
        ClassType foundClassType = classTypeService.getClassType(className);

        // assert
        assertNotNull(foundClassType);
        assertEquals(className, classType.getClassName());
        assertEquals(description, classType.getDescription());
        assertEquals(isApproved, classType.getIsApproved());
        verify(classTypeRepository, times(1)).findClassTypeByClassName(className);
    }

    @Test
    public void testGetClassTypeNull() {
        // set up
        String className = "yoga";

        // act
        Exception exception = assertThrows(SCSException.class, () -> {
            classTypeService.getClassType(className);
        });

        // assert
        assertEquals("Class type with name " + className + " does not exist.", exception.getMessage());
    }

    @Test
    public void testGetAllClassTypes() {
        // set up
        String className1 = "yoga";
        String description1 = "strech your body";
        boolean isApproved1 = true;
        String className2 = "swim";
        String description2 = "in the water";
        boolean isApproved2 = true;
        ClassType classType1 = new ClassType(className1, description1, isApproved1);
        ClassType classType2 = new ClassType(className2, description2, isApproved2);
        when(classTypeRepository.findAll()).thenReturn(List.of(classType1, classType2));

        // act
        List<ClassType> classTypes = classTypeService.getAllClassTypes();

        // assert
        assertNotNull(classTypes);
        assertEquals(2, classTypes.size());
        assertTrue(classTypes.contains(classType1));
        assertTrue(classTypes.contains(classType2));
        verify(classTypeRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteClassType() {
        // set up
        String className = "yoga";
        String description = "strech your body";
        boolean isApproved = true;
        ClassType classType = new ClassType(className, description, isApproved);
        when(classTypeRepository.findClassTypeByClassName(className)).thenReturn(classType).thenReturn(null); // adjusted to return null on subsequent calls

        // act
        classTypeService.deleteClassType(className);

        // assert deletion
        verify(classTypeRepository, times(1)).delete(classType);

        // act & assert exception
        Exception exception = assertThrows(SCSException.class, () -> {
            classTypeService.getClassType(className);
        });

        // assert
        assertEquals("Class type with name " + className + " does not exist.", exception.getMessage());
    }

    @Test
    public void testDeleteClassTypeNull() {
        // set up
        String className2 = "swim";
        when(classTypeRepository.findClassTypeByClassName(className2)).thenReturn(null);

        // act & assert
        SCSException exception = assertThrows(SCSException.class, () -> {
            classTypeService.deleteClassType(className2);
        });

        // assert
        assertEquals("Class type with name " + className2 + " does not exist.", exception.getMessage());
        verify(classTypeRepository, times(0)).delete(any(ClassType.class));
    }

    @Test
    public void testDeleteAllClassTypes() {
        // set up
        String className1 = "yoga";
        String description1 = "strech your body";
        boolean isApproved1 = true;
        String className2 = "swim";
        String description2 = "in the water";
        boolean isApproved2 = true;
        ClassType classType1 = new ClassType(className1, description1, isApproved1);
        ClassType classType2 = new ClassType(className2, description2, isApproved2);
        when(classTypeRepository.findAll()).thenReturn(List.of(classType1, classType2));
        
        // act
        classTypeService.deleteAllClassTypes();
        when(classTypeRepository.findAll()).thenReturn(Collections.emptyList()); // adjusted to return empty list on subsequent calls
        List<ClassType> classTypes = classTypeService.getAllClassTypes();
        
        // assert
        assertNotNull(classTypes);
        assertEquals(0, classTypes.size());
        verify(classTypeRepository, times(1)).deleteAll(); // verifies deleteAll was called
    }
}
