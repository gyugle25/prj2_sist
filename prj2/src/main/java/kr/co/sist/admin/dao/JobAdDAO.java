package kr.co.sist.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.sist.admin.vo.JobAdNewVO;
import kr.co.sist.admin.vo.ModifyAdVO;
import kr.co.sist.admin.vo.SearchAdVO;
import kr.co.sist.dao.DbConnection;

public class JobAdDAO {

	
	
	
	
		//모든 공고 수
		public int selectCntAll() throws SQLException {
			int result=0;
			
			DbConnection db=DbConnection.getInstance();
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			
			try {
				con=db.getConn();
				
			
				StringBuilder selectCntAds = new StringBuilder();
				selectCntAds.append("SELECT COUNT(*) ")
			        .append("FROM job_ad ")
			        .append("where trunc(end_date) >= trunc(sysdate) ");
			
			
				pstmt=con.prepareStatement(selectCntAds.toString());
				rs=pstmt.executeQuery();
				
				 if (rs.next()) {
				      result = rs.getInt(1); //1占쏙옙째 占쏙옙
				    }//end if
				
				 
			}finally {
				db.dbClose(rs, pstmt, con);
			
			}//end finally
			
				 return result;
			
		}//selectCntAll

	
	
	
	//공고 중인 기업 수
	public int selectCountAdCorp() throws SQLException {
		int result=0;
		
		DbConnection db=DbConnection.getInstance();
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			con=db.getConn();
			
		
			StringBuilder selectCountCorp = new StringBuilder();
			selectCountCorp.append("SELECT COUNT(DISTINCT name) ")
		        .append("FROM job_ad ")
		        .append("WHERE trunc(end_date) >= trunc(sysdate)");
			
			
		
			pstmt=con.prepareStatement(selectCountCorp.toString());
			rs=pstmt.executeQuery();
			
			 if (rs.next()) {
			      result = rs.getInt(1); 
			    }//end if
			
			 
		}finally {
			db.dbClose(rs, pstmt, con);
		
		}//end finally
		
			 return result;
		
		
	}//selectCountAdCorp

	
	
	
	//오늘 마감 공고 수
	public int selectCntclosing() throws SQLException {
		int result=0;
		
		
		DbConnection db=DbConnection.getInstance();
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			con=db.getConn();
			
			StringBuilder selectClosingAd = new StringBuilder();
			selectClosingAd.append("SELECT COUNT(*) ")
			    .append("FROM job_ad ")
			    .append("where trunc(end_date) = trunc(sysdate)");
		
				pstmt=con.prepareStatement(selectClosingAd.toString());
				rs=pstmt.executeQuery();
			
				if (rs.next()) {
				      result = rs.getInt(1); 
				    }//end if
				
			}finally {
				db.dbClose(rs, pstmt, con);
			}//end finally
		
			return result;
	}//selectCntclosing

	
	
	//마감된 공고 수
	public int selectCntClosed() throws SQLException {
		int result=0;
		
		DbConnection db=DbConnection.getInstance();
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			con=db.getConn();
			
		
			StringBuilder selectClosedAd = new StringBuilder();
			selectClosedAd.append("SELECT count(*) ")
		        .append("FROM job_ad ")
		        .append("WHERE trunc(END_DATE) < trunc(sysdate)	");
		
		
			pstmt=con.prepareStatement(selectClosedAd.toString());
			rs=pstmt.executeQuery();
			
			 if (rs.next()) {
			      result = rs.getInt(1); 
			    }//end if
			
			 
		}finally {
			db.dbClose(rs, pstmt, con);
		
		}//end finally
		
			 return result;
		
		
	}//selectCntClosed
	
	
	
	   
	   //공고 리스트
	   public List<SearchAdVO> selectAd(String cName) throws SQLException {
	      
	      List<SearchAdVO> list=new ArrayList<SearchAdVO>();
	      
	      DbConnection db=DbConnection.getInstance();
	      Connection con=null;
	      PreparedStatement pstmt=null;
	      ResultSet rs=null;
	      
	      try {
	         con=db.getConn();
	         
	         
	         StringBuilder selectAd = new StringBuilder();
	         selectAd.append("SELECT job_ad.JOB_NUM, name, TITLE, INPUT_DATE, END_DATE, COUNT(CASE WHEN apply.cancel = 'NC' THEN user_id END) as TOTAL_APPLY ")
	             .append("FROM job_ad ")
	             .append("LEFT OUTER JOIN apply ON job_ad.job_num = apply.job_num ")
	             .append("WHERE trunc(end_date) >= trunc(sysdate) ");

	         
	         //회사명이 입력된 경우에는 조건 추가
	         if (!cName.isEmpty()) {
	             selectAd.append("AND name = ? ");
	         }

	         
	         selectAd.append("GROUP BY job_ad.JOB_NUM, name, TITLE, INPUT_DATE, END_DATE   ")
	             .append("ORDER BY trunc(end_date)");

	         
	         pstmt = con.prepareStatement(selectAd.toString());
	         
	         
	         //회사명이 입력된 경우 바인드 변수 설정
	         if (!cName.isEmpty()) {
	             pstmt.setString(1, cName);
	         }

	         
	         rs = pstmt.executeQuery();

	         
	         SearchAdVO sVO = null;
	         while (rs.next()) {
	             sVO = new SearchAdVO();
	             sVO.setJobNum(rs.getInt("JOB_NUM"));
	             sVO.setcName(rs.getString("name"));
	             sVO.setTitle(rs.getString("TITLE"));
	             sVO.setTotalApply(rs.getInt("TOTAL_APPLY"));
	             sVO.setInputDate(rs.getDate("INPUT_DATE"));
	             sVO.setEndDate(rs.getDate("END_DATE"));
	             list.add(sVO);
	         }

	     } finally {
	         db.dbClose(rs, pstmt, con);
	     }

	     return list;
	      
	   }//selectAd
	   
	   
	   
	   
	   	//마감된 공고 리스트
	      public List<SearchAdVO> selectClosedAd(String cName2) throws SQLException {
	         
	         List<SearchAdVO> list=new ArrayList<SearchAdVO>();
	         
	         DbConnection db=DbConnection.getInstance();
	         Connection con=null;
	         PreparedStatement pstmt=null;
	         ResultSet rs=null;
	         
				try {
					con = db.getConn();

					StringBuilder selectQuery = new StringBuilder();
					selectQuery.append(
							"SELECT job_ad.JOB_NUM, name, TITLE, INPUT_DATE, END_DATE, COUNT(CASE WHEN apply.cancel = 'NC' THEN user_id END) as TOTAL_APPLY ")
							.append("FROM job_ad ").append("LEFT OUTER JOIN apply ON job_ad.job_num = apply.job_num ");

					if (cName2.isEmpty()) { // 회사명이 없는 경우
						selectQuery.append("WHERE trunc(END_DATE) >= trunc(sysdate) ");
					} else {
						selectQuery.append("WHERE name = ? AND trunc(END_DATE) >= trunc(sysdate) ");
					} // end if

					selectQuery.append("GROUP BY job_ad.JOB_NUM, name, TITLE, INPUT_DATE, END_DATE   ")
							.append("ORDER BY trunc(end_date)");

					pstmt = con.prepareStatement(selectQuery.toString());

					
					//회사명이 있는 경우 바인드 변수 설정
					if (!cName2.isEmpty()) {
						pstmt.setString(1, cName2);
					}

					rs = pstmt.executeQuery();

					SearchAdVO sVO = null;
					while (rs.next()) {
						sVO = new SearchAdVO();
						sVO.setJobNum(rs.getInt("JOB_NUM"));
						sVO.setcName(rs.getString("name"));
						sVO.setTitle(rs.getString("TITLE"));
						sVO.setInputDate(rs.getDate("INPUT_DATE"));
						sVO.setEndDate(rs.getDate("END_DATE"));
						sVO.setTotalApply(rs.getInt("TOTAL_APPLY"));
						list.add(sVO);
					}
				} finally {
					db.dbClose(rs, pstmt, con);
				} // end finally

				return list;

			}// selectAd
	   
	
	
	      //기업명으로 등록 여부 조회
		   public boolean selectFindcName(String cName)throws SQLException{
		      boolean resultFlag=false;
		      
		      DbConnection dbCon=DbConnection.getInstance();
		      Connection con=null;
		      PreparedStatement pstmt=null;
		      
		   try {   
		      con=dbCon.getConn();
		      
		      StringBuilder sb=new StringBuilder();
		      sb
		      .append(" select name    ")
		      .append(" from   corp    ")
		      .append(" where  name= ? "); 
		      
		      pstmt=con.prepareStatement(sb.toString());
		      
		      pstmt.setString(1, cName);
		      
		      
		      int result=pstmt.executeUpdate();
		      
		      resultFlag=result > 0;
		      
		   }finally {
		      dbCon.dbClose(null, pstmt, con);
		   }//end finally
		   
		   return resultFlag;
		}//selectFindcName
		  
		   
		   //공고 등록하기
		   public void insertAd (JobAdNewVO jVO) throws SQLException{

		         DbConnection dbCon=DbConnection.getInstance();
		         Connection con=null;
		         PreparedStatement pstmt=null;
		         
		      try {   
		         con=dbCon.getConn();
		         StringBuilder sb=new StringBuilder();
		         
		         sb
		         .append(" insert into job_ad (name, title, career, education, empform, sal, area, "
		               + "work_hours, recruit_field, description, job_num, recruit_people,"
		               + "start_date, end_date, input_date) ")
		         .append("values(?,?,?,?,?,?,?,?,?,?,seq_job.nextval,?,?,?,sysdate)" );
		         
		         pstmt=con.prepareStatement(sb.toString());
		         
		         
		         pstmt.setString(1, jVO.getcName());
		         pstmt.setString(2, jVO.getTitle());
		         pstmt.setString(3, jVO.getCareer());
		         pstmt.setString(4, jVO.getEducation());
		         pstmt.setString(5, jVO.getEmpform());
		         pstmt.setString(6, jVO.getSal());
		         pstmt.setString(7, jVO.getArea());
		         pstmt.setString(8, jVO.getWorkHours());
		         pstmt.setString(9, jVO.getRecruitField());
		         pstmt.setString(10, jVO.getDescription());
		         pstmt.setInt(11, jVO.getRecruitPeople());
		         pstmt.setDate(12, jVO.getStartDate());
		         pstmt.setDate(13, jVO.getEndDate());
		         
		         pstmt.executeUpdate();
		         
		      }finally {
		         dbCon.dbClose(null, pstmt, con);
		      }//end finally
		      
		   }//insertAd
	
	
	
		   //공고 내용 불러오기
	public ModifyAdVO selectAdContent(int jobNum) throws SQLException {

		ModifyAdVO mVO=null;

		
		DbConnection db=DbConnection.getInstance();
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			con=db.getConn();
		
		
		StringBuilder selectAdContent = new StringBuilder();
		selectAdContent.append("SELECT JOB_NUM, name, TITLE, CAREER, EDUCATION, EMPFORM, SAL, AREA, WORK_HOURS, START_DATE, END_DATE, RECRUIT_FIELD, RECRUIT_PEOPLE, DESCRIPTION ")
        .append("FROM JOB_AD ")
        .append("WHERE JOB_NUM = ?");

	    pstmt = con.prepareStatement(selectAdContent.toString());
	    pstmt.setInt(1, jobNum);

	    rs = pstmt.executeQuery();

		
		if (rs.next()) {
			mVO = new ModifyAdVO();
			mVO.setJobNum(rs.getInt("JOB_NUM"));
			mVO.setcName(rs.getString("name"));
			mVO.setTitle(rs.getString("TITLE"));
			mVO.setCareer(rs.getString("CAREER"));
			mVO.setEducation(rs.getString("EDUCATION"));
			mVO.setEmpform(rs.getString("EMPFORM"));
			mVO.setSal(rs.getString("SAL"));
			mVO.setArea(rs.getString("AREA"));
			mVO.setWorkHours(rs.getString("WORK_HOURS"));
			mVO.setStartDate(rs.getDate("START_DATE"));
			mVO.setEndDate(rs.getDate("END_DATE"));
			mVO.setRecruitField(rs.getString("RECRUIT_FIELD")); // 占쏙옙占쏙옙占쏙옙 占싸븝옙
			mVO.setRecruitPeople(rs.getInt("RECRUIT_PEOPLE"));
			mVO.setDescription(rs.getString("DESCRIPTION"));
			}
		
		}finally {
			db.dbClose(rs, pstmt, con);
			
		}//end finally
		
		
		return mVO;
		
	}//selectAdContent
	
	
	
	
	//공고 업데이트
	public int updateAd(ModifyAdVO mVO) throws SQLException {
		
		int cnt=0;
		
		DbConnection db=DbConnection.getInstance();
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			con=db.getConn();
		
			
			StringBuilder updateAdContent = new StringBuilder();
			updateAdContent.append("UPDATE JOB_AD ")
			        .append("SET TITLE=?, CAREER=?, EDUCATION=?, DESCRIPTION=?, EMPFORM=?, SAL=?, AREA=?, WORK_HOURS=?, START_DATE=?, END_DATE=?, RECRUIT_FIELD=?, RECRUIT_PEOPLE=? ")
			        .append("WHERE JOB_NUM = ?");

			pstmt = con.prepareStatement(updateAdContent.toString());
			
			pstmt.setString(1, mVO.getTitle());
			pstmt.setString(2, mVO.getCareer());
			pstmt.setString(3, mVO.getEducation());
			pstmt.setString(4, mVO.getDescription());
			pstmt.setString(5, mVO.getEmpform());
			pstmt.setString(6, mVO.getSal());
			pstmt.setString(7, mVO.getArea());
			pstmt.setString(8, mVO.getWorkHours());
			pstmt.setDate(9, mVO.getStartDate());
			pstmt.setDate(10, mVO.getEndDate());
			pstmt.setString(11, mVO.getRecruitField());
			pstmt.setInt(12, mVO.getRecruitPeople());
			pstmt.setInt(13, mVO.getJobNum());

			cnt = pstmt.executeUpdate();
			
			
		}finally {
			db.dbClose(rs, pstmt, con);
		}//end finally
		
		return cnt; 
		
	}//updateAd

	
	
	
	//공고 삭제하기
	public int deleteAd(int jobNum) throws SQLException {
		
		int cnt = 0;
		DbConnection db=DbConnection.getInstance();
		Connection con=null;
		PreparedStatement pstmt=null;

		try {
			
			con=db.getConn();
			
			String deleteAd=" delete from job_ad where job_num=?";
			pstmt=con.prepareStatement(deleteAd);
			
			pstmt.setInt(1, jobNum);
			cnt=pstmt.executeUpdate();
			
		}finally {
			db.dbClose(null, pstmt, con);
		}//end finally
		
		return cnt; 
	}//deleteAd
	
	
	

	
}//class
