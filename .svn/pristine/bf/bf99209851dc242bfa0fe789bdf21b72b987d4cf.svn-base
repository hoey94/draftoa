//----------------------------------------------------------------------------- 
/* 存放当前框的原始选项 */

/* 候选框(Candidate)-->当前框(Current):检查候选框中是否有选中的元素要添加到当前框 */
function check_add(selectfrom)
{
 found=0;
 for (i=0;i<selectfrom.length;i++)
 if (selectfrom.options[i].selected) { found=1; break; }
 if (found) return true;
 return false;
}
/* 当前框(Current)-->候选框(Candidate):检查当前框中是否有选中的元素要删除 */
function check_del(selectfrom)
{
 found=0;
 for (i=0;i<selectfrom.length;i++)
 if (selectfrom.options[i].selected) { found=1; break; }
 if (found) return true;
 return false;
}
/* 添加操作 */
function myadd(selectfrom,selectto,b1,b2)
{
  if (check_add(selectfrom))
  {
    moveSelected(selectfrom,selectto);
    buttonChanged(b1,b2);
  }
  //测试：
  /* 生成返回串 */
  var s="";
  if (selectto.length>0)
  {
   for (i=0;i<selectto.length;i++)
   {
     s+=selectto.options[i].value+",";
   }
   formId.resultvalue.value=s;
  }
}
/* 删除操作 */
function mydel(selectfrom,selectto,b1,b2)
{
 if (check_del(selectfrom))
 {
    moveSelected(selectfrom,selectto);
    buttonChanged(b1,b2);
 }
 //测试：
  /* 生成返回串 */
  var s="";
  //if (selectfrom.length>0)
  //{
   for (i=0;i<selectfrom.length;i++)
   {
     s+=selectfrom.options[i].value+",";
   }
   formId.resultvalue.value=s;
  //}
}
/* 克隆一个选项 */
function cloneOption(option) {
  var out = new Option(option.text,option.value);
  out.selected = option.selected;
  out.defaultSelected = option.defaultSelected;
  return out;
}
/* 移动选中的元素 */
function moveSelected(from,to) 
{
  newTo = new Array();
  newFrom = new Array();

  for(i=from.options.length - 1; i >= 0; i--) 
  {
    if (from.options[i].selected) 
	{
      newTo[newTo.length] = cloneOption(from.options[i]);
    }
    else
    {
      newFrom[newFrom.length] = cloneOption(from.options[i]);
    }
  }

  for(i=to.options.length - 1; i >= 0; i--) 
  {
    newTo[newTo.length] = cloneOption(to.options[i]);
    newTo[newTo.length-1].selected = false;
  }

  to.options.length = 0;
  from.options.length = 0;

  for(i=newTo.length - 1; i >=0 ; i--) 
  {
    to.options[to.options.length] = newTo[i];
  }

  for(i=newFrom.length - 1; i >=0 ; i--) 
  {
    from.options[from.options.length] = newFrom[i];
  }
}
/* 非焦点列表框中所有元素选中状态清为未选中 */
function clearSelected(unfocusedElement) 
{
  for(i=0; i<unfocusedElement.options.length; i++) {
    unfocusedElement.options[i].selected=false;
  }
}
/* 改变按钮图标 */
function buttonChanged(b1,b2)
{
  var a1 = document.getElementById(b1);
  var a2 = document.getElementById(b2);
  a1.disabled = true;
  var s1 = a1.src.indexOf("_disabled.gif");
  if (s1==-1)
  {
    s1 = a1.src.indexOf(".gif");
	a1.src = a1.src.substring(0,s1)+"_disabled.gif";
  }
  a2.disabled = false;
  var s2 = a2.src.indexOf("_disabled.gif");
  if (s2!=-1)
  {
    a2.src = a2.src.substring(0,s2)+".gif";
  }
}
//-----------------------------------------------------------------------------
