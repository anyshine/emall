<!-- 模仿天猫整站j2ee 教程 为how2j.cn 版权所有-->
<!-- 本教程仅用于学习使用，切勿用于非法用途，由此引起一切后果与本站无关-->
<!-- 供购买者学习，请勿私自传播，否则自行承担相关法律责任-->

<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<div class="homepageCategoryProducts">
	<c:forEach items="${cs}" var="c" varStatus="stc">
		<div class="eachHomepageCategoryProducts">
			<div class="left-mark"></div>
			<span class="categoryTitle">${c.name}</span>
			<div style="clear:both"></div>
			<c:forEach items="${c.products}" var="p" varStatus="stp">
				<c:if test="${stp.count<=5}">
					<div class="productItem">
						<a href="foreproduct?pid=${p.id}"><img width="100px" src="/pic/tmall_img/productSingle_middle/${p.firstProductImage.id}.jpg"></a>
						<a href="foreproduct?pid=${p.id}" class="productItemDescLink">
							<span class="productItemDesc">[热销]
							${fn:substring(p.name,0,20)}
							</span>
					    </a>
						<span class="productPrice">
							<fmt:formatNumber value="${p.promotePrice}" pattern="0.00" />
						</span>
					</div>
				</c:if>
			</c:forEach>
			<div style="clear:both"></div>
	</c:forEach>

	<img src="img/site/end.png" class="endpng" id="endpng">
</div>
<%-- <c:if test="${empty param.categorycount}">
	<c:set var="categorycount" scope="page" value="100"/>
</c:if>

<c:if test="${!empty param.categorycount}">
	<c:set var="categorycount" scope="page" value="${param.categorycount}"/>
</c:if>

<div class="homepageCategoryProducts">
	<c:forEach items="${cs}" var="c" varStatus="stc">
		<c:if test="${stc.count<=categorycount}">
			<div class="eachHomepageCategoryProducts">
				<div class="left-mark"></div>
				<span class="categoryTitle">${c.name}</span>
				<br>
				<c:forEach items="${c.products}" var="p" varStatus="st">
					<c:if test="${st.count<=5}">
						<div class="productItem" >
							<a href="foreproduct?pid=${p.id}"><img width="100px" src="img/productSingle_middle/${p.firstProductImage.id}.jpg"></a>
							<a class="productItemDescLink" href="foreproduct?pid=${p.id}">
								<span class="productItemDesc">[热销]
								${fn:substring(p.name, 0, 20)}
								</span>
						    </a>
							<span class="productPrice">
								<fmt:formatNumber type="number" value="${p.promotePrice}" minFractionDigits="2"/>
							</span>
						</div>
					</c:if>				
				</c:forEach>
				<div style="clear:both"></div>
			</div>
		</c:if>
	</c:forEach>
	
	
	<img id="endpng" class="endpng" src="img/site/end.png">

</div> --%>