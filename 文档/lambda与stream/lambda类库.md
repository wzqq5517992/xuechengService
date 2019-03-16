# 一些JDK提供的函数式接口

```
//JDK 7 已经提供的一些接口
java.lang.Runnable  ==> void run();
java.util.concurrent.Callable   ==> V call() throws Exception;
java.security.PrivilegedAction  ==> T run();
java.util.Comparator    ==> int compare(T o1, T o2);
java.io.FileFilter  ==> boolean accept(File pathname);
java.beans.PropertyChangeListener ==> void propertyChange(PropertyChangeEvent evt);

//JDK 8 提供的一些更纯洁的函数式接口 java.util.function包里边, 虽然比较面生，多瞅瞅，一回生，两回熟嘛！
//毕竟你以后要经常和他们打交道
Predicate<T>——接收 T 并返回 boolean  ==> boolean test(T t);
Consumer<T>——接收 T，不返回值   ==> void accept(T t);
Function<T, R>——接收 T，返回 R    ==> R apply(T t);
Supplier<T>——提供 T 对象（例如工厂），不接收值  ==> T get();

```
JDK提供了好些个函数式接口，当然也提供了很多实用函数式接口作参数的方法

比如说这个：
```
public interface Iterable<T> {

    /**
    * 这个接口是对某个实现了Iterable的结合准备的，入参是一个函数式接口，
    * 这个函数式接口负责消费这个集合中的某一个元素（其实就是给了一段业务层面的可执行代码，来对这个元素处理），
    */
    default void forEach(Consumer<? super T> action) {  
    //这个default关键字是JDK 8新出的一个叫默认方法的东东，是为了不改变原类库，而只在原接口上新增接口，
    //我们有空再深入将这个，可以忽略这个default关键字
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }
}
```
遍历集合，然后对集合中的每个元素做一些操作，然后就没有然后了。。。
所以对于我们客户端程序员来说，只需要负责把对集合元素的操作写了就行了，就是实现 ```Consumer<T>``` 这个接口，把我们的操作写进去就行了，比方说这样, 是不是很简单😜：

```
List<Integer> list = Arrays.asList(1, 2, 3);
Consumer<Integer> consumer = (x) -> {System.out.println(x)}; 
list.forEach(consumer);
```

童鞋，看到现在有木有觉得lambda表达式给你一种小清新的赶脚
再次重申一下我们现在在干吗，哔哔了这么长时间我们的目的是干啥， 咳咳，比较重要，仔细看，仔细想：

```
我们将代码的控制权分离开来，客户端程序员只关心做什么，由类库去解决怎么做的问题。
所以我们希望有一种便捷的数据结构去把我们所关心的做什么给封装起来，然后让类库去回调。
在java 8中设计者采用了函数式接口来作为这个数据结构，我们可以用lambda表达式这样便捷的结构定义出我们客户端程序员关心的做什么问题
然后将怎么做的优化交给类库设计者处理，毕竟他们更专业，这样我们就不用重复早轮子，到处优化怎么做的问题。
```

所以，对于现有类库来说，我们完全都可以重新推到原先的设计，重新造一份依赖函数式接口的类库，但是原有类库已经深入到Java开发的每一个角落，新类库只能是逐渐取代老类库，而不能一下子给翻了天，所以我们提出了一种兼容现有类库的折中方案，来运行Java 8带来的函数式编程这一新概念。

# 正式类库开整
我们关注的主要是Java集合的类库对lambda表达式的支持。

为了兼容现有集合类库，我们引入一个关键的抽象概念 
 
 ==> java.util.stream.Stream（流）
 
 我们对现有Java ```Collection```类库(包括 List, Set, Queue...)进行lambda表达式的交互时，都首先把集合转换成一个 ```Stream```的概念，然后再操作，这样Stream完全封装了对集合的函数式编程（封装就是为了避免我们跟各个具体的集合概念打交道，以后只和Stream打交道就好了）
 
 说了太多了，还是举个栗子吧：
 
```
List<String> list = Arrays.asList("ab", "ac", "bc");    //声明一个List并初始化
Stream<String> strem = s.stream();  //拿到这个List对应的流
List<String> newList = stream.filter((x) -> x.contains("a"))    //意思是过滤包含"a"d额元素
                        .collect(Collectors.toList());    //将流再转为List
```

# Stream的一些特性

### 1. 对外提供的一些接口
Stream是对集合的再一次封装，并对外提供了一些方便集合操作的接口（其实有好多，这就举几个🌰了，我们这里列出的这些要多看几遍，毕竟你以后会天天和他们打交道）：

```
List<String> list = Arrays.asList("ab", "ac", "bc");    //声明一个List并初始化

//根据客户端传入的过滤条件，就是Predicate 的函数式接口，满足条件则留下，不满足就pass, 最后返回过滤后的流
Stream<T> filter(Predicate<? super T> predicate);   
//比如： 这个栗子返回 [ab, ac]
List<String> newList = list.stream().filter((x) -> x.contains("a")).collect(Collectors.toList());   

//将流中的每一个元素都根据客户端传入的 映射条件 Function, 然后返回新的Stream
<R> Stream<R> map(Function<? super T, ? extends R> mapper);
//比如： 这个栗子返回[AB, AC, BC]
List<String> newList = list.stream().map((x) -> x.toUpperCase()).collect(Collectors.toList()));

//根据客户端传入的排序规则，对流中的元素排序
Stream<T> sorted(Comparator<? super T> comparator);
//比如：这个栗子返回 [ab, ac, bc]
List<String> newList = list.stream().sorted((a, b) -> a.compareTo(b)).collect(Collectors.toList()));

//根据客户端传入的消费行为，对集合里的每个元素都消费一遍，这个就不举🌰了
void forEach(Consumer<? super T> action);
T reduce(T identity, BinaryOperator<T> accumulator);

//这个是把流转换为集合，这个我们后面会重点讲一讲的
<R, A> R collect(Collector<? super T, A, R> collector);
```

### 比较重要的特性
##### 1.Pipeline操作：
对流的好多操作可以叠加在一块使用，比如：

```
public class Hero {
    private String name;
    private String postion;
    public Hero(){}
    public Hero(String name, String postion){this.name = name; this.postion = postion;}
    
    public String getName() {return name;}
    public String getPostion() {return postion;}
    public Boolean isMale() {return isMale;}
    
    @Override
    public String toString() {
        return "[name=" + name + ", postion: " + postion +", isMale: " + isMale + "]";
    }
}
List<String> heros = Arrays.asList(
    new Hero("Master Yi", "打野", true),
    new Hero("Ashe", "adc", false),
    new Hero("Lee Sin", "打野", true),
    new Hero("Garen", "上单", true),
    new Hero("Annie", "中单", false) 
);   //声明一个List并初始化

heros.stream().filter(x -> x.isMale())  //过滤男性同胞
            .map(x -> x.getName())  //把他们的名字拿出来
            .filter(x -> x.contains("a"))  //过滤名字里有 a字母的
            .sorted((x, y) -> x.compareTo(y))   //按字符创从大到小排序
            .forEach(x -> {     //把每个元素都消费掉
                System.out.println(x);
            });
```

上边的流的pineline操作一定让你觉得好爽有木有， so easy 并且还清晰

##### 2. 不存储值
Stream需要一个数据源(我们之前见到的都是把集合作为Stream的数据源，当然，数据源也可以来源于别的地方，比如I/O)

##### 3. 天然的函数式风格
对流的操作会产生一个结果，但流的数据源不会被修改，客户端程序员提供产生结果的方式就好(比如```filter```, ```map```), 或者消费方式(比如```forEach```)。

##### 4. 惰性求值
看一下下面对流的pineline操作的代码:

```
heros.stream().filter(x -> x.isMale())  //过滤男性同胞
            .map(x -> x.getName())  //把他们的名字拿出来
            .filter(x -> x.contains("a"))  //过滤名字里有 a字母的
            .forEach(x -> {     //把每个元素都消费掉
                System.out.println(x);
            });
```

```
大家觉得这段代码是怎么执行的呢？
1. 过滤男性同胞， 然后返回一个只有男性同胞的Stream
2. 对只有男性同胞的Stream，把每一个英雄都拿出来，然后把他们的名字都拿出来，组成一个只有男性同胞名字的Stream
3. 对男性同胞名字的Stream，把每一个名字都输出来

这样执行的结果就是进行了3次循环
```
上边的执行顺序是我们的惯性思维，认为执行完一个函数再执行一个，我们把这种遍历方式叫做==急性求值==, 

其实有些操作是可以合并起来执行的，不用执行多次，比如filter，map操作可以随着流水线在需要用到的时候才去执行真正的操作，意思就是上边例子可以只执行一遍循环，在最后输出的时候名字之前，filter和map是不执行的，这样一可以节省循环次数，二可以介绍维持中间结果的开销，我们把这样的遍历方式叫做==惰性求值==

这个是套路：
```
1. 将数据源(一般是集合)转化成Stream
2. 对Stream进行一系列的惰性操作(比如map, filter)
3. 用急性操作结束(forEach, collect, toArray..)
```
在使用流水线的过程中，我们客户端程序员(写业务的)不用具体操作Stream的惰性操作是怎么实现的(类库给我们做了), 而只需要管主要我们的业务(对元素做什么)，这就是牛逼之处，，，之一。


### 5. 短路
一种及时停止遍历的说法

```

Optional<Hero> firstHero =
            heros.stream()
                .filter(s -> s.getPostion() == "上单")
                .findFirst();
//选择出第一个是上单的英雄，显然遍历到盖伦就不用再往下遍历了
//这就是及时停止遍历的🌰，也叫短路
```

### 6.并行

```
//串行执行就是在一个线程上一条道走到黑
int x = 1 + 2 + 3 + ... + 10000;

```
==并行执行==就是把上边的累加操作先 ==拆分子任务== ，比如拆成 ``` (1 + 2 + ··· + 1000) ``` ``` (1001 + 1002 + 1003 + ··· + 2000) ``` ``` ···``` ```(9001 + 9002 +9003 + ··· + 10000)``` 这样的十个子任务，各个任务可以分散在不同的线程里执行，最后将这些任务的结果==汇总==起来，得出最终的结果。

遍历集合的操作可以选择并行执行或者是串行执行，默认是串行

```
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
int sum = list.stream().filter(x -> x%2==0)
                        .sum();
```

转换为并行流容易：
```
//将stream() 替换为parallelStream()
int sum = list.parallelStream().filter(x -> x%2==0)
                        .sum();
```

## Collector
把Stream转换成数据集合(List, Set, Map...你想转成啥就转成啥)这个特性很牛逼！

我们看看怎么转换(Stream的collect方法)：
先举个例子，
```
        List<Hero> newHeros = heros.stream().filter(x -> x.isMale())
        .collect(Collectors.toList());  //把男性英雄过滤到newHeros列表里

```

在
```
public interface Stream<T> extends BaseStream<T, Stream<T>> {
    
    //如果你看不懂泛型，😯，那就别看了，等以后专门整个泛型的文章瞅瞅
    //这里你只需要知道，调用collect方法时，需要一个Collector类型的参数
    //这个参数决定了这个Stream要生成一个什么样的集合，一般JDK已经给我们提供了很多工厂方法让我们生成不同的集合，往下看
    <R, A> R collect(Collector<? super T, A, R> collector);
}
```

1. Collectors.toList(): 将Stream转为List
```
List<Hero> newHeros = heros.stream().filter(x -> x.isMale())
        .collect(Collectors.toList());  //把男性英雄过滤到newHeros列表里
```

2. Collectors.toSet(): 将Stream转为Set
```
Set<String> newHeros = heros.stream().map(x -> x.getName())
        .collect(Collectors.toList());  //把英雄名字映射到newHeros Set里
```

2. Collectors.toMap(): 将Stream转为Map, 这个需要指定建立什么到什么的Map，所以zai ```Collectors.toMap``` fang发里需要指定规则 ---> 传入两个lambda表达式， 来指定映射什么东东
```
Map<String, String> name2postionMap = heros.stream()
        .collect(Collectors.toMap(x -> x.getName(), x -> x.getPostion()));  //把建立英雄名字到位置的Map

//不知道你忘了前面说过的方法引用没，上边的代码也可以写成：
Map<String, String> name2postionMap = heros.stream()
        .collect(Collectors.toMap(Hero::getName, Hero::getPostion));  //把建立英雄名字到位置的Map
        
//上面是建立对象的字段到字段的映射，如果是字段映射到对象自己呢？
Map<String, Hero> name2HeroMap = heros.stream()
        .collect(Collectors.toMap(Hero::getName, x -> x));  //lambda表达式直接返回自己

//或者可以写成：Function.identity() 的实现就是一个 x -> x 的lambda表达式
Map<String, Hero> name2HeroMap = heros.stream()
                .collect(Collectors.toMap(Hero::getName, Function.identity()));
```

3. Collectors.groupingBy(), 将Stream转化为一个字段到一个list的map
```
    Map<String, List<Hero>> postion2HeroListMap = heros.stream()
    .collect(Collectors.groupingBy(Hero::getPostion));
```

是不是很方便，原来我们要建立一个map要写好几行代码，现在统统一行搞定，是不是Stream只能按照上边3中方式生成集合呢？？of course not,结论是你想生成什么样，你就能生成什么样.

Collectors 类包含大量的方法，这些方法被用来创造各式各样的收集器，以便进行查询、列表（tabulation）和分组等工作，当然你也可以实现一个自定义 Collector。