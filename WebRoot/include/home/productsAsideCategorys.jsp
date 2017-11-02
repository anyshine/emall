

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<script type="text/javascript">
$(function(){
	$("div.productsAsideCategorys div.row a").each(function(){
		var v = Math.round(Math.random() * 6);
		if(v == 1) {
			$(this).css("color","#87CEFA");
		}
	});
});
</script>
<div style="position: relative;left: 0;top: 0;">
	<c:forEach items="${cs}" var="c">
		<div class="productsAsideCategorys" cid="${c.id}">
			<c:forEach items="${c.productsByRow}" var="ps">
				<div class="row show1">
					<c:forEach items="${ps}" var="p">
						<c:if test="${not empty p.subTitle}">
							<a href="foreproduct?pid=${p.id}">
								${fn:substringBefore(p.subTitle," ")}
							</a>
						</c:if>
						
					</c:forEach>
					<div class="seperator"></div>
				</div>
			</c:forEach>
		</div>
	</c:forEach>
</div>    
