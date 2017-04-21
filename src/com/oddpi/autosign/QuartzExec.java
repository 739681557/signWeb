package com.oddpi.autosign;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QuartzExec {

	public static void main(String[] args) {
		testJob();
	}

	private static void testJob() {
		SignJob job = new SignJob();
		String jobName = "11"; //
		String softRight = "1.7.6";
		try {
			System.out.println("service startup at " + new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format( new Date() ));
			System.out.println("right:" + softRight);
			//QuartzManager.addJob(jobName, job, "15 17,18 15 * * ?"); // 周一至周五的上午10:15触发
			QuartzManager.addJob(jobName, job, "0 0 18 * * ?"); //每天9点 18点 执行
			//QuartzManager.addJob(jobName, job, "0 0 9 * * ?"); //每天9点 18点 执行 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}