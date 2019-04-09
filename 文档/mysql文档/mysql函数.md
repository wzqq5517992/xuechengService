#  mysql数据库函数

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

##20.HAVING关键词

按照部门分组查询平均薪资大于2000的记录

select DEPTNO,AVG(SAL) AVGSAL from EMP group by DEPTNO HAVING  AVGSAL>2000

#连接查询即多表查询

外键：子表（一对多的多）的外键值的必须在主表的主键值范围内；

​            子表的外键值可重复 可以为null

##1.多表查询分类

###1.1 按照连接方式分类

内连接:满足查询一一对应关系的数据；比如员工所属部门，部门下所拥有的员工，这样满足以一一对应

外连接:不满足一一对应的关系；比如有的员工没有所属部门，有的部门没有员工；

###1.2按照语法年代分类

1992: from可以写多个表名用逗号隔开

1999:from后面只能跟单个表，其他表用join来连接

###2.1内连接分类

   ***等值连接***：建立在父子表关系上，用等号来连接俩个表

eg:查询员工表及其部门表且姿薪资在1600-3000

1992语法：select E.EMPNO,E.ENAME,E.DEPTNO,D.DEPTNAME from  EMP E ,DEPT D

​                    where (E.DEPTID=D.DEPTID). AND (E.SAL BETWEEN 1600 ADN 3000)

1999语法：select E.EMPNO,E.ENAME,E.DEPTNO,D.DEPTNAME from  EMP E  inner join DEPT D

​                      ON   E.DEPTID=D.DEPTID. WHERE  E.SAL BETWEEN  1600 ADN 3000

  ***非等值连接***：俩个表没有父子关系用非等号连接俩个表

eg:  查询俩个非关系表

1992语法：select. E.EMPNO,E.SAL,S.LOSAL,S.HISAI    from EMP E, SAKGRADE S

​                      where (E.SAL between  S.LOSAL  and. s.HISAL) 

1999语法：select. E.EMPNO,E.SAL,S.LOSAL,S.HISAI    from EMP E inner join SAKGRADE S  on

​                    E.SAL between  S.LOSAL  and. s.HISAL

​    ***自连接***：使用别名将一个表虚拟成俩个表，然后做等值连接

   1992语法：select E.EMPNO, "员工编号",E.ENAME “员工姓名”,M.EMPNO  "经理编号",M.ENAME “经理姓名”                      

​                        from EMP E,EMP M  where E.MGR=M.EMPNO

   1999语法：select E.EMPNO, "员工编号",E.ENAME “员工姓名”,M.EMPNO  "经理编号",M.ENAME “经理姓名”                      

​                        from EMP E   inner join  EMP  M  on    E.MGR=M.EMPNO

##2.2外连接分类

​                                                                                                                                             查询没有一一对应的数据，比如 某个员工没有所属部门，某个部门没有员工；

使用外连接查询数据的条数大于等于内连接查询数据的条数；

数据库会把不匹配的数据设置为NULL

外连接中的outer关键字可以省略

***左外连接( left outer join)***： 右表不满足对应关系的字段值设为null显示出来

Select E.ENAME,E.JOB,D.DEPTID,D.DEPTNAME from EMP left  join DEPT D on E.DEPTID=D.DEPTID

***右外连接(right outer join)***：左表不满足对应关系的字段值设为null显示出来

***全外连接(full. Outer join)***：俩个表不满足对应关系的值都设置为null显示出来(mysql不支持全外连接，可食用union all连接左右外连接实现)



#子查询

用来给主查询提供查询条件或查询数据而优先执行的sql查询语句，子查询必须放在“()”内；

Eg：查询工资比平均工资低的员工

select * from EMP where SAL<(select AVG(SAL) avg  from EMP)

## 1.子查询分类

1.1出现在where中的子查询，用来给主查询提供查询条件接口

1.2出现在from后面的子查询，用来给主查询提供数据，也就是先执行子查询，然后吧子查询的数据当作一张

​     虚拟的表，然后主查询从虚拟表中查询数据

1.3出现在查询列表中的子查询，功能类似于外连接的效果

易错查询：

***查询每个部门最高薪水的人员名称. (子查询和连标查)***

 select   T.DEPTNO,T.MAXSAL,E.ENAME  from  EMP E  

join ( select DEPTNO, MAX(SAL)  MAXSAL   from EMP gurop by DEPTNO)  T 

 on   (E.DEPTID=T.DEPTID and E.SAL=T.MAXAL) 

***查询每个部门薪资在平均薪资之上的员工***

思路：第一步按照部门编号查询各部门的平均薪资，第二步把第一步的结果当作临时表T做连表查询

 select  DEPTNO, AVG(SAL)   AVGSAL from EMP  group by DEPTNO

最终结果：

select E.DEPTNO,    E.ENAME,E.SAL    from  EMP E    inner   join   (select  DEPTNO, AVG(SAL)   AVGSAL from EMP  group by DEPTNO )   as  T

 on  (E.DEPTNO=T.DEPTNO where  E.SAL>T.AVGSAL ) 

###2.UNION的使用

*把俩个查询结果的结果合并成一个查询结果；要求结果集的结构必须一致(字段类型，字段顺序，字段个数)*

2.1 UNION:：合并相同的数据

Select E.NAME FROM EMP E. WHERE SAL>200. UNION SELECT F.ENAME FROM EMP F WHERE SAL>3000

2.2UNION  ALL：不合并相同的数据

###3.limit的使用

*可查询表中最前面的几条记录或者中间某几条记录，比如 LIMIT M,N  M代表索引从0开始  N代表连续的条数 M为0时可以省略*

eg：查询工资最高的前4个前四个员工

select E.ENAME  FROM EMP  ORDER BY E.SAL DESC  LIMIT 0,4

eg：查询入学最早的前五个员工

select * from EMP. ORDER BY ASC. LIMIT 5

实现分页 （pageNO-1)*pageSize, pageSize => M,N

###4.insert update delete 略



#DDL语句

包括创建表，删除表，修改表

ddl语句执行时数据库返回0

### 1.创建表（create  table ）

CREATE TABLE  tableName(

Id,int(10),

name  varchar(20)；

);set charcter_set_result='utf-8'

### 2.删除表

 drop   tablename 

Drop table if exists  Stus(只能子在mysql中使用)

###********建表和查询语句结合使用（即复制表中指定的数据）********

将查询出来的数据放到新建的表中，如果查询语句的结果集为空 则新建的是空表，数据为空

CREATE  TABLE EMP_INFO 

AS

select E.ENAME,E.DEPTNO,D.DEPTNAME,E.JOB FROM EMP E INNER JOIN  DEPT. D ON E.DEPTNO=D.DEPTNO WHERER E.DEPTNO LIKE '%125'

###****插入语句和查询语句结合使用******

注意：插入语句的字段顺序，类型，个数要和查询一句的字段个数一致才能正确执行

INSERT INTO EMP_BAK  SELECT * FROM EMP WHERE DEPTNO>20

### 3.ALTER语句

在不影响表数据的情况下修改表，主要修改表的字段对字段进行增删改

查询表结构

DESC. 表名

添加字段： 为表结构增加列

ALTER TABLE 表名 ADD 字段名  BIGINT(11);

修改字段：1.当表中某一字段没有数据时，可以修改其字段类型及字段长度；

ALTER TABLE 表名 MODIFY  字段名  CHAR(11) 

​                   2.当表中该字段有数据时，增大字段长度时可以的

ALTER TABLE 表名 MODIFY  字段 名 CHAR(20) 

​                   3.当表中该字段有数据时，减小字段长度时需要根据数据的最大值长度来决定

ALTER TABLE 表名 MODIFY  字段名  CHAR(5) 

​                   4.当表中该字段有数据时，修改字段的类型要看数据能否转化为新类型

删除字段：删除表中某一列

ALTER TABLE 表名  DROP  字段名

###4.字段约束

即加载表上面的（字段上的检测条件）检测条件，只有符合条件的数据才能被操作（增删改）

####*约束分类：*

1.列级约束：作用在单独某一列的约束条件；

2.表级约束：作用在多个列上的约束条件

####*创建约束时间*

1.创建表的同时船桨约束条件

2.建表后，使用ALTER单独给表中添加约束

####*约束分类：*

1.非空约束(NOT NULL)

2.唯一约束(UNQUE)

3.主键约束(PRIMARY KEY)

4.外键约束(FOREIGN KEY)

*注释：*

在一个表中可以同时有多个非空约束，多个唯一约束，多个外键约束，但主键约束只能有一个，非空约束时唯一的一个列级约束；

#####*非空约束：*

即字段数据不能为空 

eg：  CREATE TABLE STUS(

​     SID          INT(4),

​     SNAME  CHAR(4)  NOT NULL, 

​      AGE        INT(3)    

);

插入，更新数据时sname不能为空

#####*唯一约束：*

一个唯一约束可以作用在单列字段也可以作用在多列字段，所以它时一个表级约束，唯一约束保证字段的数组或字段组合的数据不能重复，但是可以为空

1.作用在单列字段上：保证字段的数据不能重复，但可为空即多条数据都为NULL。  

eg：  CREATE TABLE STUS(

​     SID          INT(4),

​     SNAME  CHAR(4)  NOT NULL, 

​      AGE        INT(3).

​      TEL.         CHAR(11)   UNIQUE     —给TEL字段添加唯一约束    

);

创建约束的时候也可以使用标准方式，也就是给约束命名

约束和表一样都是独立的数据库对象，保存在数据库的系统表中

标准方式：约束的关键字+约束名称+约束类型+作用字段

eg：  CREATE TABLE STUS(

​     SID          INT(4),

​     SNAME  CHAR(4)  NOT NULL, 

​      AGE        INT(3).

​      TEL.         CHAR(11),  

​      CONSTRAINT  TEL_UNIQUE  UNIQUE (TEL),  

​       CONSTRAINT  TEL_UNIQUE  UNIQUE (SNAME)    

);

一个唯一约束可以同时作用在一个表中的多个字段保证字段组合的数据不能重复,但是可以为空

eg：  CREATE TABLE STUS(

​     SID          INT(4),

​     SNAME  CHAR(4)  NOT NULL, 

​      AGE        INT(3).

​      TEL.         CHAR(11),  

​      CONSTRAINT  TEL_UNIQUE  UNIQUE   (TEL,SNAME)  

);

#####*非空且唯一约束：* 

#####在一个字段上面可以同时又多个约束，如保证字段数据不重复且部位空

eg：  CREATE TABLE STUS(

​     SID          INT(4),

​     SNAME  CHAR(4)  NOT NULL, 

​      AGE        INT(3).

​      TEL.         CHAR(11),  

​      CONSTRAINT  TEL_UNIQUE  UNIQUE (SNAME)  

);

#####*主键约束：*

##### 从功能上说相当于非空唯一约束

主键约束和非空唯一约束的区别：

1.在一个表中，可以同时又多个非空唯一约束，但是只能又一个唯一的逐渐约束

2.数据库会自动为主键约束创建索引，不会为非空且唯一约束创建索引

######主键约束的相关名字

1.主键约束：是一个约束对象

2.主键字段：主键约束所在的字段称为主键字段，如表中的主键

3.主键值：主键字段的数值

4.主键约束的作用：保证数据的唯一性且不为空，通过主键值可以确定为宜的一条记录

**主键约束可以作用在单列字段上，也可以作用在多列字段上面，称为联合主键**

创建主键约束的表

eg：  CREATE TABLE STUS(

​     SID          INT(4)   PRIMARY KEY,

​     SNAME  CHAR(4)  NOT NULL, 

​      AGE        INT(3),

​      TEL         CHAR(11)

);

**联合主键：**

主键约束同时作用在多列字段上面，保证字段组合的数据不能重复，并且都不能为空，联合主键必须要使用标准方式创建

eg：  CREATE TABLE STUS(

​     SID          INT(4) ,

​     SNAME  CHAR(4)  NOT NULL, 

​      AGE        INT(3),

​      TEL         CHAR(11),

​     CONSTRAINT   STUS_PK   PRIMARY KEY   (SID,SNAME)  

);

**主键自增（mysql中存在） **

eg：  CREATE TABLE STUS(

​     SID          INT(4)   AUTO INCREMENT,   —自增字段 从1开始

​     SNAME  CHAR(4)  NOT NULL, 

​      AGE        INT(3),

​      TEL         CHAR(11),

​     CONSTRAINT   STUS_PK   PRIMARY KEY   (SID)  

);

SID使用了自增，在插入数据的时候不需要插入主键值，数据库会自己维护主键值

#####外键约束：

实体和实体之间又一对多，多对一，在数据库中通过外键来体现实体间的关系

父表中的主键或唯一键字段，才能被子表引用为外键

外键约束相关名称：

1.外键约束：是一个数据库对象，也就是约束

2.外键字段：外键约束所在的字段，如emp表中的外键约束作用在deptno上，则deptno就是外键字段

3.外键值：外键字段的数值

外键约束的作用：

1.子表的外键值必须要父表的主键值范围内

2.外键约束的值可以重复，可以为空

3.一个表中可以有多个外键约束

4.可以作用在单列字段，也可以作用在多列字段

eg：

创建父表

 CREATE TABLE DEPT (

​    DEPTNO     INT(4)  PRIMARY KEY,

​    DNAME   VARCHAR(10),

​    LOC   VARCHAR(20)         

);

创建子表

CREATE. TABLE. EMP1(

  EMPNO  INT(4)   PRIMARY KEY,

  ENAME   VARCHAR(10),

  JOB        VARCHAR(10),

  MGR       INT(4),

  DEPTID     INT(4)     —用来做外键字段 外键的名称和父表的主键名称可以不同

  CONSTRAINT   EMP_DEPT_FK   FOREIGN  KEY (DEPTID)  REFERENCES DEPT  (DEPTNO)  --外键的创建只能使用约束的标准模式创建

);

*注释：*

如果我们要更新父表的主键值，我们需要先把子表的外键值设置为NULL，然后再更新父表主键值，然后再把子表的外键值更新成父表的最新主键值

**外键的主要作用是保障数据的安全，但是会影响数据库的执行性能，很多情况下数据库不设置外键 而是在java代码中通过逻辑判断保障数据的安全性**

#####*级联操作*

包括级联更新和级联删除，在外键约束的条件下，我们不能直接操作父表数据（更新父表的主键值或删除父表数据），我们可以设置级联操作，也就是在操作父表的数据时，连同子表的相关数据一起操作

注释：级联操作必须建立在外键之上，必须先建立外键，才能指定级联操作

1.级联更新(ON UPDATE CASCADE)：

eg：

创建父表

 CREATE TABLE DEPT (

​    DEPTNO     INT(4)  PRIMARY KEY,

​    DNAME   VARCHAR(10),

​    LOC   VARCHAR(20)         

);

创建子表

CREATE. TABLE. EMP1(

  EMPNO  INT(4)   PRIMARY KEY,

  ENAME   VARCHAR(10),

  JOB        VARCHAR(10),

  MGR       INT(4),

  DEPTID     INT(4)     —用来做外键字段 外键的名称和父表的主键名称可以不同

--外键的创建只能使用约束的标准模式创建

—创建级联更新

  CONSTRAINT   EMP_DEPT_FK   FOREIGN  KEY (DEPTID)  REFERENCES DEPT  (DEPTNO)  ON UPDATE CASCADE

);

父表的外键字段的值改变后，子表的外键字段的值也跟着改变

2.级联删除( ON DELETE CASCADE)：

eg：

创建父表

 CREATE TABLE DEPT (

​    DEPTNO     INT(4)  PRIMARY KEY,

​    DNAME   VARCHAR(10),

​    LOC   VARCHAR(20)         

);

创建子表

CREATE. TABLE. EMP1(

  EMPNO  INT(4)   PRIMARY KEY,

  ENAME   VARCHAR(10),

  JOB        VARCHAR(10),

  MGR       INT(4),

  DEPTID     INT(4)     —用来做外键字段 外键的名称和父表的主键名称可以不同

--外键的创建只能使用约束的标准模式创建

—创建级联删除

  CONSTRAINT   EMP_DEPT_FK   FOREIGN  KEY (DEPTID)  REFERENCES DEPT  (DEPTNO)  ON DELETE CASCADE

);

##### *删除约束*

约束和表一样，都是一个独立的数据库对象，约束时建立在表上面的，删除表的话 约束也会被删除，

单独删除约束不会影响数据库表的结构和数据

1.删除主键约束：一个表中只有一个主键约束，所以删除主键约束的时候不需要指定约束名称

​        ALTER TABLE DEPT DROP  PRIMARY KEY

2.删除外键约束：一个表中可以有多个外键，所以删除外键约束需要指定约束名称

​       ALTER TABLE EMP DROP FOREIGN KEY EMP_DEPT_FK

3.删除唯一约束：需指定唯一约束的名称 并且要使用DROP INDEX

​       ALTER TABLE EMP DROP INDEX  UNIQUE_DEPTNO

4. 删除非空约束：删除非空约束不能使用DROP，要使用MODIFY 

   ALTER TABLE DEPT MODIFY DEPTNAME VARCHAR(10)  NULL

   

# 存储引擎

存储引擎是mysql特有的 其他数据库没有存储引擎

查看服务器存在几种存储引擎：show  engines\G

#####InnoDB存储引擎：是mysql的缺省引擎；

特征：

1.每个InnoDB表在数据库目录以.frm格式文件表示

2.InnoDB表空间tablespace被用于存储表的内容

3.提供一组用来记录事务性活动的日志文件

4.用COMMIT(提交)，SAVRPOINT及ROLLBACK回滚，**支持事务处理**

5.提供ACID兼容

6.在mysql服务器崩溃后提供自动回复

7.多版本（mvcc）和行级锁定

8.支持外键的引用和完整性，包括级联更新和级联删除

**MEMORY存储引擎**

其数据保存在内存中，且长度固定，使得其速度特别快 。

**MYISAM存储引擎**

适合大量读数据，少量更新数据的情况下使用

特征：

1.使用mytable.frim,mytable.myd,mytable.myis三种文件分别存储表的结构，数据和索引

2.数据可以被压缩，节省空间

# 数据库事务

一个业务逻辑完成的执行，要么都成功，要么都失败，例如银行转账，俩个更新的语句都执行成功。

事务只与DML语句有关即 INSERT，UPDATE，DELETE

####事务的四个特性（ACID）：

1.原子性（Atonicithy）

事务的最小单元，不可再分

2.一致性（Consistency）：

事务要求所有的DML语句执行时要么都成功，要么都失败

3.隔离性（Isolation）：

一个事物的运行不会影响其他事物的运行

4.持久性（Durability）：

事务完成后，对数据库的更改是永久的，并且不会回滚

####相关操作

开启事务：start   transaction

结束事务：end   transaction

提交事务：commit  transaction

回滚事务：rollback

开启标记：任何一条DMl语句执行的开始，标志事务的开启，事务结束后，数据库的更改才会生效，DML语句的执行只是会在内存中做记录；

#### mysql管理事务的默认方式：自动提交

mysql默认认为，每执行一个DML语句，都会引起一个事务，这个DMl语句执行结束的时候，mysql会自动提交事务，提交后事务对数据库的修改是永久的，所有的会话都会查询到这些修改

#### 关闭自动提交

1.执行语句 start   transaction  关闭自动提交；这是关闭本次自动提交，事务结束之后，新的事物又是自动提交

2.执行语句 set  autocommit=off；关闭会话过程中的自动提交 ，在整个会话中都是有效的，新的会话中还是默认自动提交； 关闭事务的第一个DML语句会引起一个事务，只要这个事务没结束，我们所有的DML语句都隶属于同一个事务，事务中对数据库的修改会保存在内存中，只有当前事务可以查询到这些修改

##### 结束事务的俩种方式：

1.commit事务：

提交后，对数据库的修改永久的保存到了数据库

2.回滚事务：

回滚结束事务的时候，事务对数据库的修改统统放弃，数据库恢复到事务开始前的状态

查询会话中自动提交的状态

show    Variables   like '%commit%'

#### 事务的隔离级别（用来解决数据库的读并发问题）

读并发问题：多个事务同事修改相同的数据；类似java中的线程同步的问题，给数据进行加锁，表现形式是方法加锁，

#####1.读未提交

一个事务读取到另一个事务尚未提交的数据

##### 2.读已提交

一个事务读取到另一个事务已经提交的数据

可避免脏读，oracle默认的隔离级别，但是会导致不可重复读（在同一个事务每次读取数据的条数都不同）

##### 3.可重复读

一个事务提交之后的数据另一个事务，依然读取不到，但是会导致数据库实际条数和同一事务读取到的不一致，称为幻读

mysql默认的隔离级别

##### 4.可串行化

一个事务操作表的时候，会对表进行上锁，称为表级锁，其他事务只能等待，但是会导致数据库处理能力变di

隔离级别范围：

1.会话级别的隔离级别：只对当前会话有效

2.全局级别的隔离级别：对所有会话有效

设置隔离级别的sql

set global  transaction   isolation  Level    read uncommonted;—未提交读

这种隔离级别存在脏读；只在理论上存在，实际中没有使用

set global  transaction   isolation  Level   read  commit.  —已提交读

set global  transaction   isolation  Level   repeatable  read。—可重复读

set global  transaction   isolation  Level   serializable。—可串行化

查看全局的隔离级别

select    @@  global. tx_isolation;

演示隔离级别相关sql

mysql -uroot -proot；

start   transaction；

Use  数据库名；

# 行级锁

只会锁住某行或者某几行，不会锁住其他记录

使用过程如下：

1.关闭自动提交

start transaction

2.执行语句 select * from  emp  where empno   in (7369,7401).  For update; 开启事务，开启行级锁锁住俩条记录，

注意：启动行级锁的语句必须是主键作为查询条件，否则依旧是表级锁

3.启动行级锁的目的是只有当前事务可以修改 7369和7401俩行数据

updtae emp  set comm=200,sal=12000 where. Empno in (7369,7401)

4.结束事务，释放行级锁

commit ；   

# 数据库索引

类似一本书的目录，是一个独立的数据库对象，可以加快数据的查询速度

数据库检索数据的方式：

1.全盘扫描：

2.通过索引查询数据：

#### 创建索引的方式：

1.数据库自动创建：

数据库自动为主键创建索引

2.手动创建：

#####什么样的字段应当创建索引？

1.该字段中存储的数据量比较大

2.该字段的数据很少执行dml操作

3该字段经常出现在查询条件即where后面

eg：给emp表的ename字段船桨索引

CREATE INDEX. ENAME_INDEX  ON  EMP   (ENAME);

查询索引

show index   from   emp；

删除索引

drop index    ENAME_INDEX   on  emp；  

# 视图

是一个数据库对象，实际就是一个查询的命名，也就是把一个数据查询结果当作一个临时表，然后从临时表中查询数据，

注意：视图都是和一个有效的查询语句绑定在一起的 ，视图和出现在from后面的子查询类似

##### 创建视图

create  view   视图名称   as   有效的查询语句

查询视图结构

desc 视图名称

删除视图

drop view if exists 视图名称



create  view   v_emp as select * from emp  where sal >200

可对视图进行dml操作

# 数据库设计三范式

数据库设计时，依据的三个规范

##### 第一范式

要求有主键，数据库中不能出现重复记录，每一个字段时原子性的不能再分；

##### 第二范式

数据库中所有非主键字段完全依赖主键，不能产生部分依赖 即尽量不使用联合主键 ，多对多关系使用中间表

##### 第三范式

非主键字段之间不能产生传递依赖



 

