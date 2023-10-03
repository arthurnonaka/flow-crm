package com.example.application.views;

import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

/**
 * DashboardView está mapeado como dashboard e usa MainLayout como layout pai
 */
@PermitAll
@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Vaadin CRM")
public class DashboardView extends VerticalLayout {
  private final CrmService service;

  /**
   * Recebe CrmService como construtor e salva como atributo
   * @param service
   */
  public DashboardView(CrmService service) {
    this.service = service;
    addClassName("dashboard-view");
    setDefaultHorizontalComponentAlignment(
        Alignment.CENTER); // Centraliza o conteúdo na tela
    add(getContactStats(), getCompaniesChart());
  }

  private Component getContactStats() {
    /**
     * Chama o método do serviço para obter o número de contatos
     */
    Span stats = new Span(service.countContacts() + " contacts");
    stats.addClassNames(LumoUtility.FontSize.XLARGE,
                        LumoUtility.Margin.Top.MEDIUM);
    return stats;
  }

  /**
   * Chama o serviço para obter todas as empresas, cria um DataSeriesItem para
   * cada uma, contendo o nome da empresa e o número de empregados em cada
   */
  private Chart getCompaniesChart() {
    Chart chart = new Chart(ChartType.PIE);

    DataSeries dataSeries = new DataSeries();
    service.findAllCompanies().forEach(
        company
        -> dataSeries.add(new DataSeriesItem(company.getName(),
                                             company.getEmployeesCount())));
    chart.getConfiguration().setSeries(dataSeries);
    return chart;
  }
}
