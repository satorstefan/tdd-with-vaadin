package com.example.application.views.karibu;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Tabs")
@Route("tabs-form")
@Menu(order = 3, icon = LineAwesomeIconUrl.MAP_MARKER_SOLID)
@RolesAllowed("ADMIN")
public class TabsView extends Composite<VerticalLayout> {

    private final TabSheet tabs = new TabSheet();
    
    public TabsView() {
        var tab1 = new Tab("Tab 1");
        var vl1 = new VerticalLayout();
        vl1.add(new Button("Button 1"));

        var tab2 = new Tab("Tab 2");
        var vl2 = new VerticalLayout();
        vl2.add(new Button("Button 2"));

        tabs.add(tab1, vl1);
        tabs.add(tab2, vl2);
        
        getContent().add(tabs);
        
        //selecting the tab2 makes the test fail
        //tabs.setSelectedTab(tab2);
    }
}
 