<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.example.appengine.java8.Data.VoteTime" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.lang.invoke.ConstantCallSite" %>
<%@ page import="com.example.appengine.java8.helper.Constants" %><%--
  Created by IntelliJ IDEA.
  User: oormila
  Date: 12/28/2018
  Time: 7:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Set Date and Time</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.15.1/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.7.14/js/bootstrap-datetimepicker.min.js"></script>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.7.14/css/bootstrap-datetimepicker.min.css">

    <style>
        .container {
            width: 500px;
            margin-top: 50px;
        }

        .editTIming {
            margin-bottom: 50px;
        }
    </style>
</head>
<body>
<div class="container">
    <%
        VoteTime timings=null;
        SimpleDateFormat sdf=new SimpleDateFormat(Constants.DateFormat_MM_DD_YYYY_HH_MM_A);
        String sDate="",eDate="";
        try {
             timings = (VoteTime) request.getAttribute("timings");
             sDate=sdf.format(timings.getStartDate());
            eDate=sdf.format(timings.getEndDate());

        }catch (Exception e){
            //log exception
            e.printStackTrace();
        }
    %>
    <button type="button" <%=timings!=null ? "hidden" : ""%> class="editTIming btn btn-primary">Edit</button>
    <form action="/admin/votetime" id="timeform" method="POST">
        <label for="startdate">Start Date</label>
        <div class="form-group">
            <div class="col-xs-12 input-group date form_datetime" data-date="1979-09-16T05:25:07Z"
                 data-date-format="dd MM yyyy - HH:ii p" data-link-field="dtp_input1">
                <input type="text" onkeydown="return false" required class="form_datetime form-control" <%=timings!=null ?"readonly":""%>
                       id="startdate" aria-describedby="startdateHelp" name="startdate"
                       placeholder="start date" value=<%=timings!=null ?"'"+sDate+"'":""%>>

            </div>
        </div>


        <label for="enddate">End Date</label>
        <div class="form-group">
            <div class="col-xs-12 input-group date form_datetime" data-date="1979-09-16T05:25:07Z"
                 data-date-format="dd MM yyyy - HH:ii p" data-link-field="dtp_input2">
                <input type="text" onkeydown="return false" required class="form_datetime form-control" <%=timings!=null ?"readonly":""%>
                       id="enddate" aria-describedby="enddateHelp" name="enddate"
                       placeholder="end date" value=<%=timings!=null ?"'"+eDate+"'":""%>>

            </div>
        </div>
        <button type="button" align="center" class="btn btn-danger cancel <%=timings!=null ?"hidden":""%>">Cancel</button>
        <button type="submit" align="center" class="btn btn-primary saveTiming <%=timings!=null ?"hidden":""%>">Save</button>
    </form>
    <div class="col-1" align="center">
    <a href="/" class="btn btn-danger">Home</a>
    </div>
</div>


<script type="application/javascript">

    var date = new Date();
    date.setDate(date.getDate()-1);

    $('.form_datetime').datetimepicker({
        minDate: date
    });

    $("#timeform").submit(function(e) {


        var form = $(this);
        var url = form.attr('action');

        $.ajax({
            type: "POST",
            url: url,
            data: form.serialize(), // serializes the form's elements.
            success: function(data)
            {
                if(data=="true")
                    location.reload();
                else
                    alert(data);
            }
        });

        e.preventDefault();
    });
   // $('.form_datetime').datetimepicker({});

    $(".editTIming").click(function () {
        $("input").removeAttr("readonly");
        $(".saveTiming").removeClass("hidden");
        $(".cancel").removeClass("hidden");
    });

    $(".cancel").click(function () {
        $("input").attr("readonly","readonly");
        $(".saveTiming").addClass("hidden");
        $(".cancel").addClass("hidden");
    });

</script>


</body>
</html>
