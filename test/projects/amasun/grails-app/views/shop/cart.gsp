<%@ page import="nl.jworks.amasun.domain.book.Book" %>
<html>
<head>
    <title>Featured books</title>
    <meta name="layout" content="shop"/>
</head>


<div class="title"><span class="title_icon"><g:img src="images/bullet1.gif" alt="" title="" /></span>My cart</div>

<div class="feat_prod_box_details">
            
<table class="cart_table">
    <tr class="cart_title">
        <td>Item pic</td>
        <td>Book name</td>
        <td>Unit price</td>
        <td>Qty</td>
        <td>Total</td>
    </tr>

    <g:each in="${order.contents}" var="entry">
        <g:set var="book" value="${entry.key}" />
        <g:set var="amount" value="${entry.value}" />

    <tr>
        <td><a href="details.html?id=${book.id}"><g:img src="images/cart_thumb.gif" alt="" title="" border="0" class="cart_thumb" /></a></td>
        <td>${book.title}</td>
        <td>${book.price}$</td>
        <td>${amount}</td>
        <td>${book.price * amount}$</td>
    </tr>

    </g:each>
<!--

    <tr>
        <td><a href="details.html"><g:img src="images/cart_thumb.gif" alt="" title="" border="0" class="cart_thumb" /></a></td>
        <td>Books</td>
        <td>100$</td>
        <td>1</td>
        <td>100$</td>
    </tr>
    <tr>
        <td><a href="details.html"><g:img src="images/cart_thumb.gif" alt="" title="" border="0" class="cart_thumb" /></a></td>
        <td>Books</td>
        <td>100$</td>
        <td>1</td>
        <td>100$</td>
    </tr>
-->
    <tr>
    <td colspan="4" class="cart_total"><span class="red">TOTAL BEFORE DISCOUNT:</span></td>
    <td> ${order.subtotal}$</td>
    </tr>
                
    <tr>
    <td colspan="4" class="cart_total"><span class="red">DISCOUNT:</span></td>
    <td> ${order.discount}$</td>
    </tr>

    <tr>
    <td colspan="4" class="cart_total"><span class="red">TOTAL:</span></td>
    <td> ${order.total}$</td>
    </tr>

</table>
<a href="#" class="continue">&lt; continue</a>
<a href="#" class="checkout">checkout &gt;</a>
            

             
            
</div>
            
              

</body>
</html>