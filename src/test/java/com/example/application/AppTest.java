package com.example.application;

import com.github.mvysny.fakeservlet.FakeRequest;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.github.mvysny.kaributesting.v10.spring.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.spring.SpringServlet;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An abstract class which sets up Spring, Karibu-Testing and your app.
 * The easiest way to use this class in your tests is having your test class to extend
 * this class.
 * <p></p>
 * You can perform programmatic logins via {@link #login(String, String, List)}.
 * Alternatively, you can use the <code>@WithMockUser</code> annotation
 * as described at <a href="https://www.baeldung.com/spring-security-integration-tests">Spring Security IT</a>,
 * but you will need still to call {@link FakeRequest#setUserPrincipalInt(Principal)}
 * and {@link FakeRequest#setUserInRole(Function2)}.
 */
@SpringBootTest
@ActiveProfiles("test")
//@TestPropertySource(locations = "/application-test.properties")
public abstract class AppTest {
    private static Routes routes;

    protected AppTest() {
    }

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.example");
    }

    @Autowired
    protected ApplicationContext ctx;

    @Autowired
    protected UserDetailsService userDetailsService;
    
    protected void login(String user, String pass, final List<String> roles) {
        // taken from https://www.baeldung.com/manually-set-user-authentication-spring-security
        // also see https://github.com/mvysny/karibu-testing/issues/47 for more details.
        final List<SimpleGrantedAuthority> authorities = roles.stream().map(it -> new SimpleGrantedAuthority("ROLE_"+it)).collect(Collectors.toList());

        UserDetails userDetails = userDetailsService.loadUserByUsername(user);
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(userDetails, pass, authorities);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authReq);

        // however, you also need to make sure that ViewAccessChecker works properly;
        // that requires a correct MockRequest.userPrincipal and MockRequest.isUserInRole()
        final FakeRequest request = (FakeRequest) VaadinServletRequest.getCurrent().getRequest();

        //var test = UtilsKt.getMock(VaadinServletRequest.getCurrent().getRequest());
        
        request.setUserPrincipalInt(authReq);
        request.setUserInRole((principal, role) -> roles.contains(role));
    }

    protected void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        if (VaadinServletRequest.getCurrent() != null) {
            final FakeRequest request = (FakeRequest) VaadinServletRequest.getCurrent().getRequest();
            request.setUserPrincipalInt(null);
            request.setUserInRole((principal, role) -> false);
        }
    }

    @BeforeEach
    public void setup() {
        final Function0<UI> uiFactory = UI::new;
        final SpringServlet servlet = new MockSpringServlet(routes, ctx, uiFactory);
        MockVaadin.setup(uiFactory, servlet);
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }

    @AfterEach
    public void performLogout() {
        logout();
    }
}