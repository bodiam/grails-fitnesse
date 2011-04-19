<%@ page import="nl.jworks.amasun.domain.book.Book" %>
<html>
<head>
    <title>Featured books</title>
    <meta name="layout" content="shop"/>
</head>

<body>

<div class="title"><span class="title_icon"><g:img src="images/bullet1.gif" alt="" title=""/></span>Featured books</div>

<g:each in="${Book.list()}" var="book">

<div class="feat_prod_box">

    <div class="prod_img"><a href="details.html?id=${book.id}"><g:img src="images/${book.title}.jpg" style="width: 115px" alt="" title="" border="0"/></a></div>

    <div class="prod_det_box">
        <div class="box_top"></div>
        <div class="box_center">
            <div class="prod_title">${book.title}</div>

            <p class="details">${book.details}</p>
            <a href="details.html?id=${book.id}" class="more">- more details -</a>
            <div class="clear"></div>
        </div>

        <div class="box_bottom"></div>
    </div>
    <div class="clear"></div>
</div>

</g:each>

<!--
<div class="feat_prod_box">

    <div class="prod_img"><a href="details.html"><g:img src="images/prod2.gif" alt="" title="" border="0"/></a></div>

    <div class="prod_det_box">
        <div class="box_top"></div>
        <div class="box_center">
            <div class="prod_title">Product name</div>

            <p class="details">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation.</p>
            <a href="details.html" class="more">- more details -</a>
            <div class="clear"></div>
        </div>

        <div class="box_bottom"></div>
    </div>
    <div class="clear"></div>
</div>
-->


<div class="title"><span class="title_icon"><g:img src="images/bullet2.gif" alt="" title=""/></span>New books</div>

<div class="new_products">

    <div class="new_prod_box">
        <a href="details.html">product name</a>
        <div class="new_prod_bg">
            <span class="new_icon"><g:img src="images/new_icon.gif" alt="" title=""/></span>
            <a href="details.html"><g:img src="images/thumb1.gif" alt="" title="" class="thumb" border="0"/></a>
        </div>
    </div>

    <div class="new_prod_box">
        <a href="details.html">product name</a>
        <div class="new_prod_bg">
            <span class="new_icon"><g:img src="images/new_icon.gif" alt="" title=""/></span>
            <a href="details.html"><g:img src="images/thumb2.gif" alt="" title="" class="thumb" border="0"/></a>
        </div>
    </div>

    <div class="new_prod_box">
        <a href="details.html">product name</a>
        <div class="new_prod_bg">
            <span class="new_icon"><g:img src="images/new_icon.gif" alt="" title=""/></span>
            <a href="details.html"><g:img src="images/thumb3.gif" alt="" title="" class="thumb" border="0"/></a>
        </div>
    </div>

</div>
</body>
</html>