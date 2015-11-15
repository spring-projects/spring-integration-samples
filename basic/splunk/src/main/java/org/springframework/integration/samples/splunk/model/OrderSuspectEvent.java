package org.springframework.integration.samples.splunk.model;

import org.springframework.integration.splunk.event.SplunkEvent;


/**
 * @author fbalicchia
 * @version $Id: $
 */
public class OrderSuspectEvent extends SplunkEvent
{

    private static final long serialVersionUID = 4203808769099452241L;

    public static String ORDER_NUMBER = "recipient";

    public static String EAN_ITEM = "sender";

    public static String EMAIL_USER = "subject";

    public OrderSuspectEvent()
    {
        super();
    }

    public void setOrderNumber(String orderNumber)
    {
        addPair(ORDER_NUMBER, orderNumber);
    }

    public void setEan(String eanNumber)
    {
        addPair(EAN_ITEM, eanNumber);
    }

    public void setEmailuser(String email)
    {
        addPair(EMAIL_USER, email);
    }

}
