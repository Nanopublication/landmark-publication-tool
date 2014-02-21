<%-- 
    Document   : home
    Created on : 14-mei-2013, 17:08:11
    Author     : reinout
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:url var="resources" value="/resources/" />
<spring:url var="webjars" value="/webjars/" />
<spring:url var="home" value="/" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Landmark discovery registration</title>
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

        <div class="bgControl">
            <img id="bgBack" src="${resources}/icons/Actions-media-seek-backward-icon.png">
            <img id="bgShow" src="${resources}/icons/Actions-edit-find-icon.png">
            <img id="bgForward" src="${resources}/icons/Actions-media-seek-forward-icon.png">
        </div>
        <div id="navigationList" class="content ui-corner-right ui-widget">
            Go to the list off all landmark claims
        </div>
        <div id="explanation" class="content ui-corner-right ui-widget">
            <img id="infoAction" style="width:50px;" src="${resources}/icons/info-large.png">
            <div id="explContent">
                Claiming your discovery takes only seconds... and you get:
                <ul>
                    <li> More citations to your work: Get credit for your (and your institution's) discovery!
                    <li> You can search for other LandMark discoveries.. and easily cite them!
                    <li> Be part of the Semantic Web (LandMark is powered by nanopublications) !
                    <li> Cool <em>trending</em> tools for predicting novel gene-disease associations are in 
                        development... join now so you don't miss out.
                </ul>
                By claiming your LandMark discovery, you contribute to a new, Open, Gold Standard Benchmark Dataset for the development of advanced biomedical literature mining tools.
            </div>
        </div>
        <div id="accordion">
            <h3 class="acc-header"><a href="#">Gene&nbsp;-&nbsp;Disease</a></h3>

            <div class="formdiv">
                <form:form modelAttribute="landmarkPublication" method="post" id="inputForm" action="${home}">
                    <form:errors path="*" cssClass="errorblock" element="div" />
                    <div id="subject">
                        <span id="subjectLabel"></span>
                        <img id="subInfo" class="info tooltip" src="${resources}/icons/info.png" title="Fill in the gene name. With autocomplete the gene name will be resolved to a unique UUID in the concept wiki.">
                        <!--<input type="text" name="autosub" id="completeSubject" class="validateConcept">-->
                        <form:input path="subjectLabel" name="autosub" id="completeSubject" class="validateConcept"/>
                        <img id="subRemove" class="remove" src="${resources}/icons/undo.png">
                        <a id="completeSubjectInfo" class="cwinfo">Show concept</a>
                        <form:input path="subjectUri" type="hidden" name="sub" class="hidden" id="completeSubjectUUID"/>
                        <br>
                    </div>
                    <div id="object">
                        <span id="objectLabel"></span>
                        <img id="objInfo" class="info tooltip" src="${resources}/icons/info.png" title="Fill in the disease name. With autocomplete the disease name will be resolved to a unique UUID in the concept wiki.">
                        <form:input path="objectLabel" name="autoobj" id="completeObject" class="validateConcept"/>
                        <img id="objRemove" class="remove" src="${resources}/icons/undo.png">
                        <a id="completeObjectInfo" class="cwinfo">Show concept</a>
                        <form:input path="objectUri" type="hidden" name="obj" class="hidden" id="completeObjectUUID"/>
                    </div>
                    <div id="provenance">
                        <table>
                            <tr>
                                <td><span id="testclick">PubMedID</span><img id="doiInfo" class="info tooltip" src="${resources}/icons/info.png" title="Fill in the PubMed identifier of the LandMark publication, e.g. 21280221">&nbsp;</td>
                                <td><form:input path="pubmedId" name="doi"/></td>
                            </tr>
                            <tr>
                                <td><span>Publication Date</span><img id="pdateInfo" class="info tooltip" src="${resources}/icons/info.png" title="">&nbsp;</td>
                                <td><form:input path="publicationDate" type="text" name="pdate" id="datepicker"/></td>
                            </tr>
                            <tr><td><span>Author</span><img id="ridInfo" class="info tooltip" src="${resources}/icons/info.png" title="Fill in the first author of the paper. This may also be an email address. Please do with name and surname if possible.">&nbsp;</td>
                                <td><form:input path="author" type="text" name="rid" class="rid"/></td>
                            </tr>
                            <tr><td><span>Curator ID</span><img id="cidInfo" class="info tooltip" src="${resources}/icons/info.png" title="Are you making the claim on someone else's behalve? Then you deserve credits as a curator! Please fill in your name and/or email address.">&nbsp;</td>
                                <td><form:input path="curatorId" type="text" name="cid" class="cid"/></td>
                            </tr>
                            <tr><td><span>Institution</span><img id="instInfo" class="info tooltip" src="${resources}/icons/info.png" title="Which institution is mainly to be credited for this discovery?">&nbsp;</td>
                                <td><form:input path="institution" type="text" id="inst" name="inst" class="inst"/></td>
                            </tr>
                            <tr><td><span>Experiment type</span><img id="expInfo" class="info tooltip" src="${resources}/icons/info.png" title="Choose an experiment type that is described in the landmark publication that obtained the landmark result.">&nbsp;</td>
                                <td>
                                    <form:select path="experimentType" name="component-select">
                                <OPTION selected value="choose">Please choose one</OPTION>
                                <OPTION>NGS</OPTION>
                                <OPTION>PCR</OPTION>
                                <OPTION>qPCR</OPTION>
                                <OPTION>Western blot</OPTION>
                                <OPTION>Eastern blot</OPTION>
                                <OPTION>Microarray</OPTION>
                                <OPTION>Pull down assays</OPTION>
                                <OPTION>Mass spectrometry</OPTION>
                                <OPTION>Yeast2Hybrid</OPTION>
                                <OPTION>Not listed/Other</OPTION>
                                <OPTION>Unknown</OPTION>
                                </form:select>
                            </td>
                            </tr>
                            <!--<tr><td><span>Motivation</span><img id="instInfo" class="info" src="${resources}/icons/info.png">&nbsp;</td>
                                <td>
                                    <SELECT name="component-select">
                                        <OPTION selected value="choose">Please choose one</OPTION>
                                        <OPTION value="Component_1_a">Based on previous knowledge from a literature study the experiment was logical to choose</OPTION>
                                        <OPTION value="Component_1_b">No previous knowledge. The result came as a surprise from the lab</OPTION>
                                    </SELECT>
                                </td>
                            </tr>-->
                            <tr style="font-size: 95%"><!--
                                <td align="justify" colspan="2">
                                    Would you describe the discovery more as an
                                    <span id="accidentalEnv" style="white-space:nowrap; background-color: lightgrey;">
                                        <input type="radio" name="motiv" id="accidentType">
                                        <label for="accidentType">accidental</label>
                                    </span> finding, or as the result of
                                    <span id="analyticalEnv" style="white-space:nowrap; background-color: lightgrey;">
                                        <input type="radio" name="motiv" id="analyticalType">
                                        <label for="analyticalType">analytical</label>
                                    </span> derivation based on previous knowledge from literature?
                                </td>-->
                                <td align="justify" colspan="2">
                                    Would you describe the discovery more as an
                                    <span id="accidentalEnv" style="white-space:nowrap; background-color: lightgrey;">
                                        <form:radiobutton path="discoveryType" name="motiv" value="serendipitous" id="accidentalType"/>
                                        <label for="accidentType">serendipitous</label>
                                    </span> finding, or an 
                                    <span id="analyticalEnv" style="white-space:nowrap; background-color: lightgrey;">
                                        <form:radiobutton path="discoveryType" name="motiv" value="expected" id="analyticalType"/>
                                        <label for="analyticalType">expected result</label>
                                    </span> based on previous knowledge (e.g. literature or computational prediction)? Or
                                    <span id="unknownEnv" style="white-space:nowrap; background-color: lightgrey;">
                                        <form:radiobutton path="discoveryType" name="motiv" value="unknown" id="unknownType"/>
                                        <label for="unknownType">unknown</label>?&nbsp;<img id="expInfo" class="info tooltip" src="${resources}/icons/info.png" title="Which description fits your discovery the best? Feel free to elaborate by entering <strong>details</strong> below.">
                                    </span>
                                    <!--<img id="typeInfo" class="info" src="${resources}/icons/info.png">-->
                                </td>
                            </tr>
                            <tr id="detailsQuestion">
                                <td colspan="2">
                                    Would you like to provide additional <a id="detailsAction">details?</a>
                                </td>
                            </tr><tr id="detailsText">
                                <td colspan="2">
                                    <form:textarea path="details" id="detailsArea" rows="2" />
                                    <br/>You are welcome to provide details of your discovery, or notify us if the multiple-choice options were insufficient.
                                </td>
                            </tr>
                        </table>
                        <!--<table>
                            <tr>
                                <td width="50%">Would you describe the discovery as an</td>
                                <td width="25%"><input type="radio" name="motiv">accidental<br>
                                    <input type="radio" name="motiv">analytical<br>
                                </td>
                                <td width="50%">result</td>
                            </tr>
                        </table>-->
                    </div>
                    <br>
                    <div>
                        <input type="submit" value="Publish your landmark discovery">
                    </div>
                </form:form>
            </div>

            <h3 class="acc-header"><a href="#">Protein&nbsp;-&nbsp;Protein</a></h3>
            <div class="formdiv">
            </div>

            <h3 class="acc-header"><a href="#">Drug&nbsp;-&nbsp;Disease</a></h3>
            <div class="formdiv">
            </div>

        </div>

        <script type="text/javascript" src="${resources}/scripts/landmarkHome.js"></script>
        <script type="text/javascript">
            $(window).load(function() {
                $("#background").fullBg();
            });


            var index = 0;
            var bgs = ['061003_roma_016.jpg',
                'obelistk.jpg',
                'paris.jpg',
                'lumc.jpeg',
                '061005_trdp_042.jpg',
                '061005_trdp_044.jpg',
                '061005_trdp_049.jpg',
                '061120_trdp_001.jpg',
                '090331a4565.jpg',
                '201105_cr_04_03.jpg',
            ];
            $('#bgForward').click(function() {
                rotate(1);
            }); // seems this HAS to be done after html?
            $('#bgBack').click(function() {
                rotate(-1);
            }); // seems this HAS to be done after html?

            function rotate(offset) {
                index += offset;
                var bg = bgs[index % 7];
                $('#background').attr('src', "bgs/" + bg);
                console.log(bg);
            }
            ;

            $('#explanation').css('width', '0px');
            $('#explContent').hide();

            $('#bgShow').click(function() {
                if ($('#accordion').is(":visible")) {
                    $('#accordion').hide();
                    $('#explanation').css('width', '0px');
                    $('#explContent').hide();
                } else {
                    $('#accordion').show();
                }
            });

            $('#infoAction').click(function() {
                if ($('#explContent').is(":visible")) {
                    $('#explanation').css('width', '0px');
                    $('#explContent').hide();
                } else {
                    $('#explanation').css('width', '28%');
                    $('#explContent').show('fold');
                }
            });

            $('#navigationList').on('click', function() {
                window.location = "nanopubList";
            });

            $(document).ready(function() {
                $('.tooltip').tooltipster();
            });
        </script>

    </body>

</html>
