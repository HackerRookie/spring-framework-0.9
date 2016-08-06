/*
 * ClinicImplTest.java
 * JUnit based test
 *
 */

package petclinic;

import junit.framework.*;

import petclinic.dao.ClinicDAO;
import petclinic.Owner;
import petclinic.Pet;
import petclinic.Visit;

import com.interface21.context.support.ClassPathXmlApplicationContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import javax.sql.DataSource;

/**
 *  Live Unit tests for ClinicImpl
 *
 *  @author Ken Krebs
 */
public class ClinicImplTest extends TestCase {
    
	/** Logger for this class */
	private final Log logger = LogFactory.getLog(getClass());
    
    ClinicImpl clinic;
    
    public ClinicImplTest(java.lang.String testName) throws java.io.IOException {
        super(testName);
        
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext.xml");
        DataSource ds = (DataSource) ctx.getBean("dataSource");
        ClinicDAO dao = (ClinicDAO) ctx.getBean("clinicDAO");
        clinic = (ClinicImpl) ctx.getBean("clinic");
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    /** Test of findOwners method, of class ClinicImpl. */
    public void testFindOwners() {
        System.out.println("testFindOwners");
        
        List owners = clinic.findOwners("Davis");
        assertEquals(2, owners.size());
        owners = clinic.findOwners("s");
        assertEquals(0, owners.size());
    }
    
    /** Test of findOwner method, of class ClinicImpl. */
    public void testFindOwner() {
        System.out.println("testGetOwner");
        
        Owner o1 = clinic.findOwner(1);
        assertEquals("Franklin", o1.getLastName());
        Owner o10 = clinic.findOwner(10);
        assertEquals("Carlos", o10.getFirstName());
    }
    
    /** Test of getVets method, of class ClinicImpl. */
    public void testGetVets() {
        System.out.println("testGetVets");
        
        assertEquals(6, clinic.getVets().size());
    }
    
    /** Test of findPet method, of class ClinicImpl. */
    public void testFindPet() {
        System.out.println("testFindPet");
        
        Owner o6 = clinic.findOwner(6);
        Pet p7 = clinic.findPet(7);
        assertEquals("Samantha", p7.getName());
        Pet p8 = clinic.findPet(8);
        assertEquals("Max", p8.getName());
    }
    
    /** Test of insert(Owner) method, of class ClinicImpl. */
    public void testInsertOwner() {
        System.out.println("testInsertOwner");
        
        Owner owner = new Owner();
        owner.setLastName("Schultz");
        List owners = clinic.findOwners("Schultz");
        assertEquals(0, owners.size());
        clinic.insert(owner);
        owners = clinic.findOwners("Schultz");
        assertEquals(1, owners.size());
    }
    
    /** Test of insert(Pet) method, of class ClinicImpl. */
    public void testInsertPet() {
        System.out.println("testInsertPet");
        
        Owner o6 = clinic.findOwner(6);
        Pet pet = new Pet();
        pet.setName("bowser");
        pet.setOwner(o6);
        pet.setTypeId(2);
        pet.setBirthDate(new java.util.Date());
        assertEquals(2, o6.getPets().size());
        clinic.insert(pet);
        assertEquals(3, o6.getPets().size());
    }
    
    /** Test of insert(Visit) method, of class ClinicImpl. */
    public void testInsertVisit() {
        System.out.println("testInsertVisit");
        
        Owner o6 = clinic.findOwner(6);
        Pet p7 = clinic.findPet(7);
        List visits = p7.getVisits();
        assertEquals(2, visits.size());
        Visit visit = new Visit();
        visit.setPetId(7);
        visit.setPet(p7);
        visit.setDescription("test");
        clinic.insert(visit);
        assertTrue(visit.getId() > 0);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ClinicImplTest.class);
        return suite;
    }
    
}
