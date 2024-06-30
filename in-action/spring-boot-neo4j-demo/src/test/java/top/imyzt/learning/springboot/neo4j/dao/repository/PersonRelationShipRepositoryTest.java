package top.imyzt.learning.springboot.neo4j.dao.repository;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.imyzt.learning.springboot.neo4j.pojo.entity.Person;
import top.imyzt.learning.springboot.neo4j.pojo.entity.PersonRelationShip;

import java.util.List;
import java.util.Map;


@SpringBootTest
public class PersonRelationShipRepositoryTest {

    @Resource
    private PersonRepository personRepository;
    @Resource
    private PersonRelationShipRepository personRelationShipRepository;

    @Test
    public void addRelation() {
        Person personYangjian = personRepository.findByName("杨戬");
        Person person2 = new Person();
        person2.setName("玉鼎真人");
        PersonRelationShip relationShip = new PersonRelationShip();
        relationShip.setChild(person2);
        relationShip.setRelation("师傅");
        personYangjian.getPersonRelationShips().add(relationShip);
        personRepository.save(personYangjian);
    }

    @Test
    public void addRelation2() {
        personRepository.createRelation("玉皇大帝", "妻子", "王母娘娘");
    }

    @Test
    public void queryRelation() {
        List<Map<String, String>> personRelationShips = personRelationShipRepository.findPersonRelationShips();
        System.out.println(personRelationShips);
    }
}
