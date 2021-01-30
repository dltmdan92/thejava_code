package org.example;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ClassLoader classLoader = App.class.getClassLoader();
        System.out.println(classLoader);
        System.out.println(classLoader.getParent());

        // PlatformClassLoader의 parent 객체는 native 코드로 되어있어서 null로 print 된다.
        System.out.println(classLoader.getParent().getParent());

        // native method : C나 C++로 작성된 코드 이다.
        // native method 라이브러리는 JNI(Java Native Interface)를 통해서 사용가능하며
        // 해당 쓰레드에 대해서 JVM 메모리 중 native method stack에 method call이 쌓인다.
        Thread.currentThread();
    }
}
