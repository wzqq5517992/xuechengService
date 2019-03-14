

# 第五章   InnoDB记录存储结构

## 1.学前必备

`MySQL`服务器上负责对表中数据的读取和写入工作的部分是`存储引擎`，而服务器又支持不同类型的存储引擎，比如`InnoDB`、`MyISAM`、`Memory`啥的，不同的存储引擎一般是由不同的人为实现不同的特性而开发的，真实数据在不同存储引擎中存放的格式一般是不同的，甚至有的存储引擎比如`Memory`都不用磁盘来存储数据，也就是说关闭服务器后表中的数据就消失了。由于`InnoDB`是`MySQL`默认的存储引擎，也是我们最常用到的存储引擎 .

## 2.InnoDB页简介

`InnoDB`是一个将表中的数据存储到磁盘上的存储引擎，所以即使关机后重启我们的数据还是存在的。而真正处理数据的过程是发生在内存中的，所以需要把磁盘中的数据加载到内存中，如果是处理写入或修改请求的话，还需要把内存中的内容刷新到磁盘上。而我们知道读写磁盘的速度非常慢，和内存读写差了几个数量级，所以当我们想从表中获取某些记录时，`InnoDB`存储引擎需要一条一条的把记录从磁盘上读出来么？不，那样会慢死，`InnoDB`采取的方式是：将数据划分为若干个页，以页作为磁盘和内存之间交互的基本单位，InnoDB中页的大小一般为 16KB。也就是在一般情况下，一次最少从磁盘中读取16KB的内容到内存中，一次最少把内存中的16KB内容刷新到磁盘中 .

## 3.InnoDB行格式

行格式共有4种,分别为`Compact`、`Redundant`、`Dynamic`和`Compressed`  常用的为`Compact`这里值讲解这一种,其他的也是类似一样的原理.

比如我们在`xiaohaizi`数据库里创建一个演示用的表`record_format_demo`，可以这样指定它的`行格式`： 

```mysql
mysql> USE xiaohaizi;
Database changed

mysql> CREATE TABLE record_format_demo (
    ->     c1 VARCHAR(10),
    ->     c2 VARCHAR(10) NOT NULL,
    ->     c3 CHAR(10),
    ->     c4 VARCHAR(10)
    -> ) CHARSET=ascii ROW_FORMAT=COMPACT;
Query OK, 0 rows affected (0.03 sec)
```

另外，我们还显式指定了这个表的字符集为`ascii`，因为`ascii`字符集只包括空格、标点符号、数字、大小写字母和一些不可见字符，所以我们的汉字是不能存到这个表里的。我们现在向这个表中插入两条记录： 

```mysql
mysql> INSERT INTO record_format_demo(c1, c2, c3, c4) VALUES('aaaa', 'bbb', 'cc', 'd'), ('eeee', 'fff', NULL, NULL);
Query OK, 2 rows affected (0.02 sec)
Records: 2  Duplicates: 0  Warnings: 0
```

查询该表的记录:

```mysql
mysql> SELECT * FROM record_format_demo;
+------+-----+------+------+
| c1   | c2  | c3   | c4   |
+------+-----+------+------+
| aaaa | bbb | cc   | d    |
| eeee | fff | NULL | NULL |
+------+-----+------+------+
2 rows in set (0.00 sec)

mysql>
```

行格式结构图:

![](F:\MyOwnCode\studyOnLine\xcEduService\文档\mysql文档\images\compact行格式.jpg)

一条完整的记录其实可以被分为`记录的额外信息`和`记录的真实数据`两大部分 ;

#### 记录的额外信息

这部分信息是服务器为了描述这条记录而不得不额外添加的一些信息，这些额外信息分为3类，分别是`变长字段长度列表`、`NULL值列表`和`记录头信息` ;

`MySQL`支持变长的数据类型 ,如:`VARCHAR(M)`、`VARBINARY(M)`、各种`TEXT`类型，各种`BLOB`类型 ,所以我们在存储真实数据的时候需要顺便把这些数据占用的字节数也存起来，这样才不至于把`MySQL`服务器搞懵，所以这些变长字段占用的存储空间分为两部分 :

   1.真正的数据内容

2. 占用的字节数

