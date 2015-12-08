
package ejb;

import entity.Log;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Facade EJB for log entity class
 *
 * @author  Rory Burns, 10108696
 *          Vincent O'Brien, 10111255
 *          Jamie Chambers, 10116532
 *          Ger Lynch, 10115293
 */
@Stateless
public class LogFacade extends AbstractFacade<Log> {
  @PersistenceContext(unitName = "10111255_10108696_10116532_10115293-ejbPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public LogFacade() {
    super(Log.class);
  }
  
}
