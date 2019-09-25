<%@ page import="com.example.appengine.java8.Data.Candidate" %>
<%@ page import="static java.util.Comparator.comparing" %>
<%@ page import="java.util.*" %><%--
  Created by IntelliJ IDEA.
  User: oormila
  Date: 12/28/2018
  Time: 7:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Voting Results</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="js/Chart.bundle.js"></script>
    <script src="js/utils.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.15.1/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.7.14/js/bootstrap-datetimepicker.min.js"></script>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.7.14/css/bootstrap-datetimepicker.min.css">

    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h1 class="heading1">Voting Results</h1>
<%
   HashMap<String,Integer> stats=new HashMap<>();
   stats= (HashMap<String, Integer>) request.getAttribute("statistics");
   Float vPercentage=(Float)request.getAttribute("votingpercentage");

%>
<main>
    <div class="parent">
        <div class="grid-layout">
            <div class="grid-item grid-item-1">
                <div class="insidegrid">
                    <div class="resText col-xs-12">eligible voters</div>

                    <div class="stResult eligibleVoters">
                        <%= stats.get("total_voters")!=null?stats.get("total_voters"):"0"%></div>
                </div>
            </div>
            <div class="grid-item grid-item-2">
                <div class="insidegrid">
                    <div class="resText col-xs-12">casted votes</div>

                    <div class="stResult castedVotes">
                        <%= stats.get("casted_voters")!=null?stats.get("casted_voters"):"0"%></div>
                </div>
            </div>
            <div class="grid-item grid-item-3">
                <div class="insidegrid percentage" data-percentage=" <%= vPercentage!=null?vPercentage:"0"%>">
                    <div class="col-xs-12">voter participation</div>
                    <div id="canvas-holder">
                        <canvas id="chart-area"></canvas>

                    </div>
                </div>
            </div>
        </div>
    </div>


</main>

<div class="layers">
    <div class="layer w-100 p-20"><h1 class="lh-1 candidateTtitle">Candidates</h1></div>
    <div class="container layer w-100">
        <%
        List<Candidate> candidateList=new ArrayList<>();
        candidateList= (List<Candidate>) request.getAttribute("candidateList");
            Candidate maxValueCandidate = candidateList.get(0);

        %>
        <div class="bgc-light-blue-500 c-white p-20">
            <div class="peers ai-c jc-sb gap-40 padding10">
                <div class="peer peer-greed text-right"><h4>Elected Candidate</h4></div>
                <div class="peer"><h3 class="text-right"><%=maxValueCandidate.getFirstname()+
                        " "+maxValueCandidate.getSurname()%></h3></div>
            </div>
        </div>

        <div class="table-responsive p-20">
            <table class="table">
                <thead>
                <tr>
                    <th class="bdwT-0">Name</th>
                    <th class="bdwT-0">Faculty</th>
                    <th class="bdwT-0">Vote Count</th>
                </tr>
                </thead>
                <%
                    for(Candidate candidate:candidateList)
                    {
                %>
                <tbody>
                <tr>
                    <td class="fw-600"><%=candidate.getFirstname()+" "+candidate.getSurname()%></td>
                    <td><span class="bgc-red-50 c-red-700 p-10 lh-0 tt-c badge-pill"><%=candidate.getFaculty()%></span></td>
                    <td><%=candidate.getVotecount()%></td>
                </tr>

                </tbody>
                <%
                    }
                %>
            </table>
        </div>
    </div>
</div>


<script type="text/javascript" src="js/chartFunctions.js"></script>
</body>
</html>
