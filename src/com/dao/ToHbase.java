package com.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;


public class ToHbase {

//	public static void main(String[] args) throws IOException {
//
//		System.out.println("111");
//		ToHbase th = new ToHbase();
////		th.createTable();
//		//th.deleteTable();
//		//th.searchTitle("习近平");
//
//	}
	public static void insert(int rowKey , String title,String url) throws IOException {	
		
		System.setProperty("hadoop.home.dir", "C:\\Users\\HRJ\\hadoop-2.7.7");
		
		//创建配置对象
		Configuration config = HBaseConfiguration.create();
		//配置zookeeper集群
		config.set("hbase.zookeeper.quorum", "172.17.11.141,172.17.11.144,172.17.11.145");
		config.set("hbase.zookeeper.property.clientPort", "2181");
		//配置hmaster地址
		config.set("hbase.master", "172.17.11.231:16000");
		//获取hbase数据库的链接
		Connection connection = ConnectionFactory.createConnection(config);
		//根据表名获取表
		Table table = connection.getTable(TableName.valueOf("kafkaData"));
		
		Put put = new Put(Bytes.toBytes("ROW" + rowKey));
        put.addColumn(Bytes.toBytes("title"), Bytes.toBytes("title"), Bytes.toBytes(title));
        put.addColumn(Bytes.toBytes("url"), Bytes.toBytes("url"), Bytes.toBytes(url));
        table.put(put);
           
		table.close();
		connection.close();
	}

	 public static void createTable() throws IOException {
		System.setProperty("hadoop.home.dir", "C:\\Users\\HRJ\\hadoop-2.7.7");
		//创建配置对象
		Configuration config = HBaseConfiguration.create();
		//配置zookeeper集群
		config.set("hbase.zookeeper.quorum", "172.17.11.141,172.17.11.144,172.17.11.145");
		config.set("hbase.zookeeper.property.clientPort", "2181");
		//配置hmaster地址
		config.set("hbase.master", "172.17.11.231:16000");
		Connection connection = ConnectionFactory.createConnection(config);
		
		//创建数据库管理对象
		Admin admin = connection.getAdmin();// hbase表管理类
		
		//创建表对象
		HTableDescriptor htd = new HTableDescriptor(TableName.valueOf("kafkaData"));

		//创建列族对象
		HColumnDescriptor tihcd = new HColumnDescriptor("title");
		HColumnDescriptor urlhcd = new HColumnDescriptor("url");


		//把列族加入表
		htd.addFamily(tihcd);
		htd.addFamily(urlhcd);

		//创建表
		admin.createTable(htd);
		admin.close();
		
		connection.close();
	}
	
	public static void deleteTable() throws IOException {
		System.setProperty("hadoop.home.dir", "C:\\Users\\HRJ\\hadoop-2.7.7");
		//创建配置对象
		Configuration config = HBaseConfiguration.create();
		//配置zookeeper集群
		config.set("hbase.zookeeper.quorum", "172.17.11.141,172.17.11.144,172.17.11.145");
		config.set("hbase.zookeeper.property.clientPort", "2181");
		//配置hmaster地址
		config.set("hbase.master", "172.17.11.231:16000");
		Connection connection = ConnectionFactory.createConnection(config);
		
		//创建数据库管理对象
		Admin admin = connection.getAdmin();// hbase表管理类
		
		TableName tn = TableName.valueOf("kafkaData");
        if (admin.tableExists(tn)) {
            admin.disableTable(tn);
            admin.deleteTable(tn);
        }
        admin.close();
        connection.close();
	}
	
	public static void searchTitle(String keyword) throws IOException {
		System.setProperty("hadoop.home.dir", "C:\\Users\\HRJ\\hadoop-2.7.7");
		
		//创建配置对象
		Configuration config = HBaseConfiguration.create();
		//配置zookeeper集群
		config.set("hbase.zookeeper.quorum", "172.17.11.141,172.17.11.144,172.17.11.145");
		config.set("hbase.zookeeper.property.clientPort", "2181");
		//配置hmaster地址
		config.set("hbase.master", "172.17.11.231:16000");
		//获取hbase数据库的链接
		Connection connection = ConnectionFactory.createConnection(config);
		//根据表名获取表
		Table table = connection.getTable(TableName.valueOf("kafkaData"));
		
		int i=0;
		Scan scan = new Scan();
		
		//Filter filter = new RowFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("201812120001")));
		//Filter filter = new PrefixFilter(Bytes.toBytes("01"));
		Filter filter = new ValueFilter(CompareOp.EQUAL, new SubstringComparator(keyword));

		scan.setFilter(filter);
		
		ResultScanner rs = table.getScanner(scan);
		for (Result result : rs) {
			//byte[] jn=result.getValue(Bytes.toBytes("stuno"), Bytes.toBytes("year"));
			//System.out.println(Bytes.toString(jn));
			
			System.out.print("RowKey:["+Bytes.toString(result.getRow())+"]");
			System.out.print("\t");
			
			Map map=result.getNoVersionMap();
			Set set = map.keySet();
			Iterator it = set.iterator();
			while(it.hasNext()) {
				byte[] cfkey = (byte[])it.next();
				System.out.print(Bytes.toString(cfkey)+":[");
				
				Map kvMap = (Map)map.get(cfkey);
				Set<Map.Entry> kvs = kvMap.entrySet();
				Iterator kvit = kvs.iterator();
				while(kvit.hasNext()) {
					Map.Entry<byte[],byte[]> me = (Map.Entry)kvit.next();
					System.out.print(Bytes.toString(me.getKey())+":"+Bytes.toString(me.getValue())+",");
				}
				System.out.print("]\t");
			}
			
			System.out.println("");
			i++;
		}
		table.close();
		connection.close();
		
	}
}
