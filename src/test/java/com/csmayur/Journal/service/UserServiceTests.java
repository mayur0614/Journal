package com.csmayur.Journal.service;

import com.csmayur.Journal.repository.UserEntryRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserEntryRepo userEntryRepo ;

    @Disabled
    @Test
    public void testFindByUsername(){
        assertNotNull(userEntryRepo.findByUserName("mayur"));
    }

    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,2,4",
            "3,3,6"
    })
    public void  test(int a , int b , int expected){
        assertEquals(expected,a+b);
    }
}
