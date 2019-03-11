package com.test;

import com.module.*;
import com.dao.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new ToHbase().deleteTable();
		new ToHbase().createTable();
//		
		new MyProducer().start();		
		new MyConsumer("test1").run();
		Scanner title = new Scanner(System.in);
		
		System.out.println("输入你要搜索的内容：");
        String titleKey = title.next();

        ArrayList<String> searcher = new Searcher().searchTitle(titleKey);
        for(int j = 0 ;j < searcher.size();j+= 2) {
        	System.out.println(searcher.get(j) + "  这里显示的");
        	System.out.println(searcher.get(j+1) + "  123");
        }
        
		ArrayList<String> value = new wordcount().run();
		
		for(int i = 0 ; i < value.size() ;i++) {
			System.out.println(value.get(i));
		}
		title.close();
		
	}

}
