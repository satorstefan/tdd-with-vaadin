package com.example.application.views.login;

import com.example.application.AppTest;
import com.example.application.data.Role;
import com.example.application.views.gridwithfilters.GridwithFiltersView;
import com.example.application.views.helloworld.HelloWorldView;
import com.vaadin.flow.component.UI;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.mvysny.kaributesting.v10.LocatorJ._assertOne;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class LoginViewTest extends AppTest {

    public LoginViewTest() {
        super();
    }

    @Test
    public void helloworld_does_not_require_login() {
        _assertOne(HelloWorldView.class);
    }

    @Test
    public void securityRedirectWorks() {
        UI.getCurrent().navigate(GridwithFiltersView.class);
        _assertOne(LoginView.class);
    }
    
    @Test
    void login_as_admin_works() {
        login("admin","admin", List.of("ADMIN","USER"));
        UI.getCurrent().navigate(GridwithFiltersView.class);
        _assertOne(GridwithFiltersView.class);
    }
}