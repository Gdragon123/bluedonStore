<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="easyui-panel" title="Nested Panel" data-options="width:'100%',minHeight:500,noheader:true,border:false" style="padding:10px;">
    <div class="easyui-layout" data-options="fit:true">
     <a href="javascript:importAllDocs();" class="easyui-linkbutton">导入索引库</a>
    </div>
</div>
<script type="text/javascript">
 function importAllDocs(){
	 $.ajax({
		 url:"/search/importAll",
		 method:"post",
		 success:function(data){
			 if (data.status==200) {
					$.messager.alert('提示','商品数据导入成功！');
				} else {
					
					$.messager.alert('提示','商品数据导入失败！');
				}
		 }
		 
	 })
 }
</script>

