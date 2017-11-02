<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<script>
	$(function() {
		$(".addFormSingle").submit(function(){
			if (!checkEmpty("filepathSingle","图片文件")) {
				return false;
			}
		});
		$(".addFormDetail").submit(function(){
			if (!checkEmpty("filepathDetail","图片文件")) {
				return false;
			}
		});
	});
</script>

<title>产品图片管理</title>


<div class="workingArea">

	<ol class="breadcrumb">
	  <li><a href="admin_category_list">所有分类</a></li>
	  <li><a href="admin_product_list?cid=${p.category.id}">${p.category.name}</a></li>
	  <li class="active">${p.name}</li>
	  <li class="active">产品图片管理</li>
	</ol>



	<table class="addPictureTable" align="center">
		<tr>
			<td class="addPictureTableTD">
				<div>
					<div class="panel panel-warning addPictureDiv">
						<div class="panel-heading">
							新增产品<b class="text-primary">单个</b>图片
						</div>
						<div class="panel-body">
							<form class="addFormSingle" method="post" action="admin_productImage_add" enctype="multipart/form-data">
								
								<table class="addTable">
									<tr>
										<td>请选择本地图片 尺寸400x400为佳</td>
									</tr>
									<tr>
										<td><input id="filepathSingle" accept="image/*" type="file" name="filepath" /></td>
									</tr>
									<tr>
										<td>
											<input type="hidden" name="type" value="type_single" />
											<input type="hidden" name="pid" value="${p.id}" />
											<button type="submit" class="btn btn-success">提交</button>
										</td>
									</tr>
								</table>
							</form>
						</div>
					</div>
					<table class="table talbe-striped table-bordered table-hover talbe-condensed">
						<thead>
							<tr class="success">
								<th>ID</th>
								<th>产品单个图片缩略图</th>
								<th>删除</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${singleImages}" var="img">
								<tr>
									<td>${img.id}</td>
									<td><a href="/pic/tmall_img/productSingle/${img.id}.jpg" title="点击查看原图"><img height="50px" src="/pic/tmall_img/productSingle/${img.id}.jpg" /></a></td>
									<td><a href="admin_productImage_delete?id=${img.id}" deleteLink="true"><span class="glyphicon glyphicon-trash"></span></a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</td>
			
			<td class="addPictureTableTD">
				<div>
					<div class="panel panel-warning addPictureDiv">
						<div class="panel-heading">
							新增产品<b class="text-primary">详情</b>图片
						</div>
						<div class="panel-body">
							<form class="addFormDetail" method="post" action="admin_productImage_add" enctype="multipart/form-data">
								
								<table class="addTable">
									<tr>
										<td>请选择本地图片 宽度790为佳</td>
									</tr>
									<tr>
										<td><input id="filepathDetail" accept="image/*" type="file" name="filepath" /></td>
									</tr>
									<tr>
										<td>
											<input type="hidden" name="type" value="type_detail" />
											<input type="hidden" name="pid" value="${p.id}" />
											<button type="submit" class="btn btn-success">提交</button>
										</td>
									</tr>
								</table>
							</form>
						</div>
					</div>
					<table class="table talbe-striped table-bordered table-hover talbe-condensed">
						<thead>
							<tr class="success">
								<th>ID</th>
								<th>产品详情图片缩略图</th>
								<th>删除</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${detailImages}" var="img">
								<tr>
									<td>${img.id}</td>
									<td><a href="/pic/tmall_img/productDetail/${img.id}.jpg" title="点击查看原图"><img height="50px" src="/pic/tmall_img/productDetail/${img.id}.jpg" /></a></td>
									<td><a href="admin_productImage_delete?id=${img.id}" deleteLink="true"><span class="glyphicon glyphicon-trash"></span></a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</td>
			
		</tr>
	</table>

</div>
<%@include file="../include/admin/adminFooter.jsp"%>
