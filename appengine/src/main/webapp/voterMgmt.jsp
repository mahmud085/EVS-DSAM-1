<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.example.appengine.java8.Data.Candidate" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.appengine.java8.Data.Voter" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: oormila
  Date: 12/28/2018
  Time: 7:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Voter Management</title>
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
        input.emailSent,input.reminder{
            box-shadow: none;
            height:25px;
        }
    </style>
</head>
<body>
<div class="container">
    <p>
        <button class="add-new btn btn-primary">Add Voter</button>
        <button class="notifyVoters btn btn-primary">Notify voters</button>
        <button id="FileUpload_button_id" class="btn btn-primary">Upload File</button>
    </p>
    <form id="upload_file_form_id"  action="/admin/uploadfile" method="post" enctype="multipart/form-data">
        <div class="custom-file">
            <input type="file" class="custom-file-input" id="file_id" name="myFile">
            <input type="submit" style="display:inline;" class="btn btn-primary" value="Upload">
            <small  class="form-text text-muted">
                Upload a CSV file that contains name as first colomn and email as second
            </small>
        </div>

    </form>
    <p>&nbsp;</p>


    <%
        List<Voter> voterList=null;
        voterList = (List<Voter>) request.getAttribute("voterList");
    %>
    <form class="formvalidation">
        <table id="tableid" class="table table-responsive" border="1">

            <tr>
                <th>Name</th>
                <th>Email</th>
                <th>voter notified</th>
                <th>voting reminder</th>
                <th colspan="2">Actions</th>
            </tr>


            <tbody>
            <%
                if (voterList != null) {
                    for (Voter voter : voterList) {
            %>
            <tr data-id="<%= voter.getKey()!=null?voter.getKey().getId():""%>">


                <td>
                    <div class="col-xs">
                        <input type="text" readonly class="row-values form-control plaintext name"
                               name="name"
                               value="<%= voter.getName()!=null?voter.getName():""%>">
                    </div>
                </td>

                <td>
                    <div class="col-xs">
                        <input type="text" readonly class="row-values form-control plaintext email"
                               name="email"
                               value="<%= voter.getEmail()!=null?voter.getEmail():""%>">
                    </div>
                </td>

                <td>
                    <div class="col-xs">
                        <input type="checkbox" disabled readonly class="row-values form-control plaintext emailSent"
                               name="emailSent"
                               value="<%= voter.getEmailSent()!=null?voter.getEmailSent()+"":"false"%>"
                            <%=voter.getEmailSent()!=null?(voter.getEmailSent()?"checked":""):""%>
                        >
                    </div>
                </td>

                <td>
                    <div class="col-xs">
                        <input type="checkbox" disabled readonly class="row-values form-control plaintext reminder"
                               name="reminder"
                               value="<%= voter.getReminder()!=null?voter.getReminder()+"":"false"%>"
                               <%=voter.getReminder()!=null?(voter.getReminder()?"checked":""):""%>
                        >
                    </div>
                </td>

                <td class="updateVot" style="display: none">
                    <div class="col-xs">
                        <button type="button" class="btn btn-primary updatevoter submit">
                            Update
                        </button>
                    </div>
                </td>
                <td class="updateVot" style="display: none">
                    <div class="col-xs">
                        <button type="button" id="canceledit" class="btn btn-primary cancel">
                            Cancel
                        </button>
                    </div>
                </td>
                <td class="editVot">
                    <div class="col-xs">
                        <button type="button" class="glyphicon glyphicon-pencil btn btn-primary editvoter">
                        </button>
                    </div>
                </td>
                <td class="deletevot">
                    <div class="col-xs">
                        <button type="button" class="glyphicon glyphicon-remove btn btn-danger deletevoter">
                        </button>
                    </div>
                </td>
            </tr>
            <% }
            }%>
            <tr class="newitem" style="display: none">
                <td>
                    <input type="text" class="row-values form-control plaintext name hidden" name="name">
                </td>
                <td>
                    <input type="text" class="row-values form-control plaintext email hidden"
                           name="email">
                </td>

                <td>
                </td>

                <td>
                </td>
                <td>
                    <button type="button" class="button btn btn-primary submit saveVoter">Create</button>
                </td>

                <td>
                    <button type="button" class="button btn btn-primary newitemcancel cancel">Cancel</button>
                </td>
            </tr>


            </tbody>
        </table>
    </form>
    <div class="col-1" align="center">
        <a href="/" class="btn btn-danger">Home</a>
    </div>
</div>


<script type="application/javascript">
    $(".add-new").click(function () {
        $(this).attr("disabled", "disabled");
        $(".newitem").show();
        $(".newitem").children("td").children().removeClass("hidden");
        $(".newitem").children("td").children(".form-control").attr("required", "required");
    });


    $(".cancel").click(function () {
        location.reload();
    });
    $(".submit").click(function () {

        if (!$(".formvalidation")[0].checkValidity({ignore: ':readonly,:hidden'})) {
            $(".formvalidation")[0].reportValidity();
        }
        else {
            if ($(this).hasClass("saveVoter")) {
                saveVoter();
            }
            else if ($(this).hasClass("updatevoter")) {
                var tr = $(this).closest('tr');
                updateVoterFunc(tr);
            }
        }

    });
    saveVoter = function () {
        var name = $(".newitem").children("td").children(".name").val();
        var email = $(".newitem").children("td").children(".email").val();

        $.ajax
        ({
            url: '/admin/voterlist',
            data: {
                "name": name, "email": email
            },
            type: 'POST',
            success: function (data) {
                if(data.length==0)
                location.reload();
                else
                    alert(data);
            }
        });

    }


    updateVoterFunc = function (sRow) {
        try {
            var tr = sRow;
            var elem = tr.children("td").children("div");
            var id = tr.data("id");
            var name = elem.children(".name").val();
            var email = elem.children(".email").val();
            var emailSent=elem.children(".emailSent").val();
            var reminder=elem.children(".reminder").val();
            $.ajax
            ({
                url: '/admin/voterlist',
                data: {
                    "id":id,"name": name, "email": email,"emailsent":emailSent,"reminder":reminder
                },
                type: 'put',
                success: function (data) {
                    if(data=="true")
                    location.reload();
                    else
                        alert(data);
                    console.log(data);
                }
            });
        } catch (err) {
        }
    }


    $(".editvoter").click(function () {
        var tr = $(this).closest('tr');
        var elem = tr.children("td").children("div");
        tr.children('.editVot').hide();
        tr.children('.deletevot').hide();
        tr.children('.updateVot').show();
        elem.children(".name,.email").removeAttr("readonly");
        elem.children(".name,.email").attr("required", "required");
        elem.children(".name,.email").removeAttr("disabled");

    });

    $(".notifyVoters").click(function () {
        alert("voters will be notified");
        $.ajax
        ({
            url: '/admin/notifyVoters',
            type: 'get',
            success:function (data) {
                if(!data.includes("true")&& data.toString().length>0)
                alert(data);
                else
                    location.reload();
            }
        });
    });

    $(".deletevoter").click(function () {
        var x = confirm("Are you sure you want to delete this Voter?");
        if (x) {
            var tr = $(this).closest('tr');
            var id = tr.data("id");
            $.ajax
            ({
                url: '/admin/voterlist?id='+ id,
                contentType: "application/json; charset=utf-8",
                type: 'DELETE',
                success: function (data) {
                    if(data=="true")
                   location.reload();
                    else
                        alert("Could not delete voter");
                }
            });
        }
    });

    $(document).ready(function () {
        $("#upload_file_form_id").hide();
    });

    $("#FileUpload_button_id").click(function () {
        $("#upload_file_form_id").show();
    });
</script>
</body>
</html>
