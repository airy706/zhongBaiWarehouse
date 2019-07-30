public class test {
    public static void main(String[] args) {
        String a="hello2";
        final String b="hello";
        String d="hello";
        String c=b+"2";
        String e=d+"2";
        System.out.println(a==c);
        System.out.println(a==e);
        System.out.println(c==e);
        System.out.println(a.equals(c));
        System.out.println(a.equals(e));
        System.out.println(e.equals(c));
        System.out.println(a.hashCode());
        System.out.println(c.hashCode());
        System.out.println(e.hashCode());
        Object o=new Object();
    }
}
