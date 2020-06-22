package ru.nomia.test.task.nomia.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "public", name = "section")
@Getter
@Setter
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "section", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Product> products = new HashSet<>();

    @Column(name = "path", nullable = false, columnDefinition = "ltree")
    private String path;

    public Section() {
    }
}
