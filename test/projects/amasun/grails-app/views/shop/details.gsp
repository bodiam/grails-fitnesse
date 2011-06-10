<%@ page import="nl.jworks.amasun.domain.book.Book" %>
<html>
<head>
    <title>Details</title>
    <meta name="layout" content="shop"/>

	<g:script src="js/prototype.js" type="text/javascript" />
	<g:script src="js/scriptaculous.js?load=effects" type="text/javascript" />
	<g:script src="js/lightbox.js" type="text/javascript" />
    <g:script src="js/java.js" type="text/javascript"  />    
</head>

<body>

<div class="crumb_nav">
    <a href="index.html">home</a> &gt;&gt; ${book.title}
</div>
<div class="title"><span class="title_icon"><g:img src="images/bullet1.gif" alt="" title=""/></span>${book.title}</div>

<div class="feat_prod_box_details">

    <div class="prod_img"><a href="details.html?id=${book.id}"><g:img src="images/${book.title}.jpg" style="width: 115px" alt="" title="" border="0"/></a>
        <br/><br/>
        <a href="../images/big_${book.title}.jpg" rel="lightbox"><g:img src="images/zoom.gif" alt="" title="" border="0"/></a>
    </div>

    <div class="prod_det_box">
        <div class="box_top"></div>
        <div class="box_center">
            <div class="prod_title">Details</div>

            <p class="details">${book.details}
            </p>
            <div class="price"><strong>PRICE:</strong> <span class="red">${book.price} $</span></div>
            <div class="price"><strong>COLORS:</strong>
                <span class="colors"><g:img src="images/color1.gif" alt="" title="" border="0"/></span>
                <span class="colors"><g:img src="images/color2.gif" alt="" title="" border="0"/></span>
                <span class="colors"><g:img src="images/color3.gif" alt="" title="" border="0"/></span></div>
            <a href="cart.html?id=${book.id}" class="more"><g:img src="images/order_now.gif" alt="" title="" border="0"/></a>
            <div class="clear"></div>
        </div>

        <div class="box_bottom"></div>
    </div>
    <div class="clear"></div>
</div>


<div id="demo" class="demolayout">

    <ul id="demo-nav" class="demolayout">
        <li><a class="active" href="#tab1">More details</a></li>
        <li><a class="" href="#tab2">Related books</a></li>
    </ul>

    <div class="tabs-container">

        <div style="display: block;" class="tab" id="tab1">
            <p class="more_details">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation.
            </p>
            <ul class="list">
                <li><a href="#">Lorem ipsum dolor sit amet, consectetur adipisicing elit</a></li>
                <li><a href="#">Lorem ipsum dolor sit amet, consectetur adipisicing elit</a></li>
                <li><a href="#">Lorem ipsum dolor sit amet, consectetur adipisicing elit</a></li>
                <li><a href="#">Lorem ipsum dolor sit amet, consectetur adipisicing elit</a></li>
            </ul>

            <p class="more_details">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation.
            </p>
        </div>

        <div style="display: none;" class="tab" id="tab2">
            <div class="new_prod_box">
                <a href="details.html">product name</a>
                <div class="new_prod_bg">
                    <a href="details.html"><g:img src="images/thumb1.gif" alt="" title="" class="thumb" border="0"/></a>
                </div>
            </div>

            <div class="new_prod_box">
                <a href="details.html">product name</a>
                <div class="new_prod_bg">
                    <a href="details.html"><g:img src="images/thumb2.gif" alt="" title="" class="thumb" border="0"/></a>
                </div>
            </div>

            <div class="new_prod_box">
                <a href="details.html">product name</a>
                <div class="new_prod_bg">
                    <a href="details.html"><g:img src="images/thumb3.gif" alt="" title="" class="thumb" border="0"/></a>
                </div>
            </div>

            <div class="new_prod_box">
                <a href="details.html">product name</a>
                <div class="new_prod_bg">
                    <a href="details.html"><g:img src="images/thumb1.gif" alt="" title="" class="thumb" border="0"/></a>
                </div>
            </div>

            <div class="new_prod_box">
                <a href="details.html">product name</a>
                <div class="new_prod_bg">
                    <a href="details.html"><g:img src="images/thumb2.gif" alt="" title="" class="thumb" border="0"/></a>
                </div>
            </div>

            <div class="new_prod_box">
                <a href="details.html">product name</a>
                <div class="new_prod_bg">
                    <a href="details.html"><g:img src="images/thumb3.gif" alt="" title="" class="thumb" border="0"/></a>
                </div>
            </div>



            <div class="clear"></div>
        </div>

    </div>

</div>

</body>
</html>