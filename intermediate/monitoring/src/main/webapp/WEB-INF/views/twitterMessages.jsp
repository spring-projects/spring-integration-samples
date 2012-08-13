<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
                <p>This example searches Twitter and shows the results below.</p>
                <ul class="twitterMessages">
                    <c:choose>
                        <c:when test="${not empty twitterMessages}">
                            <c:forEach items="${twitterMessages}" var="twitterMessage">
                                <li>
                                <img alt="${twitterMessage.fromUser}"
                                     title="${twitterMessage.fromUser}"
                                     src="${twitterMessage.profileImageUrl}"
                                     width="48" height="48">
                                <c:out value="${twitterMessage.text}"/></li>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>No Twitter messages found. Did you start the search?</c:otherwise>
                    </c:choose>
                </ul>
