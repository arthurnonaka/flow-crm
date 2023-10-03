package com.example.application.data.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.annotations.Formula;

@Entity
public class Company extends AbstractEntity {
  @NotBlank private String name;

  @OneToMany(mappedBy = "company")
  @Nullable
  private List<Contact> employees = new LinkedList<>();

  public String getName() { return name; }

  public void setName(String name) { this.name = name; }

  public List<Contact> getEmployees() { return employees; }

  public void setEmployees(List<Contact> employees) {
    this.employees = employees;
  }

  /**
   * Formula é uma funcionalidade do Hibernate que permite especificar o comando
   * SQL para agrupar campos especiais. A query em questão obtem o número de
   * empregados sem precisar finalizar todos.
   */
  @Formula("(select count(c.id) from Contact c where c.company_id = id)")
  private int employeeCount;

  public int getEmployeesCount() { return employeeCount; }
}
