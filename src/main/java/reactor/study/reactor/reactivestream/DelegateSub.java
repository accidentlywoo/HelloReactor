package reactor.study.reactor.reactivestream;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class DelegateSub<T, R> implements Subscriber<T> {
	private final Subscriber sub;

	public DelegateSub(Subscriber<? super R> sub) {
		this.sub = sub;
	}

	public void onSubscribe(Subscription s) {
		sub.onSubscribe(s);
	}

	public void onNext(T integer) {
		sub.onNext(integer);
	}

	public void onError(Throwable t) {
		sub.onError(t);
	}

	public void onComplete() {
		sub.onComplete();
	}
}
