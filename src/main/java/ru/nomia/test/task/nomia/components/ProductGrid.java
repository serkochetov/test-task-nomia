package ru.nomia.test.task.nomia.components;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.material.Material;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nomia.test.task.nomia.domain.Product;
import ru.nomia.test.task.nomia.domain.Section;
import ru.nomia.test.task.nomia.repository.ProductRepository;
import ru.nomia.test.task.nomia.service.ProductService;

import java.util.Optional;

@SpringComponent
@UIScope
public class ProductGrid extends VerticalLayout implements KeyNotifier {
    private Product product;

    TextField name = new TextField("Name");

    private final Binder<Product> binder = new Binder<>(Product.class);
    private Section currentSection;

    private final ProductRepository productRepository;

    private final Grid<Product> grid;
    private final Button addNewBtn;
    private final VerticalLayout addNewProductActionLayout;

    @Autowired
    public ProductGrid(ProductRepository productRepository,
                       ProductService productService) {

        Button save = new Button("Save", VaadinIcon.ENTER.create());
        save.setThemeName(Material.DARK);
        Button cancel = new Button("Cancel");
        cancel.setThemeName(Material.LIGHT);

        binder.bindInstanceFields(this);
        setSpacing(true);

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        cancel.addClickListener(e -> editProduct(null));
        setVisible(false);

        this.productRepository = productRepository;


        grid = new Grid<>(Product.class, false);
        grid.setThemeName(Material.DARK);
        grid.addColumn("id");
        grid.addColumn(Product::getName).setHeader("Name");
        grid.addComponentColumn(item -> createRemoveButton(grid, item))
                .setHeader("Actions");

        CallbackDataProvider<Product, Void> dataProvider = DataProvider
                .fromCallbacks(query -> productService
                                .findAllBySection(query.getOffset(), query.getLimit(),
                                        Optional.ofNullable(currentSection)
                                                .orElse(new Section()).getId()).stream(),
                        query -> productService.count(Optional.ofNullable(currentSection).orElse(new Section()).getId()));

        //Grid with lazy loading
        grid.setDataProvider(dataProvider);
        grid.setSelectionMode(Grid.SelectionMode.NONE);


        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        this.addNewProductActionLayout = new VerticalLayout(name, actions);
        this.addNewProductActionLayout.setVisible(false);


        //add new product in current section
        addNewBtn = new Button("Add product", VaadinIcon.PLUS.create(), e -> {
            Product newProduct = new Product();
            newProduct.setSection(currentSection);

            addNewProductActionLayout.setVisible(true);
            editProduct(newProduct);
        });
        addNewBtn.setThemeName(Material.DARK);
        addNewBtn.setVisible(false);

        VerticalLayout editLayout = new VerticalLayout(addNewBtn, addNewProductActionLayout, grid);

        add(editLayout);
        setVisible(true);
    }

    private HorizontalLayout createRemoveButton(Grid<Product> grid, Product item) {

        //remove product by section
        Button removeBtn = new Button("Remove", clickEvent -> {
            delete(item);
            grid.getDataProvider().refreshAll();
        });

        //edit product by section
        Button editBtn = new Button("Edit", clickEvent -> {
            editProduct(item);
            grid.getDataProvider().refreshAll();
        });


        return new HorizontalLayout(editBtn, removeBtn);
    }

    //component for edit section
    private void editProduct(Product newProduct) {
        if (newProduct == null) {
            addNewProductActionLayout.setVisible(false);
            return;
        }

        if (newProduct.getId() != null) {
            product = productRepository.findById(newProduct.getId()).orElse(newProduct);
        } else {
            product = newProduct;
        }

        binder.setBean(product);
        addNewProductActionLayout.setVisible(true);

        name.focus();
    }

    private void delete(Product currentProduct) {
        productRepository.delete(currentProduct);
        grid.getDataProvider().refreshAll();
        addNewProductActionLayout.setVisible(false);
    }

    private void save() {
        productRepository.save(product);
        grid.getDataProvider().refreshAll();
        addNewProductActionLayout.setVisible(false);
    }

    public void refreshGrid(Section currentSection) {
        this.currentSection = currentSection;
        grid.getDataProvider().refreshAll();
    }

    public void addNewBtnInvisible() {
        addNewBtn.setVisible(false);
    }

    public void addNewBtnVisible() {
        addNewBtn.setVisible(true);
    }
}
