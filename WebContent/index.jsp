<%@ page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head> 
    <title>谷弟搜索</title> 
    <link rel="stylesheet" type="text/css" href="css/bootstrap.css">
	<link rel="stylesheet" href="css/style.css"> 
  </head>
  
  <body>
  
  <!--  <form action="action" method="get">  
            输入关键词：<input type="text" name="keyword" /><br/>    
     <input type="submit" value="登录" />
  </form> 
  -->
  <div id="top-image">
  <div id="content" class="container center-block">
    <div class="jumbotron">
      <div class="container">
        <h1>谷弟搜索</h1>
        <br><br>
        <form action="action" method="post">
        <div class="input-group input-group-lg"> <span class="input-group-addon" id="sizing-addon1"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></span>
          <input type="text" class="form-control" placeholder="输入关键词" aria-describedby="sizing-addon1" name="keyword">
          <span class="input-group-btn">
          <button class="btn btn-default" type="submit" >搜 索</button>
          </span> </div>
          </form>
      </div>
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