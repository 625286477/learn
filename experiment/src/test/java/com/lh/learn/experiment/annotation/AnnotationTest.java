package com.lh.learn.experiment.annotation;

/**
 * 自定义注解测试
 *
 * @author liuhai on 2018/2/24.
 */
@ClassAnnotation("Class")
public class AnnotationTest {

    public static void main(String[] args) {
        ClassAnnotation t = AnnotationTest.class.getAnnotation(ClassAnnotation.class);
        System.out.println(t.value());
        MethodAnnotation tm;
        try {
            tm = AnnotationTest.class.getDeclaredMethod("test").getAnnotation(MethodAnnotation.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println(tm.value());
    }

    @MethodAnnotation("Method")
    private void test() {
        System.out.println("test");
    }
}