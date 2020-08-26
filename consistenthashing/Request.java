package consistenthashing;

public class Request {
    private String id;
    private String method;

    public Request(String id,String method) {
        this.id = id;
        this.method = method;
    }

    public String getId() {
        return id;
    }

    public String getMethod() {
        return method;
    }
}
