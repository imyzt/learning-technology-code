package top.imyzt.learning.springboot.neo4j.pojo.entity;


import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

/**
 * @author imyzt
 * @date 2024/06/30
 * @description Person
 */
@Node(labels = "person")
@Data
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @Property
    private String name;

    @Relationship(type = "西游人物关系", direction = Relationship.Direction.OUTGOING)
    private List<PersonRelationShip> personRelationShips;
}
