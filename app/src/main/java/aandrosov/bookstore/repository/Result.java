package aandrosov.bookstore.repository;

public abstract class Result {
    public static class Success extends Result {

        public Success(Object data) {
            this.data = data;
        }
    }

    public static class Error extends Result {

        public Error(Exception data) {
            this.data = data;
        }
    }

    protected Object data;

    public Object getData() {
        return data;
    }
}
