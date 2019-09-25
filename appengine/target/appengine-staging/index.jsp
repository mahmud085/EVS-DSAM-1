<!DOCTYPE html>
<!-- [START_EXCLUDE] -->
<%--
  ~ Copyright 2017 Google Inc.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License"); you
  ~ may not use this file except in compliance with the License. You may
  ~ obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
  ~ implied. See the License for the specific language governing
  ~ permissions and limitations under the License.
  --%>
<!-- [END_EXCLUDE] -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.appengine.java8.servlet.HelloAppEngine" %>
<html>
<head>
  <link href='//fonts.googleapis.com/css?family=Marmelad' rel='stylesheet' type='text/css'>
  <title>VOTE NOW</title>

  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container" align="center">
  <h1 align="center">Welcome</h1>
  <div>&nbsp;</div>
  <div>&nbsp;</div>

  <div class="row">
    <div class="col-xs-1 col-md-2"> </div>
    <div class="col-xs-10 col-md-8" align="center"><a href="/admin/votetime"
                                                      class="btn btn-primary btn-block">Set Date and Time for voting</a></div>
    <div class="col-xs-1 col-md-2"> </div>

  </div>
  <div class="row">
    <div>&nbsp;</div>
  </div>
  <div class="row">
    <div class="col-xs-1 col-md-2"> </div>

    <div class="col-xs-10 col-md-8" align="center"><a href="/admin/voterlist"
                                                      class="btn btn-primary btn-block">Manage Voters</a></div>

    <div class="col-xs-1 col-md-2"> </div>
  </div>
  <div class="row">
    <div>&nbsp;</div>
  </div>

  <div class="row">
    <div class="col-xs-1 col-md-2"> </div>

    <div class="col-xs-10 col-md-8" align="center"><a href="/admin/candidatemanagement"
                                                      class="btn btn-primary btn-block">Manage Candidates</a></div>
    <div class="col-xs-1 col-md-2"> </div>

  </div>

  <div class="row">
    <div>&nbsp;</div>
  </div>

  <div class="row">
    <div class="col-xs-1 col-md-2"> </div>

    <div class="col-xs-10 col-md-8" align="center"><a href="/user/votingInterface"
                                                      class="btn btn-primary btn-block">Voting Booth</a></div>
    <div class="col-xs-1 col-md-2"> </div>

  </div>

  <div class="row">
    <div>&nbsp;</div>
  </div>

</div>


</body>
</html>
