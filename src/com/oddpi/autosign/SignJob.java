package com.oddpi.autosign;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class SignJob implements Job {

	public void execute(JobExecutionContext arg0) {

		System.out.println("**********************************");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

		String[][] InfoArray = { { "xxx", "xxx" } };

		for (int i = 0; i < InfoArray.length; i++) {
			try {
				System.out.println(InfoArray[i][0] + "sign time at " + sdf.format(new Date()));
				AutoSign.loginPost(InfoArray[i][0], InfoArray[i][1]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
