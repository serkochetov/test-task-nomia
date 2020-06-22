package ru.nomia.test.task.nomia.components;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.material.Material;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nomia.test.task.nomia.domain.Section;
import ru.nomia.test.task.nomia.repository.SectionRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Stream;

@SpringComponent
@UIScope
public class SectionTree extends VerticalLayout implements KeyNotifier {
    private final SectionRepository sectionRepository;

    private final ProductGrid productGrid;
    private final Header header;

    private final HierarchicalDataProvider dataProvider;
    private final VerticalLayout sectionsEdit;


    TextField name = new TextField("Name");

    private final Button save = new Button("Save", VaadinIcon.ENTER.create());
    private final Button edit = new Button("Edit", VaadinIcon.ENTER.create());
    private final Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private final Button editSection;

    private final Binder<Section> binder = new Binder<>(Section.class);


    public Section currentSection;

    @Autowired
    public SectionTree(SectionRepository sectionRepository,
                       ProductGrid productGrid,
                       Header header) {
        this.sectionRepository = sectionRepository;
        this.productGrid = productGrid;
        this.header = header;

        //create new section in current section
        Button addNewSection = new Button("Add section", VaadinIcon.PLUS.create());

        //Edit current section
        editSection = new Button("Edit section", VaadinIcon.EDIT.create());
        addNewSection.setThemeName(Material.DARK);
        editSection.setThemeName(Material.DARK);
        editSection.setVisible(false);

        save.setThemeName(Material.DARK);
        edit.setThemeName(Material.DARK);
        Button cancel = new Button("Cancel");
        cancel.setThemeName(Material.LIGHT);
        delete.setThemeName(Material.DARK);

        binder.bindInstanceFields(this);
        setSpacing(true);


//        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        edit.addClickListener(e -> edit());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editSection(null));
        setVisible(false);


        TreeGrid<Section> lazyTree = new TreeGrid<>();
        lazyTree.addHierarchyColumn(Section::getName).setHeader("Sections");

        dataProvider =
                new AbstractBackEndHierarchicalDataProvider<Section, Void>() {

                    @Override
                    public int getChildCount(HierarchicalQuery<Section, Void> query) {
                        if (query.getParent() == null) {
                            return sectionRepository.countRoot().intValue();
                        } else {
                            return sectionRepository.countByPath(query.getParent().getPath()).intValue();
                        }
                    }

                    @Override
                    public boolean hasChildren(Section item) {
                        return !sectionRepository.findChildByPath(item.getPath()).isEmpty();
                    }

                    @Override
                    protected Stream<Section> fetchChildrenFromBackEnd(
                            HierarchicalQuery<Section, Void> query) {
                        if (query.getParent() == null) {
                            return sectionRepository.getRoot().stream();
                        } else {
                            return sectionRepository.findChildByPath(query.getParent().getPath()).stream();
                        }

                    }


                };


        addNewSection.addClickListener(e -> {
            Section section = new Section();
            section.setPath(Optional.ofNullable(currentSection).orElse(new Section()).getPath());
            editSection(section);

        });

        editSection.addClickListener(e -> editSection(currentSection));

        //refresh tree
        Button button = new Button("Refresh tree", VaadinIcon.REFRESH.create(),
                e -> dataProvider.refreshAll());
        button.setThemeName(Material.DARK);

        //go root section and refresh tree
        Button buttonRoot = new Button("Go back to root", VaadinIcon.ARROW_UP.create(),
                e -> goRoot(productGrid, header));
        button.setThemeName(Material.DARK);

        HorizontalLayout sectionActionsBtn = new HorizontalLayout(addNewSection, editSection);
        HorizontalLayout actions = new HorizontalLayout(save, edit, delete, cancel);
        this.sectionsEdit = new VerticalLayout(name, actions);
        sectionsEdit.setVisible(false);
        VerticalLayout sectionsTreeVerticalLayout = new VerticalLayout(sectionActionsBtn, sectionsEdit, lazyTree);
        HorizontalLayout bottomBtns = new HorizontalLayout(/*button, */buttonRoot);

        lazyTree.setDataProvider(dataProvider);

        add(sectionsTreeVerticalLayout, bottomBtns);
        setVisible(true);

        //select section in tree
        lazyTree.addItemClickListener(event -> {
            if (event.getItem() != null) {
                this.currentSection = sectionRepository.findByPath(event.getItem().getPath());

                editSection.setVisible(true);
                productGrid.addNewBtnVisible();
                productGrid.refreshGrid(currentSection);
                header.setCurrentSectionName(currentSection.getName() + " path:[" + currentSection.getPath() + "]");
            }

        });
    }

    private void goRoot(ProductGrid productGrid, Header header) {
        currentSection = null;
        dataProvider.refreshAll();
        productGrid.refreshGrid(currentSection);

        header.setCurrentSectionName("Root");
        editSection.setVisible(false);
        productGrid.addNewBtnInvisible();
    }

    private void edit() {
        sectionRepository.update(currentSection.getName(), currentSection.getPath());
        save.setVisible(true);
        edit.setVisible(true);
        delete.setVisible(true);
        sectionsEdit.setVisible(false);

        goRoot(productGrid, header);
    }

    private void editSection(Section newSection) {
        if (newSection == null) {
            sectionsEdit.setVisible(false);
            return;
        }

        if (newSection.getId() != null) {
            edit.setVisible(true);
            save.setVisible(false);
            delete.setVisible(true);
            currentSection = sectionRepository.findById(newSection.getId()).orElse(newSection);
        } else {
            save.setVisible(true);
            edit.setVisible(false);
            delete.setVisible(false);
            currentSection = newSection;
        }

        binder.setBean(currentSection);
        sectionsEdit.setVisible(true);

        name.focus();

    }

    private void delete() {
        if (currentSection.getId() != null) {
            sectionRepository.delete(currentSection);
        }
        sectionsEdit.setVisible(false);

        goRoot(productGrid, header);
    }

    @Transactional
    private void save() {
        if (currentSection.getPath() == null) {
            currentSection.setPath(String.valueOf(sectionRepository.countRoot() + 1));
        } else {
            currentSection.setPath(currentSection.getPath() + "." + (sectionRepository.countByPath(currentSection.getPath()) + 1));
        }

        sectionRepository.create(currentSection.getName(), currentSection.getPath());
        sectionsEdit.setVisible(false);

        goRoot(productGrid, header);
    }
}
