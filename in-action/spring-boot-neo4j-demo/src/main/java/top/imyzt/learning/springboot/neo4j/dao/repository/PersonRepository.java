package top.imyzt.learning.springboot.neo4j.dao.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import top.imyzt.learning.springboot.neo4j.pojo.entity.Person;

/**
 * @author imyzt
 * @date 2024/06/30
 * @description Repository
 */
@Repository
public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Person findByName(String name);

    /**
     * 创建人物关系
     * @param from 源
     * @param relation 关系
     * @param to 目标
     */
    @Query("match (n:person {name: $from}),(m:person {name: $to}) " +
            "create (n)-[:西游人物关系{relation:$relation}]->(m)")
    void createRelation(String from, String relation, String to);
}
