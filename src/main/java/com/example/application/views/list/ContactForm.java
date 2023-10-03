package com.example.application.views.list;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import java.util.List;

/**
 * Classe que constrói o formulário de contato, para input do usuário, e
 * realização de operações de adição, alteração ou deleção de contatos
 */
public class ContactForm extends FormLayout {
  TextField firstName = new TextField("First name");
  TextField lastName = new TextField("Last name");
  EmailField email = new EmailField("Email");
  ComboBox<Status> status = new ComboBox<>("Status");
  ComboBox<Company> company = new ComboBox<>("Company");
  BeanValidationBinder<Contact> binder =
      new BeanValidationBinder<>(Contact.class);

  Button save = new Button("Save");
  Button delete = new Button("Delete");
  Button close = new Button("Cancel");

  public ContactForm(List<Company> companies, List<Status> statuses) {
    // Dá ao componente um nome para formatar pelo CSS posteriormente
    addClassName("contact-form");
    // Combina os campos das classes Contact e ContactForm pelos nomes
    binder.bindInstanceFields(this);
    company.setItems(companies);
    company.setItemLabelGenerator(Company::getName);
    status.setItems(statuses);
    status.setItemLabelGenerator(Status::getName);

    // Adiciona todos os campos ao Layout, e permite configurar os botões
    add(firstName, lastName, email, company, status, createButtonsLayout());
  }

  /**
   * Amarra os campos de contact aos campos da interface do usuário e aos
   * atributos da classe
   * @param contact
   */
  public void setContact(Contact contact) { binder.setBean(contact); }

  /**
   * Configuração dos botões
   * @return
   */
  private Component createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    save.addClickShortcut(Key.ENTER);
    close.addClickShortcut(Key.ESCAPE);

    save.addClickListener(event -> validateAndSave());
    delete.addClickListener(
        event -> fireEvent(new DeleteEvent(this, binder.getBean())));
    close.addClickListener(event -> fireEvent(new CloseEvent(this)));

    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
    return new HorizontalLayout(save, delete, close);
  }

  private void validateAndSave() {
    if (binder.isValid()) {
      fireEvent(new SaveEvent(this, binder.getBean()));
    }
  }

  /**
   * Classe abstrata que organiza os eventos que irão acontecer a partir do
   * clique dos botões do ContactForm. Herda de uma classe que é um evento cuja
   * origem é um componente
   */
  public static abstract class ContactFormEvent
      extends ComponentEvent<ContactForm> {
    private Contact contact;

    protected ContactFormEvent(ContactForm source, Contact contact) {
      super(source, false);
      this.contact = contact;
    }

    public Contact getContact() { return contact; }
  }

  public static class SaveEvent extends ContactFormEvent {
    SaveEvent(ContactForm source, Contact contact) { super(source, contact); }
  }

  public static class DeleteEvent extends ContactFormEvent {
    DeleteEvent(ContactForm source, Contact contact) { super(source, contact); }
  }

  public static class CloseEvent extends ContactFormEvent {
    CloseEvent(ContactForm source) { super(source, null); }
  }

  public Registration
  addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
    return addListener(DeleteEvent.class, listener);
  }

  public Registration
  addSaveListener(ComponentEventListener<SaveEvent> listener) {
    return addListener(SaveEvent.class, listener);
  }
  public Registration
  addCloseListener(ComponentEventListener<CloseEvent> listener) {
    return addListener(CloseEvent.class, listener);
  }
}
