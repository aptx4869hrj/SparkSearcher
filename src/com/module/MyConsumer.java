package com.module;

import com.dao.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.io.IOException;
import java.util.*;

public class MyConsumer extends Thread {
    private String groupid;
    private String topic = "test1";

    public MyConsumer(String groupid){
        this.groupid = groupid;
    }
    public void run() {
        Properties props = new Properties();

        props.put("bootstrap.servers", "172.17.11.231:9092,172.17.11.232:9092,172.17.11.233:9092");
        props.put("group.id", groupid);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        consumer.subscribe(Arrays.asList(topic));
        consumer.seekToBeginning(new ArrayList<TopicPartition>());

        Map<String, List<PartitionInfo>> listTopics = consumer.listTopics();
        Set<Map.Entry<String, List<PartitionInfo>>> entries = listTopics.entrySet();

        for (Map.Entry<String, List<PartitionInfo>> entry:
             entries) {
            System.out.println("topic:" + entry.getKey());

        }
        int num = 0;

        System.out.println("null");
        
        while(true) {
        ConsumerRecords<String, String> records = consumer.poll(1000);
        if (!records.iterator().hasNext())
        	break;
        for(ConsumerRecord<String, String> record : records) {
//        	if(num == 0 || record.key() == null || record.value() == null) {
//        		continue;
//        	}
        	ToHbase th =  new ToHbase();
        	try {
				th.insert(num,record.key(),record.value());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             System.out.println("题目：" + record.key() + "网址：" + record.value() + num);
             num ++;
        }
            //按分区读取数据
//              for (TopicPartition partition : records.partitions()) {
//            	  System.out.println("null");
//                  List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
//                  for (ConsumerRecord<String, String> record : partitionRecords) {
//                      System.out.println("fetched from partition " + partition.partition() + ", "+ record.offset() + ": " + record.value());
//                  }
//              }
    }
        }

}
