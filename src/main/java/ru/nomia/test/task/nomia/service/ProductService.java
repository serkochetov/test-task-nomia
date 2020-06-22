package ru.nomia.test.task.nomia.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.nomia.test.task.nomia.domain.Product;
import ru.nomia.test.task.nomia.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> findAllBySection(int offset, int limit, Long sectionId) {

        if (sectionId == null) {
            return new ArrayList<>();
        }

        int page = offset / limit;

        PageRequest pageRequest = PageRequest.of(page, limit);
        List<Product> items = repository.findAllBySectionId(sectionId, pageRequest).getContent();
        return items.subList(offset % limit, items.size());
    }

    public Integer count(Long sectionId) {
        if (sectionId == null) {
            return 0;
        }
        return Math.toIntExact(repository.countBySectionId(sectionId));
    }
}
