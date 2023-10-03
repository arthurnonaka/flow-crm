package com.example.application.views.list;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ContactFormTest {
  private List<Company> companies;
  private List<Status> statuses;
  private Contact marcUsher;
  private Company company1;
  private Company company2;
  private Status status1;
  private Status status2;

  /**
   * Notação @BeforeEach adiciona dados fake para teste antes de cada teste
   */
  @BeforeEach
  public void setupData() {
    companies = new ArrayList<>();
    company1 = new Company();
    company1.setName("Vaadin Ltd");
    company2 = new Company();
    company2.setName("IT Mill");
    companies.add(company1);
    companies.add(company2);

    statuses = new ArrayList<>();
    status1 = new Status();
    status1.setName("Status 1");
    status2 = new Status();
    status2.setName("Status 2");
    statuses.add(status1);
    statuses.add(status2);

    marcUsher = new Contact();
    marcUsher.setFirstName("Marc");
    marcUsher.setLastName("Usher");
    marcUsher.setEmail("marc@usher.com");
    marcUsher.setStatus(status1);
    marcUsher.setCompany(company2);
  }

  @Test
  public void formFiedsPopulated() {
    ContactForm form = new ContactForm(companies, statuses);
    form.setContact(marcUsher);
    assertEquals("Marc", form.firstName.getValue());
    assertEquals("Usher", form.lastName.getValue());
    assertEquals("marc@usher.com", form.email.getValue());
    assertEquals(company2, form.company.getValue());
    assertEquals(status1, form.status.getValue());
  }

  @Test
  public void saveEventHasCorrectValues() {
    ContactForm form = new ContactForm(companies, statuses);
    Contact contact = new Contact();
    form.setContact(contact);
    form.firstName.setValue("John");
    form.lastName.setValue("Doe");
    form.company.setValue(company1);
    form.email.setValue("john@doe.com");
    form.status.setValue(status2);

    /**
     * O uso de AtomicReference permite a que o eventListener salve o contato de
     * forma Thread-Safe
     */
    AtomicReference<Contact> savedContactRef =
        new AtomicReference<Contact>(null);
    form.addSaveListener(e -> { savedContactRef.set(e.getContact()); });
    form.save.click();
    Contact savedContact = savedContactRef.get();

    assertEquals("John", savedContact.getFirstName());
    assertEquals("Doe", savedContact.getLastName());
    assertEquals("john@doe.com", savedContact.getEmail());
    assertEquals(company1, savedContact.getCompany());
    assertEquals(status2, savedContact.getStatus());
  }
}
