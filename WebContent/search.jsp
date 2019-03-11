<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/bootstrap.css">
	<link rel="stylesheet" href="css/style.css"> 
<meta charset="UTF-8">
<title>搜索结果</title>
</head>
<body>
<div id="top-image">
<div class="container">


<div class = "right">
	<%
		String keyword = (String)request.getAttribute("keyword");
	%>
	<div class="input-group input-group-lg"> <span class="input-group-addon" id="sizing-addon1"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></span>
          <input type="text" class="form-control" placeholder=<%= keyword %> aria-describedby="sizing-addon1" name="keyword">
          <span class="input-group-btn">
          <button class="btn btn-default" type="submit" >搜 索</button>
          </span> </div>
</div>           
  <div class = "absolute">
	<%
		ArrayList<String> searcher = (ArrayList<String>) request.getAttribute("searcher");
		for(int i = 0 ;i < searcher.size();i++){
			%>
			<div>
			<%
				if(i % 2 == 0){
			%>	
			<h4>	<a href="<%= searcher.get(i+1) %>"><% out.write("<font color='white'>"+ searcher.get(i)  + "\n"+"</font>"); %> </a></h4>
				</div>
				<br>
			<%
				}else{
			%>
			<div>
				<a href="<%= searcher.get(i) %>"><% out.write("<font color='white'>"+ searcher.get(i)  + "\n"+"</font>"); %> </a>
			 </div>
			<br>
			<%
				}
			%>
		<%}%>
	</div>	
	<div class = "position2">
	<h3><font color='white'>为您推荐</font></h3>
	<br>
	<%ArrayList<String> value = (ArrayList<String>) request.getAttribute("value");
	for(int i = 0 ;i < 10;i++){%>
		<div>
		<h4><font color='white'><%= value.get(i) %></font></h4>	
		</div>
		
			
		<%}%>
	</div>	
</div>	
</div>
<script src="js/jquery.min.js"></script>
<script src="js/ios-parallax.js"></script> 
<script type="text/javascript">
$(document).ready(function() {
  $('#top-image').iosParallax({
	movementFactor: 50
  });
});
</script>
</body>
</html>