<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<!DOCTYPE HTML>
<html>
    <head>

        <title>Welcome to Spring Integration</title>

        <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />

        <link rel="stylesheet" href="<c:url value='/css/blueprint/screen.css'/>" type="text/css" media="screen, projection">
        <link rel="stylesheet" href="<c:url value='/css/blueprint/print.css'/>"  type="text/css" media="print">
        <!--[if lt IE 8]>
            <link rel="stylesheet" href="/css/blueprint/ie.css" type="text/css" media="screen, projection">
        <![endif]-->
        <link rel="stylesheet" href="<c:url value='/css/main.css'/>"  type="text/css">

        <script src="<c:url value='/js/jquery-1.6.1.min.js'/>"></script>
        <script src="<c:url value='/js/jquery.periodicalupdater.js'/>"></script>
    </head>
    <body>
        <div class="container">
            <div id="header" class="prepend-1 span-22 append-1 last">
                <h1 class="loud">
                    Welcome to Spring Integration
                </h1>
                <div>
                    <form:form id="formId">
                        <input id="startTwitter" type="submit" name="startTwitter" value="Start Twitter Search" /> |
                        <input id="stopTwitter" type="submit" name="stopTwitter"   value="Stop Twitter Search" /> |
                        <input id="shutdown" type="submit" name="shutdown"   value="Shutdown Integration Flow" />
                    </form:form>
                </div>
            </div>
            <div id="content" class="prepend-1 span-22 append-1 prepend-top last">
                <%@ include file="/WEB-INF/views/twitterMessages.jsp"%>
            </div>
        </div>

        <script type="text/javascript">

            $.PeriodicalUpdater('<c:url value="/ajax"/>', {
                        method: 'get', // method; get or post
                        data: '', // array of values to be passed to the page - e.g. {name: "John", greeting: "hello"}
                        minTimeout: 5000, // starting value for the timeout in milliseconds
                        maxTimeout: 20000, // maximum length of time between requests
                        multiplier: 2, // the amount to expand the timeout by if the response hasn't changed (up to maxTimeout)
                        type: 'text', // response type - text, xml, json, etc. See $.ajax config options
                        maxCalls: 0, // maximum number of calls. 0 = no limit.
                        autoStop: 0 // automatically stop requests after this many returns of the same data. 0 = disabled.
                    }, function(remoteData, success, xhr, handle) {
                        $('#content').html(remoteData);

                });

            $(function() {
                $('#startTwitter').bind('click', function() {
                    $.post("<c:url value='/'/>", "startTwitter=startTwitter");
                    return false;
                });

                $('#stopTwitter').bind('click', function() {
                    $.post("<c:url value='/'/>", "stopTwitter=stopTwitter");
                    return false;
                });

                $('#shutdown').bind('click', function() {
                    $.post("<c:url value='/'/>", "shutdown=shutdown");
                    return false;
                });
            });

        </script>

    </body>
</html>
