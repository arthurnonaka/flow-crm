package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.util.Collections;
import org.springframework.context.annotation.Scope;

/**
 * ListView usa MainLayout como classe pai
 */
@org.springframework.stereotype.Component
@Scope("prototype")
@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Contacts | Vaadin CRM")
public class ListView extends VerticalLayout {
  Grid<Contact> grid = new Grid<>(Contact.class);
  TextField filterText = new TextField();
  ContactForm form;
  CrmService service;

  public ListView(CrmService service) {
    this.service = service;
    addClassName("list-view");
    setSizeFull();
    configureGrid();
    configureForm();

    add(getToolbar(), getContent());
    updateList();
    closeEditor();
  }

  private void configureGrid() {
    grid.addClassNames("contact-grid");
    grid.setSizeFull();
    grid.setColumns("firstName", "lastName", "email");
    grid.addColumn(contact -> contact.getStatus().getName())
        .setHeader("Status");
    grid.addColumn(contact -> contact.getCompany().getName())
        .setHeader("Company");
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
    /**
     * Adiciona um listener ao grid
     */
    grid.asSingleSelect().addValueChangeListener(
        event -> editContact(event.getValue()));
  }

  private HorizontalLayout getToolbar() {
    filterText.setPlaceholder("Filter by name...");
    filterText.setClearButtonVisible(true);
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    // Chama updateList todas as vezes que o conteúdo do filtro mudar
    filterText.addValueChangeListener(e -> updateList());

    Button addContactButton = new Button("Add contact");
    /**
     * Quando o usuário clicar em "Add contact" chama o método addContact()
     */
    addContactButton.addClickListener(click -> addContact());

    var toolbar = new HorizontalLayout(filterText, addContactButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  /**
   * Método que retorna um HorizontalLayout, que engloba o formulário e o grid
   * @return content
   */
  private Component getContent() {
    HorizontalLayout content = new HorizontalLayout(grid, form);
    content.setFlexGrow(2, grid); // Especifica que o grid deve ter o dobro de
                                  // espaço que o formulário
    content.setFlexGrow(1, form);
    content.addClassNames("content");
    content.setSizeFull();
    return content;
  }

  /**
   * Método para inicializar o formulário
   */
  private void configureForm() {
    /**
     * Inicializa o formulário com empresas e status do serviço
     */
    form =
        new ContactForm(service.findAllCompanies(), service.findAllStatuses());
    form.setWidth("25em");
    form.addSaveListener(this::saveContact);
    form.addDeleteListener(this::deleteContact);
    form.addCloseListener(e -> closeEditor()); // Fecha o formulário
  }

  /**
   * Utiliza o serviço para salvar o contato do evento no BD, atualiza a lista e
   * fecha o formulário
   * @param event
   */
  private void saveContact(ContactForm.SaveEvent event) {
    service.saveContact(event.getContact());
    updateList();
    closeEditor();
  }

  /**
   * Usa o serviço para deletar o contato do BD, atualiza a lista e fecha o
   * formulário
   * @param event
   */
  private void deleteContact(ContactForm.DeleteEvent event) {
    service.deleteContact(event.getContact());
    updateList();
    closeEditor();
  }

  /**
   * Seta os itens do grid chamando o serviço com o conteúdo do filtro
   */
  private void updateList() {
    grid.setItems(service.findAllContacts(filterText.getValue()));
  }

  /**
   * Seta o contato selecionado e esconde ou mostra o formulário, dependendo da
   * seleção
   * @param contact
   */
  public void editContact(Contact contact) {
    if (contact == null) {
      closeEditor();
    } else {
      form.setContact(contact);
      form.setVisible(true);
      addClassName("editing");
    }
  }

  /**
   * Seta o formulário como null, limpa valores antigos, esconde o formulário e
   * remove o "editing" do CSS
   */
  private void closeEditor() {
    form.setContact(null);
    form.setVisible(false);
    removeClassName("editing");
  }

  /**
   * Limpa a seleção do grid e cria um novo contato
   */
  private void addContact() {
    grid.asSingleSelect().clear();
    editContact(new Contact());
  }
}