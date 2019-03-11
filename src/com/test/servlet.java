package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.module.Searcher;
import com.module.wordcount;

public class servlet extends HttpServlet {
	static String filepath1 = "C:\\Users\\HRJ\\Documents\\Python Scripts\\spdier\\total.txt";
	static String filepath2 = "C:\\Users\\HRJ\\Documents\\Python Scripts\\spdier\\analyse.txt";
	public servlet(){
		//第一次访问
		
	}
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
//		new test().run();
//		new testdemo();
		//		Searcher sc = new Searcher();
		ArrayList<String> searcher = new servlet().readfile(filepath1);
		//ArrayList<String> searcher = new Searcher().searchTitle("习近平");
		
		
		new test();
		ArrayList<String> value = new servlet().readfile(filepath2);
		
		String keyword = request.getParameter("keyword");
		
//		new test().run(keyword);
		
////		
//		result.add("asdasdas");
//		result.add("asdasdqwre");
//		result.add("32143546");
//		int num = result.size();
//		searcher.add("《黑镜》互动电影 让观众做主");
//		searcher.add("http://www.chinanews.com/yl/2019/01-02/8717749.shtml");
//		searcher.add("山东烟台市区公交开启“一元时代”（图）");
//		searcher.add("http://www.chinanews.com/sh/2019/01-02/8717770.shtml");
		System.out.println(keyword);
//		value.add("推荐");
//		value.add("456");
		String search = request.getParameter("username");
		String msg = "shiyan";
		

			//把当前竞猜的次数放入域对象
		request.setAttribute("searcher", searcher);
		request.setAttribute("value", value);
		request.setAttribute("keyword", keyword);


		//response.getWriter().write("游戏结束。<a href='"+request.getContextPath()+"/guess.jsp'>再来一盘</a>");


		request.getRequestDispatcher("/search.jsp").forward(request, response);
	}
 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	public ArrayList<String> readfile(String filepath){
		ArrayList<String> lines = new ArrayList<String>();
		
		try {
            String encoding="UTF-8";
            File file=new File(filepath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	lines.add(lineTxt);
                	
                }
                read.close();
//                return lines;
    }else{
        System.out.println("找不到指定的文件");
    }
    } catch (Exception e) {
        System.out.println("读取文件内容出错");
        e.printStackTrace();
    }
		return lines;
	}
}
