package com.example.application.views;

import com.example.application.security.SecurityService;
import com.example.application.views.list.ListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * AppLayout é um layout do Vaadin com cabeçalho e menu lateral responsivo, com
 * possibilidade de minimizar ou expandir
 */
public class MainLayout extends AppLayout {
  private final SecurityService securityService;
  public MainLayout(SecurityService securityService) {
    this.securityService = securityService;
    createHeader();
    createDrawer();
  }

  private void createHeader() {
    H1 logo = new H1("Vaadin CRM");
    logo.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.MEDIUM);

    String u = securityService.getAuthenticatedUser().getUsername();
    Button logout = new Button("Log out " + u, e -> securityService.logout());
    /**
     * DrawerToggle() é um botão do menu que ativa/desativa a visibilidade
     * da barra lateral
     */
    var header = new HorizontalLayout(new DrawerToggle(), logo, logout);

    /**
     * Centraliza os componentes do cabeçalho (header) no eixo vertical
     */
    header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    header.expand(logo); // Faz a logo expandir e pegar todo o espaço restante
    header.setWidthFull();
    header.addClassNames(LumoUtility.Padding.Vertical.NONE,
                         LumoUtility.Padding.Horizontal.MEDIUM);

    /**
     * Adiciona o header na navegação do app
     */
    addToNavbar(header);
  }

  /**
   * Envelopa os links possíveis em um VerticalLayout, e adiciona ao menu
   * lateral. Cria um link com o texto "List" e adiciona a classe ListView como
   * destino
   */
  private void createDrawer() {
    addToDrawer(
        new VerticalLayout(new RouterLink("List", ListView.class),
                           new RouterLink("Dashboard", DashboardView.class)));
  }
}
