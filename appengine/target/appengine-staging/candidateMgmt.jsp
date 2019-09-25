<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.example.appengine.java8.Data.Candidate" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: oormila
  Date: 12/28/2018
  Time: 7:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Candidate Management</title>
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
<div class="container">
    <p>
        <button class="add-new btn btn-primary">Add Candidate</button>
    </p>

    <p>&nbsp;</p>


    <%
        List<Candidate> candidateList =null;
        try {
            candidateList = (List<Candidate>) request.getAttribute("candidateList");
        }catch (Exception e){
        }
    %>
    <form class="formvalidation">
        <table id="tableid" class="table table-responsive" border="1">

            <tr>
                <th>Firstname</th>
                <th>Surname</th>
                <th>Faculty</th>
                <th colspan="2">Actions</th>
            </tr>


            <tbody>
            <%
                if (candidateList != null) {
                    for (Candidate candidate : candidateList) {
            %>
            <tr class="keydata" data-id="<%=candidate.getKey()!=null?candidate.getKey().getId():""%>">

                <td>
                    <div class="col-xs">
                        <input type="text" readonly class="row-values form-control plaintext name"
                               name="name"
                               value="<%= candidate.getFirstname()!=null?candidate.getFirstname():""%>">
                    </div>
                </td>

                <td>
                    <div class="col-xs">
                        <input type="text" readonly class="row-values form-control plaintext surname"
                               name="surname"
                               value="<%= candidate.getSurname()!=null?candidate.getSurname():""%>">
                    </div>
                </td>


                <td>
                    <div class="col-xs">
                        <input type="text" readonly class="row-values form-control plaintext faculty"
                               name="faculty"
                               value="<%= candidate.getFaculty()!=null?candidate.getFaculty():""%>">
                    </div>
                </td>
                <td class="updateCan" style="display: none">
                    <div class="col-xs">
                        <button type="button" class="btn btn-primary updatecandidate submit">
                            Update
                        </button>
                    </div>
                </td>
                <td class="updateCan" style="display: none">
                    <div class="col-xs">
                        <button type="button" id="canceledit" class="btn btn-primary cancel">
                            Cancel
                        </button>
                    </div>
                </td>
                <td class="editCan">
                    <div class="col-xs">
                        <button type="button" class="glyphicon glyphicon-pencil btn btn-primary editcandidate">
                        </button>
                    </div>
                </td>
                <td class="deletecan">
                    <div class="col-xs">
                        <button type="button" class="glyphicon glyphicon-remove btn btn-danger deletecandidate">
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
                    <input type="text" class="row-values form-control plaintext surname hidden"
                           name="surname">
                </td>
                <td>
                    <input type="text" class="row-values form-control plaintext faculty hidden"
                           name="faculty">
                </td>
                <td>
                    <button type="button" class="button btn btn-primary submit saveCandidate">Create</button>
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
            if ($(this).hasClass("saveCandidate")) {
                saveCandidate();
            }
            else if ($(this).hasClass("updatecandidate")) {
                var tr = $(this).closest('tr');
                updateCandidateFunc(tr);
            }
        }

    });
    saveCandidate = function () {
        var firstname = $(".newitem").children("td").children(".name").val();
        var surname = $(".newitem").children("td").children(".surname").val();
        var faculty = $(".newitem").children("td").children(".faculty").val();

        $.ajax
        ({
            url: '/admin/candidatemanagement',
            data: {
                "firstname": firstname, "surname": surname,
                "faculty": faculty
            },
            type: 'POST',
            success: function (data) {
                location.reload();
            }
        });

    }


    updateCandidateFunc = function (sRow) {
        try {
            var tr = sRow;
            var elem = tr.children("td").children("div");
            var id = tr.data("id");
            var firstname = elem.children(".name").val();
            var surname = elem.children(".surname").val();
            var faculty = elem.children(".faculty").val();

            $.ajax
            ({
                url: '/admin/candidatemanagement',
                data: {
                    "id":id,"firstname": firstname, "surname": surname,
                    "faculty": faculty
                },
                type: 'put',
                success: function (data) {
                    location.reload();
                    console.log(data);
                }
            });
        } catch (err) {
        }
    }


    $(".editcandidate").click(function () {
        var tr = $(this).closest('tr');
        var elem = tr.children("td").children("div");
        tr.children('.editCan').hide();
        tr.children('.deletecan').hide();
        tr.children('.updateCan').show();
        elem.children("input").removeAttr("readonly");
        elem.children("input").attr("required", "required");
        elem.children().removeAttr("disabled");

    });


    $(".deletecandidate").click(function () {
        var x = confirm("Are you sure you want to delete this candidate?");
        if (x) {
            var tr = $(this).closest('tr');
            var id = tr.data("id");
            $.ajax
            ({
                url: '/admin/candidatemanagement?id='+ id,
                contentType: "application/json; charset=utf-8",
                type: 'DELETE',
                success: function (data) {
                    location.reload();
                    console.log(data);
                }
            });
        }
    });
</script>
</body>
</html>
