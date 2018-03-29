<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <ul id="contentCategory" class="easyui-tree">  </ul>
</div>
<div id="contentCategoryMenu" class="easyui-menu" style="width:120px;" data-options="onClick:menuHandler">
    <div data-options="iconCls:'icon-add',name:'add'">添加</div>
    <div data-options="iconCls:'icon-remove',name:'rename'">重命名</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'icon-remove',name:'delete'">删除</div>
</div>
<script type="text/javascript">
$(function(){
	//id选择器  获取ul标签，创建一棵树
	$("#contentCategory").tree({
		url : '/content/category/list',
		animate: true,
		method : "GET",
		//右击鼠标触发的业务逻辑
		onContextMenu: function(e,node){
			//取消默认的鼠标右击事件
            e.preventDefault();
			//选中被右击鼠标点击的节点
            $(this).tree('select',node.target);
			//获取菜单项 展示出来
            $('#contentCategoryMenu').menu('show',{
            	//设置菜单栏显示的位置，就是鼠标所在的位置
                left: e.pageX,
                top: e.pageY
            });
        },
        //在编辑之后触发的事件
        //node就是传递过来的要编辑的节点
        onAfterEdit : function(node){
        	//获取树本身
        	var _tree = $(this);
        	//判断是否是新增节点
        	if(node.id == 0){
        		// 如果是，就发送请求插入数据库中（内容分类表中）
        		$.post("/content/category/create",{parentId:node.parentId,name:node.text},function(data){
        			if(data.status == 200){
        				//更新新增节点的id的值
        				_tree.tree("update",{
            				target : node.target,
            				//taotaoResutl中有一个data的对象属性，data对象中有id
            				id : data.data.id
            			});
        			}else{
        				$.messager.alert('提示','创建'+node.text+' 分类失败!');
        			}
        		});
        	}else{
        		//不是，即修改
        		$.post("/content/category/update",{id:node.id,name:node.text});
        	}
        }
	});
});

//点击事件
function menuHandler(item){
	//获取树
	var tree = $("#contentCategory");
	//获取树上被选中的节点
	var node = tree.tree("getSelected");
	//判断点击的是哪个菜单项  (添加，重命名，删除)
	if(item.name === "add"){ //=== 判断类型和值是否相同  == 判断值是否相同
		//在树上添加一个节点
		tree.tree('append', {
			//定义要添加的节点的父节点，就是右击鼠标额节点
            parent: (node?node.target:null),
            //要新增的节点的数据
            data: [{
                text: '新建分类',
                id : 0,
                parentId : node.id
            }]
        }); 
		//获取id为0的节点 ，就是要新增的节点
		var _node = tree.tree('find',0);//根节点
		//选中新增的节点，开始编辑
		tree.tree("select",_node.target).tree('beginEdit',_node.target);
	}else if(item.name === "rename"){ //重命名
		tree.tree('beginEdit',node.target);
	}else if(item.name === "delete"){ //删除
		$.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){
			if(r){
				$.post("/content/category/delete/",{id:node.id},function(){
					tree.tree("remove",node.target);
				});	
			}
		});
	}
}
</script>