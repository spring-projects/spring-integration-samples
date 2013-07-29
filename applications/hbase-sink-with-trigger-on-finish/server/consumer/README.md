Run first org.springframework.integration.samples.server.Contoller and then 
org.springframework.integration.samples.server.Consumer. 
If client send them messages, they will store them in Bbase and, once finished,
 they could run some task in ControlWorker.triggerDataTransformation()