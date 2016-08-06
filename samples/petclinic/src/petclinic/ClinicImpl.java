/*
 * ClinicImpl.java
 *
 */

package petclinic;

import petclinic.dao.ClinicDAO;

import com.interface21.beans.factory.InitializingBean;
import com.interface21.context.ApplicationContextException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

/**
 *  Default implementation of the <code>Clinic</code> interface.
 *
 *  @author  Ken Krebs
 */
public class ClinicImpl implements Clinic, InitializingBean {
    
    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
    
    /** Holds value of property dao. */
    private ClinicDAO dao;
    
    /** <code>Map</code> of all <code>Vet</code>s. */
    private Map vetsMap;
    
    /** <code>Pet</code> types Identity <code>Map</code> . */
    private Map typesMap = new HashMap();
    
    /** <code>Map</code> of all the <code>Vet</code> specialties. */
    private Map specialtiesMap = new HashMap();
    
    /** <code>Owner</code> Identity <code>Map</code> */
    private Map ownersMap = new HashMap();
    
    /** <code>Pet</code> Identity <code>Map</code> */
    private Map petsMap = new HashMap();
    
    /** Creates a new instance of ClinicImpl */
    public ClinicImpl() {
    }
    
    /** Setter for property dao.
     * @param dao New value of property dao.
     */
    public void setDao(ClinicDAO dao) {
        this.dao = dao;
    }
    
    /**
     * @see InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        if(dao == null)
            throw new ApplicationContextException("Must set dao bean property on " + getClass());
        
        loadVets();
        loadTypes();
    }
    
    private Map mapEntityList(List list) {
        Map map = new HashMap();
        Iterator iterator = list.iterator();
        while(iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();
            map.put(new Integer(entity.getId()), entity);
        }
        return map;
    }
    
    
    // START of Clinic implementation *******************************
    
    public Map getVets() {
        return vetsMap;
    }
    
    public Map getTypes() {
        return typesMap;
    }
    
    public List findOwners(String lastName) {
        List owners = dao.findOwners(lastName);
        loadPets(owners);
        return owners;
    }
    
    public Owner findOwner(int id) {
        Owner owner = (Owner) ownersMap.get(new Integer(id));
        if(owner == null) {
            owner = dao.findOwner(id);
        }
        if(owner != null && owner.getPets().size() == 0) {
            loadPets(owner);
        }
        return owner;
    }
    
    public Pet findPet(int id) {
        Pet pet = (Pet) petsMap.get(new Integer(id));
        return pet;
    }
    
    public void insert(Owner owner) {
        dao.insert(owner);
        ownersMap.put(new Integer(owner.getId()), owner);
    }
    
    public void insert(Pet pet) {
        dao.insert(pet);
        pet.getOwner().addPet(pet);
        petsMap.put(new Integer(pet.getId()), pet);
    }
    
    public void insert(Visit visit) {
        dao.insert(visit);
        visit.getPet().addVisit(visit);
    }
    
    public void update(Owner owner) throws NoSuchEntityException {
        if(ownersMap.get(new Integer(owner.getId())) == null)
            throw new NoSuchEntityException(owner);
        
        dao.update(owner);
    }
    
    public void update(Pet pet) throws NoSuchEntityException {
        if(petsMap.get(new Integer(pet.getId())) == null)
            throw new NoSuchEntityException(pet);
        
        dao.update(pet);
    }
    
    // END of Clinic implementation *******************************
    
    /** Method establishes the map/list of all vets and their specialties. */
    void loadVets() {
        // establish the Map of all vets
        List vets = dao.getVets();
        vetsMap = mapEntityList(vets);
        
        // establish the map of all the possible specialties
        specialtiesMap = mapEntityList(dao.getSpecialties());
        
        // establish each vet's List of specialties
        Iterator vi = vets.iterator();
        while(vi.hasNext()) {
            Vet vet = (Vet) vi.next();
            List vetSpecialtiesIds = dao.findVetSpecialties(vet.getId());
            List vetSpecialties = new ArrayList();
            Iterator vsi = vetSpecialtiesIds.iterator();
            while(vsi.hasNext()) {
                Integer specialtyId = (Integer) vsi.next();
                NamedEntity specialty = (NamedEntity) specialtiesMap.get(specialtyId);
                vetSpecialties.add(specialty.getName());
            }
            if(vetSpecialties.size() == 0)
                vetSpecialties.add("none");
            vet.setSpecialties(vetSpecialties);
        }
        
    }
    
    /** Method establishes the map of all the possible pet types */
    void loadTypes() {
        typesMap = mapEntityList(dao.getTypes());
    }
    
    /**
     *  Method to retrieve the <code>Pet</code> and <code>Visit</code>
     *  data for an <code>Owner</code>s.
     *  @param owner
     */
    void loadPets(Owner owner) {
        List pets = dao.findPets(owner);
        Iterator pi = pets.iterator();
        while(pi.hasNext()) {
            Pet pet = (Pet) pi.next();
            pet.setOwner(owner);
            petsMap.put(new Integer(pet.getId()), pet);
            List visits = dao.findVisits(pet);
            Iterator vi = visits.iterator();
            while(vi.hasNext()) {
                Visit visit = (Visit) vi.next();
                visit.setPet(pet);
            }
            pet.setVisits(visits);
        }
        owner.setPets(pets);
        ownersMap.put(new Integer(owner.getId()), owner);
    }
    
    /**
     *  Method to retrieve the <code>Pet</code> and <code>Visit</code>
     *  data for a <code>List</code> of <code>Owner</code>s.
     *  @param owners <code>List</code>.
     *  @see #loadPets(Owner)
     */
    void loadPets(List owners) {
        Iterator oi = owners.iterator();
        while(oi.hasNext()) {
            Owner owner = (Owner) oi.next();
            loadPets(owner);
        }
    }
    
}