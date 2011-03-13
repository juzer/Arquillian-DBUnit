package arq.sample.service;

import javax.naming.NamingException;

import org.jboss.arquillian.api.Deployment;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

import arq.sample.model.Department;
import arq.sample.model.Employee;
import arq.sample.testing.EjbTestBase;
import arq.sample.testing.EjbTestHelper;

public class ServiceImplTest extends EjbTestBase {

    @Deployment
    public static EnterpriseArchive deployment() {
	JavaArchive jar = EjbTestBase.getTestJar();
	// add classes needed by this test case to the jar file(including the
	// junit test class itself)
	jar.addClasses(Service.class, ServiceImpl.class, ServiceImplTest.class);

	// add jar file to the ear that will be deployed to the server
	EnterpriseArchive ear = EjbTestBase.getTestEar(jar);

	System.out.println(jar.toString(true));
	System.out.println(ear.toString(true));
	return ear;
    }

    @Test
    public void testFindDept() throws NamingException {
	Service service = EjbTestHelper.getEjbInstance(ServiceImpl.class, Service.class);
	assertNotNull(service);
	Department dept = service.findDept(1);
	assertNotNull(dept);
	
	assertNotNull(dept.getEmployees());
	assertEquals(2, dept.getEmployees().size());
    }
    
    @Test
    public void testAddEmployee() throws NamingException {
	Service service = EjbTestHelper.getEjbInstance(ServiceImpl.class, Service.class);
	
	assertNotNull(service);
	
	Department dept = new Department();
	dept.setName("New dept");
	
	Employee emp = new Employee();
	emp.setFirstName("New");
	emp.setLastName("Employee");
	
	service.addEmployee(emp, dept);
    }
}
