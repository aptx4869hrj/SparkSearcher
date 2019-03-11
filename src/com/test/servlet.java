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
		//��һ�η���
		
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
		//ArrayList<String> searcher = new Searcher().searchTitle("ϰ��ƽ");
		
		
		new test();
		ArrayList<String> value = new servlet().readfile(filepath2);
		
		String keyword = request.getParameter("keyword");
		
//		new test().run(keyword);
		
////		
//		result.add("asdasdas");
//		result.add("asdasdqwre");
//		result.add("32143546");
//		int num = result.size();
//		searcher.add("���ھ���������Ӱ �ù�������");
//		searcher.add("http://www.chinanews.com/yl/2019/01-02/8717749.shtml");
//		searcher.add("ɽ����̨��������������һԪʱ������ͼ��");
//		searcher.add("http://www.chinanews.com/sh/2019/01-02/8717770.shtml");
		System.out.println(keyword);
//		value.add("�Ƽ�");
//		value.add("456");
		String search = request.getParameter("username");
		String msg = "shiyan";
		

			//�ѵ�ǰ���µĴ������������
		request.setAttribute("searcher", searcher);
		request.setAttribute("value", value);
		request.setAttribute("keyword", keyword);


		//response.getWriter().write("��Ϸ������<a href='"+request.getContextPath()+"/guess.jsp'>����һ��</a>");


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
            if(file.isFile() && file.exists()){ //�ж��ļ��Ƿ����
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//���ǵ������ʽ
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	lines.add(lineTxt);
                	
                }
                read.close();
//                return lines;
    }else{
        System.out.println("�Ҳ���ָ�����ļ�");
    }
    } catch (Exception e) {
        System.out.println("��ȡ�ļ����ݳ���");
        e.printStackTrace();
    }
		return lines;
	}
}
