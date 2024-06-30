package top.imyzt.learning.springboot.neo4j.dao.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import top.imyzt.learning.springboot.neo4j.pojo.entity.Person;
import top.imyzt.learning.springboot.neo4j.pojo.entity.PersonRelationShip;

import java.util.List;
import java.util.Map;

/**
 * @author imyzt
 * @date 2024/06/30
 * @description Repository
 */
@Repository
public interface PersonRelationShipRepository extends Neo4jRepository<PersonRelationShip, Long> {

    @Query("match (n:person),(r:西游人物关系),(m:person) " +
            "where n.name=r.from and m.name = r.to " +
            "return n.name,r.relation,m.name")
    List<Map<String, String>> findPersonRelationShips();
}
