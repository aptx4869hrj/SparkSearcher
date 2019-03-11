package com.module;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import com.huaban.analysis.jieba.*;
import scala.Tuple2;

public class wordcount {
	static ArrayList<String> value = new ArrayList<String>();
	static String filepath = "C:\\Users\\HRJ\\Documents\\Python Scripts\\spdier\\wordcount.txt";
	static String filepath1 = "C:\\Users\\HRJ\\Documents\\Python Scripts\\spdier\\analyse.txt";
	static List<String> readfile = new ArrayList<String>();
	public static ArrayList<String> run() throws IOException  {
//		readTxtFile(filepath);
		try {
            String encoding="UTF-8";
            File file=new File(filepath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	readfile.add(lineTxt);
                	
                    System.out.println(lineTxt);
                }
                read.close();
    }else{
        System.out.println("找不到指定的文件");
    }
    } catch (Exception e) {
        System.out.println("读取文件内容出错");
        e.printStackTrace();
    }
 

        
        SparkConf conf = new SparkConf()
                .setAppName("WordCount")
                .setMaster("local");
        
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> lines = sc.parallelize(readfile);
//        JavaRDD<String> lines = sc.textFile(filepath);
        System.out.println(lines.count());
        System.out.println(lines.first());
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public Iterator<String> call(String line) throws Exception {
            	JiebaSegmenter segmenter = new JiebaSegmenter();
//                String[] strings=line.split("##");
                String titile=line.trim();
                List<String> list1=new ArrayList<String>();
                List<String> strs=segmenter.sentenceProcess(titile);
                for(int i=0;i<strs.size();i++){
                    if(strs.get(i).trim().length()<2){
                        strs.remove(i);
                        i--;
                    }else{
                        list1.add(strs.get(i));
                    }
                }
                return list1.iterator();  
            }
            
        });
        
        JavaPairRDD<String, Integer> pairs = words.mapToPair(
                
                new PairFunction<String, String, Integer>() {

                    private static final long serialVersionUID = 1L;
        
                    @Override
                    public Tuple2<String, Integer> call(String word) throws Exception {
                        return new Tuple2<String, Integer>(word, 1);
                    }
                    
                });

        JavaPairRDD<String, Integer> wordCounts = pairs.reduceByKey(
                
                new Function2<Integer, Integer, Integer>() {
                    
                    private static final long serialVersionUID = 1L;
        
                    @Override
                    public Integer call(Integer v1, Integer v2) throws Exception {
                        return v1 + v2;
                    }
                    
                });
        //键值对互换后开始排序
        JavaPairRDD<Integer,String> wordCounts2 = wordCounts.mapToPair(new PairFunction<Tuple2<String,Integer>, Integer, String>() {
        	private static final long serialVersionUID = 1L;
        	public Tuple2<Integer,String> call(Tuple2<String, Integer> t){
        		return new Tuple2<Integer,String>(t._2,t._1);
        	}
		});
        
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
		
        JavaPairRDD<Integer,String> sortWord = wordCounts2.sortByKey(false);
        sortWord.foreach(new VoidFunction<Tuple2<Integer,String>>() {
            
            private static final long serialVersionUID = 1L;  // 序列化版本号
            
            public void call(Tuple2<Integer, String> wordCount) throws Exception {
            	value.add(wordCount._2);
            	new wordcount().toFile(filepath1, wordCount._2);
                System.out.println(wordCount._1 + " appeared " + wordCount._2 + " times.");    // 获取tuple的元素通过_
            }
            
        });
        
        sc.close();
		return value;
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