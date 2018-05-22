package com.dev.DAO;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.dev.bean.Project;

/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*/

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.lang.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class ProjDAO {

	Date sdate=new Date();
	Date edate=new Date();
	Project proj=new Project();
	// ---------JDBC driver name and database URL------------
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:sap://bmh1078076:30515/";// ?autocommit=false",
																// "SAPHAL_ADMIN",
																// "Bosch@1234"
	
	public static final String DATA_SOURCE_NAME = "java:comp/env/jdbc/DefaultDB";

	// ----------Database credentials---------------
	static final String USER = "SAPHAL_ADMIN";
	static final String PASS = "Bosch@1234";
	//private static final Logger LOGGER = LoggerFactory.getLogger(ProjDAO.class);

	private DataSource datasource;
	Connection conn = null;
	
	PreparedStatement pstmt = null;
	
	/*--------------------SQL Statement----------------*/
	String isManagerStatement = "SELECT IS_MGR from \"RB_SCH_REAL\".\"rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_EMPL_M\" WHERE EMPNO = ?";
//	"SELECT IS_MGR from \"RB_SCH_REAL\".\"rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_EMPL_M\" WHERE EMPNO = ?"
	//SELECT IS_MGR INTO LV_IS_MGR FROM "RB_SCH_REAL"."rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_EMPL_M" WHERE EMPNO = :LV_USER;
	
	//String projectCreatestmt ="INSERT INTO \"RB_SCH_REAL\".\"rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_PRJK\"(PRJ_NO,START_DATE,END_DATE,CLIENT_ID,POS_REQ,MRG_NO,PRJ_COST,PRJ_LOC,PRJ_STATUS,PRJ_DES) VALUES (?,?,?,?,?,?,?,?,?,?)";
	String projectCreatestmt ="INSERT INTO \"RB_SCH_REAL\".\"rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_PRJK\" VALUES (?,?,?,?,?,?,?,?,?,?)";
	String getEMP = "SELECT SESSION_USER \"session user\" FROM DUMMY;";
	String deleteStatement 	 =	"DELETE from \"RB_SCH_REAL\".\"rbei.real.i1.data.transaction::RA_DDL_TRANSACTION.T_RA_PRJP\" where PRJ_NO = ?";
	String projectUpdateStatement="INSERT INTO \"RB_SCH_REAL\".\"rbei.real.i1.data.transaction::RA_DDL_TRANSACTION.T_RA_PRJP\" VALUES (?,?,?,?)"; //assigning employees to project
	String statusUpdateStatement="UPDATE \"RB_SCH_REAL\".\"rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_PRJK\" SET PRJ_STATUS=?";
	//"UPDATE \"RB_SCH_REAL\".\"rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_EMPL_M\" SET CONTACT =?, CITY =?,EMAIL=?, PINCODE = ?,HIGH_DEGREE = ?, CV_ATTACHED = ?, PROFILE_ATTACHED = ? WHERE EMPNO =? ";
	//UPDATE "RB_SCH_REAL"."rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_PRJK" SET PRJ_STATUS = 'C' WHERE PRJ_NO = :PRJ_NO; 
	String projectseqstmt="select \"RB_SCH_REAL\".\"rbei.real.i1.data.sequence::projectId\".nextval as \"seq\" from dummy";
	String projecteditstmt="UPDATE \"RB_SCH_REAL\".\"rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_PRJK\" SET PRJ_NO =?, START_DATE =?,END_DATE=?, CLIENT_ID = ?,POS_REQ = ?, MRG_NO = ?, PRJ_COST = ?,PRJ_STATUS=?,PRJ_DES=? WHERE PRJ_NO =? ";
	/*----------------------------------------------------*/
	//String projectCreatestmt ="UPDATE \"RB_SCH_REAL\".\"rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_PRJK\"(?,?,?,?,?,?,?,?,?,?)";
	//String projectCreatestmt = "INSERT INTO \"RB_SCH_REAL\".\"rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_PRJK\"
	/*----------------------------------------------------*/
//	INSERT INTO \"RB_SCH_REAL\".\"rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_PRJK\" VALUES(
//			?,
//			?,
//			''/*START_DATE <DATE>*/,
//			''/*END_DATE <DATE>*/,
//			/*POS_REQ <INTEGER>*/,
//			''/*MRG_NO <NVARCHAR(11)>*/,
//			/*PRJ_COST <INTEGER>*/,
//			''/*PRJ_LOC <NVARCHAR(30)>*/,
//			''/*PRJ_STATUS <NVARCHAR(4)>*/,
//			''/*PRJ_DES <NVARCHAR(200)>*/
//		); 
	/*----------------------------------------------------*/
	// Method to read the DB user
	public String getDBuser() throws SQLException {
		// read the DB user
		String currdbuser = "";
		try {
			pstmt = conn.prepareStatement(getEMP);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				currdbuser = rs.getString("session user");
				//LOGGER.info("Logged in DB user:" + currdbuser);
			}
			return currdbuser;
		} catch (Exception e) {

			//LOGGER.info(e.getMessage());
			return currdbuser;
		}
	}
	
	       public String createProj(Project proj) throws SQLException {
	              //boolean status = false;
	              String estatus="";
	              String ProjectId="";
	              String IS_MGR="";
	              try {
	                     System.out.println("Calling PROJECTDAO to create project" + proj.getLoginUser());
	                     
	                     if(isempDBConnection()){
	                           //is login user is manager-var stmt = 'select IS_MGR from "RB_SCH_REAL"."rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_EMPL_M" where EMPNO = SESSION_USER';
	                    	 String log_user = getDBuser();
	         				//LOGGER.info("User in UpdateEmp:" + log_user);
	         			//System.out.println("EmpNo:" + proj.getEmpNo()+"---log_user:"+log_user);
	         				
	         				//check if the user is Manager
	         				pstmt = conn.prepareStatement(isManagerStatement);
	        				pstmt.setString(1, log_user);
	                        ResultSet rs = pstmt.executeQuery();
	                           
	                           while (rs.next()) {
	                        	   IS_MGR = rs.getString("IS_MGR");
	               			//	LOGGER.info("IS_MGR:" + IS_MGR);
	               				}
	                           if (IS_MGR.equals("3")){
	                        	   try
	                                  {
	                                  convertDate(proj);
	                                  
	                                  if(sdate.before(edate))
	                                  {
	                                  /*stmt = 'insert into "RB_SCH_REAL"."rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_PRJK" (PRJ_NO,START_DATE,END_DATE,CLIENT_ID,POS_REQ,MRG_NO,PRJ_COST,PRJ_LOC,PRJ_STATUS,PRJ_DES) ' + 
	                                   'values (' +  project  + "," + "'" + input.START_DATE + "'" + "," + "'" + input.END_DATE + "'" + "," + "'" + input.CLIENT_ID + "'" + "," + "'" + input.POS_REQ + "'" + "," +
	                                    "'" + input.MRG_NO + "'" + "," + "'" + input.PRJ_COST + "'" + "," + "'" + input.PRJ_LOC + "'" + "," + "'" + 'O' + "'" + "," + "'" + input.PRJ_DES + "'" + ')';*/
	                                         //(PRJ_NO,START_DATE,END_DATE,CLIENT_ID,POS_REQ,MRG_NO,PRJ_COST,PRJ_LOC,PRJ_STATUS,PRJ_DES
	                                         pstmt = conn.prepareStatement(projectseqstmt);
	                                          ResultSet rsseq = pstmt.executeQuery();
	                                         if(rsseq.next()){
	                                        	 ProjectId= rsseq.getString("seq");
	                                                //ProjectId=seqId.toString();
	                                                proj.setProjectNumber(ProjectId.toString());
	                                                System.out.println("seqId is " +rsseq.getInt("seq"));
	                                         }
	                                         System.out.println("seqId is " +rsseq.getInt("seq"));
	                                         System.out.println("Project number is " +proj.getProjectNumber()); 
	                                         System.out.println("CLIENT number is " +proj.getClientId()); 
	                                         pstmt = conn.prepareStatement(projectCreatestmt);
	                                      /*   180,1
	                                     	''CLIENT_ID <NVARCHAR(40)>,2
	                                     	''START_DATE <DATE>,3
	                                     	''END_DATE <DATE>,4
	                                     	POS_REQ <INTEGER>,5
	                                     	''MRG_NO <NVARCHAR(11)>,6
	                                     	PRJ_COST <INTEGER>,7
	                                     	''PRJ_LOC <NVARCHAR(30)>,8
	                                     	''PRJ_STATUS <NVARCHAR(4)>,9
	                                     	''PRJ_DES <NVARCHAR(200)>10
	                                         //PRJ_NO,START_DATE,END_DATE,CLIENT_ID,POS_REQ,MRG_NO,PRJ_COST,PRJ_LOC,PRJ_STATUS,PRJ_DES
*/	                                         pstmt.setString(1, ProjectId);
											pstmt.setString(2, proj.getClientId());//CLIENT_ID
											 pstmt.setString(3, proj.getStartDate());//START_DATE
	                                         pstmt.setString(4, proj.getEndDate());//END_DATE	
	                                         pstmt.setString(5, proj.getPosRequest());//POS_REQ
	                                         pstmt.setString(6,proj.getMrgNumber());//MRG_NO  
	                                         pstmt.setString(7,proj.getProjectcost());//PRJ_COST int
	                                         pstmt.setString(8,proj.getProjectlocation());//PRJ_LOC
	                                         //pstmt.setString(9,proj.getProjectStatus());//PRJ_STATUS default O
	                                         pstmt.setString(9,"O");
	                                         pstmt.setString(10,proj.getProjectDes());//PRJ_DES
	                                         
	                                         
	                                       /*  pstmt.setString(1, ProjectId);//PRJ_NO ---
	                                         pstmt.setString(2, proj.getStartDate());//START_DATE
	                                         pstmt.setString(3, proj.getEndDate());//END_DATE
	                                         pstmt.setString(4, proj.getClientId());//CLIENT_ID
	                                         //pstmt.setString(5,proj.getPosRequest());//POS_REQ int
	                                         pstmt.setInt(5, proj.getPosRequest());//POS_REQ
	                                         pstmt.setString(6,proj.getMrgNumber());//MRG_NO
	                                         //pstmt.setString(7,proj.getProjectcost());//PRJ_COST int
	                                         pstmt.setInt(7,proj.getProjectcost());
	                                         pstmt.setString(8,proj.getProjectlocation());//PRJ_LOC
	                                         //pstmt.setString(9,proj.getProjectStatus());//PRJ_STATUS default O
	                                         pstmt.setString(9,"O");
	                                         pstmt.setString(10,proj.getProjectDes());//PRJ_DES
*/	                                         int i = pstmt.executeUpdate();
											System.out.println("executeUpdate--i:"+i);
	                                         if(i>=0){
	                                                estatus = "200-Sucess";
	                                            	conn.setAutoCommit(false);
	                                                conn.commit();
	                                                System.out.println("Project created  successfully...");
	                                                
	                                                
	                                         }
	                                         else{
	                                                System.out.println("Project creation failed...");
	                                                estatus="DBerror";
	                                         }
	                                  }
	                                  else{
	                                         System.out.println("Start date is greater than End date");
	                                         estatus="date error";
	                                         return estatus;
	                                  }
	                                  }
	                                  catch(ParseException pe)
	                                  {
	                                	  System.out.println(pe.getMessage());
	                                         pe.printStackTrace();
	                                  }
	                        	   
	                           }else{
	                        	   return "Unauthorised";
	                           }
	                                 
	                           //}
	                     }

	              }
	              catch(SQLException se)
	              {
	                     se.printStackTrace();
	                     estatus = se.getMessage();
	                     return estatus;
	              }
	              return estatus;
	       }
	       /*****************************************************Project Edit****************************************************/
	       public String editProj(Project proj) throws SQLException {
	              
	              String estatus="";
	              String ProjectId="";
	              String IS_MGR="";
	              try {
	                     System.out.println("Calling PROJECTDAO " + proj.getProjectNumber());
	                     
	                     if(isempDBConnection()){
	                          
	                    	 String log_user = getDBuser();
	         				//LOGGER.info("User in UpdateEmp:" + log_user);
	         			//System.out.println("EmpNo:" + proj.getEmpNo()+"---log_user:"+log_user);
	         				
	         				//check if the user is Manager
	         				pstmt = conn.prepareStatement(isManagerStatement);
	        				pstmt.setString(1, log_user);
	                        ResultSet rs = pstmt.executeQuery();
	                           
	                           while (rs.next()) {
	                        	   IS_MGR = rs.getString("IS_MGR");
	               				//LOGGER.info("IS_MGR:" + IS_MGR);
	               				}
	                           if (IS_MGR.equals("3")){
	                        	   try
	                                  {
	                                  convertDate(proj);
	                                  
	                                  if(sdate.before(edate))
	                                  {
	                 //PRJ_NO =?, START_DATE =?,END_DATE=?, CLIENT_ID = ?,POS_REQ = ?, MRG_NO = ?, PRJ_COST = ?,PRJ_STATUS=?,PRJ_DES=? WHERE PRJ_NO =? 
	                 //PRJ_NO,START_DATE,END_DATE,CLIENT_ID,POS_REQ,MRG_NO,PRJ_COST,PRJ_LOC,PRJ_STATUS,PRJ_D
//"UPDATE \"RB_SCH_REAL\".\"rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_PRJK\" SET PRJ_NO =?, START_DATE =?,END_DATE=?, CLIENT_ID = ?,POS_REQ = ?, MRG_NO = ?, PRJ_COST = ?,PRJ_LOC=?,PRJ_STATUS=?,PRJ_DES=? WHERE PRJ_NO =? ";	                                	  
	                                	  pstmt = conn.prepareStatement(projecteditstmt);
	                                         pstmt.setString(1, proj.getProjectNumber());
											//
											 pstmt.setString(2, proj.getStartDate());//START_DATE
	                                         pstmt.setString(3, proj.getEndDate());//END_DATE
	                                         pstmt.setString(4, proj.getClientId());//CLIENT_ID
	                                         pstmt.setString(5, proj.getPosRequest());//POS_REQ
	                                         pstmt.setString(6,proj.getMrgNumber());//MRG_NO  
	                                         pstmt.setString(7,proj.getProjectcost());//PRJ_COST int
	                                        pstmt.setString(8,proj.getProjectlocation());//PRJ_LOC
	                                         pstmt.setString(9,proj.getProjectStatus());//PRJ_STATUS 
	                                         pstmt.setString(10,proj.getProjectDes());//PRJ_DES
	                                         pstmt.setString(11, proj.getProjectNumber());
                                             int i = pstmt.executeUpdate();
											
	                                         if(i>=0){
	                                                estatus = "200-Sucess";
	                                            	conn.setAutoCommit(false);
	                                                conn.commit();
	                                                System.out.println("Project Edit  successfully...");
	                                                
	                                                
	                                         }
	                                         else{
	                                                System.out.println("Project Edit failed...");
	                                                estatus="DBerror";
	                                         }
	                                  }
	                                  else{
	                                         System.out.println("Start date is greater than End date");
	                                         estatus="date error";
	                                         return estatus;
	                                  }
	                                  }
	                                  catch(ParseException pe)
	                                  {
	                                	  System.out.println(pe.getMessage());
	                                         pe.printStackTrace();
	                                  }
	                        	   
	                           }else{
	                        	   return estatus="Unauthorised";
	                           }
	                                 
	                           //}
	                     }

	              }
	              catch(SQLException se)
	              {
	                     se.printStackTrace();
	                     estatus = se.getMessage();
	                     return estatus;
	              }
	              return estatus;
	       }
	
	/************************Project Update**************************************/
	public String updateProj(Project proj,boolean is_deleted) throws SQLException {
		String estatus = "";

		try {
			System.out.println("Calling PROJECTDAO to update project" + proj.getLoginUser());
			
			if(isempDBConnection()){
				//is login user is manager-var stmt = 'select IS_MGR from "RB_SCH_REAL"."rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_EMPL_M" where EMPNO = SESSION_USER';
				/*pstmt = conn.prepareStatement(isManagerStatement);
				pstmt.setString(1, proj.getLoginUser());// LoginUserId
				
				ResultSet rs = pstmt.executeQuery();
				if (rs.next() == false) {
					System.out.println("Login employee is not authorized to update Project details");
					
				} else {*/
					
									
					if(proj.getProjectNumber()!=null){
						//Delete project details
						//'delete from "RB_SCH_REAL"."rbei.real.i1.data.transaction::RA_DDL_TRANSACTION.T_RA_PRJP" where PRJ_NO = ?';
						if(!is_deleted)	{
							
						pstmt = conn.prepareStatement(deleteStatement);
						pstmt.setString(1, proj.getProjectNumber());
						int i = pstmt.executeUpdate();
						System.out.println("Deleted");
						}
						
					/*}
					if(proj.getEmpNumber()!=null){*/
						
													
						//stmt = 'INSERT INTO "RB_SCH_REAL"."rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_EMPL_S" VALUES (?,?,?,?)';
						//Update Skill of employee
						pstmt = conn.prepareStatement(projectUpdateStatement);
						pstmt.setString(1, proj.getProjectNumber());
						pstmt.setString(2, proj.getEmpNumber());
						pstmt.setString(3, proj.getStartDate());
						pstmt.setString(4, proj.getEndDate());
						int j = pstmt.executeUpdate();
						if(j>0){
							estatus = "200-Sucess";
							System.out.println("Project update successfully...");
							conn.setAutoCommit(false);
							conn.commit();
							return estatus;
						}
						else{
							System.out.println("400-Fail");
							estatus="DBerror";
			                 return estatus;
						}
					}
					
					
					/*}
					else{
						System.out.println("Start date is greater than End date");
						return status;
					}
					}
					catch(ParseException pe)
					{
						pe.printStackTrace();
					}*/
				//}
			}
			else{
				 estatus="400-Fail";
                 return estatus;
			}
		}
		catch(SQLException se)
		{
			se.printStackTrace();
			System.out.println("EXCEPTION==="+se.getMessage());
			estatus = se.getMessage();
		}
		return estatus;
	}
	/************************Status Update**************************************/
	public String updateStatus(Project proj) throws SQLException {
		String estatus = "";
		String mgrId="";

		try {
			System.out.println("Calling PROJECTDAO to update project" + proj.getLoginUser());
			
			if(isempDBConnection()){
				
				//hdbconn.loadProcedure("RB_SCH_REAL","rbei.real.i1.data.procedures::projectupdation");
				/*
				 SELECT SESSION_USER INTO LV_USER FROM DUMMY;
    SELECT IS_MGR INTO LV_IS_MGR FROM "RB_SCH_REAL"."rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_EMPL_M" WHERE EMPNO = :LV_USER;
    IF LV_IS_MGR = '1' THEN
        UPDATE "RB_SCH_REAL"."rbei.real.i1.data.master.attributes::RA_DDL_ATTRIBUTES.T_RA_PRJK" SET PRJ_STATUS = 'C' WHERE PRJ_NO = :PRJ_NO; 
				 */
				
				pstmt = conn.prepareStatement(isManagerStatement);
				pstmt.setString(1, proj.getLoginUser());// LoginUserId
				ResultSet rs = pstmt.executeQuery();
				
				if (rs.next() == false) {
					System.out.println("Login employee is not authorized to update Project status");
					estatus="notauthorized";
					return estatus;
				}
				else if(rs.next()){
					mgrId= rs.getString("IS_MGR");
				}
					if(mgrId.equalsIgnoreCase("1"))
					{
					pstmt = conn.prepareStatement(statusUpdateStatement);
					pstmt.setString(1, proj.getProjectNumber());
					int i = pstmt.executeUpdate();
					if(i>0){
						estatus = "200-Sucess";
						System.out.println("Project Status update successfully...");
						conn.commit();
					}
					else{
						System.out.println("Project Status Update failed...");
						estatus="DBerror";
		                 return estatus;
					}
				}
				
				
		}
		}
		catch(SQLException se)
		{
			se.printStackTrace();
			estatus = se.getMessage();
			return estatus;
		}
		return estatus;
	}
	public boolean isempDBConnection() throws SQLException {
		
		boolean dbStatus=false;
		
					try {
						// ---------------Register JDBC driver-----------
						/*Class.forName("com.mysql.jdbc.Driver");
						// ------------Open a connection-----------------
						conn = DriverManager.getConnection("jdbc:sap://bmh1078076:30515/?autocommit=false", "SAPHAL_ADMIN",
								"Bosch@1234");*/
						 InitialContext ctx = new InitialContext();
                         DataSource dataSource = (DataSource) ctx.lookup(DATA_SOURCE_NAME);
                         conn = dataSource.getConnection();
                         System.out.println("success");
                         dbStatus = true;
						System.out.println("Connected database successfully...");
					} catch (SQLException se) {
						// Handle errors for JDBC
						se.printStackTrace();
						 return dbStatus;
					}
					catch(Exception e)
					{
					e.printStackTrace();
					}
		return dbStatus;
	}
	public void convertDate(Project proj)throws ParseException{
		//convertDate();
		try{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		sdate = format.parse(proj.getStartDate());
		edate = format.parse(proj.getEndDate());
		}
		catch(ParseException pe)
		{
			pe.printStackTrace();
		}
		
	}
	
}