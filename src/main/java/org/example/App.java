package org.example;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.pool.TypePool;
import org.example.magic.Moja;

import java.io.File;
import java.io.IOException;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        ClassLoader classLoader = App.class.getClassLoader();
        System.out.println(classLoader);
        System.out.println(classLoader.getParent());

        // PlatformClassLoader의 parent 객체는 native 코드로 되어있어서 null로 print 된다.
        System.out.println(classLoader.getParent().getParent());

        // native method : C나 C++로 작성된 코드 이다.
        // native method 라이브러리는 JNI(Java Native Interface)를 통해서 사용가능하며
        // 해당 쓰레드에 대해서 JVM 메모리 중 native method stack에 method call이 쌓인다.
        Thread.currentThread();

        TypePool typePool = TypePool.Default.of(classLoader);

        // ByteBuddy를 이용해서 Moja 클래스를 조작해보자 (바이트코드 조작)
        // target/classes 에 Moja.class가 조작되어서 생성 될 것이다.
        // 조작된 Moja와 원래 Moja는 JVM에서 다른 객체로 인식한다. (풀패키지명 포함된 클래스명이 같아도...)
        //new ByteBuddy().redefine(Moja.class)

        // 이렇게 하면 실행하면서 Byte 코드를 직접 조작하고, 반영되므로 Rabbit이 출력된다.
        new ByteBuddy().redefine(typePool.describe("org.example.magic.Moja").resolve(), ClassFileLocator.ForClassLoader.of(classLoader))
                .method(named("pullOut")).intercept(FixedValue.value("Rabiit!"))
                .make().saveIn(new File("D:\\work\\workspace\\IdeaProjects\\jvminternal\\target\\classes"));

        // ""을 리턴하는 모자에서 "Rabbit"을 꺼내보자 (바이트코드를 조작)
        // 우선 위의 소스(ByteBuddy)를 먼저 실행시켜서 target class에 생성하고
        // 그 다음에 아래 출력 메소드를 실행시키면 확인 가능하다.
        // 이미 본 클래스의 코드를 실행할 때는 원래의 Moja.class 파일을 읽었기 떄문이다. (이미 읽었기 떄문에 다시 Class Loading을 하지도 않는다.)
        //System.out.println(new Moja().pullOut());
        // BUT!! 먼저 Byte 조작코드를 먼저 돌리고 나서 One-Stop으로 new Moja().pullOut()에서 Rabbit이 나올 수 있게 가능하다.

        System.out.println(new Moja().pullOut());
    }
}
