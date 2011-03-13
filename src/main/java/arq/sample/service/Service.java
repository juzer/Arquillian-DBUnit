package arq.sample.service;

import javax.ejb.Local;

import arq.sample.model.Department;
import arq.sample.model.Employee;

@Local
public interface Service {

    Department findDept(int id);

    void addEmployee(Employee employee, Department department);

}
