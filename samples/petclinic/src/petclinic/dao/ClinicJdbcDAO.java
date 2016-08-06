/*
 * ClinicJdbcDAO.java
 *
 */

package petclinic.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import petclinic.NamedEntity;
import petclinic.Owner;
import petclinic.Pet;
import petclinic.Vet;
import petclinic.Visit;

import com.interface21.beans.factory.InitializingBean;
import com.interface21.context.ApplicationContext;
import com.interface21.context.ApplicationContextAware;
import com.interface21.context.ApplicationContextException;
import com.interface21.dao.DataAccessException;
import com.interface21.jdbc.core.SqlParameter;
import com.interface21.jdbc.core.support.AbstractDataFieldMaxValueIncrementer;
import com.interface21.jdbc.object.MappingSqlQuery;
import com.interface21.jdbc.object.RdbmsOperation;
import com.interface21.jdbc.object.SqlUpdate;

/**
 *  Default JDBC implementation of ClinicDAO.
 *
 *  @author  Ken Krebs
 */
public class ClinicJdbcDAO implements ClinicDAO, InitializingBean, ApplicationContextAware {
    
    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
    
    /** Holds value of property dataSource. */
    private DataSource dataSource;
    
    /** Holds value of property applicationContext. */
    private ApplicationContext applicationContext;
    
    /** Creates a new instance of ClinicJdbcDAO */
    public ClinicJdbcDAO() {
    }
    
    /** Holds all vets Query Object. */
    private RdbmsOperation allVetsQuery;
    
    /** Holds pet types Query Object. */
    private RdbmsOperation typesQuery;
    
    /** Holds specialties Query Object. */
    private RdbmsOperation specialtiesQuery;
    
    /** Holds owners by name Query Object. */
    private RdbmsOperation ownersByNameQuery;
    
    /** Holds owner by id Query Object. */
    private RdbmsOperation ownerQuery;
    
    /** Holds pets by owner Query Object. */
    private RdbmsOperation petsByOwnerQuery;
    
    /** Holds visits Query Object. */
    private RdbmsOperation visitsQuery;
    
    /** Holds vet specialties Query Object. */
    private RdbmsOperation vetSpecialtiesQuery;
    
    /** Holds owner Insert Object. */
    private RdbmsOperation ownerInsert;
    
    /** Holds pet Insert Object. */
    private RdbmsOperation petInsert;
    
    /** Holds Visit Insert Object. */
    private RdbmsOperation visitInsert;
    
    /** Holds owner Update Object. */
    private RdbmsOperation ownerUpdate;
    
    /** Holds pet Update Object. */
    private RdbmsOperation petUpdate;
    
    /** Setter for property dataSource.
     * @param dataSource New value of property dataSource.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public void setApplicationContext(ApplicationContext ctx) throws ApplicationContextException {
        applicationContext = ctx;
        
        // make sure incrementers are set
        ((OwnerInsert) ownerInsert).incrementer = getIncrementer("owners_seq");
        ((PetInsert) petInsert).incrementer = getIncrementer("pets_seq");
        ((VisitInsert) visitInsert).incrementer = getIncrementer("visits_seq");
    }
    
    /**
     *  Method to get an incrementer for a particular table/sequence
     *  @param incrementerName name of the table/sequence incrementer
     *  @return the incrementer
     **/
    protected AbstractDataFieldMaxValueIncrementer getIncrementer(String incrementerName) {
        AbstractDataFieldMaxValueIncrementer incrementer = (AbstractDataFieldMaxValueIncrementer) applicationContext.getBean("incrementer");
        incrementer.setIncrementerName(incrementerName);
        return incrementer;
    }
    
    /**
     *  Subclasses can supply customized versions of the RdbmsOperations
     *  by overriding this method and the corresponding ClinicDAO methods.
     *  If only replacing specific RdbmsOperations, the subclass should call this method first
     *  and then replace only the desired RdbmsOperations. It may also be necessary to 
     *  override setApplicationContext for Insert objects.
     *  @see #setApplicationContext
     */ 
    public void afterPropertiesSet() throws Exception {
        if(dataSource == null) {
            throw new ApplicationContextException("Must set dataSource bean property on " + getClass());
        }
        
        allVetsQuery = new AllVetsQuery(dataSource);
        typesQuery = new TypesQuery(dataSource);
        specialtiesQuery = new SpecialtiesQuery(dataSource);
        ownersByNameQuery = new OwnersByNameQuery(dataSource);
        ownerQuery = new OwnerQuery(dataSource);
        petsByOwnerQuery = new PetsByOwnerQuery(dataSource);
        visitsQuery = new VisitsQuery(dataSource);
        vetSpecialtiesQuery = new VetSpecialtiesQuery(dataSource);
        ownerInsert = new OwnerInsert(dataSource);
        petInsert = new PetInsert(dataSource);
        visitInsert = new VisitInsert(dataSource);
        ownerUpdate = new OwnerUpdate(dataSource);
        petUpdate = new PetUpdate(dataSource);
    }
    
    
    // START of ClinicDAO implementation *******************************
    
    public List getVets() throws DataAccessException {
        return ((AllVetsQuery) allVetsQuery).execute();
    }
    
    public List getTypes() throws DataAccessException {
        return ((TypesQuery) typesQuery).execute();
    }
    
    public List getSpecialties() throws DataAccessException {
        return ((SpecialtiesQuery) specialtiesQuery).execute();
    }
    
    public List findOwners(String lastName) throws DataAccessException {
        return ((OwnersByNameQuery) ownersByNameQuery).execute(lastName);
    }
    
    public Owner findOwner(int id) throws DataAccessException {
        return (Owner) ((OwnerQuery) ownerQuery).findObject(id);
    }
    
    public List findPets(Owner owner) throws DataAccessException {
        return ((PetsByOwnerQuery) petsByOwnerQuery).execute(owner.getId());
    }
    
    public List findVisits(Pet pet) throws DataAccessException {
        return ((VisitsQuery) visitsQuery).execute(pet.getId());
    }
    
    public List findVetSpecialties(int vetId) throws DataAccessException {
        return ((VetSpecialtiesQuery) vetSpecialtiesQuery).execute(vetId);
    }
    
    public void insert(Owner owner) throws DataAccessException {
        ((OwnerInsert) ownerInsert).insert(owner);
    }
    
    public void insert(Pet pet) throws DataAccessException {
        ((PetInsert) petInsert).insert(pet);
    }
    
    public void insert(Visit visit) throws DataAccessException {
        ((VisitInsert) visitInsert).insert(visit);
    }
    
    public void update(Owner owner) throws DataAccessException {
        ((OwnerUpdate) ownerUpdate).update(owner);
    }
    
    public void update(Pet pet) throws DataAccessException {
        ((PetUpdate) petUpdate).update(pet);
    }
    
    // END of ClinicDAO implementation *******************************
    
    
    /**
     *  Abstract base class for all <code>Vet</code> Query Objects.
     */
    abstract class VetsQuery extends MappingSqlQuery  {
        
        /** 
         *  Creates a new instance of VetsQuery
         *  @param ds the DataSource to use for the query.
         *  @param sql Value of the SQL to use for the query. 
         */
        protected VetsQuery(DataSource ds, String sql) {
            super(ds, sql);
        }
        
        /** @see MappingSqlQuery#mapRow(ResultSet,int)*/
        protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
            Vet vet = new Vet();
            vet.setId(rs.getInt("id"));
            vet.setFirstName(rs.getString("first_name"));
            vet.setLastName(rs.getString("last_name"));
            return vet;
        }
        
    }
    
    /**
     *  All <code>Vet</code>s Query Object.
     */
    class AllVetsQuery extends VetsQuery {
        
        /** 
         *  Creates a new instance of AllVetsQuery
         *  @param ds the DataSource to use for the query.
         */
        protected AllVetsQuery(DataSource ds) {
            super(ds, "SELECT id,first_name,last_name FROM vets ORDER BY last_name,first_name");
            compile();
        }
        
    }
    
    /**
     *  All <code>Vet</code>s specialties Query Object.
     */
    class SpecialtiesQuery extends MappingSqlQuery  {
        
        /** 
         *  Creates a new instance of SpecialtiesQuery
         *  @param ds the DataSource to use for the query.
         */
        protected SpecialtiesQuery(DataSource ds) {
            super(ds, "SELECT id,name FROM specialties");
            compile();
        }
        
        /** @see MappingSqlQuery#mapRow(ResultSet,int)*/
        protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
            NamedEntity specialty = new NamedEntity();
            specialty.setId(rs.getInt("id"));
            specialty.setName(rs.getString("name"));
            return specialty;
        }
        
    }
    
    /**
     *  A particular <code>Vet</code>'s specialties Query Object.
     */
    class VetSpecialtiesQuery extends MappingSqlQuery  {
        
        /** 
         *  Creates a new instance of VetSpecialtiesQuery
         *  @param ds the DataSource to use for the query.
         */
        protected VetSpecialtiesQuery(DataSource ds) {
            super(ds, "SELECT specialty_id FROM vet_specialties WHERE vet_id=?");
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }
        
        /** @see MappingSqlQuery#mapRow(ResultSet,int)*/
        protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
            return new Integer(rs.getInt("specialty_id"));
        }
        
    }
    
    /**
     *  All <code>Pet</code>s types Query Object.
     */
    class TypesQuery extends MappingSqlQuery  {
        
        /** 
         *  Creates a new instance of TypesQuery
         *  @param ds the DataSource to use for the query.
         */
        protected TypesQuery(DataSource ds) {
            super(ds, "SELECT id,name FROM types ORDER BY name");
            compile();
        }
        
        /** @see MappingSqlQuery#mapRow(ResultSet,int)*/
        protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
            NamedEntity type = new NamedEntity();
            type.setId(rs.getInt("id"));
            type.setName(rs.getString("name"));
            return type;
        }
        
    }
    
    /**
     *  Abstract base class for all <code>Owner</code> Query Objects.
     */
    abstract class OwnersQuery extends MappingSqlQuery  {
        
        /** 
         *  Creates a new instance of OwnersQuery
         *  @param ds the DataSource to use for the query.
         *  @param sql Value of the SQL to use for the query. 
         */
        protected OwnersQuery(DataSource ds, String sql) {
            super(ds, sql);
        }
        
        /** @see MappingSqlQuery#mapRow(ResultSet,int)*/
        protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
            Owner owner = new Owner();
            owner.setId(rs.getInt("id"));
            owner.setFirstName(rs.getString("first_name"));
            owner.setLastName(rs.getString("last_name"));
            owner.setAddress(rs.getString("address"));
            owner.setCity(rs.getString("city"));
            owner.setTelephone(rs.getString("telephone"));
            return owner;
        }
        
    }
    
    /**
     *  <code>Owner</code>s by name Query Object.
     */
    class OwnersByNameQuery extends OwnersQuery {
        
        /** 
         *  Creates a new instance of OwnersByNameQuery
         *  @param ds the DataSource to use for the query.
         */
        protected OwnersByNameQuery(DataSource ds) {
            super(ds, "SELECT id,first_name,last_name,address,city,telephone FROM owners WHERE last_name=? ORDER BY first_name");
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }
        
    }
    
    /**
     *  <code>Owner</code> by id Query Object.
     */
    class OwnerQuery extends OwnersQuery {
        
        /** 
         *  Creates a new instance of OwnerQuery
         *  @param ds the DataSource to use for the query.
         */
        protected OwnerQuery(DataSource ds) {
            super(ds, "SELECT id,first_name,last_name,address,city,telephone FROM owners WHERE id=?");
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }
        
    }
    
    /**
     *  <code>Owner</code> Update Object.
     */
    class OwnerUpdate extends SqlUpdate {
        
        /** 
         *  Creates a new instance of OwnerUpdate
         *  @param ds the DataSource to use for the update.
         */
        protected OwnerUpdate(DataSource ds) {
            super(ds, "UPDATE owners SET first_name=?,last_name=?,address=?,city=?,telephone=? WHERE id=?");
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }
        
        /**
         *  Method to update an <code>Owner</code>'s data.
         *  @param owner to update.
         *  @return the number of rows affected by the update
         */
        protected int update(Owner owner) {
            return this.update(new Object[] {
                owner.getFirstName(),
                owner.getLastName(),
                owner.getAddress(),
                owner.getCity(),
                owner.getTelephone(),
                new Integer(owner.getId())
            });
        }
        
    }
    
    /**
     *  <code>Owner</code> Insert Object.
     */
    class OwnerInsert extends SqlUpdate {
        
        /** Holds key generator Object */
        AbstractDataFieldMaxValueIncrementer incrementer;

        /** 
         *  Creates a new instance of OwnerInsert
         *  @param ds the DataSource to use for the insert.
         */
        protected OwnerInsert(DataSource ds) {
            super(ds, "INSERT INTO owners VALUES(?,?,?,?,?,?)");
            declareParameter(new SqlParameter(Types.INTEGER));
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }
        
        /**
         *  Method to insert a new <code>Owner</code>.
         *  @param owner to insert.
         */
        protected void insert(Owner owner) {
            owner.setId(incrementer.nextIntValue());
            Object[] objs = new Object[] {
                new Integer(owner.getId()),
                owner.getFirstName(),
                owner.getLastName(),
                owner.getAddress(),
                owner.getCity(),
                owner.getTelephone()
            };
            this.update(objs);
        }
        
    }
    
    /**
     *  Abstract base class for all <code>Pet</code> Query Objects.
     */
    abstract class PetsQuery extends MappingSqlQuery  {
        
        /** 
         *  Creates a new instance of PetsQuery
         *  @param ds the DataSource to use for the query.
         *  @param sql Value of the SQL to use for the query. 
         */
        protected PetsQuery(DataSource ds, String sql) {
            super(ds, sql);
        }
        
        /** @see MappingSqlQuery#mapRow(ResultSet,int)*/
        protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
            Pet pet = new Pet();
            pet.setId(rs.getInt("id"));
            pet.setName(rs.getString("name"));
            pet.setBirthDate(rs.getDate("birth_date"));
            pet.setTypeId(rs.getInt("type_id"));
            return pet;
        }
        
    }
    
    /**
     *  <code>Pet</code> by id Query Object.
     */
    class PetQuery extends PetsQuery {
        
        /** 
         *  Creates a new instance of PetQuery
         *  @param ds the DataSource to use for the query.
         */
        protected PetQuery(DataSource ds) {
            super(ds, "SELECT id,name,birth_date,type_id FROM pets WHERE id=?");
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }
        
    }
    
    /**
     *  <code>Pet</code>s by <code>Owner</code> Query Object.
     */
    class PetsByOwnerQuery extends PetsQuery {
        
        /** 
         *  Creates a new instance of PetsByOwnerQuery
         *  @param ds the DataSource to use for the query.
         */
        protected PetsByOwnerQuery(DataSource ds) {
            super(ds, "SELECT id,name,birth_date,type_id,owner_id FROM pets WHERE owner_id=? ORDER BY name");
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }
        
    }
    
    /**
     *  <code>Pet</code> Update Object.
     */
    class PetUpdate extends SqlUpdate {
        
        /** 
         *  Creates a new instance of PetUpdate
         *  @param ds the DataSource to use for the update.
         */
        protected PetUpdate(DataSource ds) {
            super(ds, "UPDATE pets SET name=?,birth_date=?,type_id=?,owner_id=? WHERE id=?");
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.DATE));
            declareParameter(new SqlParameter(Types.INTEGER));
            declareParameter(new SqlParameter(Types.INTEGER));
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }
        
        /**
         *  Method to update an <code>Pet</code>'s data.
         *  @param pet to update.
         *  @return the number of rows affected by the update
         */
        protected int update(Pet pet) {
            return this.update(new Object[] {
                pet.getName(),
                new java.sql.Date(pet.getBirthDate().getTime()),
                new Integer(pet.getTypeId()),
                new Integer(pet.getOwner().getId()),
                new Integer(pet.getId())
            });
        }
        
    }
    
    /**
     *  <code>Pet</code> Insert Object.
     */
    class PetInsert extends SqlUpdate {
        
        /** Hold skey generator Object */
        AbstractDataFieldMaxValueIncrementer incrementer;

        /** 
         *  Creates a new instance of PetInsert
         *  @param ds the DataSource to use for the insert.
         */
        protected PetInsert(DataSource ds) {
            super(ds, "INSERT INTO pets VALUES(?,?,?,?,?)");
            declareParameter(new SqlParameter(Types.INTEGER));
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.DATE));
            declareParameter(new SqlParameter(Types.INTEGER));
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }
        
        /**
         *  Method to insert a new <code>Pet</code>.
         *  @param pet to insert.
         */
        protected void insert(Pet pet) {
            pet.setId(incrementer.nextIntValue());
            Object[] objs = new Object[] {
                new Integer(pet.getId()),
                pet.getName(),
                new java.sql.Date(pet.getBirthDate().getTime()),
                new Integer(pet.getTypeId()),
                new Integer(pet.getOwner().getId()),
            };
            this.update(objs);
        }
        
    }
    
    /**
     *  <code>Visit</code>s by <code>Pet</code> Query Object.
     */
    class VisitsQuery extends MappingSqlQuery  {
        
        /** 
         *  Creates a new instance of VisitsQuery
         *  @param ds the DataSource to use for the update.
         */
        protected VisitsQuery(DataSource ds) {
            super(ds, "SELECT id,pet_id,visit_date,description FROM visits WHERE pet_id=? ORDER BY visit_date");
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }
        
        /** @see MappingSqlQuery#mapRow(ResultSet,int)*/
        protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
            Visit visit = new Visit();
            visit.setId(rs.getInt("id"));
            visit.setPetId(rs.getInt("pet_id"));
            visit.setVisitDate(rs.getDate("visit_date"));
            visit.setDescription(rs.getString("description"));
            return visit;
        }
        
    }
    
    /**
     *  <code>Visit</code> Insert Object.
     */
    class VisitInsert extends SqlUpdate {
        
        /** Holds key generator Object */
        AbstractDataFieldMaxValueIncrementer incrementer;

        /** 
         *  Creates a new instance of VisitInsert
         *  @param ds the DataSource to use for the insert.
         */
        protected VisitInsert(DataSource ds) {
            super(ds, "INSERT INTO visits VALUES(?,?,?,?)");
            declareParameter(new SqlParameter(Types.INTEGER));
            declareParameter(new SqlParameter(Types.INTEGER));
            declareParameter(new SqlParameter(Types.DATE));
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }
        
        /**
         *  Method to insert a new <code>Visit</code>.
         *  @param visit to insert.
         */
        protected void insert(Visit visit) {
            visit.setId(incrementer.nextIntValue());
            Object[] objs = new Object[] {
                new Integer(visit.getId()),
                new Integer(visit.getPetId()),
                new java.sql.Date(visit.getVisitDate().getTime()),
                visit.getDescription()
            };
            this.update(objs);
        }
        
    }
    
}
