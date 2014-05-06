<%-- 
    Document   : curatorList
    Created on : 6 May, 2014, 4:10:15 PM
    Author     : rajaram
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<spring:url var="resources" value="/resources/" />
<spring:url var="webjars" value="/webjars/" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Curator stats</title>
        <script type="text/javascript" src="${webjars}/jquery/2.0.0/jquery.js"></script>
        <script type="text/javascript" src="${webjars}/jquery-ui/1.10.2/ui/jquery-ui.js"></script>

        <script type="text/javascript" src="${resources}/scripts/jquery.qtip.js"></script>
        <script type="text/javascript" src="${resources}/jquery-ui-extensions/jquery.fullBg.js"></script>
        <script type="text/javascript" src="${resources}/scripts/jquery.fancybox.js"></script>
        <script type="text/javascript" src="${resources}/scripts/jquery.tooltipster.js"></script>

        <link rel="stylesheet" type="text/css" href="${webjars}/jquery-ui-themes/1.10.0/ui-lightness/jquery-ui.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="${webjars}/jquery-ui-themes/1.10.0/base/jquery.ui.accordion.css" >
        <link rel="stylesheet" type="text/css" href="${resources}/css/formstyle.css" >
        <link rel="stylesheet" type="text/css" href="${resources}/css/jquery.qtip.css">
        <link rel="stylesheet" type="text/css" href="${resources}/css/tooltipster.css">
        <link rel="stylesheet" type="text/css" href="${resources}/css/jquery.fancybox.css" media="screen">
        
        <style>
        /* Beetje hacken: */
        
        	#accordion {
        		overflow-x: auto;
						overflow-y: hidden;
    				left: 10%;
   					top: 15%;
    				width: 80%;
    			}
    			
        </style>

    </head>
    <body>
        <img src="${resources}/bgs/061005trdp042.jpg" alt="background" id="background" />

        <div id="navigationNew" class="content ui-corner-right ui-widget">
        <!-- Submit a new <a href="../">landmark claim</a>  -->
        Submit a new landmark claim
        </div>
        <div id="navigationList" class="content ui-corner-right ui-widget">
        <!-- Go to the <a href="../nanopubList">list</a> of all landmark claims  -->
        Go to the list of all landmark claims
        </div>
        
        <div id="accordionList" style="height:700px; display:block;">
            <h3 class="acc-header"><a href="#">Curator stats</a></h3>
            <table cellpadding="3" style="width:100%;">

                <thead> 

                    <tr>                         
                        <th>Curator</th>
                        <th>Number of claims</th>
                    </tr>
                </thead>

                <tbody>
                    
                    <c:set var="count" value="0" scope="page" />
                    <c:set var="prevNoOfClaims" value="0" scope="page" />
                    
                    <c:forEach items="${curatorStatsSet}" var="curatorStats">
                        
                                          
                        <tr>
                            <td><c:out value="${curatorStats.name}"/></td>
                            <td><c:out value="${curatorStats.noOfClaims}"/></td>
                            
                            <c:if test="${prevNoOfClaims != curatorStats.noOfClaims}">
                                <c:set var="count" value="${count + 1}" scope="page"/> 
                            </c:if> 
                            
                            <c:set var="prevNoOfClaims" value="${curatorStats.noOfClaims}" scope="page"/> 
                            
                         
                            
                            <c:choose>
                                
                                <c:when test="${count == 1}">
                                    <td>
                                        <img src="${resources}/lead/first.jpg" id="first" height="22" width="22"/>
                                    </td>
                                </c:when>

                                <c:when test="${count == 2}">
                                    <td>
                                        <img src="${resources}/lead/second.jpg" id="second" height="22" width="22"/>
                                    </td>
                                </c:when> 

                                <c:when test="${count == 3}">
                                    <td>
                                        <img src="${resources}/lead/third.jpg" id="third" height="22" width="22"/>
                                    </td>
                                </c:when> 
                                    
                                    
                                    <c:otherwise>
                                        <td>"${count}"</td>
                                    </c:otherwise>
                                        
                            </c:choose>
                               
                                
                        </tr>
                        
                        
                        
                        
                    </c:forEach>
                </tbody>


            </table>
        </div>
        
        <script type="text/javascript" src="${resources}/scripts/landmarkHome.js"></script>
        <script type="text/javascript">
 
        $('#navigationNew').on('click', function(){
            window.location = ".";    
        });
        $('#navigationList').on('click', function(){
            window.location = "nanopubList";    
        });
/*             $('#accordion').accordion({
                heightStyle: "fill"
            }).refresh();
             
            $(function() {
                $( "#accordionList" ).accordion({
                  heightStyle: "fill"
                });
              }); */
              
              $('#accordionList').accordion();
 
 $(window).load(function() {
     $("#background").fullBg();
 });
        </script>

    </body>
</html>
