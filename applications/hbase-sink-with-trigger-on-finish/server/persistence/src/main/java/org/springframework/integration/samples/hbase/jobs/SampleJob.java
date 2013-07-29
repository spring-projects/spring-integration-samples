package org.springframework.integration.samples.hbase.jobs;
/*
 * Compile and run with:
 * javac -cp `hbase classpath` TestHBase.java 
 * java -cp `hbase classpath` TestHBase
 */
import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.springframework.integration.samples.hbase.mappers.SampleMapper;
import org.springframework.integration.samples.hbase.utils.HBaseTableUtils;

/**
 * MapReduce Job that reads received messages of NER tools and send "transformed" messages to the AMQP queue
 * 
 * @author Flavio Pompermaier
 * */
public class SampleJob {

	public static final String NAME = "sample";
	/**
	 * Sets up the counting job.
	 * 
	 * @param conf
	 *            The current configuration.
	 * @return The newly created job.
	 * @throws IOException
	 *             When setting up the job fails.
	 * @throws UndefinedAnormConfParameter
	 *             When the table name is not properly configured.
	 */
	public static Job createSampleJob(Configuration conf,
			String tableToScan, Date start) throws IOException {

		String columnFamily = conf.get("family-column");
		// final String outputTableName = "test-output";
		final byte[] family = Bytes.toBytes(columnFamily);
		final byte[] qualifier = Bytes.toBytes(HBaseTableUtils.JSON_CONTENT_QUALIFIER);
		
		// create scanner
		Scan scan = HBaseTableUtils.getTempStorageScanner(family, qualifier, start);

		Job job = new Job(conf, "Sample Mapreduce job");
		// Set the Job Jar by finding where a given class came from
		job.setJarByClass(SampleJob.class);
		// job.setCombinerClass(EntityCounterCombiner.class);
		TableMapReduceUtil.initTableMapperJob(tableToScan,// table
				scan,// scan instance
				SampleMapper.class,// mapper class
				Text.class, // mapper output key
				Text.class, // mapper output value
				job);// the current job to adjust

		// job.setCombinerClass(SampleCombiner.class);//combiner class
		// job.setReducerClass(SampleReducer.class); // reducer class
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		// job.setOutputFormatClass(TableOutputFormat.class);
		// job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE,
		// outputTableName);
		job.setOutputFormatClass(NullOutputFormat.class);
		job.setNumReduceTasks(0);

		return job;
	}

	
}