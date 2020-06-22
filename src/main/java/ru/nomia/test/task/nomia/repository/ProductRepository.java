package ru.nomia.test.task.nomia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nomia.test.task.nomia.domain.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    @Query(value = "SELECT * FROM Product p WHERE p.section_id = :id", nativeQuery = true)
    List<Product> findAllBySection(@Param("id") Long id);

    @Query(value = "SELECT * FROM Product p WHERE p.section_id = :id AND p.name like :name%", nativeQuery = true)
    List<Product> findAllByNameAndSection(@Param("id") Long id, @Param("name") String name);

    Page<Product> findAllBySectionId(Long sectionId, Pageable pageable);

    Long countBySectionId(Long sectionId);


}
