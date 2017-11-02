
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
$(function(){
	$("input.sortBarPrice").keyup(function(){
		var num= $(this).val();
		if(num.length==0){
			$("div.productUnit").show();
			return;
		}
			
		num = parseInt(num);
		if(isNaN(num))
			num= 1;
		if(num<=0)
			num = 1;
		$(this).val(num);		
		
		
		var begin = $("input.beginPrice").val();
		var end = $("input.endPrice").val();
		if(!isNaN(begin) && !isNaN(end)){
			console.log(begin);
			console.log(end);
			$("div.productUnit").hide();
			$("div.productUnit").each(function(){
				var price = $(this).attr("price");
				price = new Number(price);
				
				if(price<=end && price>=begin)
					$(this).show();
			});
		}
		
	});
});
</script>
<div class="categorySortBar">
	<table class="categorySortBarTable categorySortTable">
		<tbody><tr>
			<td <c:if test="${param.sort=='all' || empty param.sort}">class="grayColumn"</c:if>><a href="?cid=${c.id}&sort=all">综合<span class="glyphicon glyphicon-arrow-down"></span></a></td>
			<td <c:if test="${param.sort=='review'}">class="grayColumn"</c:if>><a href="?cid=${c.id}&sort=review">人气<span class="glyphicon glyphicon-arrow-down"></span></a></td>
			<td <c:if test="${param.sort=='date'}">class="grayColumn"</c:if>><a href="?cid=${c.id}&sort=date">新品<span class="glyphicon glyphicon-arrow-down"></span></a></td>
			<td <c:if test="${param.sort=='saleCount'}">class="grayColumn"</c:if>><a href="?cid=${c.id}&sort=saleCount">销量<span class="glyphicon glyphicon-arrow-down"></span></a></td>
			<td <c:if test="${param.sort=='price'}">class="grayColumn"</c:if>><a href="?cid=${c.id}&sort=price">价格<span class="glyphicon glyphicon-resize-vertical"></span></a></td>
		</tr>
	</tbody></table>
	<table class="categorySortBarTable">
		<tbody><tr>
			<td><input type="text" placeholder="请输入" class="sortBarPrice beginPrice"></td>
			<td class="grayColumn priceMiddleColumn">-</td>
			<td><input type="text" placeholder="请输入" class="sortBarPrice endPrice"></td>
		</tr>
	</tbody></table>
</div>	
