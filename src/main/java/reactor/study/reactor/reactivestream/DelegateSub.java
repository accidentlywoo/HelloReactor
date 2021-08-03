package reactor.study.reactor.reactivestream;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class DelegateSub implements Subscriber<Integer> {
	private final Subscriber sub;

	public DelegateSub(Subscriber<? super Integer> sub) {
		this.sub = sub;
	}

	public void onSubscribe(Subscription s) {
		sub.onSubscribe(s);
	}

	public void onNext(Integer integer) {
		sub.onNext(integer);
	}

	public void onError(Throwable t) {
		sub.onError(t);
	}

	public void onComplete() {
		sub.onComplete();
	}
}
