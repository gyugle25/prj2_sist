<%@page import="java.net.URLEncoder"%>
<%@page import="java.sql.SQLException"%>
<%@page import="kr.co.sist.admin.dao.JobAdDAO"%>
<%@page import="kr.co.sist.admin.vo.JobAdNewVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    info="기업명 조회 (숨겨진 div 화면에 보여주기)"
    %>
<%@ include file="check_login.jsp" %>
<%
request.setCharacterEncoding("utf-8");
JobAdDAO jDAO = new JobAdDAO();

String cName = request.getParameter("cName");
Boolean resultFlag = false; 


try{
	resultFlag = jDAO.selectFindcName(cName); 

	if(resultFlag==false){
	%>
	<script>
		alert("조회할 수 없는 회사명입니다. 다시 입력해주세요.");
		location.href="http://localhost/prj2/project_admin/5.recruitAd/recruit.jsp";
	</script>
	<%
	}else{

	// 기업등록이 되어 있는 상태라면 아래를 실행
	String location="http://localhost/prj2/project_admin/5.recruitAd/recruit.jsp?flag=y&name="+URLEncoder.encode(cName, "UTF-8");
	response.sendRedirect(location);
	
	}//end else
	
}catch(SQLException se){
	se.printStackTrace();
}//end catch
%>

 
