<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<spring:url var="resources" value="/resources/" />
<spring:url var="webjars" value="/webjars/" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Landmark publication view</title>
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

    </head>
    <body>
        <img src="${resources}/bgs/061005trdp042.jpg" alt="background" id="background" />

        <br/>
        <div id="navigationNew" class="content ui-corner-right ui-widget">
        <!-- Submit a new <a href="../">landmark claim</a>  -->
        Submit a new landmark claim
        </div>
        <div id="navigationList" class="content ui-corner-right ui-widget">
        <!-- Go to the <a href="../nanopubList">list</a> of all landmark claims  -->
        Go to the list of all landmark claims
        </div>
        <div id="accordion">
            <h3 class="acc-header"><a href="#">Landmark nanopublication</a></h3>

            <table cellpadding="4" style="width:100%; padding: 10;">
                <tr>
                    <td>Subject label</td>
                    <td><a href="${publication.subjectUri}">${publication.subjectLabel}</a></td>
                </tr>

                <tr>
                    <td>Object label</td>
                    <td><a href="${publication.objectUri}">${publication.objectLabel}</a></td>
                </tr>
                            
                <tr><td>&nbsp;</td></tr>
            
                <tr>
                    <td>Author</td>
                    <td>${publication.author}</td>
                </tr>

                <tr>
                    <td>Institution</td>
                    <td>${publication.institution}</td>
                </tr>
                
                <tr>
                    <td>PubmedId</td>
                    <td>${publication.pubmedId}</td>
                </tr>

                <tr>
                    <td>Publication date</td>
                    <td>${publication.publicationDate}</td>
                </tr>
            
                <tr><td>&nbsp;</td></tr>
                
                <tr>
                    <td>Claim ID</td>
                    <td>${publication.id}</td>
                </tr>

                <tr>
                    <td>Claim date</td>
                    <td>${publication.claimDate}</td>
                </tr>              

                <tr>
                    <td>Curator id</td>
                    <td>${publication.curatorId}</td>
                </tr>              
                                
                <tr>
                    <td>Discovery type</td>
                    <td>${publication.discoveryType}</td>
                </tr>
                
                <tr>
                    <td>High-throughput</td>
                    <td>${publication.experimentType}</td>
                </tr>
 
                <tr><td>&nbsp;</td></tr>
                
                <tr>
                    <td>Details</td>
                    <td>${publication.details}</td>
                </tr>  
                
                <tr>
                    <td>Nanopublication</td>
                    <td>
                        <a href="/landmark-publication-tool/rdf/${publication.id}" target="blank">
                           
                        Trig</a>
                    </td>
                </tr>
                
                
                
                <!-- If we have an rdf file, link to it here
                <tr>
                    <td>NanoPublication URL</td>
                    <td>${publication.nanoPublicationURL}</td>
                </tr>
                -->

            </table>
        </div>
        <script type="text/javascript" src="${resources}/scripts/landmarkHome.js"></script>
        <script type="text/javascript">
            $(window).load(function() {
                $("#background").fullBg();
            });
            $('#navigationNew').on('click', function(){
                window.location = "..";    
            });
            $('#navigationList').on('click', function(){
                window.location = "../nanopubList";    
            });
        </script>

    </body>
</html>