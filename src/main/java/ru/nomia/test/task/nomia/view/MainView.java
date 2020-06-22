package ru.nomia.test.task.nomia.view;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nomia.test.task.nomia.components.Header;
import ru.nomia.test.task.nomia.components.ProductGrid;
import ru.nomia.test.task.nomia.components.SectionTree;

@Route()
public class MainView extends VerticalLayout {


    @Autowired
    private MainView(SectionTree sectionTree,
                     ProductGrid productGrid,
                     Header header) {
        sectionTree.setWidth("100%");


        HorizontalLayout horizontalLayout = new HorizontalLayout(sectionTree, productGrid);
        horizontalLayout.setWidth("100%");


        add(header, horizontalLayout);

    }

}
