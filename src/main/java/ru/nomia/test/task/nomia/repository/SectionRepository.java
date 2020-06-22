package ru.nomia.test.task.nomia.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nomia.test.task.nomia.domain.Section;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SectionRepository extends CrudRepository<Section, Long> {
//    @Query(value = "SELECT * FROM Section WHERE path <@ CAST(:pathToSearch AS ltree)", nativeQuery = true)
//    List<Section> findAllByPath(@Param("pathToSearch") String pathToSearch);

    @Query(value = "SELECT * FROM Section WHERE path ~ '*.*{1}'", nativeQuery = true)
    List<Section> getRoot();

    @Query(value = "SELECT * FROM Section WHERE path ~ lquery(:pathToSearch || '.*{1}')", nativeQuery = true)
    List<Section> findChildByPath(@Param("pathToSearch") String pathToSearch);

    @Query(value = "SELECT * FROM Section WHERE path ~ lquery(:pathToSearch)", nativeQuery = true)
    Section findByPath(@Param("pathToSearch") String pathToSearch);

    @Query(value = "SELECT COUNT(*) FROM Section WHERE path ~ lquery(:pathToSearch || '.*{1}')", nativeQuery = true)
    Long countByPath(@Param("pathToSearch") String pathToSearch);

    @Query(value = "SELECT COUNT(*) FROM Section WHERE path ~ '*.*{1}'", nativeQuery = true)
    Long countRoot();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Section WHERE path <@ CAST(:pathToDelete AS ltree)", nativeQuery = true)
    void deleteAllByPath(@Param("pathToDelete") String pathToDelete);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Section SET path = CAST(:destinationPath AS ltree) || path WHERE path <@ CAST(:sourcePath AS ltree)", nativeQuery = true)
    void moveRootTreeDown(@Param("destinationPath") String destinationPath, @Param("sourcePath") String sourcePath);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Section SET path = CAST(:destinationPath AS ltree) || subpath(path, 1) WHERE path <@ CAST(:sourcePath AS ltree)", nativeQuery = true)
    void moveNonRootTreeDown(@Param("destinationPath") String destinationPath, @Param("sourcePath") String sourcePath);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Section (name, path) (SELECT name, CAST(:destinationPath AS ltree) || subpath(path, 1) FROM Section WHERE CAST(:sourcePath AS ltree) @> path)", nativeQuery = true)
    void copyTree(@Param("destinationPath") String destinationPath, @Param("sourcePath") String sourcePath);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Section(name, path) (SELECT name, CAST(path AS ltree) FROM ( VALUES (:name, :path)) as Section(name, path))", nativeQuery = true)
    void create(@Param("name") String name, @Param("path") String path);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Section SET name = :name WHERE path ~ lquery(:path)", nativeQuery = true)
    void update(@Param("name") String name, @Param("path") String path);


    @Query(value = "SELECT name, CAST(path as TEXT), nlevel(path) FROM Section", nativeQuery = true)
    List<TreeNLevel> nlevel();


    interface TreeNLevel {

        String getName();

        String getPath();

        Integer getNlevel();
    }


}
