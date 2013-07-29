package org.springframework.integration.samples.hbase.test;

import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.mapreduce.Job;
import org.junit.Test;
import org.springframework.integration.samples.hbase.jobs.SampleJob;


public class TestMapRedJobs {

	@Test
	public void testMapReduceJob() throws Exception {
		Configuration conf = HBaseConfiguration.create();
		Date yesterady = getDayBefore(new Date());
		Job job = SampleJob.createSampleJob(conf, "mysource", yesterady);
		boolean success = job.waitForCompletion(true);
		String successStr = success ? "successfully" : "with errors";
		System.out.println("Sample mapreduce job completed " + successStr);
	}

	private static Date getDayBefore(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}
}
