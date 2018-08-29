<html>
<body>
<h2>Hello World!</h2>
springMVC上传文件
<form name="form1" action="/manage/product/upload.do" enctype="multipart/form-data" method="post">
    <input type="file" name="upload_file"/>
    <input type="submit" value="upload"/>
</form>
富文本上传文件
<form name="form2" action="/manage/product/richtext-img-upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="upload"/>
</form>
</body>
</html>
