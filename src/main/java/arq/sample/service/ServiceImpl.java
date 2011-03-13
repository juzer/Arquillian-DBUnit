package arq.sample.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import arq.sample.model.Department;
import arq.sample.model.Employee;

@Stateless
public class ServiceImpl implements Service {

    @PersistenceContext(unitName = "arq.sample.PU")
    private EntityManager em;

    @Override
    public Department findDept(int id) {
	return em.find(Department.class, id);
    }

    @Override
    public void addEmployee(Employee employee, Department department) {
	employee.setDepartment(department);
	department.getEmployees().add(employee);
	em.merge(department);
    }
}
