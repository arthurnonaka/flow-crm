package com.example.application.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * Mapeia o caminho de login, deve vir antes de todo a janela, por isso não usa
 * o MainLayout como pai
 */
@Route("login")
@PageTitle("Login | Vaadin CRM")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
  private final LoginForm login = new LoginForm();

  public LoginView() {
    addClassName("login-view");
    setSizeFull();
    // Centraliza os componentes na horizontal e vertical
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);

    // Configura a ação de login para fazer um POST request para a o Spring
    // Security
    login.setAction("login");

    add(new H1("Vaadin CRM"), login);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
    // Informa o usuário sobre o erro no login caso a tentativa de login falhe
    if (beforeEnterEvent.getLocation()
            .getQueryParameters()
            .getParameters()
            .containsKey("error")) {
      login.setError(true);
    }
  }
}
