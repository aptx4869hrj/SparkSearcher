package com.module;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.clients.producer.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


public class MyProducer extends Thread{
	private String filepath = "C:\\Users\\HRJ\\Documents\\Python Scripts\\spdier\\new";
    public void run() {

        try {

            Properties props = new Properties();
            props.put("bootstrap.servers", "172.17.11.231:9092,172.17.11.232:9092,172.17.11.233:9092");
            props.put("acks", "0");
            props.put("retries", 0);
            props.put("batch.size", 16384);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

            Producer<String, String> producer = new KafkaProducer<String, String>(props);
            	
            	 File file = new File(filepath);
                 if(file.isDirectory()) {
                 	String[] filelist = file.list(); 
                     for (int i = 0; i < filelist.length; i++) { 
                             File readfile = new File(filepath + "\\" + filelist[i]); 
                             
                             if (!readfile.isDirectory()) {
                             	FileInputStream inputStream = new FileInputStream(readfile.getPath());
                         		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                         		
                         		
                         		String title = bufferedReader.readLine();
                         		String url = bufferedReader.readLine();	                    			
                         		System.out.println(i);
                         		ProducerRecord<String, String> record = new ProducerRecord<String, String>("test1", title, url);
                         		System.out.println(record.key());
                                producer.send(record, new Callback() {
                                	
                                	
                                    public void onCompletion(RecordMetadata metadata, Exception e) {
                                        if (e != null)
                                            e.printStackTrace();
                                        System.out.println("message send to partition " +" " + metadata.partition() + ", offset: " + metadata.offset()+ "OK");
                                    }
                                    
                                });
                         		
                         		inputStream.close();
                         		bufferedReader.close();

                             } 
                     }
                Thread.sleep(1000);
                 }
            

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}