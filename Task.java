import java.util.concurrent.Callable;

public class Task<T> {
    private final Callable<? extends T> callable;
    private volatile T result = null;
    private volatile MyRunTimeException exception = null;

    public Task(Callable<? extends T> callable) {
        this.callable = callable;
    }

    private T check() {
        if (result != null) {
            return result;
        } else if (exception != null) {
            throw exception;
        }
        return null;
    }

    public T get() {
        T firstCheck = check();
        if (firstCheck == null) {
            synchronized (this) {
                T secondCheck = check();
                if (secondCheck == null) {
                    try {
                        result = callable.call();
                        return result;
                    } catch (Exception e) {
                        exception = new MyRunTimeException("MyRunTimeException: callabale.call()");
                        throw exception;
                    }
                }
                return secondCheck;
            }
        }
        return firstCheck;
    }


}

class MyRunTimeException extends RuntimeException {
    MyRunTimeException(String message) {
        super(message);
    }

}