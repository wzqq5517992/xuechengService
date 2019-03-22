#mysql数据库函数

#单行函数

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

## 11.date_format() 日期类型转化为字符串

Eg: select EMPNO,ENAME,date_format(HIRDDATE,"%Y-%m-%d")  from EMP

#多行函数

定义：每次将多条记录当作参数输入到函数内，返回的是 单行的结果，也称为聚合函数或组函数

注释：多行函数可自动忽略null

​            多行函数不能出现在where子句中

​             多行函数不能嵌套

##12.SUM()函数 

对数值型数据进行求和

Eg: select sum(sal) as sums from EMP

注释：注意对null的判断加减

Eg: select  sum(SAL+IFNULL(COMM,0)) as. TOTAL1   (Sum(SAL)+Sum(COMM))  as. TOTAL2     From EMP

## 13.COUNT函数

查询数据总数

COUNT(*) 查询所有记录的总数

COUNT(字段)查询指定字段不为空的数据总数

select COUNT(*). From EMP

## 14.AVG()函数

求某个字段的平均值

Select AVG(SAL)   as  TOTALALL1,SUM(SAL)/COUNT(SAL). as   TOTALALL2. from. EMP

## 15.MAX()函数

适用于三种主要类型 数值型，字符串型，日期型

其中字符串型的大小规则与java中一样，即Unicode编码的大小来决定。如A的编码是97 b为98

数值型：select   MAX(SAL) as  MAXSAL from EMP

字符串型：select   MAX(NAME) as  MAXNAMEfrom EMP

日期型：select   MAX(DATE) as  MAXDATE from EMP

##16.MIN()函数

同15一样的规则

##17.组函数注意事项

嵌套问题：组函数之间不可进行相互嵌套

组函数与where：组函数不可以在where后使用,因为组函数的入参是多个记录

注释：查询工资最高的员工姓名及最薪资时，查询列表的所有字段都要出现在组函数中，否则查出来的数据是错误的

##18.distinct关键字的使用

去除查询结果中重复的记录 只能出现在select之后  查询列表前面

去除单列重复的数据：

select   distinct  JOB from EMP

去除多列的重复数据：

select   distinct  JOB ,DEPTID from EMP

##19.分组

即按照特定字段氛围多组，然后分别使用组函数进行查询

分组查询规则：

分组字段可出现在查询列表，组函数，group  by

可按照字段别名进行分组

把所有员工按照部门分组然后查询每组的最高工资：

select DEPTNO, MAX(SAL)   MAXSAL  from  EMP    Group by   DEPTNO  Having 

按照职务分组查询每组的最高工资，最低工资，平均工资，最大雇佣日期

select   JOB,  MAX(SAL) , MIN(SAL), AVG(SAL), MAX(HIREDATE)  From EMP group by  JOB

按照部门分组查询部分的最高薪资及最高工资的员工姓名

