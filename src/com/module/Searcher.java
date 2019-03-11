package com.module;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;

public class Searcher extends HttpServlet{
	static ArrayList<String> searcher = new ArrayList<String>();
	static String filepath = "C:\\Users\\HRJ\\Documents\\Python Scripts\\spdier\\wordcount.txt";
	static String filepath1 = "C:\\Users\\HRJ\\Documents\\Python Scripts\\spdier\\total.txt";
	
	public static void main(String[] args) throws IOException {

		System.out.println("111");
		Searcher sc = new Searcher();
//		th.createTable();
		//th.deleteTable();
		//sc.searchTitle("习近平");
		//sc.printValue("ROW99");

	}
	public static ArrayList<String> searchTitle(String keyword) throws IOException {
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
		
		Scan scan = new Scan();

		Filter filter = new ValueFilter(CompareOp.EQUAL, new SubstringComparator(keyword));

		scan.setFilter(filter);
		
		ResultScanner rs = table.getScanner(scan);
		for (Result result : rs) {

			System.out.print("\t");
			Searcher sc = new Searcher();
			sc.printValue(table,Bytes.toString(result.getRow()));
		}
		
		File file = new File(filepath);
		if (!file.exists()) {
		    try {
		        file.createNewFile();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    System.out.println("文件已创建");
		} else {
			file.delete();
			file.createNewFile();
		    System.out.println("文件已存在");
		}
		
		for(int j = 0 ; j <searcher.size();j+=2) {
			new Searcher().toFile(filepath, searcher.get(j));
		}
		
		File file1 = new File(filepath1);
		if (!file1.exists()) {
		    try {
		    	file1.createNewFile();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    System.out.println("文件已创建");
		} else {
			file1.delete();
			file1.createNewFile();
		    System.out.println("文件已存在");
		}
		for(int num = 0;num < searcher.size();num++) {
			new Searcher().toFile(filepath1, searcher.get(num));
		}
		table.close();
		connection.close();
		return searcher;
	}
	
	static void printValue(Table table ,String rowkey) throws IOException {
		int i=0;
		Scan scan = new Scan();
		
		Filter filter = new RowFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(rowkey)));
		//Filter filter = new PrefixFilter(Bytes.toBytes("01"));
		//Filter filter = new ValueFilter(CompareOp.EQUAL, new SubstringComparator(keyword));

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
					searcher.add(Bytes.toString(me.getValue()));
					System.out.print(Bytes.toString(me.getKey())+":"+Bytes.toString(me.getValue())+",");
				}
				System.out.print("]\t");
			}
			
			System.out.println("");
			i++;
		}
	}
	public static void toFile(String file, String conent) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true),"UTF-8"));
			out.write(conent+"\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
