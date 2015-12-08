
package ejb;

import entity.Log;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Message driven EJB to store logs
 *
 * @author  Rory Burns, 10108696
 *          Vincent O'Brien, 10111255
 *          Jamie Chambers, 10116532
 *          Ger Lynch, 10115293
 */
@MessageDriven(mappedName = "jms/NewLogMessage", activationConfig = {
  @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
  @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class NewLogMessage implements MessageListener {

  @Resource
  private MessageDrivenContext mdc;
  @PersistenceContext(unitName = "10111255_10108696_10116532_10115293-ejbPU")
  private EntityManager em;

  public NewLogMessage() {
  }

  @Override
  public void onMessage(Message message) {
    ObjectMessage msg = null;
    try {
      if (message instanceof ObjectMessage) {
        msg = (ObjectMessage) message;
        Log e = (Log) msg.getObject();
        save(e);
      }
    } catch (JMSException e) {
      e.printStackTrace();
      mdc.setRollbackOnly();
    } catch (Throwable te) {
      te.printStackTrace();
    }
  }

  public void save(Object object) {
    em.persist(object);
  }
}