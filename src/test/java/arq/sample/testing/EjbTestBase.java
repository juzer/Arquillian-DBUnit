package arq.sample.testing;

import java.io.File;
import java.io.InputStream;

import org.dbunit.JdbcBasedDBTestCase;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import arq.sample.model.Department;
import arq.sample.model.Employee;

/**
 * Superclass for every test case using the test database. Performs the database
 * setup.
 * 
 * @author Piotr Nizio
 */
@RunWith(Arquillian.class)
@Run(RunModeType.IN_CONTAINER)
public abstract class EjbTestBase extends JdbcBasedDBTestCase {

    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/arq?user=arq&password=arq";
    private static final String DBUNIT_VERSION = "2.4.8";

    protected static JavaArchive getTestJar() {
	return ShrinkWrap.create(JavaArchive.class, "test.jar").
		addClasses(EjbTestBase.getRequiredClasses()).
		addResource(new File("src/test/resources/dataset.xml"),
			"dataset.xml").
		addResource(
			new File("src/main/resources/META-INF/persistence.xml"),
			"META-INF/persistence.xml").
		addResource(new File("src/main/resources/META-INF/ejb-jar.xml"),
			"META-INF/ejb-jar.xml");
    }

    protected static EnterpriseArchive getTestEar(JavaArchive testJar) {
	EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "test.ear").
		add(testJar, "/").
		addLibraries(ShrinkWrap.createFromZipFile(JavaArchive.class,
				ArtifactResolver.resolve("org.dbunit", "dbunit", DBUNIT_VERSION)));

	return ear;
    }

    /**
     * Returns an array of classes needed by each test case.
     * 
     * @return
     */
    public static Class<?>[] getRequiredClasses() {
	return new Class[] {
		// model
		Employee.class, Department.class,
		// testing
		EjbTestHelper.class, EjbTestBase.class };
    }

    @Override
    protected void setUpDatabaseConfig(DatabaseConfig config) {
	// some mysql specific stuff
	config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
		new MySqlDataTypeFactory());
	config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER,
		new MySqlMetadataHandler());
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
	super.setUp();
    }

    @After
    @Override
    public void tearDown() throws Exception {
	super.tearDown();
    }

    @Override
    protected String getConnectionUrl() {
	return CONNECTION_STRING;
    }

    @Override
    protected String getDriverClass() {
	return DRIVER_CLASS;
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
	// dataset file has to be in the root of test jar file
	InputStream is = EjbTestBase.class
		.getResourceAsStream("/dataset.xml");

	return new FlatXmlDataSetBuilder().build(is);
    }
}
