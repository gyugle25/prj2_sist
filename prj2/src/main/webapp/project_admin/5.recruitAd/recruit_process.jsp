<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="java.io.File"%>
<%@page import="java.sql.Date"%>
<%@page import="kr.co.sist.admin.vo.JobAdNewVO"%>
<%@page import="java.sql.SQLException"%>
<%@page import="kr.co.sist.admin.dao.JobAdDAO"%>
<%@page import="kr.co.sist.util.cipher.DataEncrypt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="check_login.jsp" %>

<%
request.setCharacterEncoding("utf-8");
%>

<% 
JobAdNewVO jVO=new JobAdNewVO();
%>

<% 
// 1. 저장 디렉토리를 설정 
//File saveDirectory=new File("E:/dev/workspace/prj2/src/main/webapp/upload/");
File saveDirectory=new File("C:/Users/Kyum/git/prj2_sist/prj2/src/main/webapp/upload");
int totalMaxSize = 1024*1024*500;
int fileSize=1024*1024*10;

// 2. FileUpload Component 생성 (MultipartRequest) => 생성과 동시에 파일이 업로드
MultipartRequest mr= new MultipartRequest(request, saveDirectory.getAbsolutePath(), totalMaxSize, "UTF-8", 
		new DefaultFileRenamePolicy());

// 3. FileUpload Component를 사용하여 파라미터를 받음
String originalName = mr.getOriginalFileName("file"); 
String newFileName = mr.getFilesystemName("file");	


if(newFileName==null){ 
	jVO.setDescription(mr.getParameter("description")); 
}else{			
	jVO.setDescription(newFileName);
}//end else
	

jVO.setcName(mr.getParameter("company"));
jVO.setTitle(mr.getParameter("title"));
jVO.setCareer(mr.getParameter("career"));
jVO.setEducation(mr.getParameter("education"));
jVO.setEmpform(mr.getParameter("empform"));
jVO.setSal(mr.getParameter("sal"));
jVO.setArea(mr.getParameter("area"));
jVO.setWorkHours(mr.getParameter("workHours"));
jVO.setRecruitField(mr.getParameter("recruitField"));
jVO.setRecruitPeople(Integer.parseInt(mr.getParameter("recruitPeople")));
jVO.setStartDate(Date.valueOf(mr.getParameter("startDate")));
jVO.setEndDate(Date.valueOf(mr.getParameter("endDate")));

String cName = mr.getParameter("company");
%>



<%
JobAdDAO jDAO=new JobAdDAO();

try{
jDAO.insertAd(jVO);
//인서트 성공
%>
<script>
	alert("채용 공고가 등록되었습니다");
	location.href="http://localhost/prj2/project_admin/3.recruitAdmin/job_posting.jsp"; 
						//채용공고 지원자 현황 페이지
</script>
<%
}catch(SQLException se){
	se.printStackTrace();
 }//end catch %>  


