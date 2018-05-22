package com.dev.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import com.dev.bean.Project;
import com.dev.DAO.ProjDAO;

/**
 * Servlet implementation class PrjMain
 */
@WebServlet("/PrjMain")
public class PrjMain extends HttpServlet {
ProjDAO projdao=new ProjDAO();
	
	//private static final long serialVersionUID = 1L;
       
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			Project proj=new Project();
			HttpSession session = request.getSession(true);
			String userId = (String)session.getAttribute("Username");//loginId
			proj.setLoginUser(userId);
			System.out.println("Login userId..." +userId);
			// String input=request.getParameter("UPDATE");
			if (request.getParameter("CMD").equalsIgnoreCase("PRJ_CREATE"))

			{
				projectCreate(request, response);
			}
			
			else if (request.getParameter("CMD").equalsIgnoreCase("PRJ_EDIT"))

			{
				projectEdit(request, response);
			}
			
			else if (request.getParameter("CMD").equalsIgnoreCase("PRJ_UPDATE"))

			{
				projectUpdate(request, response);
			}
			else if (request.getParameter("CMD").equalsIgnoreCase("STATUS_UPDATE"))

			{
				statustUpdate(request, response);
			}
			
			else {
				System.out.println("No CMD");
				response.getWriter().println("Invalid Command");
				
				}
		} catch (Exception e) {
			response.getWriter().println("ProjectController failed with reason" + e.getMessage());
			

		}
	}

	// Project Create
    public void projectCreate(HttpServletRequest request, HttpServletResponse response) throws SQLException {

                // Create Project table
                String estatus="";
                Project proj=new Project();
                try {
                            
                            proj=getandparsejson(request, response);
                            
                            if(proj==null)
                            {
                                        
                                        response.getWriter().println("ProjectCreation-No input data found to update " );
                            }
                            else{
                                        
                                        //if(projdao.createProj(proj))
                                        estatus=projdao.createProj(proj);
                                        if (estatus.equalsIgnoreCase("200-Sucess"))
                                        {
                                                    response.getWriter().println("Project creation done successfully");
                                                    response.setStatus(200);
                                        }
                                        else if(estatus.equalsIgnoreCase("date error"))
                                        
                                        {
                                                    response.getWriter().println("Start date is greater than End date");
                                                    response.setStatus(400);
                                                    
                                        }
                                        else
                                        {
                                                    response.getWriter().println("Project creation failed");
                                                    response.getWriter().println(estatus);
                                                    response.setStatus(400);
                                        }
                            }
                            } catch (Exception e) {
                                        e.getMessage();

                            }

                }
 // Project Edit
    public void projectEdit(HttpServletRequest request, HttpServletResponse response) throws SQLException {

                // Edit Project table
                String estatus="";
                Project proj=new Project();
                try {
                            
                            proj=getandparsejson(request, response);
                            
                            if(proj==null)
                            {
                                        
                                        response.getWriter().println("ProjectEdit-No input data found to update " );
                            }
                            else{
                                      if(proj.getProjectNumber()!=null)  
											{ 
                                        estatus=projdao.editProj(proj);
                                        if (estatus.equalsIgnoreCase("200-Sucess"))
                                        {
                                                    response.getWriter().println("Project edit done successfully");
                                                    response.setStatus(200);
                                        }
                                        else if(estatus.equalsIgnoreCase("date error"))
                                        
                                        {
                                                    response.getWriter().println("Start date is greater than End date");
                                                    response.setStatus(400);
                                                    
                                        }
                                        else if(estatus.equalsIgnoreCase("Unauthorised"))
                                            
                                        {
                                                    response.getWriter().println("Unauthorized to edit Project");
                                                    response.setStatus(400);
                                                    
                                        }
                                        else
                                        {
                                                    response.getWriter().println("Project edit failed" +estatus);
                                                    response.setStatus(400);
                                        }
                            }
                                      else{
                                    	  response.getWriter().println("Project Number is mandatory to edit project");
                                          response.setStatus(400);
                                      }
                            }
                            } catch (Exception e) {
                                        e.getMessage();

                            }

                }
		// Project Update
				public void projectUpdate(HttpServletRequest request, HttpServletResponse response) throws SQLException {

					// Update Project table
					List<Project>  proj = null;
					String estatus="";
					
					try {
						
						//proj=getandparsejson(request, response,proj);
						proj=processGivenData(request,response);
						
						if(proj==null)
						{
							
							response.getWriter().println("ProjectUpdate-No input data found to update " );
						}
						else{
							boolean is_deleted = false;
							for(Project pro :proj){
								estatus=projdao.updateProj(pro,is_deleted);
								is_deleted=true;
								
									
							}
							
							if (estatus.equalsIgnoreCase("200-Sucess"))
							{
								response.getWriter().println("Project Updated successfully" );
								response.setStatus(200);
							}
							else if (estatus.equalsIgnoreCase("400-Fail"))
							{
							response.getWriter().println("Project update failed-database connection Fail .kindly contact system admin" );
							response.setStatus(400);
							}
							else{
								response.getWriter().println(estatus);
								 response.setStatus(400);
							}
						}
						} catch (Exception e) {
							e.getMessage();

						}

					}
				// Status Update
				public void statustUpdate(HttpServletRequest request, HttpServletResponse response) throws SQLException {

					// Update status
					Project proj=new Project();
					String estatus="";
					try {
						
						proj=getandparsejson(request, response);
						
						if(proj==null)
						{
							
							response.getWriter().println("UpdateStatus-No input data found to update " );
						}
						else{
							
							estatus=projdao.updateStatus(proj);
							
							if (estatus.equalsIgnoreCase("200-Sucess"))
							{
								response.getWriter().println("Project Status Update done successfully");
								response.setStatus(200);
							}
							else if(estatus.equalsIgnoreCase("notauthorized"))
							{
								response.getWriter().println("Login employee is not authorized to update Project status");
								response.setStatus(400);
							}
							
							else
							{
								response.getWriter().println("Status update failed-database connection Fail");
								response.getWriter().println(estatus);
								response.setStatus(400);
							}
						}
						} catch (Exception e) {
							e.getMessage();

						}

					}
		//Get json data and convert to object-Employee
		public Project getandparsejson(HttpServletRequest request, HttpServletResponse response) throws SQLException{
			Project proj=new Project();
			try {
				
				BufferedReader reader = request.getReader();
				StringBuilder sb = new StringBuilder();
				String line = reader.readLine();
				JSONObject obj = new JSONObject();

				while (line != null) {
					sb.append(line + "\n");
					line = reader.readLine();
				}
				reader.close();
				String params = sb.toString();
				String[] _params = params.split("&");
				String jsondata = null;
				for (String param : _params) {
					System.out.println("params(POST)-->" + param);
					jsondata = param.toString();
				}
				System.out.println("jsondata is -------" + jsondata);
				// Convert json string to java object
				try {
					JSONParser parser = new JSONParser();
					obj = (JSONObject) parser.parse(jsondata);
					/******************************Create Project/Edit Project******************************************/
					if(request.getParameter("CMD").equalsIgnoreCase("PRJ_CREATE")||(request.getParameter("CMD").equalsIgnoreCase("PRJ_EDIT")))
						
					{
						String projectNumber=String.valueOf(obj.get("PRJ_NO"));
						String startDate = String.valueOf(obj.get("START_DATE"));
                        String endDate = String.valueOf(obj.get("END_DATE"));
                        String posReq = String.valueOf(obj.get("POS_REQ"));//int
                      //  Integer posReq=Integer.getInteger(obj.get("POS_REQ").toString());
                        
                        String mrgNo = String.valueOf(obj.get("MRG_NO"));
                        String prjCost = String.valueOf(obj.get("PRJ_COST"));//int
                       // Integer prjCost=Integer.getInteger(obj.get("PRJ_COST").toString());
                        String prjloc = String.valueOf(obj.get("PRJ_LOC"));
                        String prjstatus=String.valueOf(obj.get("PRJ_STATUS"));
                        String prjDes = String.valueOf(obj.get("PRJ_DES"));
                        String clientId = String.valueOf(obj.get("CLIENT_ID"));
                        
                        proj.setProjectNumber(projectNumber);
                        proj.setStartDate(startDate);
                        proj.setEndDate(endDate);
                        proj.setPosRequest(posReq);
                        proj.setMrgNumber(mrgNo);
                        proj.setProjectcost(prjCost);
                        proj.setProjectlocation(prjloc);
                        proj.setProjectDes(prjDes);
                        proj.setClientId(clientId);
                        proj.setProjectStatus(prjstatus);
                        return proj;

					}
					
					/******************************UPDATE Status******************************************/
					if(request.getParameter("CMD").equalsIgnoreCase("STATUS_UPDATE"))
						
					{			
											
						String projNo = String.valueOf(obj.get("PRJ_NO"));
						
							System.out.println("UPDATE SATUS" +projNo);
							proj.setProjectNumber(projNo);
							
						return proj;
					}
				}
				catch (ParseException e) {
					e.printStackTrace();

				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return proj;
		}
		public List<Project> processGivenData(HttpServletRequest request, HttpServletResponse response) throws SQLException{
			List<Project> proList = new ArrayList<Project>();
			try {
				
				String userId = (String)request.getSession().getAttribute("Username");//Authorization
				BufferedReader reader = request.getReader();
				StringBuilder sb = new StringBuilder();
				String line = reader.readLine();
				JSONObject obj = new JSONObject();
				JSONArray jJSONArray = new JSONArray();
				while (line != null) {
					sb.append(line + "\n");
					line = reader.readLine();
				}
				reader.close();
				String params = sb.toString();
				String[] _params = params.split("&");
				String jsondata = null;
				for (String param : _params) {
					System.out.println("params(POST)-->" + param);
					jsondata = param.toString();
				}
				System.out.println("jsondata is -------" + jsondata);
				
					
					JSONParser parser = new JSONParser();
					jJSONArray = (JSONArray) parser.parse(jsondata);
					for(int i=0;i< jJSONArray.size();i++){
						
						Project proj = new Project();
						proj.setLoginUser(userId);
						System.out.println("get value" +jJSONArray.get(i));
						
						obj=(JSONObject)jJSONArray.get(i);
						//System.out.println("obj.values():: "+jJSONArray.);
						System.out.println("objEMPNO ---------"+obj.get("EMPNO"));
					
					
					
						/******************************UPDATE Project******************************************/
						if(request.getParameter("CMD").equalsIgnoreCase("PRJ_UPDATE"))
							
						{			
												
							String projNo = String.valueOf(obj.get("PRJ_NO"));
							String empno = String.valueOf(obj.get("EMP_NO"));
							String startDate = String.valueOf(obj.get("START_DATE"));
							String endDate = String.valueOf(obj.get("END_DATE"));
							
							if (empno != null && !empno.trim().isEmpty())
							{
								
								
								System.out.println("UPDATE project" +empno);
								proj.setEmpNumber(empno);
								proj.setProjectNumber(projNo);
								proj.setStartDate(startDate);
								proj.setEndDate(endDate);
							}
							else{
								response.getWriter().println(" getandparsejson-To update Project table need input data");
								//return proj;
							}
							//return proj;
						}
								
						proList.add(proj);
					}
					
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			System.out.println("emp list size:"+proList.size());
			return proList;
			
			}

}
