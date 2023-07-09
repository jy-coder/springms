//package com.springproject.inventoryservice.repository;
//
//import com.springproject.inventoryservice.BaseTest;
//import com.springproject.inventoryservice.entity.Inventory;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.math.BigDecimal;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class InventoryRepositoryTest extends BaseTest {
//    @Autowired
//    private InventoryRepository postRepository;
//
//    @Test
//    public void shouldSaveInventory() {
//        Inventory expectedInventoryObject = new Inventory(null,"Inventory1", "Inventory1", BigDecimal.valueOf(1200));
//        Inventory actualInventoryObject = postRepository.save(expectedInventoryObject);
//        assertThat(actualInventoryObject).usingRecursiveComparison()
//                .ignoringFields("id").isEqualTo(expectedInventoryObject);
//    }
//
//}