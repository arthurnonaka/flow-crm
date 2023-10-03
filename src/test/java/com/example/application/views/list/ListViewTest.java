package com.example.application.views.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.application.data.entity.Contact;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Testes de integração
 * @SpringBootTest anotation certifica que a aplicação é inicializada antes dos
 * testes, e permite usar a notação @Autowired
 */
@SpringBootTest
public class ListViewTest {
  @Autowired private ListView listView;

  /**
   * Teste que verifica se inicialmente o formulário de contato está escondido,
   * e quando selecionado o primeiro item da lista, verifica se os dados
   * conferem com o dado de teste e também se o formulário é mostrado
   */
  @Test
  public void formShownWhenContactSelected() {
    Grid<Contact> grid = listView.grid;
    Contact firsContact = getFirstItem(grid);

    ContactForm form = listView.form;

    assertFalse(form.isVisible());
    grid.asSingleSelect().setValue(firsContact);
    assertTrue(form.isVisible());
    assertEquals(firsContact.getFirstName(), form.firstName.getValue());
  }

  /**
   * Função que retorna o primeiro item do grid
   * @param grid
   * @return Contact
   */
  private Contact getFirstItem(Grid<Contact> grid) {
    return ((ListDataProvider<Contact>)grid.getDataProvider())
        .getItems()
        .iterator()
        .next();
  }
}
