<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.nuaa.util.C"%>
<%
	int sid = (Integer) request.getAttribute("Sid"); //验证是否开店
%>

<div id="inner">

	<!-- 左侧导航栏 -->
	<jsp:include page="left.jsp" flush="true"><jsp:param name="curSelected" value="shopupload" /></jsp:include>

	<!-- 右侧内容 -->	
	<div class="right">
		<%
			if(sid == C.OPENED_SHOP_NO) //没有开店,显示“我要开店”按钮
			{
				%>
					<div class="uploaddiv">
						<a href="ShopInfoClServlet?dowhat=openShop" class="greenbutton" onclick="return window.confirm('您确定要开店?');">我要开店</a>
					</div>
				<%
			}
			else //已开店，显示店铺信息
			{
				%>
					<div class="divline" style="font-size:15px;">书籍发布</div>
					<div class="contentline" style="font-size: 14px;"><br>
		
				    <form action="ShopUploadClServlet?dowhat=uploadBook" enctype="multipart/form-data" method="post">
						<!-- 商品概要 -->
						<div class="title"><span>&nbsp;书籍概要&nbsp;</span></div><br>
						<hr class="titlehr">
						
						<div class="uploaddivparent">
							<div class="uploaddiv">
								书籍标题：&nbsp;&nbsp;<input type="text" name="book_name" onkeyup="updateLeftStrLength(this)" maxlength="50" autocomplete="off" autofocus="autofocus" class="editline" style="width:50%;">&nbsp;&nbsp;<font>您还可以输入<span class="font_bold_green">50</span>个字</font>
							</div>
							
							<div class="uploaddiv">
								作者名称：&nbsp;&nbsp;<input type="text" name="book_author" onkeyup="updateLeftStrLength(this)" maxlength="15" autocomplete="off" class="editline">&nbsp;&nbsp;<font>您还可以输入<span class="font_bold_green">15</span>个字</font>
							</div>	
					
							<div class="uploaddiv">
								出&nbsp;&nbsp;版&nbsp;&nbsp;社：&nbsp;&nbsp;<input type="text" name="book_publisher" onkeyup="updateLeftStrLength(this)" maxlength="15" autocomplete="off" class="editline">&nbsp;&nbsp;<font>您还可以输入<span class="font_bold_green">15</span>个字</font>
							</div>
								
							<div class="uploaddiv">
								出版日期：&nbsp;&nbsp;<input type="date" name="book_publishdate" class="editline">
							</div>			
								
							<div class="uploaddiv">
								书籍图片：&nbsp;&nbsp;<input type="file" name="book_image">
							</div>		
							
							<div class="uploaddiv">
								所属分类：&nbsp;&nbsp;
								<select class="selectboxsmall" name="book_parent_type" onchange="onParentTypeChange()">
									<option value="0">待定</option>
									<option value="1">青春</option>
									<option value="2">文艺</option>
									<option value="3">小说</option>
									<option value="4">童书</option>
									<option value="5">教育</option>
									<option value="6">生活</option>
									<option value="7">人文社科</option>
									<option value="8">科技</option>
									<option value="9">经管</option>
								</select>
								
								<select class="selectboxsmall" name="book_child_type">
									<option value="0">待定</option>
								</select>
							</div>						
						</div>	
							
		
						<!-- 交易信息 -->
						<div class="title"><span>&nbsp;交易信息&nbsp;</span></div><br>
						<hr class="titlehr">
						<div class="uploaddivparent">
							<div class="uploaddiv">
								定&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;价：&nbsp;&nbsp;<input type="text" name="book_oriprice" autocomplete="off" class="editline" style="width:7%;">&nbsp;&nbsp;元
							</div>
							
							<div class="uploaddiv">
								促&nbsp;&nbsp;销&nbsp;&nbsp;价：&nbsp;&nbsp;<input type="text" name="book_price" autocomplete="off" class="editline" style="width:7%;">&nbsp;&nbsp;元
							</div>
							
							<div class="uploaddiv">
								数&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;量：&nbsp;&nbsp;<input type="text" name="book_stocknum" autocomplete="off" class="editline" style="width:7%;">&nbsp;&nbsp;个
							</div>
						</div>
						
						
						<!-- 书籍详情 -->
						<div class="title"><span>&nbsp;书籍详情&nbsp;</span></div><br>
						<hr class="titlehr">
						<div class="uploaddivparent">
							<div class="uploaddiv">
								版&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;次：&nbsp;&nbsp;<input type="text" name="book_version" autocomplete="off" class="editline" style="width:7%;">&nbsp;&nbsp;版
							</div>
							
							<div class="uploaddiv">
								页&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数：&nbsp;&nbsp;<input type="text" name="book_pagenum" autocomplete="off" class="editline" style="width:7%;">&nbsp;&nbsp;页
							</div>
						
							<div class="uploaddiv">
								字&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数：&nbsp;&nbsp;<input type="text" name="book_wordnum" autocomplete="off" class="editline" style="width:7%;">&nbsp;&nbsp;字
							</div>
						
							<div class="uploaddiv">
								印刷时间：&nbsp;&nbsp;<input type="date" name="book_printdate" class="editline">
							</div>	
						
							<div class="uploaddiv">
								开&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本：&nbsp;&nbsp;<input type="text" name="book_size" autocomplete="off" class="editline" style="width:7%;">&nbsp;&nbsp;开
							</div>
							
							<div class="uploaddiv">
								纸&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;张：&nbsp;&nbsp;
								<select id="" class="selectboxsmall" name="book_paperstyle">
									<option value="胶版纸">胶版纸</option>
									<option value="铜版纸">铜版纸</option>
									<option value="轻型纸">轻型纸</option>
									<option value="纯质纸">纯质纸</option>
								</select>
							</div>
							
							<div class="uploaddiv">
								印&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;次：&nbsp;&nbsp;<input type="text" name="book_printnum" autocomplete="off" class="editline" style="width:7%;">&nbsp;&nbsp;次
							</div>
							
							<div class="uploaddiv">
								包&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;装：&nbsp;&nbsp;
								<select id="" class="selectboxsmall" name="book_package">
									<option value="平装">平装</option>
									<option value="精装">精装</option>
									<option value="盒装">盒装</option>
									<option value="袋装">袋装</option>
									<option value="组合包装">组合包装</option>
								</select>
							</div>
							
							<div class="uploaddiv">
								国际标准书号ISBN：&nbsp;&nbsp;<input type="text" name="book_isbn" autocomplete="off" class="editline">
							</div>
							
							<div class="uploaddiv">
								内容简介：&nbsp;&nbsp;<font style="float:right;">您还可以输入<span class="font_bold_green">500</span>个字</font>
								<textarea class="editbox" style="width:100%;height:100px;" onkeyup="updateLeftStrLength(this)" maxlength="500" placeholder="写好这本书的内容是吸引买家的法宝哦~" name="book_contentsummary"></textarea>
							</div>
						
							<div class="uploaddiv">
								作者简介：&nbsp;&nbsp;<font style="float:right;">您还可以输入<span class="font_bold_green">500</span>个字</font>
								<textarea class="editbox" style="width:100%;height:100px;" onkeyup="updateLeftStrLength(this)" maxlength="500" placeholder="这本书的作者或许能抓住买家的心哦~" name="book_authorsummary"></textarea>
							</div>
							
							<div class="uploaddiv">
								媒体评论：&nbsp;&nbsp;<font style="float:right;">您还可以输入<span class="font_bold_green">500</span>个字</font>
								<textarea class="editbox" style="width:100%;height:100px;" onkeyup="updateLeftStrLength(this)" maxlength="500" placeholder="媒体的评论也左右着买家哦~" name="book_mediacomment"></textarea>
							</div>
							
							<div class="uploaddiv">
								试读章节：&nbsp;&nbsp;<font style="float:right;">您还可以输入<span class="font_bold_green">4000</span>个字</font>
								<textarea class="editbox" style="width:100%;height:100px;" onkeyup="updateLeftStrLength(this)" maxlength="4000" placeholder="给买家瞅瞅这本书中精彩的几章吧~" name="book_tastecontent"></textarea>
							</div>
							
							<div class="uploaddiv">
								<input type="submit" class="greenbutton" value="发布"  onclick="return checkUpLoadInfoValidness();"/>
							</div>
						</div>
						</form>
					</div>
				<%
			}
		%>
	</div>
</div>
<script>


</script>