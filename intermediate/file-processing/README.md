File Processing Sample
======================

This sample demonstrates how to wire a message flow to process Files sequentially (maintain the order) or concurrently (no order). The difference is in the *Poller* configuration - single threaded or multi-threaded. 

The Poller, configured for the File Inbound Channel Adapter and after polling files and converting them to Messages, will distribute these Messages one at the time using its own thread by default. So files will be pulled based in the order they were created in the directory and processed in such order.

See **sequentialFileProcessing-config.xml** for configuration details. The FileProcessor class will randomly delay the file processing, but the order of processing is still maintained regardless of this delay.

If order is not important, then you can process files concurrently. All you need to do, is to configure a **task-executor** for the Poller.  Based on the delay in **FileProcessor**, you'll clearly observe that the order of processing is based on the availability of the thread in the thread poll of task executor

To run sample, execute **FileProcessingTest**. You should see the following output:

	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - 
	#### Starting Sequential processing test ####
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Populating directory with files
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Populated directory with files
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Starting Spring Integration Sequential File processing
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessor - Processing File: input/file_0.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Finished processing input/file_0.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessor - Processing File: input/file_1.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Finished processing input/file_1.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessor - Processing File: input/file_2.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Finished processing input/file_2.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessor - Processing File: input/file_3.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Finished processing input/file_3.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessor - Processing File: input/file_4.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Finished processing input/file_4.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - 

	#### Starting Concurrent processing test #### 
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Populating directory with files
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Populated directory with files
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Starting Spring Integration Sequential File processing
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessor - Processing File: input/file_1.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Finished processing input/file_1.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessor - Processing File: input/file_0.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Finished processing input/file_0.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessor - Processing File: input/file_3.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Finished processing input/file_3.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessor - Processing File: input/file_4.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Finished processing input/file_4.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessor - Processing File: input/file_2.txt
	INFO : org.springframework.integration.samples.fileprocessing.FileProcessingTest - Finished processing input/file_2.txt unless you specify task-executor or introduce task-executor somewhere downstream you are fine. 