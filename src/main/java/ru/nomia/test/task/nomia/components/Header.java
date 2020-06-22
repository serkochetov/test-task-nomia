package ru.nomia.test.task.nomia.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class Header extends VerticalLayout {


    private final Text currentSectionName;

    public Header() {

        this.currentSectionName = new Text("Root");
        Text title = new Text("Current Section: ");

        setVisible(true);
        setPadding(true);
        add(title, currentSectionName);

    }

    public void setCurrentSectionName(String currentSectionName) {
        this.currentSectionName.setText(currentSectionName);
    }
}
