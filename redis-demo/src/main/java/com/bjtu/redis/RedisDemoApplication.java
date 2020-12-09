package com.bjtu.redis;

import com.sun.media.sound.UlawCodec;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  SpringBootApplication
 * 用于代替 @SpringBootConfiguration（@Configuration）、 @EnableAutoConfiguration 、 @ComponentScan。
 * <p>
 * SpringBootConfiguration（Configuration） 注明为IoC容器的配置类，基于java config
 * EnableAutoConfiguration 借助@Import的帮助，将所有符合自动配置条件的bean定义加载到IoC容器
 * ComponentScan 自动扫描并加载符合条件的组件
 */
@SpringBootApplication
public class RedisDemoApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RedisDemoApplication.class, args);
        Utils utils =new Utils();
        boolean isrun=true;
        System.out.println("................ My Redis Work ................");
        System.out.println( "Please Choose Your Operation:");
        System.out.println("\t 1.set \t 2.get \t 3.counter计数 \t 4.操作日志 \t");
        while (isrun){
            System.out.print("user>>>");
            Scanner scan = new Scanner(System.in);
            String input = scan.nextLine();
            if(!input.equals("1")&&!input.equals("2")&&!input.equals("3")&&!input.equals("4")){
                System.out.println("输入格式错误，请输入 1 或 2 或 3 或 4  ");
            }else{
                switch (input){
                    case "1":
                        set();
                        break;
                    case "2":
                        get();
                        break;
                    case "3":
                        counter();
                        break;
                    case "4":
                        Timemap();
                        break;
                }
            }
        }
    }

    public static void set() throws Exception {
        System.out.println( "Please Choose Data Type DemoTest:");
        System.out.println("1.String  2.Set  3.Hash  4.List  5.ZSet");
        System.out.print("Set>>>");
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        if(!input.equals("1")&&!input.equals("2")&&!input.equals("3")&&!input.equals("4")&&!input.equals("5")){
            System.out.println("输入格式错误，请输入 1 或 2 或 3 或 4 或 5 ");
        }else{
            switch (input){
                case "1":
                    System.out.print("Set>>>");
                    System.out.println(Utils.set("test_String_set","1"));
                    break;
                case "2":
                    Set<String> temp_set = new HashSet<>();
                    temp_set.add("1");
                    temp_set.add("2");
                    Utils.sadd("test_Set_set",temp_set);
                    System.out.println("Set>>>OK");
                    break;
                case "3":
                    System.out.print("Set>>>");
                    System.out.println(Utils.hset("test_Hash_set","first","1"));
                    break;
                case "4":
                    List<String> temp_list=new ArrayList<>();
                    temp_list.add("1");
                    temp_list.add("2");
                    Utils.lpush("test_List_set",temp_list);
                    System.out.println("Set>>>OK");
                    break;
                case "5":
                    System.out.print("Set>>>");
                    System.out.println(Utils.zadd("test_ZSet_set",0.01,"first"));
                    break;
            }
        }
    }

    public static void get() throws Exception{
        System.out.println( "Please Choose Data Type DemoTest:");
        System.out.println("String:1  Set:2  Hash:3  List:4  ZSet:5");
        System.out.print("Get>>>");
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        if(!input.equals("1")&&!input.equals("2")&&!input.equals("3")&&!input.equals("4")&&!input.equals("5")){
            System.out.println("输入格式错误，请输入 1 或 2 或 3 或 4 或 5 ");
        }else{
            switch (input){
                case "1":
                    Utils.set("test_String_set","1");
                    System.out.print("Get>>>");
                    System.out.println(Utils.get("test_String_set"));
                    break;
                case "2":
//                    Set<String> temp_set = new HashSet<>();
                    Set<String> temp_set_result=new HashSet<>();
//                    temp_set.add("1");
//                    temp_set.add("2");
//                    Utils.sadd("test_Set_set",temp_set);
                    System.out.print("Get>>>");
                    temp_set_result=Utils.smembers("test_Set_set");
                    Iterator<String> it = temp_set_result.iterator();
                    int i=0;
                    while(it.hasNext()){
                        String str = it.next();
                        if(i==0)
                            System.out.println(str);
                        else
                            System.out.println("      "+str);
                        i++;
                    }
                    break;
                case "3":
                    System.out.print("Get>>>");
                    Utils.hset("test_Hash_set","first","1");
                    System.out.println(Utils.hget("test_Hash_set","first"));
                    break;
                case "4":
//                    List<String> temp_list=new ArrayList<>();
                    List<String> temp_list_result=new ArrayList<>();
//                    temp_list.add("1");
//                    temp_list.add("2");
//                    Utils.lpush("test_List_set",temp_list);
                    temp_list_result=Utils.lrange("test_List_set",0,-1);
                    System.out.print("Get>>>");
                    int m=0;
                    for(int j=0;j<temp_list_result.size();j++){
                        String str=temp_list_result.get(j);
                        if(m==0)
                            System.out.println(str);
                        else
                            System.out.println("      "+str);
                        m++;
                    }
                    break;
                case "5":
                    System.out.print("Get>>>");
                    Utils.zadd("test_ZSet_set",0.01,"first");
                    System.out.println(Utils.zscore("test_ZSet_set","first"));
                    break;
            }
        }
    }

    public static void counter() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
        System.out.println( "Please Choose Strat And End Time(yyyy-MM-dd HH):");

        System.out.print("Counter>>>");
        System.out.print("Start Time:");
        Scanner scan = new Scanner(System.in);
        String start = scan.nextLine();
        System.out.print("Counter>>>");
        System.out.print("End  Time:");
        Scanner scan2 = new Scanner(System.in);
        String end = scan2.nextLine();
        Date from = format.parse(start);
        Date to = format.parse(end);

        System.out.println( "Please Choose Set or Get counter num");
        System.out.println("Set:1  Get:2 ");
        Scanner scan3 = new Scanner(System.in);
        String input = scan.nextLine();

        Vector<String> timemap =new Vector<>();
        if(!input.equals("1")&&!input.equals("2")){
            System.out.println("输入格式错误，请输入 1 或 2 ");
        }else{
            switch (input){
                case "1":
                    timemap=Utils.getcounter_time("set",from,to);
                    System.out.print("Counter>>>setnum:");
                    System.out.println(timemap.size());
                    break;
                case "2":
                    timemap=Utils.getcounter_time("gete",from,to);
                    System.out.print("Counter>>>getnum:");
                    System.out.println(timemap.size());
                    break;
            }
        }
    }

    public static void Timemap() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
        System.out.println( "Please Choose Strat And End Time(yyyy-MM-dd HH):");

        System.out.print("Counter>>>");
        System.out.print("Start Time:");
        Scanner scan = new Scanner(System.in);
        String start = scan.nextLine();
        System.out.print("Counter>>>");
        System.out.print("End  Time:");
        Scanner scan2 = new Scanner(System.in);
        String end = scan2.nextLine();
        Date from = format.parse(start);
        Date to = format.parse(end);

        System.out.println( "Please Choose Set or Get counter num");
        System.out.println("Set:1  Get:2 ");
        Scanner scan3 = new Scanner(System.in);
        String input = scan.nextLine();

        Vector<String> timemap =new Vector<>();
        if(!input.equals("1")&&!input.equals("2")){
            System.out.println("输入格式错误，请输入 1 或 2 ");
        }else{
            switch (input){
                case "1":
                    timemap=Utils.getcounter_time("set",from,to);
                    System.out.print("Counter>>>");
                    for(int i=0;i<timemap.size();i++){
                        if(i==0)
                            System.out.println(timemap.get(i));
                        else
                            System.out.println("          "+timemap.get(i));
                    }
                    break;
                case "2":
                    timemap=Utils.getcounter_time("gete",from,to);
                    System.out.print("Counter>>>");
                    for(int i=0;i<timemap.size();i++){
                        if(i==0)
                            System.out.println(timemap.get(i));
                        else
                            System.out.println("          "+timemap.get(i));
                    }
                    break;
            }
        }
    }

}

/*

总结：

1、获取运行环境信息和回调接口。例如ApplicationContextIntializer、ApplicationListener。
完成后，通知所有SpringApplicationRunListener执行started()。

2、创建并准备Environment。
完成后，通知所有SpringApplicationRunListener执行environmentPrepared()

3、创建并初始化 ApplicationContext 。例如，设置 Environment、加载配置等
完成后，通知所有SpringApplicationRunListener执行contextPrepared()、contextLoaded()

4、执行 ApplicationContext 的 refresh，完成程序启动
完成后，遍历执行 CommanadLineRunner、通知SpringApplicationRunListener 执行 finished()

参考：
https://blog.csdn.net/zxzzxzzxz123/article/details/69941910
https://www.cnblogs.com/shamo89/p/8184960.html
https://www.cnblogs.com/trgl/p/7353782.html

分析：

1） 创建一个SpringApplication对象实例，然后调用这个创建好的SpringApplication的实例方法

public static ConfigurableApplicationContext run(Object source, String... args)

public static ConfigurableApplicationContext run(Object[] sources, String[] args)

2） SpringApplication实例初始化完成并且完成设置后，就开始执行run方法的逻辑了，
方法执行伊始，首先遍历执行所有通过SpringFactoriesLoader可以查找到并加载的
SpringApplicationRunListener，调用它们的started()方法。


public SpringApplication(Object... sources)

private final Set<Object> sources = new LinkedHashSet<Object>();

private Banner.Mode bannerMode = Banner.Mode.CONSOLE;

...

private void initialize(Object[] sources)

3） 创建并配置当前SpringBoot应用将要使用的Environment（包括配置要使用的PropertySource以及Profile）。

private boolean deduceWebEnvironment()

4） 遍历调用所有SpringApplicationRunListener的environmentPrepared()的方法，通知Environment准备完毕。

5） 如果SpringApplication的showBanner属性被设置为true，则打印banner。

6） 根据用户是否明确设置了applicationContextClass类型以及初始化阶段的推断结果，
决定该为当前SpringBoot应用创建什么类型的ApplicationContext并创建完成，
然后根据条件决定是否添加ShutdownHook，决定是否使用自定义的BeanNameGenerator，
决定是否使用自定义的ResourceLoader，当然，最重要的，
将之前准备好的Environment设置给创建好的ApplicationContext使用。

7） ApplicationContext创建好之后，SpringApplication会再次借助Spring-FactoriesLoader，
查找并加载classpath中所有可用的ApplicationContext-Initializer，
然后遍历调用这些ApplicationContextInitializer的initialize（applicationContext）方法
来对已经创建好的ApplicationContext进行进一步的处理。

8） 遍历调用所有SpringApplicationRunListener的contextPrepared()方法。

9） 最核心的一步，将之前通过@EnableAutoConfiguration获取的所有配置以及其他形式的
IoC容器配置加载到已经准备完毕的ApplicationContext。

10） 遍历调用所有SpringApplicationRunListener的contextLoaded()方法。

11） 调用ApplicationContext的refresh()方法，完成IoC容器可用的最后一道工序。

12） 查找当前ApplicationContext中是否注册有CommandLineRunner，如果有，则遍历执行它们。

13） 正常情况下，遍历执行SpringApplicationRunListener的finished()方法、
（如果整个过程出现异常，则依然调用所有SpringApplicationRunListener的finished()方法，
只不过这种情况下会将异常信息一并传入处理）


private <T> Collection<? extends T> getSpringFactoriesInstances(Class<T> type)

private <T> Collection<? extends T> getSpringFactoriesInstances(Class<T> type,
			Class<?>[] parameterTypes, Object... args)

public void setInitializers

private Class<?> deduceMainApplicationClass()

public ConfigurableApplicationContext run(String... args)

private void configureHeadlessProperty()

private SpringApplicationRunListeners getRunListeners(String[] args)

public static List<String> loadFactoryNames(Class<?> factoryClass, ClassLoader classLoader)


*/
