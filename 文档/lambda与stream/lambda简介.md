# 一、为什么引入lambda表达式

### 1. 语法上的简洁性
对比以下代码, 开启一个线程并执行

```
    public static void main(String[] args) {
        new Thread( new Runnable() {
            System.out.println("inner class demo")
        }).start();
    }
```
我们运行一个线程，必须给Thread的构造函数传递一个 Runnable的匿名内部类，调用 Thread对象的start 方法时程序会调用Runnable的run方法 

其实本质上我们只是想传递一段需要执行的代码（例子中是输出一段话‘inner class demo’），但是java语法必须先构造一个匿名内部类，很蛋疼， 看下lambda表达式语法:

```
    public static void main(String[] args) {
        new Thread( () -> {
            System.out.println("inner class demo")
        } ).start();
    }
```
不必须构造一个匿名内部类，而是传入了一个以前没见过的一个表达式，你不用知道这玩意是怎么执行的（后面再哔哔），看着不用构造匿名内部类就行了

### 2. 设计思想上的转换
==代码控制权==的转换， 看下边这段代码

```
List<Integer> list = Arrays.asList(1, 2, 3, 4);
for (Integer i : list) {
    System.out.println(i);
}
```
很简单的一个需求： 遍历一个集合，并将集合中的每一个元素打印出来，看这段代码我们做了什么：
    
```
1. 从内存中将集合中的每一个元素依次取出     //怎么做 的问题
2. 把数据打印出来   //做什么 的问题
```

作为客户端程序员（写业务的）,其实我们并不关心程序是怎么把集合中的元素怎么取出来的（怎么做）， 我们只关心把数据打印出来就行了（做什么），

但是我们也做了一个主动遍历集合的动作，这样由客户端程序员控制集合数据遍历访问的循环，我们成为==外部循环==, 显然循环的控制权在客户端程序员手里

我们再看一个和外部循环对应的==内部循环==的例子：

```
List<Integer> list = Arrays.asList(1, 2, 3, 4);
list.forEach((i) - {
    System.out.println(i);
})
```
和上边开启线程的表达式有点像，我们只是调用了 List对象的 forEach 方法, 我们作为客户端程序员并没有关心集合数据是如何取出来的， 这个动作交给了类库的设计者做处理，至于他是怎么取出来的客户端程序员并不关心，我们只在这里写上我们的业务逻辑(把某个数字打印出来)。
这么写有什么好处呢？类库设计者在遍历集合的时候可以进行各种优化，至于是啥优化，稍后待续。。。

说简单点，就是我们只关心做什么， 而怎么做的问题交给类库处理

# 二、学习之前应该了解的一些概念（很简单的）
### 1. 【函数式接口】
有一个方法的接口 （一个方法！一个方法！一个方法！一个方法！一个方法！的 接口！！！！！）

例如： 
```
public interface Runnable {
    public abstract void run();
}
```


### 2.【lambda表达式的样子】
其实看起来像是函数的另一种写法，不过看起来怪怪的， 习惯了就好，大致上是这样的意思

(参数列表) -> {函数体}, 其中的 小括号，参数的类型，大括号都可以省略，函数体可以返回也可以不返回，函数体既可以是一个表达式，也可以是一个语句块。举个例子更好理解一些

```
1.(int x, int y) -> x + y //表示接收x, y这两个整形参数， 并且返回两个数的和
2.() -> 42 // 表示没参数，返回了常数 42
3.(String s) -> { System.out.println(s); }  //表示接收一个字符串参数，把这个字符串打印出来，然后就没有然后了

```

看了上边的例子我想你也大致明白了lambda表达式长什么样子，排列组合一下就是下边的样子

```
1. 可选类型声明：不需要声明参数类型，编译器可以统一识别参数值。
(int x) -> {x+1}, 这个括号里的 int 可以省略

2. 可选的参数圆括号：一个参数无需定义圆括号，但多个参数需要定义圆括号。
x -> {x+1}, 上边例子的括号可以省略(只有一个参数的话)

3. 可选的大括号：如果主体包含了一个语句，就不需要使用大括号。
x -> x+1, 上边例子的大括号可以省略(函数体只有一个语句的话)

4. 可选的返回关键字：如果主体只有一个表达式返回值则编译器会自动返回值，如果是大括号需要指定明表达式返回了一个数值。

x -> {System.out.println("xxx")}, 啥也不返回


```

---------------------------------------------总结一下--------------------------------------------------

看了第二个概念，lambda表达式的样子的栗子，聪明的孩子肯定想到了个问题，光知道了这个表达式的样子， 但是这个表达式到底是个啥东西，咳咳，光看不想的孩子肯定不聪明。。下边我们看下这个表达式到底是个啥东西

再举个🌰，比如

```
Integer i = 1;

String s = "why are you so diao";
```
我们可以把 1 赋值给整形类型变量i, 把 'why are you so diao'这个字符串赋值给 字符串类型的变量s
那我们可以把 lambda表达式赋值给什么类型的变量呢???, 比如
```
XXX var = (int x, int y) -> x + y;
```
XXX 是个啥类型呀？ 

...

...

...

答案就是：其实是个 函数式接口 ！！！， 就是我们第一个概念， 只有一个方法的接口，为啥呢？，先看个栗子🌰


```
Runnable runnable = () -> {System.out.println("i'm so diao")};
```

我们把lambda表达式 () -> {System.out.println("i'm so diao") 赋值给了一个Runnable接口😲，是不是有一种lambda表达式 就是个匿名内部类的赶脚，有木有觉得被骗了。。。嗯，可以先这么理解（后边我们再来找不同啦啦😋），就是个匿名内部类，有兴趣的同学可以继续看看我们为啥把lambda表达式的类型设计成一个函数式接口


```
说点正经的啊， lambda表达式的本质代表着一种函数的实现（没有名），本质是个函数。
我们完全可以新定义一种类型来作为lambda表达式的类型（比如说python里的函数类型）。
lambda表达式的设计者经过权衡，定义一种全新的类型会引入一些其他的问题。
比如说（想看就看， 不想看跳过， 结论就是lambda设计者采用了函数式接口的设计， 以下缺陷都讲清楚比较麻烦，先略过，能看懂的就看看，看不懂的就算了，以后自然就懂了）：

1. 它会为Java类型系统引入额外的复杂度，这种新的函数类型和之前的类型混用。。。
2. 它会导致类库风格的分歧——一些类库会继续使用回调接口（内部类样子的），而另一些类库会使用结构化函数类型（我们所说的这种新类型）
3. 它的语法会变得十分笨拙，尤其在包含受检异常（checked exception）之后
4. 每个函数类型很难拥有其运行时表示，这意味着开发者会受到 类型擦除（erasure） 的困扰和局限。比如说，我们无法对方法 m(T->U) 和 m(X->Y) 进行重载（Overload）

好了，说完了lambda表达式的类型是函数式接口了。。

```
结论就是， lambda表达式其实可以理解为==把函数式接口（只有一个方法的接口）中的那个方法给实现了==

---------------------------------------------总结完了--------------------------------------------------


### 3.【目标类型】

就是这个lambda表达式表示是==哪个函数式接口==的对象，比如

```
() -> "done"; //表示没参数，返回了个字符串 'done'
```
这个 lambda表达式是哪个函数式接口的类型呢？比如写成这样

```
Callable<String> c = () -> "done";
PrivilegedAction<String> a = () -> "done";
```
好像都可以哎😔。。。那到底是什么类型呢，答案就是不确定，你说是啥就是啥（说专业点就是看上下文，它利用 lambda 表达式所在上下文 所期待的类型 进行推导，这个 ==被期待的类型== 被称为 ==目标类型==。）

当然，是不是随便写个函数式接口就可以作为lambda表达式的目标类型呢，of course not... 比如

```
Runnable runnable = () -> "done";   //of course 不对， Runnable的run 方法是什么都不返回的，这个lambda表达式返回了一个字符串，是不对的。
```
所以，一个lambda表达式的目标类型有什么要求呢？

lambda表达式赋值给目标类型 T 的要求（其实就是lambda表达式的参数类型，数量，返回值，抛出的异常都必须和函数式接口T 中的方法的参数类型，数量，返回值，抛出的异常都一样），为了正式一点，还是写出来吧：

```
1. T 是一个函数式接口
2. lambda 表达式的参数和 T 的方法参数在数量和类型上一一对应
3. lambda 表达式的返回值和 T 的方法返回值相兼容
4. lambda 表达式内所抛出的异常和 T 的方法 throws 类型相兼容
```

讲完了一个具体的lambda表达式的目标类型是根据程序中的上下文来判断的，我们再来说说lambda表达式自己的类型问题。
当然，如果lambda表达式有自己的参数类型，我们叫==显式类型==（参数类型被显式指定）

```
(String x, String y) -> x+y //参数类型就是两个String， 返回了把这两个字符串拼起来之后的字符串
```
但是，

往上看，我们说lambda表达式样子的时候我们说了表达式的参数类型是可以省略的，参数类型省略了，返回值的类型如果依赖参数那也就不知道了，这个叫做==隐式类型==（参数类型被推导而知）， 举个🌰：

```
(x, y) -> x+y
```
这个lambda表达式的参数类型不知道，那么返回的类型也就不知道了😠, 聪明的你肯定已经知道了怎么确定他们的类型了😏-----程序的上下文嘛(也就是我们只要知道lambda的目标类型，我们不就可以==反过来推导出来lambda表达式的参数类型==喽)

比如：
```
public interface LambdaDemo {
    int sum(int x, int y);
}

LambdaDemo demo = (x, y) -> x+y     //x, y的类型就是目标类型LambdaDemo中的唯一方法sum中的参数类型， int, int

//有时候我们想让上边的接口更通用，我们会引入泛型
public interface LambdaDemoGeneric<T> {
    int sum(T x, T y);
}

LambdaDemoGeneric<Float> demo = (x, y) -> x+y   //lambda表达式的类型就是Float喽，就是这么简单
```
所以总结一下，lambda表达式和函数式接口中的那个唯一的方法是一一对应的，这个匹配是编译器给我们做了，我们知道就行了：

```
1. 函数式接口中的方法参数类型是啥，lambda表达式的参数类型就是啥。
2. 函数式接口中的方法返回类型是啥，lambda表达式的返回类型就是啥。
```
跟我们已经见过的一个东西很像，你看

```
List<String> list = new ArrayList();    //这个ArrayList的类型就是String， 是不是一毛一样
```


# 三、lambda表达式对匿名内部类
这两者其实本质上起到的作用是一致的，都是我们的程序需要一段可执行代码（可以有返回值，也可以没有）来让主程序回调，我们需要在语言层面提供一种尽可能简单的对这段可执行代码进行封装的方式，lambda表达式和匿名内部类都可以作为这种封装的一种实现，没有lambda表达式的时候，我们一直用内名内部类干这个事，现在更牛逼的东西出来，有啥理由不用的，看看牛逼在哪：

### 1. 语法简单了
这不不用解释了，瞅瞅就知道了

### 2. 匿名类中的 this 和变量名容易使人产生误解
这个要举个栗子:

```
public class Hello {
    Runnable r1 = () -> { System.out.println(this); };  //lambda表达式
    Runnable r2 = new Runnable() {  //匿名内部类
        @Override
        public void run(){
            System.out.println(this);
        }
    };
    public String toString() {  return "Hello, world"; }
    public static void main(String... args) {
        Hello hello = new Hello();
        hello.r1.run();   //输出 'Hello, world'
        hello.r2.run();   //输出 类似 'Hello$1@5b89a773' 的字符串
    }
}

这个栗子我们可以看出，
1. 匿名内部类中的this关键字代表这个匿名内部类的对象本身, 而如果想在内部类中访问外部类对象，需要加上限定符 OuterClass.this, 本栗子中就是 Hello.this, 
Runnable r2 = new Runnable() {
        @Override
        public void run(){
            System.out.println(Hello.this);     //这样就能输出 'Hello World'了
        }
    };

2. lambda表达式的this关键字在lambda表达式内外部都有相同语义
```
其实不只是this关键字，内部类有其自有的作用域，其中的变量名如果有和起外部类相同的话，会覆盖掉外部类的变量，

而==lambda 表达式函数体里面的变量和它外部环境的变量具有相同的语义==， 这样的好处就是程序只有一个作用域，不会混起来

再看个🌰深入理解一下

```
public class Hello {

    public static void main(String[] args) {
        int i = 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 5;  //这个变量覆盖了其外部类的变量
                System.out.println(i);
            }
        }).start();
        
        
        new Thread(() -> {
            int i = 6;  //!!! 编译不通过，因为lambda表达式没有其独自的作用域，变量 i 已经被声明过了，不能重复声明
            System.out.println(i);
        }).start();;
    }
    
}
```
结论就是，lambda表达式和匿名内部类的作用域有区别， 
==lambda表达式没有独自的作用域==

### 3. 无法捕获非 final 的局部变量
java SE 7中，内部类引用的外部变量只能是final类型的(SE 8放宽了限制， 只要该变量在初始化之后没有被赋值，那就可以使用) 


```
public class Hello {


    public static void main(String[] args) {
        new Hello1().test();
    }

    //SE 7 代码：
    public void test() {
        int i = 1;  //初始化变量
        new Thread(new Runnable() {
            @Override
            public void run() { //是编译不通过的，内部类引用了非final的外部变量
                System.out.println(i);
            }
        }).start();
    }
    
    //SE 8 代码：
    public void test1() {
        int i = 1;  //初始化变量
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(i); //编译通过的，SE 8放宽了这个限制，只要是变量在初始化之后的值没有被修改就合法
            }
        }).start();
        
        new Thread(() -> {
            System.out.println(i);  //合法
        })
    }
    
    //SE 8 代码：
    public void test1() {
        int i = 1;  //初始化变量
        new Thread(new Runnable() {
            @Override
            public void run() {
                i = 5;  //修改变量的值，编译不通过的
                System.out.println(i);
            }
        }).start();
        
        new Thread(() -> {
            i = 5;  //修改变量的值，编译不通过的
            System.out.println(i);
        })
    }
}
```

注意，这一改动是针对JDK版本的，内部类和lambda表达式都拥有这个特性

当然lambda表达式和匿名内部类还有其他别的一些特性，这就不多说，以后遇到再哔哔


# 方法引用
### 1.啥意思
不知道看到这各位对lambda表达式有个啥样的认识，让我们再总结一下

我们做这些的目的其实是封装一段可以被主程序回调的代码。

lambda表达式就是个没有名的函数，我们又不想在java语言里引入一种新的类型（带来新的复杂度和前向兼容问题）来给他一个新的定义，所以借用了函数式接口（一个方法的接口）来作为lambda表达式的目标类型，并且利用在程序中的上下文，能够推lambda表达式的隐式类型到底是个啥。

这回明白了吧，一个没有名的函数而已😏

那问题来了，有名字的函数可以赋值给一个函数式接口么？


```
public class Person {
    private String name;
    private Integer age;
    public Person(String name, Integer age) {this.name = name; this.age = age;}
    public String getName() { return name; }    //这就是个有名字的函数， 它的名字叫 getName, 怎么感觉有点白痴的旁白。。。
    public Integer getAge() { return age }


    //我们一般调用 getName() 的时候好像都要配一个对象哦😯，比如：
    public static void main(String[] ) {
        Person brotherDog = new Person("wanghongyang", 44);
        String name = brotherDog.getName();
        System.out.println("狗哥真名： " + name);
    }
    
    //对于虚拟机来调用这个 getName的方法而言，都需要一个Person对象，所以对于虚拟机来说，这个方法的入参是一个Person对象，返回结果是一个字符串
}
```

所以， 如果我们想把这个有名字的 getName函数赋值给一个函数式接口，那么这个函数式接口里的方法就需要接收一个Person对象， 返回一个字符串，这样才配嘛


```
public interface PersonFucktionInterface {
    String xxx(Person p);   //这就对上了
}

//怎么表示那个 getName的函数呢，卧槽，Java里头好像没有表示一个函数类型的表达方式。。。。坑爹了
//没事没事，我们创造一个哈哈😆

PersonFucktionInterface p = Person::getName;    //lalala,就张这个样子，惊不惊喜，意不意外，任意一个函数都可以搞成个函数式接口，

//其实这个也就相当于:
PersonFucktionInterface p1 = (p) -> p.getName;
```
有个疑问哎😔， 不是可以用 (p) -> p.getName 来表示 Person::getName 么，为啥还要用Person::getName呢？

。。。
。。。
。。。
我也不知道，他们说用 Person::getName 这样长得好看，你觉得呢。

上边说的 getName方法是Person类的一个成员方法，Person::getName 等价于 lambda表达式: (p) -> p.getName, 那如果Person类里的其他类型的方法想赋值于一个函数式接口呢，比如静态方法，构造方法呢？我们给Person类多填一些 方法


```
public class Person {
    private String name;
    private Integer age;
    public Person(String name, Integer age) {this.name = name; this.age = age;}
    public String getName() { return name; }    //这就是个有名字的函数， 它的名字叫 getName, 怎么感觉有点白痴的旁白。。。
    public Integer getAge() { return age }

    public Person() {}  //默认构造方法
    public static String toUpperCaseName(String name) {
        return name.toUpperCase;
    }
}
```

我们来总结一下方法引用的各种类型：


```
1. 引用静态方法 
Person::toUpperCaseName  <==> (s) -> Person.toUpperCaseName(s)

2. 对象的实例方法 比方说有Person brotherDog = new Person("wanghongyang", 44) 这个对象， 
//实例对象::成员函数：
brotherDog.getName  <==> () -> this.getName()

3. 类型的任意对象的实例方法 注意和 2 的对比， 
//类::成员函数：
Person::getName  <==> (s) -> s.getName()

4. 引用构造函数 
Person::new  <==> () -> new Person()
```
上边这几个方法引用和lambda表达式的关系多看几遍，多看几遍就忘了


# lambda表达式简介写完了，总结一下

```
为了实现代码控制权上的转换，
我们需要提供一段方便类库(或者说相对于具体业务的一种更高层次的抽象)回调的代码封装，
这个代码封装可以是匿名内部类，也可以是lambda表达式。
说的更高级一点就是函数式编程了。

1. lambda表达式是一个没名字的函数
2. lambda表达式的参数类型和返回类型需要在上下文中推导，编译器干了这个事情
3. 出于各种考虑，采用只有一个方法的函数式接口作为lambda表达式的类型
4. 方法引用是lambda表达式的另一种展示方式，复用了已有代码，并且在代码层面上更加清晰
```

好累啊😫， 求赞赏。。 