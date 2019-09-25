<%@ page import="com.example.appengine.java8.Data.Candidate" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.appengine.java8.services.VoteTimeManagement" %><%--
  Created by IntelliJ IDEA.
  User: shorm_jd4k5bp
  Date: 21/01/2019
  Time: 5:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>VotingInterface</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.15.1/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.7.14/js/bootstrap-datetimepicker.min.js"></script>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.7.14/css/bootstrap-datetimepicker.min.css">

    <style>
        .container {
            width: max-content;
            margin-top: 50px;
        }

        .editTIming {
            margin-bottom: 50px;
        }
    </style>
</head>
<body>
    <div class="container" align="center">

        <h1>Voting Booth</h1>

    </div>
    <div class="container">
        <%
            List<Candidate> candidateList =null;
            List<VoteTimeManagement> timing = null;
           // VoteTimeManagement service = new VoteTimeManagement();
           // HashMap<String, Date> timing = null;
            try {
                candidateList = (List<Candidate>) request.getAttribute("candidateList");
                timing = (List<VoteTimeManagement>)request.getAttribute("startdate");
            }catch (Exception e){
            }
        %>
        <form id="voteform" action = "/user/vote" method="post">
        <table id="tableid" class="table table-responsive" border="1">

            <tr>
                <th>Option</th>
                <th>Candidate Name</th>
                <th>Faculty</th>

            </tr>


            <tbody>
                <%
                if (candidateList != null) {
                    for (Candidate candidate : candidateList) {
            %>
            <tr class="keydata" data-id="<%=candidate.getKey()!=null?candidate.getKey().getId():""%>">

                <td>
                    <div class="radio">
                        <input type="radio" class=" form-control plaintext"  style="width:30px; height:20px; left:30px;
                         bottom:-15px;"
                               name="candidateSelection"
                               value="<%= candidate.getKey()!=null?candidate.getKey().getId():""  %>" required>
                    </div>
                </td>
                <td>

                        <input type="text"  readonly class="row-values form-control plaintext "
                               name="candidateName"
                               value="<%= candidate.getFirstname() !=null?candidate.getFirstname()+" "+ candidate.getSurname():"" %>">

                </td>


                <td>

                        <input type="text" readonly class="row-values form-control plaintext "
                               name="faculty"
                               value="<%= candidate.getFaculty()!=null?candidate.getFaculty():""%>">

                </td>
            </tr>

           <% }
        }%>

        </table>
            <div class="form-group" >
                <label for="token">token</label>
                <input type="text" class="form-control" id="token" required name="token" >
            </div>
            <p>
                <input type="submit"></input>
            </p>
        </form>



        <p>&nbsp;</p>
    </div>

    <script type="application/javascript">
        $("#voteform").submit(function(e) {


            var form = $(this);
            var url = form.attr('action');

            $.ajax({
                type: "POST",
                url: url,
                data: form.serialize(), // serializes the form's elements.
                success: function(data)
                {
                    if(data=="true")
                       window.location="/thankyou.jsp";
                    else
                       alert(data);
                }
            });

            e.preventDefault();
        });
    </script>

</body>
</html>
