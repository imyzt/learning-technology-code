package top.imyzt.learning.springboot.neo4j.pojo.entity;


import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

/**
 * @author imyzt
 * @date 2024/06/30
 * @description PersonRelationShip
 */
@Data
@RelationshipProperties
public class PersonRelationShip {

    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private Person child;

    @Property
    private String relation;

}
