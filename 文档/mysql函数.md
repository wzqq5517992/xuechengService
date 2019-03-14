#mysql数据库函数

##1.LOWER（str）转化为小写字符

Eg: select  ENAME  "大写别名" , LOWER(ENAME)  "小写别名" from   EMF

BINARY加在字段后 可让其严格区分大小写

##2.UPPER（str）转化为大写字符

Eg: select  ENAME  "小写别名" , UPPER(ENAME)  "大写别名" from   EMF

##3.截取字符串SUBSTR()

substr（截取字段名称,起始下标,截取长度）

Eg:select ENAME  SUBSTR(ENAME,2,1) form. EMF where   SUBSTR(ENAME,2,1)="A" 

即 查询ENAME的第二个字符

也可以使用模糊查询： select * from EMF where ENAME LIKE  "_A%"

##4.LENGTH(str)返回字符型数据的长度

Eg: select  ENAME  "员工的姓名",LENGTH(ENAME)  From   EMF

##5.TRIM(str)去掉首位空格

##6.ROUND对数值进行四舍五入

double(7,2). 数值总长度是7位，小数点后占俩位，若小数点后占三位则自动四舍五入

Eg: select ENAME,SAL "原来的工资",ROUND(SAL，1)  “精确小数点后一位的工资” FROM. EMP

注释：ROUND(SAL,-1)为精确到十位的工资 （0为个位） 默认就是精确到个位

## 7.RAND() 产生0-0.9999之间的小数

Eg:  select ROUND(RAND()*99+1).   产生一个1-100的随机数

## 8. IFNULL() 专门用来处理空值

Eg: select ENAME,EMPNO,SAL,COMM,(SAL+ IFNULL(COMM,0))*12 "年收入" FROM  EMP

##9.CASH WHEN  分支语句函数

Eg:  select EMPNO,ENAME, JOB,SAL AS "原来的薪资"，(CASE JOB WHEN "MANAGE"  THEN SAL*1.1 WHEN "SALEMAN" THEN  SAL*  *1.5  ELSE SAL END) AS  "判断后的薪资" FROM EMP;

## 10.str_to_date 将字符串数据转化为日期数据

Eg: select  * from EMP where HIRDDATE=str_to_date("2018-09-09","%Y-%m-%d");    

注释:mysql中where条件后的字符串会自动转化为date类型；

NOW()可获取当前日期。 返回类型为date

## 11 date_format() 日期类型转化为字符串

Eg: select EMPNO,ENAME,date_format(HIRDDATE,"%Y-%m-%d")  from EMP











