/*
 * ClinicDAO.java
 *
 * Created on May 27, 2003, 10:48 AM
 */

package petclinic.dao;

import petclinic.Owner;
import petclinic.Pet;
import petclinic.Visit;

import com.interface21.dao.DataAccessException;

import java.util.List;

/**
 *  The high-level petclinic Data Access Object interface.
 *
 *  @author  Ken Krebs
 */
public interface ClinicDAO {
    
    /** Method to retrieve all <code>Vet</code>s from the datastore.
     *  @return a <code>List</code> of <code>Vet</code>s.
     */
    public List getVets() throws DataAccessException;
    
    /** Method to retrieve all <code>Vet</code> specialties from the datastore.
     *  @return a <code>List</code> of specialties.
     */
    public List getSpecialties() throws DataAccessException;
    
    /** Method to retrieve all <code>Pet</code> types from the datastore.
     *  @return a <code>List</code> of types.
     */
    public List getTypes() throws DataAccessException;
    
    /** Method to retrieve <code>Owner</code>s from the datastore by last name.
     *  @param lastName Value to search for.
     *  @return a <code>List</code> of matching <code>Owner</code>s.
     */
    public List findOwners(String lastName) throws DataAccessException;
    
    /** Method to retrieve an <code>Owner</code> from the datastore by id.
     *  @param id Value to search for.
     *  @return the <code>Owner</code> if found.
     */
    public Owner findOwner(int id) throws DataAccessException;
    
    /** Method to retrieve <code>Pet</code>s from the datastore belonging to
     *  a particular <code>Owner</code>.
     *  @param owner of the <code>Pet</code>s.
     *  @return a <code>List</code> of <code>Pet</code>s.
     */
    public List findPets(Owner owner) throws DataAccessException;
    
    /** Method to retrieve <code>Visit</code>s from the datastore for
     *  a particular <code>Pet</code>.
     *  @param pet making the <code>Visit</code>s.
     *  @return a <code>List</code> of <code>Visit</code>s.
     */
    public List findVisits(Pet pet) throws DataAccessException;
    
    /** Method to retrieve all specialties from the datastore for
     *  a particular <code>Vet</code>.
     *  @param vet
     *  @return a <code>List</code> of specialties.
     */
    public List findVetSpecialties(int vetId) throws DataAccessException;
    
    /** Method to add a new <code>Owner</code> to the datastore. 
     *  @param owner to add.
     */
    public void insert(Owner owner) throws DataAccessException;
    
    /** Method to add a new <code>Pet</code> to the datastore. 
     *  @param pet to add.
     */
    public void insert(Pet pet) throws DataAccessException;
    
    /** Method to add a new <code>Visit</code> to the datastore. 
     *  @param visit to add.
     */
    public void insert(Visit visit) throws DataAccessException;
    
    /** Method to update the datastore with an <code>Owner</code>'s 
     *  revised information.
     *  @param owner to update.
     */
    public void update(Owner owner) throws DataAccessException;
    
    /** Method to update the datastore with a <code>Pet</code>'s 
     *  revised information.
     *  @param pet to update.
     */
    public void update(Pet pet) throws DataAccessException;
    
}
