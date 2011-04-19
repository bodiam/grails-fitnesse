<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>Book Store</title>
    <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'style.css')}"/>
    <link rel="stylesheet" href="/amasun/css/lightbox.css" type="text/css" media="screen" />

    <g:layoutHead/>
    <g:javascript library="application"/>
</head>
<body>
<div id="wrap">

       <div class="header">
       		<div class="logo"><a href="index.html"><g:img src="images/logo.gif" alt="" title="" border="0" /></a></div>            
        <div id="menu">
            <ul>                                                                       
            <li class="selected"><a href="index.html">home</a></li>
            <li><a href="about.html">about us</a></li>
            <li><a href="category.html">books</a></li>
            <li><a href="specials.html">specials books</a></li>
            <li><a href="myaccount.html">my account</a></li>
            <li><a href="register.html">register</a></li>
            <li><a href="details.html">prices</a></li>
            <li><a href="contact.html">contact</a></li>
            </ul>
        </div>     
            
            
       </div> 
       
       
       <div class="center_content">
       	<div class="left_content">

            <g:layoutBody />
            
        <div class="clear"></div>
        </div><!--end of left content-->
        
        <div class="right_content">
        	<div class="languages_box">
            <span class="red">Languages:</span>
            <a href="#" class="selected"><g:img src="images/gb.gif" alt="" title="" border="0" /></a>
            <a href="#"><g:img src="images/fr.gif" alt="" title="" border="0" /></a>
            <a href="#"><g:img src="images/de.gif" alt="" title="" border="0" /></a>
            </div>
                <div class="currency">
                <span class="red">Currency: </span>
                <a href="#">GBP</a>
                <a href="#">EUR</a>
                <a href="#" class="selected">USD</a>
                </div>
                

              <div class="cart">
                  <div class="title"><span class="title_icon"><g:img src="images/cart.gif" alt="" title="" /></span>My cart</div>

           <g:if test="${session.order?.books}">

                  <div class="home_cart_content">
                  ${session.order.totalItems} x items | <span class="red">TOTAL: ${session.order.total}$</span>
                  </div>
                  <a href="cart.html" class="view_cart">view cart</a>
           </g:if>

              </div>

            	
        
        
             <div class="title"><span class="title_icon"><g:img src="images/bullet3.gif" alt="" title="" /></span>About Our Store</div> 
             <div class="about">
             <p>
             <g:img src="images/about.gif" alt="" title="" class="right" />
             Welcome to Amasun.com, a demo site created for the Gr8Conf to display the capabilities of the <a href="http://www.grails.org/plugin/fitnesse">Grails Fitnesse Plugin</a>, created by Marcin Erdmann and Erik Pragt.
             </p>
             
             </div>
             
             <div class="right_box">
             
             	<div class="title"><span class="title_icon"><g:img src="images/bullet4.gif" alt="" title="" /></span>Promotions</div> 
                    <div class="new_prod_box">
                        <a href="details.html">product name</a>
                        <div class="new_prod_bg">
                        <span class="new_icon"><g:img src="images/promo_icon.gif" alt="" title="" /></span>
                        <a href="details.html"><g:img src="images/thumb1.gif" alt="" title="" class="thumb" border="0" /></a>
                        </div>           
                    </div>
                    
                    <div class="new_prod_box">
                        <a href="details.html">product name</a>
                        <div class="new_prod_bg">
                        <span class="new_icon"><g:img src="images/promo_icon.gif" alt="" title="" /></span>
                        <a href="details.html"><g:img src="images/thumb2.gif" alt="" title="" class="thumb" border="0" /></a>
                        </div>           
                    </div>                    
                    
                    <div class="new_prod_box">
                        <a href="details.html">product name</a>
                        <div class="new_prod_bg">
                        <span class="new_icon"><g:img src="images/promo_icon.gif" alt="" title="" /></span>
                        <a href="details.html"><g:img src="images/thumb3.gif" alt="" title="" class="thumb" border="0" /></a>
                        </div>           
                    </div>              
             
             </div>
             
             <div class="right_box">
             
             	<div class="title"><span class="title_icon"><g:img src="images/bullet5.gif" alt="" title="" /></span>Categories</div> 
                
                <ul class="list">
                <li><a href="#">accesories</a></li>
                <li><a href="#">books gifts</a></li>
                <li><a href="#">specials</a></li>
                <li><a href="#">hollidays gifts</a></li>
                <li><a href="#">accesories</a></li>
                <li><a href="#">books gifts</a></li>
                <li><a href="#">specials</a></li>
                <li><a href="#">hollidays gifts</a></li>
                <li><a href="#">accesories</a></li>
                <li><a href="#">books gifts</a></li>
                <li><a href="#">specials</a></li>                                              
                </ul>
                
             	<div class="title"><span class="title_icon"><g:img src="images/bullet6.gif" alt="" title="" /></span>Partners</div> 
                
                <ul class="list">
                <li><a href="#">accesories</a></li>
                <li><a href="#">books gifts</a></li>
                <li><a href="#">specials</a></li>
                <li><a href="#">hollidays gifts</a></li>
                <li><a href="#">accesories</a></li>
                <li><a href="#">books gifts</a></li>
                <li><a href="#">specials</a></li>
                <li><a href="#">hollidays gifts</a></li>
                <li><a href="#">accesories</a></li>                              
                </ul>      
             
             </div>         
             
        
        </div><!--end of right content-->
        
        
       
       
       <div class="clear"></div>
       </div><!--end of center content-->
       
              
       <div class="footer">
       	<div class="left_footer"><g:img src="images/footer_logo.gif" alt="" title="" /><br /> <a href="http://csscreme.com/freecsstemplates/" title="free templates"><g:img src="images/csscreme.gif" alt="free templates" title="free templates" border="0" /></a></div>
        <div class="right_footer">
        <a href="#">home</a>
        <a href="#">about us</a>
        <a href="#">services</a>
        <a href="#">privacy policy</a>
        <a href="#">contact us</a>
       
        </div>
        
       
       </div>
    

</div>

</body>
</html>