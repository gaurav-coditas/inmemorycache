package com.coditas.inmemorycache;

import com.coditas.inmemorycache.model.Employee;
import com.coditas.inmemorycache.repository.EmployeeRepository;
import com.coditas.inmemorycache.util.InMemoryCache;
import org.h2.util.CacheObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * verifies database calls are cached
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan("com.coditas.inmemorycache")
@EnableAspectJAutoProxy
public class EmployeeRepositoryTest {

    InMemoryCache<String, Object> inMemoryCache = InMemoryCache.getInstance();

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final String CLASS_NAME = Employee.class.getTypeName();

    /**
     * verifies that object is cachech when it's first retrieved from database
     */
    @Test
    public void whenFindById_thenObjectIsCached() {
        Employee john = new Employee();
        john.setName("John");
        entityManager.persist(john);
        entityManager.flush();

        Employee fromDb = employeeRepository.findById(john.getId()).orElse(new Employee());
        Assert.assertEquals("Names don't match", john.getName(), fromDb.getName());
        Assert.assertNotNull("Object not stored in cache",inMemoryCache.get(String.valueOf(fromDb.getId())+CLASS_NAME));

        Employee fromCache = employeeRepository.findById(john.getId()).orElse(new Employee());
        Assert.assertNotNull("Null object",fromCache);
    }

    /**
     * verifies that object is cached when it is saved
     */
    @Test
    public void whenSave_ObjectIsAddedToCache() {
        Employee john = new Employee();
        john.setName("John");

        Employee saved = employeeRepository.save(john);
        Assert.assertNotNull("Object not stored in cache",inMemoryCache.get(String.valueOf(saved.getId())+CLASS_NAME));
    }

    /**
     * verifies that object is deleted from cache when it's deleted from database
     */
    @Test
    public void whenDelete_ObjectIsDeletedFromCache() {
        Employee john = new Employee();
        john.setName("John");

        Employee saved = employeeRepository.save(john);
        Assert.assertNotNull("Object not stored in cache",inMemoryCache.get(String.valueOf(saved.getId())+CLASS_NAME));

        employeeRepository.deleteById(saved.getId());
        Assert.assertNull("Object not deleted from cache",inMemoryCache.get(String.valueOf(saved.getId())+CLASS_NAME));
    }

    /**
     * verifies cache flush API
     */
    @Test
    public void whenFlushed_cacheSizeZero() {
        Employee john = new Employee();
        john.setName("John");

        Employee saved = employeeRepository.save(john);
        Assert.assertTrue(inMemoryCache.size() > 0);
        inMemoryCache.flush();
        Assert.assertTrue("Cache not flushed", inMemoryCache.size() == 0);
    }

    @Test
    public void cacheFullTest() {
        inMemoryCache.flush();
        for(int i=0; i< 110; i++) {
            Employee john = new Employee();
            john.setName("John" + i);
            employeeRepository.save(john);
        }
        long count = ((Set<Map.Entry>)inMemoryCache.getCacheMap().entrySet()).stream()
                .filter(map -> ((Employee)((InMemoryCache.CacheObject)map.getValue()).getValue()).getName().equals("John0")).count();
        Assert.assertEquals("Least recently accessed element not removed",0, count);
    }

}
