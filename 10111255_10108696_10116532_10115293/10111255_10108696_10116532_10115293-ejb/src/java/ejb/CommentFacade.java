
package ejb;

import entity.Comment;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Facade EJB for comment entity class
 *
 * @author  Rory Burns, 10108696
 *          Vincent O'Brien, 10111255
 *          Jamie Chambers, 10116532
 *          Ger Lynch, 10115293
 */
@Stateless
public class CommentFacade extends AbstractFacade<Comment> {
  @PersistenceContext(unitName = "10111255_10108696_10116532_10115293-ejbPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public CommentFacade() {
    super(Comment.class);
  }
  
}
