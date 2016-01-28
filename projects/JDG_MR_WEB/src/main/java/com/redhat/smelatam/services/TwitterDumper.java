/*
 * Copyright 2011 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.smelatam.services;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.infinispan.AdvancedCache;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.StatusStream;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 * TwitterDumper
 */
@Startup
@Singleton
public class TwitterDumper implements Runnable{
	
	private long DUMP_PERIOD_MILLIS = 900000;

	@Inject
	public static AdvancedCache<String,String> reqCache;
	
	@PostConstruct
	public static void startTweets() {
		System.out.println("Getting tweets");
		
		
		Thread thread = new Thread(new TwitterDumper());
		thread.start();
	}
	
	
    public void run()  {
		System.out.println("getTweets");


        final TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

        final StatusListener listener = new StatusListener() {
        	
        	@Inject
        	MDRService cus;
        	
        	
            private int counter = 0;

            public void onException(Exception arg0) {
            }

            public void onDeletionNotice(StatusDeletionNotice arg0) {
            }

            public void onScrubGeo(long arg0, long arg1) {
            }

            public void onTrackLimitationNotice(int arg0) {
            }

            public void onStatus(Status arg0) {
                counter++;
                
                if (counter % 100 == 0) {
                    System.out.println("Message = " + counter);
                }
                
                
                try {
               	
                	String source = arg0.getSource();
                	source = source.substring(0, source.indexOf("</a>"));
                	source = source.substring(source.lastIndexOf(">")+1);
                	
                	if (source.indexOf("|")>0) source=source.replace("|", " ");
                	
                	String country = arg0.getPlace()==null?null:arg0.getPlace().getCountry();
                	
                	String buf = country + "|" 
                			+ source + "|"
                			+ arg0.getText() + "|" 
                			//+ arg0.getUser().getName() + "|"
                			//+ arg0.getCreatedAt().getTime();
                	        + DateFormat.getTimeInstance().format(arg0.getCreatedAt());
                	
                	DecimalFormat df = new DecimalFormat("000000");
                	
                	System.out.println(df.format(counter) +": "+ buf);
                	
                	
                	
                	TwitterDumper.reqCache.putAsync(df.format(counter), buf);
                	
                } catch (Exception e) {
                	//e.printStackTrace();
                }
            }

            public void onStallWarning(StallWarning arg0) {
            }
        };
        try {
			StatusStream stream = twitterStream.getSampleStream();
			long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start < DUMP_PERIOD_MILLIS) {
			    stream.next(listener);
			}
			twitterStream.shutdown();
			Thread.currentThread().sleep(10000);
		} catch (TwitterException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //oos.close();
    }

}
