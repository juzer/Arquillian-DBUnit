package arq.sample.testing;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Helper class for resolving bean JNDI names in JBoss container. During lookup
 * it tries the following patterns:
 * <ul>
 * <li>test/BeanClassName/remote</li>
 * <li>test/BeanClassName/local</li>
 * <li>BeanClassName/remote</li>
 * <li>BeanClassName/local</li>
 * </ul>
 * It also contains methods for beginning and finishing transactions within
 * application.
 * 
 * @author Piotr Nizio
 */
public class EjbTestHelper {

    /**
     * Brings a reference to an EJB component with given interface and
     * implementation.
     * 
     * @param <T>
     *            business interface type
     * @param beanClass
     *            implementation class
     * @param ejbInterface
     *            business interface class
     * @return reference to the component or null if not found
     * @throws NamingException
     *             if failed to create JNDI context
     */
    @SuppressWarnings("unchecked")
    public static <T> T getEjbInstance(Class<?> beanClass, Class<T> ejbInterface)
	    throws NamingException {
	InitialContext ctx = new InitialContext();
	String className = beanClass.getSimpleName();
	String interfaceName = ejbInterface.getSimpleName();
	String[] beanNames = {
		"test/" + className + "/remote",
		"test/" + className + "/local",
		className + "/local",
		className + "/remote",
		"test/" + interfaceName + "/local", // for ConfigService
		"mVoucher-service-ear/" + interfaceName + "/remote"
		// for facades with preset names
		};

	for (String nextTry : beanNames) {
	    try {
		T bean = (T) ctx.lookup(nextTry);
		if (bean != null) {
		    System.out.println("Found bean: " + nextTry);
		    return bean;
		}
	    } catch (NamingException exc) {
		// do nothing
		// System.err.println(exc.toString());
	    }
	}

	return null; // not found
    }

}
