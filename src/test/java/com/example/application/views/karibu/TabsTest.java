package com.example.application.views.karibu;

import com.example.application.AppTest;
import com.github.mvysny.kaributesting.v23.TabsKt;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.tabs.Tab;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.mvysny.kaributesting.v10.LocatorJ._assertOne;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;

class TabsViewTest extends AppTest {

    @Test
    void too_many_buttons() {
        login("admin", "admin", List.of("ADMIN", "USER"));
        UI.getCurrent().navigate(TabsView.class);
        _assertOne(TabsView.class);

        Tab tab1 = null;
        Tab tab2 = null;
        tab1 = _get(Tab.class, spec -> spec.withLabel("Tab 1"));
        tab2 = _get(Tab.class, spec -> spec.withLabel("Tab 2"));
        TabsKt._select(tab1);
        TabsKt._select(tab2);
        
        _assertOne(Button.class);
    }

}