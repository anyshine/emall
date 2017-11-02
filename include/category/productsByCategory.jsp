
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
	

<%-- <c:if test="${empty param.categorycount}">
	<c:set var="categorycount" scope="page" value="100"/>
</c:if>

<c:if test="${!empty param.categorycount}">
	<c:set var="categorycount" scope="page" value="${param.categorycount}"/>
</c:if> --%>
	
<div class="categoryProducts">
	<c:forEach items="${c.products}" var="p">
		<div price="${p.promotePrice}" class="productUnit">
			<div class="productUnitFrame">
				<a href="foreproduct?pid=${p.id}">
					<img width="100px" src="/pic/tmall_img/productSingle_middle/${p.firstProductImage.id}.jpg" class="productImage">
				</a>
				<span class="productPrice">¥<fmt:formatNumber value="${p.promotePrice}" pattern="0.00" /></span>
				<a href="foreproduct?pid=${p.id}" class="productLink">
				 ${p.name}
				</a>
				<a href="foreproduct?pid=${p.id}" class="tmallLink">天猫专卖</a>
				<div class="show1 productInfo">
					<span class="monthDeal ">月成交 <span class="productDealNumber">${p.saleCount}笔</span></span>
					<span class="productReview">评价<span class="productReviewNumber">${p.reviewCount}</span></span>
					<span class="wangwang">
					<a href="#nowhere" class="wangwanglink">
						<img src="img/site/wangwang.png">
					</a>
					</span>
				</div>
			</div>
		</div>
	</c:forEach>
		<div style="clear:both"></div>
</div>
