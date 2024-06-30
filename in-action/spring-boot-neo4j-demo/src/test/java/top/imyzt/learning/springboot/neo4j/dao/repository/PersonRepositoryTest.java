package top.imyzt.learning.springboot.neo4j.dao.repository;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import top.imyzt.learning.springboot.neo4j.pojo.entity.Person;

import java.util.List;


@SpringBootTest
public class PersonRepositoryTest {

    @Resource
    private PersonRepository personRepository;

    @Test
    public void readAll() {
        List<Person> personList = personRepository.findAll();
        Assert.isTrue(!personList.isEmpty(), "isEmpty");
    }

    @Test
    public void delById() {
        personRepository.findById(71L).ifPresent(person -> {
            System.out.println("before delete: " + person.getName());
        });
        personRepository.deleteById(71L);
        Person person = personRepository.findById(71L).orElse(null);
        System.out.println("after delete: " + person);
    }

    @Test
    public void save() {
        Person person = new Person();
        person.setName("人参果树");
        personRepository.save(person);
    }

}
