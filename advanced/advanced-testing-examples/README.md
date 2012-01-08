Advanced Testing Examples
=========================

Example test cases that show advanced techniques to test Spring Integration based applications. 

For basic testing examples see: **basic/testing-examples**

## Examples

**jms.JmsMockTests.java**

This test case shows how to test an integration flow that uses JMS inbound channel adapter by using **Mockito** to mock a *JmsTemplate* (and dependent JMS objects). The example flow in  **src/main/resources/integration-config.xml** does not depend on JMS but includes some additional error handling on **errorChannel**.  The **errorChannel** is configured on the JMS adapter. So we want test the entire flow for cases in which an invalid message is received via JMS and routed to **errorChannel**. How do we do this without requiring a JMS message broker?